package org.bl0.aeon.test.com;

import bl0.bjs.socket.services.IWebSocketService;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface IGameService extends IWebSocketService {
    void commitMove(Vector3f pos, Vector3f direction, String name);

    void acceptMove(Vector3f pos, Vector3f direction, String name);
}
