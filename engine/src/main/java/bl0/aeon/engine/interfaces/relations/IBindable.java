/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.engine.interfaces.relations;

public interface IBindable<R> {
    public R bind(INotifier<?, ?> var1);

    public R unbind(INotifier<?, ?> var1);
}

