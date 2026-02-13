package bl0.aeon.api.component.interfaces;

import bl0.aeon.api.component.Component;
import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.component.graphic.Model;

import java.nio.FloatBuffer;

public interface InstancesContainerComponent  extends Component {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
    Material getMaterial();
    void setMaterial(Material material);
    Model getModel();
    void setModel(Model model);
}
