package bl0.aeon.render.api.data.render.scene;

import org.joml.Vector3f;

public interface IBoundedRenderable {
    Vector3f getBoundsMin();
    Vector3f getBoundsMax();

    default boolean isCulled() {
        return false;
    }
}
