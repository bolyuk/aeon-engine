package bl0.aeon.test.scenes;

import bl0.aeon.base.component.Component;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.Stage;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.engine.data.component.AE_Model;
import bl0.aeon.engine.data.component.AE_Transform;
import bl0.aeon.engine.data.scene.Entity;
import bl0.aeon.engine.fabrics.LightFabric;
import bl0.aeon.engine.scene.BaseScene;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.c.resources.Textures;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CubeTestScene extends BaseScene {
    private Entity testCube;
    private AE_Transform transform;

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

    @Override
    public void onUpdate(IEngineContext ctx) {
        super.onUpdate(ctx);

        if (transform == null) return;

        float dt = (float) ctx.getFrameContext().getDeltaTime();
        float speed = (float) Math.toRadians(90.0);

        Quaternionf q = transform.rotation.get();
        q.rotateY(speed * dt);

        transform.rotation.set(q);
    }

    private void createCube(IEngineContext ctx){
        testCube = new Entity("TestCube");
        transform = new AE_Transform();
        transform.position.set(new Vector3f(0.0f, 1.0f, -2.0f));
        transform.rotation.set(new Quaternionf()
                .rotateXYZ(
                        (float) Math.toRadians(45),
                        (float) Math.toRadians(45),
                        (float) Math.toRadians(45)
                ));

        testCube.addComponent(transform);

        AE_Model model = new AE_Model();
        model.setMesh(ctx.getResourceFabric().createCube("M_TestCube"));
        testCube.addComponent(model);

        AE_Material material = new AE_Material();
        material.setTexture(ctx.getResourceManager().getResource(Textures.ERROR, Texture.class));
        material.setShaderProgram(ctx.getResourceManager().getResource(ShaderPrograms.TEXTURED_COLOR_SHADOW));
        testCube.addComponent(material);

        add(testCube);

        Entity directionalLight = new Entity("DirectionalLight");
        directionalLight.addComponent((Component) LightFabric.createDirectionalLight());
        add(directionalLight);
    }
}
