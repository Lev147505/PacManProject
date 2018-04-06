package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.GameData;
import com.mygdx.game.GameMap;

import java.util.Random;

public class Ghost extends Unit implements GameData {
    private Vector2 goal_point;
    private PacMan pacMan;
    private Random random;
    private String name;

    public Ghost( GameMap gameMap, String name, PacMan pacMan, int cell_x, int cell_y){
        this.gameMap = gameMap;
        this.name = name;
        this.pacMan = pacMan;
        this.timer_anim = 0.0f;
        this.secPerFrames = 0.15f;
        this.maxFrames = 3;
        random = new Random();
        this.position = new Vector2((cell_x*CELL_SIZE)+CELL_SIZE/2, (cell_y*CELL_SIZE)+CELL_SIZE/2);
        this.velocity = new Vector2(0,0);
        this.goal_point = new Vector2(position.x,position.y);
        xDest = position.x;
        yDest = position.y;
        this.up_right = false;
        this.down_left = false;
        this.flipX = false;
        textureRegion = Assets.getInstance().getAtlas().findRegion(name).split(CELL_SIZE,CELL_SIZE)[0];
        this.unitArea = new Circle(position,4*CELL_SIZE);
    }

    @Override
    public void render(SpriteBatch batch){
        if (flipX != textureRegion[getCurrentFrame()].isFlipX()){
            textureRegion[getCurrentFrame()].flip(true,false);
        }
        batch.draw(textureRegion[getCurrentFrame()],position.x-CELL_SIZE/2, position.y-CELL_SIZE/2,
                0,0,CELL_SIZE,CELL_SIZE,1,1,0);
    }

    @Override
    public void update(float dt){
        timer_anim += dt;
        if (timer_anim >= maxFrames*secPerFrames){
            timer_anim = 0.0f;
        }
        lookingForPacMan(pacMan.getPosition().x,pacMan.getPosition().y);
        chooseTargetPoint();
        move();
        position.mulAdd(velocity,dt);
        checkPoint();
    }
    public void chooseTargetPoint(){
        if((position.x == goal_point.x && position.y == goal_point.y)||
            (velocity.x ==0&&velocity.y==0&&chooseDirection(pacMan.getAngryTime(),pacMan.getPosition(),goal_point)==0)){
            int random_i = random.nextInt(MAP_WIDTH-CELL_SIZE)/CELL_SIZE;
            int random_j = random.nextInt(MAP_HEIGHT-CELL_SIZE)/CELL_SIZE;
            if (gameMap.getField()[random_i][random_j]!= WALL_SYMBOL&&gameMap.getField()[random_i][random_j]!= BOX_SYMBOL){
                goal_point.x = random_i*CELL_SIZE + 20;
                goal_point.y = random_j*CELL_SIZE + 20;
            }
        }
    }

    public void move(){
        if (velocity.x == 0&&velocity.y == 0){
            switch (chooseDirection(pacMan.getAngryTime(),pacMan.getPosition(),goal_point)){
                case 1://ВВЕРХ
                    up_right = true;
                    yDest = position.y+CELL_SIZE;
                    velocity.y = UNIT_SPEED;
                    break;
                case 2://ВПРАВО
                    up_right = true;
                    flipX = false;
                    xDest = position.x+CELL_SIZE;
                    velocity.x = UNIT_SPEED;
                    break;
                case 3://ВНИЗ
                    down_left = true;
                    yDest = position.y-CELL_SIZE;
                    velocity.y = -UNIT_SPEED;
                    break;
                case 4://ВЛЕВО
                    down_left = true;
                    flipX = true;
                    xDest = position.x-CELL_SIZE;
                    velocity.x = -UNIT_SPEED;
                    break;
            }
            this.unitArea.setPosition(position.x,position.y);
        }
    }

    public void lookingForPacMan(float x, float y){
        if (unitArea.contains(x,y)&&pacMan.getAngryTime()<0){
            goal_point.x = x;
            goal_point.y = y;
        }else if (unitArea.contains(x,y)&&pacMan.getAngryTime()>0){
            goal_point.x = position.x;
            goal_point.y = position.y;
        }
        pacMan.meetAction(this);
    }

    public void setGoal_point(float x, float y) {
        this.goal_point.x = x;
        this.goal_point.y = y;
    }
}
