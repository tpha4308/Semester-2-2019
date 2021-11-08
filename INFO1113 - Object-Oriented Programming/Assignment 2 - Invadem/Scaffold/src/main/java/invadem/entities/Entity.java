package invadem.entities;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Entity {

    protected PImage img;
    protected int x;
    protected int y;
    protected int speed;
    protected int health;
    protected boolean toRemove;

    public Entity(int x, int y, int speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void setImg(PImage newImg){
        this.img = newImg;
    }

    public abstract void display(PApplet app);

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public PImage getImg(){
        return this.img;
    }

    public int getHealth(){
        return health;
    }

    public boolean shouldRemove(){
        return this.toRemove;
    }

    public void removeEntity(){
        if (health <= 0){
            toRemove = true;
        }
    }

    public int getWidth() {
        return this.img.width;
    }

    public int getHeight(){
        return this.img.height;
    }

    public boolean checkCollision(Entity e) {

        boolean collision = false;
        if ((x < (e.getX() + e.getWidth())) &&
                (x + img.width > e.getX()) &&
                (y < e.getY() + e.getHeight()) &&
                (img.height + y > e.getY())) {
            collision = true;
        }
        return collision;
    }

}