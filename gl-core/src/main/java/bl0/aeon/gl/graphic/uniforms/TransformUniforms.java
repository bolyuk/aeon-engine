package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.render.common.data.render.IRenderable;

public class TransformUniforms {
    public static void setUniforms(IRenderable obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.MODEL, obj.getMatrix());
    }
}
