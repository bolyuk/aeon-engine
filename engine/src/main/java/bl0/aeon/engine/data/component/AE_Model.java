package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.graphic.Model;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Mesh;

public class AE_Model extends BaseComponent implements Model {
    private Mesh mesh;

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void setMesh(IResource mesh) {
        if(mesh instanceof Mesh m) {
            this.mesh = m;
        } else
            throw new IllegalArgumentException("Invalid mesh resource: " + mesh);
    }
}
