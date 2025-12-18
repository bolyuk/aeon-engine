package bl0.aeon.gl;

import bl0.aeon.common.base.IResource;
import bl0.aeon.common.core.IResourceManager;
import bl0.aeon.common.data.component.graphic.Shader;
import bl0.aeon.gl.base.IDisposable;
import bl0.aeon.gl.base.ResourceException;
import bl0.aeon.gl.core.EngineContext;
import bl0.aeon.gl.fabrics.ShaderFabric;
import bl0.aeon.gl.graphic.shaders.GLShader;
import bl0.bjs.common.base.BJSBaseClass;

import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager extends BJSBaseClass implements IResourceManager {
    private final ConcurrentHashMap<String, IResource> resources = new ConcurrentHashMap<>();
    private final EngineContext context;

    public ResourceManager(EngineContext ctx) {
        super(ctx);
        this.context = ctx;
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

    public void loadDefaultResources() {
        registerResource(new GLShader(GLShader.ShaderType.VERTEX, ShaderFabric.VERTEX_SOLID_COLOR_SHADOW, Shader.));
    }
}

