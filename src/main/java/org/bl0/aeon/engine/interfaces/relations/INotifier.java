/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.relations;

import org.bl0.aeon.engine.interfaces.events.VEventD;

public interface INotifier<R, E> {
    public R addListener(VEventD<E> var1);

    public R remListener(VEventD<E> var1);

    public R invoke();
}

