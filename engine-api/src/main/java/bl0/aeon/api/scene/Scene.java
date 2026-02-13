package bl0.aeon.api.scene;

import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.render.api.data.render.Camera;

import java.util.List;

public interface Scene {
    void onUpdate(IEngineContext ctx);
    List<SceneObject> getSceneObjects();

    Camera getCamera();
    void add(SceneObject sceneObject);
    void remove(SceneObject sceneObject);
}
