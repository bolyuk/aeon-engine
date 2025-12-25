package bl0.aeon.base.scene;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.render.common.base.IName;

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
