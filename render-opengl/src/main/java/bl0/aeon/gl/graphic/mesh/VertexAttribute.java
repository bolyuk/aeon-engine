package bl0.aeon.gl.graphic.mesh;

import org.lwjgl.opengl.GL11;

/**
 * One vertex attribute. The plain form ({@code new VertexAttribute(3)}) is {@code amount} tightly packed floats; the typed form
 * describes an attribute inside an interleaved byte buffer (see {@link GLMesh#GLMesh(byte[], int, java.util.List, String)}).
 */
public class VertexAttribute {
    public final int amount;
    /** GL component type (GL_FLOAT, GL_SHORT, GL_UNSIGNED_BYTE ...). */
    public final int glType;
    /** Integer types only: map to 0..1 / -1..1 instead of keeping the raw integer value. */
    public final boolean normalized;
    /** Byte offset inside the vertex (typed form only). */
    public final int byteOffset;

    public VertexAttribute(int amount) {
        this(amount, GL11.GL_FLOAT, false, -1);
    }

    public VertexAttribute(int amount, int glType, boolean normalized, int byteOffset) {
        this.amount = amount;
        this.glType = glType;
        this.normalized = normalized;
        this.byteOffset = byteOffset;
    }
}
