package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundService {

    private Sound sound;
    private Music music;

    // SFX ids for recall
    private long idExplosion;
    private long idTypewriter;
    private long idButton;
    private long idDrag;

    public void playMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.ogg"));
        music.setLooping(true);
        music.play();
    }

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

    public void playDragSFX(float pitchShift) {
        if(sound != null) {
            sound.stop(idDrag);
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/drag.ogg"));
        idDrag = sound.play(1.0f);
        sound.setLooping(idDrag, true);
        sound.setPitch(idDrag, pitchShift);
    }

    public void stopDragSFX() {
        if(sound != null) {
            sound.setLooping(idDrag, false);
            sound.setPitch(idDrag, 1);
            sound.stop(idDrag);
        }
    }

    public void dispose() {
        if(sound != null) {
            sound.dispose();
        }
        if(music != null) {
            music.stop();
            music.dispose();
        }
    }
}
