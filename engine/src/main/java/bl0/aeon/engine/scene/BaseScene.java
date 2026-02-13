package bl0.aeon.engine.scene;

import java.util.ArrayList;
import java.util.List;

import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.scene.Scene;
import bl0.aeon.api.scene.SceneObject;
import bl0.aeon.render.api.data.render.Camera;

public class BaseScene implements Scene {

    protected ArrayList<SceneObject> entities = new ArrayList();
    protected Camera camera = new AE_Camera();

    protected IEngineContext eCtx;

    @Override
    public void onUpdate(IEngineContext ctx) {
        for(SceneObject object : entities) {
            object.update(ctx.getFrameContext(), ctx);
        }
    }

    public void onShowed(IEngineContext ctx) {
        this.eCtx = ctx;
        int width = ctx.getFrameContext().getWidth();
        int height = ctx.getFrameContext().getHeight();
        camera.setAspectRatio(width/height);
    }

    public void onHided(IEngineContext ctx) {
        this.eCtx = null;
    }

    @Override
    public List<SceneObject> getSceneObjects() {
        return entities;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void add(SceneObject sceneObject) {
        entities.add(sceneObject);
    }

    @Override
    public void remove(SceneObject sceneObject) {
        entities.remove(sceneObject);
    }
}

