package bl0.aeon.al;

import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.SOFTLoopback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

/**
 * The manager with no speakers: what would have been played is rendered into an array on request, as fast as asked. For checking sound where nobody
 * can listen to it (the balance of the two channels, the fall with distance). Not for the game. Needs no sound card.
 */
public final class ALLoopback extends ALAudioManager {
    private final long device;
    public final int sampleRate;

    private ALLoopback(long device, long context, int sampleRate) {
        super(device, context);
        this.device = device;
        this.sampleRate = sampleRate;
    }

    public static ALLoopback open(int sampleRate) {
        long device = SOFTLoopback.alcLoopbackOpenDeviceSOFT((CharSequence) null);
        if (device == 0L) throw new IllegalStateException("no loopback device");
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer attributes = stack.ints(
                    SOFTLoopback.ALC_FORMAT_CHANNELS_SOFT, SOFTLoopback.ALC_STEREO_SOFT,
                    SOFTLoopback.ALC_FORMAT_TYPE_SOFT, SOFTLoopback.ALC_SHORT_SOFT,
                    ALC10.ALC_FREQUENCY, sampleRate,
                    0);
            return new ALLoopback(device, ALBackend.startContext(device, attributes), sampleRate);
        }
    }

    /** The next {@code seconds} of output: left and right samples in turn. */
    public short[] render(float seconds) {
        int frames = Math.round(seconds * sampleRate);
        short[] out = new short[frames * 2];
        SOFTLoopback.alcRenderSamplesSOFT(device, out, frames);
        return out;
    }
}
