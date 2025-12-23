package bl0.aeon.framework.components.xyz;

import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BillboardComponent extends BaseComponent implements UpdateConsumerComponent {

    private static final Quaternionf CORRECTION =
            new Quaternionf().rotateX((float) Math.toRadians(-90));
    private boolean needToBeDeleted = false;

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return;

        var t = parent.getComponent(Transform.class);

        Vector3f camPos = eCtx.getScene().getCamera().getPosition();
        Vector3f objPos = t.getPosition();

        Vector3f toCam = new Vector3f(camPos).sub(objPos);
        toCam.y = t.getRotation().y;

        if (toCam.lengthSquared() < 1e-8f) return;
        toCam.normalize();

        float yaw = (float) Math.atan2(toCam.x, toCam.z);

        Quaternionf q = new Quaternionf().rotateY(yaw).mul(CORRECTION);
        t.setRotation(q);

        if(needToBeDeleted)
            parent.removeComponent(this);
    }

    public BillboardComponent applyOnceAndRemove(){
        needToBeDeleted = true;
        return this;
    }
}
