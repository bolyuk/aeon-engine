/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.parsing;

import org.bl0.aeon.engine.context.GameContext;

public abstract class ILoader<T extends ILoadable> {
    protected GameContext context;

    public ILoader(GameContext context) {
        this.context = context;
    }

    public abstract T load(String var1);

    public abstract String parse(T var1);

    public abstract String extension();
}

