package isonomicon.io.extended;

import com.github.tommyettinger.ds.IntObjectMap;
import com.github.tommyettinger.ds.LongOrderedSet;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class VoxModel {
    public int[] palette;
    public ArrayList<byte[][][]> grids;
    public ArrayList<IntObjectMap<float[]>> links;
    public ArrayList<IntObjectMap<LongOrderedSet>> markers;
    public IntObjectMap<VoxMaterial> materials;
    public IntObjectMap<TransformChunk> transformChunks;
    public IntObjectMap<GroupChunk> groupChunks;
    public IntObjectMap<ShapeChunk> shapeChunks;
    public VoxModel(){
        palette = Arrays.copyOf(VoxIO.defaultPalette, 256);
        grids = new ArrayList<>(1);
        links = new ArrayList<>(1);
        markers = new ArrayList<>(1);
        materials = new IntObjectMap<>(256);
        transformChunks = new IntObjectMap<>(8);
        groupChunks = new IntObjectMap<>(1);
        shapeChunks = new IntObjectMap<>(8);
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
        ArrayList<IntObjectMap<float[]>> nextLinks = new ArrayList<>(links.size());
        ArrayList<IntObjectMap<LongOrderedSet>> nextMarkers = new ArrayList<>(markers.size());
        for (int i = 0; i < grids.size(); i++) {
            nextGrids.add(Tools3D.deepCopy(grids.get(i)));
        }
        next.grids = nextGrids;
        for (int i = 0; i < links.size(); i++) {
            nextLinks.add(new IntObjectMap<>(links.get(i)));
        }
        next.links = nextLinks;
        for (int i = 0; i < markers.size(); i++) {
            nextMarkers.add(new IntObjectMap<>(markers.get(i)));
        }
        next.markers = nextMarkers;
        return next;
    }
}
