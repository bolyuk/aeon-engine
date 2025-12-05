package org.bl0.aeon.test.com.server;

import bl0.bjs.common.base.IContext;
import bl0.bjs.socket.core.communication.WSServer;
import org.bl0.aeon.core.c.Colors;
import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.core.components.data.Transform;
import org.bl0.aeon.core.components.data.res.Material;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.core.systems.LightSystem;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.context.ISceneContext;
import org.bl0.aeon.engine.fabrics.LightFabric;
import org.bl0.aeon.engine.fabrics.MaterialFabric;
import org.bl0.aeon.engine.fabrics.MeshFabric;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.engine.scene.IScene;
import org.bl0.aeon.test.game.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerScene extends WSServer implements IGameServer, IScene, ISceneContext {

    Entity cube = new Entity();
    Entity light = new Entity();
    Entity directionalLight = new Entity();

    public ServerScene(IContext context, InetSocketAddress address) {
        super(context, address);
    }

    protected ArrayList<Entity> entities = new ArrayList();
    public Camera camera = new Camera();

    @Override
    public void OnBeforeRender(DrawContext var1, GameContext var2) {

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
    public void add(Entity entity) {
        this.entities.add(entity);
        entity.sceneContext = this;
        entity.onAdded();
    }

    public void update(GameContext gameCtx) {
        this.OnBeforeUpdate(gameCtx);
        this.entities.forEach(entity -> entity.getEvery(IComponent.class).forEach(c -> c.OnBeforeUpdate(gameCtx)));
    }

    public void render(DrawContext drawCtx, GameContext gameCtx) {
        this.OnBeforeRender(drawCtx, gameCtx);
        for (Entity e : this.entities) {
            e.getEvery(IComponent.class).forEach(c -> c.OnBeforeRender(drawCtx, gameCtx));
            Material m = e.get(Material.class);
            if (m == null) continue;
            ShaderProgram s = m.shaderProgram;
            e.bind();
            e.setUniforms(s);
            LightSystem.prepareUniformsFor(e, s, this.entities);
            gameCtx.scene.getCamera().setUniforms(s);
            e.draw(drawCtx);
            e.unbind();
        }
    }

    @Override
    public void dispose() {
        this.unbind(null);
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void bind(GameContext var1) {
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
    public void unbind(GameContext var1) {

    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public void commitMove(Vector3f pos, Vector3f direction, String name) {

    }

    @Override
    public void acceptMove(Vector3f pos, Vector3f direction, String name) {

    }
}
