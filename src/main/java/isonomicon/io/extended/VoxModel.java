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
    public ArrayList<GroupChunk> groupChunks;
    public ArrayList<ShapeChunk> shapeChunks;
    public VoxModel(){
        palette = Arrays.copyOf(VoxIO.defaultPalette, 256);
        grids = new ArrayList<>(1);
        materials = new IntMap<>();
        transformChunks = new ArrayList<>(1);
        groupChunks = new ArrayList<>(0);
        shapeChunks = new ArrayList<>(0);
    }
}
