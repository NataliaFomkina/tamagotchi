package org.example.backend;

public class Man extends Pet {
    public Man(){
super(90);
eatPictures[0] = "/pictures/manfood1.PNG";
eatPictures[1]= "/pictures/manfood2.PNG";
eatPictures[2] = "/pictures/manfood3.PNG";
sleepPicture = "/pictures/m6.PNG";
petPicture = "/pictures/m3.png";
itemPicture = "/pictures/football.png";
petCoord = new Coord (85, 105, 120, 120);
playCoordPet [0] = petCoord;
playCoordPet [1] = new Coord(20, 72, 120, 120);
playCoordPet [2] = new Coord(52, 34, 120, 120);
playCoordPet [3] = new Coord(174, 67, 120, 120);
playCoordPet [4] = new Coord(180, 100, 120, 120);
playCoordItem[0] = new Coord(20, 72, 70, 70);
playCoordItem[1] = new Coord(52, 34, 70, 70);
playCoordItem[2] = new Coord(174, 67, 70, 70);
playCoordItem[3] = new Coord(180, 100, 70, 70);
playCoordItem[4] = new Coord(85, 105,70,70);
    }

}
