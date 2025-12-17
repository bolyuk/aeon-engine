package bl0.aeon.gl.graphic;

import bl0.aeon.common.data.component.graphic.Material;
import bl0.aeon.gl.graphic.shaders.GLShaderProgram;
import org.joml.Vector4f;

public class GLMaterial implements Material {
    public GLShaderProgram shaderProgram;
    public Vector4f color;
}

