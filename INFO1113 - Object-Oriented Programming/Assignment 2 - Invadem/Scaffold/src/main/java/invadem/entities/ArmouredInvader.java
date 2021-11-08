package invadem.entities;

import processing.core.PImage;

public class ArmouredInvader extends Invader {
    //for an armoured invader to disappear, it has to be hit 3 times

    public ArmouredInvader(int x, int y, int speed) {
        super(x, y, speed);
        this.health = 3;
        this.scoreValue = 250;
    }
}
