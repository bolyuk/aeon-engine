package bl0.aeon.gl.graphic.mesh;

import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Mesh;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
public class GLTextMesh extends GLResource implements IBindable, Mesh {

    public final int vaoID;
    public final int vboID;

    // 6 vertices * (x, y, u, v) = 24 floats
    private static final int FLOATS_PER_VERTEX = 4;
    private static final int VERTICES = 6;
    private static final int FLOATS_TOTAL = VERTICES * FLOATS_PER_VERTEX;
    private static final int BYTES_TOTAL = FLOATS_TOTAL * Float.BYTES;

    public GLTextMesh(String name) {
        super(-1, name);

        vaoID = GL30.glGenVertexArrays();
        vboID = GL30.glGenBuffers();

        GL30.glBindVertexArray(vaoID);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, BYTES_TOTAL, GL30.GL_DYNAMIC_DRAW);

        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(0, 4, GL30.GL_FLOAT, false, 4 * Float.BYTES, 0L);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void bind() {
        GL30.glBindVertexArray(vaoID);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
    }

    @Override
    public void unbind() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void updateVertices(FloatBuffer vb) {
        GL33.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vb);
    }

    public void draw() {
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, VERTICES);
    }

    @Override
    public void dispose() {
        GL30.glDeleteBuffers(vboID);
        GL30.glDeleteVertexArrays(vaoID);
    }

    @Override
    public IResource makeCopy(String name) {
        return new GLTextMesh(name);
    }
}
