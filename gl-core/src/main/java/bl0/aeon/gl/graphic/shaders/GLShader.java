package bl0.aeon.gl.graphic.shaders;

import bl0.aeon.common.data.component.graphic.Shader;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.CoreException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL20;

public class GLShader extends GLResource implements Shader {
    public final ShaderType type;
    public final String source;

    public GLShader(@NotNull ShaderType type, @NotNull String data, @NotNull String name) {
        super(GL20.glCreateShader(type == ShaderType.VERTEX ? 35633 : 35632), name);
        this.source = data;
        this.type = type;
        GL20.glShaderSource(ID, (CharSequence)data);
        GL20.glCompileShader(ID);
        if (GL20.glGetShaderi(ID, 35713) == 0) {
            this.dispose();
            throw new CoreException("Error compiling shader: " + GL20.glGetShaderInfoLog(ID, 1024));
        }
    }

    @Override
    public void dispose() {
        GL20.glDeleteShader(ID);
    }

    public enum ShaderType {
        VERTEX,
        FRAGMENT;

    }
}

