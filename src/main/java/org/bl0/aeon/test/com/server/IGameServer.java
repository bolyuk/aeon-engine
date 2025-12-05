package org.bl0.aeon.test.com.server;

import org.bl0.aeon.core.entity.Entity;
import org.bl0.aeon.test.com.IGameService;

import java.util.List;

public interface IGameServer extends IGameService {
    public List<Entity> getEntities();
}
