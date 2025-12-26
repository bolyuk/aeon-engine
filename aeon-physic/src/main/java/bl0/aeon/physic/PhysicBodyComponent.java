package bl0.aeon.physic;

import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Vector3f;

public class PhysicBodyComponent extends BaseComponent implements UpdateConsumerComponent {
    public Vector3f velocity = new Vector3f();

    public float mass;

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {

    }
}
