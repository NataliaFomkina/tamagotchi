package org.example.backend;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.ui.SwingFrame.*;

public class Game {
    private long currentLaunchTime;//время запуска игры
    private int mouseClickX;
    private int mouseClickY;
    private Pet pet;
    public Map<Coord, String> screenImages;
    //координаты расположения
    private static final Coord scaleS = new Coord(18, 5, 110, 16);
    private static final Coord scaleH = new Coord(161, 5, 110, 16);
    private static final Coord scaleE = new Coord(18, 225, 110, 16);
    private static final Coord scaleF = new Coord(161, 225, 110, 16);
    private static final Coord backGround = new Coord(0, 0, 300, 250);
    public static final Coord foodCoord = new Coord(18, 157, 60, 60);
    public static final Coord showerCoord = new Coord(103, 25, 60, 60);
    private final static String FILE_NAME = "petFile";

    public Game() {
        Gson gson = new Gson();
        screenImages = new HashMap<Coord, String>();
        try {
            createPetFileIfNotExist();
            BufferedReader fileReader = new BufferedReader(new FileReader(FILE_NAME));
            String collect = fileReader.lines().collect(Collectors.joining());
            if (collect != null) {
                pet = gson.fromJson(collect, Pet.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        startGame();
    }

    public Pet getPet() {
        return pet;
    }

    public Map<Coord, String> getScreenImages() {
        return screenImages;
    }

    private void createPetFileIfNotExist() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    MouseListener ml = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {//создание персонажей
            mouseClickX = e.getX();
            mouseClickY = e.getY();
            if (e.getButton() == MouseEvent.BUTTON1) {
                if ((mouseClickX <= 134 && mouseClickX >= 89 && mouseClickY <= 151 && mouseClickY >= 101) || (mouseClickX <= 189 && mouseClickX >= 136 && mouseClickY <= 190 && mouseClickY >= 152) || (mouseClickX <= 230 && mouseClickX >= 190 && mouseClickY <= 153 && mouseClickY >= 103)) {//если координаты соответствуют картинкам, закрываем слушателя
                    patLive.removeMouseListener(ml);
                    userChosePet();
                    updateSatietyScale();
                    updateEnergyScale();
                    updateFunScale();
                    updateHygieneScale();
                    patLive.repaint();
                }

            }
        }
    };

    private void userChosePet() {
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

    public void updateSatietyScale() {
        if (pet != null) {
            screenImages.put(scaleS, pet.newScales(pet.getSatiety()));
        }
        updateStatePicture();
    }

    public void updateHygieneScale() {
        if (pet != null) {
            screenImages.put(scaleH, pet.newScales(pet.getHygiene()));
        }
        updateStatePicture();
    }

    public void updateFunScale() {
        if (pet != null) {
            screenImages.put(scaleF, pet.newScales(pet.getFun()));
        }
        updateStatePicture();
    }

    public void updateEnergyScale() {
        if (pet != null) {
            screenImages.put(scaleE, pet.newScales(pet.getEnergy()));
        }
    }

    public void updateStatePicture() {
        if (pet.getState() == Pet.States.HAPPY) {
            if (pet.petPicture.equals("/pictures/p1.png")) screenImages.put(pet.petCoord, "/pictures/p1.png");
            if (pet.petPicture.equals("/pictures/m3.png")) screenImages.put(pet.petCoord, "/pictures/m3.png");
            if (pet.petPicture.equals("/pictures/y3.png")) screenImages.put(pet.petCoord, "/pictures/y3.png");
        } else if (pet.getState() == Pet.States.USUAL) {
            if (pet.petPicture.equals("/pictures/p1.png")) screenImages.put(pet.petCoord, "/pictures/p3.png");
            if (pet.petPicture.equals("/pictures/m3.png")) screenImages.put(pet.petCoord, "/pictures/m2.png");
            if (pet.petPicture.equals("/pictures/y3.png")) screenImages.put(pet.petCoord, "/pictures/y1.png");
        } else if (pet.getState() == Pet.States.SAD) {
            if (pet.petPicture.equals("/pictures/p1.png")) screenImages.put(pet.petCoord, "/pictures/p8.png");
            if (pet.petPicture.equals("/pictures/m3.png")) screenImages.put(pet.petCoord, "/pictures/m1.png");
            if (pet.petPicture.equals("/pictures/y3.png")) screenImages.put(pet.petCoord, "/pictures/y2.png");
        } else if (pet.getState() == Pet.States.DEAD) {
            screenImages.put(pet.petCoord, "/pictures/rip1.gif");
            petDying();
        }
    }

    private void petDying() {
        screenImages.clear();
        screenImages.put(backGround, "/pictures/rip1.gif");
        int delay = 300000; //milliseconds задержка до создания нового персонажа, 5 минут
        disableButtons();
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try (FileWriter writer = new FileWriter("petFile")) {
                    writer.write("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                game = new Game();
            }
        };
        Timer t = new Timer(delay, taskPerformer);
        t.setRepeats(false);
        t.start();
        disableButtons();
    }

    public void createPet() {
        if (pet == null) {
            screenImages.put(backGround, "/pictures/start.png");
            patLive.repaint();
            int delay = 2000; //milliseconds
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    screenImages.clear();
                    screenImages.put(backGround, "/pictures/choose.png");
                    patLive.repaint();
                    patLive.addMouseListener(ml);
                }
            };
            Timer t = new Timer(delay, taskPerformer);
            t.setRepeats(false);
            t.start();
        }
    }

    private void createPetFromFile() {
        int del = 1800000; //milliseconds, 30 минут
        if (pet != null && pet.lastLaunchTime != 0) {
            if (pet.getState() == Pet.States.DEAD) {
                updateStatePicture();
            } else {
                int difference = (int) (currentLaunchTime - pet.lastLaunchTime);
                int decreaseCharacteristics = difference / del;
                pet.setSatiety(-decreaseCharacteristics);
                pet.setEnergy(-decreaseCharacteristics / 2);
                pet.setHygiene(-decreaseCharacteristics / 2);
                pet.setFun(-decreaseCharacteristics / 2);
                pet.updateState();
                updateStatePicture();
                updateSatietyScale();
                updateEnergyScale();
                updateFunScale();
                updateHygieneScale();
                patLive.repaint();
            }
        }

    }

    public void startGame() {
        currentLaunchTime = System.currentTimeMillis();
        enableButtons();
        createPet();//создаем нового персонажа, если он еще не был создан
        createPetFromFile();//если был создан
        int del = 1800000;//сытость уменьшается каждые полчаса
        ActionListener chrPerformer = new ActionListener() {//жизненный цикл питомца
            int i = 0;//это счетчик, чтобы уменьшать характеристики с разной скоростью
            public void actionPerformed(ActionEvent evt) {
                if (pet != null && pet.getState() != Pet.States.DEAD && pet.getState() != Pet.States.SlEEP) {
                    pet.setSatiety(-1);
                    if (i % 2 == 0) {
                        if (pet.getEnergy() > 0) pet.setEnergy(-1);
                        if (pet.getFun() > 0) pet.setFun(-1);
                        if (pet.getHygiene() > 0) pet.setHygiene(-1);
                    }
                    pet.updateState();
                    updateStatePicture();
                    updateSatietyScale();
                    updateEnergyScale();
                    updateFunScale();
                    updateHygieneScale();
                    patLive.repaint();
                    i++;
                }
            }
        };
        Timer characteristicsLower = new Timer(del, chrPerformer);
        characteristicsLower.start();
    }
}