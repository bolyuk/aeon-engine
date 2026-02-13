package bl0.aeon.api.interfaces;

import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.render.api.data.input.InputData;

public interface IInputConsumer {
    boolean onInput(InputData data, IEngineContext ctx);
}
