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
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.digital.Hasher;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.ModelMaker;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.SmudgeRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShipVoxGenerator extends ApplicationAdapter {
    private VoxModel model;
    private byte[][][] voxels;
    private String name;
    public ShipVoxGenerator(String[] args){
        Tools3D.STUFFS = Stuff.STUFFS_B;
        VoxIOExtended.GENERAL = true;
    }
    @Override
    public void create() {
        long startTime = TimeUtils.nanoTime(), seed = Hasher.randomize3(startTime);
        Gdx.files.local("out/vox").mkdirs();
        ModelMaker mm = new ModelMaker(seed, new PaletteReducer());
        for (int i = 0; i < 16; i++) {
            mm.rng.setSeed(seed = Hasher.randomize3(seed));
            voxels = mm.shipLargeSmoothColorized();
            VoxIO.writeVOX("out/vox/" + seed + ".vox", voxels, Coloring.BETTS64, Stuff.MATERIALS_B);
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
        config.setWindowedMode(400, 400);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final ShipVoxGenerator app = new ShipVoxGenerator(arg);
        new Lwjgl3Application(app, config);
    }
}
