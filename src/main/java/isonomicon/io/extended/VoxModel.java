package isonomicon.io.extended;

import com.badlogic.gdx.utils.IntMap;
import com.github.tommyettinger.ds.LongOrderedSet;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class VoxModel {
    public int[] palette;
    public ArrayList<byte[][][]> grids;
    public ArrayList<IntMap<float[]>> links;
    public ArrayList<IntMap<LongOrderedSet>> markers;
    public IntMap<VoxMaterial> materials;
    public IntMap<TransformChunk> transformChunks;
    public IntMap<GroupChunk> groupChunks;
    public IntMap<ShapeChunk> shapeChunks;
    public VoxModel(){
        palette = Arrays.copyOf(VoxIO.defaultPalette, 256);
        grids = new ArrayList<>(1);
        links = new ArrayList<>(1);
        markers = new ArrayList<>(1);
        materials = new IntMap<>(256);
        transformChunks = new IntMap<>(8);
        groupChunks = new IntMap<>(1);
        shapeChunks = new IntMap<>(8);
    }
    public VoxModel mergeWith(VoxModel other) {
        grids.addAll(other.grids);
        links.addAll(other.links);
        markers.addAll(other.markers);
        return this;
    }

    public VoxModel copy(){
        VoxModel next = new VoxModel();
        next.palette = Arrays.copyOf(palette, palette.length);
        ArrayList<byte[][][]> nextGrids = new ArrayList<>(grids.size());
        ArrayList<IntMap<float[]>> nextLinks = new ArrayList<>(links.size());
        ArrayList<IntMap<LongOrderedSet>> nextMarkers = new ArrayList<>(markers.size());
        for (int i = 0; i < grids.size(); i++) {
            nextGrids.add(Tools3D.deepCopy(grids.get(i)));
        }
        next.grids = nextGrids;
        for (int i = 0; i < links.size(); i++) {
            nextLinks.add(new IntMap<>(links.get(i)));
        }
        next.links = nextLinks;
        for (int i = 0; i < markers.size(); i++) {
            nextMarkers.add(new IntMap<>(markers.get(i)));
        }
        next.markers = nextMarkers;
        return next;
    }
}
