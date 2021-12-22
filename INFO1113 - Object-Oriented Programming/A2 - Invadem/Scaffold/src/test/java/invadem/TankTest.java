package invadem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import invadem.entities.*;
import processing.core.PImage;

import java.util.ArrayList;

public class TankTest{
    private PImage img;
    private PImage newImg;
    private PImage projectile_regular;
    private PImage projectile_power;
    private Tank tank;

    @Before
    public void setUp() {
        img = new PImage(22, 16);
        newImg = new PImage(22, 16);
        tank = new Tank(320, 120, 1);
        tank.setImg(img);
        projectile_regular = new PImage(1, 3);
        projectile_power = new PImage(2, 5);
    }

    @After
    public void tearDown(){
        tank = null;
    }

    @Test
    public void testTankConstruction(){
        assertNotNull(tank);
    }

    @Test
    public void testGetWidth(){
        int actual = tank.getWidth();
        assertEquals(22, actual);
    }

    @Test
    public void testGetX(){
        assertEquals(320, tank.getX());
    }

    @Test
    public void testGetY(){
        assertEquals(120, tank.getY());
    }

    @Test
    public void testGetHealth(){
        assertEquals(3, tank.getHealth());
    }

    @Test
    public void testGotShot(){
        tank.gotShot(3);
        assertEquals(0, tank.getHealth());
    }

    @Test
    public void testMoveRight(){
        tank.movingRight();
        tank.move();
        assertEquals(321, tank.getX());
    }

    @Test
    public void testMoveLeft(){
        tank.movingLeft();
        tank.move();
        assertEquals(319, tank.getX());
    }

    @Test
    public void testSetImage(){
        tank.setImg(newImg);
        assertEquals(newImg, tank.getImg());
    }

    @Test
    public void testSetX(){
        tank.setX(99);
        assertEquals(99, tank.getX());
    }

    @Test
    public void testSetY(){
        tank.setY(150);
        assertEquals(150, tank.getY());
    }

    @Test
    public void testStopMovingLeft(){
        tank.movingLeft();
        tank.move();
        tank.stopMovingLeft();
        tank.move();
        assertEquals(319, tank.getX());
    }

    @Test
    public void testStopMovingRight(){
        tank.movingRight();
        tank.move();
        tank.move();
        tank.stopMovingRight();
        tank.move();
        assertEquals(322, tank.getX());
    }

    @Test
    public void testCheckCollisionFalse(){
        Projectile p = new Projectile(50, 50, -1, 2);
        p.setImg(projectile_regular);
        boolean actual = tank.checkCollision(p);
        assertFalse(actual);
    }

    @Test
    public void testCheckCollisionTrue(){
        Projectile p = new Projectile(325, 120, -1, 3);
        p.setImg(projectile_power);
        boolean actual = tank.checkCollision(p);
        assertTrue(actual);
    }

    @Test
    public void testTankIsNotDead(){
        tank.gotShot(1);
        assertFalse(tank.shouldRemove());
    }

    /**
     * Test the fire method in the tank class
     * Once fire, the list of bullets should have one projectile
     */
    @Test
    public void testTankFire(){
        ArrayList<Projectile> bullets = new ArrayList<>();
        tank.fire(bullets);
        assertNotNull(bullets.get(0));
    }

    /**
     * Create a list of two projectiles, one intersects with the tank and another doesn't
     * Check for the interaction of the tank with the bullets in the list
     * Should delete the projectile that intersects with the tank, and tank got damaged by 1
     */
    @Test
    public void testInteractionWithBullets(){
        Projectile p1 = new Projectile(50, 50, -1, 2);
        Projectile p2 = new Projectile(325, 120, -1, 2);

        p1.setImg(projectile_regular);
        p2.setImg(projectile_regular);
        ArrayList<Projectile> bullets = new ArrayList<>();
        bullets.add(p1);
        bullets.add(p2);

        tank.interactionWithBullet(bullets);
        assertEquals(1, bullets.size());
        assertEquals(2, tank.getHealth());
    }
}