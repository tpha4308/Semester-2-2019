package invadem.entities;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Barrier extends Entity {

    private ArrayList<PImage> spritesData;

    public Barrier(int x, int y, int speed){
        super(x, y, speed);
        this.toRemove = false;
        this.health = 3;
    }

    public void display(PApplet app){
        app.image(spritesData.get(3-health), x, y);
    }

    public void gotShot(int damage){
        health -= damage;
        removeEntity();
    }

    public void setSpritesData(ArrayList<PImage> spritesData){
        this.spritesData = spritesData;
    }

    /**
     * Loop through the given Projectile list to check if the current Barrier object collided with any projectile in the list
     * call gotShot() with the projectile's damage level
     * set the projectile to be removed
     * Call the Projectile static method to remove any dead (already collided with some other object) Projectile
     */

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
     * Loop through the list of seven components in each barrier
     * Remove any component that was set to be removed
     */
    public static void removeDeadComponent(ArrayList<Barrier> each_barrier){
        for (int i = 0; i < each_barrier.size(); i++){
            if (each_barrier.get(i) != null && each_barrier.get(i).shouldRemove()){
                each_barrier.remove(each_barrier.get(i));
                break;
            }
        }
    }

    //for testing
    public ArrayList<PImage> getSpritesData(){
        return spritesData;
    }
}