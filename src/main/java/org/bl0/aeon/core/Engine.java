/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core;

import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.graphic.Window;
import org.bl0.aeon.engine.interfaces.input.AbstractInputManager;
import org.bl0.aeon.engine.interfaces.IDisposable;
import org.bl0.aeon.engine.interfaces.init.BaseInitializable;
import org.bl0.aeon.engine.scene.IScene;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Engine
extends BaseInitializable
implements IDisposable {
    public final GameContext context = new GameContext();
    private DrawContext drawContext;
    public boolean isRunning;
    private double lastFrame = 0.0;
    private final Object lock_obj = new Object();

    @Override
    public void initCall() {
        Window window;
        GLFW.glfwInit();
        GLFW.glfwWindowHint(139266, 3);
        GLFW.glfwWindowHint(139267, 3);
        GLFW.glfwWindowHint(139272, 204801);
        try {
            window = new Window(800, 600, "AeonVR 0.0.1-a");
        }
        catch (Exception e) {
            e.printStackTrace();
            GLFW.glfwTerminate();
            return;
        }
        GLFW.glfwMakeContextCurrent(window.ID);
        GL.createCapabilities();
        GL11.glViewport(0, 0, 800, 600);
        context.window = window;
        drawContext = new DrawContext();
        GLFW.glfwSetFramebufferSizeCallback(window.ID, (win, w, h) -> {
            GL11.glViewport(0, 0, w, h);
            context.window.width = w;
            context.window.height = h;
            context.scene.getCamera().aspectRatio.set(Float.valueOf((float)w / (float)h));
        });
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2884);
        GL11.glCullFace(1029);
        GL11.glFrontFace(2305);
        GL11.glDepthFunc(513);
        context.resourceManager.init();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void start() {
        throwIfNotInitialized();
        isRunning = true;
        lastFrame = GLFW.glfwGetTime();
        while (isRunning && !GLFW.glfwWindowShouldClose(context.window.ID)) {
            
            synchronized (lock_obj) {
                GL11.glClear(16640);
                double current = GLFW.glfwGetTime();
                double delta = current - lastFrame;
                lastFrame = current;
                if (delta > 1.0 || delta <= 0.0) {
                    delta = 0.016;
                }
                drawContext.deltaTime = delta;
                context.deltaTime = delta;
                doTick();
                GLFW.glfwSwapBuffers(context.window.ID);
                GLFW.glfwPollEvents();
            }
        }
    }

    private void doTick() {
        IScene scene = context.scene;
        if (context.inputManager != null) {
            context.inputManager.processInput(context);
        }
        if (scene == null) {
            return;
        }
        scene.update(context);
        scene.render(drawContext, context);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setScene(IScene scene) {
        
        synchronized (lock_obj) {
            if (context.scene != null) {
                context.scene.dispose();
            }
            context.scene = scene;
            scene.bind(context);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setInputManager(AbstractInputManager inputManager) {
        synchronized (lock_obj) {
            if (context.inputManager != null) {
                context.inputManager.unbind(context);
            }
            context.inputManager = inputManager;
            context.inputManager.bind(context);
        }
    }

    public void addResourceClassLoader(ClassLoader classLoader) {
        throwIfInitialized();
        context.resourceManager.throwIfInitialized();
        context.resourceManager.addClassLoader(classLoader);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void dispose() {
        synchronized (lock_obj) {
            isRunning = false;
            context.dispose();
            GLFW.glfwTerminate();
        }
    }
}

