package bl0.aeon.gl.graphic.mesh;

import java.util.List;

import bl0.aeon.gl.c.Sizes;
import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;

import org.lwjgl.opengl.GL30;

public class GLMesh extends GLResource implements IBindable, Mesh {
    public final int vboID;
    public final int vaoID;
    public final List<VertexAttribute> vertexAttributes;
    public final float[] vertecies;
    public final int vertexCount;

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
        GL30.glDrawArrays(4, 0, this.vertexCount);
    }
}

