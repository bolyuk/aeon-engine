package bl0.aeon.base.events;

import bl0.bjs.eventbus.IEventBusNode;

public interface ViewPortChangeEvent extends IEventBusNode<ViewPortChangeEvent.VPCEPayload> {
    record VPCEPayload(int width, int height, float aspectRatio) {}
}
