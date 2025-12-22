package bl0.aeon.render.common.data.light;

import org.joml.Vector3f;

public interface PointLight extends Light {
    float getConstant();
    float getLinear();
    float getQuadratic();
    Vector3f getPosition();
}
