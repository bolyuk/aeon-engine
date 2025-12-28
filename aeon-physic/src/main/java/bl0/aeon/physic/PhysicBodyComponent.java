package bl0.aeon.physic;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import bl0.aeon.render.common.c.Vectors;
import org.joml.Vector3f;

public class PhysicBodyComponent extends BaseComponent implements UpdateConsumerComponent {

    public Vector3f velocity = new Vector3f();
    public float mass;
    public float damping = 1f;

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return;
        if(velocity.equals(Vectors.ZERO())) return;

        var t = parent.getComponent(Transform.class);
        float dt = (float) fCtx.getDeltaTime();

        // position += v * dt
        t.setPosition(t.getPosition().fma(dt, velocity));
        velocity.mul((float) Math.pow(damping, dt * 60f));
    }
}
