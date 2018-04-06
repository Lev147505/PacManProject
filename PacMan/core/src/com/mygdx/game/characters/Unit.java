package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameData;
import com.mygdx.game.GameMap;

public abstract class Unit implements GameData {
    GameMap gameMap;
    Vector2 position;
    Vector2 velocity;
    Circle unitArea;
    float timer_anim;
    float secPerFrames;
    int maxFrames;
    float yDest;
    float xDest;
    boolean up_right;
    boolean down_left;
    boolean flipX;
    boolean flipY;
    TextureRegion[] textureRegion;

    abstract void render(SpriteBatch batch);
    abstract void update(float dt);

    public int chooseDirection(float angryTimer, Vector2 positionPacMan, Vector2 goal_point){
        //ВВЕРХ
        if (isClear((int)(position.x/CELL_SIZE),(int)(position.y/CELL_SIZE+1))){
            if ((angryTimer<0&&Vector2.dst(goal_point.x,goal_point.y,position.x,position.y+CELL_SIZE)<
                    Vector2.dst(goal_point.x,goal_point.y,position.x,position.y))||(angryTimer>0&&
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y+CELL_SIZE)>
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y))){
                return 1;
            }
        }
        //ВПРАВО
        if (isClear((int)(position.x/CELL_SIZE+1),(int)(position.y/CELL_SIZE))){
            if ((angryTimer<0&&Vector2.dst(goal_point.x,goal_point.y,position.x+CELL_SIZE,position.y)<
                    Vector2.dst(goal_point.x,goal_point.y,position.x,position.y))||(angryTimer>0&&
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x+CELL_SIZE,position.y)>
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y))){
                return 2;
            }
        }
        //ВНИЗ
        if (isClear((int)(position.x/CELL_SIZE),(int)(position.y/CELL_SIZE-1))){
            if ((angryTimer<0&&Vector2.dst(goal_point.x,goal_point.y,position.x,position.y-CELL_SIZE)<
                    Vector2.dst(goal_point.x,goal_point.y,position.x,position.y))||(angryTimer>0&&
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y-CELL_SIZE)>
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y))){
                return 3;
            }
        }
        //ВЛЕВО
        if (isClear((int)(position.x/CELL_SIZE-1),(int)(position.y/CELL_SIZE))){
            if ((angryTimer<0&&Vector2.dst(goal_point.x,goal_point.y,position.x-CELL_SIZE,position.y)<
                    Vector2.dst(goal_point.x,goal_point.y,position.x,position.y))||(angryTimer>0&&
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x-CELL_SIZE,position.y)>
                    Vector2.dst(positionPacMan.x,positionPacMan.y,position.x,position.y))){
                return 4;
            }
        }
        return 0;
    }

    public int chooseDirection(PacMan pacMan){
        if ((Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.UP))&&
                isClear((int) (position.x/CELL_SIZE),(int) (position.y/CELL_SIZE+1))&&
                velocity.x == 0&&velocity.y == 0){
            //ВВЕРХ
            return 1;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.D)||Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&
                isClear((int)(position.x/CELL_SIZE+1),(int) (position.y/CELL_SIZE))&&
                velocity.x == 0&&velocity.y == 0){
            //ВПРАВО
            return 2;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.S)||Gdx.input.isKeyPressed(Input.Keys.DOWN))&&
                isClear((int)(position.x/CELL_SIZE),(int) (position.y/CELL_SIZE-1))&&
                velocity.x == 0&&velocity.y == 0){
            //ВНИЗ
            return 3;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.A)||Gdx.input.isKeyPressed(Input.Keys.LEFT))&&
                isClear((int)(position.x/CELL_SIZE-1),(int) (position.y/CELL_SIZE))&&
                velocity.x == 0&&velocity.y == 0){
            //ВЛЕВО
            return 4;
        }
        return 0;
    }

    public boolean isClear(int i,int j){
        if (i < 0||i > MAP_WIDTH/CELL_SIZE-1){
            return true;
        }
        if (gameMap.getField()[i][j]!= WALL_SYMBOL&&gameMap.getField()[i][j]!= BOX_SYMBOL){
            return true;
        }
        return false;
    }

    public void checkPoint() {
        if (up_right && (yDest < position.y || xDest < position.x)) {
            position.x = xDest;
            position.y = yDest;
            velocity.x = 0;
            velocity.y = 0;
            up_right = false;
            down_left = false;
            if(position.x > MAP_WIDTH+32/2){
                position.x=-32/2;
            }
        } else if (down_left && (yDest > position.y || xDest > position.x)) {
            position.x = xDest;
            position.y = yDest;
            velocity.x = 0;
            velocity.y = 0;
            up_right = false;
            down_left = false;
            if(position.x < -32/2){
                position.x=MAP_WIDTH+32/2;
            }
        }
    }

    public Vector2 getPosition() {
        return this.position;
    }
    public int getCurrentFrame(){
        return  (int)(timer_anim/secPerFrames);
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }
    public void setyDest(float yDest) {
        this.yDest = yDest;
    }
    public void setxDest(float xDest) {
        this.xDest = xDest;
    }
}
