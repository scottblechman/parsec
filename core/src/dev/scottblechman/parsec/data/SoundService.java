package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundService {

    private Sound sound;

    // SFX ids for recall
    private long idExplosion;
    private long idTypewriter;
    private long idButton;

    public void playExplosionSFX() {
        if(sound != null) {
            sound.stop(idExplosion);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.ogg"));
        idExplosion = sound.play(1.0f);
    }

    public void playTypewriterSFX() {
        if(sound != null) {
            sound.stop(idTypewriter);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/type.ogg"));
        idTypewriter = sound.play(1.0f);
        sound.setLooping(idTypewriter, true);
    }

    public void stopTypewriterSFX() {
        if(sound != null) {
            sound.setLooping(idTypewriter, false);
            sound.stop(idTypewriter);
        }
    }

    public void playButtonSFX() {
        if(sound != null) {
            sound.stop(idButton);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/button.ogg"));
        idButton = sound.play(1.0f);
    }

    public void dispose() {
        if(sound != null) {
            sound.dispose();
        }
    }
}
