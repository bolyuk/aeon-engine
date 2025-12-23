package bl0.aeon.engine.data.render;

import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.ISingleRenderable;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;

public class RenderObj implements ISingleRenderable {

    private final ShaderProgram shaderProgram;
    private final Matrix4f transform;
    private final Vector4f color;
    private final DirectionalLight directionalLight;
    private final List<PointLight> pointLights;
    private final Mesh mesh;
    private final Texture texture;
    private final boolean isDepthTest;

    public  RenderObj(ShaderProgram shaderProgram,
                      Vector4f color,
                      Mesh mesh,
                      Matrix4f transform,
                      DirectionalLight directionalLight,
                      List<PointLight> pointLights, Texture texture, boolean isDepthTest) {
        this.shaderProgram = shaderProgram;
        this.transform = transform;
        this.color = color;
        this.mesh = mesh;
        this.directionalLight = directionalLight;
        this.pointLights = pointLights;
        this.texture = texture;
        this.isDepthTest = isDepthTest;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public List<PointLight> getPointLights() {
        return pointLights;
    }

    @Override
    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    @Override
    public Matrix4f getMatrix() {
        return transform;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public boolean isDepthTestEnabled() {
        return isDepthTest;
    }
}
