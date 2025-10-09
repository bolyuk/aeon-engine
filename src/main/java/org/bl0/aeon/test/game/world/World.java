/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.test.game.world;

import org.bl0.aeon.core.c.Colors;
import org.bl0.aeon.core.components.data.Transform;
import org.bl0.aeon.core.components.data.res.Material;
import org.bl0.aeon.core.components.data.res.Mesh;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.core.entity.EntityGroup;
import org.bl0.aeon.engine.fabrics.MaterialFabric;
import org.bl0.aeon.engine.fabrics.MeshFabric;
import org.joml.Vector3f;

public class World
extends EntityGroup {
    Material material = MaterialFabric.solidColorShadow(Colors.WHITE);
    Mesh cube = MeshFabric.cube();

    @Override
    public void onAdded() {
        super.onAdded();
        this.generateChunks();
    }

    public void generateChunks() {
        this.dispose();
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                Entity e = new Entity();
                Transform t = new Transform();
                t.scale.change(obj -> obj.mul(32.0f));
                t.position.set(new Vector3f(32 * x, 0.0f, 32 * z));
                e.add(t);
                e.add(this.cube);
                e.add(this.material);
                this.sceneContext.add(e);
            }
        }
    }
}

