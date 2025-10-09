/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.interfaces.component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bl0.aeon.core.entity.Entity;

public class BaseComponentContainer<E extends BaseComponentContainer<E>> {
    private final ConcurrentHashMap<Class<? extends IComponent>, Object> components = new ConcurrentHashMap();
    private Entity entity;

    protected void bindEntity(Entity entity) {
        this.entity = entity;
    }

    public E add(IComponent component) {
        this.components.put(component.getComponentClass(), component);
        component.onAdded(this.entity);
        return (E)this;
    }

    public E del(IComponent component) {
        this.components.remove(component.getComponentClass());
        component.onRemoved(this.entity);
        return (E)this;
    }

    public E del(Class<? extends IComponent> clazz) {
        this.components.remove(clazz);
        return (E)this;
    }

    public boolean has(Class<? extends IComponent> clazz) {
        return this.components.containsKey(clazz);
    }

    public <T extends IComponent> T get(Class<T> clazz) {
        return (T)((IComponent)this.components.get(clazz));
    }

    public <T> List<T> getEvery(Class<T> intrfc) {
        return this.components.keySet().stream().filter(intrfc::isAssignableFrom).map(k -> (T)this.components.get(k)).toList();
    }
}

