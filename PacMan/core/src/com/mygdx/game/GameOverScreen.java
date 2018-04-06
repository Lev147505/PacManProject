package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.characters.PacMan;

public class GameOverScreen implements Screen,GameData {
    private PacMan pacMan;
    private StringBuilder statisticBuilder;
    private BitmapFont font100;
    private BitmapFont font32;
    //private Music music;
    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;

    public GameOverScreen(SpriteBatch batch){
        this.statisticBuilder = new StringBuilder(100);
        this.batch = batch;
    }

    @Override
    public void show() {
        font32 = Assets.getInstance().getAssetManager().get("p_font32.ttf",BitmapFont.class);
        font100 = Assets.getInstance().getAssetManager().get("p_font96.ttf",BitmapFont.class);
        //music = Gdx.audio.newMusic(Gdx.files.internal("file name"));
        //music.setLooping(true);
        //music.play();
        createGUI();
        generateStatistic();
    }

    @Override
    public void render(float dt) {
        update(dt);
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (ScreenManager.getInstance().getIsWin()){
            font100.draw(batch, "YOU WIN!!!",0, 650, 1280, 1, false);
        }else{
            font100.draw(batch, "GAME IS OVER",0, 650, 1280, 1, false);
        }
        font32.draw(batch,statisticBuilder,0,550,1280,1,false);
        font32.draw(batch,HighScoreSystem.showTable(),460,550,1280,1,false);
        batch.end();
        stage.draw();

    }

    public void update(float dt){
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simple_button");
        textButtonStyle.font = font32;

        TextButton.TextButtonStyle saveButtonStyle = new TextButton.TextButtonStyle();
        saveButtonStyle.up = skin.getDrawable("shortButton");
        saveButtonStyle.font = font32;

        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = font32;
        tfs.background = skin.getDrawable("nameField");
        tfs.fontColor = Color.WHITE;
        tfs.cursor = skin.getDrawable("cursor");

        skin.add("simpleSkin", textButtonStyle);
        skin.add("saveSkin",saveButtonStyle);
        skin.add("textFieldStyle", tfs);

        final TextField field = new TextField("Player_Name",skin,"textFieldStyle");
        field.setWidth(300);
        field.setPosition(660 - 560/2,300);

        Button btnNewGame = new TextButton("Restart Game", skin, "simpleSkin");
        Button btnExitGame = new TextButton("Game Menu", skin, "simpleSkin");
        Button btnSaveScore = new TextButton(" Save score ",skin,"saveSkin");


        btnNewGame.setPosition(640 - 280, 180);
        btnExitGame.setPosition(640 - 280, 60);
        btnSaveScore.setPosition(690,300);

        if (!HighScoreSystem.isResultFit(pacMan.getScore())){
            btnSaveScore.setVisible(false);
            field.setVisible(false);
        }

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        stage.addActor(btnSaveScore);
        stage.addActor(field);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        btnSaveScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnSaveScore.setVisible(false);
                field.setVisible(false);
                HighScoreSystem.saveResult(field.getText(),pacMan.getScore());
                HighScoreSystem.showTable();
            }
        });
    }

    public void setPacMan(PacMan pacMan){
        HighScoreSystem.initTopScoreTab();
        HighScoreSystem.showTable();
        this.pacMan = pacMan;
    }
    public void generateStatistic(){
        statisticBuilder.setLength(0);
        statisticBuilder = statisticBuilder.append("General score: ").append(pacMan.getScore()).append("\n").
        append("Ghosts was swallowed: ").append(pacMan.getGhostCount()).append("\n").
        append("Bonuses was swallowed: ").append(pacMan.getBonusCount()).append("\n").
        append("Food was swallowed: ").append(pacMan.getFoodCount());
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
