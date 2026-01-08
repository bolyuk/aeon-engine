package bl0.aeon.engine.core;

import bl0.aeon.base.component.interfaces.IWindowSizeChangeConsumerComponent;
import bl0.aeon.base.component.interfaces.InputConsumerComponent;
import bl0.aeon.base.component.interfaces.InstancesContainerComponent;
import bl0.aeon.base.events.ViewPortChangeEvent;
import bl0.aeon.base.interfaces.IInputConsumer;
import bl0.aeon.base.interfaces.IWindowSizeChangeConsumer;
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
import bl0.aeon.engine.data.ActionTags;
import bl0.aeon.engine.data.render.InstancedRenderObj;
import bl0.aeon.engine.data.render.RenderObj;
import bl0.aeon.engine.data.component.light.AE_DirectionalLight;
import bl0.aeon.engine.data.component.light.AE_PointLight;
import bl0.aeon.engine.scene.BaseScene;
import bl0.aeon.render.common.backend.BackendContainer;
import bl0.aeon.render.common.c.resources.Fonts;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.c.resources.Textures;
import bl0.aeon.render.common.backend.IResourceFabric;
import bl0.aeon.base.core.IResourceManager;
import bl0.aeon.render.common.data.render.RenderFrame;
import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;
import bl0.bjs.common.core.tuple.Pair;
import bl0.bjs.eventbus.IEventBus;
import bl0.bjs.logging.ILogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AeonEngine extends BJSBaseClass implements IEngineContext {

    private final BackendContainer backend;
    private final IResourceManager resourceManager;

    private final Dispatcher dispatcher;
    private Scene scene;

    private final Object lock = new Object();

    private final FrameContext fCtx = new FrameContext();
    private double lastTime = 0;
    private boolean isRunning = true;

    private int fpsLimit = 60;

    private HashMap<Key, Long> sysKeysDelayData = new HashMap<>();

    private long sysKeyDelay = 300;

    public AeonEngine(IContext ctx, BackendContainer backend) {
        super(ctx);
        this.backend = backend;
        this.resourceManager = new ResourceManager(ctx);
        this.dispatcher = new Dispatcher();
        backend.getWindowManager().setOnWindowSizeChangedListener(this::winChanged);
        regEvents();
    }

    private void regEvents() {
       var eBus = this.ctx.getEventBus();
       eBus.getController(ViewPortChangeEvent.class).subscribe(this::vpcheListener);
    }

    private void vpcheListener(ViewPortChangeEvent.VPCEPayload VPCEPayload) {
        dispatcher.dispatchUnique(Stage.SYSTEM, ActionTags.WIN_RESIZE_CALLBACK_TAG ,(c) -> {
            this.fCtx.width = VPCEPayload.width();
            this.fCtx.height = VPCEPayload.height();
            backend.getWindowManager().changeViewPort(VPCEPayload.width(), VPCEPayload.height(), VPCEPayload.aspectRatio());
        });
    }

    private void winChanged(Pair<Integer, Integer> wh) {
        enqueueWinSizeChange(wh);
    }

    private void enqueueWinSizeChange(Pair<Integer, Integer> wh) {
        dispatcher.dispatchUnique(Stage.BEFORE_SCENE_UPDATE, ActionTags.WIN_RESIZE_TAG, (eCtx) -> {
            this.fCtx.width = wh.first;
            this.fCtx.height = wh.second;
            if(scene == null) return;
            for(var so : scene.getSceneObjects()){
                if(so instanceof IComponentContainer ico)
                    for(var consumer : ico.getEveryComponent(IWindowSizeChangeConsumerComponent.class))
                        consumer.onWindowSizeChange(eCtx);

                if(so instanceof IWindowSizeChangeConsumer consumer)
                    consumer.onWindowSizeChange(eCtx);
            }
        });
    }

    public void initialize(String title, int w, int h){
        printLogo();
        var winManager = backend.getWindowManager();
        winManager.initialize(title, w, h);
        winManager.bindContext();
        fCtx.height = h;
        fCtx.width = w;
    }
    
    private void printLogo(){
        System.out.println(
                " ________  _______   ________  ________      \n" +
                "|\\   __  \\|\\  ___ \\ |\\   __  \\|\\   ___  \\    \n" +
                "\\ \\  \\|\\  \\ \\   __/|\\ \\  \\|\\  \\ \\  \\\\ \\  \\   \n" +
                " \\ \\   __  \\ \\  \\_|/_\\ \\  \\\\\\  \\ \\  \\\\ \\  \\  \n" +
                "  \\ \\  \\ \\  \\ \\  \\_|\\ \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \n" +
                "   \\ \\__\\ \\__\\ \\_______\\ \\_______\\ \\__\\\\ \\__\\\n" +
                "    \\|__|\\|__|\\|_______|\\|_______|\\|__| \\|__|\n" +
                "\n");
    }
    public void loadDefaultResources(){
        loadAndSaveTexture("textures/error-texture.png", Textures.ERROR);
        loadAndSaveTexture("textures/gear-b-texture.png", Textures.GEAR_BIG);
        loadAndSaveTexture("textures/logo-texture.png", Textures.LOGO);

        loadAndSaveShader("shaders/texture_shadow", ShaderPrograms.TEXTURED_COLOR_SHADOW);
        loadAndSaveShader("shaders/color_solid", ShaderPrograms.COLOR_SOLID);
        loadAndSaveShader("shaders/texture_solid", ShaderPrograms.TEXTURED_COLOR_SOLID);
        loadAndSaveShader("shaders/instanced_texture_solid", ShaderPrograms.INSTANCED_TEXTURED_COLOR_SOLID);

        var font = backend.getResourceFabric().loadFontFromResourcePath("fonts/default-font.ttf", Fonts.DEFAULT, 16);
        resourceManager.registerResource(font);
        loadAndSaveShader("shaders/text_solid", ShaderPrograms.TEXT_SOLID);
    }

    public void loadAndSaveTexture(String path, String name) {
        var texture = backend.getResourceFabric().loadTextureFromResourcePath(path, name);
        resourceManager.registerResource(texture);
    }

    public void loadAndSaveShader(String path, String name) {
        var shaderProgram = backend.getResourceFabric().loadShaderProgramFromResourcePath(path, name);
        resourceManager.registerResource(shaderProgram);
    }

    public void start(){
        cycle();
    }

    private void cycle() {
        lastTime = backend.getRenderEngine().getTime();

        final double targetDt = 1.0 / fpsLimit;

        while (isRunning) {
            double frameStart = backend.getRenderEngine().getTime();
            double dt = frameStart - lastTime;
            lastTime = frameStart;

            if (dt > 0.25) dt = 0.25;
            fCtx.deltaTime = dt;
            backend.getWindowManager().pollEvents();

            synchronized (lock) {
                try {
                    if (scene != null) {
                        dispatcher.fire(Stage.SYSTEM, this);
                        onKeyUpdate();
                        dispatcher.fire(Stage.BEFORE_SCENE_UPDATE, this);
                        onUpdate();
                        dispatcher.fire(Stage.AFTER_SCENE_UPDATE, this);
                        dispatcher.fire(Stage.BEFORE_SCENE_RENDER, this);
                        onRender();
                        dispatcher.fire(Stage.AFTER_SCENE_RENDER, this);
                    }
                } catch (Exception e) {
                    l.err(e);
                }
            }

            if (!isRunning)
                return;

            backend.getRenderEngine().swapBuffers();

            double frameEnd = backend.getRenderEngine().getTime();
            double frameTime = frameEnd - frameStart;
            double sleepSec = targetDt - frameTime;
            if (sleepSec > 0) {
                sleepMillisPrecise(sleepSec);
            }

            fCtx.fps = dt > 0 ? (1.0 / dt) : 0.0;
        }
    }

    private void sleepMillisPrecise(double seconds) {
        long ms = (long) (seconds * 1000.0);
        int ns = (int) ((seconds * 1_000_000_000.0) - (ms * 1_000_000L));

        try {
            if (ms > 0) Thread.sleep(ms);
            if (ns > 0) Thread.sleep(0, ns);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            l.err(e);
        }
    }


    private void onKeyUpdate(){
        var winManager = backend.getWindowManager();
        var input = winManager.pollInputData();
        if(input == null) return;
        if(isKeyDownAndDelayPassed(input, Key.ESCAPE)) {
            dispatcher.dispatchUnique(Stage.AFTER_SCENE_RENDER, ActionTags.KEY_INPUT_ESC_TAG , (c)->{
                winManager.terminate();
                isRunning = false;
            });
        }
        if(isKeyDownAndDelayPassed(input, Key.F11)) {
            dispatcher.dispatchUnique(Stage.AFTER_SCENE_RENDER,ActionTags.KEY_INPUT_F11_TAG, (c)->{
                winManager.setFullscreen(!winManager.isFullscreen());
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

    private boolean isKeyDownAndDelayPassed(InputData input, Key key) {
        sysKeysDelayData.computeIfAbsent(key, k -> 0L);
        if(input.isKeyDown(key) && sysKeysDelayData.get(key)+sysKeyDelay <= System.currentTimeMillis()) {
            sysKeysDelayData.put(key, System.currentTimeMillis());
            return true;
        }
        return false;
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

        backend.getRenderEngine().render(new RenderFrame(scene.getCamera(),
                prepared,
                getFrameContext().getWidth(),
                getFrameContext().getHeight()));
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
        return backend.getResourceFabric();
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

            scene.getCamera().setAspectRatio((float)fCtx.width/fCtx.height);
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

    @Override
    public IEventBus getEventBus() {
        return ctx.getEventBus();
    }
}
