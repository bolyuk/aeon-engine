/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.bind;

public interface IBindable {
    public void bind();

    public void unbind();

    default public boolean isBound() {
        throw new UnsupportedOperationException();
    }
}

