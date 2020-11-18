package dev.scottblechman.parsec.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.models.interfaces.IEntity;

import java.util.Random;

public class Moon implements IEntity {
    private final Entity entity;
    int radius;

    // Movement vectors
    Vector2 pivotPoint;
    Vector2 direction;

    // Preserves orbital motion
    DistanceJoint joint;

    public Moon(World world, int radius, boolean target) {
        this.radius = radius;
        this.direction = new Vector2();
        this.pivotPoint = new Vector2();

        // Fetch star from world for initial calculations
        Body star = null;
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body b : bodies) {
            EntityType type = (EntityType) b.getUserData();

            if (type == EntityType.SUN) {
                star = b;
                pivotPoint = b.getPosition();
                break;
            }
        }

        int theta = new Random().nextInt(360);
        assert star != null;
        this.entity = new Entity.Builder(world)
                .position(new Vector2(star.getPosition().x + (radius * (float) Math.cos(theta)), star.getPosition().y + (radius * (float) Math.sin(theta))))
                .radius(Constants.Entities.MOON_RADIUS)
                .bodyType(BodyDef.BodyType.DynamicBody)
                .entityType(target? EntityType.TARGET_MOON : EntityType.MOON)
                .isSensor(false)
                .build();

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(star, entity.getBody(), star.getPosition(), entity.getBody().getPosition());
        jointDef.collideConnected = true;

        joint = (DistanceJoint) world.createJoint(jointDef);
    }

    @Override
    public Body getBody() {
        return entity.getBody();
    }

    @Override
    public Vector2 getPosition() {
        return entity.getBody().getPosition();
    }

    public void update() {
        direction = entity.getBody().getPosition().sub(pivotPoint).rotate(90).nor();
        entity.getBody().applyForceToCenter(direction.cpy().scl(radius * Constants.Physics.MOON_FORCE_SCALAR), true);
    }
}
