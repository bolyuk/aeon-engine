package bl0.aeon.render.common.data.render;

import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import org.joml.Vector4f;

public interface IRenderable {
    ShaderProgram getShaderProgram();
    Vector4f getColor();
    Mesh getMesh();
}
