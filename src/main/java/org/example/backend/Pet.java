package org.example.backend;


public class Pet {
    public String petPicture;
    public String itemPicture;
    public Coord petCoord;
    public final Coord [] playCoordPet = new Coord[5];
    public final Coord [] playCoordItem = new Coord [5];
    public String [] eatPictures = new String[3];
    public String sleepPicture;
    public enum States{USUAL,HAPPY,SAD}
    private int satiety;//сытость
    private int energy;//энергия
    private int hygiene;//гигиена
    private int fun;//веселье
    private States state;//настроение
    private int age;
    public final int maxAge;
    public Pet(int maxAge){
        state = States.HAPPY;
        satiety = 5;
        energy = 7;
        hygiene = 2;
        fun = 1;
        age = 0;
        this.maxAge=maxAge;
    };//конструктор
    //геттеры
    public States getState(){return state;}
    public int getSatiety(){return satiety;}
    public int getEnergy(){return energy;}
    public int getHygiene(){return hygiene;}
    public int getFun(){return fun;}
    public int getAge(){return age;}
    //сеттеры
    public void setState(States s){state = s;}
    public void setSatiety(int n){satiety += n;}
    public void setEnergy(int n){energy += n;}
    public void setHygiene(int n){hygiene += n;}
    public void setFun(int n){fun += n;}
    public void setAge(int n){age += n;}
    //методы
    public void updateState(){
        if((satiety+energy+hygiene+fun)/4 > 8 ) state=States.HAPPY;
                else if ((satiety+energy+hygiene+fun)/4 > 5) state = States.USUAL;
                else  state = States.SAD;
    }
    public boolean eat() {
        if(getSatiety()<=9){
            if(getSatiety()<=7){
                setSatiety(3);
            } else if (getSatiety()==8){
                setSatiety(2);
            }else setSatiety(1);
            updateState();
            return true;
        }else
            return false;
    }
    public boolean bath(){
        if(getHygiene()<=9){
            if(getHygiene()<=7){
                setHygiene(3);
            } else if (getHygiene()==8){
                setHygiene(2);
            }else setHygiene(1);
            updateState();
            return true;
        }else
            return false;
    }
    public boolean play(){
        if(getFun()<=9){
            if(getFun()<=7){
                setFun(3);
            } else if (getFun()==8){
                setFun(2);
            } else if (getFun()==9){
                setFun(1);
            }
            updateState();
            return true;
        }else
            return false;
    }
    public boolean sleep(){

        if(getEnergy()<=9){

         setEnergy(10-getEnergy());
            updateState();
            return true;
        }else
            return false;
    }
    public  String newScales(int sS){//возвращает картинку для шкалы
        if(sS >= 0 && sS <=1){
return "/pictures/0scale.png";
        }else if(sS >= 1 && sS <=3){
            return "/pictures/1scale.png";
        }else if(sS >= 4 && sS <=6){
            return "/pictures/2scale.png";
        }else if (sS >= 7 && sS <=9){
            return "/pictures/3scale.png";
        }else if (sS == 10){
            return "/pictures/4scale.png";
        } else return null;
    }
}
