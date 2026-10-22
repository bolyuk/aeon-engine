package bl0.aeon.gl.graphic.mesh;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class GLMeshPool {
    public static final int GRANULE = 64;

    private final int stride;
    private final List<VertexAttribute> attributes;
    private final int arenaVertices;
    private final List<Arena> arenas = new ArrayList<>();

    public GLMeshPool(int stride, List<VertexAttribute> attributes, int arenaVertices) {
        this.stride = stride;
        this.attributes = attributes;
        this.arenaVertices = (arenaVertices + GRANULE - 1) / GRANULE * GRANULE;
    }

    public GLMesh allocate(byte[] data, String name) {
        int count = data.length / stride;
        if (count == 0) return null;
        int slot = (count + GRANULE - 1) / GRANULE * GRANULE;
        if (slot > arenaVertices) return new GLMesh(data, stride, attributes, name);

        for (Arena a : arenas) {
            int first = a.take(slot);
            if (first >= 0) return a.upload(data, first, slot, count, name);
        }
        Arena a = new Arena();
        arenas.add(a);
        return a.upload(data, a.take(slot), slot, count, name);
    }

    public int arenaCount() { return arenas.size(); }
    public long usedVertices() { return arenas.stream().mapToLong(a -> a.used).sum(); }

    public void dispose() {
        for (Arena a : arenas) {
            GL30.glDeleteBuffers(a.vbo);
            GL30.glDeleteVertexArrays(a.vao);
        }
        arenas.clear();
    }

    private final class Arena {
        final int vao, vbo;
        final TreeMap<Integer, Integer> free = new TreeMap<>();
        int largestFreeHint;
        long used;

        Arena() {
            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);
            vbo = GL30.glGenBuffers();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, (long) arenaVertices * stride, GL30.GL_STATIC_DRAW);
            for (int i = 0; i < attributes.size(); ++i) {
                VertexAttribute a = attributes.get(i);
                GL30.glVertexAttribPointer(i, a.amount, a.glType, a.normalized, stride, (long) a.byteOffset);
                GL30.glEnableVertexAttribArray(i);
            }
            GL30.glBindVertexArray(0);
            free.put(0, arenaVertices);
            largestFreeHint = arenaVertices;
        }

        int take(int slot) {
            if (slot > largestFreeHint) return -1;
            int largest = 0;
            for (Map.Entry<Integer, Integer> e : free.entrySet()) {
                int len = e.getValue();
                if (len >= slot) {
                    int start = e.getKey();
                    free.remove(start);
                    if (len > slot) free.put(start + slot, len - slot);
                    used += slot;
                    return start;
                }
                largest = Math.max(largest, len);
            }
            largestFreeHint = largest;              // nothing fits: so the next large request skips this arena at once
            return -1;
        }

        void release(int start, int slot) {
            used -= slot;
            int len = slot;
            Map.Entry<Integer, Integer> after = free.ceilingEntry(start);
            if (after != null && after.getKey() == start + len) {
                len += after.getValue();
                free.remove(after.getKey());
            }
            Map.Entry<Integer, Integer> before = free.floorEntry(start);
            if (before != null && before.getKey() + before.getValue() == start) {
                start = before.getKey();
                len += before.getValue();
            }
            free.put(start, len);
            largestFreeHint = Math.max(largestFreeHint, len);
        }

        GLMesh upload(byte[] data, int first, int slot, int count, String name) {
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            try {
                buffer.put(data).flip();
                GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
                GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, (long) first * stride, buffer);
                GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
            } finally {
                MemoryUtil.memFree(buffer);
            }
            return new PooledMesh(this, name, first, slot, count);
        }
    }

    private final class PooledMesh extends GLMesh {
        private final Arena arena;
        private final int slot;
        private boolean released;

        PooledMesh(Arena arena, String name, int first, int slot, int count) {
            super(name, arena.vao, arena.vbo, first, count, attributes);
            this.arena = arena;
            this.slot = slot;
        }

        @Override
        public void dispose() {
            if (released) return;
            released = true;
            arena.release(firstVertex, slot);
        }

        @Override
        public void enableMatrixInstancing(int baseLocation, int initialCapacity) {
            throw new UnsupportedOperationException("pooled meshes share a vertex array and cannot be instanced: " + name);
        }
    }
}
