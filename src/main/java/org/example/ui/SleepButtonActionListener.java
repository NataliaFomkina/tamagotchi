//package org.example.ui;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class SleepButtonActionListener implements ActionListener {
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (game.pet != null) {
//            if (game.pet.getEnergy() != 10) {
//                disableButtons();
//                game.screenImages.put(game.pet.petCoord, game.pet.sleepPicture);
//                patLive.repaint();
//                int delay = 2500; //milliseconds
//                ActionListener taskPerformer = new ActionListener() {
//                    int i = game.pet.getEnergy();
//                    public void actionPerformed(ActionEvent evt) {
//                        if (i < 10) {
//                            game.pet.sleep();
//                            game.updateEnergyScale();
//                            patLive.repaint();
//                            i++;
//                        } else {
//                            game.pet.updateState();
//                            game.updateStatePicture();
//                            patLive.repaint();
//                            enableButtons();
//                            ((Timer) evt.getSource()).stop();
//                        }
//                    }
//                };
//                Timer t = new Timer(delay, taskPerformer);
//                t.setInitialDelay(0);
//                t.start();
//            }
//        }
//    }
//}
