/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.engine.fabrics;

import org.bl0.aeon.core.components.data.res.Material;
import org.joml.Vector4f;

public class MaterialFabric {
    public static Material solidColor(Vector4f color) {
        Material mat = new Material();
        mat.shaderProgram = ShaderFabric.solidColor();
        mat.color = color;
        return mat;
    }

    public static Material solidColorShadow(Vector4f color) {
        Material mat = new Material();
        mat.shaderProgram = ShaderFabric.solidColorShadow();
        mat.color = color;
        return mat;
    }
}

