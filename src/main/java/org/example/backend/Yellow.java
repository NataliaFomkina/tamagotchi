package org.example.backend;

public class Yellow extends Pet {

    public Yellow(){
        super(50);
        eatPictures[0] = "/pictures/yellowfood1.PNG";
        eatPictures[1]= "/pictures/yellowfood2.PNG";
        eatPictures[2] = "/pictures/yellowfood3.PNG";
        sleepPicture = "/pictures/y6.PNG";
        petPicture = "/pictures/y3.png";
        itemPicture = "/pictures/skateboard.png";
        petCoord = new Coord (105, 105, 80, 110);
        playCoordPet [0] = petCoord;
        playCoordPet [1] = new Coord(20, 72, 80, 110);
        playCoordPet [2] = new Coord(52, 34, 80, 110);
        playCoordPet [3] = new Coord(174, 67, 80, 110);
        playCoordPet [4] = new Coord(180, 100, 80, 110);
        playCoordItem[0] = new Coord(20, 72, 70, 70);
        playCoordItem[1] = new Coord(52, 34, 70, 70);
        playCoordItem[2] = new Coord(174, 67, 70, 70);
        playCoordItem[3] = new Coord(180, 100, 70, 70);
        playCoordItem[4] = new Coord(105, 105,70,70);
    }
}