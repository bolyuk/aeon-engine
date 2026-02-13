package bl0.aeon.render.api.data.light;

import org.joml.Vector3f;

public interface DirectionalLight extends Light {
    Vector3f getDirection();
}
