/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.test.game.world.chunk.octree;

import bl0.aeon.test.game.world.chunk.IVoxelDataObject;
import org.joml.Vector3f;

public class OctreeNode<T>
implements IVoxelDataObject<T> {
    public final Vector3f min;
    public final Vector3f max;
    public final Vector3f center;
    public final int depth;
    public final int depthStep;
    public T value;
    public final OctreeNode<T>[] children;

    public OctreeNode(int depth, int depthStep, Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
        this.depth = depth;
        this.depthStep = depthStep;
        this.center = min.add(max).div(2.0f);
        this.children = depth > 0 ? new OctreeNode[depth] : null;
    }

    @Override
    public void set(int x, int y, int z, T value) {
        if (this.depth > 0) {
            byte buf = 0;
            buf = (byte)(buf + (byte)(x > (int)this.center.x ? 4 : 0));
            buf = (byte)(buf + (byte)(y > (int)this.center.y ? 2 : 0));
            if (this.children[buf = (byte)(buf + (byte)(z > (int)this.center.z ? 1 : 0))] == null) {
                this.children[buf] = new OctreeNode<T>(this.depth - this.depthStep, this.depthStep, new Vector3f((buf & 4) != 0 ? this.center.x : this.min.x, (buf & 2) != 0 ? this.center.y : this.min.y, (buf & 1) != 0 ? this.center.z : this.min.z), new Vector3f((buf & 4) != 0 ? this.max.x : this.center.x, (buf & 2) != 0 ? this.max.y : this.center.y, (buf & 1) != 0 ? this.max.z : this.center.z));
            }
            this.children[buf].set(x, y, z, value);
        } else {
            this.value = value;
        }
    }

    @Override
    public T get(int x, int y, int z) {
        if (this.depth > 0) {
            for (int i = 0; i < this.children.length; ++i) {
                if (this.children[i] == null || !this.children[i].contains(x, y, z)) continue;
                return this.children[i].get(x, y, z);
            }
        }
        return this.value;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return this.max.x >= (float)x && this.min.x <= (float)x && this.max.y >= (float)y && this.min.y <= (float)y && this.max.z >= (float)z && this.min.z <= (float)z;
    }
}

