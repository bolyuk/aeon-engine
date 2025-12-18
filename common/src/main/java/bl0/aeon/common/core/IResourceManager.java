package bl0.aeon.common.core;

import bl0.aeon.common.base.IResource;

public interface IResourceManager {

    IResource getResource(String name);

    <T extends IResource> T getResource(String name, Class<T> type);

    <T extends IResource> void registerResource(T resource);

    <T extends IResource> void registerResource(String jsonResource, Class<T> type);

    void clear();
}
