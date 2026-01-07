package bl0.aeon.render.common.data.light;

import org.joml.Vector3f;

public interface Light {
    Vector3f getAmbient();
    Vector3f getDiffuse();
    Vector3f getSpecular();
}
