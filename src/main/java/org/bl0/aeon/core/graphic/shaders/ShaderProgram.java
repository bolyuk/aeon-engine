/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package org.bl0.aeon.core.graphic.shaders;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.bl0.aeon.engine.interfaces.BaseGLResource;
import org.bl0.aeon.engine.interfaces.relations.IGetter;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class ShaderProgram
extends BaseGLResource {
    private final Shader vertexShader;
    private final Shader fragmentShader;
    private final FloatBuffer matrixBuffer;
    private final HashMap<String, Integer> indexes;
    private final HashMap<String, Integer> uniforms;

    public ShaderProgram(@NotNull Shader vertexShader, @NotNull Shader fragmentShader) {
        if (vertexShader == null) {
            ShaderProgram.$$$reportNull$$$0(0);
        }
        if (fragmentShader == null) {
            ShaderProgram.$$$reportNull$$$0(1);
        }
        super(GL20.glCreateProgram());
        this.matrixBuffer = BufferUtils.createFloatBuffer(16);
        this.indexes = new HashMap();
        this.uniforms = new HashMap();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        if (vertexShader.type != Shader.ShaderType.VERTEX) {
            throw new IllegalArgumentException("vertexShader type must be VERTEX");
        }
        if (fragmentShader.type != Shader.ShaderType.FRAGMENT) {
            throw new IllegalArgumentException("fragmentShader type must be FRAGMENT");
        }
        GL20.glAttachShader(this.ID, vertexShader.ID);
        GL20.glAttachShader(this.ID, fragmentShader.ID);
        GL20.glLinkProgram(this.ID);
        GL20.glDetachShader(this.ID, vertexShader.ID);
        GL20.glDetachShader(this.ID, fragmentShader.ID);
        if (GL20.glGetProgrami(this.ID, 35714) == 0) {
            this.dispose();
            throw new RuntimeException("Could not link shader program: " + GL20.glGetProgramInfoLog(this.ID, 1024));
        }
    }

    public void setUniform(String name, int value) {
        this.throwIfDisposed();
        this.throwIfNotBound();
        name = this.replacePlaceHolders(name);
        GL20.glUniform1i(this.getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        this.throwIfDisposed();
        this.throwIfNotBound();
        name = this.replacePlaceHolders(name);
        GL20.glUniform1f(this.getUniformLocation(name), value);
    }

    public <T> void setUniform(String name, IGetter<T> value) {
        T data = value.get();
        if (data instanceof Float) {
            Float f = (Float)data;
            this.setUniform(name, f.floatValue());
        } else if (data instanceof Integer) {
            Integer i = (Integer)data;
            this.setUniform(name, i);
        } else if (data instanceof Vector3f) {
            Vector3f v3 = (Vector3f)data;
            this.setUniform(name, v3);
        } else if (data instanceof Vector4f) {
            Vector4f v4 = (Vector4f)data;
            this.setUniform(name, v4);
        } else if (data instanceof Matrix4f) {
            Matrix4f m4 = (Matrix4f)data;
            this.setUniform(name, m4);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + String.valueOf(data.getClass()));
        }
    }

    public void setUniform(String name, Vector3f value) {
        this.throwIfDisposed();
        this.throwIfNotBound();
        name = this.replacePlaceHolders(name);
        GL20.glUniform3f(this.getUniformLocation(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        this.throwIfDisposed();
        this.throwIfNotBound();
        name = this.replacePlaceHolders(name);
        GL20.glUniform4f(this.getUniformLocation(name), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Matrix4f value) {
        this.throwIfDisposed();
        this.throwIfNotBound();
        name = this.replacePlaceHolders(name);
        GL20.glUniformMatrix4fv(this.getUniformLocation(name), false, value.get(this.matrixBuffer));
    }

    @Override
    protected void disposeCall() {
        GL20.glDeleteProgram(this.ID);
        this.vertexShader.dispose();
        this.fragmentShader.dispose();
    }

    @Override
    protected void bindCall() {
        GL20.glUseProgram(this.ID);
    }

    @Override
    protected void unbindCall() {
        GL20.glUseProgram(0);
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
            location = GL20.glGetUniformLocation(this.ID, name);
            this.uniforms.put(name, location);
        }
        return location;
    }

    public static ShaderProgram gen(String vertexShader, String fragmentShader) {
        return new ShaderProgram(new Shader(Shader.ShaderType.VERTEX, vertexShader), new Shader(Shader.ShaderType.FRAGMENT, fragmentShader));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[0] = "vertexShader";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[0] = "fragmentShader";
                break;
            }
        }
        objectArray[1] = "org/bl0/aeon/aeon/core/graphic/data/shaders/ShaderProgram";
        objectArray[2] = "<init>";
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

