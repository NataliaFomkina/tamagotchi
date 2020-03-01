package org.example.ui;
import org.example.backend.Coord;
import org.example.backend.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SwingFrame extends JFrame {
    public static Game game;
    static FeedButton feedbutton;
    static BathButton bathButton;
    static PlayButton playButton;
    static SleepButton sleepButton;
public int x;
public int y;
   public static JPanel patLive;
    public SwingFrame(){
        super("Tamagotchi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,400);
        //панель с вертикальным блочным расположением, поместим в нее все панели
        JPanel main = BoxLayoutUtils.createVerticalPanel();
        JPanel upperButtons = BoxLayoutUtils.createHorizontalPanel();
        JPanel bottomButtons = BoxLayoutUtils.createHorizontalPanel();
        patLive = new JPanel(){
           @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
               for(Map.Entry<Coord, String> obj:game.screenImages.entrySet())
             g.drawImage(getImage(obj.getValue()),obj.getKey().x, obj.getKey().y,obj.getKey().width,obj.getKey().height,this);
          }
        };
        patLive.setLayout(null);//new FlowLayout());
        feedbutton = new FeedButton();
        bathButton = new BathButton();
        upperButtons.add(feedbutton);
        upperButtons.add(bathButton);
        sleepButton = new SleepButton();
        playButton = new PlayButton();
        bottomButtons.add(sleepButton);
        bottomButtons.add(playButton);
        ImageIcon icon1 = new ImageIcon(getImage("/pictures/plate.png"));
        feedbutton.setIcon(icon1);
        ImageIcon icon2 = new ImageIcon(getImage("/pictures/play.png"));
        playButton.setIcon(icon2);
        ImageIcon icon3 = new ImageIcon(getImage("/pictures/sleep.png"));
        sleepButton.setIcon(icon3);
        ImageIcon icon4 = new ImageIcon(getImage("/pictures/bath.png"));
        bathButton.setIcon(icon4);
        bathButton.addActionListener((e -> {
            if(game.pet!=null) {
                if (game.pet.bath()) {
                    game.screenImages.put(game.showerCoord, "/pictures/shower.png");
                    patLive.repaint();
                    int delay = 2500; //milliseconds
                    ActionListener taskPerformer = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            // screenImages.clear();
                            game.screenImages.remove(game.showerCoord);
                            game.updateHygieneScale();
                            patLive.repaint();
                        }
                    };
                    Timer t = new Timer(delay, taskPerformer);
                    t.setRepeats(false);
                    t.start();
                }
            }
        } ));
        sleepButton.addActionListener((e -> {
            if(game.pet!=null) {
                if (game.pet.sleep()) {
                    game.screenImages.remove(game.pet.petCoord);
                    game.screenImages.put(game.pet.petCoord, game.pet.sleepPicture);
                    patLive.repaint();
                  try{
                       Thread.sleep(6000);
                    }catch(Exception ex){
                   }
                   game.screenImages.remove(game.pet.petCoord);
                    game.updateEnergyScale();
                    patLive.repaint();
                }
            }
        } ));
        feedbutton.addActionListener((e -> {
            if(game.pet!=null) {
                if (game.pet.eat()){
                    int delay = 1000; //milliseconds
                ActionListener eatPerformer = new ActionListener() {
                    int i = 0;
                        public void actionPerformed (ActionEvent evt){
                            if(i < 3){
                        game.screenImages.put(game.foodCoord, game.pet.eatPictures[i]);//
                        patLive.repaint();
                        i++;
                    }else if (i==3){
                               game.screenImages.remove(game.foodCoord);
                                game.updateSatietyScale();////////
                               patLive.repaint();
                                ( ( Timer ) evt.getSource() ).stop();
                               i = 0;
                            }
                    }
                };
                Timer eat = new Timer(delay, eatPerformer);
                eat.start();
               // patLive.repaint();
            }}
            } ));
        playButton.addActionListener((e -> {
            if(game.pet!=null) {
                if(game.pet.play()) {
                    int delay = 700; //milliseconds
                    ActionListener playPerformer = new ActionListener() {
                        int i = 1;
                        public void actionPerformed (ActionEvent et){
                             game.screenImages.remove(game.pet.playCoordItem[i-1]);
                            game.screenImages.remove(game.pet.playCoordPet[i-1]);
                          //
                            if(i < 5){
                                game.screenImages.put(game.pet.playCoordPet[i], game.pet.petPicture);//
                                game.screenImages.put(game.pet.playCoordItem[i], game.pet.itemPicture);
                                patLive.repaint();
                                i++;
                            }else if (i==5){
                                game.screenImages.put(game.pet.playCoordPet[0], game.pet.petPicture);
                                game.updateFunScale();////////
                                patLive.repaint();
                                ( ( Timer ) et.getSource() ).stop();
                                i = 1;
                            }
                        }
                    };
                    Timer play = new Timer(delay, playPerformer);
                    play.start();
                }
            }
        } ));
        patLive.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        patLive.setBackground(Color.WHITE);
        //панели для отображения состояния питомца
        JPanel hungry = BoxLayoutUtils.createHorizontalPanel();
        JPanel bath = BoxLayoutUtils.createHorizontalPanel();
        JPanel sleep = BoxLayoutUtils.createHorizontalPanel();
        JPanel play = BoxLayoutUtils.createHorizontalPanel();
        patLive.add(hungry);
        patLive.add(bath);
        patLive.add(play);
        patLive.add(sleep);
        //
        main.add(upperButtons);
        main.add(patLive);
        main.add(bottomButtons);
        getContentPane().add(main);
        setVisible(true);
        setResizable(false);
        game = new Game();
    }
    private static SwingFrame swf;
public Image getImage(String name){
    ImageIcon icon = new ImageIcon(getClass().getResource(name));
    return icon.getImage();
}
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable(){
            public void run()  {
                swf = new SwingFrame();
            }
        });
    }
}
