package bl0.aeon.xtra.components.ui;

import bl0.aeon.api.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import bl0.aeon.engine.data.component.ui.TextObject;
import bl0.aeon.render.api.resource.Font;
import org.joml.Quaternionf;

public class TextPulseComponent extends BaseComponent implements UpdateConsumerComponent {

    private final TextObject boundText;

    private final int minSize;
    private final int maxSize;
    private final float cycleDurationMs;
    private final String nameWildCard;

    // rotation
    private final float minAngle;
    private final float maxAngle;

    private float phase = 0f;
    private boolean ascending = true;

    private long lastUpdateMs;

    public TextPulseComponent(TextObject boundText, float cycleDurationMs,
                              int minSize, int maxSize, String nameWildCard,
                              float minAngle, float maxAngle) {
        this.boundText      = boundText;
        this.cycleDurationMs = cycleDurationMs;
        this.minSize        = minSize;
        this.maxSize        = maxSize;
        this.nameWildCard   = nameWildCard;
        this.minAngle       = minAngle;
        this.maxAngle       = maxAngle;

        this.lastUpdateMs = System.currentTimeMillis();
    }

    public TextPulseComponent(TextObject boundText, float cycleDurationMs,
                              int minSize, int maxSize, String nameWildCard) {
        this(boundText, cycleDurationMs, minSize, maxSize, nameWildCard, 0f, 0f);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        long now     = System.currentTimeMillis();
        float deltaMs = now - lastUpdateMs;
        lastUpdateMs  = now;

        float step = deltaMs / (cycleDurationMs / 2f);

        if (ascending) {
            phase += step;
            if (phase >= 1f) { phase = 1f; ascending = false; }
        } else {
            phase -= step;
            if (phase <= 0f) { phase = 0f; ascending = true; }
        }

        float smooth = smoothStep(phase);

        int targetSize = Math.round(minSize + (maxSize - minSize) * smooth);
        int clampedSize = Math.max(minSize, Math.min(maxSize, targetSize));
        boundText.font.set(
                eCtx.getResourceManager().getResource(nameWildCard + clampedSize, Font.class)
        );

        if (minAngle != maxAngle) {
            float angle = minAngle + (maxAngle - minAngle) * smooth;
            Quaternionf rotation = new Quaternionf().rotationZ(angle);
            boundText.rotation.set(rotation);
        }
    }

    /** smoothstep: плавный старт и конец */
    private float smoothStep(float t) {
        return t * t * (3f - 2f * t);
    }
}