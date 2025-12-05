package org.bl0.aeon.engine.scene;

import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.bind.IBindWData;

public interface IScene extends IDisposable, IBindWData<GameContext> {
    void OnBeforeRender(DrawContext var1, GameContext var2);
    void OnBeforeUpdate(GameContext var1);
    void add(Entity entity);
    void update(GameContext gameCtx);
    void render(DrawContext drawCtx, GameContext gameCtx);

    void dispose();

    Camera getCamera();
}
