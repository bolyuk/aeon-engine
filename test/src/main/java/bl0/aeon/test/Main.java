package bl0.aeon.test;

import bl0.aeon.engine.core.AeonEngine;
import bl0.aeon.gl.GLEngine;
import bl0.aeon.test.scenes.CubeTestScene;
import bl0.bjs.boot.BJSInitializer;
import bl0.bjs.common.base.IContext;

public class Main {
    public static void main(String[] args) {
        IContext ctx = BJSInitializer.defaultInit("TEST");

        AeonEngine engine = new AeonEngine(ctx, new GLEngine(ctx));
        engine.initialize("AEON TEST", 800, 500);
        engine.setScene(new CubeTestScene());
        engine.start();
    }
}

