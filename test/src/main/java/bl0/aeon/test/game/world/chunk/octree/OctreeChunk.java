/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.test.game.world.chunk.octree;

import org.bl0.aeon.core.components.data.res.Mesh;
import org.bl0.aeon.engine.relations.NotifyObject;
import bl0.aeon.test.game.world.chunk.AbstractVoxelChunk;
import org.joml.Vector3f;

public class OctreeChunk
extends AbstractVoxelChunk<Byte> {
    private final NotifyObject<OctreeNode<Byte>> octreeRoot;

    public OctreeChunk(int x, int y, int z, int chunkSize) {
        super(x, y, z, chunkSize);
        this.octreeRoot = new NotifyObject(new OctreeNode(chunkSize, 1, new Vector3f(x, y, z), new Vector3f(x + chunkSize, y + chunkSize, z + chunkSize)));
    }

    @Override
    public void set(int x, int y, int z, Byte value) {
        this.octreeRoot.change(obj -> obj.set(x, y, z, value));
    }

    @Override
    public Byte get(int x, int y, int z) {
        return this.octreeRoot.get().get(x, y, z);
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return this.octreeRoot.get().contains(x, y, z);
    }

    @Override
    public NotifyObject<Mesh> getMesh() {
        return null;
    }

    @Override
    public void dispose() {
    }
}

