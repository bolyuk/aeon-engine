package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.render.api.base.IResource;
import bl0.aeon.render.api.resource.Texture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;

public class GLCharacter extends GLResource implements IBindable, Texture {
    public final int width;
    public final int height;

    public final int advance;

    public final Vector2f size;
    public final Vector2f bearing;
    public GLCharacter(String name, int width, int height, Vector2f size, int advance, Vector2f bearing, ByteBuffer data) {
        super(GL33.glGenTextures(), name);
        this.width = width;
        this.height = height;
        this.size = size;
        this.advance = advance;
        this.bearing = bearing;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        int tw = Math.max(width, 1);
        int th = Math.max(height, 1);

        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL30.GL_R8,
                tw,
                th,
                0,
                GL11.GL_RED,
                GL11.GL_UNSIGNED_BYTE,
                (width <= 0 || height <= 0) ? (ByteBuffer) null : data
        );

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override public void bind() { GL11.glBindTexture(GL11.GL_TEXTURE_2D, ID); }
    @Override public void unbind() { GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); }
    @Override public void dispose() { GL11.glDeleteTextures(ID); }
    @Override public IResource makeCopy(String name) { throw new UnsupportedOperationException("Glyph textures not copyable"); }
}

