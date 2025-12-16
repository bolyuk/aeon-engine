/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.core.components.data;

import org.bl0.aeon.core.c.Vectors;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.IUniformsSetter;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.engine.interfaces.parsing.ILoadable;
import org.bl0.aeon.engine.relations.BoundObject;
import org.bl0.aeon.engine.relations.NotifyObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform
implements IComponent,
IUniformsSetter,
ILoadable {
    public final NotifyObject<Vector3f> position = new NotifyObject<Vector3f>(new Vector3f());
    public final NotifyObject<Vector3f> scale = new NotifyObject<Vector3f>(Vectors.ONE());
    public final NotifyObject<Quaternionf> rotation = new NotifyObject<Quaternionf>(new Quaternionf());
    public final BoundObject<Matrix4f> matrix = new BoundObject<Matrix4f>(new Matrix4f(), () -> new Matrix4f().translate(this.position.get()).rotate(this.rotation.get()).scale(this.scale.get()), this.position, this.rotation, this.scale);

    public Transform setPos(Vector3f pos) {
        this.position.set(pos);
        return this;
    }

    public Transform setScale(Vector3f scale) {
        this.scale.set(scale);
        return this;
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        program.setUniform("model", this.matrix);
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return Transform.class;
    }
}

