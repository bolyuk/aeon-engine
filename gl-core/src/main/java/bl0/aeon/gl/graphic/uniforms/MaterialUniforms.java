package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.graphic.GLMaterial;
import bl0.aeon.gl.graphic.shaders.GLShaderProgram;

public class MaterialUniforms {
    public static void setUniforms(GLMaterial obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform("color", obj.color);
    }
}
