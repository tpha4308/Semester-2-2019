package invadem.entities;

import processing.core.PApplet;
import java.util.ArrayList;

public class Projectile extends Entity{
    private int shooter; // 1 = tank, 2 = regular + armoured invader, 3 = power invader
    private int damage;

    public Projectile(int x, int y, int speed, int shooter){
        super(x, y, speed);
        this.shooter = shooter;
        if (shooter == 1 || shooter == 2){
            this.damage = 1;
        }
        else if (shooter == 3){
            this.damage = 3;
        }
        this.health = 1;
        this.toRemove = false;
    }

    public void display(PApplet app){
        app.image(img, x, y);
    }

    public void move(){
        y -=  speed;
    }

    public int getShooter(){
        return this.shooter;
    }

    public int getDamage(){
        return this.damage;
    }

    public void setToRemove(){
        this.toRemove = true;
    }

    public void outOfScreenOrNot(){
        if (this.y < 0 || this.y > 480){
            this.toRemove = true;
        }
    }

    /**
     * Loop through the given list of Projectile, remove any Projectile that was set to be removed
     */
    public static void removeDeadBullets(ArrayList<Projectile> bullets){
        for (int i = 0; i < bullets.size(); i++){
            if (bullets.get(i).toRemove) {
                bullets.remove(bullets.get(i));
            }
        }
    }
}
