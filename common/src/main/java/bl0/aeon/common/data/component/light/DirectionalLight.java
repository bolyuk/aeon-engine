package bl0.aeon.common.data.component.light;

import org.joml.Vector3f;

public interface DirectionalLight extends Light {
    Vector3f getDirection();
}
