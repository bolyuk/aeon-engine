package bl0.aeon.framework.components.xy;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Vector3f;

public class CameraXYBindComponent extends BaseComponent implements UpdateConsumerComponent {
    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return;

        var transformPos = parent.getComponent(Transform.class).getPosition();
        var camera = eCtx.getScene().getCamera();

        camera.setPosition(new Vector3f(transformPos.x, transformPos.y, camera.getPosition().z));
    }
}
