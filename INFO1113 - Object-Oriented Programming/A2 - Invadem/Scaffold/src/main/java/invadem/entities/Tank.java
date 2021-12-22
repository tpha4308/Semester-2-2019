package invadem.entities;

import processing.core.PApplet;
import java.util.ArrayList;

public class Tank extends Entity {
    /**
     * Boolean variables sideMoveLeft and sideMoveRight marks if the tank is moving at the corresponding direction or not
     * Int variable hit determines the number of times the tank got hit
     */
    private boolean sideMoveLeft;
    private boolean sideMoveRight;

    public Tank(int x, int y, int speed){
        super(x, y, speed);
        this.sideMoveLeft = false;
        this.sideMoveRight = false;
        this.health = 3;
    }

    public void display(PApplet app){
        x = app.constrain(x, 180, 436);
        app.image(img, x, y);
    }

    /**
     * If the boolean variables sideMoveRight or sideMoveLeft is true, the tank will move at the corresponding direction
     * If the tank is moving from left to right, its location is determined by its current position + speed
     * If from right to left, new position = current position - speed
     */
    public void move(){
        if (sideMoveLeft){
            x -= speed;
        }
        if (sideMoveRight){
            x += speed;
        }
    }

    public void setX(int newXpos){
        this.x = newXpos;
    }

    public void setY(int newYpos){
        this.y = newYpos;
    }

    /**
     * If the tank is shot by the invader, the number of hit increments by 1
     */
    public void gotShot(int damage){
        health -= damage;
        removeEntity();
    }

    /**
     * Setter method that set the moving state of the tank is to be true
     * If set, the tank is moving left
     */
    public void movingLeft(){
        sideMoveLeft = true;
    }

    /**
     * If set, the tank is moving right
     */
    public void movingRight(){
        sideMoveRight = true;
    }

    /**
     * If set, the tank is no longer moving left
     */
    public void stopMovingLeft(){
        sideMoveLeft = false;
    }

    /**
     * If set, the tank is no longer moving right
     */
    public void stopMovingRight(){
        sideMoveRight = false;
    }

    public void interactionWithBullet(ArrayList<Projectile> bullets){
        for (Projectile p : bullets){
            if (p != null && this.checkCollision(p)){
                int damage = p.getDamage();
                this.gotShot(damage);
                p.setToRemove();
            }
        }
        Projectile.removeDeadBullets(bullets);
    }

    /**
     * Will be called in the App class
     * Create a new projectile at the x and y coordinates of the fired tank
     * Add the projectile to the given list
     */
    public void fire(ArrayList<Projectile> bullets){
        Projectile p = new Projectile(this.getX() + this.getWidth()/2, this.getY() - 3, 1, 1);
        bullets.add(p);
    }
}
