package dev.scottblechman.parsec.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.screens.level.LevelViewModel;

public class ProjectileListener implements ContactListener {

    private final LevelViewModel viewModel;

    public ProjectileListener(LevelViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void beginContact(Contact contact) {
        switch((EntityType) contact.getFixtureB().getUserData()) {
            case SUN:
                viewModel.reset(true);
                break;
            case PROJECTILE:
            case UNDEFINED:
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Method not used
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Method not used
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Method not used
    }
}