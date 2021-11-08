package invadem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import invadem.entities.*;
import processing.core.PApplet;
import processing.core.PImage;


public class ProjectileTest {
    private PImage projectile_regular;
    private PImage projectile_power;
    private PImage tank_img;
    private PImage invader_img;
    private Projectile bullet_regular;
    private Projectile bullet_power;
    private Projectile bullet_armoured;

    private Projectile bulletOutOfScreen;
    private Projectile bulletOutOfScreen2;


    @Before
    public void setUp() {
        projectile_power = new PImage(2, 5);
        projectile_regular = new PImage(1, 3);
        tank_img = new PImage(22, 16);
        invader_img = new PImage(16, 16);
        bullet_regular = new Projectile(50, 50, -1, 2); //bullet from regular invader
        bullet_regular.setImg(projectile_regular);
        bullet_power = new Projectile(50, 50, -1, 3); // bullet from power invader
        bullet_power.setImg(projectile_power);
        bullet_armoured = new Projectile(50, 50, -1, 2); // bullet from armoured invader
        bullet_armoured.setImg(projectile_regular);

        bulletOutOfScreen = new Projectile(320, -5, 1, 1);
        bulletOutOfScreen2 = new Projectile(320, 489, -1, 2);
    }

    @After
    public void tearDown(){
        bullet_regular = null;
        bullet_power = null;
        bullet_armoured = null;

        bulletOutOfScreen = null;
        bulletOutOfScreen2 = null;
    }



    @Test
    public void testProjectileConstructionType1(){
        assertNotNull(bullet_regular);
    }

    @Test
    public void testProjectileConstructionType2(){
        assertNotNull(bullet_power);
    }

    @Test
    public void testProjectileMove(){
        bullet_power.move();
        assertEquals(51, bullet_power.getY());
    }

    @Test
    public void testGetShooter(){
        assertEquals(3, bullet_power.getShooter());
    }

    @Test
    public void testGetDamageProjectilePower(){
        assertEquals(3, bullet_power.getDamage());
    }

    @Test
    public void testGetDamageProjectileRegular(){
        assertEquals(1, bullet_regular.getDamage());
    }

    @Test
    public void testGetDamageProjectileArmoured(){
        assertEquals(1, bullet_armoured.getDamage());
    }

    @Test
    public void testProjectileGetWidth(){
        assertEquals(projectile_regular.width, bullet_armoured.getWidth());
    }

    @Test
    public void testRegularProjectileCheckCollision(){
        Tank tank = new Tank(45, 50, 1);
        tank.setImg(tank_img);
        assertTrue(bullet_regular.checkCollision(tank));
    }

    @Test
    public void testPowerProjectileCheckCollision(){
        Tank tank = new Tank(51, 50, 1);
        tank.setImg(tank_img);
        assertFalse(bullet_regular.checkCollision(tank));
        assertTrue(bullet_power.checkCollision(tank));
    }

    @Test
    public void testProjectileOutOfScreen(){
        bulletOutOfScreen.outOfScreenOrNot();
        bulletOutOfScreen2.outOfScreenOrNot();
        assertTrue(bulletOutOfScreen.shouldRemove());
        assertTrue(bulletOutOfScreen2.shouldRemove());
    }
}
