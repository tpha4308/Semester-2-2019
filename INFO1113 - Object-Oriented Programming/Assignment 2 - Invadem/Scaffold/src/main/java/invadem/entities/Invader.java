package invadem.entities;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Invader extends Entity {

    protected ArrayList<PImage> spritesData;
    protected int x_pos;
    protected int y_pos;
    protected int scoreValue;
    protected int currentState;
    protected int previousState;
    protected boolean spriteIndex1;

    public Invader(int x, int y, int speed){
        super(x, y, speed);
        this.currentState = 1;
        this.previousState = -1;
        this.spriteIndex1 = true;
        this.x_pos = x;
        this.y_pos = y;
        this.toRemove = false;
    }

    public void display(PApplet app){
        if (spritesData.size() == 2) {
            if (spriteIndex1) {
                app.image(spritesData.get(0), x, y);
            } else {
                app.image(spritesData.get(1), x, y);
            }
        }
    }

    public void setSpritesData(ArrayList<PImage> spritesData){
        this.spritesData = spritesData;
    }

    public void gotShot(){
        health--;
        removeEntity();
    }

    private void changeSpriteIndex(){
        spriteIndex1 = !spriteIndex1;
    }

    public int getScoreValue(){
        return scoreValue;
    }

    public void move(){
        //move from left to right, if moved 30 steps, it will change its current movement to move downwards and x_pos will change to the invader current x position
        //previousState will also be logged, which will help for repetition
        if (currentState == 1){
            x = x + speed;
            if (x == (x_pos + 30)) {
                x_pos = x;
                previousState = currentState;
                currentState = 2;
            }
        }

        //move downwards, if moved 8 steps, it will change to its new sprite
        //based on its previousState, the invader will determine its sideways movement
        else if (currentState == 2){
            y = y + speed;
            if (y == (y_pos + 8)){
                y_pos = y;
                changeSpriteIndex();
                if (previousState == 3){
                    previousState = currentState;
                    currentState = 1;
                }
                else if (previousState == 1){
                    previousState = currentState;
                    currentState = 3;
                }
            }
        }

        //move sideways, from right to left
        else if (currentState == 3){
            x = x - speed;
            if (x == (x_pos - 30)){
                x_pos = x;
                previousState = currentState;
                currentState = 2;
            }
        }
    }

    public void interactionWithBullets(ArrayList<Projectile> bullets){
        for (Projectile p : bullets){
            if (p != null ){
                if (p.getShooter() == 1 && this.checkCollision(p)){
                    this.gotShot();
                    p.setToRemove();
                }
            }
        }
        Projectile.removeDeadBullets(bullets);
    }

    public static void removeDeadInvader(ArrayList<Invader> invaders){
        for (int inv = 0; inv < invaders.size(); inv++){
            if (invaders.get(inv) != null && invaders.get(inv).shouldRemove()){
                invaders.remove(invaders.get(inv));
                break;
            }
        }
    }

    /**
     * Will be called in the App class
     * Create a new projectile at the random chosen invader x and y coordinates
     * Check for the shooter's instance
     * Add the newly created projectile to the given Projectile list
     */
    public void invFire(ArrayList<Projectile> bullets){
        int invaderType = -1;
        if (this instanceof RegularInvader || this instanceof ArmouredInvader){
            invaderType = 2;
        }
        else if (this instanceof PowerInvader){
            invaderType = 3;
        }
        Projectile p = new Projectile(this.x, this.y, -1, invaderType);
        bullets.add(p);
    }

    //for testing
    public void setCurrentState(int state){
        this.currentState = state;
    }

    public int getCurrentState(){
        return this.currentState;
    }

    public ArrayList<PImage> getSpritesData(){
        return this.spritesData;
    }
}