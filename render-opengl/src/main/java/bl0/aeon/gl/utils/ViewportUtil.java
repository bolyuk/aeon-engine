package bl0.aeon.gl.utils;

public final class ViewportUtil {

    public static int[] calcViewport(int winW, int winH, float targetAspect) {
        float windowAspect = (float) winW / (float) winH;

        int vpX, vpY, vpW, vpH;

        if (windowAspect > targetAspect) {
            vpH = winH;
            vpW = Math.round(winH * targetAspect);
            vpX = (winW - vpW) / 2;
            vpY = 0;
        } else {
            vpW = winW;
            vpH = Math.round(winW / targetAspect);
            vpX = 0;
            vpY = (winH - vpH) / 2;
        }

        return new int[]{vpX, vpY, vpW, vpH};
    }

    private ViewportUtil() {}
}
