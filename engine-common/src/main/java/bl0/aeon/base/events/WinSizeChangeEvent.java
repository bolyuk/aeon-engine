package bl0.aeon.base.events;

import bl0.bjs.eventbus.IEventBusNode;

public interface WinSizeChangeEvent extends IEventBusNode<WinSizeChangeEvent.WSCEPayload> {
    record WSCEPayload (int width, int height, float aspectRatio) {}
}
