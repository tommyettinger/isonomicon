package isonomicon.app;

import com.github.tommyettinger.anim8.Dithered.DitherAlgorithm;

public final class AppConfig {
    private AppConfig() {
    }

    public static final DitherAlgorithm DITHER = DitherAlgorithm.GOURD;
    public static final float STRENGTH = 0.3f;
//
//    public static final DitherAlgorithm DITHER = DitherAlgorithm.LOAF;
//    public static final float STRENGTH = 0.2_0f;
}
