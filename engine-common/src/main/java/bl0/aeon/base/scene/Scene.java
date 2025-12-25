package bl0.aeon.base.scene;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.render.common.data.render.Camera;

import java.util.List;

public interface Scene {
    void onUpdate(IEngineContext ctx);
    List<SceneObject> getSceneObjects();

    Camera getCamera();
    void add(SceneObject sceneObject);
    void remove(SceneObject sceneObject);
}
