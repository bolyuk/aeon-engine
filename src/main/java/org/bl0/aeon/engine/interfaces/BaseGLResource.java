/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces;

import java.util.UUID;

import org.bl0.aeon.engine.interfaces.bind.IBindable;
import org.bl0.aeon.engine.interfaces.parsing.ILoadable;

public abstract class BaseGLResource
implements IBindable,
IDisposable,
IResource,
ILoadable {
    public final int ID;
    public final UUID resourceID;
    private boolean isDisposed;
    private boolean isBound;

    protected BaseGLResource(int id) {
        this.ID = id;
        this.resourceID = UUID.randomUUID();
    }

    protected BaseGLResource(int id, UUID resourceID) {
        this.ID = id;
        this.resourceID = resourceID;
    }

    protected abstract void disposeCall();

    protected abstract void bindCall();

    protected abstract void unbindCall();

    public void throwIfDisposed() {
        if (this.isDisposed) {
            throw new IllegalStateException("Resource is disposed");
        }
    }

    public void throwIfBound() {
        if (this.isBound) {
            throw new IllegalStateException("Resource is already bound");
        }
    }

    public void throwIfNotBound() {
        if (!this.isBound) {
            throw new IllegalStateException("Resource is not bound");
        }
    }

    @Override
    public final void dispose() {
        this.throwIfDisposed();
        this.isDisposed = true;
        this.disposeCall();
    }

    @Override
    public final void bind() {
        this.throwIfDisposed();
        this.throwIfBound();
        this.isBound = true;
        this.bindCall();
    }

    @Override
    public final void unbind() {
        this.throwIfDisposed();
        this.throwIfNotBound();
        this.isBound = false;
        this.unbindCall();
    }

    public final boolean isDisposed() {
        return this.isDisposed;
    }

    @Override
    public final boolean isBound() {
        return this.isBound;
    }
}

