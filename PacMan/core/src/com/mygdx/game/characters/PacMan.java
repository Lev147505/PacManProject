package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;

public class PacMan extends Unit implements GameData {

    private float angryTime;
    private int lives;
    private int score;
    private int ghostCount;
    private int foodCount;
    private int bonusCount;
    private int rotation;
    private StringBuilder guiHelper;
    TextureRegion[] textureRegions_bonus;

    public PacMan(GameMap gameMap, int cell_x, int cell_y){
        this.gameMap = gameMap;
        this.position = new Vector2((cell_x+0.5f)*CELL_SIZE, (cell_y+0.5f)*CELL_SIZE);
        this.velocity = new Vector2(0,0);
        this.xDest = position.x;
        this.yDest = position.y;
        this.up_right = false;
        this.down_left = false;
        this.flipX = false;
        this.flipY = false;
        this.textureRegion = Assets.getInstance().getAtlas().findRegion("pacman").split(CELL_SIZE,CELL_SIZE)[0];
        this.textureRegions_bonus = Assets.getInstance().getAtlas().findRegion("angry_pacman").split(CELL_SIZE,CELL_SIZE)[0];
        this.timer_anim = 0.0f;
        this.secPerFrames = 0.15f;
        this.maxFrames = 4;
        this.unitArea = new Circle(position,20);
        this.angryTime = 0.0f;
        this.lives = 1;
        this.score = 0;
        this.guiHelper = new StringBuilder(100);
    }

    @Override
    public void render(SpriteBatch batch){
        drawPacMan(batch);
    }
    public void renderGUI(SpriteBatch batch, BitmapFont font){
        guiHelper.setLength(0);
        guiHelper.append("Lives: ").append(lives).append("\nScore: ").append(score);
        font.draw(batch, guiHelper, 20, 700);
    }
    @Override
    public void update(float dt){
        clock(dt);
        move();
        position.mulAdd(velocity,dt);
        checkPoint();
        eatingFood();
    }

    public void move(){
        switch (chooseDirection(this)){
            case 1: //ВВЕРХ
                up_right = true;
                rotation = 90;
                flipX = false;
                flipY = false;
                yDest = position.y+CELL_SIZE;
                velocity.y = UNIT_SPEED;
                break;
            case 2://ВПРАВО
                up_right = true;
                rotation = 0;
                flipX = false;
                flipY = false;
                xDest = position.x+CELL_SIZE;
                velocity.x = UNIT_SPEED;
                break;
            case 3://ВНИЗ
                down_left = true;
                rotation = 270;
                flipX = false;
                flipY = true;
                yDest = position.y-CELL_SIZE;
                velocity.y = -UNIT_SPEED;
                break;
            case 4://ВЛЕВО
                down_left = true;
                rotation = 0;
                flipY = false;
                flipX = true;
                xDest = position.x-CELL_SIZE;
                velocity.x = -UNIT_SPEED;
                break;
        }
        this.unitArea.setPosition(position.x,position.y);
    }

    public void meetAction(Ghost enemy){
        if (this.unitArea.contains(enemy.getPosition().x,enemy.getPosition().y)){
            if (angryTime >0){
                ghostCount++;
                score += 150;
                enemy.setPosition(21.5f*CELL_SIZE,18.5f*CELL_SIZE);
                enemy.setxDest(enemy.getPosition().x);
                enemy.setyDest(enemy.getPosition().y);
                enemy.setGoal_point(enemy.getPosition().x,enemy.getPosition().y);
            }else {
                position.x = 19.5f*CELL_SIZE;
                position.y = 16.5f*CELL_SIZE;
                xDest = position.x;
                yDest = position.y;
                velocity.x = 0;
                velocity.y = 0;
                up_right = false;
                down_left = false;
                lives --;
            }
        }

    }

    public void eatingFood(){
        if(position.x/CELL_SIZE < 0||position.x/CELL_SIZE>MAP_WIDTH/CELL_SIZE-1){
            return;
        }
        if (position.x==xDest&&position.y==yDest){
            if (gameMap.getField()[(int)(xDest/CELL_SIZE)][(int)(yDest/CELL_SIZE)] == BONUS_SIMBOL){
                angryTime = 5;
                score += 50;
                bonusCount++;
                gameMap.getField()[(int)(xDest/CELL_SIZE)][(int)(yDest/CELL_SIZE)] = EMPTY_CELL_SYMBOL;
            }
            if (gameMap.getField()[(int)(xDest/CELL_SIZE)][(int)(yDest/CELL_SIZE)] == FOOD_SIMBOL){
                gameMap.correctFoodCount();
                score += 5;
                foodCount++;
                gameMap.getField()[(int) (xDest / CELL_SIZE)][(int) (yDest / CELL_SIZE)] = EMPTY_CELL_SYMBOL;
            }
        }
    }

    public int getLives() {
        return lives;
    }
    public float getAngryTime() {
        return angryTime;
    }
    public void drawPacMan(SpriteBatch batch){
        if (angryTime > 0){
            if (flipX != textureRegions_bonus[getCurrentFrame()].isFlipX()){
                textureRegions_bonus[getCurrentFrame()].flip(true,false);
            }
            if (flipY != textureRegions_bonus[getCurrentFrame()].isFlipY()){
                textureRegions_bonus[getCurrentFrame()].flip(false,true);
            }
            batch.draw(textureRegions_bonus[getCurrentFrame()],position.x-CELL_SIZE/2, position.y-CELL_SIZE/2,
                    CELL_SIZE/2,CELL_SIZE/2,CELL_SIZE,CELL_SIZE,1,1,rotation);
        }else {
            if (flipX != textureRegion[getCurrentFrame()].isFlipX()){
                textureRegion[getCurrentFrame()].flip(true,false);
            }
            if (flipY != textureRegion[getCurrentFrame()].isFlipY()){
                textureRegion[getCurrentFrame()].flip(false,true);
            }
            batch.draw(textureRegion[getCurrentFrame()],position.x-CELL_SIZE/2, position.y-CELL_SIZE/2,
                    CELL_SIZE/2,CELL_SIZE/2,CELL_SIZE,CELL_SIZE,1,1,rotation);
        }
    }
    public void clock(float dt){
        timer_anim += dt;
        angryTime -= dt;
        if (timer_anim >= maxFrames*secPerFrames){
            timer_anim = 0.0f;
        }
        if (angryTime < -10){
            angryTime = 0;
        }
    }

    public int getScore() {
        return score;
    }
    public int getFoodCount() {
        return foodCount;
    }
    public int getBonusCount() {
        return bonusCount;
    }
    public int getGhostCount() {
        return ghostCount;
    }
}
