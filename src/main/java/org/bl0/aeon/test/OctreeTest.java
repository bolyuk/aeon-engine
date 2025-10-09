/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.test;

import org.bl0.aeon.test.game.world.chunk.octree.OctreeChunk;

public class OctreeTest {
    public static void main(String[] args) {
        OctreeChunk chunk = new OctreeChunk(0, 0, 0, 32);
        chunk.set(32, 32, 32, (byte)1);
        System.out.println(chunk.get(32, 32, 32));
    }
}

