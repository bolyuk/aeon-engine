package bl0.aeon.base.component.interfaces;

import bl0.aeon.base.component.Component;
import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Model;

import java.nio.FloatBuffer;

public interface InstancesContainerComponent  extends Component {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
    Material getMaterial();
    void setMaterial(Material material);
    Model getModel();
    void setModel(Model model);
}
