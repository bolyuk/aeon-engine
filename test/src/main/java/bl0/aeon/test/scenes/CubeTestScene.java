package bl0.aeon.test.scenes;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.Stage;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.engine.data.component.AE_Model;
import bl0.aeon.engine.data.component.AE_Transform;
import bl0.aeon.engine.data.scene.Entity;
import bl0.aeon.engine.scene.BaseScene;
import bl0.aeon.gl.c.Shaders;
import bl0.aeon.render.common.c.Colors;

public class CubeTestScene extends BaseScene {

    @Override
    public void onShowed(IEngineContext ctx) {
        super.onShowed(ctx);
        ctx.getDispatcher().dispatch(Stage.BEFORE_SCENE_UPDATE, this::createCube);
    }

    @Override
    public void onHided(IEngineContext ctx) {
        super.onHided(ctx);
        entities.clear();
    }

    private void createCube(IEngineContext ctx){
        Entity testCube = new Entity("TestCube");
        testCube.addComponent(new AE_Transform());

        AE_Model model = new AE_Model();
        model.setMesh(ctx.getResourceFabric().createCube("M_TestCube"));
        testCube.addComponent(model);

        AE_Material material = new AE_Material();
        material.setColor(Colors.RED);
        material.setShaderProgram(ctx.getResourceFabric().createShaderProgram(Shaders.VERTEX_SOLID_COLOR, Shaders.FRAGMENT_SOLID_COLOR, "TestCubeShader"));
        testCube.addComponent(material);

        add(testCube);
    }
}
