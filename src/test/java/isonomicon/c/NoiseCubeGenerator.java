package isonomicon.c;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.tommyettinger.digital.ArrayTools;
import com.github.yellowstonegames.grid.*;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.visual.Coloring;

public class NoiseCubeGenerator extends ApplicationAdapter {

    public static final int SIZE = 40;
    public static final int CUBE_SIZE = 32;
    public static final int OFFSET = SIZE - CUBE_SIZE >>> 1;

    @Override
    public void create() {
        byte[][][] data = new byte[SIZE][SIZE][SIZE];
        fillEarth(data, 123L);
        VoxIOExtended.writeVOX("specialized/c/cubes/" + "Earth_Cube" + ".vox", data, Coloring.YAM4, null);
        ArrayTools.fill(data, (byte) 0);
        fillEarthGrassTop(data, 1234L);
        VoxIOExtended.writeVOX("specialized/c/cubes/" + "Earth_Grass_Cube" + ".vox", data, Coloring.YAM4, null);
//        ArrayTools.fill(data, 0);
//        INoise base = new CellularNoise(123l, CellularNoise.NoiseType.DISTANCE);
//        INoise base = new FoamNoise(123L);
//        INoise base = new HuskyNoise(123L, 3, 1.5f);
//        fillGradient(data, base);
//        VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_Cube_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);

        Gdx.app.exit();
    }

    public void fillEarth(byte[][][] data, long seed){
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x + OFFSET][y + OFFSET][z] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 19 : 18);
                }
                float n = noise.getNoise(x, y, CUBE_SIZE);
                data[x + OFFSET][y + OFFSET][CUBE_SIZE] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 0 : 18);
            }
        }
    }


    public void fillEarthGrassTop(byte[][][] data, long seed){
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x + OFFSET][y + OFFSET][z] = (byte) (n > 0.9f ? 35 : n > 0.1f ? 19 : 18);
                }
                int rn = BlueNoise.getSeeded(x, y, (int) seed);
                int bn = (rn + 10) / 40;
                for (int z = Math.min(0, bn); z <= Math.max(0, bn); z++) {
                    data[x + OFFSET][y + OFFSET][CUBE_SIZE + z] = (byte)((rn & 7) < 2 ? 95 : 96);
                }
            }
        }
    }

    public void fillGradient(byte[][][] data, INoise base){
        INoise noise = new NoiseWrapper(base, 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x + OFFSET][y + OFFSET][z] = (byte)(236 + n * 16);
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
