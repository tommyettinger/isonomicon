package isonomicon.smudge;

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
import com.github.yellowstonegames.grid.FlawedPointHash;
import com.github.yellowstonegames.grid.IPointHash;
import com.github.yellowstonegames.grid.Noise;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;
import isonomicon.visual.Coloring;
import isonomicon.visual.SmudgeRenderer;

import java.io.IOException;

public class NoiseRenderer extends ApplicationAdapter {
//    public static final int QUALITY = 48;
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SmudgeRenderer renderer;
    private Noise noise;

    private static final int SMALL_SIZE = 64, MID_SIZE = SMALL_SIZE << 1, LARGE_SIZE = SMALL_SIZE << 2;
    private byte[][][] tempVoxels = new byte[SMALL_SIZE][SMALL_SIZE][SMALL_SIZE];
    private byte[][][] midVoxels = new byte[MID_SIZE][MID_SIZE][MID_SIZE];
    private byte[][][] voxels = new byte[LARGE_SIZE][LARGE_SIZE][LARGE_SIZE];
    private String name;
    private AnimatedGif gif;
    PixmapIO.PNG png;
//    private PNG8 png8;
//    private AnimatedPNG apng;
    public NoiseRenderer(String[] args){
    }
    @Override
    public void create() {
        System.out.println("Setting up...");
        noise = new Noise(123456789, 0x1p-3f, Noise.CUBIC_FRACTAL, 1);
//        noise2 = new FastNoise(-4321, 0x1p-4f, FastNoise.PERLIN_FRACTAL, 2);
        noise.setFractalType(Noise.RIDGED_MULTI);
        noise.setPointHash(new CubeHash(1234, 8));
        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
//        png = new PixmapIO.PNG();
//        png8 = new PNG8();
        gif = new AnimatedGif();
        png = new PixmapIO.PNG();
        png.setFlipY(true);
        png.setCompression(2);
//        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.NEUE);
        gif.setDitherStrength(0.75f);
        Gdx.files.local("out/vox").mkdirs();
        System.out.println("Loading...");
//        System.out.println("Produced "+SMALL_SIZE+"x"+SMALL_SIZE+"x"+SMALL_SIZE+" noise.");

//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
        Pixmap pixmap;
        Array<Pixmap> pm = new Array<>(SMALL_SIZE);
        final float fraction = 1f / SMALL_SIZE;
        for (int i = 0; i < SMALL_SIZE; i++) {
            for (int f = 0; f < 1; f++) {
                System.out.print(i + ": ");
                load(i);
                pixmap = renderer.drawSplats(voxels, i * fraction, f, VoxIO.lastMaterials);
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
//                    sum += (tempVoxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.91f) >> 31) & 44)); // gray
//                    sum += (tempVoxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.91f) >> 31) & 175)); // blue
                    sum += (tempVoxels[x][y][z] = (byte) ((~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.91f) >> 31) & 68))); // orange
//                    sum += (tempVoxels[x][y][z] = (byte) ((~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.91f) >> 31) & (int)(12 + 2f * noise2.getConfiguredNoise(x, y, z, TrigTools.sin_(frame * 0x1p-6f), TrigTools.cos_(frame * 0x1p-6f)))))); // jungle greenery
//                    sum += (tempVoxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.91f) >> 31) & (x + y + z + frame & 63))); // rainbow
                }
            }
        }
        System.out.println(sum);
        Tools3D.simpleScale(tempVoxels, midVoxels);
//        Tools3D.simpleScale(midVoxels, voxels);
        Tools3D.soakInPlace(midVoxels);
        voxels = midVoxels;
        this.name = "Noise";
        if(renderer == null) {
            for (int i = 1; i < 256; i++) {
                VoxIO.lastMaterials.put(i, new VoxMaterial("Metal", "Roughness 0.6 Reflection 0.2"));
            }
            renderer = new SmudgeRenderer(voxels.length);
            renderer.palette(Coloring.BETSY256);
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
