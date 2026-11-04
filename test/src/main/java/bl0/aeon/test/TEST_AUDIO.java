package bl0.aeon.test;

import bl0.aeon.al.ALAudioManager;
import bl0.aeon.al.ALBackend;
import bl0.aeon.al.ALLoopback;
import bl0.aeon.audio.api.IAudioManager;
import bl0.aeon.audio.api.ISound;
import bl0.aeon.audio.api.IVoice;
import bl0.aeon.audio.api.NullAudioManager;
import bl0.aeon.audio.api.SoundParams;

/**
 * Checks the sound without anybody listening. Most of it renders through {@link ALLoopback} and measures the samples, the rest asks the real device
 * about its sources. Prints what it measured; the exit code is the number of failed checks.
 */
public class TEST_AUDIO {
    private static final int RATE = 44100;
    /** OpenAL Soft dithers its output, so silence measures about 1 and not 0. */
    private static final double SILENCE = 3;
    private static int failed;

    public static void main(String[] args) throws Exception {
        rendered(args);
        noDevice();
        realDevice();
        System.out.println(failed == 0 ? "ALL PASSED" : failed + " FAILED");
        System.exit(failed);
    }

    private static void check(String what, boolean ok, String measured) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what + "  [" + measured + "]");
        if (!ok) failed++;
    }

    private static short[] tone(float freq, float seconds) {
        short[] pcm = new short[(int) (RATE * seconds)];
        for (int i = 0; i < pcm.length; i++) pcm[i] = (short) (Math.sin(2 * Math.PI * freq * i / RATE) * 20000);
        return pcm;
    }

    /** Root mean square of one channel: 0 left, 1 right. */
    private static double rms(short[] stereo, int channel) {
        double sum = 0;
        for (int i = channel; i < stereo.length; i += 2) sum += (double) stereo[i] * stereo[i];
        return Math.sqrt(sum / (stereo.length / 2.0));
    }

    private static int crossings(short[] stereo) {
        int n = 0;
        for (int i = 2; i < stereo.length; i += 2) if (stereo[i - 2] < 0 != stereo[i] < 0) n++;
        return n;
    }

    /** Level of both channels together for a looping tone at this point. */
    private static double level(ALLoopback al, ISound s, SoundParams p) {
        IVoice v = al.play(s, p.looping(true));
        short[] out = al.render(0.2f);
        v.stop();
        al.render(0.02f);
        return rms(out, 0) + rms(out, 1);
    }

    /** @param files classpath resources that should load and sound, to try other formats */
    private static void rendered(String[] files) {
        ALLoopback al = ALLoopback.open(RATE);
        System.out.println("-- loopback, " + al.sourceCount() + " sources");
        ISound s = al.createSound("tone", tone(440f, 0.25f), 1, RATE);
        check("sound length", Math.abs(s.getDuration() - 0.25f) < 0.001f, s.getDuration() + " s");

        // the listener at the origin looks along -z, so +x is to the right
        al.setListener(0, 0, 0, 0, 0, -1, 0, 1, 0);
        IVoice right = al.play(s, SoundParams.at(10, 0, 0).looping(true).reach(20, 200));
        short[] out = al.render(0.2f);
        double l = rms(out, 0), r = rms(out, 1);
        check("a source on the right is louder on the right", r > l * 2, String.format("L %.0f R %.0f", l, r));
        check("it is playing", right.isPlaying(), "AL_PLAYING");

        al.setListener(0, 0, 0, 0, 0, 1, 0, 1, 0);          // turned around
        out = al.render(0.2f);
        l = rms(out, 0);
        r = rms(out, 1);
        check("turning the listener around swaps the sides", l > r * 2, String.format("L %.0f R %.0f", l, r));

        al.setListener(20, 0, 0, 0, 0, -1, 0, 1, 0);        // moved past it: now it is on the left
        out = al.render(0.2f);
        l = rms(out, 0);
        r = rms(out, 1);
        check("moving the listener past it swaps the sides", l > r * 2, String.format("L %.0f R %.0f", l, r));
        right.stop();
        al.render(0.02f);

        al.setListener(0, 0, 0, 0, 0, -1, 0, 1, 0);
        double at0 = level(al, s, SoundParams.at(0, 0, -3).reach(4, 64));
        double at34 = level(al, s, SoundParams.at(0, 0, -34).reach(4, 64));
        double at64 = level(al, s, SoundParams.at(0, 0, -64).reach(4, 64));
        double at100 = level(al, s, SoundParams.at(0, 0, -100).reach(4, 64));
        check("full inside the reference distance, half midway", at34 > at0 * 0.4 && at34 < at0 * 0.6, String.format("%.0f, %.0f", at0, at34));
        check("silent at the maximum distance and beyond", at64 < SILENCE && at100 < SILENCE, String.format("%.1f, %.1f", at64, at100));

        double local = level(al, s, SoundParams.local());
        check("a local sound does not depend on the listener", local > at0 * 0.8, String.format("%.0f", local));
        double half = level(al, s, SoundParams.local().volume(0.5f));
        check("volume", half > local * 0.4 && half < local * 0.6, String.format("%.0f of %.0f", half, local));

        al.setMasterVolume(0.5f);
        double master = level(al, s, SoundParams.local());
        al.setMasterVolume(0f);
        double mute = level(al, s, SoundParams.local());
        al.setMasterVolume(1f);
        check("master volume", master > local * 0.4 && master < local * 0.6 && mute < SILENCE, String.format("half %.0f, zero %.1f", master, mute));

        IVoice v = al.play(s, SoundParams.local().looping(true));
        int normal = crossings(al.render(0.2f));
        v.setPitch(2f);
        int doubled = crossings(al.render(0.2f));
        check("pitch", doubled > normal * 1.8 && doubled < normal * 2.2, normal + " -> " + doubled + " crossings");

        al.render(1f);
        check("a loop outlasts its sound", v.isPlaying(), "after 1.4 s of 0.25 s");
        v.setLooping(false);
        al.render(0.5f);
        check("taking the loop off lets it end", !v.isPlaying(), "");
        IVoice once = al.play(s, SoundParams.local());
        check("a plain sound starts", once.isPlaying(), "");
        al.render(0.3f);
        check("and ends by itself", !once.isPlaying(), "");
        once.setVolume(1f);       // an ended voice: nothing happens
        once.stop();

        check("the pool is empty again", al.activeVoices() == 0, al.activeVoices() + " active");
        IVoice first = al.play(s, SoundParams.local());
        IVoice last = first;
        for (int i = 0; i < al.sourceCount() + 5; i++) last = al.play(s, SoundParams.local());
        check("more sounds than sources: the oldest gives way", !first.isPlaying() && last.isPlaying() && al.activeVoices() == al.sourceCount(), al.activeVoices() + " active");
        al.stopAll();
        check("stop all", al.activeVoices() == 0, "");

        IVoice[] loops = new IVoice[al.sourceCount()];
        for (int i = 0; i < loops.length; i++) loops[i] = al.play(s, SoundParams.local().looping(true));
        IVoice extra = al.play(s, SoundParams.local());
        boolean loopsAlive = true;
        for (IVoice loop : loops) loopsAlive &= loop.isPlaying();
        check("all sources looping: the new sound does not play, the loops go on", !extra.isPlaying() && loopsAlive, "");
        al.stopAll();

        ISound missing = al.loadSound("missing", "no/such/file.wav");
        check("a missing file gives a silent sound", missing.getDuration() == 0 && !al.play(missing, SoundParams.local()).isPlaying(), "");
        for (String file : files) {
            ISound f = al.loadSound(file, file);
            IVoice p = al.play(f, SoundParams.local());
            short[] rendered = al.render(Math.min(0.5f, f.getDuration()));
            p.stop();
            double sounded = rms(rendered, 0);
            check(file + " loads and sounds", f.getDuration() > 0 && sounded > 100, String.format("%.2f s, %d ch, level %.0f", f.getDuration(), f.getChannels(), sounded));
        }
        check("no OpenAL errors", al.takeError() == 0, "alGetError " + al.takeError());

        al.dispose();
        al.dispose();
        check("after closing it does nothing", !al.isAvailable() && !al.play(s, SoundParams.local()).isPlaying(), "");
    }

    private static void noDevice() {
        System.out.println("-- -Daeon.noAudio");
        System.setProperty("aeon.noAudio", "true");
        IAudioManager none = ALBackend.open();
        System.clearProperty("aeon.noAudio");
        check("no device gives the empty manager", none instanceof NullAudioManager && !none.isAvailable(), none.getClass().getSimpleName());
        ISound s = none.loadSound("x", "no/such/file.wav");
        IVoice v = none.play(s, SoundParams.at(1, 2, 3).looping(true));
        v.setVolume(0.5f);
        v.setPosition(1, 1, 1);
        none.setListener(0, 0, 0, 0, 0, -1, 0, 1, 0);
        none.setMasterVolume(0.5f);
        none.update();
        none.stopAll();
        none.dispose();
        check("and every call on it does nothing", !v.isPlaying() && none.activeVoices() == 0, "");
    }

    private static void realDevice() throws InterruptedException {
        System.out.println("-- the real device");
        IAudioManager audio = ALBackend.open();
        if (!(audio instanceof ALAudioManager al)) {
            System.out.println("skip  no sound device on this machine");
            return;
        }
        check("opened", al.isAvailable(), al.sourceCount() + " sources");
        al.setMasterVolume(0.05f);          // somebody may be wearing headphones
        al.setListener(0, 0, 0, 0, 0, -1, 0, 1, 0);
        ISound s = al.createSound("tone", tone(330f, 0.3f), 1, RATE);
        IVoice once = al.play(s, SoundParams.at(2, 0, -2));
        IVoice loop = al.play(s, SoundParams.local().looping(true));
        check("sources are playing", once.isPlaying() && loop.isPlaying() && al.activeVoices() == 2, al.activeVoices() + " active");
        Thread.sleep(600);
        check("the plain one has ended in real time, the loop has not", !once.isPlaying() && loop.isPlaying(), al.activeVoices() + " active");
        loop.stop();
        check("the loop stops", !loop.isPlaying() && al.activeVoices() == 0, "");
        check("no OpenAL errors", al.takeError() == 0, "");
        al.dispose();
        check("closed", !al.isAvailable(), "");

        // left open and playing on purpose: the end of the process has to close it (the engine just returns from its loop, nothing is disposed there)
        IAudioManager again = ALBackend.open();
        again.setMasterVolume(0.05f);
        again.play(again.createSound("tone", tone(330f, 0.3f), 1, RATE), SoundParams.local().looping(true));
        check("opens again after closing, and is left open", again.isAvailable() && again.activeVoices() == 1, "");
    }
}
