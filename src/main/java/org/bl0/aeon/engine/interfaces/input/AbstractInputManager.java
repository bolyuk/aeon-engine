/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.input;

import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.interfaces.bind.IBindWData;

public abstract class AbstractInputManager
implements IBindWData<GameContext> {
    public abstract void processInput(GameContext var1);
}

