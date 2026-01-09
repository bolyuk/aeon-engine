package bl0.aeon.gl.graphic.mesh.ui;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Mesh;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;

public class UIQuadMesh extends GLResource implements IBindable, Mesh {

    public final int vaoID;
    public final int vboID;

    // vertex = vec4 (x, y, u, v)
    private static final int FLOATS_PER_VERTEX = 4;
    private static final int VERTICES = 6;
    private static final int FLOATS_TOTAL = VERTICES * FLOATS_PER_VERTEX;
    private static final int BYTES_TOTAL = FLOATS_TOTAL * Float.BYTES;

    private final FloatBuffer vb; // 24 floats

    public UIQuadMesh(String name, FloatBuffer vb) {
        super(-1, name);
        this.vb = vb;

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

    /** pos = левый верх (если ortho2D(0,w,h,0)), size = (w,h) */
    public void draw(Vector2f pos, Vector2f size) {
        float x = pos.x;
        float y = pos.y;
        float w = size.x;
        float h = size.y;

        // UV полный квадрат (если потом будет текстура)
        float u0 = 0f, v0 = 0f;
        float u1 = 1f, v1 = 1f;

        vb.clear();

        // tri 1
        vb.put(x).put(y).put(u0).put(v0);
        vb.put(x).put(y + h).put(u0).put(v1);
        vb.put(x + w).put(y + h).put(u1).put(v1);

        // tri 2
        vb.put(x).put(y).put(u0).put(v0);
        vb.put(x + w).put(y + h).put(u1).put(v1);
        vb.put(x + w).put(y).put(u1).put(v0);

        vb.flip();

        GL33.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vb);
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, VERTICES);
    }

    @Override
    public void dispose() {
        GL30.glDeleteBuffers(vboID);
        GL30.glDeleteVertexArrays(vaoID);
    }

    @Override
    public IResource makeCopy(String name) {
        // копия ок, но vb тебе надо будет передать снаружи (или выделять новый)
        throw new UnsupportedOperationException("UIQuadMesh copy requires external FloatBuffer");
    }
}
