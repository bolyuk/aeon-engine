/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.entity;

import java.util.UUID;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.ISceneContext;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.IUniformsSetter;
import org.bl0.aeon.engine.interfaces.bind.IBindable;
import org.bl0.aeon.engine.interfaces.component.BaseComponentContainer;
import org.bl0.aeon.engine.interfaces.draw.IDrawable;

public class Entity
extends BaseComponentContainer<Entity>
implements IDisposable,
IDrawable,
IBindable,
IUniformsSetter {
    public final UUID uuid = UUID.randomUUID();
    public ISceneContext sceneContext;

    public Entity() {
        this.bindEntity(this);
    }

    public void onAdded() {
    }

    @Override
    public void dispose() {
        this.getEvery(IDisposable.class).forEach(IDisposable::dispose);
    }

    @Override
    public void draw(DrawContext drawCtx) {
        this.getEvery(IDrawable.class).forEach(x -> x.draw(drawCtx));
    }

    @Override
    public void bind() {
        this.getEvery(IBindable.class).forEach(IBindable::bind);
    }

    @Override
    public void unbind() {
        this.getEvery(IBindable.class).forEach(IBindable::unbind);
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        this.getEvery(IUniformsSetter.class).forEach(x -> x.setUniforms(program));
    }
}

