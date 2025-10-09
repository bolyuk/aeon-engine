/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package org.bl0.aeon.core.graphic.shaders;

import org.bl0.aeon.engine.interfaces.BaseGLResource;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL20;

public class Shader
extends BaseGLResource {
    public final ShaderType type;
    public final String source;

    public Shader(@NotNull ShaderType type, @NotNull String data) {
        if (type == null) {
            Shader.$$$reportNull$$$0(0);
        }
        if (data == null) {
            Shader.$$$reportNull$$$0(1);
        }
        super(GL20.glCreateShader(type == ShaderType.VERTEX ? 35633 : 35632));
        this.source = data;
        this.type = type;
        GL20.glShaderSource(this.ID, (CharSequence)data);
        GL20.glCompileShader(this.ID);
        if (GL20.glGetShaderi(this.ID, 35713) == 0) {
            this.dispose();
            throw new RuntimeException("Error compiling shader: " + GL20.glGetShaderInfoLog(this.ID, 1024));
        }
    }

    @Override
    protected void disposeCall() {
        GL20.glDeleteShader(this.ID);
    }

    @Override
    protected void bindCall() {
    }

    @Override
    protected void unbindCall() {
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[0] = "type";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[0] = "data";
                break;
            }
        }
        objectArray[1] = "org/bl0/aeon/aeon/core/graphic/data/shaders/Shader";
        objectArray[2] = "<init>";
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }

    public static enum ShaderType {
        VERTEX,
        FRAGMENT;

    }
}

