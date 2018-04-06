package com.mygdx.game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameMap implements GameData {
    private static byte [][] field;
    private TextureRegion wall;
    private TextureRegion box;
    private TextureRegion[] food;
    private TextureRegion bonus;
    private int foodCount;
    private float food_timer_anim;
    private float secPerFrames;
    private int maxFrames;

    public GameMap(){
        this.food_timer_anim = 0.0f;
        this.secPerFrames = 0.5f;
        this.maxFrames = 3;
        this.wall = Assets.getInstance().getAtlas().findRegion("wall");
        this.box = Assets.getInstance().getAtlas().findRegion("box");
        this.food = Assets.getInstance().getAtlas().findRegion("food").split(CELL_SIZE,CELL_SIZE)[0];
        this.bonus = Assets.getInstance().getAtlas().findRegion("cherry");
        this.field = new byte [MAP_WIDTH/CELL_SIZE][MAP_HEIGHT/CELL_SIZE];
        mapInit();
    }

    public void render(SpriteBatch batch){
        int currentFrame = (int)(food_timer_anim/secPerFrames);
        for (int i = 0; i <field.length ; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == WALL_SYMBOL) {
                    batch.draw(wall, i * CELL_SIZE, j * CELL_SIZE);
                }
                if (field[i][j] == BOX_SYMBOL) {
                    batch.draw(box, i * CELL_SIZE, j * CELL_SIZE);
                }
                if (field[i][j] == FOOD_SIMBOL) {
                    batch.draw(food[currentFrame], i * CELL_SIZE, j * CELL_SIZE,0,0,CELL_SIZE,CELL_SIZE,1,1,0);
                }
                if (field[i][j] == BONUS_SIMBOL) {
                    batch.draw(bonus, i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }
    public void update(float dt){
        food_timer_anim += dt;
        if (food_timer_anim >= maxFrames*secPerFrames){
            food_timer_anim = 0.0f;
        }
    }
    public void mapInit(){
        foodCount = 0;
        for (int i = 0; i <field.length ; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (i==0||j==0||i==field.length-1||j==field[i].length-1){
                    field[i][j] = WALL_SYMBOL;
                }else if (i%2==0&&j%2==0&&field[i][j]!=WALL_SYMBOL||i>=16&&i<=24&&j==17||
                        (i==16||i==24)&&(j>=17&&j<=20)){
                    field[i][j] = BOX_SYMBOL;
                }else if (i==17&&j==16||i==23&&j==16){
                    field[i][j] = BONUS_SIMBOL;
                }else {
                    field[i][j] = FOOD_SIMBOL;
                }
                if (i>16&&i<24&&j>17&&j<21||i==20&&j==16||i==0&&j==10||i==field.length-1&&j==10){
                    field[i][j] = EMPTY_CELL_SYMBOL;
                }
                if (field[i][j]==FOOD_SIMBOL){
                    foodCount++;
                }
            }
        }
    }
    public byte[][] getField(){
        return field;
    }
    public void correctFoodCount(){
        this.foodCount --;
    }

    public int getFoodCount() {
        return foodCount;
    }
}
