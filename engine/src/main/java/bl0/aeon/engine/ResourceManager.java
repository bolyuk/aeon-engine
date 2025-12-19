package bl0.aeon.engine;

import bl0.aeon.base.exception.ResourceException;
import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.render.common.c.ShaderPrograms;
import bl0.aeon.base.core.IResourceManager;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.IDisposable;
import bl0.bjs.common.base.BJSBaseClass;

import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager extends BJSBaseClass implements IResourceManager {
    private final ConcurrentHashMap<String, IResource> resources = new ConcurrentHashMap<>();
    private final EngineContext context;

    public ResourceManager(EngineContext ctx) {
        super(ctx);
        this.context = ctx;
        loadDefaultResources();
    }

    private void loadDefaultResources() {
        registerResource(new GLShaderProgram(Shaders.VERTEX_SOLID_COLOR, Shaders.FRAGMENT_SOLID_COLOR, ShaderPrograms.SOLID_COLOR));
        registerResource(new GLShaderProgram(Shaders.VERTEX_SOLID_COLOR_SHADOW, Shaders.FRAGMENT_SOLID_COLOR_SHADOW, ShaderPrograms.SOLID_COLOR_SHADOW));
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

    @Override
    public ShaderProgram buildShaderProgram(String vertexShader, String fragmentShader, String name) {
        return new GLShaderProgram(vertexShader, fragmentShader, name);
    }
}

