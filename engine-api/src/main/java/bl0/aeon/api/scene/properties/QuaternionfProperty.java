package bl0.aeon.api.scene.properties;

import bl0.bjs.common.core.relations.v2.Property;
import org.joml.Quaternionf;

public class QuaternionfProperty extends Property<Quaternionf> {
    public QuaternionfProperty() {
        this.value = new Quaternionf();
    }

    public QuaternionfProperty(Quaternionf value) {
        this.value = value;
    }
}
