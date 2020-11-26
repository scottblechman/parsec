package dev.scottblechman.parsec.screens.score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.Button;
import dev.scottblechman.parsec.state.enums.ScreenState;
import dev.scottblechman.parsec.util.TextUtils;

public class ScoreScreen implements Screen, InputProcessor {

    private final Parsec game;
    private final OrthographicCamera camera;
    private final ScoreViewModel viewModel;

    private TextUtils textUtils;

    private final Button newGameButton;
    private final Button quitButton;

    Vector3 tp = new Vector3();

    public ScoreScreen(Parsec game) {
        this.game = game;
        this.viewModel = new ScoreViewModel();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Camera.VIEWPORT_WIDTH, Constants.Camera.VIEWPORT_HEIGHT);

        newGameButton = new Button("NEW GAME", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 3f, Constants.Camera.VIEWPORT_HEIGHT / 11f), game.getFont());
        quitButton = new Button("QUIT", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 1.5f, Constants.Camera.VIEWPORT_HEIGHT / 11f), game.getFont());

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        this.textUtils = new TextUtils(game.getFont(), game.getSpriteBatch());
    }

    @Override
    public void render(float delta) {
        Color color = Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        // Draw star field
        for(Vector3 star : viewModel.getStarField()) {
            game.getShapeRenderer().point(star.x, star.y, 0);
        }

        if(Constants.Game.DEBUG_MODE) {
            game.getShapeRenderer().rect(newGameButton.getBounds().x, newGameButton.getBounds().y, newGameButton.getBounds().width, newGameButton.getBounds().height);
            game.getShapeRenderer().rect(quitButton.getBounds().x, quitButton.getBounds().y, quitButton.getBounds().width, quitButton.getBounds().height);
        }
        game.getShapeRenderer().end();

        game.getSpriteBatch().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        game.getSpriteBatch().begin();
        textUtils.writeGrid("GAME OVER", 5, 2, 4);
        textUtils.writeGrid("TOTAL SCORE: " + game.getScoreService().getTotal(), 11, 5, 1);
        textUtils.writeList(makeScoreList());
        game.getSpriteBatch().end();

        newGameButton.draw(game.getSpriteBatch(), game.getShapeRenderer());
        quitButton.draw(game.getSpriteBatch(), game.getShapeRenderer());

        viewModel.update();
    }

    @Override
    public void resize(int width, int height) {
        // Intentionally left empty
    }

    @Override
    public void pause() {
        // Intentionally left empty
    }

    @Override
    public void resume() {
        // Intentionally left empty
    }

    @Override
    public void hide() {
        // Intentionally left empty
    }

    @Override
    public void dispose() {
        // Intentionally left empty
    }

    private String[] makeScoreList() {
        String[] scoreLines = new String[game.getScoreService().getScores().size()];
        for(int i = 0; i < game.getScoreService().getScores().size(); i++) {
            scoreLines[i] = "SYSTEM " + (i + 1) + ": " + game.getScoreService().getScores().get(i);
        }
        return scoreLines;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        camera.unproject(tp.set(screenX, screenY, 0));
        if(newGameButton.getBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            game.navigateTo(ScreenState.GAME);
        } else if(quitButton.getBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            Gdx.app.exit();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
