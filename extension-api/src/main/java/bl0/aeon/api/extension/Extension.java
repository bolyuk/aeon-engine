package bl0.aeon.api.extension;

import bl0.aeon.api.core.IEngineContext;

public abstract class Extension {
    protected IEngineContext ctx;

    public Extension(IEngineContext ctx){
        this.ctx = ctx;
    }

    public abstract void initialize();

    public abstract IExtensionContext getExtensionCtx();
}
