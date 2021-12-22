package invadem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import invadem.entities.*;
import processing.core.PImage;

import java.util.ArrayList;

public class BarrierTest {

    private PImage barrierImg1;
    private PImage barrierImg2;
    private PImage barrierImg3;
    private PImage bullet;
    private PImage bulletPower;

    private ArrayList<PImage> barrierSprites;

    private Barrier barrier;
    private Projectile bulletFromInvader;
    private Projectile bulletFromPowerInvader;
    private Projectile bulletFromTank;

    @Before
    public void setUp(){
        barrierImg1 = new PImage(8, 8);
        barrierImg2 = new PImage(8, 8);
        barrierImg3 = new PImage(8, 8);

        barrierSprites = new ArrayList<>();
        barrierSprites.add(barrierImg1);
        barrierSprites.add(barrierImg2);
        barrierSprites.add(barrierImg3);

        bullet = new PImage(1, 3);
        bulletPower = new PImage(2, 5);
        barrier = new Barrier(240, 320, 0);
        barrier.setImg(barrierImg1);
    }

    @After
    public void tearDown(){
        barrier = null;
    }

    @Test
    public void testBarrierConstruction(){
        assertNotNull(barrier);
    }

    @Test
    public void testBarrieNotDestroyed(){
        barrier.gotShot(1);
        assertFalse(barrier.shouldRemove());
    }

    @Test
    public void testBarrierGetHealth(){
        barrier.gotShot(1);
        barrier.gotShot(1);
        assertEquals(1, barrier.getHealth());
    }

    @Test
    public void testBarrierDestroyed(){
        barrier.gotShot(3);
        assertEquals(0, barrier.getHealth());
        assertTrue(barrier.shouldRemove());
    }

    @Test
    public void testBarrierCheckCollision(){
        bulletFromInvader = new Projectile(242, 318, -1, 2);
        bulletFromInvader.setImg(bullet);
        bulletFromTank = new Projectile(246, 327, 1, 1);
        bulletFromTank.setImg(bullet);
        assertTrue(barrier.checkCollision(bulletFromInvader));
        assertTrue(barrier.checkCollision(bulletFromTank));
    }

    @Test
    public void testSetBarrierSprites(){
        barrier.setSpritesData(barrierSprites);
        assertEquals(3, barrier.getSpritesData().size());
    }

    @Test
    public void testInteractionWithBullets(){
        //barrier = new Barrier(240, 320, 0);
        bulletFromInvader = new Projectile(242, 318, -1, 2);
        bulletFromInvader.setImg(bullet);

        ArrayList<Projectile> bullets = new ArrayList<>();
        bullets.add(bulletFromInvader);
        barrier.interactionWithBullet(bullets);

        assertEquals(0, bullets.size());
        assertEquals(2, barrier.getHealth());
        bulletFromInvader = null;
    }

    @Test
    public void testRemoveDeadComponents(){
        bulletFromPowerInvader = new Projectile(242, 318, -1, 3);
        bulletFromPowerInvader.setImg(bulletPower);

        ArrayList<Projectile> bullets = new ArrayList<>();
        bullets.add(bulletFromPowerInvader);

        ArrayList<Barrier> oneComponent = new ArrayList<>();
        oneComponent.add(barrier);

        barrier.interactionWithBullet(bullets);
        Barrier.removeDeadComponent(oneComponent);
        assertEquals(0, barrier.getHealth());
        assertEquals(0, oneComponent.size());
    }
}
