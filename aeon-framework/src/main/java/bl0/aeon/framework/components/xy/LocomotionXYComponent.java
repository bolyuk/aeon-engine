package bl0.aeon.framework.components.xy;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.InputConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.engine.data.component.BaseComponent;
import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LocomotionXYComponent extends BaseComponent implements InputConsumerComponent {

    @Override
    public boolean onInput(InputData data, IEngineContext ctx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return false;

        float delta = (float) ctx.getFrameContext().getDeltaTime();
        var transform = parent.getComponent(Transform.class);

        Vector3f move = new Vector3f();

        if (data.isKeyDown(Key.W)) move.z += 1; // forward
        if (data.isKeyDown(Key.S)) move.z -= 1; // back
        if (data.isKeyDown(Key.D)) move.x += 1; // right
        if (data.isKeyDown(Key.A)) move.x -= 1; // left

        if (move.lengthSquared() > 0) {
            move.normalize();
            transform.getRotation().transform(move);
            move.mul(delta);
            transform.getPosition().add(move);
        }

        float rotSpeed = 2.5f * delta;
        Quaternionf rot = new Quaternionf(transform.getRotation());

        if (data.isKeyDown(Key.Q)) rot.rotateY(-rotSpeed);
        if (data.isKeyDown(Key.E)) rot.rotateY(rotSpeed);

        transform.setRotation(rot);

        return true;
    }

}
