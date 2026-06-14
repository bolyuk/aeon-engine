package bl0.aeon.engine.data.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.component.ui.UIDrawableElement;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.api.scene.SceneObject;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.api.scene.properties.Vector2fProperty;
import bl0.aeon.engine.data.component.ui.props.HorizontalAlignment;
import bl0.aeon.engine.data.component.ui.props.VerticalAlignment;
import bl0.aeon.render.api.c.resources.ShaderPrograms;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.api.component.ui.UIContainer;
import bl0.bjs.common.core.event.action.Action;
import org.joml.Vector2f;

public class BaseUIElement extends SceneObject implements UIDrawableElement {
    private UIContainer parent;

    public final Vector2fProperty pos = new Vector2fProperty();
    public final Vector2fProperty size = new Vector2fProperty();

    public VerticalAlignment verticalAlignment = null;
    public HorizontalAlignment horizontalAlignment = null;

    public Action<Void> action = null;

    public Material material = new AE_Material();
    public Mesh mesh;

    public BaseUIElement(String name, IEngineContext eCtx) {
        super(name);
        mesh = eCtx.getResourceFabric().createUIQuadMesh(name+"_mesh");
        material.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.UI_SOLID));
        size.addListener(this::recalculatePosition);
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
        if(parent != null)
            parent.sizeProperty().addListener(this::recalculatePosition);
        else if(this.parent != null)
            this.parent.sizeProperty().remListener(this::recalculatePosition);

        this.parent = parent;
        recalculatePosition(null);
    }

    @Override
    public Vector2f getPosition() {
        return pos.get();
    }

    @Override
    public Vector2fProperty positionProperty() {
        return pos;
    }

    @Override
    public Vector2f getSize() {
        return size.get();
    }

    @Override
    public Vector2fProperty sizeProperty() {
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
    public void onClick() {
        if(action != null)
            action.invoke(null);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
    }

    protected void recalculatePosition(Vector2f ignored) {
        if ((verticalAlignment == null && horizontalAlignment == null) || parent == null)
            return;

        var pSize = parent.getSize();
        var pPos = parent.positionProperty().get();

        if(parent.sizeProperty() != null) {
            if (pSize.x < size.x())
                parent.sizeProperty().setX(size.x());
            if (pSize.y < size.y())
                parent.sizeProperty().setY(size.y());
        }

        if(horizontalAlignment != null)
        switch (horizontalAlignment) {
            case RIGHT:
                pos.setX(pPos.x+pSize.x-size.x());
                break;
            case CENTER:
                pos.setX(pPos.x+pSize.x/2 -size.x()/2);
                break;
            case LEFT:
                pos.setX(0);
        }

        if(verticalAlignment != null)
        switch (verticalAlignment) {
            case CENTER:
                pos.setY(pPos.y+pSize.y/2 -size.y()/2);
                break;
            case BOTTOM:
                pos.setY(pPos.y+pSize.y - size.y());
                break;
            case TOP:
                pos.setY(pPos.y);
                break;
        }
    }
}
