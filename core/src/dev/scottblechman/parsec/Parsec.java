package dev.scottblechman.parsec;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.scottblechman.parsec.data.SoundService;
import dev.scottblechman.parsec.state.ScoreService;
import dev.scottblechman.parsec.state.ScreenNavigation;
import dev.scottblechman.parsec.state.enums.ScreenState;

public class Parsec extends Game {
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	private ScreenNavigation navigator;
	private ScoreService scoreService;
	private SoundService soundService;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font/kenney_future.fnt"));
		shapeRenderer = new ShapeRenderer();

		scoreService = new ScoreService();
		soundService = new SoundService();
		navigator = new ScreenNavigation(this);
		soundService.playMusic();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
		this.screen.dispose();
		soundService.dispose();
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

	public ScoreService getScoreService() {
		return scoreService;
	}

	public SoundService getSoundService() {
		return soundService;
	}

	public void navigateTo(ScreenState nextScreen) {
		navigator.setState(nextScreen);
		navigator.changeScreen();
	}
}
