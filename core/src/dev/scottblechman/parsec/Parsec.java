package dev.scottblechman.parsec;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.scottblechman.parsec.state.ScreenNavigation;
import dev.scottblechman.parsec.state.enums.ScreenState;

public class Parsec extends Game {
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	private ScreenNavigation navigator;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font/kenney_future.fnt"));
		shapeRenderer = new ShapeRenderer();

		navigator = new ScreenNavigation(this);
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
		this.screen.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public void navigateTo(ScreenState nextScreen) {
		navigator.setState(nextScreen);
		navigator.changeScreen();
	}
}
