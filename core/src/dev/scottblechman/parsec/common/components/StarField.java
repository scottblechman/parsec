package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.interfaces.IComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StarField implements IComponent {
    private final ArrayList<Vector3> starFieldPool;
    private final Random random;

    public StarField() {
        this.starFieldPool = new ArrayList<>();
        random = new Random();

        for(int i = 0; i < Constants.Graphics.STAR_FIELD_SIZE; i++) {
            if(random.nextInt(100) < Constants.Graphics.STAR_SPAWN_CHANCE) {
                spawnStar();
            }
        }
    }

    private void spawnStar() {
        int x = random.nextInt(Constants.Camera.VIEWPORT_WIDTH);
        int y = random.nextInt(Constants.Camera.VIEWPORT_HEIGHT);
        // Use z component to store lifespan
        int z = random.nextInt(Constants.Graphics.STAR_BURNOUT_MAX - Constants.Graphics.STAR_BURNOUT_MIN) + Constants.Graphics.STAR_BURNOUT_MIN;
        starFieldPool.add(new Vector3(x, y, z));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        // Intentionally left empty.
    }

    @Override
    public void update() {
        Iterator<Vector3> itr = starFieldPool.iterator();
        while (itr.hasNext()) {
            Vector3 star = itr.next();
            if (star.z < 0) {
                itr.remove();
            } else {
                star.x--;
                star.z--;
            }
        }
        // If there is room, roll a chance to add a new star
        if(starFieldPool.size() < Constants.Graphics.STAR_FIELD_SIZE && random.nextInt(100) < Constants.Graphics.STAR_SPAWN_CHANCE) {
            spawnStar();
        }
    }

    public List<Vector3> getPool() {
        return starFieldPool;
    }
}
