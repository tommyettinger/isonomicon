package isonomicon.util;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.PaletteReducer;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VoxModelCheck extends ApplicationAdapter {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("VoxModel Check");
        config.setWindowedMode(320, 320);
        config.setIdleFPS(1);
        config.setResizable(false);
        new Lwjgl3Application(new VoxModelCheck(), config);
    }

    public void create() {
        String name = "specialized/vox/FigureSplit.vox";
        try {
            VoxModel model = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            for(byte[][][] grid : model.grids)
                System.out.println(Tools3D.hash64(grid));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gdx.app.exit();
    }
}
