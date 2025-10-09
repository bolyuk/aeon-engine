/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.init;

public abstract class BaseInitializable
implements IInitailizable {
    private boolean initialized;

    public abstract void initCall();

    public boolean isInitialized() {
        return this.initialized;
    }

    public void throwIfInitialized() {
        if (this.initialized) {
            throw new IllegalStateException("Object already initialized");
        }
    }

    public void throwIfNotInitialized() {
        if (!this.initialized) {
            throw new IllegalStateException("Object not initialized");
        }
    }

    @Override
    public final void init() {
        this.throwIfInitialized();
        this.initialized = true;
        this.initCall();
    }
}

