package isonomicon.io.extended;

import com.github.tommyettinger.ds.IntObjectMap;
import com.github.tommyettinger.ds.LongOrderedSet;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.util.ArrayList;
import java.util.Arrays;

import static isonomicon.io.extended.VoxIOExtended.SCALE;
import static isonomicon.io.extended.VoxIOExtended.SOAK;

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
        groupChunks = new IntObjectMap<>(1);
        transformChunks = new IntObjectMap<>(8);
        shapeChunks = new IntObjectMap<>(8);
    }

    public VoxModel(byte[][][] voxelData, int[] palette, IntObjectMap<VoxMaterial> materials){
        this.palette = Arrays.copyOf(palette, 256);
        grids = new ArrayList<>(1);
        if(SCALE){
            if(SOAK)
                voxelData = Tools3D.scaleAndSoak(voxelData);
            else
                voxelData = Tools3D.simpleScale(voxelData);
        } else {
            if(SOAK)
                Tools3D.soakInPlace(voxelData);
        }

        grids.add(voxelData);
        links = new ArrayList<>(1);
        markers = new ArrayList<>(1);
        this.materials = new IntObjectMap<>(materials);
        groupChunks = new IntObjectMap<>(1);
        groupChunks.put(0, new GroupChunk(0, new String[0][2], new int[]{0}));
        transformChunks = new IntObjectMap<>(8);
        TransformChunk latest = new TransformChunk(0, new String[0][2], 0, 0, 0, new String[1][0][2]);
        latest.translation.z -= voxelData[0][0].length * 0.5f;
        transformChunks.put(0, latest);
        shapeChunks = new IntObjectMap<>(8);
        ShapeModel sm = new ShapeModel(0, new String[2][0]);
        ShapeChunk sc = new ShapeChunk(0, new String[0][2], new ShapeModel[]{sm});
        shapeChunks.put(0, sc);

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
            IntObjectMap<float[]> ls = new IntObjectMap<>(links.get(i).size());
            for(IntObjectMap.Entry<float[]> e : links.get(i)){
                ls.put(e.key, Arrays.copyOf(e.value, 4));
            }
            nextLinks.add(ls);
        }
        next.links = nextLinks;
        for (int i = 0; i < markers.size(); i++) {
            IntObjectMap<LongOrderedSet> ms = new IntObjectMap<>(markers.get(i));
            for(IntObjectMap.Entry<LongOrderedSet> e : markers.get(i)){
                ms.put(e.key, new LongOrderedSet(e.value));
            }
            nextMarkers.add(ms);
        }
        next.markers = nextMarkers;
        return next;
    }
}
