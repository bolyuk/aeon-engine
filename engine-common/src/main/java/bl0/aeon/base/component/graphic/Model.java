package bl0.aeon.base.component.graphic;

import bl0.aeon.base.component.Component;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Mesh;

public interface Model extends Component {
    Mesh getMesh();

    void setMesh(IResource mesh);
}
