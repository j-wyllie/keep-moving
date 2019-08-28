package com.mygdx.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scene.Hud;
import com.mygdx.game.ZombieTrain;

import static com.mygdx.game.tool.InputHandler.isTouched;

public class MenuScreen implements Screen {
    private ZombieTrain game;
    private Stage stage;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private FileHandle file;

    private Texture background;

    private static final Float MENU_WIDTH = 1080f;
    private static final Float MENU_HEIGHT = 1920f;
    private static final long INACTIVE_TIME_MILLIS = 1000;

    private Integer allTimeHighScore = 0;
    private final Vector2 SCORE_POS = new Vector2(MENU_WIDTH / 2 - 100, MENU_HEIGHT - 800);
    private Label highScoreLabel;

    private final String INSTRUCTION_TEXT = "tap to play\nhigh score";
    private final Vector2 TEXT_POS = new Vector2(MENU_WIDTH / 2 - 350, MENU_HEIGHT - 680);
    private Label instructionText;
    private long inactiveTimer;


    public MenuScreen(ZombieTrain game) {
        this.game = game;

        // create cam used to follow the player through cam world
        gameCam = new OrthographicCamera();

        // create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(1080, 1920, gameCam);

        // initially set our gamcam to be centered correctly at the start of of map
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        stage = new Stage(gamePort, game.batch);

        background = new Texture("menu_background_highres.png");

        file = Gdx.files.local("data/score.txt");
        if (Gdx.files.isLocalStorageAvailable() && file.exists()) {
            try {
                allTimeHighScore = Integer.valueOf(file.readString());
            } catch (NumberFormatException exception) {
                allTimeHighScore = 0;
            }
        }

        Label.LabelStyle deannaLabelStyle = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("*.fnt"));
        deannaLabelStyle.font = myFont;
        deannaLabelStyle.fontColor = Color.GOLDENROD;

        highScoreLabel = new Label(String.valueOf(allTimeHighScore), deannaLabelStyle);
        //highScoreLabel.setSize(MENU_WIDTH, 100);
        highScoreLabel.setPosition(SCORE_POS.x, SCORE_POS.y);
        //highScoreLabel.setFontScale(2);

        instructionText = new Label(INSTRUCTION_TEXT, deannaLabelStyle);
        instructionText.setPosition(TEXT_POS.x, TEXT_POS.y);
        instructionText.setAlignment(Align.center);

        stage.addActor(highScoreLabel);
        stage.addActor(instructionText);
    }

    @Override
    public void show() {
        inactiveTimer = TimeUtils.millis();
//        ZombieTrain.adHandler.showAds(true);
    }

    private void update(float dt) {
        if (isTouched() && (TimeUtils.millis() - inactiveTimer) > INACTIVE_TIME_MILLIS) {
            inactiveTimer = TimeUtils.millis();
            PlayScreen playScreen = new PlayScreen(game);
            playScreen.setup();
            game.setScreen(playScreen);

        }

        // save highscore
        if (Hud.getLocalHighScore() > allTimeHighScore) {
            allTimeHighScore = Hud.getLocalHighScore();
            file.writeString(String.valueOf(allTimeHighScore), false);
        }
        highScoreLabel.setText(String.valueOf(allTimeHighScore));

    }

    @Override
    public void render(float dt) {
        update(dt);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        //ZombieTrain.adHandler.showAds(true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
