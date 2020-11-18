package dev.scottblechman.parsec.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.models.interfaces.IEntity;

public class Projectile implements IEntity {
    private final Entity entity;

    public Projectile(World world) {
        this.entity = new Entity.Builder(world)
                .position(Constants.Entities.PROJECTILE_INIT_POS)
                .radius(Constants.Entities.PROJECTILE_RADIUS)
                .bodyType(BodyDef.BodyType.DynamicBody)
                .entityType(EntityType.PROJECTILE)
                .isSensor(true)
                .build();
    }

    @Override
    public Body getBody() {
        return entity.getBody();
    }

    public Vector2 getPosition() {
        return entity.getPosition();
    }
}
