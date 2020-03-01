package org.example.backend;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static org.example.ui.SwingFrame.*;

public class Game {
    private int lastLaunchTime;//время последнего запуска игры
    private int currentLaunchTime;//
    public int mouseClickX;
    public int mouseClickY;
    public Pet pet;
    public Map<Coord, String> screenImages;
    //координаты расположения
    private static final Coord scaleS = new Coord(18, 5, 110, 16);
    private static final Coord scaleH = new Coord(161, 5, 110, 16);
    private static final Coord scaleE = new Coord(18, 225, 110, 16);
    private static final Coord scaleF = new Coord(161, 225, 110, 16);
    private static final Coord backGround = new Coord(0, 0, 300, 250);
    public static final Coord foodCoord = new Coord(18, 157, 60, 60);
    public static final Coord showerCoord = new Coord(103, 25, 60, 60);

    public Game() {
        screenImages = new HashMap<Coord, String>();
        startGame();
    }

    MouseListener ml = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {//создание персонажей
            mouseClickX = e.getX();
            mouseClickY = e.getY();
            System.out.println(mouseClickX);
            System.out.println(mouseClickY);
            if (e.getButton() == MouseEvent.BUTTON1) {
                if ((mouseClickX <= 134 && mouseClickX >= 89 && mouseClickY <= 151 && mouseClickY >= 101) || (mouseClickX <= 189 && mouseClickX >= 136 && mouseClickY <= 190 && mouseClickY >= 152) || (mouseClickX <= 230 && mouseClickX >= 190 && mouseClickY <= 153 && mouseClickY >= 103)) {//если координаты соответствуют картинкам, закрываем слушателя
                    patLive.removeMouseListener(ml);
                    //располагаем шкалы
                    if ((mouseClickX <= 134 && mouseClickX >= 89 && mouseClickY <= 151 && mouseClickY >= 101)) {
                        pet = new Penguin();
                        screenImages.clear();
                        screenImages.put(pet.petCoord, "/pictures/p1.png");
                    } else if ((mouseClickX <= 189 && mouseClickX >= 136 && mouseClickY <= 190 && mouseClickY >= 152)) {
                        pet = new Man();
                        screenImages.clear();
                        screenImages.put(pet.petCoord, "/pictures/m3.png");
                    } else {
                        pet = new Yellow();
                        screenImages.clear();
                        screenImages.put(pet.petCoord, "/pictures/y3.png");
                    }
                }
                updateSatietyScale();
                updateEnergyScale();
                updateFunScale();
                updateHygieneScale();
            }
            patLive.repaint();
        }
    };

    public void updateSatietyScale() {
        if (pet != null) screenImages.put(scaleS, pet.newScales(pet.getSatiety()));
        updateStatePicture();
    }

    public void updateHygieneScale() {
        if (pet != null) screenImages.put(scaleH, pet.newScales(pet.getHygiene()));
        updateStatePicture();
    }

    public void updateFunScale() {
        if (pet != null) screenImages.put(scaleF, pet.newScales(pet.getFun()));
        updateStatePicture();
    }

    public void updateEnergyScale() {
        if (pet != null) {
            screenImages.put(scaleE, pet.newScales(pet.getEnergy()));

            updateStatePicture();
        }
    }

    public void updateStatePicture() {
        if (pet.getState() == Pet.States.HAPPY) {
            if (pet instanceof Penguin) screenImages.put(pet.petCoord, "/pictures/p1.png");
            if (pet instanceof Man) screenImages.put(pet.petCoord, "/pictures/m3.png");
            if (pet instanceof Yellow) screenImages.put(pet.petCoord, "/pictures/y3.png");
        } else if (pet.getState() == Pet.States.USUAL) {
            if (pet instanceof Penguin) screenImages.put(pet.petCoord, "/pictures/p3.png");
            if (pet instanceof Man) screenImages.put(pet.petCoord, "/pictures/m2.png");
            if (pet instanceof Yellow) screenImages.put(pet.petCoord, "/pictures/y1.png");
        } else if (pet.getState() == Pet.States.SAD) {
            if (pet instanceof Penguin) screenImages.put(pet.petCoord, "/pictures/p8.png");
            if (pet instanceof Man) screenImages.put(pet.petCoord, "/pictures/m1.png");
            if (pet instanceof Yellow) screenImages.put(pet.petCoord, "/pictures/y2.png");
        }
    }

    public void startGame() {
        if (pet == null) {
            int delay = 2000; //milliseconds
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    // screenImages.clear();
                    screenImages.put(backGround, "/pictures/choose.png");
                    patLive.repaint();
                    patLive.addMouseListener(ml);
                }
            };

            Timer t = new Timer(delay, taskPerformer);
            t.setRepeats(false);
            t.start();

            screenImages.put(backGround, "/pictures/start.png");
        } else {
            //updateSatietyScale();
        }
    }
}