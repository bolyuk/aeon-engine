/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.scene;

import java.util.ArrayList;
import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.core.components.data.res.Material;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.context.SceneContext;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.bind.IBindWData;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.core.systems.LightSystem;

public abstract class AbstractScene
extends SceneContext
implements IDisposable,
IBindWData<GameContext> {
    ArrayList<Entity> entities = new ArrayList();
    public Camera camera = new Camera();

    public abstract void OnBeforeRender(DrawContext var1, GameContext var2);

    public abstract void OnBeforeUpdate(GameContext var1);

    @Override
    public void add(Entity entity) {
        this.entities.add(entity);
        entity.sceneContext = this;
        entity.onAdded();
    }

    protected void disposeEntity(Entity entity) {
        entity.dispose();
        this.entities.remove(entity);
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
            gameCtx.scene.camera.setUniforms(s);
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
}

