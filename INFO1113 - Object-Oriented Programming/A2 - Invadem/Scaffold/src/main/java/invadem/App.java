package invadem;

//to change to java 8: export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)

import java.util.ArrayList;

import invadem.entities.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class App extends PApplet {
    private ArrayList<Projectile> bullets;
    private ArrayList<Tank> tanks;
    private ArrayList<Invader> invaders;
    private ArrayList<ArrayList<Barrier>> barriers;

    private int gameState;

    private int playerMode;
    private boolean chosenPlayerMode;

    private int currentScore;
    private int highScore;
    private int level;
    private int barrierLimit;
    private int fireRate;
    private PFont font;

    public App() {
        //Set up your objects
    }

    public void setup() {
        frameRate(60);

        fireRate = 5;
        level = 1;
        chosenPlayerMode = false;
        currentScore = 0;
        highScore = 10000;

        font = createFont("src/main/java/data/PressStart2P-Regular.ttf", 12);
        gameState = 0;

        restart();
    }

    public void settings() {
        size(640, 480);
    }

    public void draw() {
        switch (gameState){
            case 0: //game state 0: menu display
                menuFunction();
                break;
            case 1: //game state 1: instruction display
                instructionDisplay();
                break;
            case 2: // game tate 2: game ongoing
                gameDisplay();
                break;
            case 3: //game state 3: game lost
                gameOverFunction();
                break;
            case 4: //game state 4: game won
                gameWonFunction();
                break;
        }
    }

    /**
     * Reset the objects to its initial state
     */
    private void restart(){
        invaders = new ArrayList<>();
        barriers = new ArrayList<>();
        bullets = new ArrayList<>();
        tanks = new ArrayList<>();

        setInvaders();
        setBarriers();
        setTank();

        //this method is called here specifically for restarting game, not at the start
        if (chosenPlayerMode) {
            setTankBasedOnPlayerMode();
        }
    }

    /**
     * Display the instruction screen
     * Precondition: chosenPlayerMode = true
     */
    private void instructionDisplay(){
        background(0,0,0);
        fill(233, 221, 174);

        textAlign(CENTER);
        textFont(font, 15);
        text("KEYBOARD COMMANDS", (float) width/2, (float) height/3 - 20);

        textAlign(LEFT);
        textFont(font, 12);

        if (playerMode == 1){
            text("LEFT: Arrow Left", width/3, height / 2);
            text("RIGHT: Arrow Right", width/3, height / 2 + 20);
            text("FIRE: Space", width/3, height/2 + 40);
        }

        else if (playerMode == 2){
            fill(73, 189, 180);

            text("Player 1", width/7 + 50, height/2 - 20);
            text("LEFT: A", width/ 7 + 50, height / 2);
            text("RIGHT: D", width/7 + 50, height / 2 + 20);
            text("FIRE: X", width/7 + 50 , height/2 + 40);

            fill(243, 241, 137);
            text("Player 2", width/2, height/2 - 20);
            text("LEFT: Arrow Left", width/2, height / 2);
            text("RIGHT: Arrow Right", width/2, height / 2 + 20);
            text("FIRE: Space", width/2 , height/2 + 40);
        }

        fill(233, 221, 174);
        textAlign(CENTER);
        textFont(font, 15);
        text(">> PRESS ENTER TO START <<", width/2, height/4 * 3);
        text("(or BACKSPACE to go back)", width/2, height/4 * 3 + 25);
        if (keyPressed){
            if (key == ENTER || key == RETURN){
                setTankBasedOnPlayerMode();
            }
            else if (key == BACKSPACE || key == DELETE){
                gameState = 0;
            }
        }
    }

    private void menuFunction(){
        menuDisplay();
        if (keyPressed){
            if (key == 'O' || key == 'o'){
                chosenPlayerMode = true;
                playerMode = 1;
                gameState = 1;
            }
            else if (key == 'T' || key == 't'){
                chosenPlayerMode = true;
                playerMode = 2;
                gameState = 1;
            }
        }
    }

    /**
     * If number of players equals to 1 then remove the second tank in the setup list
     * Setting tanks' sprites
     * Change the gameState to the transition to the main game loop
     */
    private void setTankBasedOnPlayerMode(){
        if (playerMode == 1) {
            tanks.remove(tanks.get(1));
            tanks.get(0).setImg(loadImage("tank1.png"));
            gameState = 2;
        }
        else if (playerMode == 2){
            tanks.get(0).setX(410);
            tanks.get(0).setImg(loadImage("tank_p1.png"));
            tanks.get(1).setX(210);
            tanks.get(1).setImg(loadImage("tank_p2.png"));
            gameState = 2;
        }
    }

    private void menuDisplay(){
        background(0,0,0);
        fill(87, 89, 250);
        textAlign(CENTER);
        textFont(font, 45);
        text("INVADEM", (float) width/2, (float) height/4 + 20);
        for (int i = 0; i <= 10; i++){
            PImage sprite2 = loadImage("invader2_armoured.png");
            image(sprite2, (float) (width/4) + 30*i, (float) height/4 + 25);
        }

        fill(233, 221, 174);
        textFont(font, 15);
        text("Please choose numbers of players ", (float) width/2, (float) height/2);
        textFont(font, 14);

        text("1 player", (float) width/3,  (float) height/3 * 2);
        text("Press O" , (float) width/3, (float) height/3 * 2 + 20);

        text("2 players", (float) width/3 *2, (float) height/3 * 2);
        text("Press T", (float) width/3 *2, (float) height/3 *2  + 20);
    }

    /**
     * Main game display, will be looped over and over
     * gameWon() and gameLost() will be invoked when their condition(s) is met
     */
    private void gameDisplay(){
        background(0,0,0);

        displayTank();
        displayBullets();
        displayInvader();
        displayBarrier();
        displayInfo();

        gameWon();
        gameLost();
    }

    /**
     * When game is lost, set highScore to currentScore if currentScore is greater highScore
     * set the currentScore back to 0
     */
    private void gameOverFunction(){
        if (frameCount % 180 == 0){
            if (currentScore > highScore){
                highScore = currentScore;
            }
            currentScore = 0;
            restart();
        }
        TextDisplay();
    }

    private void gameWonFunction(){
        if (frameCount % 180 == 0){
            restart();
        }
        TextDisplay();
    }

    /**
     * If the player is pressing the left key and the tank's x-coordinate is greater than the left barrier, then we set the tank be moving right (which invokes its movement)
     * Same goes for player pressing the right key and the tank's x-coordinate is less than the right barrier minus for its width
     * The same method applies for second player but for the keys A and D
     */
    public void keyPressed(){
        //only allow these methods to be invoked during game
        if (gameState  == 2) {
            if(key == CODED){
                if (keyCode == LEFT && tanks.get(0).getX() > 180){
                    tanks.get(0).movingLeft();
                }
                else if (keyCode == RIGHT && tanks.get(0).getX() < 438) {
                    tanks.get(0).movingRight();
                }
            }
            if (playerMode == 2){
                if (key == 'A' || key == 'a' && tanks.get(1).getX() > 180){
                    tanks.get(1).movingLeft();
                }
                else if (key == 'D' || key == 'd' && tanks.get(1).getX() < 438){
                    tanks.get(1).movingRight();
                }
            }
        }
    }

    /**
     * When the player releases the key (either left or right), the tank will stop moving
     * When the player releases the space bar, a new Projectile object will be initialised
     */
    public void keyReleased() {
        // only allow these methods to invoke during gameState 2
        if (gameState == 2) {
            if (key == CODED) {
                if (keyCode == LEFT) {
                    tanks.get(0).stopMovingLeft();
                } else if (keyCode == RIGHT) {
                    tanks.get(0).stopMovingRight();
                }
            }
            if (key == ' ') {
                TankFire(tanks.get(0));
            }

            //control keys for the second tank in two player mode, only allow these methods to invoke when playerMode = 2
            if (playerMode == 2){
                if (key == 'A' || key == 'a'){
                    tanks.get(1).stopMovingLeft();
                }
                else if (key == 'D' || key == 'd'){
                    tanks.get(1).stopMovingRight();
                }
                if (key == 'X' || key == 'x'){
                    TankFire(tanks.get(1));
                }
            }
        }
    }

    /**
     * Display the current score, current level, and high score
     * If current score > high score, high score will change accordingly to current score
     */
    private void displayInfo(){
        if (currentScore > highScore){
            highScore = currentScore;
        }
        fill(255, 255, 255);
        textFont(font, 11);
        textAlign(LEFT);
        text("Score", 15, 25);
        text(currentScore, 15, 45);

        textAlign(CENTER);
        text("Level", (float) width/2, 25);
        text(level, (float) width/2, 45);

        textAlign(RIGHT);
        text("High Score", 630, 25);
        text(highScore, 630, 45);
    }

    private void setTank(){
        Tank tank1 = new Tank(width/2 - 11, 454,1);
        Tank tank2 = new Tank(width/2 - 11, 454,1);
        tanks.add(tank1);
        tanks.add(tank2);
    }

    private void setInvaders(){
        ArrayList<ArrayList<PImage>> spriteLists = new ArrayList<>(3);
        String[] filename = new String[] {"", "_armoured", "_power"};
        for (String each_name : filename){
            PImage sprite1 = loadImage("invader1" + each_name + ".png");
            PImage sprite2 = loadImage("invader2" + each_name + ".png");
            ArrayList<PImage> temp_ls = new ArrayList<>(2);
            temp_ls.add(sprite1);
            temp_ls.add(sprite2);
            spriteLists.add(temp_ls);
        }

        int x = 170;
        int y = 60;
        for (int row = 0; row < 4; row++){
            for (int column = 0; column < 10; column++){
                // armoured invader
                if (row == 0){
                    Invader i = new ArmouredInvader(x + 30 * column, y+ 30 * row, 1);
                    i.setImg(spriteLists.get(1).get(0));
                    i.setSpritesData(spriteLists.get(1));
                    invaders.add(i);
                }
                // power invader
                else if (row == 1){
                    Invader i = new PowerInvader( x + 30 * column, y+ 30 * row, 1);
                    i.setImg(spriteLists.get(2).get(0));
                    i.setSpritesData(spriteLists.get(2));
                    invaders.add(i);
                }
                //regular invader
                else {
                    Invader i = new RegularInvader(x + 30 * column, y + 30 * row, 1);
                    i.setImg(spriteLists.get(0).get(0));
                    i.setSpritesData(spriteLists.get(0));
                    invaders.add(i);
                }
            }
        }
    }

    private void setBarriers(){
        String[] filename = new String[] {"top", "left", "right", "solid"};

        ArrayList<ArrayList<PImage>> barrierSpriteLists = new ArrayList<>(4);
        for (String i : filename){
            ArrayList<PImage> componentSpriteList = new ArrayList<>(3);
            for (int s = 1; s <= 3; s++){
                componentSpriteList.add(loadImage("barrier_" + i + s + ".png"));
            }
            barrierSpriteLists.add(componentSpriteList);
        }

        int x = 216;
        int y = 410;
        barrierLimit = y;

        for (int i = 1; i<= 3; i++){
            ArrayList<Barrier> single_barrier = new ArrayList<>(7);

            Barrier top_b = new Barrier(x, y, 0);
            top_b.setImg(barrierSpriteLists.get(0).get(0));
            top_b.setSpritesData(barrierSpriteLists.get(0));
            single_barrier.add(top_b);

            Barrier left_b = new Barrier(x-8, y, 0);
            left_b.setImg(barrierSpriteLists.get(1).get(0));
            left_b.setSpritesData(barrierSpriteLists.get(1));
            single_barrier.add(left_b);

            Barrier right_b = new Barrier(x+8, y, 0);
            right_b.setImg(barrierSpriteLists.get(2).get(0));
            right_b.setSpritesData(barrierSpriteLists.get(2));
            single_barrier.add(right_b);

            Barrier solid1 = new Barrier(x-8, y+8, 0);
            solid1.setImg(barrierSpriteLists.get(3).get(0));
            solid1.setSpritesData(barrierSpriteLists.get(3));
            single_barrier.add(solid1);

            Barrier solid2 = new Barrier(x-8, y+2*8, 0);
            solid2.setImg(barrierSpriteLists.get(3).get(0));
            solid2.setSpritesData(barrierSpriteLists.get(3));
            single_barrier.add(solid2);

            Barrier solid3 = new Barrier(x+8, y+8, 0);
            solid3.setImg(barrierSpriteLists.get(3).get(0));
            solid3.setSpritesData(barrierSpriteLists.get(3));
            single_barrier.add(solid3);

            Barrier solid4 = new Barrier( x+8, y+2*8, 0);
            solid4.setImg(barrierSpriteLists.get(3).get(0));
            solid4.setSpritesData(barrierSpriteLists.get(3));
            single_barrier.add(solid4);

            barriers.add(single_barrier);
            x += 100;
        }
    }

    /**
     * Display, movements, check if out of screen
     * Remove if toRemove = true
     */
    private void displayBullets(){
        for (Projectile p : bullets){
            if (p != null){
                p.display(this);
                p.outOfScreenOrNot();
                p.move();
            }
        }
        Projectile.removeDeadBullets(bullets);
    }

    /**
     * Display, movements, check health values
     * gameLost() is invoked when tank's health <= 0
     */
    private void displayTank(){
        if (tanks != null){
            for (Tank t : tanks){
                t.display(this);
                t.move();
                t.interactionWithBullet(bullets);
            }
            gameLost();
        }
    }

    private void setBulletsImg(){
        for (Projectile p : bullets){
            if (p.getShooter() == 3){
                p.setImg(loadImage("projectile_lg.png"));
            }
            else{
                p.setImg(loadImage("projectile.png"));
            }
        }
    }

    /**
     * When invoked, a bullet will be initialised at the x and y coordinates of the corresponding tank
     * Will then set the image for the bullet and display them
     * @param t tank object
     */
    private void TankFire(Tank t){
        t.fire(bullets);
        setBulletsImg();
        displayBullets();
    }

    /**
     * Choose a random invader from the remaining invaders
     * Same as above
     */
    private void InvaderFire(){
        if (invaders.size() > 0){
            Invader random_invader = invaders.get((int) random(0, invaders.size()));

            if (frameCount % (60*fireRate) == 0){
                random_invader.invFire(bullets);
                setBulletsImg();
            }
            displayBullets();
        }
    }

    /**
     * Setting the state at which the player has lost the game (when an invader reaches within 10 pixels from the top of the barrier)
     * If one of the remaining invaders reaches 10px from the top barrier or tank's health <= 0, gameState will change to 2
     */
    private void gameLost(){
        boolean lost = false;
        for (Tank t : tanks){
            if (t.getHealth() <= 0){
                lost = true;
                break;
            }
        }
        for (Invader inv : invaders){
            if (inv != null && inv.getY() + 16 > barrierLimit - 10){
                lost = true;
                break;
            }
        }
        if (lost){
            gameState = 3;
            fireRate = 5;
        }
    }

    /**
     * Setting the condition to move to next level, which is when their is no more invaders in the invader list
     * once moved to the new level, fire rate will decrease by 1, and stays at 1
     */
    private void gameWon(){
        if (invaders.size() == 0){
            fireRate--;
            if (fireRate < 1){
                fireRate = 1;
            }
            level++;
            gameState = 4;
        }
    }

    private void TextDisplay(){
        background(0);
        if (gameState == 3){
            PImage gameOver = loadImage("gameover.png");
            image(gameOver,  (float) (width/2 - gameOver.width/2), (float) height/2);
            fill(255, 255, 255);
            textAlign(CENTER);
            text("Game Restarting...", (float) width/2,  (float) height/2 + 50);

        }
        else if (gameState == 4){
            PImage nextLevel = loadImage("nextlevel.png");
            image(nextLevel, (float) (width/2 - nextLevel.width/2), (float) height/2);
        }
    }

    /**
     * If an invader is set to be removed, increment the currentScore by the invader's score value
     */
    private void scoreIncrement(){
        for (Invader inv : invaders){
            if (inv != null && inv.shouldRemove()){
                currentScore += inv.getScoreValue();
                break;
            }
        }
    }

    /**
     * Display, movements, choosing random invader and fire, increment score before removing, remove dead invaders
     */
    private void displayInvader() {
        for (Invader inv : invaders) {
            if (inv != null) {
                inv.display(this);
                if (frameCount % 2 == 0) {
                    inv.move();
                }
                inv.interactionWithBullets(bullets);
            }
        }
        InvaderFire();
        scoreIncrement();
        Invader.removeDeadInvader(invaders);
    }

    /**
     * Display barriers, check for collision with bullets, remove
     */
    private void displayBarrier(){
        for (ArrayList<Barrier> eachBarrier : barriers) {
            if (eachBarrier != null) {
                for (Barrier component : eachBarrier) {
                    if (component != null) {
                        component.display(this);
                        component.interactionWithBullet(bullets);
                    }
                }
            }
        }
        for (ArrayList<Barrier> eachBarrier : barriers){
            Barrier.removeDeadComponent(eachBarrier);
        }
    }

    public static void main(String[] args) {
        PApplet.main("invadem.App");
    }
}