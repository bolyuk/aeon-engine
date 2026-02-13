package bl0.aeon.api.scene;

import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.render.api.base.IName;

public abstract class SceneObject implements IName {
    private final String name;

    public SceneObject(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public abstract void update(IFrameContext fCtx, IEngineContext eCtx);
}
