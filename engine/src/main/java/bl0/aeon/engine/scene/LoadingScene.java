package bl0.aeon.engine.scene;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.Stage;

public class LoadingScene extends BaseScene {

    @Override
    public void onShowed(IEngineContext ctx) {
        super.onShowed(ctx);
        ctx.getDispatcher().dispatch(Stage.BEFORE_SCENE_UPDATE, this::genScene);
    }

    @Override
    public void onHided(IEngineContext ctx) {
        super.onHided(ctx);
        entities.clear();
    }


    private void genScene(IEngineContext iEngineContext) {

    }
}
