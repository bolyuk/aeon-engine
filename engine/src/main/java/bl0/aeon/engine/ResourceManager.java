package bl0.aeon.engine;

import bl0.aeon.api.exception.ResourceException;
import bl0.aeon.render.api.base.IResource;
import bl0.aeon.api.core.IResourceManager;
import bl0.aeon.render.api.base.IDisposable;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;

import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager extends BJSBaseClass implements IResourceManager {
    private final ConcurrentHashMap<String, IResource> resources = new ConcurrentHashMap<>();

    public ResourceManager(IContext ctx) {
        super(ctx);
    }

    @Override
    public IResource getResource(String name) {
        IResource resource = resources.get(name);
        if(resource == null)
            throw new ResourceException("No such resource: " + name);
        return resource;
    }

    @Override
    public <T extends IResource> T getResource(String name, Class<T> type) {
        IResource resource = getResource(name);
        if(!type.isAssignableFrom(resource.getClass()))
            throw new ResourceException("wrong resource type: " + type+" for: "+name+"("+resource.getClass().getSimpleName()+")");
        return (T) resource;
    }

    @Override
    public <T extends IResource> void registerResource(T resource) {
        if(resources.containsKey(resource.getName()))
            throw new ResourceException("Resource already registered: " + resource.getName());

        resources.put(resource.getName(), resource);
    }

    @Override
    public <T extends IResource> void registerResource(String jsonResource, Class<T> type) {
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        resources.forEach((name, resource) -> {
           if(resource instanceof IDisposable d)
               d.dispose();
        });
        resources.clear();
    }
}

