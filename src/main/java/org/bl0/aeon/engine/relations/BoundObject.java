/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.relations;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.events.REventV;
import org.bl0.aeon.engine.interfaces.events.VEventD;
import org.bl0.aeon.engine.interfaces.relations.IBindable;
import org.bl0.aeon.engine.interfaces.relations.IGetter;
import org.bl0.aeon.engine.interfaces.relations.INotifier;

public class BoundObject<T>
implements IDisposable,
IGetter<T>,
IBindable<BoundObject<T>> {
    private T object;
    private final ConcurrentLinkedQueue<INotifier<?, ?>> bindings = new ConcurrentLinkedQueue();
    private final REventV<T> factory;
    private boolean isDirty = true;
    private final VEventD updateListener = e -> this.markDirty();

    private long delay = 5;
    private long lastDelay = 0;

    public BoundObject(T object, REventV<T> factory, INotifier<?, ?> ... bindings) {
        this.object = object;
        this.factory = factory;
        for (INotifier<?, ?> binding : bindings) {
            this.bind((INotifier)binding);
        }
    }

    public BoundObject(T object, REventV<T> factory, long delay, INotifier<?, ?> ... bindings) {
        this.object = object;
        this.factory = factory;
        this.delay = delay;
        for (INotifier<?, ?> binding : bindings) {
            this.bind((INotifier)binding);
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public BoundObject<T> unbindAll() {
        for (INotifier<?, ?> binding : this.bindings) {
            this.unbind((INotifier)binding);
        }
        return this;
    }

    @Override
    public T get() {
        if (this.isDirty && lastDelay+delay < System.currentTimeMillis()) {
            this.object = this.factory.invoke();
            lastDelay = System.currentTimeMillis();
        }
        this.isDirty = false;
        return this.object;
    }

    @Override
    public void dispose() {
        this.unbindAll();
    }

    @Override
    public BoundObject<T> bind(INotifier<?, ?> notifier) {
        this.bindings.add(notifier);
        notifier.addListener(this.updateListener);
        return this;
    }

    @Override
    public BoundObject<T> unbind(INotifier<?, ?> notifier) {
        this.bindings.remove(notifier);
        notifier.addListener(this.updateListener);
        return this;
    }
}

