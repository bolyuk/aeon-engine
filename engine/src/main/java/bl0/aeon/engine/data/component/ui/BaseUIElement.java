package bl0.aeon.engine.data.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.component.ui.UIDrawableElement;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.api.scene.SceneObject;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.api.scene.properties.Vector2fProperty;
import bl0.aeon.render.api.c.resources.ShaderPrograms;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.api.component.ui.UIContainer;
import bl0.bjs.common.core.event.action.Action;
import org.joml.Vector2f;

public class BaseUIElement extends SceneObject implements UIDrawableElement {
    private UIContainer parent;
    private boolean isEnabled = true;

    public final Vector2fProperty pos = new Vector2fProperty();
    public final Vector2fProperty size = new Vector2fProperty();

    public Alignment verticalAlignment = null;
    public Alignment horizontalAlignment = null;

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
        return pos.get();
    }

    @Override
    public Vector2f getSize() {
        return size.get();
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
        recalculatePosition();
    }

    protected void recalculatePosition() {
        if (verticalAlignment == null && horizontalAlignment == null && parent == null)
            return;
    }
}
