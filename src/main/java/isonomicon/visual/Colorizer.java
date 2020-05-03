package isonomicon.visual;

import com.badlogic.gdx.math.MathUtils;
import squidpony.squidmath.IRNG;
import isonomicon.io.PaletteReducer;

/**
 * Created by Tommy Ettinger on 1/13/2019.
 */
public abstract class Colorizer {
    private Colorizer() {

    }

    protected Colorizer(PaletteReducer reducer) {
        this.reducer = reducer;
    }

    protected PaletteReducer reducer;
    
    /**
     * Refers to {@link #dimmer(int, byte)} to get the answer.
     */
    public int bright(byte voxel) {
        return dimmer(3, voxel);
    }

    /**
     * Refers to {@link #dimmer(int, byte)} to get the answer.
     */
    public int medium(byte voxel) {
        return dimmer(2, voxel);
    }

    /**
     * Refers to {@link #dimmer(int, byte)} to get the answer.
     */
    public int dim(byte voxel) {
        return dimmer(1, voxel);
    }

    /**
     * Refers to {@link #dimmer(int, byte)} to get the answer.
     */
    public int dark(byte voxel) {
        return dimmer(0, voxel);
    }
    
    /**
     * Allows implementors to mark whether a Colorizer allows shading and/or outlining to be disabled for each color
     * index, determined by the status of a specific shade bit. If this returns 0, the palette does not support custom
     * shading rules. If this returns a power of two between 1 and 128, when {@code (voxel & getShadeBit()) != 0}, an
     * alternate set of shading rules will be used, which usually disables shading and outlining for color indices with
     * that bit set.
     *
     * @return 0 if this does not have configurable shading, or a power of two between 1 and 128 when that bit marks special voxels with different shading rules
     */
    public int getShadeBit() {
        return 0;
    }

    /**
     * Allows implementors to mark whether a Colorizer allows the shading of a voxel to vary depending on that voxel's
     * position in 3D space plus time, determined by the status of a specific wave bit. If this returns 0, the palette
     * does not support custom shading rules. If this returns a power of two between 1 and 128, when
     * {@code (voxel & getWaveBit()) != 0}, an alternate set of shading rules will be used. This has different behavior
     * if the bit that can be specified by {@link #getShadeBit()} is set at the same time the wave bit is specified.
     * If the wave bit is set and the shade bit (if any) is not set, then this is expected to use some form of 4D
     * continuous noise or seamless 3D noise to change the shading of a voxel, but the outline should be drawn with the
     * same color (when using {@link #dimmer(int, byte)}, brightness 0 stays the same, while the other brightnesses
     * should change using the noise). If both the wave bit and the shade bit are set, then the "wave" this refers to is
     * a pulsing light wave with a wavelength, and as with the shade bit on its own there should be no outline. The
     * pulsing effect for non-outline colors is suggested to use
     * {@code int brightness = (x + y + z + time & 3); brightness += 1 - (brightness & (brightness << 1));}, which will
     * cause the brightness to zigzag between 1 and 3, spending more time at brightness 2.
     *
     * @return 0 if this does not have wave shading, or a power of two between 1 and 128 when that bit marks special voxels that change shading over time and across space
     */
    public int getWaveBit() {
        return 0;
    }
    /**
     * @param voxel A color index
     * @return A brighter version of the voxel color, or the lightest color index in the palette if none is available.
     */
    public abstract byte brighten(byte voxel);

    /**
     * @param voxel A color index
     * @return A darker version of the same color, or the darkest color index in the palette if none is available.
     */
    public abstract byte darken(byte voxel);

    /**
     * @return An array of main colors as byte indices, chosen for aesthetic reasons as the primary colors to use.
     */
    public abstract byte[] mainColors();

    /**
     * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
     */
    public abstract byte[] grayscale();

    /**
     * @param voxel      A color index
     * @param brightness An integer representing how many shades brighter (if positive) or darker (if negative) the result should be
     * @return A different shade of the same color
     */
    public byte colorize(byte voxel, int brightness) {
        if (brightness > 0) {
            for (int i = 0; i < brightness; i++) {
                voxel = brighten(voxel);
            }
        } else if (brightness < 0) {
            for (int i = 0; i > brightness; i--) {
                voxel = darken(voxel);
            }
        }
        return voxel;
    }

    /**
     * @param color An RGBA8888 color
     * @return The nearest available color index in the palette
     */
    public byte reduce(int color) {
        return reducer.reduceIndex(color);
    }

    /**
     * Uses {@link #colorize(byte, int)} to figure out what index has the correct brightness, then looks that index up
     * in the {@link #reducer}'s stored palette array to get an RGBA8888 int.
     *
     * @param brightness 0 for dark, 1 for dim, 2 for medium and 3 for bright. Negative numbers are expected to normally 
     *                   be interpreted as black and numbers higher than 3 as white.
     * @param voxel      The color index of a voxel
     * @return An rgba8888 color
     */
    public int dimmer(int brightness, byte voxel) {
        return reducer.paletteArray[colorize(voxel, brightness - 2) & 0xFF];
    }

    /**
     * Gets a PaletteReducer that contains the RGBA8888 int colors that the byte indices these deals with correspond to.
     * This PaletteReducer can be queried for random colors with {@link PaletteReducer#randomColor(IRNG)} (for an int
     * color) or {@link PaletteReducer#randomColorIndex(IRNG)} (for a byte this can use again).
     *
     * @return the PaletteReducer this uses to store the corresponding RGBA8888 colors for the palette
     */
    public PaletteReducer getReducer() {
        return reducer;
    }

    /**
     * Sets the PaletteReducer this uses.
     *
     * @param reducer a PaletteReducer that should not be null
     */
    protected void setReducer(PaletteReducer reducer) {
        this.reducer = reducer;
    }

    private static int luma(final int r, final int g, final int b) {
        return r * 0x9C + g * 0xF6 + b * 0x65 + 0x18 - (Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b))) * 0x19;
