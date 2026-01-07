package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Texture;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class GLTexture extends GLResource implements IBindable, Texture {
    protected final int width;
    protected final int height;
    protected final ByteBuffer data;

    public GLTexture(String name, int width, int height, ByteBuffer data) {
        super(GL11.glGenTextures(), name);
        this.width = width;
        this.height = height;
        this.data = data;

        GL11.glBindTexture(GL_TEXTURE_2D, ID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);

        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void dispose() {
        GL11.glDeleteTextures(ID);
    }

    @Override
    public IResource makeCopy(String name) {
        return new GLTexture(name, width, height, data);
    }

    @Override
    public void bind() {
        GL30.glBindTexture(GL_TEXTURE_2D, ID);
    }

    @Override
    public void unbind() {
        GL30.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
