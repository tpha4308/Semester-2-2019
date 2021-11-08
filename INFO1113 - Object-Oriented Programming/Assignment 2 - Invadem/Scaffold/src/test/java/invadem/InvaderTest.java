package invadem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import invadem.entities.*;
import processing.core.PImage;

import java.util.ArrayList;

public class InvaderTest {

    private PImage img_regular;
    private PImage img_armoured;
    private PImage img_power;

    private PImage imgRegular2;
    private PImage imgRegular3;

    private ArrayList<PImage> invSprites;

    Invader a;
    Invader b;
    Invader c;

    @Before
    public void setUp(){
        img_regular = new PImage(16, 16);
        img_armoured = new PImage(16, 16);
        img_power = new PImage(16, 16);
        a = new RegularInvader(40, 50, 1);
        a.setImg(img_regular);
        b = new PowerInvader(40, 90, 1);
        b.setImg(img_power);
        c = new ArmouredInvader(50, 80, 1);
        c.setImg(img_armoured);

        imgRegular2 = new PImage(16, 16);
        imgRegular3 = new PImage(16, 16);
        invSprites = new ArrayList<>();
        invSprites.add(img_regular);
        invSprites.add(imgRegular2);
        invSprites.add(imgRegular3);
    }

    @After
    public void tearDown(){
        a = null;
        b = null;
        c = null;
    }

    @Test
    public void testRegularInvaderConstruction(){
        assertNotNull(a);
    }

    @Test
    public void testPowerInvaderConstruction(){
        assertNotNull(b);
    }

    @Test
    public void testArmouredPowerConstruction(){
        assertNotNull(c);
    }

    @Test
    public void testRegularInvaderGotShot(){
        a.gotShot();
        assertTrue(a.shouldRemove());
        assertEquals(0, a.getHealth());
    }

    @Test
    public void testArmouredInvaderGotShot(){
        c.gotShot();
        assertFalse(c.shouldRemove());
        assertEquals(2, c.getHealth());
    }

    @Test
    public void testGetScoreValue(){
        assertEquals(100, a.getScoreValue());
        assertEquals(250, b.getScoreValue());
        assertEquals(250, c.getScoreValue());
    }

    @Test
    public void testSetSpritesData(){
        a.setSpritesData(invSprites);
        assertEquals(3, a.getSpritesData().size());
    }

    @Test
    public void testMoveLeftToRight(){
        a.setCurrentState(1);
        a.move();
        assertEquals(41, a.getX());
    }

    @Test
    public void testMoveLeftToRight30(){
        a.setCurrentState(1);
        for (int i = 0; i < 30; i++){
            a.move();
        }
        assertEquals(70, a.getX());
        assertEquals(2, a.getCurrentState());
    }

    @Test
    public void testMoveDownwards(){
        //a = new RegularInvader(40, 50, 1);
        a.setCurrentState(2);
        a.move();
        assertEquals(51, a.getY());
    }

    @Test
    public void testLotsOfMovements(){
        a.setCurrentState(1);
        for (int i = 0; i < 30; i++){
            a.move();
        }
        for (int i = 0; i < 5; i++){
            a.move();
        }
        assertEquals(70, a.getX());
        assertEquals(55, a.getY());
    }

    @Test
    public void testMoveRightToLeft(){
        Invader test = new Invader(320, 180, 1);
        test.setCurrentState(3);
        for (int i = 0; i < 30; i++){
            test.move();
        }
        assertEquals(290, test.getX());
        test = null;
    }

    @Test
    public void testAllMovementStates(){
        Invader test = new Invader(320, 180, 1);
        test.setCurrentState(1);
        for (int i = 0; i < 30; i++) {
            test.move();
        }
        for (int i = 0; i < 8; i++) {
            test.move();
        }
        for (int i = 0; i < 20; i++) {
            test.move();
        }
        assertEquals(330, test.getX());
        assertEquals(188, test.getY());
        assertEquals(3, test.getCurrentState());
        test = null;
    }

    @Test
    public void testAllMovementStates2(){
        Invader test = new Invader(220, 170, 1);
        test.setCurrentState(3);
        for (int i = 0; i < 30; i++) {
            test.move();
        }
        for (int i = 0; i < 8; i++) {
            test.move();
        }
        for (int i = 0; i < 20; i++) {
            test.move();
        }
        assertEquals(210, test.getX());
        assertEquals(178, test.getY());
        assertEquals(1, test.getCurrentState());
        test = null;
    }

    @Test
    public void testInteractionWithBullets(){
        Projectile p1 = new Projectile(50, 82, 1, 1);
        p1.setImg(new PImage(1,3));
        Projectile p2 = new Projectile(53, 80, -1, 2);
        p2.setImg(new PImage(1,3));
        Projectile p3 = new Projectile(54, 80, -1, 3);
        p3.setImg(new PImage(2,5));

        ArrayList<Projectile> bullets = new ArrayList<>();
        bullets.add(p1);
        bullets.add(p2);
        bullets.add(p3);

        c.interactionWithBullets(bullets);

        assertEquals(2, bullets.size());
        assertFalse(c.shouldRemove()); //because armoured
    }

    @Test
    public void testRemoveDeadInvaders(){
        ArrayList<Invader> inv = new ArrayList<>();
        inv.add(a);
        inv.add(b);
        inv.add(c);

        a.gotShot();
        b.gotShot();
        c.gotShot();

        Invader.removeDeadInvader(inv);
        assertEquals(2, inv.size());
    }

    @Test
    public void testInvFire(){
        ArrayList<Projectile> bullets = new ArrayList<>();

        a.invFire(bullets);
        b.invFire(bullets);
        c.invFire(bullets);
        assertEquals(3, bullets.size());
    }
}
