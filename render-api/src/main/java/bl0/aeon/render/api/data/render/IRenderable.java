package bl0.aeon.render.api.data.render;

import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.render.api.resource.ShaderProgram;
import org.joml.Vector4f;

public interface IRenderable {
    ShaderProgram getShaderProgram();
    Vector4f getColor();
    Mesh getMesh();
}
