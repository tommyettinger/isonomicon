package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.AnimatedPNG;
import com.github.tommyettinger.anim8.Dithered;
import com.github.tommyettinger.anim8.PaletteReducer;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.visual.SmudgeRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Isomancer extends ApplicationAdapter {
//    public static final int QUALITY = 48;
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static boolean GLITCH = false;
    private SmudgeRenderer renderer;
    private byte[][][] voxels;
    private String name;
    private String[] inputs;
    private PixmapIO.PNG png;
    private AnimatedGif gif;
//    private PNG8 png8;
    private AnimatedPNG apng;
    public Isomancer(String[] args){
        if(args != null && args.length > 0)
            inputs = args;
        else 
        {
            System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/Oklab.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Lomuk.vox", "vox/Tree.vox", "vox/Eye_Tyrant.vox", "vox/IPT.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Infantry_Firing.vox"};
//            inputs = new String[]{"vox/IPT_No_Pow.vox"};
//            inputs = new String[]{"vox/Box.vox", "vox/Direction_Cube.vox"};
//            inputs = new String[]{"vox/IPT_Original.vox"};
//            inputs = new String[]{"vox/IPT.vox"};
//            inputs = new String[]{"vox/LAB.vox"};
//            inputs = new String[]{"vox/Oklab.vox"};
            inputs = new String[]{"vox/Oklab.vox", "vox/LAB.vox", "vox/IPT.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox"};
//            inputs = new String[]{"vox/Floor.vox"};
//            inputs = new String[]{"vox/Bear.vox"};
//            inputs = new String[]{"vox/Lomuk.vox"};
//            inputs = new String[]{"vox/teapot.vox"};
            if(!new File(inputs[0]).exists())
                System.exit(0);
        }
    }
    @Override
    public void create() {
        if (inputs == null) Gdx.app.exit();
        long startTime = TimeUtils.millis();
        RandomXS128 random = new RandomXS128(1, 1);
//        Gdx.files.local("out/vox/").mkdirs();
        png = new PixmapIO.PNG();
//        png8 = new PNG8();
        gif = new AnimatedGif();
//        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
//        png8.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
        gif.palette = new PaletteReducer();
//        png8.palette = gif.palette;
        gif.palette.setDitherStrength(0.625f);
        Gdx.files.local("out/vox").mkdirs();
        for (String s : inputs) {
            System.out.println("Rendering " + s);
            load(s);
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
            try {
                Pixmap pixmap;
                Array<Pixmap> pm = new Array<>(64);
                for (int i = 0; i < 64; i++) {
                    // glitch mode
                    if(GLITCH) {
                        random.setSeed(s.hashCode() + i);
                        renderer.saturation(random.nextFloat() - 0.5f);
                        for (int x = 0; x < voxels.length; x++) {
                            for (int y = 0; y < voxels[0].length; y++) {
                                for (int z = 0; z < voxels[0][0].length; z++) {
                                    if(voxels[x][y][z] != 0 && (random.nextLong() & 62L) == 0L)
                                        voxels[x][y][z] = (byte) (random.nextInt(256) & -random.nextInt(2) & -random.nextInt(2));
                                }
                            }
                        }
                        voxels = Tools3D.translateCopy(voxels,
                                (random.nextInt(3) & random.nextInt(3)) - (random.nextInt(3) & random.nextInt(3)),
                                (random.nextInt(3) & random.nextInt(3)) - (random.nextInt(3) & random.nextInt(3)),
                                (random.nextInt(3) & random.nextInt(3)) - (random.nextInt(3) & random.nextInt(3)));
                        pixmap = renderer.drawSplats(voxels, (i & 63) * 0x1p-6f + (random.nextFloat() - random.nextFloat()) * 0x1p-5f, VoxIO.lastMaterials);
                    }
                    else {
                        pixmap = renderer.drawSplats(voxels, (i & 63) * 0x1p-6f, VoxIO.lastMaterials);
                    }
                    Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                    p.drawPixmap(pixmap, 0, 0);
                    pm.add(p);
                    png.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p);
//                    for (int colorCount : new int[]{3, 8, 32, 64, 86, 128, 256}) {
//                        png8.palette.exact(Coloring.HALTONIC255, colorCount);
//                        png8.write(Gdx.files.local("out/lowColor/" + colorCount + "/" + name + '/' + name + "_angle" + i + ".png"), p, false);
//                    }
//                    VoxIO.writeVOX("out/vox/" + s.substring(4, s.length() - 4) + "_angle"+i+".vox", renderer.remade, VoxIO.lastPalette);
                }
//                for (int colorCount : new int[]{3, 8, 32, 64, 86, 128, 256}) {
//                    gif.palette.exact(Coloring.HALTONIC255, colorCount);
//                    gif.write(Gdx.files.local("out/lowColor/" + colorCount + "/" + name + '/' + name + ".gif"), pm, 12);
//                }
                gif.palette.analyze(pm);
                gif.write(Gdx.files.local("out/" + name + '/' + name + ".gif"), pm, 12);
                gif.palette.setDefaultPalette();
                gif.write(Gdx.files.local("out/" + name + '/' + name + "-256-color.gif"), pm, 12);
//                apng.write(Gdx.files.local("out/" + name + '/' + name + ".png"), pm, 12);
                for(Pixmap pix : pm) {
                    pix.dispose();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        final Isomancer app = new Isomancer(arg);
        new Lwjgl3Application(app, config);
    }

    public void load(String name) {
        try {
            //// loads a file by its full path, which we get via a command-line arg
            voxels = VoxIO.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            if(voxels == null) {
                voxels = new byte[][][]{{{1}}};
                return;
            }
            voxels = Tools3D.scaleAndSoak(voxels);
            int nameStart = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1;
            this.name = name.substring(nameStart, name.indexOf('.', nameStart));
//            renderer = new NextRenderer(voxels.length, QUALITY);
//            renderer = new AngledRenderer(voxels.length);
            renderer = new SmudgeRenderer(voxels.length);
            renderer.palette(VoxIO.lastPalette);
            renderer.saturation(0f);
            
        } catch (FileNotFoundException e) {
            voxels = new byte[][][]{{{1}}}; 
        }
    }
}
