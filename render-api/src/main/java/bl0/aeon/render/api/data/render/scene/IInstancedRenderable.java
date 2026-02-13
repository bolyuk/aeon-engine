package bl0.aeon.render.api.data.render.scene;

import java.nio.FloatBuffer;

public interface IInstancedRenderable extends ISceneRenderable {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
}
