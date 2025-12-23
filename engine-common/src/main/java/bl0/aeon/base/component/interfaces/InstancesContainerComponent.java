package bl0.aeon.base.component.interfaces;

import bl0.aeon.base.component.Component;

import java.nio.FloatBuffer;

public interface InstancesContainerComponent  extends Component {
    int getInstanceCount();
    FloatBuffer getInstanceMatrices();
}
