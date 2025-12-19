package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.common.data.component.Transform;
import bl0.aeon.gl.graphic.GLShaderProgram;

public class TransformUniforms {
    public static void setUniforms(Transform obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform("model", obj.getMatrix());
    }
}
