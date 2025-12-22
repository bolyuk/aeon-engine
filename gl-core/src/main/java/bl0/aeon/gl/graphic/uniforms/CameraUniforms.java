package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.render.common.data.render.Camera;
import bl0.aeon.gl.graphic.GLShaderProgram;

public class CameraUniforms {

    public static void setUniforms(Camera obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.Camera.PROJECTION, obj.getProjectionMatrix());
        shaderProgram.setUniform(Uniforms.Camera.VIEW, obj.getViewMatrix());
        shaderProgram.setUniform(Uniforms.Camera.VIEW_POS, obj.getPosition());
    }
}
