package bl0.aeon.gl.graphic;

import java.nio.FloatBuffer;
import java.util.HashMap;

import bl0.aeon.render.api.base.IResource;
import bl0.aeon.render.api.resource.ShaderProgram;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.gl.base.CoreException;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL30;

public class GLShaderProgram extends GLResource implements IBindable, ShaderProgram {
    private final GLShader vertexShader;
    private final GLShader fragmentShader;
    private final FloatBuffer matrixBuffer;

    private final HashMap<String, Integer> indexes;
    private final HashMap<String, Integer> uniforms;

    public GLShaderProgram(@NotNull String vertexShader, @NotNull String fragmentShader, @NotNull String name) {
        super(GL30.glCreateProgram(), name);
        this.matrixBuffer = BufferUtils.createFloatBuffer(16);
        this.indexes = new HashMap<>();
        this.uniforms = new HashMap<>();
        this.vertexShader = new GLShader(ShaderType.VERTEX, vertexShader, name+"_vertex_inner");
        this.fragmentShader = new GLShader(ShaderType.FRAGMENT, fragmentShader, name+"_fragment_inner");;

        GL30.glAttachShader(this.ID, this.vertexShader.ID);
        GL30.glAttachShader(this.ID, this.fragmentShader.ID);
        GL30.glLinkProgram(this.ID);
        GL30.glDetachShader(this.ID, this.vertexShader.ID);
        GL30.glDetachShader(this.ID, this.fragmentShader.ID);

        if (GL30.glGetProgrami(this.ID, 35714) == 0) {
            this.dispose();
            throw new CoreException("Could not link shader program: " + GL30.glGetProgramInfoLog(this.ID, 1024));
        }
    }

    public void setUniform(String name, int value) {
        name = this.replacePlaceHolders(name);
        GL30.glUniform1i(this.getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        name = this.replacePlaceHolders(name);
        GL30.glUniform1f(this.getUniformLocation(name), value);
    }

    public void setUniform(String name, Vector3f value) {
        name = this.replacePlaceHolders(name);
        GL30.glUniform3f(this.getUniformLocation(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        name = this.replacePlaceHolders(name);
        GL30.glUniform4f(this.getUniformLocation(name), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Matrix4f value) {
        name = this.replacePlaceHolders(name);
        this.matrixBuffer.clear();
        GL30.glUniformMatrix4fv(this.getUniformLocation(name), false, value.get(this.matrixBuffer));
    }

    @Override
    public void dispose() {
        GL30.glDeleteProgram(this.ID);
        this.matrixBuffer.clear();
        this.vertexShader.dispose();
        this.fragmentShader.dispose();
    }

    @Override
    public void bind() {
        GL30.glUseProgram(this.ID);
    }

    @Override
    public void unbind() {
        GL30.glUseProgram(0);
        this.indexes.clear();
    }

    public void incrementIndex(String pointer) {
        int index = this.indexes.getOrDefault(pointer, 0);
        this.indexes.put(pointer, index + 1);
    }

    public void resetIndex(String pointer) {
        this.indexes.put(pointer, 0);
    }

    private String replacePlaceHolders(String data) {
        if (data.contains("$index")) {
            String pointer = data.substring(0, data.indexOf("["));
            int index = this.indexes.getOrDefault(pointer, 0);
            this.indexes.put(pointer, index);
            return data.replace("$index", String.valueOf(index));
        }
        return data;
    }

    private int getUniformLocation(String name) {
        Integer location = this.uniforms.get(name);
        if (location == null) {
            location = GL30.glGetUniformLocation(this.ID, name);
            this.uniforms.put(name, location);
        }
        return location;
    }

    @Override
    public IResource makeCopy(String name) {
        return new GLShaderProgram(this.vertexShader.source, this.fragmentShader.source, name);
    }

    private final class GLShader extends GLResource  {
        public final ShaderType type;
        public final String source;

        public GLShader(@NotNull ShaderType type, @NotNull String data, @NotNull String name) {
            super(GL30.glCreateShader(type == ShaderType.VERTEX ? 35633 : 35632), name);
            this.source = data;
            this.type = type;
            GL30.glShaderSource(ID, data);
            GL30.glCompileShader(ID);
            if (GL30.glGetShaderi(ID, 35713) == 0) {
                this.dispose();
                throw new CoreException("Error compiling shader: " + GL30.glGetShaderInfoLog(ID, 1024));
            }
        }

        @Override
        public void dispose() {
            GL30.glDeleteShader(ID);
        }

        @Override
        public IResource makeCopy(String name) {
            return new  GLShader(this.type, this.source, name);
        }
    }


    private enum ShaderType {
        VERTEX,
        FRAGMENT;

    }
}

