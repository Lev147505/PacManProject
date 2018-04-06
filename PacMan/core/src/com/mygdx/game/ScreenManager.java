package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.characters.PacMan;

public class ScreenManager implements GameData {

    public enum ScreenType{
        MENU, GAME, GAMEOVER
    }
    private SpriteBatch batch;
    private boolean isWin;

    private PacManWorld game;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private LoadingScreen loadingScreen;
    private Viewport viewport;
    private Camera camera;

    private Screen targetScreen;

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private ScreenManager() {
    }

    public void init(PacManWorld game, SpriteBatch batch){
        this.isWin = false;
        this.batch = batch;
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280,720, camera);
        this.menuScreen = new MenuScreen(batch);
        this.gameScreen = new GameScreen(batch,camera);
        this.gameOverScreen = new GameOverScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        viewport.apply();
    }

    public void resize(int width, int height){
        viewport.update(width,height);
        viewport.apply();
    }

    public void resetCamera(){
        camera.position.set(640,360,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void changeScreen(ScreenType type){
        Screen currentScreen = game.getScreen();
        if (currentScreen != null){
            currentScreen.dispose();
        }
        resetCamera();
        switch (type){
            case MENU:
                game.setScreen(loadingScreen);
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                game.setScreen(loadingScreen);
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case GAMEOVER:
                game.setScreen(loadingScreen);
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAMEOVER);
        }
    }

    public void transferPacMan(PacMan pacMan){
        gameOverScreen.setPacMan(pacMan);
    }
    public void setIsWin(boolean var){
        isWin = var;
    }
    public boolean getIsWin() {
        return isWin;
    }
    public void goToTarget(){
        game.setScreen(targetScreen);
    }
    public Viewport getViewport() {
        return viewport;
    }
}
