/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.graphic;

import java.nio.ByteBuffer;
import org.bl0.aeon.engine.interfaces.BaseGLResource;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL30C;

public class Texture
extends BaseGLResource {
    public Texture(byte[] data, int width, int height) {
        super(GL11C.glGenTextures());
        GL11C.glBindTexture(3553, this.ID);
        GL11C.glTexParameteri(3553, 10242, 10497);
        GL11C.glTexParameteri(3553, 10243, 10497);
        GL11C.glTexParameteri(3553, 10241, 9729);
        GL11C.glTexParameteri(3553, 10240, 9729);
        GL11C.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, ByteBuffer.wrap(data));
        GL30C.glGenerateMipmap(3553);
    }

    @Override
    protected void disposeCall() {
        GL11C.glDeleteTextures(this.ID);
    }

    @Override
    protected void bindCall() {
        GL13C.glActiveTexture(33984);
        GL11C.glBindTexture(3553, this.ID);
    }

    @Override
    protected void unbindCall() {
    }
}

