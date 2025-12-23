package bl0.aeon.framework.components;

import bl0.aeon.base.component.UpdatableComponent;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BillboardComponent extends BaseComponent implements UpdatableComponent {

    private static final Quaternionf CORRECTION =
            new Quaternionf().rotateX((float) Math.toRadians(-90));

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if(parent == null || !parent.hasComponent(Transform.class)) return;

        var transform = parent.getComponent(Transform.class);

        var camPos = eCtx.getScene().getCamera().getPosition();
        var objPos = transform.getPosition();

        Vector3f dir = new Vector3f(camPos).sub(objPos).normalize();

        Quaternionf q = new Quaternionf()
                .lookAlong(dir.negate(), new Vector3f(0, 1, 0))
                .mul(CORRECTION);

        transform.setRotation(q);
    }
}
