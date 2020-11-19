package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.models.Moon;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.util.TextUtils;

public class LevelScreen implements Screen, InputProcessor {

    final Parsec game;
    OrthographicCamera camera;
    LevelViewModel viewModel;

    Vector3 tp = new Vector3();
    boolean dragging;
    // Starting and ending points of the drag motion
    Vector2 dragStart = new Vector2();
    Vector2 dragEnd = new Vector2();

    Box2DDebugRenderer debugRenderer;
    TextUtils textUtils;

    // Debug flags
    boolean showWorld = false;
    boolean show4x4 = false;
    boolean show3x3 = false;

    public LevelScreen(Parsec game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Camera.VIEWPORT_WIDTH, Constants.Camera.VIEWPORT_HEIGHT);

        viewModel = new LevelViewModel(game);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        // Method intentionally left empty.
        debugRenderer = new Box2DDebugRenderer();
        this.textUtils = new TextUtils(game.getFont(), game.getSpriteBatch());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);

        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        // Draw projectile
        game.getShapeRenderer().circle(viewModel.getProjectilePosition().x, viewModel.getProjectilePosition().y,
                Constants.Entities.PROJECTILE_RADIUS);
        // Draw sun
        game.getShapeRenderer().circle(viewModel.getSunPosition().x, viewModel.getSunPosition().y,
                Constants.Entities.SUN_RADIUS);
        // Draw drag (if occurring)
        if(dragging) {
            game.getShapeRenderer().line(dragStart, dragEnd);
        }
        for(Moon moon : viewModel.getMoons()) {
            if(moon.getType() == EntityType.TARGET_MOON) {
                game.getShapeRenderer().setColor(Color.GOLDENROD);
            }
            game.getShapeRenderer().circle(moon.getPosition().x, moon.getPosition().y, Constants.Entities.MOON_RADIUS);
            if(moon.getType() == EntityType.TARGET_MOON) {
                game.getShapeRenderer().setColor(Color.WHITE);
            }
        }
        game.getShapeRenderer().end();

        // Debug rendering
        if(showWorld) {
            debugRenderer.render(viewModel.world, camera.combined);
        }
        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        if(show4x4) {
            drawGrid(4f);
        }
        if(show3x3) {
            drawGrid(3f);
        }
        game.getShapeRenderer().end();

        game.getSpriteBatch().begin();
        textUtils.writeGrid("SHOTS:  " + viewModel.getShots(), 4, 1, 3);
        textUtils.writeGrid("SYSTEM  " + viewModel.getLevelNumber(), 4, 2, 3);
        game.getSpriteBatch().end();

        viewModel.stepWorld();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // Method intentionally left empty.
    }

    @Override
    public void resume() {
        // Method intentionally left empty.
    }

    @Override
    public void hide() {
        // Method intentionally left empty.
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        viewModel.dispose();
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
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        if(!viewModel.isInMotion()) {
            dragging = true;
            dragStart.x = screenX;
            dragEnd.x = screenX;
            dragStart.y = camera.viewportHeight - screenY;
            dragEnd.y = camera.viewportHeight - screenY;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0 || !dragging) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        if(!viewModel.isInMotion()) {
            dragging = false;
            viewModel.shootProjectile(dragStart, dragEnd);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        if(!viewModel.isInMotion()) {
            dragEnd.x = screenX;
            // Ensure end point is never higher than start point (pellet never shot negative)
            if (camera.viewportHeight - screenY < dragStart.y)
                dragEnd.y = camera.viewportHeight - screenY;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void drawGrid(float squares) {
        float segmentWidth = Constants.Camera.VIEWPORT_WIDTH / squares;
        float segmentHeight = Constants.Camera.VIEWPORT_HEIGHT / squares;
        for(int i = 0; i < (int) squares; i++) {
            for(int j = 0; j < (int) squares; j++) {
                Vector2 start = new Vector2(segmentWidth * i, segmentHeight * j);
                Vector2 end = new Vector2(start.x + segmentWidth, start.y + segmentHeight);
                game.getShapeRenderer().rect(start.x, start.y, end.x,  end.y);
            }
        }
    }
}
