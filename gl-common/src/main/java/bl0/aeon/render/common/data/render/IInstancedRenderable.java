package bl0.aeon.render.common.data.render;

import java.nio.FloatBuffer;

public interface IInstancedRenderable extends IRenderable {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
}
