/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.engine.relations;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.events.VEventD;
import org.bl0.aeon.engine.interfaces.relations.IGetter;
import org.bl0.aeon.engine.interfaces.relations.INotifier;

public class NotifyObject<T>
implements IDisposable,
IGetter<T>,
INotifier<NotifyObject<T>, T> {
    private T object;
    private final ConcurrentLinkedQueue<VEventD<T>> listeners = new ConcurrentLinkedQueue();

    public NotifyObject(T object) {
        this.object = object;
    }

    @Override
    public T get() {
        return this.object;
    }

    public NotifyObject<T> change(VEventD<T> supplier) {
        supplier.invoke(this.object);
        this.invoke();
        return this;
    }

    public NotifyObject<T> set(T object) {
        this.object = object;
        this.invoke();
        return this;
    }

    @Override
    public NotifyObject<T> addListener(VEventD<T> e) {
        this.listeners.add(e);
        return this;
    }

    @Override
    public NotifyObject<T> remListener(VEventD<T> e) {
        this.listeners.remove(e);
        return this;
    }

    @Override
    public NotifyObject<T> invoke() {
        if (!this.listeners.isEmpty()) {
            for (VEventD<T> e : this.listeners) {
                e.invoke(this.object);
            }
        }
        return this;
    }

    @Override
    public void dispose() {
        this.listeners.clear();
    }
}

