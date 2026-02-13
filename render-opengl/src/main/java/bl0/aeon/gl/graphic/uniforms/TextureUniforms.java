package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;

public class TextureUniforms {

    public static void setUniforms(GLTexture texture, GLShaderProgram shaderProgram) {
        if(texture != null) {
            shaderProgram.setUniform(Uniforms.TEXTURE, 0);
            shaderProgram.setUniform(Uniforms.USE_TEXTURE, 1);
        }
        else
            shaderProgram.setUniform(Uniforms.USE_TEXTURE, 0);
    }
}
