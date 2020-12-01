package dev.scottblechman.parsec.listeners;

import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.screens.level.LevelViewModel;

public class ProjectileListener implements ContactListener {

    private final LevelViewModel viewModel;

    public ProjectileListener(LevelViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void beginContact(Contact contact) {
        // We abstract the "colliding" and "target" entities in the contact to account for either one being
        // fixture A or B in the object.
        Fixture target;

        // We can skip any collisions that don't involve the projectile (i.e. moon and sun in tutorial levels)
        if(contact.getFixtureA().getUserData() == EntityType.PROJECTILE) {
            target = contact.getFixtureB();
        } else if(contact.getFixtureB().getUserData() == EntityType.PROJECTILE) {
            target = contact.getFixtureA();
        } else {
            return;
        }

        // If we should always advance, any projectile collision counts
        if(viewModel.shouldAlwaysAdvance()) {
            viewModel.finishLevel();
            return;
        }

        switch((EntityType) target.getUserData()) {
            case SUN:
            case MOON:
            case BARRIER:
                if(!viewModel.isLevelFinished()) {
                    viewModel.reset(true);
                }
                break;
            case TARGET_MOON:
                viewModel.finishLevel();
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
