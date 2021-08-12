package isonomicon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.Dithered;
import com.github.tommyettinger.anim8.PaletteReducer;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;
import isonomicon.visual.SmudgeRenderer;
import squidpony.squidmath.FastNoise;
import squidpony.squidmath.FlawedPointHash;
import squidpony.squidmath.IPointHash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NoiseRenderer extends ApplicationAdapter {
//    public static final int QUALITY = 48;
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SmudgeRenderer renderer;
    private FastNoise noise;
    private byte[][][] tempVoxels = new byte[64][64][64];
    private byte[][][] voxels = new byte[128][128][128];
    private String name;
    private PixmapIO.PNG png;
    private AnimatedGif gif;
//    private PNG8 png8;
//    private AnimatedPNG apng;
    public NoiseRenderer(String[] args){
    }
    @Override
    public void create() {
        System.out.println("Setting up...");
        noise = new FastNoise(1234567, 0x1p-3f, FastNoise.CUBIC_FRACTAL, 1);
        noise.setFractalType(FastNoise.RIDGED_MULTI);
        noise.setPointHash(new CubeHash(123, 8));
        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
        png = new PixmapIO.PNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
//        png8 = new PNG8();
        gif = new AnimatedGif();
//        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
//        png8.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
        gif.palette = new PaletteReducer();
        gif.palette.setDitherStrength(0.625f);
//        png8.palette = gif.palette;
        Gdx.files.local("out/vox").mkdirs();
        System.out.println("Loading...");
        System.out.println("Produced 64x64x64 noise.");
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
        Pixmap pixmap;
        Array<Pixmap> pm = new Array<>(64);
        for (int i = 0; i < 64; i++) {
            for (int f = 0; f < 1; f++) {
                load(i);
                pixmap = renderer.drawSplats(voxels, i * 0x1p-6f, f, VoxIO.lastMaterials);
                Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                p.drawPixmap(pixmap, 0, 0);
                pm.add(p);
                try {
                    png.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
            }
//            pm.insertRange(pm.size - 4, 4);
        }
        System.out.println("Mostly done, gif stuff in progress...");
        gif.palette.analyze(pm);
        gif.write(Gdx.files.local("out/" + name + '/' + name + ".gif"), pm, 8);
//                gif.palette.exact(Coloring.HALTONITE240, PRELOAD);
//                gif.write(Gdx.files.local("out/" + name + '/' + name + "-256-color.gif"), pm, 1);
//                apng.write(Gdx.files.local("out/" + name + '/' + name + ".png"), pm, 12);
        for (Pixmap pix : pm) {
            if (!pix.isDisposed())
                pix.dispose();
        }
        System.out.println("Finished in " + TimeUtils.timeSinceMillis(startTime) * 0.001 + " seconds.");
        Gdx.app.exit();
    }

    @Override
    public void render() {
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Writing Test");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final NoiseRenderer app = new NoiseRenderer(arg);
        new Lwjgl3Application(app, config);
    }

    public void load(int frame) {
        int sum = 0;
        for (int x = 0; x < tempVoxels.length; x++) {
            for (int y = 0; y < tempVoxels[0].length; y++) {
                for (int z = 0; z < tempVoxels[0][0].length; z++) {
//                    sum += (tempVoxels[x][y][z] = (byte) ((Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame)) >> 31) & 81)); // gray-green
//                    sum += (tempVoxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.875f) >> 31) & 81)); // gray-green
                    sum += (tempVoxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.9f) >> 31) & 44)); // gray
                }
            }
        }
        System.out.println(sum);
        Tools3D.simpleScale(tempVoxels, voxels);
        Tools3D.soakInPlace(voxels);
        this.name = "Noise";
        if(renderer == null) {
            for (int i = 1; i < 256; i++) {
                VoxIO.lastMaterials.put(i, new VoxMaterial());
            }
            renderer = new SmudgeRenderer(voxels.length);
            renderer.palette(VoxIO.lastPalette);
            renderer.saturation(0f);
        }
    }
    public class CubeHash extends IPointHash.LongImpl implements FlawedPointHash {
        private int size = 6;
        private long mask = (1L << size) - 1L;
        public CubeHash() {
        }

        public CubeHash(long state) {
            super(state);
        }

        public CubeHash(long state, int size) {
            super(state);
            setSize(size);
        }

        public long getState() {
            return state;
        }

        public int getSize() {
            return 1 << size;
        }

        public void setSize(int size) {
            this.size = 32 - Integer.numberOfLeadingZeros(Math.max(1, size));
            mask = (1L << this.size) - 1L;
        }

        public long hashLongs(long x, long y, long s) {
            x &= mask;
            y &= mask;
            x *= x * 0xC13FA9A902A6328FL;
            y *= y * 0x91E10DA5C79E7B1DL;
            x &= mask;
            y &= mask;
            long t;
            if (x < y) {
                t = x;
                x = y;
                y = t;
            }
            x = (x + 0x9E3779B97F4A7C15L ^ x) * (s + y);
            y = (y + 0x9E3779B97F4A7C15L ^ y) * (x + s);
            s = (s + 0x9E3779B97F4A7C15L ^ s) * (y + x);
            return s;
        }

        public long hashLongs(long x, long y, long z, long s) {
            x &= mask;
            y &= mask;
            z &= mask;
            x *= x * 0xD1B54A32D192ED03L;
            y *= y * 0xABC98388FB8FAC03L;
            z *= z * 0x8CB92BA72F3D8DD7L;
            x &= mask;
            y &= mask;
            z &= mask;
            long t;
            if (x < y) {
                t = x;
                x = y;
                y = t;
            }
            if(x < z){
                t = x;
                x = z;
                z = t;
            }
            if(y < z){
                t = y;
                y = z;
                z = t;
            }
            x = (x + 0x9E3779B97F4A7C15L ^ x) * (s + z);
            y = (y + 0x9E3779B97F4A7C15L ^ y) * (x + s);
            z = (z + 0x9E3779B97F4A7C15L ^ z) * (y + x);
            s = (s + 0x9E3779B97F4A7C15L ^ s) * (z + y);
            return s;
        }

        public long hashLongs(long x, long y, long z, long w, long s) {
            x &= mask;
            y &= mask;
            z &= mask;
            w &= mask;
            x *= x * 0xDB4F0B9175AE2165L;
            y *= y * 0xBBE0563303A4615FL;
            z *= z * 0xA0F2EC75A1FE1575L;
            w *= w * 0x89E182857D9ED689L;
            x &= mask;
            y &= mask;
            z &= mask;
            w &= mask;
            long t;
            if (x < y) {
                t = x;
                x = y;
                y = t;
            }
            if(x < z){
                t = x;
                x = z;
                z = t;
            }
            if(x < w){
                t = x;
                x = w;
                w = t;
            }
            if(y < z){
                t = y;
                y = z;
                z = t;
            }
            if(y < w){
                t = y;
                y = w;
                w = t;
            }
            if(z < w){
                t = z;
                z = w;
                w = t;
            }
            x = (x + 0x9E3779B97F4A7C15L ^ x) * (s + w);
            y = (y + 0x9E3779B97F4A7C15L ^ y) * (x + s);
            z = (z + 0x9E3779B97F4A7C15L ^ z) * (y + x);
            w = (w + 0x9E3779B97F4A7C15L ^ w) * (z + y);
            s = (s + 0x9E3779B97F4A7C15L ^ s) * (w + z);
            return s;
        }

        public long hashLongs(long x, long y, long z, long w, long u, long s) {
            return hashLongs(x, hashLongs(y, hashLongs(z, hashLongs(w, u, s), s), s), s);
        }

        public long hashLongs(long x, long y, long z, long w, long u, long v, long s) {
            return hashLongs(x, hashLongs(y, hashLongs(z, hashLongs(w, hashLongs(u, v, s), s), s), s), s);
        }

        @Override
        public int hashWithState(int x, int y, int state) {
            return (int)(hashLongs(x, y, state) >>> 32);
        }

        @Override
        public int hashWithState(int x, int y, int z, int state) {
            return (int)(hashLongs(x, y, z, state) >>> 32);
        }

        @Override
        public int hashWithState(int x, int y, int z, int w, int state) {
            return (int)(hashLongs(x, y, z, w, state) >>> 32);
        }

        @Override
        public int hashWithState(int x, int y, int z, int w, int u, int state) {
            return (int)(hashLongs(x, y, z, w, u, state) >>> 32);
        }

        @Override
        public int hashWithState(int x, int y, int z, int w, int u, int v, int state) {
            return (int)(hashLongs(x, y, z, w, u, v, state) >>> 32);
        }
    }

}
