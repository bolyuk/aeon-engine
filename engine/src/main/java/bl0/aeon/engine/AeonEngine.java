package bl0.aeon.engine;

import bl0.aeon.base.component.IComponentContainer;
import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Model;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.scene.Scene;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.base.stage.Stage;
import bl0.aeon.render.common.core.IResourceManager;
import bl0.aeon.render.common.core.RenderEngine;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.bjs.common.async.control.IAsyncController;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;

import java.util.ArrayList;
import java.util.List;

public class AeonEngine extends BJSBaseClass implements IEngineContext {

    private final RenderEngine renderEngine;
    private final IResourceManager resourceManager;

    private final Dispatcher dispatcher;
    private IAsyncController threadController;
    private Scene scene;

    private final Object lock = new Object();

    public AeonEngine(IContext ctx, RenderEngine renderEngine) {
        super(ctx);
        this.renderEngine = renderEngine;
        this.resourceManager = new  ResourceManager(ctx);
        this.dispatcher = new Dispatcher();
    }

    public void start(){
        threadController = ctx.getAsyncBus().register(this::cycle);
    }

    public void stop(){
        threadController.interrupt(true);
    }

    public void cycle(){
        while (threadController.isRunning()) {
            synchronized (lock) {
                dispatcher.fire(Stage.BEFORE_SCENE_UPDATE, this);
                onUpdate();
                dispatcher.fire(Stage.AFTER_SCENE_UPDATE, this);
                dispatcher.fire(Stage.BEFORE_SCENE_RENDER, this);
                onRender();
            }
        }
    }

    private void onRender() {
        List<SceneObject> objects = scene.getSceneObjects();
        ArrayList<IRenderable> prepared = new ArrayList<>();

        for (SceneObject object : objects) {
            if(object instanceof IRenderable){
                prepared.add((IRenderable) object);
            } else if(object instanceof IComponentContainer cmpc) {
                if(!cmpc.hasComponent(Transform.class)) continue;
                if(!cmpc.hasComponent(Model.class)) continue;
                if(!cmpc.hasComponent(Material.class)) continue;
            }
        }
        renderEngine.render(new RenderFrame(scene.getCamera(), prepared));
    }

    private void onUpdate() {
        scene.onUpdate(this);
    }

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public void setScene(Scene scene) {
        synchronized (lock) {
            this.scene = scene;
        }
    }
}
