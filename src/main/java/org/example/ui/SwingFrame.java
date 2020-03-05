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
    private static JButton feedbutton;
    private static JButton bathButton;
    private static JButton playButton;
    private static JButton sleepButton;
    public static JPanel patLive;

    public SwingFrame() {
        super("Tamagotchi");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {//сохраняем в файл
                if (game.getPet() != null) {
                    game.getPet().lastLaunchTime = System.currentTimeMillis();
                    game.getPet().updateState();
                    Gson gson = new Gson();
                    String petString = gson.toJson(game.getPet());
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
                super.paintComponent(g);
                for (Map.Entry<Coord, String> obj : game.screenImages.entrySet()) {
                    g.drawImage(getImage(obj.getValue()), obj.getKey().x, obj.getKey().y, obj.getKey().width, obj.getKey().height, this);
                }
            }
        };
        createButtons();
        bottomButtons.add(sleepButton);
        bottomButtons.add(playButton);
        upperButtons.add(feedbutton);
        upperButtons.add(bathButton);
        addActionListenersToButtons();
        patLive.setLayout(null);
        patLive.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        patLive.setBackground(Color.WHITE);
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

    private void createButtons() {
        feedbutton = new JButton();
        bathButton = new JButton();
        sleepButton = new JButton();
        playButton = new JButton();
        ImageIcon icon1 = new ImageIcon(getImage("/pictures/plate.png"));
        feedbutton.setIcon(icon1);
        ImageIcon icon2 = new ImageIcon(getImage("/pictures/play.png"));
        playButton.setIcon(icon2);
        ImageIcon icon3 = new ImageIcon(getImage("/pictures/sleep.png"));
        sleepButton.setIcon(icon3);
        ImageIcon icon4 = new ImageIcon(getImage("/pictures/bath.png"));
        bathButton.setIcon(icon4);
    }

    private void addActionListenersToButtons() {
        bathButton.addActionListener((e -> {
            if (game.getPet() != null) {
                if (game.getPet().bath()) {
                    game.getScreenImages().put(game.showerCoord, "/pictures/shower.png");
                    patLive.repaint();
                    int delay = 2500; //milliseconds
                    disableButtons();
                    ActionListener taskPerformer = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            enableButtons();
                            game.getScreenImages().remove(game.showerCoord);
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
            if (game.getPet() != null) {
                if (game.getPet().getEnergy() != 10) {
                    disableButtons();
                    game.getScreenImages().put(game.getPet().petCoord, game.getPet().sleepPicture);
                    patLive.repaint();
                    int delay = 2500; //milliseconds
                    ActionListener taskPerformer = new ActionListener() {
                        int i = game.getPet().getEnergy();

                        public void actionPerformed(ActionEvent evt) {
                            if (i < 10) {
                                game.getPet().sleep();
                                game.updateEnergyScale();
                                patLive.repaint();
                                i++;
                            } else {
                                game.getPet().updateState();
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
            if (game.getPet() != null && game.getPet().eat()) {
                int delay = 1000; //milliseconds
                disableButtons();
                ActionListener eatPerformer = new ActionListener() {
                    int i = 0;

                    public void actionPerformed(ActionEvent evt) {
                        if (i < 3) {
                            game.getScreenImages().put(game.foodCoord, game.getPet().eatPictures[i]);//
                            patLive.repaint();
                            i++;
                        } else if (i == 3) {
                            game.getScreenImages().remove(game.foodCoord);
                            game.updateSatietyScale();
                            patLive.repaint();
                            enableButtons();
                            ((Timer) evt.getSource()).stop();
                        }
                    }
                };
                Timer eat = new Timer(delay, eatPerformer);
                eat.start();
            }
        }));
        playButton.addActionListener((e -> {
            if (game.getPet() != null && game.getPet().play()) {
                int delay = 700; //milliseconds
                disableButtons();
                ActionListener playPerformer = new ActionListener() {
                    int i = 1;

                    public void actionPerformed(ActionEvent et) {
                        game.getScreenImages().remove(game.getPet().petCoord);
                        game.getScreenImages().remove(game.getPet().playCoordItem[i - 1]);
                        game.getScreenImages().remove(game.getPet().playCoordPet[i - 1]);
                        if (i < 5) {
                            game.getScreenImages().put(game.getPet().playCoordPet[i], game.getPet().petPicture);//
                            game.getScreenImages().put(game.getPet().playCoordItem[i], game.getPet().itemPicture);
                            patLive.repaint();
                            i++;
                        } else if (i == 5) {
                            game.getScreenImages().put(game.getPet().playCoordPet[0], game.getPet().petPicture);
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
        }));
    }

    private static SwingFrame swf;

    public Image getImage(String name) {

        ImageIcon icon = new ImageIcon(getClass().getResource(name));
        return icon.getImage();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                swf = new SwingFrame();
            }
        });
    }
}
