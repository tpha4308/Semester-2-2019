package invadem.entities;

import processing.core.PImage;

public class PowerInvader extends Invader {

    public PowerInvader(int x, int y, int speed) {
        super(x, y, speed);
        this.health = 1;
        this.scoreValue = 250; }

}
