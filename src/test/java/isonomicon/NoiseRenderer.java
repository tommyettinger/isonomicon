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
        noise.setPointHash(new FlawedPointHash.CubeHash(123, 16));
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
        System.out.println("Produced 128x128x128 noise.");
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
        for (int x = 0; x < voxels.length; x++) {
            for (int y = 0; y < voxels[0].length; y++) {
                for (int z = 0; z < voxels[0][0].length; z++) {
                    sum += (voxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.875f) >> 31) & 81)); // gray-green
//                    sum += (voxels[x][y][z] = (byte) (~(Float.floatToRawIntBits(noise.getConfiguredNoise(x, y, z, frame) - 0.875f) >> 31) & 44)); // gray
                }
            }
        }
        System.out.println(sum);
        voxels = Tools3D.soak(voxels);
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
}
