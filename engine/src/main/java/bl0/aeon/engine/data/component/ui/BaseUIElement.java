package bl0.aeon.engine.data.component.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.base.component.ui.UIContainer;
import bl0.aeon.base.component.ui.UIElement;
import bl0.bjs.common.core.event.action.Action;
import org.joml.Vector2f;

public class BaseUIElement extends SceneObject implements UIElement {
    private UIContainer parent;
    private boolean isEnabled = true;

    public Vector2f pos = new Vector2f();
    public Vector2f size = new Vector2f();

    public Action<Void> action = null;

    public Material material = new AE_Material();
    public Mesh mesh;

    public BaseUIElement(String name, IEngineContext eCtx) {
        super(name);
        mesh = eCtx.getResourceFabric().createPlane(name+"_mesh");
        material.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.UI_SOLID));
    }

    public BaseUIElement(String name) {
        super(name);
    }

    @Override
    public UIContainer getParent() {
        return parent;
    }

    @Override
    public void setParent(UIContainer parent) {
        this.parent = parent;
    }

    @Override
    public Vector2f getPosition() {
        return pos;
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public void onClick() {
        if(action != null)
            action.invoke(null);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {

    }
}
