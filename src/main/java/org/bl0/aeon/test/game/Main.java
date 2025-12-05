package org.bl0.aeon.test.game;

import bl0.bjs.boot.BJSInitializer;
import bl0.bjs.common.base.IContext;
import bl0.bjs.socket.core.data.NamedSocket;
import org.bl0.aeon.core.Engine;
import org.bl0.aeon.engine.scene.IScene;
import org.bl0.aeon.test.com.client.ClientScene;
import org.bl0.aeon.test.com.server.ServerScene;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        IContext ctx;
        IScene chanel;
        if(args.length != 0) {
            String name = "CLIENT"+ UUID.randomUUID();
            ctx = BJSInitializer.defaultInit(name);
            chanel = new ClientScene(ctx, new URI(args[0]), name);
        } else {
            ctx = BJSInitializer.defaultInit(NamedSocket.SERVER);
            chanel = new ServerScene(ctx, new InetSocketAddress("0.0.0.0",9091));
        }

        Engine gameEngine = new Engine();
        gameEngine.addResourceClassLoader(Main.class.getClassLoader());
        gameEngine.init();
        gameEngine.setScene(chanel);
        gameEngine.setInputManager(new DefaultKeyboardInputManager());
        gameEngine.start();
    }
}

