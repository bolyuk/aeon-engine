package bl0.aeon.engine.core;

import bl0.aeon.base.component.interfaces.InputConsumerComponent;
import bl0.aeon.base.component.interfaces.InstancesContainerComponent;
import bl0.aeon.base.interfaces.IInputConsumer;
import bl0.aeon.base.scene.IComponentContainer;
import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Model;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.base.scene.Scene;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.base.stage.Stage;
import bl0.aeon.engine.Dispatcher;
import bl0.aeon.engine.ResourceManager;
import bl0.aeon.engine.data.render.InstancedRenderObj;
import bl0.aeon.engine.data.render.RenderObj;
import bl0.aeon.engine.data.component.light.AE_DirectionalLight;
import bl0.aeon.engine.data.component.light.AE_PointLight;
import bl0.aeon.engine.scene.BaseScene;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.c.resources.Textures;
import bl0.aeon.render.common.core.IResourceFabric;
import bl0.aeon.render.common.core.IResourceManager;
import bl0.aeon.render.common.core.RenderEngine;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.data.input.Key;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;
import bl0.bjs.logging.ILogger;

import java.util.ArrayList;
import java.util.List;

public class AeonEngine extends BJSBaseClass implements IEngineContext {

    private final RenderEngine renderEngine;
    private final IResourceManager resourceManager;
    private final IResourceFabric resourceFabric;

    private final Dispatcher dispatcher;
    private Scene scene;

    private final Object lock = new Object();

    private final FrameContext fCtx = new FrameContext();
    private double lastTime = 0;
    private boolean isRunning = true;

    public AeonEngine(IContext ctx, RenderEngine renderEngine) {
        super(ctx);
        this.renderEngine = renderEngine;
        this.resourceFabric = renderEngine.getFabric();
        this.resourceManager = new ResourceManager(ctx);
        this.dispatcher = new Dispatcher();
    }

    public void initialize(String title, int w, int h){
        renderEngine.initialize(title, w, h);
        fCtx.height = h;
        fCtx.width = w;
        renderEngine.bindContext();
    }

    public void loadDefaultResources(){
        loadAndSaveTexture("textures/error-texture.png", Textures.ERROR);
        loadAndSaveTexture("textures/gear-b-texture.png", Textures.GEAR_BIG);
        loadAndSaveTexture("textures/logo-texture.png", Textures.LOGO);

        loadAndSaveShader("shaders/texture_shadow", ShaderPrograms.TEXTURED_COLOR_SHADOW);
        loadAndSaveShader("shaders/color_solid", ShaderPrograms.COLOR_SOLID);
        loadAndSaveShader("shaders/texture_solid", ShaderPrograms.TEXTURED_COLOR_SOLID);
        loadAndSaveShader("shaders/instanced_texture_solid", ShaderPrograms.INSTANCED_TEXTURED_COLOR_SOLID);
    }

    public void loadAndSaveTexture(String path, String name) {
        var texture = resourceFabric.loadTextureFromResourcePath(path, name);
        resourceManager.registerResource(texture);
    }

    public void loadAndSaveShader(String path, String name) {
        var shaderProgram = resourceFabric.loadShaderProgramFromResourcePath(path, name);
        resourceManager.registerResource(shaderProgram);
    }

    public void start(){
        cycle(); //TODO
    }

    private void cycle(){
        lastTime = renderEngine.getTime();

        while (isRunning) {
            renderEngine.pollEvents();
            double now = renderEngine.getTime();
            fCtx.deltaTime = now - lastTime;

            synchronized (lock) {
                try {
                    if(scene == null) continue;

                    onKeyUpdate(); // but not sure if it is the right place.
                    dispatcher.fire(Stage.BEFORE_SCENE_UPDATE, this);
                    onUpdate();
                    dispatcher.fire(Stage.AFTER_SCENE_UPDATE, this);
                    dispatcher.fire(Stage.BEFORE_SCENE_RENDER, this);
                    onRender();
                    dispatcher.fire(Stage.AFTER_SCENE_RENDER, this);
                } catch (Exception e) {
                    l.err(e);
                }
            }
            lastTime = now;
            if(isRunning)
                renderEngine.swapBuffers();
        }
    }

    private void onKeyUpdate(){
        var input = renderEngine.pollInputData();
        if(input == null) return;

        if(input.isKeyDown(Key.ESCAPE)) {
            dispatcher.dispatch(Stage.AFTER_SCENE_RENDER, (c)->{
                renderEngine.terminate();
                isRunning = false;
            });
        }

        // TODO probably need to stop on first onInput() == true but still not sure.
        for (SceneObject so : scene.getSceneObjects()) {
            if (so instanceof IComponentContainer c) {
               var consumersList = c.getEveryComponent(InputConsumerComponent.class);
               for (var consumer : consumersList)
                   consumer.onInput(input, this);
            } else if(so instanceof IInputConsumer ic){
                ic.onInput(input, this);
            }
        }
    }

    private void onRender() {
        List<SceneObject> sceneObjects = scene.getSceneObjects();

        ArrayList<IRenderable> prepared = new ArrayList<>(sceneObjects.size());
        DirectionalLight directionalLight = null;
        ArrayList<PointLight> pointLights = new ArrayList<>();

        for (SceneObject so : sceneObjects) {
            if (!(so instanceof IComponentContainer c)) continue;

            if (directionalLight == null && c.hasComponent(AE_DirectionalLight.class))
                directionalLight = c.getComponent(AE_DirectionalLight.class);

            if (c.hasComponent(AE_PointLight.class)) {
                PointLight pl = c.getComponent(AE_PointLight.class);
                if (pl != null) pointLights.add(pl);
            }
        }

        for (SceneObject so : sceneObjects) {

            if (so instanceof IRenderable r) {
                prepared.add(r);
                continue;
            }

            if (!(so instanceof IComponentContainer c)) continue;

            Transform transform = c.getComponent(Transform.class);
            Model model = c.getComponent(Model.class);
            Material material = c.getComponent(Material.class);
            var iccs = c.getEveryComponent(InstancesContainerComponent.class);

            if(iccs != null && !iccs.isEmpty())
                for (var icc : iccs)
                    prepared.add(new InstancedRenderObj(
                            icc.getMaterial(),
                            icc,
                            icc.getModel().getMesh(),
                            null,
                            null,
                            icc.getMaterial().isDepthTestEnabled()
                    ));

            if (transform == null || model == null || material == null) continue;

            prepared.add(new RenderObj(
                    material.getShaderProgram(),
                    material.getColor(),
                    model.getMesh(),
                    transform.getMatrix(),
                    directionalLight,
                    pointLights,
                    material.getTexture(),
                    material.isDepthTestEnabled()
            ));
        }

        renderEngine.render(new RenderFrame(scene.getCamera(), prepared));
    }

    private void onUpdate() {
        scene.onUpdate(this);
    }

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public IResourceFabric getResourceFabric() {
        return resourceFabric;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void setScene(Scene scene) {
        synchronized (lock) {
            if(this.scene != null && this.scene instanceof BaseScene bs)
                bs.onHided(this);

            this.scene = scene;
            if(scene instanceof BaseScene bs)
                bs.onShowed(this);
        }
    }

    @Override
    public IFrameContext getFrameContext() {
        return fCtx;
    }

    @Override
    public ILogger getDefaultLogger() {
        return l;
    }
}
