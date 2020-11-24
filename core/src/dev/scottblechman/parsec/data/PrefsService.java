package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefsService {

    private final Preferences prefs;

    public PrefsService() {
        this.prefs = Gdx.app.getPreferences("parsec");
    }

    public boolean showTutorial() {
        // If we haven't already, do the tutorial; we'll set the flag afterwards.
        return prefs.getBoolean("show-tutorial", true);
    }

    public void markTutorialDone() {
        prefs.putBoolean("show-tutorial", false);
        prefs.flush();
    }
}
