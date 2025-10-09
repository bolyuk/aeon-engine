/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.context;

import org.bl0.aeon.core.ResourceManager;
import org.bl0.aeon.engine.scene.AbstractScene;
import org.bl0.aeon.core.graphic.Window;
import org.bl0.aeon.engine.interfaces.input.AbstractInputManager;
import org.bl0.aeon.engine.interfaces.IDisposable;

public class GameContext
implements IDisposable {
    public ResourceManager resourceManager = new ResourceManager(this);
    public Window window;
    public AbstractScene scene;
    public AbstractInputManager inputManager;
    public double deltaTime = 0.0;

    @Override
    public void dispose() {
        if (this.scene != null) {
            this.scene.dispose();
        }
        if (this.inputManager != null) {
            this.inputManager.unbind(this);
        }
        this.resourceManager.dispose();
    }
}

