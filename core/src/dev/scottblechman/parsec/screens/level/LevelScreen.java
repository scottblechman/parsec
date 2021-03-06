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
import dev.scottblechman.parsec.common.components.Explosion;
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
        Color color = Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);

        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        // Draw star field
        for(Vector3 star : viewModel.getStarField()) {
            game.getShapeRenderer().point(star.x, star.y, 0);
        }
        game.getShapeRenderer().end();

        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        game.getShapeRenderer().setColor(getSatColor());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        // Draw projectile
        if(!viewModel.isLevelFinished()) {
            drawSatellite();
        }
        game.getShapeRenderer().end();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);

        // Draw barrier
        if(viewModel.getBarrier() != null) {
            game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.SATELLITE));
            game.getShapeRenderer().rect(viewModel.getBarrier().getPosition().x - (viewModel.getBarrier().getWidth() / 2),
                    viewModel.getBarrier().getPosition().y - (viewModel.getBarrier().getHeight() / 2),
                    viewModel.getBarrier().getWidth(), viewModel.getBarrier().getHeight());
        }

        // Draw drag (if occurring)
        if(dragging) {
            game.getShapeRenderer().line(dragStart, dragEnd);
        }
        drawMoons();

        // Draw sun
        game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.SUN));
        game.getShapeRenderer().circle(viewModel.getSunPosition().x, viewModel.getSunPosition().y,
                Constants.Entities.SUN_RADIUS);
        game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));

        game.getShapeRenderer().end();

        // Debug rendering
        if(Constants.Game.DEBUG_MODE) {
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
        if(viewModel.tutorialMode() && viewModel.onTutorialLevel()) {
            textUtils.writeGrid(viewModel.getLevelMessage(), 3, 1, 2);
        } else {
            textUtils.writeGrid("SHOTS:  " + viewModel.getShots(), 4, 1, 3);
            textUtils.writeGrid("SYSTEM  " + viewModel.getLevelNumber(), 4, 2, 3);
        }
        if(viewModel.isLevelFinished()) {
            textUtils.writeGrid(viewModel.getCompleteMessage(), 5, 2, 3);
        }
        game.getSpriteBatch().end();

        if(viewModel.isLevelFinished()) {
            viewModel.getNextLevelButton().draw(game.getSpriteBatch(), game.getShapeRenderer());
        }

        // Draw particle effects
        for(Explosion e : viewModel.getExplosions()) {
            e.draw(game.getSpriteBatch(), game.getShapeRenderer());
        }

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
        if(keycode == Input.Keys.ESCAPE && viewModel.isInMotion()) {
            viewModel.reset(true);
        }
        return true;
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
        if(viewModel.isLevelFinished() && viewModel.getNextLevelButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            game.getSoundService().playButtonSFX();
            viewModel.nextLevel();
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
            game.getSoundService().stopDragSFX();
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
            game.getSoundService().playDragSFX(dragStart.dst(dragEnd) / 50);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        camera.unproject(tp.set(screenX, screenY, 0));
        viewModel.getNextLevelButton().setHover(viewModel.getNextLevelButton().getHoverBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY));
        return true;
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

    /**
     * Uses the timeout to add a hue and lightness adjustment to the satellite, mimicking heating.
     * @return color with hue and lightness adjusted for timeout state
     */
    private Color getSatColor() {
        // Mod satellite lightness based on timer
        Color satelliteColor = Color.valueOf(Constants.Colors.SATELLITE);
        float[] hsv = new float[3];
        satelliteColor.toHsv(hsv);
        if(hsv[0] > 0) {
            hsv[0] -= viewModel.getTimeout() * Constants.Graphics.SATELLITE_HUE_SCALAR;
        } else {
            hsv[0] = 0;
        }
        hsv[2] += (viewModel.getTimeout()) / 100;

        satelliteColor = satelliteColor.fromHsv(hsv);
        if(viewModel.isProjectileInvulnerable()) {
            satelliteColor.a = (float) (1 - Math.exp(viewModel.getRemainingProjectileInvulnerability()));
        }
        return satelliteColor;
    }

    private void drawMoons() {
        for(Moon moon : viewModel.getMoons()) {
            if(moon.getType() == EntityType.TARGET_MOON) {
                game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.TARGET));
            } else {
                game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.MOON));
            }
            game.getShapeRenderer().circle(moon.getPosition().x, moon.getPosition().y, Constants.Entities.MOON_RADIUS);
            if(moon.getType() == EntityType.TARGET_MOON) {
                game.getShapeRenderer().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
            }
        }
    }

    private void drawSatellite() {
        game.getShapeRenderer().circle(viewModel.getProjectilePosition().x, viewModel.getProjectilePosition().y,
                Constants.Entities.PROJECTILE_RADIUS / 1.5f);
        game.getShapeRenderer().rect(viewModel.getProjectilePosition().x - Constants.Entities.PROJECTILE_RADIUS * 1.5f,
                viewModel.getProjectilePosition().y - Constants.Entities.PROJECTILE_RADIUS / 3f,
                Constants.Entities.PROJECTILE_RADIUS * 3f, Constants.Entities.PROJECTILE_RADIUS / 1.5f);
    }
}
