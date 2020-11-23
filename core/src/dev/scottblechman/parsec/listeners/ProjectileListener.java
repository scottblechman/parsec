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
        // If we should always advance, any projectile collision counts
        if(viewModel.shouldAlwaysAdvance() && (contact.getFixtureA().getUserData() == EntityType.PROJECTILE || contact.getFixtureB().getUserData() == EntityType.PROJECTILE)) {
            viewModel.nextLevel();
            return;
        }

        // Handle barrier collisions separately
        if(contact.getFixtureA().getUserData() == EntityType.BARRIER && contact.getFixtureB().getUserData() == EntityType.PROJECTILE) {
            viewModel.reset(true);
            return;
        }

        switch((EntityType) contact.getFixtureB().getUserData()) {
            case SUN:
            case MOON:
            case BARRIER:
                viewModel.reset(true);
                break;
            case TARGET_MOON:
                if(contact.getFixtureA().getUserData() == EntityType.PROJECTILE) {
                    viewModel.nextLevel();
                }
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
