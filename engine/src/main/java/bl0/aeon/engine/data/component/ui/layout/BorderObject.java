package bl0.aeon.engine.data.component.ui.layout;

import bl0.aeon.api.component.ui.UIContainer;
import bl0.aeon.api.component.ui.UIDrawableElement;
import bl0.aeon.api.component.ui.UIElement;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.scene.properties.Vector2fProperty;
import bl0.aeon.engine.data.component.ui.BaseUIElement;
import bl0.bjs.common.base.IContext;

import java.util.ArrayList;
import java.util.List;

public class BorderObject extends BaseUIElement implements UIContainer, UIDrawableElement {
    private final ArrayList<UIElement> children = new ArrayList<>();

    public BorderObject(String name, IEngineContext eCtx) {
        super(name, eCtx);
    }

    @Override
    public List<UIElement> getUIElements() {
        return children;
    }

    public void addChild(UIElement child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void setParent(UIContainer parent) {
        super.setParent(parent);
        for (UIElement child : children) {
            child.setParent(this);
        }
    }
}
