/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.fabrics;

import org.bl0.aeon.core.components.light.DirectionalLight;
import org.bl0.aeon.core.components.light.PointLight;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightFabric {
    public static PointLight createPointLight(Vector4f color) {
        return new PointLight(new Vector3f(color.x, color.y, color.z), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0.1f, 0.1f, 0.1f), 1.0f, 0.0f, 0.0f);
    }

    public static DirectionalLight createDirectionalLight() {
        return new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(0.05f, 0.05f, 0.05f), new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(0.5f, 0.5f, 0.5f));
    }
}

