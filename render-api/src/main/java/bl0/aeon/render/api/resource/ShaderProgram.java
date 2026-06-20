package bl0.aeon.render.api.resource;

import bl0.aeon.render.api.base.IResource;
import bl0.bjs.common.core.tuple.Pair;
import org.joml.*;

import java.util.HashMap;
import java.util.function.Consumer;

public interface ShaderProgram extends IResource {

    void setUniformProviderInt(Consumer<HashMap<String, Integer>> values);

    void setUniformProvider1f(Consumer<HashMap<String, Float>> values);
    void setUniformProvider2f(Consumer<HashMap<String, Vector2f>> values);
    void setUniformProvider3f(Consumer<HashMap<String, Vector3f>> values);
    void setUniformProvider4f(Consumer<HashMap<String, Vector4f>> values);

    void setUniformProviderQuat(Consumer<HashMap<String, Quaternionf>> values);

    void setUniformProviderMat4(Consumer<HashMap<String, Matrix4f>> values);
}
