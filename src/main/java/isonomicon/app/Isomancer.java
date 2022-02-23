package isonomicon.app;

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
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.SmudgeRenderer;

import java.io.*;

public class Isomancer extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SmudgeRenderer renderer;
    private byte[][][] voxels;
    private String name;
    private String[] inputs;
    private PixmapIO.PNG png;
    private AnimatedGif gif;
//    private PNG8 png8;
//    private AnimatedPNG apng;
    public Isomancer(String[] args){
        Tools3D.STUFFS = Stuff.STUFFS;
        if(args != null && args.length > 0)
            inputs = args;
        else 
        {
            System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Damned.vox", "vox/Bear.vox", "vox/Infantry.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/Box.vox", "vox/Direction_Cube.vox", "vox/teapot.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Lomuk.vox", "vox/Tree.vox", "vox/Eye_Tyrant.vox", "vox/IPT.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Infantry_Firing.vox"};
//            inputs = new String[]{"vox/Infantry.vox"};
//            inputs = new String[]{"vox/IPT_No_Pow.vox"};
//            inputs = new String[]{"vox/Box.vox", "vox/Direction_Cube.vox"};
//            inputs = new String[]{"vox/IPT_Original.vox"};
//            inputs = new String[]{"vox/IPT.vox"};
//            inputs = new String[]{"vox/LAB.vox"};
//            inputs = new String[]{"vox/Oklab.vox"};
//            inputs = new String[]{"vox/Oklab.vox", "vox/LAB.vox", "vox/IPT.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox"};
//            inputs = new String[]{"vox/Floor.vox"};
//            inputs = new String[]{"vox/Bear.vox"};
            inputs = new String[]{"vox/Lomuk.vox"};
//            inputs = new String[]{"vox/Lomuk.vox", "vox/Damned.vox"};
//            inputs = new String[]{"vox/Damned.vox"};
//            inputs = new String[]{"vox/teapot.vox"};
//            inputs = new String[]{"vox/BareBear.vox", "vox/Grin.vox", "vox/Castle.vox"};
            if(!new File(inputs[0]).exists())
                System.exit(0);
        }
    }
    @Override
    public void create() {
        if (inputs == null) Gdx.app.exit();
        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
        png = new PixmapIO.PNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
//        png8 = new PNG8();
        gif = new AnimatedGif();
//        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.NEUE);
//        png8.setDitherAlgorithm(Dithered.DitherAlgorithm.NEUE);
        gif.palette = new PaletteReducer();
//        gif.palette = new PaletteReducer(Coloring.YAM2, Gdx.files.local("assets/Yam2Preload.dat").readBytes());
        gif.palette.setDitherStrength(0.625f);
//        png8.palette = gif.palette;
        Gdx.files.local("out/vox").mkdirs();
        for (String s : inputs) {
            System.out.println("Rendering " + s);
            load(s);
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
            Pixmap pixmap;
            Array<Pixmap> pm = new Array<>(8);
            for (int i = 0; i < 8; i++) {
                for (int f = 0; f < 4; f++) {
                    pixmap = renderer.drawSplats(voxels, i * 0.125f, f, VoxIO.lastMaterials);
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
                pm.insertRange(pm.size - 4, 4);
            }
            gif.palette.analyze(pm);
            gif.write(Gdx.files.local("out/" + name + '/' + name + ".gif"), pm, 8);
//                gif.palette.exact(Coloring.HALTONITE240, PRELOAD);
//                gif.write(Gdx.files.local("out/" + name + '/' + name + "-256-color.gif"), pm, 1);
//                apng.write(Gdx.files.local("out/" + name + '/' + name + ".png"), pm, 12);
            for (Pixmap pix : pm) {
                if (!pix.isDisposed())
                    pix.dispose();
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
//            voxels = Tools3D.soak(voxels);

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
