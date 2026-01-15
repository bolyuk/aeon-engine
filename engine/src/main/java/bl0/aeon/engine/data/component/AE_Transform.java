package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.render.common.c.Vectors;
import bl0.bjs.common.core.relations.BoundObject;
import bl0.bjs.common.core.relations.NotifyObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AE_Transform extends BaseComponent implements Transform {
    public final NotifyObject<Vector3f> position = new NotifyObject<Vector3f>(new Vector3f());
    public final NotifyObject<Vector3f> scale = new NotifyObject<Vector3f>(Vectors.ONE());
    public final NotifyObject<Quaternionf> rotation = new NotifyObject<Quaternionf>(new Quaternionf());

    public transient final BoundObject<Matrix4f> matrix = new BoundObject<Matrix4f>(new Matrix4f(), (e) -> new Matrix4f().translate(this.position.get()).rotate(this.rotation.get()).scale(this.scale.get()), this.position, this.rotation, this.scale);

    @Override
    public Matrix4f getMatrix(){
        return this.matrix.get();
    }

    @Override
    public Vector3f getPosition() {
        return position.get();
    }

    @Override
    public Quaternionf getRotation() {
        return rotation.get();
    }

    @Override
    public void setRotation(Quaternionf q) {
        rotation.set(q);
    }

    @Override
    public void setPosition(Vector3f pos) {
        position.set(pos);
    }
}

