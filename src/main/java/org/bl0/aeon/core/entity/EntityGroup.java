/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.entity;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;

public class EntityGroup
extends Entity {
    public final ConcurrentLinkedQueue<Entity> entities = new ConcurrentLinkedQueue();

    public EntityGroup addEntity(Entity entity) {
        this.entities.add(entity);
        return this;
    }

    public EntityGroup removeEntity(Entity entity) {
        this.entities.remove(entity);
        return this;
    }

    public Entity getEntity(UUID uuid) {
        return this.entities.stream().filter(e -> e.uuid.equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public void bind() {
        super.bind();
        this.entities.forEach(Entity::bind);
    }

    @Override
    public void unbind() {
        super.unbind();
        this.entities.forEach(Entity::unbind);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
    }

    @Override
    public void draw(DrawContext drawCtx) {
        super.draw(drawCtx);
        this.entities.forEach(e -> e.draw(drawCtx));
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        super.setUniforms(program);
        this.entities.forEach(e -> e.setUniforms(program));
    }
}

