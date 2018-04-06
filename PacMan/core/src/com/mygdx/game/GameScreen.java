package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.characters.Ghost;
import com.mygdx.game.characters.PacMan;

import java.util.Arrays;
import java.util.List;

public class GameScreen implements Screen,GameData{

    private SpriteBatch batch;

    private PacMan pacMan;
    private List<Ghost> enemy;
    private GameMap gameMap;
    private TextureAtlas atlas;
    private int level;
    private Camera camera;
    private BitmapFont font48;

    public GameScreen(SpriteBatch batch, Camera camera){

        this.batch = batch;
        this.camera = camera;
    }

    @Override
    public void show() {
        level = 1;
        atlas = new TextureAtlas(Gdx.files.internal("p_atlas.pack"));
        font48 = Assets.getInstance().getAssetManager().get("p_font48.ttf");
        gameMap = new GameMap();
        pacMan = new PacMan(gameMap,20,16);
        enemy = Arrays.asList(new Ghost(gameMap,"ghost", pacMan,18,18),
                new Ghost(gameMap,"ghost1", pacMan,19,18),
                new Ghost(gameMap,"ghost2", pacMan,20,18));
        camera.position.set(pacMan.getPosition().x, pacMan.getPosition().y,0);
        camera.update();
    }

    @Override
    public void render(float dt) {
        update(dt);
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        gameMap.render(batch);
        pacMan.render(batch);
        for (int i = 0; i < level; i++) {
            enemy.get(i).render(batch);
        }
        resetCamera();
        batch.setProjectionMatrix(camera.combined);
        pacMan.renderGUI(batch, font48);
        batch.end();

    }

    public void update(float dt){
        pacMan.update(dt);
        for (int i = 0; i < level; i++) {
            enemy.get(i).update(dt);
        }
        gameMap.update(dt);
        trackPacManCamera();
        if (pacMan.getLives()==0){
            ScreenManager.getInstance().transferPacMan(pacMan);
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER);
        }
        if (gameMap.getFoodCount()==0){
            if (this.level==enemy.size()){
                ScreenManager.getInstance().setIsWin(true);
                ScreenManager.getInstance().transferPacMan(pacMan);
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER);
            }else{
                startNewLevel();
            }
        }
    }

    public void startNewLevel(){
        level++;
        gameMap.mapInit();
        for (int i = 0; i < level; i++) {
            enemy.get(i).setPosition((18.5f+i)* CELL_SIZE,18.5f*CELL_SIZE);
            enemy.get(i).setxDest(enemy.get(i).getPosition().x);
            enemy.get(i).setyDest(enemy.get(i).getPosition().y);
            enemy.get(i).setGoal_point(enemy.get(i).getPosition().x,enemy.get(i).getPosition().y);
        }
        pacMan.setPosition(20.5f*CELL_SIZE,16.5f*CELL_SIZE);
        pacMan.setxDest(pacMan.getPosition().x);
        pacMan.setyDest(pacMan.getPosition().y);

    }

    public void trackPacManCamera(){
        camera.position.set(pacMan.getPosition().x,pacMan.getPosition().y,0);
        if (camera.position.x < 640){
            camera.position.x = 640;
        }
        if (camera.position.y < 360){
            camera.position.y = 360;
        }
        if (camera.position.x > MAP_WIDTH - 640){
            camera.position.x = MAP_WIDTH - 640;
        }
        if (camera.position.y > MAP_HEIGHT - 360){
            camera.position.y = MAP_HEIGHT - 360;
        }
        camera.update();
    }

    public void resetCamera(){
        camera.position.set(640,360,0);
        camera.update();
    }

    public PacMan getPacMan() {
        return pacMan;
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