//        return color.r * 0x8.Ap-5f + color.g * 0xF.Fp-5f + color.b * 0x6.1p-5f
//                + 0x1.6p-5f - (Math.max(color.r, Math.max(color.g, color.b))
//                - Math.min(color.r, Math.min(color.g, color.b))) * 0x1.6p-5f;
        // r * 0x8A + g * 0xFF + b * 0x61 + 0x15 - (Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b))) * 0x16;
        // 0x8A + 0xFF + 0x61 + 0x16 - 0x16
    }

    /**
     * Approximates the "color distance" between two colors defined by their YCoCg values. The luma in parameters y1 and
     * y2 should be calculated with {@link #luma(int, int, int)}, which is not the standard luminance value for YCoCg;
     * it will range from 0 to 63 typically. The chrominance orange values co1 and co2, and the chrominance green values
     * cg1 and cg2, should range from 0 to 31 typically by taking the standard range of -0.5 to 0.5, adding 0.5,
     * multiplying by 31 and rounding to an int.
     *
     * @param y1  luma for color 1; from 0 to 63, calculated by {@link #luma(int, int, int)}
     * @param co1 chrominance orange for color 1; from 0 to 31, usually related to {@code red - blue}
     * @param cg1 chrominance green for color 1; from 0 to 31, usually related to {@code green - (red + blue) * 0.5}
     * @param y2  luma for color 2; from 0 to 63, calculated by {@link #luma(int, int, int)}
     * @param co2 chrominance orange for color 2; from 0 to 31, usually related to {@code red - blue}
     * @param cg2 chrominance green for color 2; from 0 to 31, usually related to {@code green - (red + blue) * 0.5}
     * @return a non-negative int that is larger for more-different colors; typically somewhat large
     */
    private static int difference(int y1, int co1, int cg1, int y2, int co2, int cg2) {
        return ((y1 - y2) * (y1 - y2) << 2) + (((co1 - co2) * (co1 - co2) + (cg1 - cg2) * (cg1 - cg2)) * 3);
    }


    public static final Colorizer AuroraColorizer = new Colorizer(new PaletteReducer(Coloring.AURORA)) {
        private final byte[] primary = {
                -104,
                62,
                -98,
                40,
                -73,
                17,
                -52,
                -127
        }, grays = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return AURORA_RAMPS[voxel & 0xFF][0];
        }

        @Override
        public byte darken(byte voxel) {
            return AURORA_RAMPS[voxel & 0xFF][2];
        }
    };
    public static final Colorizer AuroraBonusColorizer = new Colorizer(new PaletteReducer()) {
        private final byte[] primary = {
                -104,
                62,
                -98,
                40,
                -73,
                17,
                -52,
                -127
        }, grays = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return AURORA_RAMPS[voxel & 0xFF][0];
        }

        @Override
        public byte darken(byte voxel) {
            return AURORA_RAMPS[voxel & 0xFF][2];
        }

        private final int[][] RAMP_VALUES = new int[][]{
                {0x00000000, 0x00000000, 0x00000000, 0x00000000},
                {0x000000FF, 0x000000FF, 0x010101FF, 0x010101FF},
                {0x070707FF, 0x0D0D0DFF, 0x131313FF, 0x1B1B1BFF},
                {0x0D0D0DFF, 0x191919FF, 0x252525FF, 0x353535FF},
                {0x141414FF, 0x252525FF, 0x373737FF, 0x4F4F4FFF},
                {0x1B1B1BFF, 0x323232FF, 0x494949FF, 0x686868FF},
                {0x222222FF, 0x3E3E3EFF, 0x5B5B5BFF, 0x828282FF},
                {0x292929FF, 0x4B4B4BFF, 0x6E6E6EFF, 0x9E9E9EFF},
                {0x303030FF, 0x585858FF, 0x808080FF, 0xB8B8B8FF},
                {0x363636FF, 0x646464FF, 0x929292FF, 0xD1D1D1FF},
                {0x3D3D3DFF, 0x707070FF, 0xA4A4A4FF, 0xEBEBEBFF},
                {0x444444FF, 0x7D7D7DFF, 0xB6B6B6FF, 0xFFFFFFFF},
                {0x4B4B4BFF, 0x8A8A8AFF, 0xC9C9C9FF, 0xFFFFFFFF},
                {0x525252FF, 0x969696FF, 0xDBDBDBFF, 0xFFFFFFFF},
                {0x585858FF, 0xA2A2A2FF, 0xEDEDEDFF, 0xFFFFFFFF},
                {0x5F5F5FFF, 0xAFAFAFFF, 0xFFFFFFFF, 0xFFFFFFFF},
                {0x005728FF, 0x005661FF, 0x007F7FFF, 0x3EA29EFF},
                {0x006945FF, 0x297D91FF, 0x3FBFBFFF, 0x9AFEFAFF},
                {0x009E61FF, 0x009ED0FF, 0x00FFFFFF, 0x7EFFFFFF},
                {0x2A6D62FF, 0x85A9B9FF, 0xBFFFFFFF, 0xFFFFFFFF},
                {0x330FA2FF, 0x4B5EB2FF, 0x8181FFFF, 0xD1CCFFFF},
                {0x1400F4FF, 0x0000C2FF, 0x0000FFFF, 0x2E26EEFF},
                {0x1F008FFF, 0x222C8AFF, 0x3F3FBFFF, 0x726ED2FF},
                {0x0C007CFF, 0x000063FF, 0x00007FFF, 0x161276FF},
                {0x0C0045FF, 0x08083DFF, 0x0F0F50FF, 0x211F52FF},
                {0x840084FF, 0x5A005AFF, 0x7F007FFF, 0x8E268EFF},
                {0x910091FF, 0x7C327CFF, 0xBF3FBFFF, 0xEA82EAFF},
                {0xEB00EBFF, 0x9C0B9CFF, 0xF500F5FF, 0xFF4BFFFF},
                {0x9B009DFF, 0x9D689FFF, 0xFD81FFFF, 0xFFDFFFFF},
                {0x7E3651FF, 0xAD8983FF, 0xFFC0CBFF, 0xFFFFFFFF},
                {0xA10F33FF, 0xB15E4BFF, 0xFF8181FF, 0xFFCCD1FF},
                {0xF40015FF, 0xC20000FF, 0xFF0000FF, 0xEE262FFF},
                {0x8F001FFF, 0x8A2C22FF, 0xBF3F3FFF, 0xD26E72FF},
                {0x7C000DFF, 0x630000FF, 0x7F0000FF, 0x761217FF},
                {0x46000EFF, 0x3F0B0BFF, 0x551414FF, 0x582628FF},
                {0x4F1700FF, 0x5F2B00FF, 0x7F3F00FF, 0x8A5A2BFF},
                {0x672F00FF, 0x8B5723FF, 0xBF7F3FFF, 0xE6B686FF},
                {0x9F2F00FF, 0xBF5700FF, 0xFF7F00FF, 0xFFB657FF},
                {0x7F4611FF, 0xB78351FF, 0xFFBF81FF, 0xFFFFE4FF},
                {0x626D2AFF, 0xB9A985FF, 0xFFFFBFFF, 0xFFFFFFFF},
                {0x619E00FF, 0xD09E01FF, 0xFFFF00FF, 0xFFFF7FFF},
                {0x456900FF, 0x917D29FF, 0xBFBF3FFF, 0xFAFE9AFF},
                {0x285700FF, 0x615600FF, 0x7F7F00FF, 0x9EA23FFF},
                {0x008300FF, 0x005900FF, 0x007F00FF, 0x278E27FF},
                {0x009100FF, 0x337C33FF, 0x3FBF3FFF, 0x82EA82FF},
                {0x00F200FF, 0x0EA10EFF, 0x00FF00FF, 0x4FFF4FFF},
                {0x1D841DFF, 0x85A285FF, 0xAFFFAFFF, 0xFFFFFFFF},
                {0x4D3A51FF, 0x7F7A83FF, 0xBCAFC0FF, 0xFFFFFFFF},
                {0x5C3F23FF, 0x8F745AFF, 0xCBAA89FF, 0xFFF4DCFF},
                {0x413E2EFF, 0x736C62FF, 0xA6A090FF, 0xE7E3D7FF},
                {0x233D37FF, 0x566468FF, 0x7E9494FF, 0xC0D0D1FF},
                {0x1E3534FF, 0x4A585FFF, 0x6E8287FF, 0xA9B8BCFF},
                {0x37281DFF, 0x584B40FF, 0x7E6E60FF, 0xAA9E94FF},
                {0x591A21FF, 0x70483CFF, 0xA0695FFF, 0xC79D97FF},
                {0x6E1A2AFF, 0x875448FF, 0xC07872FF, 0xEEB6B4FF},
                {0x722622FF, 0x936149FF, 0xD08A74FF, 0xFFCDBFFF},
                {0x782F21FF, 0x9F6C4EFF, 0xE19B7DFF, 0xFFE5D1FF},
                {0x783625FF, 0xA57658FF, 0xEBAA8CFF, 0xFFF8E3FF},
                {0x7A3E2CFF, 0xAD8164FF, 0xF5B99BFF, 0xFFFFFAFF},
                {0x724534FF, 0xAB8A72FF, 0xF6C8AFFF, 0xFFFFFFFF},
                {0x655347FF, 0xAA9B8EFF, 0xF5E1D2FF, 0xFFFFFFFF},
                {0x300C18FF, 0x3E2828FF, 0x573B3BFF, 0x6F585AFF},
                {0x470A17FF, 0x522C26FF, 0x73413CFF, 0x8B6462FF},
                {0x550F24FF, 0x643B36FF, 0x8E5555FF, 0xAF8385FF},
                {0x5F1B2EFF, 0x78504BFF, 0xAB7373FF, 0xDAAEB0FF},
                {0x682637FF, 0x8A645DFF, 0xC78F8FFF, 0xFFD6D8FF},
                {0x723241FF, 0x9D7970FF, 0xE3ABABFF, 0xFFFEFFFF},
                {0x6E4354FF, 0xA89390FF, 0xF8D2DAFF, 0xFFFFFFFF},
                {0x634A32FF, 0x9F8872FF, 0xE3C7ABFF, 0xFFFFFFFF},
                {0x5B3C15FF, 0x8C6C4BFF, 0xC49E73FF, 0xFDE1C1FF},
                {0x442B13FF, 0x664F39FF, 0x8F7357FF, 0xBAA590FF},
                {0x392008FF, 0x523B25FF, 0x73573BFF, 0x927D68FF},
                {0x1C1004FF, 0x291E13FF, 0x3B2D1FFF, 0x4B4036FF},
                {0x162200FF, 0x2E2D16FF, 0x414123FF, 0x565740FF},
                {0x283B00FF, 0x544E27FF, 0x73733BFF, 0x9B9C71FF},
                {0x344503FF, 0x67603AFF, 0x8F8F57FF, 0xC3C499FF},
                {0x3B5200FF, 0x766C38FF, 0xA2A255FF, 0xDADCA1FF},
                {0x425508FF, 0x83784DFF, 0xB5B572FF, 0xF7F9C5FF},
                {0x4B581AFF, 0x908563FF, 0xC7C78FFF, 0xFFFFE9FF},
                {0x535D2AFF, 0x9C9276FF, 0xDADAABFF, 0xFFFFFFFF},
                {0x5B613AFF, 0xA89F8AFF, 0xEDEDC7FF, 0xFFFFFFFF},
                {0x3E6926FF, 0x90957AFF, 0xC7E3ABFF, 0xFFFFFFFF},
                {0x315F19FF, 0x7C8466FF, 0xABC78FFF, 0xF9FFE4FF},
                {0x197000FF, 0x6A7B3CFF, 0x8EBE55FF, 0xCFF8A5FF},
                {0x194D01FF, 0x525F3CFF, 0x738F57FF, 0xA9C094FF},
                {0x084D00FF, 0x3F542AFF, 0x587D3EFF, 0x85A372FF},
                {0x132B02FF, 0x313721FF, 0x465032FF, 0x646C55FF},
                {0x041200FF, 0x111509FF, 0x191E0FFF, 0x23271CFF},
                {0x003503FF, 0x163727FF, 0x235037FF, 0x446753FF},
                {0x043204FF, 0x293C29FF, 0x3B573BFF, 0x5D735DFF},
                {0x113111FF, 0x374437FF, 0x506450FF, 0x798979FF},
                {0x004900FF, 0x274D33FF, 0x3B7349FF, 0x699574FF},
                {0x005700FF, 0x3F603FFF, 0x578F57FF, 0x8FBC8FFF},
                {0x0C5F0CFF, 0x537153FF, 0x73AB73FF, 0xB7E4B7FF},
                {0x00710FFF, 0x4A7D63FF, 0x64C082FF, 0xB1FAC8FF},
                {0x196719FF, 0x688268FF, 0x8FC78FFF, 0xDFFFDFFF},
                {0x226B22FF, 0x768D76FF, 0xA2D8A2FF, 0xFAFFFAFF},
                {0x49615FFF, 0x9BA8B0FF, 0xE1F8FAFF, 0xFFFFFFFF},
                {0x286E3CFF, 0x829C94FF, 0xB4EECAFF, 0xFFFFFFFF},
                {0x25683CFF, 0x79958FFF, 0xABE3C5FF, 0xFFFFFFFF},
                {0x1A5921FF, 0x607866FF, 0x87B48EFF, 0xD0F5D6FF},
                {0x024510FF, 0x375444FF, 0x507D5FFF, 0x83A78FFF},
                {0x005000FF, 0x064833FF, 0x0F6946FF, 0x3A8264FF},
                {0x011A06FF, 0x131F18FF, 0x1E2D23FF, 0x2F3B33FF},
                {0x00201AFF, 0x152C32FF, 0x234146FF, 0x41585CFF},
                {0x003B28FF, 0x264E54FF, 0x3B7373FF, 0x719C9BFF},
                {0x00533FFF, 0x43727DFF, 0x64ABABFF, 0xB2EAE8FF},
                {0x1A584BFF, 0x628590FF, 0x8FC7C7FF, 0xE9FFFFFF},
                {0x256156FF, 0x7697A4FF, 0xABE3E3FF, 0xFFFFFFFF},
                {0x37635CFF, 0x8AA2ADFF, 0xC7F1F1FF, 0xFFFFFFFF},
                {0x3D4C69FF, 0x7E90A7FF, 0xBED2F0FF, 0xFFFFFFFF},
                {0x324A63FF, 0x71889FFF, 0xABC7E3FF, 0xFFFFFFFF},
                {0x364064FF, 0x6E7F99FF, 0xA8B9DCFF, 0xFFFFFFFF},
                {0x284059FF, 0x5E758CFF, 0x8FABC7FF, 0xE0F5FFFF},
                {0x043566FF, 0x356290FF, 0x578FC7FF, 0xA3CDF7FF},
                {0x132B44FF, 0x384F66FF, 0x57738FFF, 0x90A5BAFF},
                {0x082039FF, 0x243B52FF, 0x3B5773FF, 0x687D92FF},
                {0x02051DFF, 0x091022FF, 0x0F192DFF, 0x1E2435FF},
                {0x0F0228FF, 0x15142CFF, 0x1F1F3BFF, 0x323047FF},
                {0x180C31FF, 0x28283FFF, 0x3B3B57FF, 0x5A586FFF},
                {0x1F0E44FF, 0x303253FF, 0x494973FF, 0x706E90FF},
                {0x240F55FF, 0x373C65FF, 0x57578FFF, 0x8886B2FF},
                {0x301761FF, 0x4A4D77FF, 0x736EAAFF, 0xADA8D7FF},
                {0x2F1579FF, 0x49538EFF, 0x7676CAFF, 0xBAB6F9FF},
                {0x372668FF, 0x5C648AFF, 0x8F8FC7FF, 0xD8D6FFFF},
                {0x413272FF, 0x6F799DFF, 0xABABE3FF, 0xFFFEFFFF},
                {0x494D6CFF, 0x8B97ACFF, 0xD0DAF8FF, 0xFFFFFFFF},
                {0x544E6DFF, 0x989EAFFF, 0xE3E3FFFF, 0xFFFFFFFF},
                {0x4F1F68FF, 0x706687FF, 0xAB8FC7FF, 0xF3DAFFFF},
                {0x580089FF, 0x5A4088FF, 0x8F57C7FF, 0xC797F1FF},
                {0x3E0757FF, 0x4D3D64FF, 0x73578FFF, 0xA38AB8FF},
                {0x35004EFF, 0x3B2952FF, 0x573B73FF, 0x7B6290FF},
                {0x270027FF, 0x2A162AFF, 0x3C233CFF, 0x4E394EFF},
                {0x280528FF, 0x312231FF, 0x463246FF, 0x5F4E5FFF},
                {0x4A004AFF, 0x4E2C4EFF, 0x724072FF, 0x946B94FF},
                {0x570057FF, 0x603E60FF, 0x8F578FFF, 0xBC8EBCFF},
                {0x700070FF, 0x714071FF, 0xAB57ABFF, 0xDC97DCFF},
                {0x5F0B5FFF, 0x725372FF, 0xAB73ABFF, 0xE4B6E4FF},
                {0x76256EFF, 0x9A7E92FF, 0xEBACE1FF, 0xFFFFFFFF},
                {0x6F4667FF, 0xAA9CA2FF, 0xFFDCF5FF, 0xFFFFFFFF},
                {0x623D62FF, 0x988D98FF, 0xE3C7E3FF, 0xFFFFFFFF},
                {0x68365BFF, 0x97848BFF, 0xE1B9D2FF, 0xFFFFFFFF},
                {0x6C2657FF, 0x90737CFF, 0xD7A0BEFF, 0xFFF3FFFF},
                {0x671B5BFF, 0x846679FF, 0xC78FB9FF, 0xFFDCFFFF},
                {0x741151FF, 0x875A67FF, 0xC87DA0FF, 0xFFC4E2FF},
                {0x830058FF, 0x84425CFF, 0xC35A91FF, 0xEE9AC9FF},
                {0x320021FF, 0x351A25FF, 0x4B2837FF, 0x5D404EFF},
                {0x250018FF, 0x240E18FF, 0x321623FF, 0x3D2632FF},
                {0x23001BFF, 0x1E0516FF, 0x280A1EFF, 0x2E1527FF},
                {0x300007FF, 0x30100AFF, 0x401811FF, 0x472824FF},
                {0x4F0000FF, 0x4A0E00FF, 0x621800FF, 0x63291AFF},
                {0x92000BFF, 0x7D0B00FF, 0xA5140AFF, 0xA2312EFF},
                {0xBB000BFF, 0xA21400FF, 0xDA2010FF, 0xD94742FF},
                {0x97001EFF, 0x983A28FF, 0xD5524AFF, 0xEE8886FF},
                {0xCA0000FF, 0xBE2B00FF, 0xFF3C0AFF, 0xFF6D4DFF},
                {0xAE0004FF, 0xB24014FF, 0xF55A32FF, 0xFF9278FF},
                {0xB3002AFF, 0xB44835FF, 0xFF6262FF, 0xFFA4AAFF},
                {0x785D00FF, 0xB97D19FF, 0xF6BD31FF, 0xFFFF97FF},
                {0x8C4200FF, 0xBC701EFF, 0xFFA53CFF, 0xFFEA9BFF},
                {0x6C5000FF, 0xA26700FF, 0xD79B0FFF, 0xFDD267FF},
                {0x892700FF, 0xA44C00FF, 0xDA6E0AFF, 0xF19F55FF},
                {0x702100FF, 0x863D00FF, 0xB45A00FF, 0xC5813EFF},
                {0x671700FF, 0x773300FF, 0xA04B05FF, 0xAD6D39FF},
                {0x3B0D00FF, 0x452109FF, 0x5F3214FF, 0x6B4933FF},
                {0x1A3400FF, 0x3D3702FF, 0x53500AFF, 0x686832FF},
                {0x1D4400FF, 0x494300FF, 0x626200FF, 0x7A7C31FF},
                {0x39370EFF, 0x64573CFF, 0x8C805AFF, 0xBBB296FF},
                {0x465B00FF, 0x846300FF, 0xAC9400FF, 0xD0C14FFF},
                {0x3C7100FF, 0x897402FF, 0xB1B10AFF, 0xDFE462FF},
                {0x5E6900FF, 0xAD8B3CFF, 0xE6D55AFF, 0xFFFFC2FF},
                {0x747800FF, 0xC68904FF, 0xFFD510FF, 0xFFFF82FF},
                {0x6B7900FF, 0xC49631FF, 0xFFEA4AFF, 0xFFFFBFFF},
                {0x319B00FF, 0xA19E34FF, 0xC8FF41FF, 0xFFFFB2FF},
                {0x0E9B00FF, 0x7F973AFF, 0x9BF046FF, 0xE9FFAAFF},
                {0x109A00FF, 0x7A8B15FF, 0x96DC19FF, 0xD4FF77FF},
                {0x009B00FF, 0x5F8106FF, 0x73C805FF, 0xABF359FF},
                {0x008200FF, 0x536F01FF, 0x6AA805FF, 0x98CD4DFF},
                {0x005600FF, 0x2B4C0BFF, 0x3C6E14FF, 0x5E8740FF},
                {0x022800FF, 0x1C2500FF, 0x283405FF, 0x36411CFF},
                {0x003C00FF, 0x153102FF, 0x204608FF, 0x355423FF},
                {0x005800FF, 0x054105FF, 0x0C5C0CFF, 0x2A6B2AFF},
                {0x009000FF, 0x0E6602FF, 0x149605FF, 0x42AC37FF},
                {0x00CB00FF, 0x0F8C0FFF, 0x0AD70AFF, 0x4EF44EFF},
                {0x00D300FF, 0x189310FF, 0x14E60AFF, 0x5CFF55FF},
                {0x00A200FF, 0x679E5FFF, 0x7DFF73FF, 0xDAFFD3FF},
                {0x00AD00FF, 0x41954EFF, 0x4BF05AFF, 0xA1FFADFF},
                {0x00BB00FF, 0x038114FF, 0x00C514FF, 0x42E051FF},
                {0x009600FF, 0x047641FF, 0x05B450FF, 0x4BD784FF},
                {0x006A00FF, 0x125E3BFF, 0x1C8C4EFF, 0x54AD7AFF},
                {0x00230BFF, 0x092623FF, 0x123832FF, 0x2C4944FF},
                {0x00681EFF, 0x086562FF, 0x129880FF, 0x58C1ABFF},
                {0x008D14FF, 0x037E74FF, 0x06C491FF, 0x5DF3C6FF},
                {0x00B200FF, 0x058D5CFF, 0x00DE6AFF, 0x58FFA8FF},
                {0x009A1AFF, 0x249388FF, 0x2DEBA8FF, 0x92FFEFFF},
                {0x00A611FF, 0x339D89FF, 0x3CFEA5FF, 0xA7FFF6FF},
                {0x008E37FF, 0x51A0A2FF, 0x6AFFCDFF, 0xD9FFFFFF},
                {0x0B676CFF, 0x629BBCFF, 0x91EBFFFF, 0xFFFFFFFF},
                {0x00736FFF, 0x3895C3FF, 0x55E6FFFF, 0xC7FFFFFF},
                {0x036068FF, 0x538FB1FF, 0x7DD7F0FF, 0xE4FFFFFF},
                {0x008E49FF, 0x048DABFF, 0x08DED5FF, 0x74FFFFFF},
                {0x004F73FF, 0x0068A8FF, 0x109CDEFF, 0x68D3FFFF},
                {0x003D1DFF, 0x003E44FF, 0x055A5CFF, 0x317473FF},
                {0x000B34FF, 0x0D1E3EFF, 0x162C52FF, 0x334260FF},
                {0x000A55FF, 0x03255DFF, 0x0F377DFF, 0x37538AFF},
                {0x001965FF, 0x003376FF, 0x004A9CFF, 0x356BAAFF},
                {0x002551FF, 0x1B446DFF, 0x326496FF, 0x6A8FB5FF},
                {0x0005B3FF, 0x0039B8FF, 0x0052F6FF, 0x4781FFFF},
                {0x002770FF, 0x05488CFF, 0x186ABDFF, 0x5A98D6FF},
                {0x002985FF, 0x0C52A3FF, 0x2378DCFF, 0x6EADF9FF},
                {0x0D3E5CFF, 0x436B8DFF, 0x699DC3FF, 0xB7DEFBFF},
                {0x003D8DFF, 0x2670BAFF, 0x4AA4FFFF, 0xA7EBFFFF},
                {0x253687FF, 0x597BB4FF, 0x90B0FFFF, 0xECFFFFFF},
                {0x00567DFF, 0x3684BDFF, 0x5AC5FFFF, 0xC0FFFFFF},
                {0x49347EFF, 0x7A83ABFF, 0xBEB9FAFF, 0xFFFFFFFF},
                {0x00697DFF, 0x007DC5FF, 0x00BFFFFF, 0x6AFEFFFF},
                {0x002F9FFF, 0x0057BFFF, 0x007FFFFF, 0x56B6FFFF},
                {0x042872FF, 0x2B5791FF, 0x4B7DC8FF, 0x92B7F0FF},
                {0x36029FFF, 0x4550A7FF, 0x786EF0FF, 0xC0B4FFFF},
                {0x1900B8FF, 0x2342B7FF, 0x4A5AFFFF, 0x9198FFFF},
                {0x3F00C1FF, 0x3631AFFF, 0x6241F6FF, 0x9E7EFFFF},
                {0x2000C2FF, 0x1A2BB1FF, 0x3C3CF5FF, 0x7973FFFF},
                {0x0E00BFFF, 0x0012A4FF, 0x101CDAFF, 0x4043D8FF},
                {0x0500ABFF, 0x000890FF, 0x0010BDFF, 0x282FB6FF},
                {0x240087FF, 0x13086FFF, 0x231094FF, 0x412E96FF},
                {0x000531FF, 0x051536FF, 0x0C2148FF, 0x233150FF},
                {0x4F00A3FF, 0x320980FF, 0x5010B0FF, 0x713AB9FF},
                {0x5F00C1FF, 0x3B0B96FF, 0x6010D0FF, 0x8641DAFF},
                {0x6900ABFF, 0x542691FF, 0x8732D2FF, 0xB56DEEFF},
                {0x7100C8FF, 0x5C35ADFF, 0x9C41FFFF, 0xD689FFFF},
                {0x8600F6FF, 0x4C03B4FF, 0x7F00FFFF, 0xA63AFFFF},
                {0x7900B3FF, 0x724FA8FF, 0xBD62FFFF, 0xFFB4FFFF},
                {0x591097FF, 0x726CABFF, 0xB991FFFF, 0xFFE7FFFF},
                {0x691D8CFF, 0x887BA9FF, 0xD7A5FFFF, 0xFFFFFFFF},
                {0x583777FF, 0x8B8BA8FF, 0xD7C3FAFF, 0xFFFFFFFF},
                {0x733377FF, 0xA291A6FF, 0xF8C6FCFF, 0xFFFFFFFF},
                {0x9000A6FF, 0x8E5DA3FF, 0xE673FFFF, 0xFFCBFFFF},
                {0xBA00BAFF, 0x9D499DFF, 0xFF52FFFF, 0xFFABFFFF},
                {0xBD00C3FF, 0x8B1F90FF, 0xDA20E0FF, 0xFF68FFFF},
                {0x9E00D8FF, 0x7327A9FF, 0xBD29FFFF, 0xED72FFFF},
                {0xB100B8FF, 0x7C0F83FF, 0xBD10C5FF, 0xDA4DE0FF},
                {0x8200AEFF, 0x5B0F84FF, 0x8C14BEFF, 0xAC49D2FF},
                {0x50006DFF, 0x3D0E58FF, 0x5A187BFF, 0x733C8CFF},
                {0x5B005BFF, 0x470C47FF, 0x641464FF, 0x773677FF},
                {0x490066FF, 0x2E0049FF, 0x410062FF, 0x4E1867FF},
                {0x310043FF, 0x240435FF, 0x320A46FF, 0x401E4FFF},
                {0x46002CFF, 0x3E0F26FF, 0x551937FF, 0x61314BFF},
                {0x8F0075FF, 0x6F1157FF, 0xA01982FF, 0xB649A0FF},
                {0xC60080FF, 0x8F004EFF, 0xC80078FF, 0xD23296FF},
                {0xBD0085FF, 0xA74273FF, 0xFF50BFFF, 0xFF9FFCFF},
                {0xAD007BFF, 0xA85579FF, 0xFF6AC5FF, 0xFFBDFFFF},
                {0x891F51FF, 0xA87574FF, 0xFAA0B9FF, 0xFFF7FFFF},
                {0xCA0068FF, 0xAD2F52FF, 0xFC3A8CFF, 0xFF7EC5FF},
                {0xCA006AFF, 0xA21849FF, 0xE61E78FF, 0xF657A4FF},
                {0xAE003BFF, 0x8C0921FF, 0xBD1039FF, 0xC1385EFF},
                {0x740033FF, 0x6D2431FF, 0x98344DFF, 0xAD5E75FF},
                {0x820034FF, 0x6C0C23FF, 0x911437FF, 0x993656FF},
        };
        
        @Override
        public int dimmer(int brightness, byte voxel) {
            return RAMP_VALUES[voxel & 255][
                    brightness <= 0
                            ? 0
                            : brightness >= 3
                            ? 3
                            : brightness
                    ];
        }

    };
    /**
     * Color values as RGBA8888 ints to use when shading an Aurora-palette model.
     * The color in index 1 of each 4-element sub-array is the "dimmer" color and the one that will match the voxel
     * color used in a model. The color at index 0 is "bright", index 2 is "dim", and index 3 is "dark".
     * To visualize this, <a href="https://i.imgur.com/rQDrPE7.png">use this image</a>, with the first 32 items in
     * AURORA_RAMPS corresponding to the first column from top to bottom, the next 32 items in the second column, etc.
     */
    public static final int[][] AURORA_RAMP_VALUES = new int[][]{
            { 0x00000000, 0x00000000, 0x00000000, 0x00000000 },
            { 0x252525FF, 0x010101FF, 0x010101FF, 0x010101FF },
            { 0x3C233CFF, 0x131313FF, 0x010101FF, 0x010101FF },
            { 0x3B3B57FF, 0x252525FF, 0x010101FF, 0x010101FF },
            { 0x5B5B5BFF, 0x373737FF, 0x0F192DFF, 0x131313FF },
            { 0x6E6E6EFF, 0x494949FF, 0x3C233CFF, 0x252525FF },
            { 0x6E8287FF, 0x5B5B5BFF, 0x3B3B57FF, 0x373737FF },
            { 0x7E9494FF, 0x6E6E6EFF, 0x494949FF, 0x3B3B57FF },
            { 0xA4A4A4FF, 0x808080FF, 0x5B5B5BFF, 0x494949FF },
            { 0xBCAFC0FF, 0x929292FF, 0x6E6E6EFF, 0x5B5B5BFF },
            { 0xC9C9C9FF, 0xA4A4A4FF, 0x808080FF, 0x6E8287FF },
            { 0xE3C7E3FF, 0xB6B6B6FF, 0x929292FF, 0x7E9494FF },
            { 0xE3E3FFFF, 0xC9C9C9FF, 0xA4A4A4FF, 0x929292FF },
            { 0xFFFFFFFF, 0xDBDBDBFF, 0xB6B6B6FF, 0xBCAFC0FF },
            { 0xFFFFFFFF, 0xEDEDEDFF, 0xC9C9C9FF, 0xB6B6B6FF },
            { 0xFFFFFFFF, 0xFFFFFFFF, 0xDBDBDBFF, 0xE3C7E3FF },
            { 0x06C491FF, 0x007F7FFF, 0x0F377DFF, 0x0C2148FF },
            { 0x5AC5FFFF, 0x3FBFBFFF, 0x06C491FF, 0x129880FF },
            { 0x55E6FFFF, 0x00FFFFFF, 0x00BFFFFF, 0x109CDEFF },
            { 0xFFFFFFFF, 0xBFFFFFFF, 0xABC7E3FF, 0x8FC7C7FF },
            { 0xB991FFFF, 0x8181FFFF, 0x4A5AFFFF, 0x6241F6FF },
            { 0x3C3CF5FF, 0x0000FFFF, 0x00007FFF, 0x010101FF },
            { 0x4A5AFFFF, 0x3F3FBFFF, 0x5010B0FF, 0x231094FF },
            { 0x101CDAFF, 0x00007FFF, 0x010101FF, 0x010101FF },
            { 0x0F377DFF, 0x0F0F50FF, 0x010101FF, 0x010101FF },
            { 0x8C14BEFF, 0x7F007FFF, 0x320A46FF, 0x010101FF },
            { 0xFF52FFFF, 0xBF3FBFFF, 0xBD10C5FF, 0x8C14BEFF },
            { 0xFF52FFFF, 0xF500F5FF, 0x8C14BEFF, 0x7F007FFF },
            { 0xF8C6FCFF, 0xFD81FFFF, 0xFF52FFFF, 0xBF3FBFFF },
            { 0xFFFFFFFF, 0xFFC0CBFF, 0xD7A0BEFF, 0xC78FB9FF },
            { 0xFAA0B9FF, 0xFF8181FF, 0xD5524AFF, 0xBF3F3FFF },
            { 0xFF3C0AFF, 0xFF0000FF, 0xA5140AFF, 0x7F0000FF },
            { 0xFF6262FF, 0xBF3F3FFF, 0xBD1039FF, 0x911437FF },
            { 0x911437FF, 0x7F0000FF, 0x280A1EFF, 0x010101FF },
            { 0x573B3BFF, 0x551414FF, 0x010101FF, 0x010101FF },
            { 0xBF3F3FFF, 0x7F3F00FF, 0x621800FF, 0x401811FF },
            { 0xE19B7DFF, 0xBF7F3FFF, 0xBF3F3FFF, 0xA04B05FF },
            { 0xFFA53CFF, 0xFF7F00FF, 0xB45A00FF, 0xDA2010FF },
            { 0xFFFFBFFF, 0xFFBF81FF, 0xE19B7DFF, 0xD08A74FF },
            { 0xFFFFFFFF, 0xFFFFBFFF, 0xDADAABFF, 0xE3C7ABFF },
            { 0xFFFFBFFF, 0xFFFF00FF, 0xB1B10AFF, 0xAC9400FF },
            { 0xE6D55AFF, 0xBFBF3FFF, 0xAC9400FF, 0x7F7F00FF },
            { 0xB1B10AFF, 0x7F7F00FF, 0x53500AFF, 0x5F3214FF },
            { 0x00C514FF, 0x007F00FF, 0x191E0FFF, 0x010101FF },
            { 0x4BF05AFF, 0x3FBF3FFF, 0x1C8C4EFF, 0x149605FF },
            { 0x4BF05AFF, 0x00FF00FF, 0x00C514FF, 0x149605FF },
            { 0xE1F8FAFF, 0xAFFFAFFF, 0x8FC78FFF, 0x87B48EFF },
            { 0xE3C7E3FF, 0xBCAFC0FF, 0x929292FF, 0xAB73ABFF },
            { 0xE3C7ABFF, 0xCBAA89FF, 0xC07872FF, 0xAB7373FF },
            { 0xC9C9C9FF, 0xA6A090FF, 0x808080FF, 0x6E6E6EFF },
            { 0xB6B6B6FF, 0x7E9494FF, 0x6E6E6EFF, 0x73578FFF },
            { 0xA4A4A4FF, 0x6E8287FF, 0x5B5B5BFF, 0x494973FF },
            { 0x929292FF, 0x7E6E60FF, 0x494949FF, 0x573B3BFF },
            { 0xC78F8FFF, 0xA0695FFF, 0x98344DFF, 0x73413CFF },
            { 0xE19B7DFF, 0xC07872FF, 0x8E5555FF, 0x98344DFF },
            { 0xEBAA8CFF, 0xD08A74FF, 0xA0695FFF, 0x8E5555FF },
            { 0xF6C8AFFF, 0xE19B7DFF, 0xC07872FF, 0xA0695FFF },
            { 0xFFC0CBFF, 0xEBAA8CFF, 0xD08A74FF, 0xC07872FF },
            { 0xF8D2DAFF, 0xF5B99BFF, 0xD08A74FF, 0xC07872FF },
            { 0xFFFFBFFF, 0xF6C8AFFF, 0xCBAA89FF, 0xC78F8FFF },
            { 0xFFFFFFFF, 0xF5E1D2FF, 0xC9C9C9FF, 0xE1B9D2FF },
            { 0x8E5555FF, 0x573B3BFF, 0x321623FF, 0x280A1EFF },
            { 0x7E6E60FF, 0x73413CFF, 0x551937FF, 0x401811FF },
            { 0xAB7373FF, 0x8E5555FF, 0x573B3BFF, 0x551937FF },
            { 0xC78F8FFF, 0xAB7373FF, 0x8E5555FF, 0x73413CFF },
            { 0xE3ABABFF, 0xC78F8FFF, 0xAB7373FF, 0xA0695FFF },
            { 0xF8D2DAFF, 0xE3ABABFF, 0xC78F8FFF, 0xC87DA0FF },
            { 0xFFFFFFFF, 0xF8D2DAFF, 0xD7A0BEFF, 0xC78FB9FF },
            { 0xEDEDEDFF, 0xE3C7ABFF, 0xCBAA89FF, 0xC78F8FFF },
            { 0xE3ABABFF, 0xC49E73FF, 0xAB7373FF, 0xA0695FFF },
            { 0x929292FF, 0x8F7357FF, 0x73573BFF, 0x73413CFF },
            { 0x8F7357FF, 0x73573BFF, 0x414123FF, 0x3B2D1FFF },
            { 0x494949FF, 0x3B2D1FFF, 0x010101FF, 0x010101FF },
            { 0x73573BFF, 0x414123FF, 0x191E0FFF, 0x131313FF },
            { 0x8F8F57FF, 0x73733BFF, 0x465032FF, 0x414123FF },
            { 0xA6A090FF, 0x8F8F57FF, 0x73733BFF, 0x73573BFF },
            { 0xCBAA89FF, 0xA2A255FF, 0x8F7357FF, 0x73733BFF },
            { 0xE3C7ABFF, 0xB5B572FF, 0x8F8F57FF, 0x8C805AFF },
            { 0xEDEDC7FF, 0xC7C78FFF, 0xC49E73FF, 0xA2A255FF },
            { 0xEDEDEDFF, 0xDADAABFF, 0xCBAA89FF, 0xA6A090FF },
            { 0xFFFFFFFF, 0xEDEDC7FF, 0xC9C9C9FF, 0xB6B6B6FF },
            { 0xEDEDEDFF, 0xC7E3ABFF, 0xABC78FFF, 0x8FC78FFF },
            { 0xC7E3ABFF, 0xABC78FFF, 0x929292FF, 0x8F8F57FF },
            { 0xABC78FFF, 0x8EBE55FF, 0x738F57FF, 0x587D3EFF },
            { 0x87B48EFF, 0x738F57FF, 0x506450FF, 0x465032FF },
            { 0x73AB73FF, 0x587D3EFF, 0x3B573BFF, 0x465032FF },
            { 0x507D5FFF, 0x465032FF, 0x1E2D23FF, 0x191E0FFF },
            { 0x373737FF, 0x191E0FFF, 0x010101FF, 0x010101FF },
            { 0x3B7349FF, 0x235037FF, 0x0F192DFF, 0x131313FF },
            { 0x507D5FFF, 0x3B573BFF, 0x1E2D23FF, 0x252525FF },
            { 0x6E8287FF, 0x506450FF, 0x373737FF, 0x1E2D23FF },
            { 0x6E8287FF, 0x3B7349FF, 0x235037FF, 0x123832FF },
            { 0x73AB73FF, 0x578F57FF, 0x3B573BFF, 0x235037FF },
            { 0x8FC78FFF, 0x73AB73FF, 0x578F57FF, 0x507D5FFF },
            { 0x8FC7C7FF, 0x64C082FF, 0x578F57FF, 0x507D5FFF },
            { 0xABE3C5FF, 0x8FC78FFF, 0x73AB73FF, 0x6E8287FF },
            { 0xC7F1F1FF, 0xA2D8A2FF, 0x87B48EFF, 0x73AB73FF },
            { 0xFFFFFFFF, 0xE1F8FAFF, 0xBED2F0FF, 0xABC7E3FF },
            { 0xE1F8FAFF, 0xB4EECAFF, 0x8FC7C7FF, 0x8FC78FFF },
            { 0xE1F8FAFF, 0xABE3C5FF, 0x87B48EFF, 0x64ABABFF },
            { 0xA2D8A2FF, 0x87B48EFF, 0x808080FF, 0x6E8287FF },
            { 0x7E9494FF, 0x507D5FFF, 0x3B573BFF, 0x235037FF },
            { 0x3B7373FF, 0x0F6946FF, 0x123832FF, 0x0C2148FF },
            { 0x494949FF, 0x1E2D23FF, 0x010101FF, 0x010101FF },
            { 0x3B7373FF, 0x234146FF, 0x0F192DFF, 0x010101FF },
            { 0x6E8287FF, 0x3B7373FF, 0x3B3B57FF, 0x234146FF },
            { 0x8FC7C7FF, 0x64ABABFF, 0x57738FFF, 0x3B7373FF },
            { 0xABE3E3FF, 0x8FC7C7FF, 0x64ABABFF, 0x578FC7FF },
            { 0xE1F8FAFF, 0xABE3E3FF, 0x8FC7C7FF, 0x8FABC7FF },
            { 0xFFFFFFFF, 0xC7F1F1FF, 0xABC7E3FF, 0xA8B9DCFF },
            { 0xE1F8FAFF, 0xBED2F0FF, 0xABABE3FF, 0x8FABC7FF },
            { 0xE3E3FFFF, 0xABC7E3FF, 0x8FABC7FF, 0x8F8FC7FF },
            { 0xD0DAF8FF, 0xA8B9DCFF, 0x8F8FC7FF, 0x7676CAFF },
            { 0xABC7E3FF, 0x8FABC7FF, 0x7E9494FF, 0x736EAAFF },
            { 0x90B0FFFF, 0x578FC7FF, 0x326496FF, 0x0F377DFF },
            { 0x7E9494FF, 0x57738FFF, 0x3B5773FF, 0x494973FF },
            { 0x57738FFF, 0x3B5773FF, 0x162C52FF, 0x0C2148FF },
            { 0x234146FF, 0x0F192DFF, 0x010101FF, 0x010101FF },
            { 0x3B3B57FF, 0x1F1F3BFF, 0x010101FF, 0x010101FF },
            { 0x57578FFF, 0x3B3B57FF, 0x1F1F3BFF, 0x0F192DFF },
            { 0x57738FFF, 0x494973FF, 0x162C52FF, 0x1F1F3BFF },
            { 0x7676CAFF, 0x57578FFF, 0x3B3B57FF, 0x162C52FF },
            { 0x8F8FC7FF, 0x736EAAFF, 0x494973FF, 0x573B73FF },
            { 0x90B0FFFF, 0x7676CAFF, 0x57578FFF, 0x3F3FBFFF },
            { 0xABABE3FF, 0x8F8FC7FF, 0x736EAAFF, 0x73578FFF },
            { 0xBED2F0FF, 0xABABE3FF, 0x8F8FC7FF, 0x7676CAFF },
            { 0xFFFFFFFF, 0xD0DAF8FF, 0xA8B9DCFF, 0xABABE3FF },
            { 0xFFFFFFFF, 0xE3E3FFFF, 0xBEB9FAFF, 0xA8B9DCFF },
            { 0xBEB9FAFF, 0xAB8FC7FF, 0x736EAAFF, 0x73578FFF },
            { 0xBD62FFFF, 0x8F57C7FF, 0x8C14BEFF, 0x5A187BFF },
            { 0xAB73ABFF, 0x73578FFF, 0x573B73FF, 0x5A187BFF },
            { 0x73578FFF, 0x573B73FF, 0x410062FF, 0x320A46FF },
            { 0x494949FF, 0x3C233CFF, 0x010101FF, 0x010101FF },
            { 0x724072FF, 0x463246FF, 0x280A1EFF, 0x010101FF },
            { 0xAB57ABFF, 0x724072FF, 0x641464FF, 0x3C233CFF },
            { 0xAB73ABFF, 0x8F578FFF, 0x573B73FF, 0x641464FF },
            { 0xAB8FC7FF, 0xAB57ABFF, 0x724072FF, 0xA01982FF },
            { 0xD7A0BEFF, 0xAB73ABFF, 0x8F578FFF, 0x73578FFF },
            { 0xFFDCF5FF, 0xEBACE1FF, 0xC78FB9FF, 0xC87DA0FF },
            { 0xFFFFFFFF, 0xFFDCF5FF, 0xE1B9D2FF, 0xEBACE1FF },
            { 0xFFFFFFFF, 0xE3C7E3FF, 0xBCAFC0FF, 0xC78FB9FF },
            { 0xFFDCF5FF, 0xE1B9D2FF, 0xC78FB9FF, 0xAB73ABFF },
            { 0xE3C7E3FF, 0xD7A0BEFF, 0xC87DA0FF, 0xAB73ABFF },
            { 0xEBACE1FF, 0xC78FB9FF, 0xAB57ABFF, 0x8F578FFF },
            { 0xD7A0BEFF, 0xC87DA0FF, 0xAB57ABFF, 0x8F578FFF },
            { 0xC78FB9FF, 0xC35A91FF, 0x8E5555FF, 0xA01982FF },
            { 0x724072FF, 0x4B2837FF, 0x280A1EFF, 0x010101FF },
            { 0x463246FF, 0x321623FF, 0x010101FF, 0x010101FF },
            { 0x4B2837FF, 0x280A1EFF, 0x010101FF, 0x010101FF },
            { 0x573B3BFF, 0x401811FF, 0x010101FF, 0x010101FF },
            { 0x7F3F00FF, 0x621800FF, 0x280A1EFF, 0x010101FF },
            { 0xBF3F3FFF, 0xA5140AFF, 0x280A1EFF, 0x010101FF },
            { 0xF55A32FF, 0xDA2010FF, 0x7F0000FF, 0x280A1EFF },
            { 0xFF8181FF, 0xD5524AFF, 0xBD1039FF, 0x911437FF },
            { 0xF55A32FF, 0xFF3C0AFF, 0xA5140AFF, 0x7F0000FF },
            { 0xFF8181FF, 0xF55A32FF, 0xFF3C0AFF, 0xDA2010FF },
            { 0xEBAA8CFF, 0xFF6262FF, 0xBF3F3FFF, 0xBD1039FF },
            { 0xFFEA4AFF, 0xF6BD31FF, 0xD79B0FFF, 0xAC9400FF },
            { 0xFFBF81FF, 0xFFA53CFF, 0xD79B0FFF, 0xDA6E0AFF },
            { 0xFFA53CFF, 0xD79B0FFF, 0xB45A00FF, 0xA04B05FF },
            { 0xFFA53CFF, 0xDA6E0AFF, 0xA04B05FF, 0xA5140AFF },
            { 0xBF7F3FFF, 0xB45A00FF, 0x7F3F00FF, 0xA5140AFF },
            { 0xDA6E0AFF, 0xA04B05FF, 0xA5140AFF, 0x621800FF },
            { 0x73573BFF, 0x5F3214FF, 0x280A1EFF, 0x010101FF },
            { 0x73733BFF, 0x53500AFF, 0x283405FF, 0x191E0FFF },
            { 0x73733BFF, 0x626200FF, 0x283405FF, 0x401811FF },
            { 0x929292FF, 0x8C805AFF, 0x73573BFF, 0x465032FF },
            { 0xBFBF3FFF, 0xAC9400FF, 0xA04B05FF, 0x7F3F00FF },
            { 0xE6D55AFF, 0xB1B10AFF, 0x7F7F00FF, 0x626200FF },
            { 0xFFFFBFFF, 0xE6D55AFF, 0xBFBF3FFF, 0xB1B10AFF },
            { 0xFFEA4AFF, 0xFFD510FF, 0xD79B0FFF, 0xAC9400FF },
            { 0xFFFFBFFF, 0xFFEA4AFF, 0xF6BD31FF, 0xD79B0FFF },
            { 0xFFFFBFFF, 0xC8FF41FF, 0x96DC19FF, 0x73C805FF },
            { 0xAFFFAFFF, 0x9BF046FF, 0x8EBE55FF, 0x73C805FF },
            { 0xC8FF41FF, 0x96DC19FF, 0x6AA805FF, 0x7F7F00FF },
            { 0x9BF046FF, 0x73C805FF, 0x7F7F00FF, 0x3C6E14FF },
            { 0x8EBE55FF, 0x6AA805FF, 0x626200FF, 0x53500AFF },
            { 0x578F57FF, 0x3C6E14FF, 0x204608FF, 0x283405FF },
            { 0x465032FF, 0x283405FF, 0x010101FF, 0x010101FF },
            { 0x465032FF, 0x204608FF, 0x131313FF, 0x010101FF },
            { 0x3B573BFF, 0x0C5C0CFF, 0x191E0FFF, 0x131313FF },
            { 0x3FBF3FFF, 0x149605FF, 0x0C5C0CFF, 0x204608FF },
            { 0x4BF05AFF, 0x0AD70AFF, 0x149605FF, 0x007F00FF },
            { 0x4BF05AFF, 0x14E60AFF, 0x149605FF, 0x007F00FF },
            { 0xAFFFAFFF, 0x7DFF73FF, 0x4BF05AFF, 0x3FBF3FFF },
            { 0x7DFF73FF, 0x4BF05AFF, 0x3FBF3FFF, 0x05B450FF },
            { 0x4BF05AFF, 0x00C514FF, 0x007F00FF, 0x0C5C0CFF },
            { 0x06C491FF, 0x05B450FF, 0x0F6946FF, 0x0C5C0CFF },
            { 0x578F57FF, 0x1C8C4EFF, 0x123832FF, 0x0F192DFF },
            { 0x3B5773FF, 0x123832FF, 0x010101FF, 0x010101FF },
            { 0x3FBFBFFF, 0x129880FF, 0x055A5CFF, 0x0C2148FF },
            { 0x3FBFBFFF, 0x06C491FF, 0x007F7FFF, 0x055A5CFF },
            { 0x2DEBA8FF, 0x00DE6AFF, 0x007F7FFF, 0x0F6946FF },
            { 0x6AFFCDFF, 0x2DEBA8FF, 0x06C491FF, 0x129880FF },
            { 0xBFFFFFFF, 0x3CFEA5FF, 0x00DE6AFF, 0x06C491FF },
            { 0xBFFFFFFF, 0x6AFFCDFF, 0x2DEBA8FF, 0x3FBFBFFF },
            { 0xBFFFFFFF, 0x91EBFFFF, 0x5AC5FFFF, 0x3FBFBFFF },
            { 0xBFFFFFFF, 0x55E6FFFF, 0x08DED5FF, 0x4AA4FFFF },
            { 0xBFFFFFFF, 0x7DD7F0FF, 0x3FBFBFFF, 0x699DC3FF },
            { 0x00FFFFFF, 0x08DED5FF, 0x109CDEFF, 0x007F7FFF },
            { 0x4AA4FFFF, 0x109CDEFF, 0x186ABDFF, 0x004A9CFF },
            { 0x129880FF, 0x055A5CFF, 0x0C2148FF, 0x0F0F50FF },
            { 0x3B5773FF, 0x162C52FF, 0x010101FF, 0x010101FF },
            { 0x3F3FBFFF, 0x0F377DFF, 0x0F0F50FF, 0x00007FFF },
            { 0x186ABDFF, 0x004A9CFF, 0x00007FFF, 0x010101FF },
            { 0x4B7DC8FF, 0x326496FF, 0x0F377DFF, 0x162C52FF },
            { 0x007FFFFF, 0x0052F6FF, 0x101CDAFF, 0x0010BDFF },
            { 0x4B7DC8FF, 0x186ABDFF, 0x004A9CFF, 0x0F377DFF },
            { 0x4AA4FFFF, 0x2378DCFF, 0x004A9CFF, 0x0010BDFF },
            { 0x90B0FFFF, 0x699DC3FF, 0x4B7DC8FF, 0x57738FFF },
            { 0x55E6FFFF, 0x4AA4FFFF, 0x109CDEFF, 0x2378DCFF },
            { 0x91EBFFFF, 0x90B0FFFF, 0x8181FFFF, 0x786EF0FF },
            { 0x91EBFFFF, 0x5AC5FFFF, 0x109CDEFF, 0x2378DCFF },
            { 0xE3E3FFFF, 0xBEB9FAFF, 0x8F8FC7FF, 0x7676CAFF },
            { 0x00FFFFFF, 0x00BFFFFF, 0x007FFFFF, 0x0052F6FF },
            { 0x00BFFFFF, 0x007FFFFF, 0x0052F6FF, 0x004A9CFF },
            { 0x8181FFFF, 0x4B7DC8FF, 0x186ABDFF, 0x326496FF },
            { 0xB991FFFF, 0x786EF0FF, 0x6241F6FF, 0x3F3FBFFF },
            { 0x786EF0FF, 0x4A5AFFFF, 0x101CDAFF, 0x0010BDFF },
            { 0x786EF0FF, 0x6241F6FF, 0x6010D0FF, 0x101CDAFF },
            { 0x8181FFFF, 0x3C3CF5FF, 0x101CDAFF, 0x0010BDFF },
            { 0x3C3CF5FF, 0x101CDAFF, 0x00007FFF, 0x010101FF },
            { 0x3C3CF5FF, 0x0010BDFF, 0x010101FF, 0x010101FF },
            { 0x3F3FBFFF, 0x231094FF, 0x010101FF, 0x010101FF },
            { 0x3B3B57FF, 0x0C2148FF, 0x010101FF, 0x010101FF },
            { 0x8732D2FF, 0x5010B0FF, 0x00007FFF, 0x010101FF },
            { 0x6241F6FF, 0x6010D0FF, 0x231094FF, 0x00007FFF },
            { 0xBD62FFFF, 0x8732D2FF, 0x5010B0FF, 0x410062FF },
            { 0xBD62FFFF, 0x9C41FFFF, 0x7F00FFFF, 0x6010D0FF },
            { 0xBD29FFFF, 0x7F00FFFF, 0x231094FF, 0x00007FFF },
            { 0xB991FFFF, 0xBD62FFFF, 0x9C41FFFF, 0x8732D2FF },
            { 0xD7C3FAFF, 0xB991FFFF, 0xBD62FFFF, 0x8F57C7FF },
            { 0xF8C6FCFF, 0xD7A5FFFF, 0xBD62FFFF, 0x8F57C7FF },
            { 0xFFFFFFFF, 0xD7C3FAFF, 0xABABE3FF, 0xAB8FC7FF },
            { 0xFFFFFFFF, 0xF8C6FCFF, 0xD7A5FFFF, 0xC78FB9FF },
            { 0xD7A5FFFF, 0xE673FFFF, 0xBF3FBFFF, 0x8732D2FF },
            { 0xFD81FFFF, 0xFF52FFFF, 0xDA20E0FF, 0xBD10C5FF },
            { 0xFF52FFFF, 0xDA20E0FF, 0x8C14BEFF, 0x7F007FFF },
            { 0xBD62FFFF, 0xBD29FFFF, 0x8C14BEFF, 0x7F00FFFF },
            { 0xFF52FFFF, 0xBD10C5FF, 0x7F007FFF, 0x410062FF },
            { 0xBD29FFFF, 0x8C14BEFF, 0x5010B0FF, 0x7F007FFF },
            { 0x724072FF, 0x5A187BFF, 0x0F0F50FF, 0x010101FF },
            { 0xA01982FF, 0x641464FF, 0x280A1EFF, 0x010101FF },
            { 0x573B73FF, 0x410062FF, 0x010101FF, 0x010101FF },
            { 0x5A187BFF, 0x320A46FF, 0x010101FF, 0x010101FF },
            { 0x724072FF, 0x551937FF, 0x010101FF, 0x010101FF },
            { 0xBF3FBFFF, 0xA01982FF, 0x410062FF, 0x280A1EFF },
            { 0xFC3A8CFF, 0xC80078FF, 0x7F007FFF, 0x7F0000FF },
            { 0xFD81FFFF, 0xFF50BFFF, 0xFC3A8CFF, 0xE61E78FF },
            { 0xFD81FFFF, 0xFF6AC5FF, 0xFC3A8CFF, 0xE61E78FF },
            { 0xF8D2DAFF, 0xFAA0B9FF, 0xFF6AC5FF, 0xC87DA0FF },
            { 0xFF50BFFF, 0xFC3A8CFF, 0xC80078FF, 0x911437FF },
            { 0xFF50BFFF, 0xE61E78FF, 0x911437FF, 0x7F0000FF },
            { 0xE61E78FF, 0xBD1039FF, 0x7F0000FF, 0x280A1EFF },
            { 0xC35A91FF, 0x98344DFF, 0x551937FF, 0x551414FF },
            { 0xBF3F3FFF, 0x911437FF, 0x7F0000FF, 0x280A1EFF },
    };
    /**
     * Bytes that correspond to palette indices to use when shading an Aurora-palette model.
     * The color in index 1 of each 4-element sub-array is the "dimmer" color and the one that will match the voxel
     * color used in a model. The color at index 0 is "bright", index 2 is "dim", and index 3 is "dark". Normal usage
     * with a renderer will use {@link #AURORA_RAMP_VALUES}; this array would be used to figure out what indices are related to
     * another color for the purpose of procedurally using similar colors with different lightness.
     * To visualize this, <a href="https://i.imgur.com/rQDrPE7.png">use this image</a>, with the first 32 items in
     * AURORA_RAMPS corresponding to the first column from top to bottom, the next 32 items in the second column, etc.
     */
    public static final byte[][] AURORA_RAMPS = new byte[][]{
        { 0, 0, 0, 0 },
        { 3, 1, 1, 1 },
        { -124, 2, 1, 1 },
        { 119, 3, 1, 1 },
        { 6, 4, 117, 2 },
        { 7, 5, -124, 3 },
        { 51, 6, 119, 4 },
        { 50, 7, 5, 119 },
        { 10, 8, 6, 5 },
        { 47, 9, 7, 6 },
        { 12, 10, 8, 51 },
        { -116, 11, 9, 50 },
        { 127, 12, 10, 9 },
        { 15, 13, 11, 47 },
        { 15, 14, 12, 11 },
        { 15, 15, 13, -116 },
        { -65, 16, -53, -32 },
        { -44, 17, -65, -66 },
        { -59, 18, -42, -56 },
        { 15, 19, 111, 107 },
        { -25, 20, -38, -37 },
        { -36, 21, 23, 1 },
        { -38, 22, -31, -33 },
        { -35, 23, 1, 1 },
        { -53, 24, 1, 1 },
        { -16, 25, -12, 1 },
        { -20, 26, -17, -16 },
        { -20, 27, -16, 25 },
        { -22, 28, -20, 26 },
        { 15, 29, -114, -113 },
        { -6, 30, -103, 32 },
        { -102, 31, -105, 33 },
        { -100, 32, -3, -1 },
        { -1, 33, -108, 1 },
        { 61, 34, 1, 1 },
        { 32, 35, -106, -107 },
        { 56, 36, 32, -94 },
        { -98, 37, -95, -104 },
        { 39, 38, 56, 55 },
        { 15, 39, 79, 68 },
        { 39, 40, -88, -89 },
        { -87, 41, -89, 42 },
        { -88, 42, -92, -93 },
        { -70, 43, 87, 1 },
        { -71, 44, -68, -75 },
        { -71, 45, -70, -75 },
        { 97, 46, 95, 100 },
        { -116, 47, 9, -119 },
        { 68, 48, 54, 64 },
        { 12, 49, 8, 7 },
        { 11, 50, 7, -126 },
        { 10, 51, 6, 120 },
        { 9, 52, 5, 61 },
        { 65, 53, -2, 62 },
        { 56, 54, 63, -2 },
        { 57, 55, 53, 63 },
        { 59, 56, 54, 53 },
        { 29, 57, 55, 54 },
        { 67, 58, 55, 54 },
        { 39, 59, 48, 65 },
        { 15, 60, 12, -115 },
        { 63, 61, -109, -108 },
        { 52, 62, -11, -107 },
        { 64, 63, 61, -11 },
        { 65, 64, 63, 62 },
        { 66, 65, 64, 53 },
        { 67, 66, 65, -112 },
        { 15, 67, -114, -113 },
        { 14, 68, 48, 65 },
        { 66, 69, 64, 53 },
        { 9, 70, 71, 62 },
        { 70, 71, 73, 72 },
        { 5, 72, 1, 1 },
        { 71, 73, 87, 2 },
        { 75, 74, 86, 73 },
        { 49, 75, 74, 71 },
        { 48, 76, 70, 74 },
        { 68, 77, 75, -90 },
        { 80, 78, 69, 76 },
        { 14, 79, 48, 49 },
        { 15, 80, 12, 11 },
        { 14, 81, 82, 95 },
        { 81, 82, 9, 75 },
        { 82, 83, 84, 85 },
        { 100, 84, 90, 86 },
        { 93, 85, 89, 86 },
        { 101, 86, 103, 87 },
        { 4, 87, 1, 1 },
        { 91, 88, 117, 2 },
        { 101, 89, 103, 3 },
        { 51, 90, 4, 103 },
        { 51, 91, 88, -67 },
        { 93, 92, 89, 88 },
        { 95, 93, 92, 101 },
        { 107, 94, 92, 101 },
        { 99, 95, 93, 51 },
        { 109, 96, 100, 93 },
        { 15, 97, 110, 111 },
        { 97, 98, 107, 95 },
        { 97, 99, 100, 106 },
        { 96, 100, 8, 51 },
        { 50, 101, 89, 88 },
        { 105, 102, -67, -32 },
        { 5, 103, 1, 1 },
        { 105, 104, 117, 1 },
        { 51, 105, 119, 104 },
        { 107, 106, 115, 105 },
        { 108, 107, 106, 114 },
        { 97, 108, 107, 113 },
        { 15, 109, 111, 112 },
        { 97, 110, 125, 113 },
        { 127, 111, 113, 124 },
        { 126, 112, 124, 123 },
        { 111, 113, 50, 122 },
        { -45, 114, -51, -53 },
        { 50, 115, 116, 120 },
        { 115, 116, -54, -32 },
        { 104, 117, 1, 1 },
        { 119, 118, 1, 1 },
        { 121, 119, 118, 117 },
        { 115, 120, -54, 118 },
        { 123, 121, 119, -54 },
        { 124, 122, 120, -125 },
        { -45, 123, 121, 22 },
        { 125, 124, 122, -126 },
        { 110, 125, 124, 123 },
        { 15, 126, 112, 125 },
        { 15, 127, -43, 112 },
        { -43, -128, 122, -126 },
        { -26, -127, -16, -15 },
        { -119, -126, -125, -15 },
        { -126, -125, -13, -12 },
        { 5, -124, 1, 1 },
        { -122, -123, -108, 1 },
        { -120, -122, -14, -124 },
        { -119, -121, -125, -14 },
        { -128, -120, -122, -10 },
        { -114, -119, -121, -126 },
        { -117, -118, -113, -112 },
        { 15, -117, -115, -118 },
        { 15, -116, 47, -113 },
        { -117, -115, -113, -119 },
        { -116, -114, -112, -119 },
        { -118, -113, -120, -121 },
        { -114, -112, -120, -121 },
        { -113, -111, 63, -10 },
        { -122, -110, -108, 1 },
        { -123, -109, 1, 1 },
        { -110, -108, 1, 1 },
        { 61, -107, 1, 1 },
        { 35, -106, -108, 1 },
        { 32, -105, -108, 1 },
        { -101, -104, 33, -108 },
        { 30, -103, -3, -1 },
        { -101, -102, -105, 33 },
        { 30, -101, -102, -104 },
        { 57, -100, 32, -3 },
        { -85, -99, -97, -89 },
        { 38, -98, -97, -96 },
        { -98, -97, -95, -94 },
        { -98, -96, -94, -105 },
        { 36, -95, 35, -105 },
        { -96, -94, -105, -106 },
        { 71, -93, -108, 1 },
        { 74, -92, -78, 87 },
        { 74, -91, -78, -107 },
        { 9, -90, 71, 86 },
        { 41, -89, -94, 35 },
        { -87, -88, 42, -91 },
        { 39, -87, 41, -88 },
        { -85, -86, -97, -89 },
        { 39, -85, -99, -97 },
        { 39, -84, -82, -81 },
        { 46, -83, 83, -81 },
        { -84, -82, -80, 42 },
        { -83, -81, 42, -79 },
        { 83, -80, -91, -92 },
        { 92, -79, -77, -78 },
        { 86, -78, 1, 1 },
        { 86, -77, 2, 1 },
        { 89, -76, 87, 2 },
        { 44, -75, -76, -77 },
        { -71, -74, -75, 43 },
        { -71, -73, -75, 43 },
        { 46, -72, -71, 44 },
        { -72, -71, 44, -69 },
        { -71, -70, 43, -76 },
        { -65, -69, 102, -76 },
        { 92, -68, -67, 117 },
        { 116, -67, 1, 1 },
        { 17, -66, -55, -32 },
        { 17, -65, 16, -55 },
        { -63, -64, 16, 102 },
        { -61, -63, -65, -66 },
        { 19, -62, -64, -65 },
        { 19, -61, -63, 17 },
        { 19, -60, -44, 17 },
        { 19, -59, -57, -46 },
        { 19, -58, 17, -47 },
        { 18, -57, -56, 16 },
        { -46, -56, -49, -52 },
        { -66, -55, -32, 24 },
        { 116, -54, 1, 1 },
        { 22, -53, 24, 23 },
        { -49, -52, 23, 1 },
        { -40, -51, -53, -54 },
        { -41, -50, -35, -34 },
        { -40, -49, -52, -53 },
        { -46, -48, -52, -34 },
        { -45, -47, -40, 115 },
        { -59, -46, -56, -48 },
        { -60, -45, 20, -39 },
        { -60, -44, -56, -48 },
        { 127, -43, 124, 123 },
        { 18, -42, -41, -50 },
        { -42, -41, -50, -52 },
        { 20, -40, -49, -51 },
        { -25, -39, -37, 22 },
        { -39, -38, -35, -34 },
        { -39, -37, -30, -35 },
        { 20, -36, -35, -34 },
        { -36, -35, 23, 1 },
        { -36, -34, 1, 1 },
        { 22, -33, 1, 1 },
        { 119, -32, 1, 1 },
        { -29, -31, 23, 1 },
        { -37, -30, -33, 23 },
        { -26, -29, -31, -13 },
        { -26, -28, -27, -30 },
        { -18, -27, -33, 23 },
        { -25, -26, -28, -29 },
        { -23, -25, -26, -127 },
        { -22, -24, -26, -127 },
        { 15, -23, 125, -128 },
        { 15, -22, -24, -113 },
        { -24, -21, 26, -29 },
        { 28, -20, -19, -17 },
        { -20, -19, -16, 25 },
        { -26, -18, -16, -27 },
        { -20, -17, 25, -13 },
        { -18, -16, -31, 25 },
        { -122, -15, 24, 1 },
        { -10, -14, -108, 1 },
        { -125, -13, 1, 1 },
        { -15, -12, 1, 1 },
        { -122, -11, 1, 1 },
        { 26, -10, -13, -108 },
        { -5, -9, 25, 33 },
        { 28, -8, -5, -4 },
        { 28, -7, -5, -4 },
        { 67, -6, -7, -112 },
        { -8, -5, -9, -1 },
        { -8, -4, -1, 33 },
        { -4, -3, 33, -108 },
        { -111, -2, -11, 34 },
        { 32, -1, 33, -108 },
    };

    public static final int[] FlesurrectBonusPalette = new int[256];
    private static final int[][] FLESURRECT_BONUS_RAMP_VALUES = new int[][]{
            {0x00000000, 0x00000000, 0x00000000, 0x00000000},
            {0x0A0A23FF, 0x11112AFF, 0x1F1833FF, 0x1F1F38FF},
            {0x12122BFF, 0x1D1D36FF, 0x2B2E42FF, 0x33334CFF},
            {0x23122BFF, 0x312039FF, 0x3E3546FF, 0x4C3C55FF},
            {0x1E1E37FF, 0x2F2F48FF, 0x414859FF, 0x51516AFF},
            {0x2A3A43FF, 0x45555EFF, 0x68717AFF, 0x7B8B93FF},
            {0x405059FF, 0x66767EFF, 0x90A1A8FF, 0xB1C2CAFF},
            {0x54646CFF, 0x84949CFF, 0xB6CBCFFF, 0xE3F4FCFF},
            {0x607078FF, 0x96A6AEFF, 0xD3E5EDFF, 0xFFFFFFFF},
            {0x7A7A82FF, 0xB8B8C0FF, 0xFFFFFFFF, 0xFFFFFFFF},
            {0x33121AFF, 0x43222AFF, 0x5C3A41FF, 0x63424AFF},
            {0x47263FFF, 0x624159FF, 0x826481FF, 0x97778FFF},
            {0x57262EFF, 0x74434BFF, 0x966C6CFF, 0xAE7C85FF},
            {0x392820FF, 0x503F37FF, 0x715A56FF, 0x7D6D65FF},
            {0x5F3E25FF, 0x836249FF, 0xAB947AFF, 0xCAAA91FF},
            {0x9E1B23FF, 0xC5424AFF, 0xF68181FF, 0xFF8F98FF},
            {0xB40000FF, 0xCC0000FF, 0xF53333FF, 0xFC262EFF},
            {0x420000FF, 0x490000FF, 0x5A0A07FF, 0x570500FF},
            {0x740100FF, 0x8A170FFF, 0xAE4539FF, 0xB6433BFF},
            {0x4D1C03FF, 0x633219FF, 0x8A503EFF, 0x8F5E45FF},
            {0x7C1900FF, 0x99360DFF, 0xCD683DFF, 0xD37047FF},
            {0x983500FF, 0xC15E14FF, 0xFBA458FF, 0xFFB066FF},
            {0xA10D00FF, 0xBF2B00FF, 0xFB6B1DFF, 0xFB671DFF},
            {0x735239FF, 0xA18067FF, 0xDDBBA4FF, 0xFCDBC3FF},
            {0x8B5A31FF, 0xBF8E64FF, 0xFDD7AAFF, 0xFFF5CCFF},
            {0x903E00FF, 0xB56300FF, 0xFFA514FF, 0xFFAC10FF},
            {0x69380FFF, 0x8C5B32FF, 0xC29162FF, 0xD2A077FF},
            {0x7E4C00FF, 0xA37100FF, 0xE8B710FF, 0xECBB0EFF},
            {0x876600FF, 0xB59400FF, 0xFBE626FF, 0xFFF033FF},
            {0x5B5B00FF, 0x7E7E00FF, 0xC0B510FF, 0xC3C317FF},
            {0x7B7B00FF, 0xB0B035FF, 0xFBFF86FF, 0xFFFFA0FF},
            {0x467700FF, 0x6FA004FF, 0xB4D645FF, 0xC0F255FF},
            {0x2C4D03FF, 0x48691FFF, 0x729446FF, 0x80A157FF},
            {0x56774DFF, 0x88A980FF, 0xC8E4BEFF, 0xEEFFE6FF},
            {0x00A100FF, 0x0DC200FF, 0x45F520FF, 0x4FFF25FF},
            {0x077A00FF, 0x26990DFF, 0x51C43FFF, 0x63D74BFF},
            {0x003300FF, 0x003C00FF, 0x0E4904FF, 0x0C4E04FF},
            {0x009227FF, 0x25B94EFF, 0x55F084FF, 0x73FF9CFF},
            {0x007E34FF, 0x009A50FF, 0x1EBC73FF, 0x2DD288FF},
            {0x009269FF, 0x02B78EFF, 0x30E1B9FF, 0x4CFFD8FF},
            {0x1D7F67FF, 0x48AB92FF, 0x7FE0C2FF, 0xA0FFEAFF},
            {0x45878FFF, 0x7DBEC7FF, 0xB8FDFFFF, 0xECFFFFFF},
            {0x007047FF, 0x00875EFF, 0x039F78FF, 0x10B58CFF},
            {0x0E7179FF, 0x3598A0FF, 0x63C2C9FF, 0x83E6EEFF},
            {0x00435CFF, 0x055770FF, 0x216981FF, 0x2D7F98FF},
            {0x168992FF, 0x46B9C1FF, 0x7FE8F2FF, 0xA6FFFFFF},
            {0x1C2CB8FF, 0x3748D4FF, 0x5369EFFF, 0x6F80FFFF},
            {0x0557A1FF, 0x2578C2FF, 0x4D9BE6FF, 0x67B9FFFF},
            {0x081852FF, 0x15255FFF, 0x28306FFF, 0x2F3F79FF},
            {0x1B3C86FF, 0x3859A3FF, 0x5C76BFFF, 0x7293DDFF},
            {0x1C1C97FF, 0x3232ADFF, 0x4D44C0FF, 0x5E5ED9FF},
            {0x0600C3FF, 0x1100CEFF, 0x180FCFFF, 0x2716E4FF},
            {0x31005AFF, 0x400F69FF, 0x53207DFF, 0x5E2C87FF},
            {0x491793FF, 0x6634B0FF, 0x8657CCFF, 0x9F6EE9FF},
            {0x54339EFF, 0x7B5AC5FF, 0xA884F3FF, 0xC9A8FFFF},
            {0x400048FF, 0x4B0054FF, 0x630867FF, 0x63116CFF},
            {0x64017CFF, 0x7E1C97FF, 0xA03EB2FF, 0xB452CDFF},
            {0x5C0095FF, 0x7100AAFF, 0x881AC4FF, 0x9A27D4FF},
            {0x7D3B96FF, 0xAD6BC5FF, 0xE4A8FAFF, 0xFFCBFFFF},
            {0x720049FF, 0x8C1963FF, 0xB53D86FF, 0xC04D97FF},
            {0x9C0094FF, 0xC11DB9FF, 0xF34FE9FF, 0xFF67FFFF},
            {0x520018FF, 0x631029FF, 0x7A3045FF, 0x84324BFF},
            {0x9F0024FF, 0xBE1942FF, 0xF04F78FF, 0xFC5780FF},
            {0x8F0000FF, 0xA40008FF, 0xC93038FF, 0xCE2932FF},

            {0x00000000, 0x00000000, 0x00000000, 0x00000000},
            {0x11112AFF, 0x1F1833FF, 0x1F1833FF, 0x1F1833FF},
            {0x1D1D36FF, 0x2B2E42FF, 0x2B2E42FF, 0x2B2E42FF},
            {0x312039FF, 0x3E3546FF, 0x3E3546FF, 0x3E3546FF},
            {0x2F2F48FF, 0x414859FF, 0x414859FF, 0x414859FF},
            {0x45555EFF, 0x68717AFF, 0x68717AFF, 0x68717AFF},
            {0x66767EFF, 0x90A1A8FF, 0x90A1A8FF, 0x90A1A8FF},
            {0x84949CFF, 0xB6CBCFFF, 0xB6CBCFFF, 0xB6CBCFFF},
            {0x96A6AEFF, 0xD3E5EDFF, 0xD3E5EDFF, 0xD3E5EDFF},
            {0xB8B8C0FF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF},
            {0x43222AFF, 0x5C3A41FF, 0x5C3A41FF, 0x5C3A41FF},
            {0x624159FF, 0x826481FF, 0x826481FF, 0x826481FF},
            {0x74434BFF, 0x966C6CFF, 0x966C6CFF, 0x966C6CFF},
            {0x503F37FF, 0x715A56FF, 0x715A56FF, 0x715A56FF},
            {0x836249FF, 0xAB947AFF, 0xAB947AFF, 0xAB947AFF},
            {0xC5424AFF, 0xF68181FF, 0xF68181FF, 0xF68181FF},
            {0xCC0000FF, 0xF53333FF, 0xF53333FF, 0xF53333FF},
            {0x490000FF, 0x5A0A07FF, 0x5A0A07FF, 0x5A0A07FF},
            {0x8A170FFF, 0xAE4539FF, 0xAE4539FF, 0xAE4539FF},
            {0x633219FF, 0x8A503EFF, 0x8A503EFF, 0x8A503EFF},
            {0x99360DFF, 0xCD683DFF, 0xCD683DFF, 0xCD683DFF},
            {0xC15E14FF, 0xFBA458FF, 0xFBA458FF, 0xFBA458FF},
            {0xBF2B00FF, 0xFB6B1DFF, 0xFB6B1DFF, 0xFB6B1DFF},
            {0xA18067FF, 0xDDBBA4FF, 0xDDBBA4FF, 0xDDBBA4FF},
            {0xBF8E64FF, 0xFDD7AAFF, 0xFDD7AAFF, 0xFDD7AAFF},
            {0xB56300FF, 0xFFA514FF, 0xFFA514FF, 0xFFA514FF},
            {0x8C5B32FF, 0xC29162FF, 0xC29162FF, 0xC29162FF},
            {0xA37100FF, 0xE8B710FF, 0xE8B710FF, 0xE8B710FF},
            {0xB59400FF, 0xFBE626FF, 0xFBE626FF, 0xFBE626FF},
            {0x7E7E00FF, 0xC0B510FF, 0xC0B510FF, 0xC0B510FF},
            {0xB0B035FF, 0xFBFF86FF, 0xFBFF86FF, 0xFBFF86FF},
            {0x6FA004FF, 0xB4D645FF, 0xB4D645FF, 0xB4D645FF},
            {0x48691FFF, 0x729446FF, 0x729446FF, 0x729446FF},
            {0x88A980FF, 0xC8E4BEFF, 0xC8E4BEFF, 0xC8E4BEFF},
            {0x0DC200FF, 0x45F520FF, 0x45F520FF, 0x45F520FF},
            {0x26990DFF, 0x51C43FFF, 0x51C43FFF, 0x51C43FFF},
            {0x003C00FF, 0x0E4904FF, 0x0E4904FF, 0x0E4904FF},
            {0x25B94EFF, 0x55F084FF, 0x55F084FF, 0x55F084FF},
            {0x009A50FF, 0x1EBC73FF, 0x1EBC73FF, 0x1EBC73FF},
            {0x02B78EFF, 0x30E1B9FF, 0x30E1B9FF, 0x30E1B9FF},
            {0x48AB92FF, 0x7FE0C2FF, 0x7FE0C2FF, 0x7FE0C2FF},
            {0x7DBEC7FF, 0xB8FDFFFF, 0xB8FDFFFF, 0xB8FDFFFF},
            {0x00875EFF, 0x039F78FF, 0x039F78FF, 0x039F78FF},
            {0x3598A0FF, 0x63C2C9FF, 0x63C2C9FF, 0x63C2C9FF},
            {0x055770FF, 0x216981FF, 0x216981FF, 0x216981FF},
            {0x46B9C1FF, 0x7FE8F2FF, 0x7FE8F2FF, 0x7FE8F2FF},
            {0x3748D4FF, 0x5369EFFF, 0x5369EFFF, 0x5369EFFF},
            {0x2578C2FF, 0x4D9BE6FF, 0x4D9BE6FF, 0x4D9BE6FF},
            {0x15255FFF, 0x28306FFF, 0x28306FFF, 0x28306FFF},
            {0x3859A3FF, 0x5C76BFFF, 0x5C76BFFF, 0x5C76BFFF},
            {0x3232ADFF, 0x4D44C0FF, 0x4D44C0FF, 0x4D44C0FF},
            {0x1100CEFF, 0x180FCFFF, 0x180FCFFF, 0x180FCFFF},
            {0x400F69FF, 0x53207DFF, 0x53207DFF, 0x53207DFF},
            {0x6634B0FF, 0x8657CCFF, 0x8657CCFF, 0x8657CCFF},
            {0x7B5AC5FF, 0xA884F3FF, 0xA884F3FF, 0xA884F3FF},
            {0x4B0054FF, 0x630867FF, 0x630867FF, 0x630867FF},
            {0x7E1C97FF, 0xA03EB2FF, 0xA03EB2FF, 0xA03EB2FF},
            {0x7100AAFF, 0x881AC4FF, 0x881AC4FF, 0x881AC4FF},
            {0xAD6BC5FF, 0xE4A8FAFF, 0xE4A8FAFF, 0xE4A8FAFF},
            {0x8C1963FF, 0xB53D86FF, 0xB53D86FF, 0xB53D86FF},
            {0xC11DB9FF, 0xF34FE9FF, 0xF34FE9FF, 0xF34FE9FF},
            {0x631029FF, 0x7A3045FF, 0x7A3045FF, 0x7A3045FF},
            {0xBE1942FF, 0xF04F78FF, 0xF04F78FF, 0xF04F78FF},
            {0xA40008FF, 0xC93038FF, 0xC93038FF, 0xC93038FF},

            {0x00000000, 0x00000000, 0x00000000, 0x00000000},
            {0x0A0A23FF, 0x11112AFF, 0x1F1833FF, 0x1F1F38FF},
            {0x12122BFF, 0x1D1D36FF, 0x2B2E42FF, 0x33334CFF},
            {0x23122BFF, 0x312039FF, 0x3E3546FF, 0x4C3C55FF},
            {0x1E1E37FF, 0x2F2F48FF, 0x414859FF, 0x51516AFF},
            {0x2A3A43FF, 0x45555EFF, 0x68717AFF, 0x7B8B93FF},
            {0x405059FF, 0x66767EFF, 0x90A1A8FF, 0xB1C2CAFF},
            {0x54646CFF, 0x84949CFF, 0xB6CBCFFF, 0xE3F4FCFF},
            {0x607078FF, 0x96A6AEFF, 0xD3E5EDFF, 0xFFFFFFFF},
            {0x7A7A82FF, 0xB8B8C0FF, 0xFFFFFFFF, 0xFFFFFFFF},
            {0x33121AFF, 0x43222AFF, 0x5C3A41FF, 0x63424AFF},
            {0x47263FFF, 0x624159FF, 0x826481FF, 0x97778FFF},
            {0x57262EFF, 0x74434BFF, 0x966C6CFF, 0xAE7C85FF},
            {0x392820FF, 0x503F37FF, 0x715A56FF, 0x7D6D65FF},
            {0x5F3E25FF, 0x836249FF, 0xAB947AFF, 0xCAAA91FF},
            {0x9E1B23FF, 0xC5424AFF, 0xF68181FF, 0xFF8F98FF},
            {0xB40000FF, 0xCC0000FF, 0xF53333FF, 0xFC262EFF},
            {0x420000FF, 0x490000FF, 0x5A0A07FF, 0x570500FF},
            {0x740100FF, 0x8A170FFF, 0xAE4539FF, 0xB6433BFF},
            {0x4D1C03FF, 0x633219FF, 0x8A503EFF, 0x8F5E45FF},
            {0x7C1900FF, 0x99360DFF, 0xCD683DFF, 0xD37047FF},
            {0x983500FF, 0xC15E14FF, 0xFBA458FF, 0xFFB066FF},
            {0xA10D00FF, 0xBF2B00FF, 0xFB6B1DFF, 0xFB671DFF},
            {0x735239FF, 0xA18067FF, 0xDDBBA4FF, 0xFCDBC3FF},
            {0x8B5A31FF, 0xBF8E64FF, 0xFDD7AAFF, 0xFFF5CCFF},
            {0x903E00FF, 0xB56300FF, 0xFFA514FF, 0xFFAC10FF},
            {0x69380FFF, 0x8C5B32FF, 0xC29162FF, 0xD2A077FF},
            {0x7E4C00FF, 0xA37100FF, 0xE8B710FF, 0xECBB0EFF},
            {0x876600FF, 0xB59400FF, 0xFBE626FF, 0xFFF033FF},
            {0x5B5B00FF, 0x7E7E00FF, 0xC0B510FF, 0xC3C317FF},
            {0x7B7B00FF, 0xB0B035FF, 0xFBFF86FF, 0xFFFFA0FF},
            {0x467700FF, 0x6FA004FF, 0xB4D645FF, 0xC0F255FF},
            {0x2C4D03FF, 0x48691FFF, 0x729446FF, 0x80A157FF},
            {0x56774DFF, 0x88A980FF, 0xC8E4BEFF, 0xEEFFE6FF},
            {0x00A100FF, 0x0DC200FF, 0x45F520FF, 0x4FFF25FF},
            {0x077A00FF, 0x26990DFF, 0x51C43FFF, 0x63D74BFF},
            {0x003300FF, 0x003C00FF, 0x0E4904FF, 0x0C4E04FF},
            {0x009227FF, 0x25B94EFF, 0x55F084FF, 0x73FF9CFF},
            {0x007E34FF, 0x009A50FF, 0x1EBC73FF, 0x2DD288FF},
            {0x009269FF, 0x02B78EFF, 0x30E1B9FF, 0x4CFFD8FF},
            {0x1D7F67FF, 0x48AB92FF, 0x7FE0C2FF, 0xA0FFEAFF},
            {0x45878FFF, 0x7DBEC7FF, 0xB8FDFFFF, 0xECFFFFFF},
            {0x007047FF, 0x00875EFF, 0x039F78FF, 0x10B58CFF},
            {0x0E7179FF, 0x3598A0FF, 0x63C2C9FF, 0x83E6EEFF},
            {0x00435CFF, 0x055770FF, 0x216981FF, 0x2D7F98FF},
            {0x168992FF, 0x46B9C1FF, 0x7FE8F2FF, 0xA6FFFFFF},
            {0x1C2CB8FF, 0x3748D4FF, 0x5369EFFF, 0x6F80FFFF},
            {0x0557A1FF, 0x2578C2FF, 0x4D9BE6FF, 0x67B9FFFF},
            {0x081852FF, 0x15255FFF, 0x28306FFF, 0x2F3F79FF},
            {0x1B3C86FF, 0x3859A3FF, 0x5C76BFFF, 0x7293DDFF},
            {0x1C1C97FF, 0x3232ADFF, 0x4D44C0FF, 0x5E5ED9FF},
            {0x0600C3FF, 0x1100CEFF, 0x180FCFFF, 0x2716E4FF},
            {0x31005AFF, 0x400F69FF, 0x53207DFF, 0x5E2C87FF},
            {0x491793FF, 0x6634B0FF, 0x8657CCFF, 0x9F6EE9FF},
            {0x54339EFF, 0x7B5AC5FF, 0xA884F3FF, 0xC9A8FFFF},
            {0x400048FF, 0x4B0054FF, 0x630867FF, 0x63116CFF},
            {0x64017CFF, 0x7E1C97FF, 0xA03EB2FF, 0xB452CDFF},
            {0x5C0095FF, 0x7100AAFF, 0x881AC4FF, 0x9A27D4FF},
            {0x7D3B96FF, 0xAD6BC5FF, 0xE4A8FAFF, 0xFFCBFFFF},
            {0x720049FF, 0x8C1963FF, 0xB53D86FF, 0xC04D97FF},
            {0x9C0094FF, 0xC11DB9FF, 0xF34FE9FF, 0xFF67FFFF},
            {0x520018FF, 0x631029FF, 0x7A3045FF, 0x84324BFF},
            {0x9F0024FF, 0xBE1942FF, 0xF04F78FF, 0xFC5780FF},
            {0x8F0000FF, 0xA40008FF, 0xC93038FF, 0xCE2932FF},

            {0x00000000, 0x00000000, 0x00000000, 0x00000000},
            {0x1F1833FF, 0x11112AFF, 0x1F1833FF, 0x1F1F38FF},
            {0x2B2E42FF, 0x1D1D36FF, 0x2B2E42FF, 0x33334CFF},
            {0x3E3546FF, 0x312039FF, 0x3E3546FF, 0x4C3C55FF},
            {0x414859FF, 0x2F2F48FF, 0x414859FF, 0x51516AFF},
            {0x68717AFF, 0x45555EFF, 0x68717AFF, 0x7B8B93FF},
            {0x90A1A8FF, 0x66767EFF, 0x90A1A8FF, 0xB1C2CAFF},
            {0xB6CBCFFF, 0x84949CFF, 0xB6CBCFFF, 0xE3F4FCFF},
            {0xD3E5EDFF, 0x96A6AEFF, 0xD3E5EDFF, 0xFFFFFFFF},
            {0xFFFFFFFF, 0xB8B8C0FF, 0xFFFFFFFF, 0xFFFFFFFF},
            {0x5C3A41FF, 0x43222AFF, 0x5C3A41FF, 0x63424AFF},
            {0x826481FF, 0x624159FF, 0x826481FF, 0x97778FFF},
            {0x966C6CFF, 0x74434BFF, 0x966C6CFF, 0xAE7C85FF},
            {0x715A56FF, 0x503F37FF, 0x715A56FF, 0x7D6D65FF},
            {0xAB947AFF, 0x836249FF, 0xAB947AFF, 0xCAAA91FF},
            {0xF68181FF, 0xC5424AFF, 0xF68181FF, 0xFF8F98FF},
            {0xF53333FF, 0xCC0000FF, 0xF53333FF, 0xFC262EFF},
            {0x5A0A07FF, 0x490000FF, 0x5A0A07FF, 0x570500FF},
            {0xAE4539FF, 0x8A170FFF, 0xAE4539FF, 0xB6433BFF},
            {0x8A503EFF, 0x633219FF, 0x8A503EFF, 0x8F5E45FF},
            {0xCD683DFF, 0x99360DFF, 0xCD683DFF, 0xD37047FF},
            {0xFBA458FF, 0xC15E14FF, 0xFBA458FF, 0xFFB066FF},
            {0xFB6B1DFF, 0xBF2B00FF, 0xFB6B1DFF, 0xFB671DFF},
            {0xDDBBA4FF, 0xA18067FF, 0xDDBBA4FF, 0xFCDBC3FF},
            {0xFDD7AAFF, 0xBF8E64FF, 0xFDD7AAFF, 0xFFF5CCFF},
            {0xFFA514FF, 0xB56300FF, 0xFFA514FF, 0xFFAC10FF},
            {0xC29162FF, 0x8C5B32FF, 0xC29162FF, 0xD2A077FF},
            {0xE8B710FF, 0xA37100FF, 0xE8B710FF, 0xECBB0EFF},
            {0xFBE626FF, 0xB59400FF, 0xFBE626FF, 0xFFF033FF},
            {0xC0B510FF, 0x7E7E00FF, 0xC0B510FF, 0xC3C317FF},
            {0xFBFF86FF, 0xB0B035FF, 0xFBFF86FF, 0xFFFFA0FF},
            {0xB4D645FF, 0x6FA004FF, 0xB4D645FF, 0xC0F255FF},
            {0x729446FF, 0x48691FFF, 0x729446FF, 0x80A157FF},
            {0xC8E4BEFF, 0x88A980FF, 0xC8E4BEFF, 0xEEFFE6FF},
            {0x45F520FF, 0x0DC200FF, 0x45F520FF, 0x4FFF25FF},
            {0x51C43FFF, 0x26990DFF, 0x51C43FFF, 0x63D74BFF},
            {0x0E4904FF, 0x003C00FF, 0x0E4904FF, 0x0C4E04FF},
            {0x55F084FF, 0x25B94EFF, 0x55F084FF, 0x73FF9CFF},
            {0x1EBC73FF, 0x009A50FF, 0x1EBC73FF, 0x2DD288FF},
            {0x30E1B9FF, 0x02B78EFF, 0x30E1B9FF, 0x4CFFD8FF},
            {0x7FE0C2FF, 0x48AB92FF, 0x7FE0C2FF, 0xA0FFEAFF},
            {0xB8FDFFFF, 0x7DBEC7FF, 0xB8FDFFFF, 0xECFFFFFF},
            {0x039F78FF, 0x00875EFF, 0x039F78FF, 0x10B58CFF},
            {0x63C2C9FF, 0x3598A0FF, 0x63C2C9FF, 0x83E6EEFF},
            {0x216981FF, 0x055770FF, 0x216981FF, 0x2D7F98FF},
            {0x7FE8F2FF, 0x46B9C1FF, 0x7FE8F2FF, 0xA6FFFFFF},
            {0x5369EFFF, 0x3748D4FF, 0x5369EFFF, 0x6F80FFFF},
            {0x4D9BE6FF, 0x2578C2FF, 0x4D9BE6FF, 0x67B9FFFF},
            {0x28306FFF, 0x15255FFF, 0x28306FFF, 0x2F3F79FF},
            {0x5C76BFFF, 0x3859A3FF, 0x5C76BFFF, 0x7293DDFF},
            {0x4D44C0FF, 0x3232ADFF, 0x4D44C0FF, 0x5E5ED9FF},
            {0x180FCFFF, 0x1100CEFF, 0x180FCFFF, 0x2716E4FF},
            {0x53207DFF, 0x400F69FF, 0x53207DFF, 0x5E2C87FF},
            {0x8657CCFF, 0x6634B0FF, 0x8657CCFF, 0x9F6EE9FF},
            {0xA884F3FF, 0x7B5AC5FF, 0xA884F3FF, 0xC9A8FFFF},
            {0x630867FF, 0x4B0054FF, 0x630867FF, 0x63116CFF},
            {0xA03EB2FF, 0x7E1C97FF, 0xA03EB2FF, 0xB452CDFF},
            {0x881AC4FF, 0x7100AAFF, 0x881AC4FF, 0x9A27D4FF},
            {0xE4A8FAFF, 0xAD6BC5FF, 0xE4A8FAFF, 0xFFCBFFFF},
            {0xB53D86FF, 0x8C1963FF, 0xB53D86FF, 0xC04D97FF},
            {0xF34FE9FF, 0xC11DB9FF, 0xF34FE9FF, 0xFF67FFFF},
            {0x7A3045FF, 0x631029FF, 0x7A3045FF, 0x84324BFF},
            {0xF04F78FF, 0xBE1942FF, 0xF04F78FF, 0xFC5780FF},
            {0xC93038FF, 0xA40008FF, 0xC93038FF, 0xCE2932FF},
    };
    public static final int[] LawnBonusPalette = new int[256];
    public static final byte[][] LAWN_RAMPS = new byte[][]{
            { 0, 0, 0, 0 },
            { 10, 63, 1, 58 },
            { 1, 1, 2, 42 },
            { 43, 57, 3, 57 },
            { 8, 2, 4, 2 },
            { 36, 24, 5, 32 },
            { 22, 31, 6, 31 },
            { 27, 41, 7, 41 },
            { 1, 1, 8, 19 },
            { 43, 57, 9, 57 },
            { 47, 27, 10, 27 },
            { 60, 48, 11, 48 },
            { 51, 56, 12, 56 },
            { 3, 15, 13, 62 },
            { 1, 1, 14, 47 },
            { 41, 57, 15, 34 },
            { 59, 34, 16, 34 },
            { 36, 32, 17, 32 },
            { 21, 63, 18, 57 },
            { 2, 43, 19, 43 },
            { 7, 61, 20, 13 },
            { 47, 27, 21, 41 },
            { 18, 48, 22, 51 },
            { 13, 55, 23, 59 },
            { 50, 36, 24, 63 },
            { 58, 22, 25, 53 },
            { 39, 9, 26, 9 },
            { 50, 42, 27, 42 },
            { 1, 8, 28, 19 },
            { 57, 61, 29, 61 },
            { 36, 32, 30, 18 },
            { 25, 51, 31, 12 },
            { 10, 63, 32, 63 },
            { 44, 22, 33, 53 },
            { 57, 15, 34, 16 },
            { 39, 41, 35, 3 },
            { 50, 47, 36, 24 },
            { 61, 15, 37, 16 },
            { 43, 9, 38, 61 },
            { 8, 43, 39, 43 },
            { 1, 1, 40, 42 },
            { 43, 42, 41, 57 },
            { 14, 47, 42, 41 },
            { 2, 42, 43, 42 },
            { 24, 48, 44, 48 },
            { 46, 31, 45, 12 },
            { 44, 48, 46, 22 },
            { 1, 14, 47, 42 },
            { 57, 15, 48, 15 },
            { 60, 46, 49, 25 },
            { 40, 47, 50, 47 },
            { 48, 22, 51, 34 },
            { 60, 48, 52, 48 },
            { 25, 51, 53, 51 },
            { 7, 57, 54, 55 },
            { 54, 15, 55, 15 },
            { 51, 34, 56, 9 },
            { 42, 41, 57, 15 },
            { 1, 48, 58, 48 },
            { 15, 62, 59, 16 },
            { 5, 63, 60, 48 },
            { 9, 57, 61, 15 },
            { 26, 61, 62, 34 },
            { 27, 32, 63, 32 },
    };
    private static final int[][] LAWN_BONUS_RAMP_VALUES = new int[][] {
            { 0x00000000, 0x00000000, 0x00000000, 0x00000000 },
            { 0x12A315FF, 0x34B337FF, 0x31E635FF, 0x82F884FF },
            { 0x042367FF, 0x112C67FF, 0x0F368AFF, 0x2C467DFF },
            { 0x03847BFF, 0x24958DFF, 0x1CBDB2FF, 0x6BD4CDFF },
            { 0x200DB1FF, 0x2C1CABFF, 0x341DE9FF, 0x4E3FC4FF },
            { 0x724500FF, 0x835B14FF, 0xA56D07FF, 0xBE9956FF },
            { 0x8F9D00FF, 0xB2BE32FF, 0xD9EA22FF, 0xFFFFA4FF },
            { 0xA5056FFF, 0xAF2481FF, 0xE41DA2FF, 0xE967BEFF },
            { 0x15075EFF, 0x1B0F5BFF, 0x21107CFF, 0x2E236AFF },
            { 0x205686FF, 0x396992FF, 0x3D81BCFF, 0x75A2C8FF },
            { 0x69080FFF, 0x6E191FFF, 0x901720FF, 0x8E3F45FF },
            { 0x7E7A00FF, 0x99962AFF, 0xBCB71EFF, 0xEFEB88FF },
            { 0x94A365FF, 0xBECB94FF, 0xE4F7A9FF, 0xFFFFFFFF },
            { 0xA23BA9FF, 0xBB60C1FF, 0xEB6AF3FF, 0xFFBDFFFF },
            { 0x110613FF, 0x140A16FF, 0x1A0C1CFF, 0x1E151FFF },
            { 0x676475FF, 0x848190FF, 0x9F9BB0FF, 0xD7D4E2FF },
            { 0x739F99FF, 0x9EC4BFFF, 0xB9EFE8FF, 0xFFFFFFFF },
            { 0xAB070AFF, 0xB22225FF, 0xEA1D21FF, 0xE35D60FF },
            { 0x06852FFF, 0x249347FF, 0x1EBC51FF, 0x64CB85FF },
            { 0x1A3395FF, 0x2C4398FF, 0x3050CAFF, 0x596EBEFF },
            { 0x1098A5FF, 0x37AFBAFF, 0x32DCECFF, 0x8FFEFFFF },
            { 0x85084CFF, 0x8E215CFF, 0xB81D71FF, 0xBC578EFF },
            { 0x985D4DFF, 0xB27F71FF, 0xDE9581FF, 0xFFDBCEFF },
            { 0x1CAB92FF, 0x47C4AEFF, 0x45F7D8FF, 0xA8FFFFFF },
            { 0x433E08FF, 0x514E1EFF, 0x645F1BFF, 0x7F7C4FFF },
            { 0x689E54FF, 0x8FBE7DFF, 0xA7EA8EFF, 0xF7FFE7FF },
            { 0x7209ACFF, 0x7F24B2FF, 0xA220EBFF, 0xB45FE3FF },
            { 0x034F26FF, 0x155734FF, 0x12703DFF, 0x3D7A59FF },
            { 0x151187FF, 0x201C83FF, 0x251FB2FF, 0x3C3898FF },
            { 0x1671A4FF, 0x3685B2FF, 0x35A6E6FF, 0x7FC8F2FF },
            { 0x8D2E0EFF, 0x9A472BFF, 0xC65028FF, 0xD5886DFF },
            { 0x9F8A49FF, 0xC2B077FF, 0xEED483FF, 0xFFFFEAFF },
            { 0x196418FF, 0x2F712FFF, 0x328F31FF, 0x64A163FF },
            { 0x58A900FF, 0x7CC32EFF, 0x90F521FF, 0xDCFF94FF },
            { 0x8F7992FF, 0xB3A0B5FF, 0xD9BEDDFF, 0xFFFFFFFF },
            { 0xAC00A5FF, 0xB820B2FF, 0xEF16E6FF, 0xF669F1FF },
            { 0x1F3702FF, 0x2B4011FF, 0x33510EFF, 0x4C5F34FF },
            { 0x7464AEFF, 0x9486C7FF, 0xB29FFBFF, 0xF0E4FFFF },
            { 0x1E55B1FF, 0x3969BAFF, 0x3C80F3FF, 0x79A5F0FF },
            { 0x004D6CFF, 0x125873FF, 0x0C6F96FF, 0x3F7F99FF },
            { 0x021706FF, 0x07190BFF, 0x07210CFF, 0x132416FF },
            { 0x19615EFF, 0x32716EFF, 0x348D8AFF, 0x6DA7A5FF },
            { 0x2F2A40FF, 0x3C384BFF, 0x49435EFF, 0x625E70FF },
            { 0x56075CFF, 0x5E1964FF, 0x791781FF, 0x824287FF },
            { 0x8A5415FF, 0x9F7039FF, 0xC88536FF, 0xEABE8BFF },
            { 0xA7A809FF, 0xCECE44FF, 0xFBFC37FF, 0xFFFFC3FF },
            { 0x618C12FF, 0x81A73CFF, 0x99CF37FF, 0xD9FC99FF },
            { 0x12270FFF, 0x1B2D18FF, 0x1F391BFF, 0x324330FF },
            { 0x5B7549FF, 0x789069FF, 0x8FB079FF, 0xCBE1BDFF },
            { 0xA47400FF, 0xBE951FFF, 0xEEB30BFF, 0xFFF385FF },
            { 0x3E0604FF, 0x41100FFF, 0x56100EFF, 0x552726FF },
            { 0x728968FF, 0x96AA8DFF, 0xB2CFA6FF, 0xFAFFF2FF },
            { 0xA94500FF, 0xBB6322FF, 0xEF7215FF, 0xFFB376FF },
            { 0xA76F58FF, 0xC69581FF, 0xF6B093FF, 0xFFFEEBFF },
            { 0x1B9A59FF, 0x40AF76FF, 0x3FDD8CFF, 0x93FAC5FF },
            { 0xA73976FF, 0xBD5C92FF, 0xEF66B2FF, 0xFFB5E6FF },
            { 0x9E9E90FF, 0xCACABDFF, 0xF3F3E1FF, 0xFFFFFFFF },
            { 0x604061FF, 0x745975FF, 0x8F6890FF, 0xB49AB4FF },
            { 0xAC4042FF, 0xC16364FF, 0xF56F71FF, 0xFFB9BBFF },
            { 0xA6639AFF, 0xC68CBBFF, 0xF5A2E6FF, 0xFFF7FFFF },
            { 0x5E6A01FF, 0x768025FF, 0x8F9E1CFF, 0xBCC671FF },
            { 0x6D4599FF, 0x8663ACFF, 0xA472DBFF, 0xD1B0F5FF },
            { 0x5484AEFF, 0x7AA4C9FF, 0x8CC8FCFF, 0xDCFFFFFF },
            { 0x662F28FF, 0x74453EFF, 0x934F46FF, 0xA87C76FF },
            { 0x00000000, 0x00000000, 0x00000000, 0x00000000 },
            { 0x34B337FF, 0x31E635FF, 0x31E635FF, 0x31E635FF },
            { 0x112C67FF, 0x0F368AFF, 0x0F368AFF, 0x0F368AFF },
            { 0x24958DFF, 0x1CBDB2FF, 0x1CBDB2FF, 0x1CBDB2FF },
            { 0x2C1CABFF, 0x341DE9FF, 0x341DE9FF, 0x341DE9FF },
            { 0x835B14FF, 0xA56D07FF, 0xA56D07FF, 0xA56D07FF },
            { 0xB2BE32FF, 0xD9EA22FF, 0xD9EA22FF, 0xD9EA22FF },
            { 0xAF2481FF, 0xE41DA2FF, 0xE41DA2FF, 0xE41DA2FF },
            { 0x1B0F5BFF, 0x21107CFF, 0x21107CFF, 0x21107CFF },
            { 0x396992FF, 0x3D81BCFF, 0x3D81BCFF, 0x3D81BCFF },
            { 0x6E191FFF, 0x901720FF, 0x901720FF, 0x901720FF },
            { 0x99962AFF, 0xBCB71EFF, 0xBCB71EFF, 0xBCB71EFF },
            { 0xBECB94FF, 0xE4F7A9FF, 0xE4F7A9FF, 0xE4F7A9FF },
            { 0xBB60C1FF, 0xEB6AF3FF, 0xEB6AF3FF, 0xEB6AF3FF },
            { 0x140A16FF, 0x1A0C1CFF, 0x1A0C1CFF, 0x1A0C1CFF },
            { 0x848190FF, 0x9F9BB0FF, 0x9F9BB0FF, 0x9F9BB0FF },
            { 0x9EC4BFFF, 0xB9EFE8FF, 0xB9EFE8FF, 0xB9EFE8FF },
            { 0xB22225FF, 0xEA1D21FF, 0xEA1D21FF, 0xEA1D21FF },
            { 0x249347FF, 0x1EBC51FF, 0x1EBC51FF, 0x1EBC51FF },
            { 0x2C4398FF, 0x3050CAFF, 0x3050CAFF, 0x3050CAFF },
            { 0x37AFBAFF, 0x32DCECFF, 0x32DCECFF, 0x32DCECFF },
            { 0x8E215CFF, 0xB81D71FF, 0xB81D71FF, 0xB81D71FF },
            { 0xB27F71FF, 0xDE9581FF, 0xDE9581FF, 0xDE9581FF },
            { 0x47C4AEFF, 0x45F7D8FF, 0x45F7D8FF, 0x45F7D8FF },
            { 0x514E1EFF, 0x645F1BFF, 0x645F1BFF, 0x645F1BFF },
            { 0x8FBE7DFF, 0xA7EA8EFF, 0xA7EA8EFF, 0xA7EA8EFF },
            { 0x7F24B2FF, 0xA220EBFF, 0xA220EBFF, 0xA220EBFF },
            { 0x155734FF, 0x12703DFF, 0x12703DFF, 0x12703DFF },
            { 0x201C83FF, 0x251FB2FF, 0x251FB2FF, 0x251FB2FF },
            { 0x3685B2FF, 0x35A6E6FF, 0x35A6E6FF, 0x35A6E6FF },
            { 0x9A472BFF, 0xC65028FF, 0xC65028FF, 0xC65028FF },
            { 0xC2B077FF, 0xEED483FF, 0xEED483FF, 0xEED483FF },
            { 0x2F712FFF, 0x328F31FF, 0x328F31FF, 0x328F31FF },
            { 0x7CC32EFF, 0x90F521FF, 0x90F521FF, 0x90F521FF },
            { 0xB3A0B5FF, 0xD9BEDDFF, 0xD9BEDDFF, 0xD9BEDDFF },
            { 0xB820B2FF, 0xEF16E6FF, 0xEF16E6FF, 0xEF16E6FF },
            { 0x2B4011FF, 0x33510EFF, 0x33510EFF, 0x33510EFF },
            { 0x9486C7FF, 0xB29FFBFF, 0xB29FFBFF, 0xB29FFBFF },
            { 0x3969BAFF, 0x3C80F3FF, 0x3C80F3FF, 0x3C80F3FF },
            { 0x125873FF, 0x0C6F96FF, 0x0C6F96FF, 0x0C6F96FF },
            { 0x07190BFF, 0x07210CFF, 0x07210CFF, 0x07210CFF },
            { 0x32716EFF, 0x348D8AFF, 0x348D8AFF, 0x348D8AFF },
            { 0x3C384BFF, 0x49435EFF, 0x49435EFF, 0x49435EFF },
            { 0x5E1964FF, 0x791781FF, 0x791781FF, 0x791781FF },
            { 0x9F7039FF, 0xC88536FF, 0xC88536FF, 0xC88536FF },
            { 0xCECE44FF, 0xFBFC37FF, 0xFBFC37FF, 0xFBFC37FF },
            { 0x81A73CFF, 0x99CF37FF, 0x99CF37FF, 0x99CF37FF },
            { 0x1B2D18FF, 0x1F391BFF, 0x1F391BFF, 0x1F391BFF },
            { 0x789069FF, 0x8FB079FF, 0x8FB079FF, 0x8FB079FF },
            { 0xBE951FFF, 0xEEB30BFF, 0xEEB30BFF, 0xEEB30BFF },
            { 0x41100FFF, 0x56100EFF, 0x56100EFF, 0x56100EFF },
            { 0x96AA8DFF, 0xB2CFA6FF, 0xB2CFA6FF, 0xB2CFA6FF },
            { 0xBB6322FF, 0xEF7215FF, 0xEF7215FF, 0xEF7215FF },
            { 0xC69581FF, 0xF6B093FF, 0xF6B093FF, 0xF6B093FF },
            { 0x40AF76FF, 0x3FDD8CFF, 0x3FDD8CFF, 0x3FDD8CFF },
            { 0xBD5C92FF, 0xEF66B2FF, 0xEF66B2FF, 0xEF66B2FF },
            { 0xCACABDFF, 0xF3F3E1FF, 0xF3F3E1FF, 0xF3F3E1FF },
            { 0x745975FF, 0x8F6890FF, 0x8F6890FF, 0x8F6890FF },
            { 0xC16364FF, 0xF56F71FF, 0xF56F71FF, 0xF56F71FF },
            { 0xC68CBBFF, 0xF5A2E6FF, 0xF5A2E6FF, 0xF5A2E6FF },
            { 0x768025FF, 0x8F9E1CFF, 0x8F9E1CFF, 0x8F9E1CFF },
            { 0x8663ACFF, 0xA472DBFF, 0xA472DBFF, 0xA472DBFF },
            { 0x7AA4C9FF, 0x8CC8FCFF, 0x8CC8FCFF, 0x8CC8FCFF },
            { 0x74453EFF, 0x934F46FF, 0x934F46FF, 0x934F46FF },
            { 0x00000000, 0x00000000, 0x00000000, 0x00000000 },
            { 0x12A315FF, 0x34B337FF, 0x31E635FF, 0x82F884FF },
            { 0x042367FF, 0x112C67FF, 0x0F368AFF, 0x2C467DFF },
            { 0x03847BFF, 0x24958DFF, 0x1CBDB2FF, 0x6BD4CDFF },
            { 0x200DB1FF, 0x2C1CABFF, 0x341DE9FF, 0x4E3FC4FF },
            { 0x724500FF, 0x835B14FF, 0xA56D07FF, 0xBE9956FF },
            { 0x8F9D00FF, 0xB2BE32FF, 0xD9EA22FF, 0xFFFFA4FF },
            { 0xA5056FFF, 0xAF2481FF, 0xE41DA2FF, 0xE967BEFF },
            { 0x15075EFF, 0x1B0F5BFF, 0x21107CFF, 0x2E236AFF },
            { 0x205686FF, 0x396992FF, 0x3D81BCFF, 0x75A2C8FF },
            { 0x69080FFF, 0x6E191FFF, 0x901720FF, 0x8E3F45FF },
            { 0x7E7A00FF, 0x99962AFF, 0xBCB71EFF, 0xEFEB88FF },
            { 0x94A365FF, 0xBECB94FF, 0xE4F7A9FF, 0xFFFFFFFF },
            { 0xA23BA9FF, 0xBB60C1FF, 0xEB6AF3FF, 0xFFBDFFFF },
            { 0x110613FF, 0x140A16FF, 0x1A0C1CFF, 0x1E151FFF },
            { 0x676475FF, 0x848190FF, 0x9F9BB0FF, 0xD7D4E2FF },
            { 0x739F99FF, 0x9EC4BFFF, 0xB9EFE8FF, 0xFFFFFFFF },
            { 0xAB070AFF, 0xB22225FF, 0xEA1D21FF, 0xE35D60FF },
            { 0x06852FFF, 0x249347FF, 0x1EBC51FF, 0x64CB85FF },
            { 0x1A3395FF, 0x2C4398FF, 0x3050CAFF, 0x596EBEFF },
            { 0x1098A5FF, 0x37AFBAFF, 0x32DCECFF, 0x8FFEFFFF },
            { 0x85084CFF, 0x8E215CFF, 0xB81D71FF, 0xBC578EFF },
            { 0x985D4DFF, 0xB27F71FF, 0xDE9581FF, 0xFFDBCEFF },
            { 0x1CAB92FF, 0x47C4AEFF, 0x45F7D8FF, 0xA8FFFFFF },
            { 0x433E08FF, 0x514E1EFF, 0x645F1BFF, 0x7F7C4FFF },
            { 0x689E54FF, 0x8FBE7DFF, 0xA7EA8EFF, 0xF7FFE7FF },
            { 0x7209ACFF, 0x7F24B2FF, 0xA220EBFF, 0xB45FE3FF },
            { 0x034F26FF, 0x155734FF, 0x12703DFF, 0x3D7A59FF },
            { 0x151187FF, 0x201C83FF, 0x251FB2FF, 0x3C3898FF },
            { 0x1671A4FF, 0x3685B2FF, 0x35A6E6FF, 0x7FC8F2FF },
            { 0x8D2E0EFF, 0x9A472BFF, 0xC65028FF, 0xD5886DFF },
            { 0x9F8A49FF, 0xC2B077FF, 0xEED483FF, 0xFFFFEAFF },
            { 0x196418FF, 0x2F712FFF, 0x328F31FF, 0x64A163FF },
            { 0x58A900FF, 0x7CC32EFF, 0x90F521FF, 0xDCFF94FF },
            { 0x8F7992FF, 0xB3A0B5FF, 0xD9BEDDFF, 0xFFFFFFFF },
            { 0xAC00A5FF, 0xB820B2FF, 0xEF16E6FF, 0xF669F1FF },
            { 0x1F3702FF, 0x2B4011FF, 0x33510EFF, 0x4C5F34FF },
            { 0x7464AEFF, 0x9486C7FF, 0xB29FFBFF, 0xF0E4FFFF },
            { 0x1E55B1FF, 0x3969BAFF, 0x3C80F3FF, 0x79A5F0FF },
            { 0x004D6CFF, 0x125873FF, 0x0C6F96FF, 0x3F7F99FF },
            { 0x021706FF, 0x07190BFF, 0x07210CFF, 0x132416FF },
            { 0x19615EFF, 0x32716EFF, 0x348D8AFF, 0x6DA7A5FF },
            { 0x2F2A40FF, 0x3C384BFF, 0x49435EFF, 0x625E70FF },
            { 0x56075CFF, 0x5E1964FF, 0x791781FF, 0x824287FF },
            { 0x8A5415FF, 0x9F7039FF, 0xC88536FF, 0xEABE8BFF },
            { 0xA7A809FF, 0xCECE44FF, 0xFBFC37FF, 0xFFFFC3FF },
            { 0x618C12FF, 0x81A73CFF, 0x99CF37FF, 0xD9FC99FF },
            { 0x12270FFF, 0x1B2D18FF, 0x1F391BFF, 0x324330FF },
            { 0x5B7549FF, 0x789069FF, 0x8FB079FF, 0xCBE1BDFF },
            { 0xA47400FF, 0xBE951FFF, 0xEEB30BFF, 0xFFF385FF },
            { 0x3E0604FF, 0x41100FFF, 0x56100EFF, 0x552726FF },
            { 0x728968FF, 0x96AA8DFF, 0xB2CFA6FF, 0xFAFFF2FF },
            { 0xA94500FF, 0xBB6322FF, 0xEF7215FF, 0xFFB376FF },
            { 0xA76F58FF, 0xC69581FF, 0xF6B093FF, 0xFFFEEBFF },
            { 0x1B9A59FF, 0x40AF76FF, 0x3FDD8CFF, 0x93FAC5FF },
            { 0xA73976FF, 0xBD5C92FF, 0xEF66B2FF, 0xFFB5E6FF },
            { 0x9E9E90FF, 0xCACABDFF, 0xF3F3E1FF, 0xFFFFFFFF },
            { 0x604061FF, 0x745975FF, 0x8F6890FF, 0xB49AB4FF },
            { 0xAC4042FF, 0xC16364FF, 0xF56F71FF, 0xFFB9BBFF },
            { 0xA6639AFF, 0xC68CBBFF, 0xF5A2E6FF, 0xFFF7FFFF },
            { 0x5E6A01FF, 0x768025FF, 0x8F9E1CFF, 0xBCC671FF },
            { 0x6D4599FF, 0x8663ACFF, 0xA472DBFF, 0xD1B0F5FF },
            { 0x5484AEFF, 0x7AA4C9FF, 0x8CC8FCFF, 0xDCFFFFFF },
            { 0x662F28FF, 0x74453EFF, 0x934F46FF, 0xA87C76FF },
            { 0x00000000, 0x00000000, 0x00000000, 0x00000000 },
            { 0x31E635FF, 0x34B337FF, 0x31E635FF, 0x82F884FF },
            { 0x0F368AFF, 0x112C67FF, 0x0F368AFF, 0x2C467DFF },
            { 0x1CBDB2FF, 0x24958DFF, 0x1CBDB2FF, 0x6BD4CDFF },
            { 0x341DE9FF, 0x2C1CABFF, 0x341DE9FF, 0x4E3FC4FF },
            { 0xA56D07FF, 0x835B14FF, 0xA56D07FF, 0xBE9956FF },
            { 0xD9EA22FF, 0xB2BE32FF, 0xD9EA22FF, 0xFFFFA4FF },
            { 0xE41DA2FF, 0xAF2481FF, 0xE41DA2FF, 0xE967BEFF },
            { 0x21107CFF, 0x1B0F5BFF, 0x21107CFF, 0x2E236AFF },
            { 0x3D81BCFF, 0x396992FF, 0x3D81BCFF, 0x75A2C8FF },
            { 0x901720FF, 0x6E191FFF, 0x901720FF, 0x8E3F45FF },
            { 0xBCB71EFF, 0x99962AFF, 0xBCB71EFF, 0xEFEB88FF },
            { 0xE4F7A9FF, 0xBECB94FF, 0xE4F7A9FF, 0xFFFFFFFF },
            { 0xEB6AF3FF, 0xBB60C1FF, 0xEB6AF3FF, 0xFFBDFFFF },
            { 0x1A0C1CFF, 0x140A16FF, 0x1A0C1CFF, 0x1E151FFF },
            { 0x9F9BB0FF, 0x848190FF, 0x9F9BB0FF, 0xD7D4E2FF },
            { 0xB9EFE8FF, 0x9EC4BFFF, 0xB9EFE8FF, 0xFFFFFFFF },
            { 0xEA1D21FF, 0xB22225FF, 0xEA1D21FF, 0xE35D60FF },
            { 0x1EBC51FF, 0x249347FF, 0x1EBC51FF, 0x64CB85FF },
            { 0x3050CAFF, 0x2C4398FF, 0x3050CAFF, 0x596EBEFF },
            { 0x32DCECFF, 0x37AFBAFF, 0x32DCECFF, 0x8FFEFFFF },
            { 0xB81D71FF, 0x8E215CFF, 0xB81D71FF, 0xBC578EFF },
            { 0xDE9581FF, 0xB27F71FF, 0xDE9581FF, 0xFFDBCEFF },
            { 0x45F7D8FF, 0x47C4AEFF, 0x45F7D8FF, 0xA8FFFFFF },
            { 0x645F1BFF, 0x514E1EFF, 0x645F1BFF, 0x7F7C4FFF },
            { 0xA7EA8EFF, 0x8FBE7DFF, 0xA7EA8EFF, 0xF7FFE7FF },
            { 0xA220EBFF, 0x7F24B2FF, 0xA220EBFF, 0xB45FE3FF },
            { 0x12703DFF, 0x155734FF, 0x12703DFF, 0x3D7A59FF },
            { 0x251FB2FF, 0x201C83FF, 0x251FB2FF, 0x3C3898FF },
            { 0x35A6E6FF, 0x3685B2FF, 0x35A6E6FF, 0x7FC8F2FF },
            { 0xC65028FF, 0x9A472BFF, 0xC65028FF, 0xD5886DFF },
            { 0xEED483FF, 0xC2B077FF, 0xEED483FF, 0xFFFFEAFF },
            { 0x328F31FF, 0x2F712FFF, 0x328F31FF, 0x64A163FF },
            { 0x90F521FF, 0x7CC32EFF, 0x90F521FF, 0xDCFF94FF },
            { 0xD9BEDDFF, 0xB3A0B5FF, 0xD9BEDDFF, 0xFFFFFFFF },
            { 0xEF16E6FF, 0xB820B2FF, 0xEF16E6FF, 0xF669F1FF },
            { 0x33510EFF, 0x2B4011FF, 0x33510EFF, 0x4C5F34FF },
            { 0xB29FFBFF, 0x9486C7FF, 0xB29FFBFF, 0xF0E4FFFF },
            { 0x3C80F3FF, 0x3969BAFF, 0x3C80F3FF, 0x79A5F0FF },
            { 0x0C6F96FF, 0x125873FF, 0x0C6F96FF, 0x3F7F99FF },
            { 0x07210CFF, 0x07190BFF, 0x07210CFF, 0x132416FF },
            { 0x348D8AFF, 0x32716EFF, 0x348D8AFF, 0x6DA7A5FF },
            { 0x49435EFF, 0x3C384BFF, 0x49435EFF, 0x625E70FF },
            { 0x791781FF, 0x5E1964FF, 0x791781FF, 0x824287FF },
            { 0xC88536FF, 0x9F7039FF, 0xC88536FF, 0xEABE8BFF },
            { 0xFBFC37FF, 0xCECE44FF, 0xFBFC37FF, 0xFFFFC3FF },
            { 0x99CF37FF, 0x81A73CFF, 0x99CF37FF, 0xD9FC99FF },
            { 0x1F391BFF, 0x1B2D18FF, 0x1F391BFF, 0x324330FF },
            { 0x8FB079FF, 0x789069FF, 0x8FB079FF, 0xCBE1BDFF },
            { 0xEEB30BFF, 0xBE951FFF, 0xEEB30BFF, 0xFFF385FF },
            { 0x56100EFF, 0x41100FFF, 0x56100EFF, 0x552726FF },
            { 0xB2CFA6FF, 0x96AA8DFF, 0xB2CFA6FF, 0xFAFFF2FF },
            { 0xEF7215FF, 0xBB6322FF, 0xEF7215FF, 0xFFB376FF },
            { 0xF6B093FF, 0xC69581FF, 0xF6B093FF, 0xFFFEEBFF },
            { 0x3FDD8CFF, 0x40AF76FF, 0x3FDD8CFF, 0x93FAC5FF },
            { 0xEF66B2FF, 0xBD5C92FF, 0xEF66B2FF, 0xFFB5E6FF },
            { 0xF3F3E1FF, 0xCACABDFF, 0xF3F3E1FF, 0xFFFFFFFF },
            { 0x8F6890FF, 0x745975FF, 0x8F6890FF, 0xB49AB4FF },
            { 0xF56F71FF, 0xC16364FF, 0xF56F71FF, 0xFFB9BBFF },
            { 0xF5A2E6FF, 0xC68CBBFF, 0xF5A2E6FF, 0xFFF7FFFF },
            { 0x8F9E1CFF, 0x768025FF, 0x8F9E1CFF, 0xBCC671FF },
            { 0xA472DBFF, 0x8663ACFF, 0xA472DBFF, 0xD1B0F5FF },
            { 0x8CC8FCFF, 0x7AA4C9FF, 0x8CC8FCFF, 0xDCFFFFFF },
            { 0x934F46FF, 0x74453EFF, 0x934F46FF, 0xA87C76FF },
    };

    static {
        for (int i = 0; i < 64; i++) {
            System.arraycopy(FLESURRECT_BONUS_RAMP_VALUES[i], 0, FlesurrectBonusPalette, i << 2, 4);
            System.arraycopy(LAWN_BONUS_RAMP_VALUES[i], 0, LawnBonusPalette, i << 2, 4);
        }
    }

    public static final Colorizer AzurestarColorizer = new Colorizer(new PaletteReducer(Coloring.AZURESTAR33)) {
        private final byte[] primary = {
                reducer.reduceIndex(0xFF0000FF),reducer.reduceIndex(0xFFFF00FF),
                reducer.reduceIndex(0x00FF00FF),reducer.reduceIndex(0x00FFFFFF),
                reducer.reduceIndex(0x0000FFFF),reducer.reduceIndex(0xFF00FFFF),
        }, grays = {
                reducer.reduceIndex(0x000000FF),reducer.reduceIndex(0x444444FF),
                reducer.reduceIndex(0x666666FF),reducer.reduceIndex(0x999999FF),
                reducer.reduceIndex(0xBBBBBBFF),reducer.reduceIndex(0xFFFFFFFF),
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return Coloring.AZURESTAR_RAMPS[(voxel & 0x3F) % 33][3];
        }

        @Override
        public byte darken(byte voxel) {
            // the second half of voxels (with bit 0x40 set) don't shade visually, but Colorizer uses this method to
            // denote a structural change to the voxel's makeup, so this uses the first 64 voxel colors to shade both
            // halves, then marks voxels from the second half back to being an unshaded voxel as the last step.
            return Coloring.AZURESTAR_RAMPS[(voxel & 0x3F) % 33][1];
        }

        @Override
        public int dimmer(int brightness, byte voxel) {
            if(brightness < 0) return Coloring.AZURESTAR33[1];
            if(brightness > 3) return Coloring.AZURESTAR33[13];
            return Coloring.AZURESTAR33[Coloring.AZURESTAR_RAMPS[(voxel & 0x3F) % 33][brightness] & 0xFF];
        }

        @Override
        public int getShadeBit() {
            return 0;
        }
        @Override
        public int getWaveBit() {
            return 0;
        }
    };

    public static final Colorizer SplayColorizer = new Colorizer(new PaletteReducer(Coloring.SPLAY32)) {
        private final byte[] primary = {
                reducer.reduceIndex(0xFF0000FF),reducer.reduceIndex(0xFFFF00FF),
                reducer.reduceIndex(0x00FF00FF),reducer.reduceIndex(0x00FFFFFF),
                reducer.reduceIndex(0x0000FFFF),reducer.reduceIndex(0xFF00FFFF),
        }, grays = {
                reducer.reduceIndex(0x000000FF),reducer.reduceIndex(0x444444FF),
                reducer.reduceIndex(0x666666FF),reducer.reduceIndex(0x999999FF),
                reducer.reduceIndex(0xBBBBBBFF),reducer.reduceIndex(0xFFFFFFFF),
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return Coloring.SPLAY_RAMPS[voxel & 0x1F][3];
        }

        @Override
        public byte darken(byte voxel) {
            // the second half of voxels (with bit 0x40 set) don't shade visually, but Colorizer uses this method to
            // denote a structural change to the voxel's makeup, so this uses the first 64 voxel colors to shade both
            // halves, then marks voxels from the second half back to being an unshaded voxel as the last step.
            return Coloring.SPLAY_RAMPS[voxel & 0x1F][1];
        }

        @Override
        public int dimmer(int brightness, byte voxel) {
            if(brightness < 0) return Coloring.SPLAY32[1];
            if(brightness > 3) return Coloring.SPLAY32[13];
            return Coloring.SPLAY32[Coloring.SPLAY_RAMPS[voxel & 0x1F][brightness] & 0x1F];
        }

        @Override
        public int getShadeBit() {
            return 64;
        }
        @Override
        public int getWaveBit() {
            return 128;
        }
    };

    public static final Colorizer ZigguratColorizer = new Colorizer(new PaletteReducer(Coloring.ZIGGURAT64)) {
        private final byte[] primary = {
                27, 32, 34, 39, 49, 55,
        }, grays = {
                1, 11, 19, 21, 22, 23
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return Coloring.ZIGGURAT_RAMPS[voxel & 0x3F][3];
        }

        @Override
        public byte darken(byte voxel) {
            // the second half of voxels (with bit 0x40 set) don't shade visually, but Colorizer uses this method to
            // denote a structural change to the voxel's makeup, so this uses the first 64 voxel colors to shade both
            // halves, then marks voxels from the second half back to being an unshaded voxel as the last step.
            return Coloring.ZIGGURAT_RAMPS[voxel & 0x3F][1];
        }

        @Override
        public int dimmer(int brightness, byte voxel) {
            if(brightness < 0) return Coloring.ZIGGURAT64[1];
            if(brightness > 3) return Coloring.ZIGGURAT64[23];
            return Coloring.ZIGGURAT64[Coloring.ZIGGURAT_RAMPS[voxel & 0x3F][brightness] & 0xFF];
        }

        @Override
        public int getShadeBit() {
            return 0;
        }
        @Override
        public int getWaveBit() {
            return 0;
        }
    };

    
    
    
    public static final Colorizer ManosColorizer = new Colorizer(new PaletteReducer()) {
        private final byte[] primary = {
                11, 17, 23, 31, 45, 53
        }, grays = {
                1, 2, 4, 5, 6, 7, 10
        };

        @Override
        public byte[] mainColors() {
            return primary;
        }

        /**
         * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
         */
        @Override
        public byte[] grayscale() {
            return grays;
        }

        @Override
        public byte brighten(byte voxel) {
            return Coloring.MANOS_RAMPS[voxel & 0x3F][3];
        }

        @Override
        public byte darken(byte voxel) {
            // the second half of voxels (with bit 0x40 set) don't shade visually, but Colorizer uses this method to
            // denote a structural change to the voxel's makeup, so this uses the first 64 voxel colors to shade both
            // halves, then marks voxels from the second half back to being an unshaded voxel as the last step.
            return Coloring.MANOS_RAMPS[voxel & 0x3F][1];
        }

        @Override
        public int dimmer(int brightness, byte voxel) {
            if(brightness < 0) return Coloring.MANOS64[1];
            if(brightness > 3) return Coloring.MANOS64[10];
            return Coloring.MANOS64[Coloring.MANOS_RAMPS[voxel & 0x3F][brightness] & 0xFF];
        }

        @Override
        public int getShadeBit() {
            return 0;
        }
        @Override
        public int getWaveBit() {
            return 0;
        }
    };








    public static Colorizer arbitraryColorizer(final int[] palette) {
        final int COUNT = palette.length;
        PaletteReducer reducer = new PaletteReducer(palette);

        final byte[] primary = {
                reducer.reduceIndex(0xFF0000FF), reducer.reduceIndex(0xFFFF00FF), reducer.reduceIndex(0x00FF00FF),
                reducer.reduceIndex(0x00FFFFFF), reducer.reduceIndex(0x0000FFFF), reducer.reduceIndex(0xFF00FFFF)
        }, grays = {
                reducer.reduceIndex(0x000000FF), reducer.reduceIndex(0x444444FF), reducer.reduceIndex(0x888888FF),
                reducer.reduceIndex(0xCCCCCCFF), reducer.reduceIndex(0xFFFFFFFF)
        };
        final int THRESHOLD = 64;//0.011; // threshold controls the "stark-ness" of color changes; must not be negative.
        final byte[] paletteMapping = new byte[1 << 16];
        final int[] reverse = new int[COUNT];
        final byte[][] ramps = new byte[COUNT][4];
        final int[] lumas = new int[COUNT], cos = new int[COUNT], cgs = new int[COUNT];
        final int yLim = 63, coLim = 31, cgLim = 31, shift1 = 6, shift2 = 11;
        int color, r, g, b, co, cg, t;
        for (int i = 1; i < COUNT; i++) {
            color = palette[i];
            if((color & 0x80) == 0)
            {
                lumas[i] = -0x70000000; // very very negative, blocks transparent colors from mixing into opaque ones
                continue;
            }
            r = (color >>> 24);
            g = (color >>> 16 & 0xFF);
            b = (color >>> 8 & 0xFF);
            co = r - b;
            t = b + (co >> 1);
            cg = g - t;
            paletteMapping[
                    reverse[i] = 
                              (lumas[i] = luma(r, g, b) >>> 11)
                            | (cos[i] = co + 255 >>> 4) << shift1
                            | (cgs[i] = cg + 255 >>> 4) << shift2] = (byte) i;
        }

        for (int icg = 0; icg <= cgLim; icg++) {
            for (int ico = 0; ico <= coLim; ico++) {
                for (int iy = 0; iy <= yLim; iy++) {
                    final int c2 = icg << shift2 | ico << shift1 | iy;
                    if (paletteMapping[c2] == 0) {
                        int dist = 0x7FFFFFFF;
                        for (int i = 1; i < COUNT; i++) {
                            if (Math.abs(lumas[i] - iy) < 28 && dist > (dist = Math.min(dist, difference(lumas[i], cos[i], cgs[i], iy, ico, icg))))
                                paletteMapping[c2] = (byte) i;
                        }
                    }
                }
            }
        }

        float adj, cof, cgf;
        int idx2;
//        System.out.println("{\n{ 0, 0, 0, 0 },");
        for (int i = 1; i < COUNT; i++) {
            int rev = reverse[i], y = rev & yLim, match = i;
            cof = ((co = cos[i]) - 16) * 0x1.111112p-5f;
            cgf = ((cg = cgs[i]) - 16) * 0x1.111112p-5f;
            ramps[i][2] = (byte)i;
            ramps[i][3] = grays[4];//15;  //0xFFFFFFFF, white
            ramps[i][1] = grays[0];//0x010101FF, black
            ramps[i][0] = grays[0];//0x010101FF, black
            for (int yy = y + 2, rr = rev + 2; yy <= yLim; yy++, rr++) {
                if ((idx2 = paletteMapping[rr] & 255) != i && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                    ramps[i][3] = paletteMapping[rr];
                    break;
                }
                adj = 1f + ((yLim + 1 >>> 1) - yy) * 0x1p-10f;
                cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                cgf = MathUtils.clamp(cgf * adj + 0x1.8p-10f, -0.5f, 0.5f);

                rr = yy
                        | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                        | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;
            }
            cof = ((co = cos[i]) - 16) * 0x0.Bp-5f;
            cgf = ((cg = cgs[i]) - 16) * 0x0.Bp-5f;
            for (int yy = y - 2, rr = rev - 2; yy > 0; rr--) {
                if ((idx2 = paletteMapping[rr] & 255) != i && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                    ramps[i][1] = paletteMapping[rr];
                    rev = rr;
                    y = yy;
                    match = paletteMapping[rr] & 255;
                    break;
                }
                adj = 1f + (yy - (yLim + 1 >>> 1)) * 0x1p-10f;
                cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                cgf = MathUtils.clamp(cgf * adj - 0x1.8p-10f, -0.5f, 0.5f);

//                cof = (cof - 0.5f) * 0.984375f + 0.5f;
//                cgf = (cgf + 0.5f) * 0.984375f - 0.5f;
                rr = yy
                        | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                        | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;

//                cof = MathUtils.clamp(cof * 0.9375f, -0.5f, 0.5f);
//                cgf = MathUtils.clamp(cgf * 0.9375f, -0.5f, 0.5f);
//                rr = yy
//                        | (int) ((cof + 0.5f) * 63) << 7
//                        | (int) ((cgf + 0.5f) * 63) << 13;
                if (--yy == 0) {
                    match = -1;
                }
            }
            if (match >= 0) {
                cof = ((co = cos[match]) - 16) * 0x1.111112p-5f;
                cgf = ((cg = cgs[match]) - 16) * 0x1.111112p-5f;
                for (int yy = y - 3, rr = rev - 3; yy > 0; yy--, rr--) {
                    if ((idx2 = paletteMapping[rr] & 255) != match && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                        ramps[i][0] = paletteMapping[rr];
                        break;
                    }
                    adj = 1f + (yy - (yLim + 1 >>> 1)) * 0x1p-10f;
                    cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                    cgf = MathUtils.clamp(cgf * adj - 0x1.8p-10f, -0.5f, 0.5f);

//                    cof = (cof - 0.5f) * 0.96875f + 0.5f;
//                    cgf = (cgf + 0.5f) * 0.96875f - 0.5f;
                    rr = yy
                            | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                            | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;

//                    cof = MathUtils.clamp(cof * 0.9375f, -0.5f, 0.5f);
//                    cgf = MathUtils.clamp(cgf * 0.9375f, -0.5f, 0.5f);
//                    rr = yy
//                            | (int) ((cof + 0.5f) * 63) << 7
//                            | (int) ((cgf + 0.5f) * 63) << 13;
                }
            }
//            System.out.println("{ " + ramps[i][0] + ", " + ramps[i][1] + ", " + ramps[i][2] + ", " + ramps[i][3] + " },");
        }
//        System.out.println("};");


        return new Colorizer(reducer) {

            @Override
            public byte[] mainColors() {
                return primary;
            }

            /**
             * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
             */
            @Override
            public byte[] grayscale() {
                return grays;
            }

            @Override
            public byte brighten(byte voxel) {
                return ramps[(voxel & 0xFF) % COUNT][3];
            }

            @Override
            public byte darken(byte voxel) {
                return ramps[(voxel & 0xFF) % COUNT][1];
            }

            @Override
            public int dimmer(int brightness, byte voxel) {
                return palette[ramps[(voxel & 0xFF) % COUNT][
                        brightness <= 0
                                ? 0
                                : brightness >= 3
                                ? 3
                                : brightness
                        ] & 0xFF];
            }

            @Override
            public int getShadeBit() {
                return 0;
            }

            @Override
            public int getWaveBit() {
                return 0;
            }
        };
    }
    public static Colorizer arbitraryBonusColorizer(final int[] palette) {
        final int COUNT = palette.length;
        PaletteReducer reducer = new PaletteReducer(palette);

        final byte[] primary = {
                reducer.reduceIndex(0xFF0000FF), reducer.reduceIndex(0xFFFF00FF), reducer.reduceIndex(0x00FF00FF),
                reducer.reduceIndex(0x00FFFFFF), reducer.reduceIndex(0x0000FFFF), reducer.reduceIndex(0xFF00FFFF)
        }, grays = {
                reducer.reduceIndex(0x000000FF), reducer.reduceIndex(0x444444FF), reducer.reduceIndex(0x888888FF),
                reducer.reduceIndex(0xCCCCCCFF), reducer.reduceIndex(0xFFFFFFFF)
        };
        final int THRESHOLD = 64;//0.011; // threshold controls the "stark-ness" of color changes; must not be negative.
        final byte[] paletteMapping = new byte[1 << 16];
        final int[] reverse = new int[COUNT];
        final byte[][] ramps = new byte[COUNT][4];
        final int[][] values = new int[COUNT][4];
        
        final int[] lumas = new int[COUNT], cos = new int[COUNT], cgs = new int[COUNT];
        final int yLim = 63, coLim = 31, cgLim = 31, shift1 = 6, shift2 = 11;
        int color, r, g, b, co, cg, t;
        for (int i = 1; i < COUNT; i++) {
            color = palette[i];
            if((color & 0x80) == 0)
            {
                lumas[i] = -0x70000000; // very very negative, blocks transparent colors from mixing into opaque ones
                continue;
            }
            r = (color >>> 24);
            g = (color >>> 16 & 0xFF);
            b = (color >>> 8 & 0xFF);
            co = r - b;
            t = b + (co >> 1);
            cg = g - t;
            paletteMapping[
                    reverse[i] = 
                              (lumas[i] = luma(r, g, b) >>> 11)
                            | (cos[i] = co + 255 >>> 4) << shift1
                            | (cgs[i] = cg + 255 >>> 4) << shift2] = (byte) i;
        }

        for (int icg = 0; icg <= cgLim; icg++) {
            for (int ico = 0; ico <= coLim; ico++) {
                for (int iy = 0; iy <= yLim; iy++) {
                    final int c2 = icg << shift2 | ico << shift1 | iy;
                    if (paletteMapping[c2] == 0) {
                        int dist = 0x7FFFFFFF;
                        for (int i = 1; i < COUNT; i++) {
                            if (Math.abs(lumas[i] - iy) < 28 && dist > (dist = Math.min(dist, difference(lumas[i], cos[i], cgs[i], iy, ico, icg))))
                                paletteMapping[c2] = (byte) i;
                        }
                    }
                }
            }
        }

        float adj, cof, cgf;
        int idx2;
        for (int i = 1; i < COUNT; i++) {
            int rev = reverse[i], y = rev & yLim, match = i,
                    yBright = y * 5, yDim = y * 3, yDark = y << 1, chromO, chromG;

            cof = ((co = cos[i]) - 16) * 0x1.111112p-5f;
            cgf = ((cg = cgs[i]) - 16) * 0x1.111112p-5f;

            //values[i][0] = values[i][1] = values[i][3] = 
            values[i][2] = palette[i];             

            chromO = (co * 395 + 31 >> 5) - 192;
            chromG = (cg * 395 + 31 >> 5) - 192;
            t = yDim - (chromG >> 1);
            g = chromG + t;
            b = t - (chromO >> 1);
            r = b + chromO;

            values[i][1] =
                    MathUtils.clamp(r, 0, 255) << 24 |
                            MathUtils.clamp(g, 0, 255) << 16 |
                            MathUtils.clamp(b, 0, 255) << 8 | 0xFF;
            chromO = (co * 333 + 31 >> 5) - 162;
            chromG = (cg * 333 + 31 >> 5) - 162;//(cg * (256 - yBright) * 395 + 4095 >> 12) - 192;
            t = yBright - (chromG >> 1);
            g = chromG + t;
            b = t - (chromO >> 1);
            r = b + chromO;
            values[i][3] =
                    MathUtils.clamp(r, 0, 255) << 24 |
                            MathUtils.clamp(g, 0, 255) << 16 |
                            MathUtils.clamp(b, 0, 255) << 8 | 0xFF;
            chromO = (co * 215 >> 4) - 208;
            chromG = (cg * 215 >> 4) - 208;//(cg * (256 - yDark) * 215 >> 11) - 208;
            t = yDark - (chromG >> 1);
            g = chromG + t;
            b = t - (chromO >> 1);
            r = b + chromO;
            values[i][0] =
                    MathUtils.clamp(r, 0, 255) << 24 |
                            MathUtils.clamp(g, 0, 255) << 16 |
                            MathUtils.clamp(b, 0, 255) << 8 | 0xFF;

            ramps[i][2] = (byte)i;
            ramps[i][3] = grays[4];//15;  //0xFFFFFFFF, white
            ramps[i][1] = grays[0];//0x010101FF, black
            ramps[i][0] = grays[0];//0x010101FF, black
            for (int yy = y + 2, rr = rev + 2; yy <= yLim; yy++, rr++) {
                if ((idx2 = paletteMapping[rr] & 255) != i && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                    ramps[i][3] = paletteMapping[rr];
                    break;
                }
                adj = 1f + ((yLim + 1 >>> 1) - yy) * 0x1p-10f;
                cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                cgf = MathUtils.clamp(cgf * adj + 0x1.8p-10f, -0.5f, 0.5f);

                rr = yy
                        | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                        | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;
            }
            cof = ((co = cos[i]) - 16) * 0x0.Bp-5f;
            cgf = ((cg = cgs[i]) - 16) * 0x0.Bp-5f;
            for (int yy = y - 2, rr = rev - 2; yy > 0; rr--) {
                if ((idx2 = paletteMapping[rr] & 255) != i && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                    ramps[i][1] = paletteMapping[rr];
                    rev = rr;
                    y = yy;
                    match = paletteMapping[rr] & 255;
                    break;
                }
                adj = 1f + (yy - (yLim + 1 >>> 1)) * 0x1p-10f;
                cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                cgf = MathUtils.clamp(cgf * adj - 0x1.8p-10f, -0.5f, 0.5f);

                rr = yy
                        | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                        | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;

                if (--yy == 0) {
                    match = -1;
                }
            }
            if (match >= 0) {
                cof = ((co = cos[match]) - 16) * 0x1.111112p-5f;
                cgf = ((cg = cgs[match]) - 16) * 0x1.111112p-5f;
                for (int yy = y - 3, rr = rev - 3; yy > 0; yy--, rr--) {
                    if ((idx2 = paletteMapping[rr] & 255) != match && difference(lumas[idx2], cos[idx2], cgs[idx2], y, co, cg) > THRESHOLD) {
                        ramps[i][0] = paletteMapping[rr];
                        break;
                    }
                    adj = 1f + (yy - (yLim + 1 >>> 1)) * 0x1p-10f;
                    cof = MathUtils.clamp(cof * adj, -0.5f, 0.5f);
                    cgf = MathUtils.clamp(cgf * adj - 0x1.8p-10f, -0.5f, 0.5f);
                    
                    rr = yy
                            | (co = (int) ((cof + 0.5f) * coLim)) << shift1
                            | (cg = (int) ((cgf + 0.5f) * cgLim)) << shift2;
                }
            }
        }


        return new Colorizer(reducer) {

            @Override
            public byte[] mainColors() {
                return primary;
            }

            /**
             * @return An array of grayscale or close-to-grayscale color indices, with the darkest first and lightest last.
             */
            @Override
            public byte[] grayscale() {
                return grays;
            }

            @Override
            public byte brighten(byte voxel) {
                return ramps[(voxel & 0xFF) % COUNT][3];
            }

            @Override
            public byte darken(byte voxel) {
                return ramps[(voxel & 0xFF) % COUNT][1];
            }

            @Override
            public int dimmer(int brightness, byte voxel) {
                return values[voxel & 0xFF][
                        brightness <= 0
                                ? 0
                                : Math.min(brightness, 3)
                        ];
            }

            @Override
            public int getShadeBit() {
                return 0;
            }

            @Override
            public int getWaveBit() {
                return 0;
            }
        };
    }
}
