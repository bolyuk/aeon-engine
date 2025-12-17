package bl0.aeon.common.data.component.light;

public interface PointLight extends Light {
    float getConstant();
    float getLinear();
    float getQuadratic();
}
