package bl0.aeon.render.common.data.render;

import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;

public interface IRenderable {
    ShaderProgram getShaderProgram();
    Vector4f getColor();
    Texture getTexture();

    List<PointLight> getPointLights();
    DirectionalLight getDirectionalLight();

    Matrix4f getMatrix();

    Mesh getMesh();
}
