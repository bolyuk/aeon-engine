package bl0.aeon.engine.fabrics;

import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.c.Vectors;
import bl0.aeon.engine.data.component.light.AE_DirectionalLight;
import bl0.aeon.engine.data.component.light.AE_PointLight;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightFabric {
    public static PointLight createPointLight(Vector4f color) {
        return new AE_PointLight(new Vector3f(color.x, color.y, color.z), Vectors.ONE(), new Vector3f(0.1f, 0.1f, 0.1f), 1.0f, 0.0f, 0.0f);
    }

    public static DirectionalLight createDirectionalLight() {
        return new AE_DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(0.05f, 0.05f, 0.05f), new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(0.5f, 0.5f, 0.5f));
    }
}

