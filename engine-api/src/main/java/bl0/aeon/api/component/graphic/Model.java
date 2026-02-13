package bl0.aeon.api.component.graphic;

import bl0.aeon.api.component.Component;
import bl0.aeon.render.api.base.IResource;
import bl0.aeon.render.api.resource.Mesh;

public interface Model extends Component {
    Mesh getMesh();

    void setMesh(IResource mesh);
}
