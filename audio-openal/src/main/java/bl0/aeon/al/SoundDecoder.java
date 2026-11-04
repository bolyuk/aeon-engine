package bl0.aeon.al;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/** Files to samples: WAV (PCM of 8, 16, 24 or 32 bits, or 32 bit float) and OGG Vorbis, always to 16 bits. */
final class SoundDecoder {
    private SoundDecoder() {}

    /** The samples of a sound, interleaved. */
    record Pcm(short[] samples, int channels, int sampleRate) {}

    static Pcm decode(byte[] file, String path) throws IOException {
        return path.toLowerCase().endsWith(".ogg") ? decodeOgg(file) : decodeWav(file);
    }

    static Pcm decodeWav(byte[] file) throws IOException {
        ByteBuffer b = ByteBuffer.wrap(file).order(ByteOrder.LITTLE_ENDIAN);
        if (file.length < 12 || !tag(file, 0, "RIFF") || !tag(file, 8, "WAVE")) throw new IOException("not a WAV file");
        int format = 0, channels = 0, rate = 0, bits = 0;
        int at = 12;
        while (at + 8 <= file.length) {
            int size = b.getInt(at + 4);
            int body = at + 8;
            if (size < 0 || body + size > file.length) size = file.length - body;    // some writers leave a wrong length on the last chunk
            if (tag(file, at, "fmt ")) {
                if (size < 16) throw new IOException("broken format chunk");
                format = b.getShort(body) & 0xFFFF;
                channels = b.getShort(body + 2) & 0xFFFF;
                rate = b.getInt(body + 4);
                bits = b.getShort(body + 14) & 0xFFFF;
                if (format == 0xFFFE && size >= 26) format = b.getShort(body + 24) & 0xFFFF;     // WAVE_FORMAT_EXTENSIBLE: the real format is in the sub-format
            } else if (tag(file, at, "data")) {
                if (channels < 1 || channels > 2 || rate <= 0) throw new IOException("data before the format, or " + channels + " channels");
                return new Pcm(samples(b, body, size, format, bits), channels, rate);
            }
            at = body + size + (size & 1);          // chunks are padded to an even length
        }
        throw new IOException("no sound data");
    }

    private static short[] samples(ByteBuffer b, int at, int size, int format, int bits) throws IOException {
        int bytes = bits / 8;
        boolean pcm = format == 1 && (bits == 8 || bits == 16 || bits == 24 || bits == 32);
        boolean floats = format == 3 && bits == 32;
        if (!pcm && !floats) throw new IOException("unsupported WAV format " + format + ", " + bits + " bits");
        short[] out = new short[size / bytes];
        for (int i = 0; i < out.length; i++, at += bytes) {
            out[i] = floats ? (short) (Math.max(-1f, Math.min(1f, b.getFloat(at))) * 32767f)
                    : switch (bits) {
                        case 8 -> (short) (((b.get(at) & 0xFF) - 128) << 8);
                        case 16 -> b.getShort(at);
                        case 24 -> (short) ((b.get(at + 1) & 0xFF) | b.get(at + 2) << 8);
                        default -> (short) (b.getInt(at) >> 16);
                    };
        }
        return out;
    }

    private static boolean tag(byte[] file, int at, String tag) {
        for (int i = 0; i < 4; i++) if (file[at + i] != tag.charAt(i)) return false;
        return true;
    }

    static Pcm decodeOgg(byte[] file) throws IOException {
        ByteBuffer in = MemoryUtil.memAlloc(file.length);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            in.put(file).flip();
            IntBuffer error = stack.mallocInt(1);
            long vorbis = STBVorbis.stb_vorbis_open_memory(in, error, null);
            if (vorbis == 0L) throw new IOException("not an OGG Vorbis file, error " + error.get(0));
            try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
                STBVorbis.stb_vorbis_get_info(vorbis, info);
                int channels = info.channels();
                short[] samples = new short[STBVorbis.stb_vorbis_stream_length_in_samples(vorbis) * channels];
                int frames = STBVorbis.stb_vorbis_get_samples_short_interleaved(vorbis, channels, samples);
                return new Pcm(frames * channels == samples.length ? samples : Arrays.copyOf(samples, frames * channels), channels, info.sample_rate());
            } finally {
                STBVorbis.stb_vorbis_close(vorbis);
            }
        } finally {
            MemoryUtil.memFree(in);
        }
    }
}
