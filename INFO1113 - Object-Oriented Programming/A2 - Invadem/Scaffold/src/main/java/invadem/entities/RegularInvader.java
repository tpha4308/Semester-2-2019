package invadem.entities;

import processing.core.PImage;

public class RegularInvader extends Invader {

    public RegularInvader(int x, int y, int speed) {
        super(x, y, speed);
        this.health = 1;
        this.scoreValue = 100;
    }
}
