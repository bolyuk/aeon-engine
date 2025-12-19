package bl0.aeon.render.common.data.light;

public interface PointLight extends Light {
    float getConstant();
    float getLinear();
    float getQuadratic();
}
