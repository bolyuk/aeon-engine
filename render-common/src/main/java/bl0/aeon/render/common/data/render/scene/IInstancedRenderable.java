package bl0.aeon.render.common.data.render.scene;

import java.nio.FloatBuffer;

public interface IInstancedRenderable extends ISceneRenderable {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
}
