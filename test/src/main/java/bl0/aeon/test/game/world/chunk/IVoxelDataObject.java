/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.test.game.world.chunk;

public interface IVoxelDataObject<T> {
    public void set(int var1, int var2, int var3, T var4);

    public T get(int var1, int var2, int var3);

    public boolean contains(int var1, int var2, int var3);
}

