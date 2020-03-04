package org.example.ui;

import com.google.gson.Gson;
import org.example.backend.Coord;
import org.example.backend.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SwingFrame extends JFrame {
    public static Game game;
    static JButton feedbutton;
    static JButton bathButton;
    static JButton playButton;
    static JButton sleepButton;
    public static JPanel patLive;

    public SwingFrame() {
        super("Tamagotchi");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {//сохраняем в файл
                if(game.pet!=null) {
                    game.pet.lastLaunchTime = System.currentTimeMillis();
                    game.pet.updateState();/////////////
                    Gson gson = new Gson();
                    String petString = gson.toJson(game.pet);
                    System.out.println(petString);
                    try (FileWriter writer = new FileWriter("petFile")) {
                        writer.write(petString);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
        setSize(300, 400);
        //панель с вертикальным блочным расположением, поместим в нее все панели
        JPanel main = BoxLayoutUtils.createVerticalPanel();
        JPanel upperButtons = BoxLayoutUtils.createHorizontalPanel();
        JPanel bottomButtons = BoxLayoutUtils.createHorizontalPanel();

        patLive = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
               if(game!=null){
                if (!game.screenImages.isEmpty()){//getValue вместо get
                    for ( Map.Entry<Coord, String> obj : game.screenImages.entrySet()){////////////////
                    if(obj.getValue()!=null&&!obj.getValue().equals("")) {ImageIcon icon = new ImageIcon(getClass().getResource(obj.getValue()));

                       g.drawImage(icon.getImage(), obj.getKey().x, obj.getKey().y, obj.getKey().width, obj.getKey().height, this);
                        //g.drawImage(getImage(obj.getValue()), obj.getKey().x, obj.getKey().y, obj.getKey().width, obj.getKey().height, this);
            }}}}
            }
        };
        patLive.setLayout(null);
        feedbutton = new JButton();
        bathButton = new JButton();
        upperButtons.add(feedbutton);
        upperButtons.add(bathButton);
        sleepButton = new JButton();
        playButton = new JButton();
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
            if (game.pet != null) {
                if (game.pet.bath()) {
                    game.screenImages.put(game.showerCoord, "/pictures/shower.png");
                    patLive.repaint();
                    int delay = 2500; //milliseconds
                    Utils.disableButtons(bathButton,playButton,feedbutton,sleepButton);
                    disableButtons();
                    ActionListener taskPerformer = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            enableButtons();
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
        }));
        sleepButton.addActionListener((e -> {
            if (game.pet != null) {
                if (game.pet.getEnergy() != 10) {
                    disableButtons();
                    game.screenImages.put(game.pet.petCoord, game.pet.sleepPicture);
                    patLive.repaint();
                    int delay = 2500; //milliseconds
                    ActionListener taskPerformer = new ActionListener() {
                        int i = game.pet.getEnergy();
                        public void actionPerformed(ActionEvent evt) {
                            if (i < 10) {
                                game.pet.sleep();
                                game.updateEnergyScale();
                                patLive.repaint();
                                i++;
                            } else {
                                game.pet.updateState();
                                game.updateStatePicture();
                                patLive.repaint();
                                enableButtons();
                                ((Timer) evt.getSource()).stop();
                            }
                        }
                    };
                    Timer t = new Timer(delay, taskPerformer);
                    t.setInitialDelay(0);
                    t.start();
                }
            }
        }));
        feedbutton.addActionListener((e -> {
            if (game.pet != null) {
                if (game.pet.eat()) {
                    int delay = 1000; //milliseconds
                    disableButtons();
                    ActionListener eatPerformer = new ActionListener() {
                        int i = 0;
                        public void actionPerformed(ActionEvent evt) {
                            if (i < 3) {
                                game.screenImages.put(game.foodCoord, game.pet.eatPictures[i]);//
                                patLive.repaint();
                                i++;
                            } else if (i == 3) {
                                game.screenImages.remove(game.foodCoord);
                                game.updateSatietyScale();////////
                                patLive.repaint();
                                enableButtons();
                                ((Timer) evt.getSource()).stop();
                            }
                        }
                    };
                    Timer eat = new Timer(delay, eatPerformer);
                    eat.start();
                }
            }
        }));
        playButton.addActionListener((e -> {
            if (game.pet != null) {
                if (game.pet.play()) {
                    int delay = 700; //milliseconds
                    disableButtons();
                    ActionListener playPerformer = new ActionListener() {
                        int i = 1;
                        public void actionPerformed(ActionEvent et) {
                            game.screenImages.remove(game.pet.petCoord);
                            game.screenImages.remove(game.pet.playCoordItem[i - 1]);
                            game.screenImages.remove(game.pet.playCoordPet[i - 1]);
                            if (i < 5) {
                                game.screenImages.put(game.pet.playCoordPet[i], game.pet.petPicture);//
                                game.screenImages.put(game.pet.playCoordItem[i], game.pet.itemPicture);
                                patLive.repaint();
                                i++;
                            } else if (i == 5) {
                               // game.screenImages.remove(game.pet.petCoord);
                                game.screenImages.put(game.pet.playCoordPet[0], game.pet.petPicture);
                                game.updateFunScale();
                                enableButtons();
                                patLive.repaint();
                                ((Timer) et.getSource()).stop();
                            }
                        }
                    };
                    Timer play = new Timer(delay, playPerformer);
                    play.start();
                }
            }
        }));
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

    public static void enableButtons() {
        bathButton.setEnabled(true);
        playButton.setEnabled(true);
        feedbutton.setEnabled(true);
        sleepButton.setEnabled(true);
    }

    public static void disableButtons() {
        bathButton.setEnabled(false);
        playButton.setEnabled(false);
        feedbutton.setEnabled(false);
        sleepButton.setEnabled(false);
    }

    private static SwingFrame swf;

    public Image getImage(String name) {
//        if(name==null) {
//            System.out.println("00000000");
//            name = "/pictures/rip.png";
//        }
        ImageIcon icon = new ImageIcon(getClass().getResource(name));
        return icon.getImage();
    }

    public static void main(String[] args) throws Exception {
            SwingUtilities.invokeLater(new Runnable() {
                   public void run() {
                            swf = new SwingFrame();}
            });
    }
}
