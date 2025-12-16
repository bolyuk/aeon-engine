/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.test.game.world.chunk;

import org.bl0.aeon.core.components.data.res.Mesh;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.engine.relations.NotifyObject;

public abstract class AbstractVoxelChunk<T>
implements IVoxelDataObject<T>,
IDisposable,
IComponent {
    public final int x;
    public final int y;
    public final int z;
    public final int chunkSize;

    public AbstractVoxelChunk(int x, int y, int z, int chunkSize) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkSize = chunkSize;
    }

    protected abstract NotifyObject<Mesh> getMesh();

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return AbstractVoxelChunk.class;
    }
}

