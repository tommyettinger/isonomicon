package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.AnimatedPNG;
import com.github.tommyettinger.anim8.Dithered;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.SmudgeRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Turntable extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SmudgeRenderer renderer;
    private VoxModel model;
    private byte[][][] voxels;
    private String name;
    private String[] inputs;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    public Turntable(String[] args){
        Tools3D.STUFFS = Stuff.STUFFS;
        VoxIOExtended.GENERAL = true;
        if(args != null && args.length > 0)
            inputs = args;
        else 
        {
            System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Damned.vox", "vox/Bear.vox", "vox/Infantry.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/Box.vox", "vox/Direction_Cube.vox", "vox/teapot.vox"};
            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Tree.vox"};
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
//            inputs = new String[]{"vox/Oklab.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Oklab.vox", "vox/LAB.vox", "vox/IPT.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox"};
//            inputs = new String[]{"vox/Floor.vox"};
//            inputs = new String[]{"vox/Bear.vox"};
//            inputs = new String[]{"vox/Lomuk.vox"};
//            inputs = new String[]{"vox/Predator.vox"};
//            inputs = new String[]{"vox/FigureSplit.vox"};
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
        gif = new AnimatedGif();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.LOAF);
        apng = new AnimatedPNG();
        gif.palette = new com.github.tommyettinger.anim8.QualityPalette(); // Uses DB Aurora
        gif.setDitherStrength(1.0_0f);
        gif.fastAnalysis = false;
        Gdx.files.local("out/vox").mkdirs();
        for (String s : inputs) {
            System.out.println("Rendering " + s);
            load(s);
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
            Pixmap pixmap;
            Array<Pixmap> pm = new Array<>(128);
            for (int i = 0; i < 128; i++) {
                pixmap = renderer.drawSplats(voxels, i * 0x1p-7f + 0.125f, 0, 0, i & 3, 0, 0, 0, VoxIO.lastMaterials);
                Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                p.drawPixmap(pixmap, 0, 0);
                pm.add(p);
            }
            gif.palette.analyze(pm, 75.0, 256);
            gif.write(Gdx.files.local("out/Turntable/" + name + ".gif"), pm, 24);
            apng.write(Gdx.files.local("out/Turntable/" + name + ".png"), pm, 24);
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
        final Turntable app = new Turntable(arg);
        new Lwjgl3Application(app, config);
    }

    public void load(String name) {
        try {
            //// loads a file by its full path, which we get via a command-line arg
            model = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            if(model == null) {
                model = new VoxModel();
                return;
            }
            int nameStart = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1;
            this.name = name.substring(nameStart, name.indexOf('.', nameStart));
            voxels = VoxIOExtended.mergeModel(model);
            renderer = new SmudgeRenderer(voxels.length);
            renderer.palette(VoxIO.lastPalette);
            renderer.saturation(0f);
//            renderer.init(); // only needed when using `SpotRenderer` class, not `SmudgeRenderer`
        } catch (FileNotFoundException e) {
            model = new VoxModel();
        }
    }
}
