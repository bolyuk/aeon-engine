/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.engine.interfaces.component;

import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.entity.Entity;

public interface IComponent {
    public Class<? extends IComponent> getComponentClass();

    default public void OnBeforeRender(DrawContext drawCtx, GameContext gameCtx) {
    }

    default public void OnBeforeUpdate(GameContext gameCtx) {
    }

    default public void onAdded(Entity e) {
    }

    default public void onRemoved(Entity e) {
    }
}

