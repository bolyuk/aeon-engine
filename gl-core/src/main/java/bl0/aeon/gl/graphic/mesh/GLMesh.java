package bl0.aeon.gl.graphic.mesh;

import java.nio.FloatBuffer;
import java.util.List;

import bl0.aeon.gl.c.Sizes;
import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

public class GLMesh extends GLResource implements IBindable, Mesh {
    public final int vboID;
    public final int vaoID;
    public final List<VertexAttribute> vertexAttributes;
    public final float[] vertecies;
    public final int vertexCount;

    private int instanceVboID = 0;
    private int instanceCapacity = 0;

    public GLMesh(float[] vertices, List<VertexAttribute> vertexAttributes, String name) {
        super(-1, name);
        this.vertexAttributes = vertexAttributes;
        this.vertecies = vertices;
        int totalAttributeSize = 0;
        totalAttributeSize = vertexAttributes != null ? vertexAttributes.stream().mapToInt(a -> a.amount).sum() : 3;
        this.vertexCount = vertices.length / totalAttributeSize;
        this.vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vaoID);
        this.vboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.vboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);
        if (vertexAttributes == null || vertexAttributes.isEmpty()) {
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 12, 0L);
            GL30.glEnableVertexAttribArray(0);
        } else {
            int stride = totalAttributeSize * Sizes.FLOAT;
            int offset = 0;
            for (int i = 0; i < vertexAttributes.size(); ++i) {
                VertexAttribute attribute = vertexAttributes.get(i);
                GL30.glVertexAttribPointer(i, attribute.amount, GL30.GL_FLOAT, false, stride, (long)offset * Sizes.FLOAT);
                GL30.glEnableVertexAttribArray(i);
                offset += attribute.amount;
            }
        }
        GL30.glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        GL30.glDeleteBuffers(this.vboID);
        if (instanceVboID != 0) GL30.glDeleteBuffers(instanceVboID);
        GL30.glDeleteVertexArrays(this.vaoID);
    }

    @Override
    public void bind() {
        GL30.glBindVertexArray(this.vaoID);
    }

    @Override
    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    @Override
    public IResource makeCopy(String name) {
        return new GLMesh(this.vertecies, this.vertexAttributes, name);
    }

    public void draw(){
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.vertexCount);
    }

    public void drawInstanced(int count) {
        if (instanceVboID == 0) throw new IllegalStateException("Instancing not enabled for mesh: " + name);
        GL33.glDrawArraysInstanced(GL30.GL_TRIANGLES, 0, this.vertexCount, count);
    }

    public void enableMatrixInstancing(int baseLocation, int initialCapacity) {
        if (instanceVboID != 0) return;

        instanceCapacity = Math.max(1, initialCapacity);

        // VAO должен быть активен, потому что мы настраиваем атрибуты
        GL30.glBindVertexArray(this.vaoID);

        instanceVboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, instanceVboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER,
                (long) instanceCapacity * 16L * Sizes.FLOAT,
                GL30.GL_DYNAMIC_DRAW);

        int stride = 16 * Sizes.FLOAT;
        long offsetBytes = 0;

        for (int col = 0; col < 4; col++) {
            int loc = baseLocation + col;
            GL30.glEnableVertexAttribArray(loc);
            GL30.glVertexAttribPointer(loc, 4, GL30.GL_FLOAT, false, stride, offsetBytes);
            GL33.glVertexAttribDivisor(loc, 1);
            offsetBytes += 4L * Sizes.FLOAT;
        }

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void uploadInstanceMatrices(FloatBuffer matrices, int count) {
        if (instanceVboID == 0) throw new IllegalStateException("Instancing not enabled for mesh: " + name);
        ensureInstanceCapacity(count);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, instanceVboID);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, matrices);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    private void ensureInstanceCapacity(int needed) {
        if (needed <= instanceCapacity) return;

        while (instanceCapacity < needed) instanceCapacity *= 2;

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, instanceVboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER,
                (long) instanceCapacity * 16L * Sizes.FLOAT,
                GL30.GL_DYNAMIC_DRAW);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }
}

