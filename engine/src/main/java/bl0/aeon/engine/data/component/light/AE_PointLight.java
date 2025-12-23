package bl0.aeon.engine.data.component.light;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.engine.data.component.AE_Transform;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Vector3f;

public class AE_PointLight extends BaseComponent implements PointLight {
    public Vector3f diffuse;
    public Vector3f specular;
    public Vector3f ambient;
    public float constant;
    public float linear;
    public float quadratic;

    public AE_PointLight(Vector3f diffuse, Vector3f specular, Vector3f ambient, float constant, float linear, float quadratic) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.ambient = ambient;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
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

    @Override
    public float getConstant() {
        return constant;
    }

    @Override
    public float getLinear() {
        return linear;
    }

    @Override
    public float getQuadratic() {
        return quadratic;
    }

    @Override
    public Vector3f getPosition() {
        if(parent != null && parent.hasComponent(Transform.class))
            return parent.getComponent(Transform.class).getPosition();
        return null;
    }
}
