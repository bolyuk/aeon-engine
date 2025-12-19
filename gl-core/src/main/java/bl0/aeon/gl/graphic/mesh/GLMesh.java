package bl0.aeon.gl.graphic.mesh;

import java.util.List;

import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30C;

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
        this.vaoID = GL30C.glGenVertexArrays();
        GL30C.glBindVertexArray(this.vaoID);
        this.vboID = GL30C.glGenBuffers();
        GL30C.glBindBuffer(34962, this.vboID);
        GL30C.glBufferData(34962, vertices, 35044);
        if (vertexAttributes == null || vertexAttributes.isEmpty()) {
            GL20.glVertexAttribPointer(0, 3, 5126, false, 12, 0L);
            GL20.glEnableVertexAttribArray(0);
        } else {
            int stride = totalAttributeSize * 4;
            int offset = 0;
            for (int i = 0; i < vertexAttributes.size(); ++i) {
                VertexAttribute attribute = vertexAttributes.get(i);
                GL20.glVertexAttribPointer(i, attribute.amount, 5126, false, stride, (long)offset * 4L);
                GL20.glEnableVertexAttribArray(i);
                offset += attribute.amount;
            }
        }
        GL30C.glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        GL30C.glDeleteBuffers(this.vboID);
        GL30C.glDeleteVertexArrays(this.vaoID);
    }

    @Override
    public void bind() {
        GL30C.glBindVertexArray(this.vaoID);
    }

    @Override
    public void unbind() {
        GL30C.glBindVertexArray(0);
    }

    @Override
    public IResource makeCopy(String name) {
        return new GLMesh(this.vertecies, this.vertexAttributes, name);
    }

    public void draw(){
        GL30C.glDrawArrays(4, 0, this.vertexCount);
    }
}

