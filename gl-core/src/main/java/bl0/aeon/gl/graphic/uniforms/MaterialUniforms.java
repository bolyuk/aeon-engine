package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.graphic.GLShaderProgram;

public class MaterialUniforms {
    public static void setUniforms(Material obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform("color", obj.getColor());
    }
}
