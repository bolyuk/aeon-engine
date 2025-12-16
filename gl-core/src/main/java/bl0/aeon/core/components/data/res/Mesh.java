/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.core.components.data.res;

import java.util.List;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.core.graphic.mesh.VertexAttribute;
import org.bl0.aeon.engine.interfaces.BaseGLResource;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.engine.interfaces.draw.IDrawable;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30C;

public class Mesh
extends BaseGLResource
implements IDrawable,
IComponent {
    public final int vboID;
    public final int vaoID;
    public final List<VertexAttribute> vertexAttributes;
    public final int vertexCount;

    public Mesh(float[] vertices, List<VertexAttribute> vertexAttributes) {
        super(-1);
        this.vertexAttributes = vertexAttributes;
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
    protected void disposeCall() {
        GL30C.glDeleteBuffers(this.vboID);
        GL30C.glDeleteVertexArrays(this.vaoID);
    }

    @Override
    protected void bindCall() {
        GL30C.glBindVertexArray(this.vaoID);
    }

    @Override
    protected void unbindCall() {
        GL30C.glBindVertexArray(0);
    }

    @Override
    public void draw(DrawContext drawCtx) {
        this.throwIfNotBound();
        this.throwIfDisposed();
        GL30C.glDrawArrays(4, 0, this.vertexCount);
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return Mesh.class;
    }
}

