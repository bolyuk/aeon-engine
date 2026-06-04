package bl0.aeon.engine.data.component;

import bl0.aeon.api.component.graphic.Transform;
import bl0.aeon.render.api.c.Vectors;
import bl0.bjs.common.core.relations.FactorizedObject;
import bl0.bjs.common.core.relations.ObservableObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AE_Transform extends BaseComponent implements Transform {
    public final ObservableObject<Vector3f> position = new ObservableObject<Vector3f>(new Vector3f());
    public final ObservableObject<Vector3f> scale = new ObservableObject<Vector3f>(Vectors.ONE());
    public final ObservableObject<Quaternionf> rotation = new ObservableObject<Quaternionf>(new Quaternionf());
    public final FactorizedObject<Matrix4f> matrix = new FactorizedObject<Matrix4f>(new Matrix4f(), (e) -> new Matrix4f().translate(this.position.get()).rotate(this.rotation.get()).scale(this.scale.get()), this.position, this.rotation, this.scale);

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

