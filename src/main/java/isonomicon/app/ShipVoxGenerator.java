package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.digital.Hasher;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.physical.ModelMaker;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;

public class ShipVoxGenerator extends ApplicationAdapter {
    public ShipVoxGenerator(){
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
            byte[][][] voxels = mm.shipLargeSmoothColorized();
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
        final ShipVoxGenerator app = new ShipVoxGenerator();
        new Lwjgl3Application(app, config);
    }
}
