/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.graphic;

import org.lwjgl.glfw.GLFW;

public class Window {
    public long ID;
    public long width;
    public long height;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.ID = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (this.ID == 0L) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
    }
}

