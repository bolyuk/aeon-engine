package bl0.aeon.engine.component.light;

import bl0.aeon.common.data.component.light.DirectionalLight;
import org.joml.Vector3f;

public class AE_DirectionalLight implements DirectionalLight {
    public Vector3f direction;
    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;

    public AE_DirectionalLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular) {
        this.direction = direction;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    @Override
    public Vector3f getDirection() {
        return direction;
    }

    @Override
    public Vector3f getAmbient() {
        return ambient;
    }

    @Override
    public Vector3f getDiffuse() {
        return diffuse;
    }

    @Override
    public Vector3f getSpecular() {
        return specular;
    }
}
