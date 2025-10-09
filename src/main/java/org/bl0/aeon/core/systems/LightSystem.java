/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.systems;

import java.util.ArrayList;
import java.util.List;
import org.bl0.aeon.core.c.Uniforms;
import org.bl0.aeon.core.components.data.Transform;
import org.bl0.aeon.core.components.light.DirectionalLight;
import org.bl0.aeon.core.components.light.ILightComponent;
import org.bl0.aeon.core.components.light.PointLight;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;

public class LightSystem {
    public static void prepareUniformsFor(Entity e, ShaderProgram program, ArrayList<Entity> list) {
        program.resetIndex("pointLight");
        Entity directionalLightEntity = list.stream().filter(x -> x.has(DirectionalLight.class)).findFirst().orElse(null);
        if (directionalLightEntity != null) {
            directionalLightEntity.get(DirectionalLight.class).setUniforms(program);
        }
        List<Entity> filtered = list.stream().filter(x -> !x.uuid.equals(e.uuid) && x.has(ILightComponent.class)).toList();
        for (Entity x2 : filtered) {
            List<ILightComponent> lights = x2.getEvery(ILightComponent.class);
            for (ILightComponent light : lights) {
                light.setUniforms(program);
                if (!(light instanceof PointLight)) continue;
                if (x2.has(Transform.class)) {
                    Transform transform = x2.get(Transform.class);
                    program.setUniform(Uniforms.Light.Point.POSITION_INDEX, transform.position);
                }
                program.incrementIndex("pointLight");
            }
        }
    }
}

