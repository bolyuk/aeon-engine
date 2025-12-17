package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.common.behavior.Camera;
import bl0.aeon.gl.graphic.shaders.GLShaderProgram;

public class CameraUniforms {

    public static void setUniforms(Camera obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform("projection", obj.getProjectionMatrix());
        shaderProgram.setUniform("view", obj.getViewMatrix());
        shaderProgram.setUniform("viewPos", obj.getPosition());
    }
}
