package bl0.aeon.test;

import bl0.aeon.api.core.GameInfo;
import bl0.aeon.engine.core.AeonEngine;
import bl0.aeon.gl.GLBackend;
import bl0.aeon.test.scenes.CubeTestScene;
import bl0.bjs.boot.BJSInitializer;
import bl0.bjs.common.base.IContext;

public class TEST {
    public static void main(String[] args) {
        IContext ctx = BJSInitializer.defaultInit("TEST");

        AeonEngine engine = new AeonEngine(ctx, new GameInfo("AEON TEST", 1));

        engine.setGraphicBackend(new GLBackend(ctx).get());

        engine.initialize(800, 500);
        engine.loadDefaultResources();
        engine.setScene(new CubeTestScene());
        engine.start();
    }
}

