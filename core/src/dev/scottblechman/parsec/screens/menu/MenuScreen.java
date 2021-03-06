package dev.scottblechman.parsec.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.state.enums.ScreenState;
import dev.scottblechman.parsec.util.TextUtils;

public class MenuScreen implements Screen, InputProcessor {

    private final Parsec game;
    private final OrthographicCamera camera;

    private TextUtils textUtils;

    Vector3 tp = new Vector3();

    private final MenuViewModel viewModel;

    public MenuScreen(Parsec game) {
        this.game = game;
        this.viewModel = new MenuViewModel(game);
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Camera.VIEWPORT_WIDTH, Constants.Camera.VIEWPORT_HEIGHT);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        textUtils = new TextUtils(game.getFont(), game.getSpriteBatch());
    }

    @Override
    public void render(float delta) {
        Color color = Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.getSpriteBatch().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        game.getSpriteBatch().begin();
        game.getFont().getData().setScale(3f);
        textUtils.writeGrid("(PAR) SEC", 5, 2, 3);
        game.getFont().getData().setScale(1f);
        game.getSpriteBatch().end();

        game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        // Draw star field
        for(Vector3 star : viewModel.getStarField()) {
            game.getShapeRenderer().point(star.x, star.y, 0);
        }

        if(Constants.Game.DEBUG_MODE) {
            game.getShapeRenderer().rect(viewModel.getNewGameButton().getBounds().x, viewModel.getNewGameButton().getBounds().y, viewModel.getNewGameButton().getBounds().width, viewModel.getNewGameButton().getBounds().height);
            game.getShapeRenderer().rect(viewModel.getQuitButton().getBounds().x, viewModel.getQuitButton().getBounds().y, viewModel.getQuitButton().getBounds().width, viewModel.getQuitButton().getBounds().height);
        }
        game.getShapeRenderer().end();

        viewModel.getNewGameButton().draw(game.getSpriteBatch(), game.getShapeRenderer());
        viewModel.getQuitButton().draw(game.getSpriteBatch(), game.getShapeRenderer());

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
        if(viewModel.getNewGameButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            game.getSoundService().playButtonSFX();
            game.navigateTo(ScreenState.GAME);
        } else if(viewModel.getQuitButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            game.getSoundService().playButtonSFX();
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
        camera.unproject(tp.set(screenX, screenY, 0));
        viewModel.getNewGameButton().setHover(viewModel.getNewGameButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY));
        viewModel.getQuitButton().setHover(viewModel.getQuitButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY));
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
