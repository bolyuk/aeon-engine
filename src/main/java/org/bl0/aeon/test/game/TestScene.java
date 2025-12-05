/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.test.game;

import bl0.bjs.common.base.IContext;
import bl0.bjs.socket.base.IWSBase;
import org.bl0.aeon.core.c.Colors;
import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.core.components.data.Transform;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.engine.fabrics.LightFabric;
import org.bl0.aeon.engine.fabrics.MaterialFabric;
import org.bl0.aeon.engine.fabrics.MeshFabric;
import org.bl0.aeon.engine.scene.BaseScene;
import org.bl0.aeon.test.game.world.World;
import org.joml.Vector3f;

public class TestScene
extends BaseScene {
    Entity cube = new Entity();
    Entity light = new Entity();
    Entity directionalLight = new Entity();

    private final IContext ctx;
    private final IWSBase chanel;

    public TestScene(IContext ctx, IWSBase chanel) {
        this.ctx = ctx;
        this.chanel = chanel;
    }

    @Override
    public void OnBeforeRender(DrawContext drawCtx, GameContext gameCtx) {
    }

    @Override
    public void OnBeforeUpdate(GameContext gameCtx) {
        float delta = (float)gameCtx.deltaTime;
        float angle = (float)Math.toRadians(45.0) * delta;
        this.cube.get(Transform.class).rotation.change(obj -> obj.rotateAxis(angle, 0.0f, 1.0f, 0.0f));
        float time = (float)(System.currentTimeMillis() % 100000L) / 1000.0f;
        float radius = 3.0f;
        float x = (float)Math.sin(time) * radius;
        float z = (float)Math.cos(time) * radius;
        this.light.get(Transform.class).position.change(obj -> obj.set(x, z, z * x));
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void bind(GameContext data) {
        this.directionalLight.add(LightFabric.createDirectionalLight());
        this.add(this.directionalLight);
        this.cube.add(MeshFabric.cube());
        this.cube.add(new Transform());
        this.cube.add(MaterialFabric.solidColorShadow(Colors.WHITE));
        this.add(this.cube);
        this.light.add(MeshFabric.sphere(36, 18, 1.0f));
        this.light.add(new Transform().setPos(new Vector3f(0.0f, 1.5f, 3.0f)).setScale(new Vector3f(0.5f, 0.5f, 0.5f)));
        this.light.add(MaterialFabric.solidColor(Colors.RED));
        this.light.add(LightFabric.createPointLight(Colors.RED));
        this.add(this.light);
        this.add(new World());
    }

    @Override
    public void unbind(GameContext data) {
    }
}

