/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.test.game;

import org.bl0.aeon.core.Engine;

public class Main {
    public static void main(String[] args) {
        Engine gameEngine = new Engine();
        gameEngine.addResourceClassLoader(Main.class.getClassLoader());
        gameEngine.init();
        gameEngine.setScene(new TestScene());
        gameEngine.setInputManager(new DefaultKeyboardInputManager());
        gameEngine.start();
    }
}

