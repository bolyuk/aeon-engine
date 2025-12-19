package bl0.aeon.gl.core;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.common.core.IDispatcher;
import bl0.aeon.base.core.IResourceManager;
import bl0.aeon.gl.ResourceManager;
import bl0.bjs.common.async.control.IAsyncBus;
import bl0.bjs.common.base.IContext;
import bl0.bjs.eventbus.IEventBus;
import bl0.bjs.logging.ILogger;
import bl0.bjs.services.IServiceContainer;

public class EngineContext implements IEngineContext, IContext {
    private final IContext ctx;
    private final IResourceManager resManager;
    private final IDispatcher dispatcher;
    private final IFrameContext frameContext = new FrameContext();

    public EngineContext(IContext ctx, IDispatcher dispatcher) {
        this.ctx = ctx;
        this.dispatcher = dispatcher;
        this.resManager = new ResourceManager(this);
    }

    @Override
    public IResourceManager getResourceManager() {
        return resManager;
    }

    @Override
    public IFrameContext getFrameContext() {
        return frameContext;
    }

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /// IContext
    @Override
    public IEventBus getEventBus() {
        return ctx.getEventBus();
    }
    /// IContext
    @Override
    public IServiceContainer getServiceContainer() {
        return ctx.getServiceContainer();
    }
    /// IContext
    @Override
    public ILogger generateLogger(Class<?> clazz) {
        return ctx.generateLogger(clazz);
    }
    /// IContext
    @Override
    public String getHostname() {
        return ctx.getHostname();
    }
    /// IContext
    @Override
    public IAsyncBus getAsyncBus() {
        return ctx.getAsyncBus();
    }
}
