package isonomicon.io.extended;

import com.badlogic.gdx.utils.IntMap;
import isonomicon.io.VoxIO;
import isonomicon.physical.VoxMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class VoxModel {
    public int[] palette;
    public ArrayList<byte[][][]> grids;
    public IntMap<VoxMaterial> materials;
    public ArrayList<TransformChunk> transformChunks;
    public VoxModel(){
        palette = Arrays.copyOf(VoxIO.defaultPalette, 256);
        grids = new ArrayList<>(1);
        materials = new IntMap<>();
        transformChunks = new ArrayList<>(0);
    }
}
