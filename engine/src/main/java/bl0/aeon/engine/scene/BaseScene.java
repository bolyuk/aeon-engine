package bl0.aeon.engine.scene;

import java.util.ArrayList;
import java.util.List;

import bl0.aeon.api.component.ui.UIContainer;
import bl0.aeon.api.component.ui.UIElement;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.scene.Scene;
import bl0.aeon.api.scene.SceneObject;
import bl0.aeon.api.scene.properties.QuaternionfProperty;
import bl0.aeon.api.scene.properties.Vector2fProperty;
import bl0.aeon.render.api.data.render.Camera;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class BaseScene implements Scene, UIContainer {

    public final Vector2fProperty sizeProperty = new Vector2fProperty();
    private final Vector2fProperty positionProperty = new Vector2fProperty();

    protected ArrayList<SceneObject> entities = new ArrayList();
    protected Camera camera = new AE_Camera();

    protected IEngineContext eCtx;

    @Override
    public void onUpdate(IEngineContext ctx) {
        for(SceneObject object : entities) {
            object.update(ctx.getFrameContext(), ctx);
        }
    }

    public void onShowed(IEngineContext ctx) {
        this.eCtx = ctx;
        camera.setAspectRatio(ctx.getFrameContext().sizeProperty().getAspectRatio());
        sizeProperty.bind(ctx.getFrameContext().sizeProperty());;
    }

    public void onHided(IEngineContext ctx) {
        this.eCtx = null;
        sizeProperty.unbind(ctx.getFrameContext().sizeProperty());;
    }

    @Override
    public List<SceneObject> getSceneObjects() {
        return entities;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void add(SceneObject sceneObject) {
        entities.add(sceneObject);
        if(sceneObject instanceof UIElement uie)
            uie.setParent(this);
    }

    @Override
    public void remove(SceneObject sceneObject) {
        entities.remove(sceneObject);
    }

    @Override
    public List<UIElement> getUIElements() {
        return null;
    }

    @Override
    public Vector2fProperty sizeProperty() {
        return sizeProperty;
    }

    @Override
    public Quaternionf getRotation() {
        return null;
    }

    @Override
    public QuaternionfProperty rotationProperty() {
        return null;
    }


    @Override
    public Vector2f getSize() {
        return sizeProperty.get();
    }

    @Override
    public UIContainer getParent() {
        return null; //TODO
    }

    @Override
    public void setParent(UIContainer parent) {

    }

    @Override
    public Vector2f getPosition() {
        return positionProperty.get();
    }

    @Override
    public Vector2fProperty positionProperty() {
        return positionProperty;
    }

    @Override
    public void onClick() {
        //TODO
    }

    @Override
    public String getName() {
        return "_SCENE";
    }
}

