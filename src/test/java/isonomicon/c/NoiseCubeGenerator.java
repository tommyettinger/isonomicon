package isonomicon.c;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.tommyettinger.digital.ArrayTools;
import com.github.yellowstonegames.grid.*;
import isonomicon.b.StuffNotes;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.visual.Coloring;

public class NoiseCubeGenerator extends ApplicationAdapter {

    public static final int SIZE = 32;

    @Override
    public void create() {
        byte[][][] data = new byte[SIZE][SIZE][SIZE];
//        fillRocky(data, 123L);
//        VoxIOExtended.writeVOX("specialized/c/cubes/" + "Rocky_Cube" + ".vox", data, Coloring.YAM4, null);
//        ArrayTools.fill(data, 0);
//        INoise base = new CellularNoise(123l, CellularNoise.NoiseType.DISTANCE);
//        INoise base = new FoamNoise(123L);
        INoise base = new HuskyNoise(123L, 3, 1.5f);
        fillGradient(data, base);
        VoxIOExtended.writeVOX("specialized/c/cubes/" + "Gradient_Cube_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
        Gdx.app.exit();
    }

    public void fillRocky(byte[][][] data, long seed){
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE - 1; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x][y][z] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 19 : 18);
                }
                float n = noise.getNoise(x, y, SIZE - 1);
                data[x][y][SIZE - 1] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 0 : 18);
            }
        }
    }

    public void fillGradient(byte[][][] data, INoise base){
        INoise noise = new NoiseWrapper(base, 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x][y][z] = (byte)(236 + n * 16);
                }
            }
        }
    }

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Noise Cubes");
        config.setWindowedMode(600, 600);
        config.setIdleFPS(10);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        new Lwjgl3Application(new NoiseCubeGenerator(), config);
    }

}
