package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundService {

    private Sound sound;

    // SFX id for recall
    private long id;

    public void playExplosionSFX() {
        if(sound != null) {
            sound.stop(id);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.ogg"));
        id = sound.play(1.0f);
    }

    public void playTypewriterSFX() {
        if(sound != null) {
            sound.stop(id);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/type.ogg"));
        id = sound.play(1.0f);
    }

    public void dispose() {
        if(sound != null) {
            sound.dispose();
        }
    }
}
