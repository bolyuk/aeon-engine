/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.gl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.init.BaseInitializable;
import org.bl0.aeon.engine.interfaces.parsing.ILoader;
import org.bl0.aeon.engine.loaders.FragmentShaderLoader;
import org.bl0.aeon.engine.loaders.VertexShaderLoader;

public class ResourceManager
extends BaseInitializable
implements IDisposable {
    private final ConcurrentHashMap<String, Object> resources = new ConcurrentHashMap();
    private final ConcurrentLinkedQueue<ILoader<?>> loaders = new ConcurrentLinkedQueue();
    private final ArrayList<ClassLoader> classLoaders = new ArrayList();
    private final ConcurrentHashMap<String, URI> cached = new ConcurrentHashMap();

    public ResourceManager(GameContext gameContext) {
        this.loaders.add(new FragmentShaderLoader(gameContext));
        this.loaders.add(new VertexShaderLoader(gameContext));
        this.classLoaders.add(this.getClass().getClassLoader());
    }

    public void addClassLoader(ClassLoader classLoader) {
        this.classLoaders.add(classLoader);
    }

    public <T> T get(String name, Class<T> type) {
        Object value = this.resources.get(name);
        if (value == null) {
            String found = this.find(name);
            if (found == null) {
                throw new NoSuchElementException("Resource '" + name + "' is not found");
            }
            if (!found.contains(".")) {
                throw new RuntimeException("Resource does not contain a postfix");
            }
            String postfix = found.substring(found.lastIndexOf(46));
            ILoader loader = this.loaders.stream().filter(x -> x.extension().equals(postfix)).findFirst().orElse(null);
            if (loader == null) {
                throw new RuntimeException("No loader found for '" + name + "'");
            }
            Object data = loader.load(this.readFromRes(found));
            if (!type.equals(data.getClass())) {
                throw new RuntimeException("Data type mismatch for '" + name + "'");
            }
            return (T)data;
        }
        if (!type.isAssignableFrom(value.getClass())) {
            throw new ClassCastException("Resource '" + name + "' is not of type " + type.getName());
        }
        return (T)this.resources.get(name);
    }

    public String find(String name) {
        if (name.contains(".")) {
            return this.getClass().getClassLoader().getResource(name.substring(0, name.lastIndexOf("."))).toString();
        }
        String full = null;
        for (String ext : this.loaders.stream().map(ILoader::extension).toList()) {
            full = name + "." + ext;
        }
        if (this.getClass().getClassLoader().getResource(full) != null) {
            return full;
        }
        return null;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public String readFromRes(String path) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);){
            String string;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));){
                string = reader.lines().collect(Collectors.joining("\n"));
            }
            return string;
        }
        catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to load resource: " + path, e);
        }
    }

    private String getExtensionOrThrow(String name) {
        if (!name.contains(".")) {
            throw new RuntimeException("Resource does not contain a extension");
        }
        return name.substring(name.lastIndexOf(46));
    }

    private ILoader<?> getLoaderByExtensionOrThrow(String extension) {
        return this.loaders.stream().filter(x -> x.extension().equals(extension)).findFirst().orElseThrow(() -> new NoSuchElementException("No loader found for '" + extension + "'"));
    }

    @Override
    public void dispose() {
        this.resources.values().stream().filter(x -> x instanceof IDisposable).forEach(x -> ((IDisposable)x).dispose());
        this.resources.clear();
    }

    @Override
    public void initCall() {
        for (ClassLoader loader : this.classLoaders) {
            try {
                Enumeration<URL> enumeration = loader.getResources("/");
                while (enumeration.hasMoreElements()) {
                    URL uRL = enumeration.nextElement();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

