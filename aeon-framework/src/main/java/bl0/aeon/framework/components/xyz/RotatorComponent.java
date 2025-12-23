package bl0.aeon.framework.components.xyz;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RotatorComponent extends BaseComponent implements UpdateConsumerComponent {
    public Vector3f rotationVector;
    public Transform orbit;

    public RotatorComponent(Vector3f rotationVector, Transform orbit) {
        this.rotationVector = rotationVector;
        this.orbit = orbit;
    }

    public RotatorComponent(Vector3f rotationVector) {
        this.rotationVector = rotationVector;
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return;

        Transform self = parent.getComponent(Transform.class);
        float dt = (float) fCtx.getDeltaTime();

        // if no target - spin
        if (orbit == null) {
            Quaternionf rot = new Quaternionf(self.getRotation());
            rot.rotateXYZ(
                    rotationVector.x * dt,
                    rotationVector.y * dt,
                    rotationVector.z * dt
            );
            self.setRotation(rot);
            return;
        }

        Vector3f axis = new Vector3f(rotationVector);
        float speed = axis.length();
        if (speed < 1e-8f) return;
        axis.div(speed);

        float angle = speed * dt;

        Vector3f center = orbit.getPosition();
        Vector3f offset = new Vector3f(self.getPosition()).sub(center);

        new Quaternionf().fromAxisAngleRad(axis, angle).transform(offset);

        self.getPosition().set(center).add(offset);
    }
}
