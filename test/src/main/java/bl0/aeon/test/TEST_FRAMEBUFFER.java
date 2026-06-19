package bl0.aeon.test;

import bl0.aeon.api.core.GameInfo;
import bl0.aeon.api.stage.Stage;
import bl0.aeon.engine.core.AeonEngine;
import bl0.aeon.gl.GLBackend;
import bl0.aeon.render.api.c.resources.ShaderPrograms;
import bl0.aeon.test.scenes.CubeTestScene;
import bl0.bjs.boot.BJSInitializer;
import bl0.bjs.common.base.IContext;

public class TEST_FRAMEBUFFER {
    public static void main(String[] args) {
        IContext ctx = BJSInitializer.defaultInit("TEST");

        AeonEngine engine = new AeonEngine(ctx, new GameInfo("AEON TEST", 1));

        engine.setGraphicBackend(new GLBackend(ctx).get());

        engine.initialize(800, 500);
        engine.loadDefaultResources();

        var shader = engine.getResourceFabric().loadShaderProgramFromResourcePath("shaders/framebuffer_inverted", ShaderPrograms.FRAMEBUFFER+"_INVERTED");

        engine.initializeDefaultFramebuffer();
        engine.getFramebuffer().setShader(shader);

        engine.setScene(new CubeTestScene());
        engine.start();
    }
}
