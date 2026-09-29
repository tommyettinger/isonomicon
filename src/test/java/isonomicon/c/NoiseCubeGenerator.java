package isonomicon.c;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.tommyettinger.digital.ArrayTools;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import com.github.tommyettinger.function.IntIntToIntBiFunction;
import com.github.yellowstonegames.grid.*;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.visual.Coloring;

import java.util.Map;

public class NoiseCubeGenerator extends ApplicationAdapter {

    public static final int SIZE = 40;
    public static final int CUBE_SIZE = 32;
    public static final int OFFSET = SIZE - CUBE_SIZE >>> 1;

    public static final ObjectObjectOrderedMap<String, IntIntToIntBiFunction> SHAPES =
            new ObjectObjectOrderedMap<>(
                    new String[]{
                            "Cube",
                            "Slope",
                            "Corner",
                            "Nick",
                            "Ridge",
                            "Valley",
                    },
                    new IntIntToIntBiFunction[]{
                            (x, y) -> CUBE_SIZE,
                            (x, y) -> x,
                            Math::min,
                            Math::max,
                            (x, y) -> CUBE_SIZE - Math.abs(x - y),
                            (x, y) -> Math.abs(x - y),
                    });

    @Override
    public void create() {
        byte[][][] data = new byte[SIZE][SIZE][SIZE];
        for (Map.Entry<String, IntIntToIntBiFunction> e : SHAPES.entrySet()) {

            fillRock(data, Hasher.hashBulk64(121L, e.getKey()), e.getValue());
            VoxIOExtended.writeVOX("specialized/c/cubes/" + "Rock_" + e.getKey() + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            fillSand(data, Hasher.hashBulk64(122L, e.getKey()), e.getValue());
            VoxIOExtended.writeVOX("specialized/c/cubes/" + "Sand_" + e.getKey() + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            fillEarth(data, Hasher.hashBulk64(123L, e.getKey()), e.getValue());
            VoxIOExtended.writeVOX("specialized/c/cubes/" + "Earth_" + e.getKey() + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            fillEarthGrassTop(data, Hasher.hashBulk64(124L, e.getKey()), e.getValue());
            VoxIOExtended.writeVOX("specialized/c/cubes/" + "Earth_Grass_" + e.getKey() + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);

            INoise base;
            base = new CellularNoise(Hasher.hashBulk64(1234L, e.getKey()), CellularNoise.NoiseType.DISTANCE);
            fillGradient(data, base, e.getValue());
            VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_" + e.getKey() + "_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            base = new FoamNoise(Hasher.hashBulk64(1234L, e.getKey()));
            fillGradient(data, base, e.getValue());
            VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_" + e.getKey() + "_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            base = new HuskyNoise(Hasher.hashBulk64(1234L, e.getKey()), 3, 1.5f);
            fillGradient(data, base, e.getValue());
            VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_" + e.getKey() + "_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            base = new PerlueNoise(Hasher.hashBulk64(1234L, e.getKey()));
            fillGradient(data, base, e.getValue());
            VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_" + e.getKey() + "_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
            base = new SimplexNoise(Hasher.hashBulk64(1234L, e.getKey()));
            fillGradient(data, base, e.getValue());
            VoxIOExtended.writeVOX("specialized/c/test/" + "Gradient_" + e.getKey() + "_" + INoise.Serializer.serialize(base) + ".vox", data, Coloring.YAM4, null);
            ArrayTools.fill(data, (byte) 0);
        }
        Gdx.app.exit();
    }

    public void fillEarth(byte[][][] data, long seed, IntIntToIntBiFunction topFinder){
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                int top = topFinder.applyAsInt(x, y);
                for (int z = 0; z < top; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x + OFFSET][y + OFFSET][z] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 19 : 18);
                }
                float n = noise.getNoise(x, y, top);
                data[x + OFFSET][y + OFFSET][top] = (byte)(n > 0.9f ? 35 : n > 0.1f ? 0 : 18);
            }
        }
    }

    public void fillEarthGrassTop(byte[][][] data, long seed, IntIntToIntBiFunction topFinder) {
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                int top = topFinder.applyAsInt(x, y);
                for (int z = 0; z < top; z++) {
                    float n = noise.getNoise(x, y, z);
                    data[x + OFFSET][y + OFFSET][z] = (byte) (n > 0.9f ? 35 : n > 0.1f ? 19 : 18);
                }
                int rn = BlueNoise.getSeeded(x, y, (int) seed);
                int bn = (rn + 10) / 40;
                for (int z = 0; z < 2; z++) {
                    data[x + OFFSET][y + OFFSET][top + z] = 17;
                }
                for (int z = Math.min(0, bn); z <= Math.max(0, bn); z++) {
                    data[x + OFFSET][y + OFFSET][top + 2 + z] = (byte)((rn & 7) < 2 ? 95 : 96);
                }
            }
        }
    }

    public void fillSand(byte[][][] data, long seed, IntIntToIntBiFunction topFinder) {
        INoise noise = new NoiseWrapper(new FoamNoise(seed), 0.14f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                int top = topFinder.applyAsInt(x, y);
                for (int z = 0; z < top; z++) {
                    data[x + OFFSET][y + OFFSET][z] = 20;
                }
                float n = noise.getNoise(x, y);
                data[x + OFFSET][y + OFFSET][top] = (byte)(n < 0.85f ? 20 : 0);
                data[x + OFFSET][y + OFFSET][top + 1] = (byte)(n < 0.7f ? 20 : 0);
            }
        }
    }

    public void fillRock(byte[][][] data, long seed, IntIntToIntBiFunction topFinder) {
        INoise noise = new NoiseWrapper(new CellularNoise(seed, CellularNoise.NoiseType.DISTANCE), 0.14f, NoiseWrapper.RIDGED_MULTI, 1);
        final float threshold = 0.04f;
        for (int x = 1; x < CUBE_SIZE - 1; x++) {
            for (int y = 1; y < CUBE_SIZE - 1; y++) {
                int top = topFinder.applyAsInt(x, y);
                for (int z = 0; z < top; z++) {
                    data[x + OFFSET][y + OFFSET][z] = 32;
                }
            }
        }
        for (int fx = 0; fx < CUBE_SIZE; fx++) {
            for (int fz = 0; fz < CUBE_SIZE; fz++) {
                if(topFinder.applyAsInt(fx, 0) > fz) {
                    float n = noise.getNoiseWithSeed(fx, fz, seed);
                    data[fx + OFFSET][OFFSET+2][fz] = (byte) (n > threshold ? 0 : 32);
                    data[fx + OFFSET][OFFSET+1][fz] = (byte) (n > threshold - 0.1f? 0 : 32);
                    data[fx + OFFSET][OFFSET][fz] = (byte) (n > threshold - 0.2f ? 0 : 32);
                }
                if(topFinder.applyAsInt(fx, CUBE_SIZE - 1) > fz) {
                    float n = noise.getNoiseWithSeed(fx, fz, seed + 1);
                    data[fx + OFFSET][CUBE_SIZE - 3 + OFFSET][fz] = (byte) (n > threshold ? 0 : 32);
                    data[fx + OFFSET][CUBE_SIZE - 2 + OFFSET][fz] = (byte) (n > threshold - 0.1f? 0 : 32);
                    data[fx + OFFSET][CUBE_SIZE - 1 + OFFSET][fz] = (byte) (n > threshold - 0.2f ? 0 : 32);
                }
                if(topFinder.applyAsInt(0, fx) > fz) {
                    float n = noise.getNoiseWithSeed(fx, fz, seed + 2);
                    data[OFFSET+2][fx + OFFSET][fz] = (byte) (n > threshold ? 0 : 32);
                    data[OFFSET+1][fx + OFFSET][fz] = (byte) (n > threshold - 0.1f? 0 : 32);
                    data[OFFSET  ][fx + OFFSET][fz] = (byte) (n > threshold - 0.2f ? 0 : 32);
                }
                if(topFinder.applyAsInt(CUBE_SIZE - 1, fx) > fz) {
                    float n = noise.getNoiseWithSeed(fx, fz, seed + 3);
                    data[CUBE_SIZE - 3 + OFFSET][fx + OFFSET][fz] = (byte) (n > threshold ? 0 : 32);
                    data[CUBE_SIZE - 2 + OFFSET][fx + OFFSET][fz] = (byte) (n > threshold - 0.1f? 0 : 32);
                    data[CUBE_SIZE - 1 + OFFSET][fx + OFFSET][fz] = (byte) (n > threshold - 0.2f ? 0 : 32);
                }
            }
        }

        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                int top = topFinder.applyAsInt(x, y);
                float n = noise.getNoiseWithSeed(x, y, seed+4);
                data[x + OFFSET][y + OFFSET][top] = (byte)(n > threshold ? 0 : 32);
                data[x + OFFSET][y + OFFSET][top+1] = (byte)(n > threshold - 0.1f ? 0 : 32);
                data[x + OFFSET][y + OFFSET][top+2] = (byte)(n > threshold - 0.2f ? 0 : 32);
            }
        }
    }

    public void fillGradient(byte[][][] data, INoise base, IntIntToIntBiFunction topFinder) {
        INoise noise = new NoiseWrapper(base, 0.15f, NoiseWrapper.RIDGED_MULTI, 1);
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                int top = topFinder.applyAsInt(x, y);
                for (int z = 0; z < top; z++) {
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
