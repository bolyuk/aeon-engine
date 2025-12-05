package org.bl0.aeon.test.com.client;

import bl0.bjs.common.base.IContext;
import bl0.bjs.socket.core.communication.WSClient;
import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.scene.IScene;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.net.URI;

public class ClientScene extends WSClient implements IGameClient, IScene {

    public ClientScene(IContext context, URI serverUri, String name) {
        super(context, serverUri, name);
    }

    @Override
    public void OnBeforeRender(DrawContext var1, GameContext var2) {

    }

    @Override
    public void OnBeforeUpdate(GameContext var1) {

    }

    @Override
    public void add(Entity entity) {

    }

    @Override
    public void update(GameContext gameCtx) {

    }

    @Override
    public void render(DrawContext drawCtx, GameContext gameCtx) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Camera getCamera() {
        return null;
    }

    @Override
    public void bind(GameContext var1) {

    }

    @Override
    public void unbind(GameContext var1) {

    }

    @Override
    public void commitMove(Vector3f pos, Vector3f direction, String name) {

    }

    @Override
    public void acceptMove(Vector3f pos, Vector3f direction, String name) {

    }
}
