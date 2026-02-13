package bl0.aeon.framework.components;

import bl0.aeon.api.component.interfaces.IWindowSizeChangeConsumerComponent;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.api.events.ViewPortChangeEvent;
import bl0.aeon.engine.data.component.BaseComponent;

public class CameraSettingsComponent extends BaseComponent
        implements IWindowSizeChangeConsumerComponent {

    public float minAspect = 4f / 3f;
    public float maxAspect = 21f / 9f;

    @Override
    public void onWindowSizeChange(IEngineContext eCtx) {
        IFrameContext fCtx = eCtx.getFrameContext();

        float windowAspect = (float) fCtx.getWidth() / fCtx.getHeight();
        float usedAspect = windowAspect;

        if (windowAspect < minAspect) {
            usedAspect = minAspect;
        } else if (windowAspect > maxAspect) {
            usedAspect = maxAspect;
        }

        var cam = eCtx.getScene().getCamera();
        cam.setAspectRatio(usedAspect);

        eCtx.getEventBus()
                .getController(ViewPortChangeEvent.class)
                .fireEvent(new ViewPortChangeEvent.VPCEPayload(fCtx.getWidth(), fCtx.getHeight(), usedAspect));
    }
}

