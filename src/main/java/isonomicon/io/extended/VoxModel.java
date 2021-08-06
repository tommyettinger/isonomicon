package isonomicon.io.extended;

import com.badlogic.gdx.utils.IntMap;
import isonomicon.io.VoxIO;
import isonomicon.physical.VoxMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class VoxModel {
    public int[] palette;
    public ArrayList<byte[][][]> grids;
    public ArrayList<IntMap<float[]>> links;
    public IntMap<VoxMaterial> materials;
    public IntMap<TransformChunk> transformChunks;
    public IntMap<GroupChunk> groupChunks;
    public IntMap<ShapeChunk> shapeChunks;
    public VoxModel(){
        palette = Arrays.copyOf(VoxIO.defaultPalette, 256);
        grids = new ArrayList<>(1);
        links = new ArrayList<>(1);
        materials = new IntMap<>();
        transformChunks = new IntMap<>(8);
        groupChunks = new IntMap<>(1);
        shapeChunks = new IntMap<>(8);
    }
}
