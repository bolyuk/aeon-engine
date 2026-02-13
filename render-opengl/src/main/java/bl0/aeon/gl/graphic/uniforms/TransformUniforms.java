package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.render.api.data.render.scene.ISingleRenderable;

public class TransformUniforms {
    public static void setUniforms(ISingleRenderable obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.MODEL, obj.getMatrix());
    }
}
