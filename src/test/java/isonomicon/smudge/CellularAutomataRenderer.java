package isonomicon.smudge;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.digital.Base;
import com.github.yellowstonegames.grid.FlawedPointHash;
import com.github.yellowstonegames.grid.IPointHash;
import com.github.yellowstonegames.grid.Noise;
import isonomicon.app.AppConfig;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;
import isonomicon.visual.Coloring;
import isonomicon.visual.SmudgeRenderer;
import org.apache.http.cookie.SM;

public class CellularAutomataRenderer extends ApplicationAdapter {
//    public static final int QUALITY = 48;
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SmudgeRenderer renderer;
    private int seed = 0xDE4D;

    private static final int SMALL_SIZE = 32, MID_SIZE = SMALL_SIZE << 1, LARGE_SIZE = SMALL_SIZE << 2;
    private byte[][][][] pingPong = new byte[2][SMALL_SIZE][SMALL_SIZE][SMALL_SIZE];
    private byte[][][] tempVoxels = new byte[SMALL_SIZE][SMALL_SIZE][SMALL_SIZE];
    private byte[][][] tempVoxelsA = new byte[SMALL_SIZE][SMALL_SIZE][SMALL_SIZE];
    private byte[][][] tempVoxelsB = new byte[SMALL_SIZE][SMALL_SIZE][SMALL_SIZE];
    private byte[][][] midVoxels = new byte[MID_SIZE][MID_SIZE][MID_SIZE];
    private byte[][][] voxels = new byte[LARGE_SIZE][LARGE_SIZE][LARGE_SIZE];
    private String name;
    private AnimatedGif gif;
//    PixmapIO.PNG png;
//    private PNG8 png8;
//    private AnimatedPNG apng;
    public CellularAutomataRenderer(String[] args){
    }
    @Override
    public void create() {
        System.out.println("Setting up...");
        this.name = "CA_"+seed;

        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
//        png = new PixmapIO.PNG();
//        png8 = new PNG8();
        gif = new AnimatedGif();
//        png = new PixmapIO.PNG();
//        png.setFlipY(true);
//        png.setCompression(2);
//        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(AppConfig.DITHER);
        gif.setDitherStrength(AppConfig.STRENGTH);

        System.out.println("Loading...");

        tempVoxels = VoxIO.readVox(Gdx.files.local("specialized/b/vox/ca/ca1.vox").read());
        Tools3D.deepCopyInto(tempVoxels, pingPong[0]);

        Pixmap pixmap;
        Array<Pixmap> pm = new Array<>(MID_SIZE << 2);
        final float fraction = 1f / (MID_SIZE << 2);
        for (int i = 0; i < MID_SIZE << 2; i++) {
            for (int f = 0; f < 1; f++) {
                System.out.print(i + ": ");
                if((i & 3) == 0)
                    load(i >>> 2);
                pixmap = renderer.drawSplats(voxels, i * fraction, f, VoxIO.lastMaterials);
                Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                p.drawPixmap(pixmap, 0, 0);
                pm.add(p);
//                try {
//                    png.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), p);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
            }
//            pm.insertRange(pm.size - 4, 4);
        }
        System.out.println("Mostly done, animation stuff in progress...");
        gif.palette = new com.github.tommyettinger.anim8.QualityPalette(pm);
        gif.write(Gdx.files.local("out/" + name + '/' + name + ".gif"), pm, 30);
//                gif.palette.exact(Coloring.HALTONITE240, PRELOAD);
//                gif.write(Gdx.files.local("out/" + name + '/' + name + "-256-color.gif"), pm, 1);
//        apng.write(Gdx.files.local("out/" + name + '/' + name + ".png"), pm, 30);
        for (Pixmap pix : pm) {
            if (!pix.isDisposed())
                pix.dispose();
        }
        System.out.println("Wrote to " + "out/" + name + '/' + name + ".gif");
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
        final CellularAutomataRenderer app = new CellularAutomataRenderer(arg);
        new Lwjgl3Application(app, config);
    }

    public void load(int frame) {
        byte[][][] prior = pingPong[frame & 1], current = pingPong[~frame & 1];
        Tools3D.fill(current, 0);
        int total = 0;
        final int SMALL_MASK = SMALL_SIZE - 1;
        for (int x = 0; x < SMALL_SIZE; x++) {
            for (int y = 0; y < SMALL_SIZE; y++) {
                for (int z = 0; z < SMALL_SIZE; z++) {
                    int sum = 0;
                    for (int xx = -1; xx <= 1; xx++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            for (int zz = -1; zz <= 1; zz++) {
                                sum += prior[x + xx & SMALL_MASK][y + yy & SMALL_MASK][z + zz & SMALL_MASK];
                            }
                        }
                    }
//                    sum += prior[x - 1 & SMALL_MASK][y & SMALL_MASK][z & SMALL_MASK];
//                    sum += prior[x + 1 & SMALL_MASK][y & SMALL_MASK][z & SMALL_MASK];
//                    sum += prior[x & SMALL_MASK][y - 1 & SMALL_MASK][z & SMALL_MASK];
//                    sum += prior[x & SMALL_MASK][y + 1 & SMALL_MASK][z & SMALL_MASK];
//                    sum += prior[x & SMALL_MASK][y & SMALL_MASK][z - 1 & SMALL_MASK];
//                    sum += prior[x & SMALL_MASK][y & SMALL_MASK][z + 1 & SMALL_MASK];
                    total += (current[x][y][z] = (byte) (((((sum = ((sum *= seed) ^ sum >>> 3) * 0x9E377) ^ sum >>> 11)) & 3) % 3));
                }
            }
        }
        System.out.println(total);
        Tools3D.basicScale(current, midVoxels);
//        Tools3D.fill(tempVoxelsA, 0);
//        Tools3D.fill(tempVoxelsB, 0);
//        Tools3D.simpleScale(current, midVoxels, tempVoxelsA, tempVoxelsB);
//        Tools3D.simpleScale(midVoxels, voxels);
//        Tools3D.soakInPlace(midVoxels);
        voxels = midVoxels;
        if(renderer == null) {
            for (int i = 1; i < 256; i++) {
                VoxIO.lastMaterials.put(i, new VoxMaterial("Metal", "Roughness 0.6 Reflection 0.4 Dapple -0.04"));
            }
            renderer = new SmudgeRenderer(voxels.length);
            renderer.palette(new int[]{0, (Coloring.BETSY256[175] >>> 1 & 0x7F7F7F00) | 0xFF, Coloring.BETSY256[175]}, 3);
            renderer.saturation(0f);
        }
    }
}
