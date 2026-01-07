package bl0.aeon.framework.scenes;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.Stage;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.engine.data.component.AE_Model;
import bl0.aeon.engine.data.component.AE_Transform;
import bl0.aeon.engine.data.scene.Entity;
import bl0.aeon.engine.scene.BaseScene;
import bl0.aeon.framework.components.xyz.BillboardComponent;
import bl0.aeon.framework.components.xyz.RotatorComponent;
import bl0.aeon.render.common.base.IDisposable;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.c.resources.Textures;
import bl0.aeon.render.common.resource.*;
import org.joml.Vector3f;

public class LoadingScene extends BaseScene {

    private final long timeOut;
    private long startTime;
    private final BaseScene nextScene;

    private IResource shader;
    private Mesh plane;

    public LoadingScene(BaseScene nextScene, long timeOut) {
        this.nextScene = nextScene;
        this.timeOut = timeOut;
    }


    @Override
    public void onShowed(IEngineContext ctx) {
        super.onShowed(ctx);
        startTime = System.currentTimeMillis();
        ctx.getDispatcher().dispatch(Stage.BEFORE_SCENE_UPDATE, this::genScene);
    }

    @Override
    public void onHided(IEngineContext ctx) {
        super.onHided(ctx);
        entities.clear();
        if(plane instanceof IDisposable iDisposable)
            iDisposable.dispose();
    }

    @Override
    public void onUpdate(IEngineContext ctx) {
        super.onUpdate(ctx);
        if(startTime+timeOut<=System.currentTimeMillis())
            ctx.setScene(nextScene);

    }

    private void genScene(IEngineContext eCtx) {
        shader = eCtx.getResourceManager().getResource(ShaderPrograms.TEXTURED_COLOR_SOLID);
        plane = eCtx.getResourceFabric().createPlane("M_Plane");
        Entity logo = new Entity("Logo");
        logo.addComponent(new BillboardComponent().applyOnceAndRemove());
        var logo_mat = new AE_Material();
        logo_mat.setTexture(eCtx.getResourceManager().getResource(Textures.LOGO, Texture.class));
        logo_mat.setShaderProgram(shader);
        logo.addComponent(logo_mat);

        var logo_model = new AE_Model();
        logo_model.setMesh(plane);
        logo.addComponent(logo_model);
        var logo_transform = new AE_Transform();
        logo_transform.scale.set(new Vector3f(4,1,2));
        logo_transform.position.set(new Vector3f(1f,0,0));
        logo.addComponent(logo_transform);

        Entity gear = new Entity("Gear");
        gear.addComponent(new BillboardComponent().applyOnceAndRemove());
        var gear_mat = new AE_Material();
        gear_mat.setTexture(eCtx.getResourceManager().getResource(Textures.GEAR_BIG, Texture.class));
        gear_mat.setShaderProgram(shader);
        gear.addComponent(gear_mat);

        var gear_model = new AE_Model();
        gear_model.setMesh(plane);
        gear.addComponent(gear_model);
        var gear_transform = new AE_Transform();
        gear_transform.position.set(new Vector3f(-2,0,-0.1f));
        gear_transform.scale.set(new Vector3f(2,1,2));
        gear.addComponent(gear_transform);
        gear.addComponent(new RotatorComponent(new Vector3f(0,1,0)));

        add(gear);
        add(logo);

    }
}
