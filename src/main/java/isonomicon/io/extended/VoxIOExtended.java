package isonomicon.io.extended;

import com.badlogic.gdx.math.Vector3;
import com.github.tommyettinger.ds.IntObjectMap;
import com.github.tommyettinger.ds.LongOrderedSet;
import com.github.yellowstonegames.core.StringTools;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PrimitiveIterator;

import static isonomicon.io.VoxIO.lastMaterials;
import static isonomicon.io.VoxIO.lastPalette;


/**
 * Handles reading MagicaVoxel .vox files from file to VoxModel objects.
 * Major credit for figuring out the largely-undocumented MagicaVoxel .vox extension format goes to Zarbuz, since I had
 * to check <a href="https://github.com/Zarbuz/FileToVox">FileToVox</a> many times for help.
 * <br>
 * Created by Tommy Ettinger on 6/28/2021.
 */
public class VoxIOExtended {
    /**
     * If this is a general-purpose .vox loader, this should be true. For CG-specialized models, false.
     */
    public static boolean GENERAL = false;
    public static int minX = Integer.MAX_VALUE;
    public static int maxX;
    public static int minY = Integer.MAX_VALUE;
    public static int maxY;
    public static int minZ = Integer.MAX_VALUE;
    public static int maxZ;

    protected static String readString(LittleEndianDataInputStream stream) throws IOException {
        int len = stream.readInt();
        byte[] buf = new byte[len];
        stream.read(buf, 0, len);
        return new String(buf, StandardCharsets.ISO_8859_1);
    }
    protected static String[][] readStringPairs(LittleEndianDataInputStream stream) throws IOException {
        int len = stream.readInt();
        String[][] pairs = new String[len][2];
        for (int i = 0; i < len; i++) {
            pairs[i][0] = readString(stream);
            pairs[i][1] = readString(stream);
        }
        return pairs;
    }

    public static void getRotation(TransformChunk chunk, String[][] pairs){
        for(String[] pair : pairs){
            if("_roll".equals(pair[0])){
                try{
                    chunk.roll = Float.parseFloat(pair[1]);
                } catch (Exception ignored){
                    return;
                }
            }
            else if("_pitch".equals(pair[0])){
                try{
                    chunk.pitch = Float.parseFloat(pair[1]);
                } catch (Exception ignored){
                    return;
                }
            }
            else if("_yaw".equals(pair[0])){
                try{
                    chunk.yaw = Float.parseFloat(pair[1]);
                } catch (Exception ignored){
                    return;
                }
            }
        }
    }

    public static void getTranslation(Vector3 result, String[][] pairs){
        for(String[] pair : pairs) {
            if("_t".equals(pair[0])) {
                String[] parts = StringTools.split(pair[1], " ");
                if (parts.length > 0) {
                    try {
                        result.x = Float.parseFloat(parts[0]);
                    } catch (Exception ignored){}
                }
                if (parts.length > 1) {
                    try {
                        result.y = Float.parseFloat(parts[1]);
                    } catch (Exception ignored){}
                }
                if (parts.length > 2) {
                    try {
                        result.z = Float.parseFloat(parts[2]);
                    } catch (Exception ignored){}
                }
                return;
            }
        }
    }

    public static VoxModel readVox(InputStream stream) {
        return readVox(new LittleEndianDataInputStream(stream));
    }

    public static VoxModel readVox(LittleEndianDataInputStream stream) {
        // check out https://github.com/ephtracy/voxel-model/blob/master/MagicaVoxel-file-format-vox.txt for the file format used below
        lastMaterials.clear();
        VoxModel model = new VoxModel();
        byte[][][] voxelData = null;
        IntObjectMap<ShapeModel> shapes = new IntObjectMap<>(8);
        TransformChunk latest = null;

        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        minZ = Integer.MAX_VALUE;
        maxX = 0;
        maxY = 0;
        maxZ = 0;

        try {
            byte[] chunkId = new byte[4];
            if (4 != stream.read(chunkId))
                return null;
            //int version = 
            stream.readInt();
            int sizeX = 16, sizeY = 16, size = 16, sizeZ = 16, offX = 0, offY = 0;
            byte[] key = new byte[6]; // used for MaterialTrait
            byte[] val = new byte[10]; // used for MaterialType and numbers
            // a MagicaVoxel .vox file starts with a 'magic' 4 character 'VOX ' identifier
            if (chunkId[0] == 'V' && chunkId[1] == 'O' && chunkId[2] == 'X' && chunkId[3] == ' ') {
                while (stream.available() > 0) {
                    // each chunk has an ID, size and child chunks
                    stream.read(chunkId);
                    int chunkSize = stream.readInt();
                    //int childChunks = 
                    stream.readInt();
                    String chunkName = new String(chunkId, StandardCharsets.ISO_8859_1);
                    if (chunkName.equals("SIZE")) {
                        sizeX = stream.readInt();
                        sizeY = stream.readInt();
                        sizeZ = stream.readInt();
                        size = Math.max(sizeZ, Math.max(sizeX, sizeY));
                        offX = size - sizeX >> 1;
                        offY = size - sizeY >> 1;
                        voxelData = new byte[size][size][size];
                        stream.skipBytes(chunkSize - 4 * 3);
                    } else if (chunkName.equals("XYZI") && voxelData != null) {
                        // XYZI contains n voxels
                        int numVoxels = stream.readInt();

                        ShapeModel shp = shapes.get(model.grids.size());
                        if(shp == null) {
                            shp = new ShapeModel(model.grids.size(), new String[0][0]);
                            shapes.put(model.grids.size(), shp);
                        }
                        shp.minX = Integer.MAX_VALUE;
                        shp.minY = Integer.MAX_VALUE;
                        shp.minZ = Integer.MAX_VALUE;
                        shp.maxX = 0;
                        shp.maxY = 0;
                        shp.maxZ = 0;

                        IntObjectMap<float[]> linkage = new IntObjectMap<>(8);
                        IntObjectMap<LongOrderedSet> markers = new IntObjectMap<>(8);
                        // each voxel has x, y, z and color index values
                        for (int i = 0; i < numVoxels; i++) {
                            int x = stream.read() + offX;
                            int y = stream.read() + offY;
                            int z = stream.read();
                            byte color = stream.readByte();

                            shp.minX = Math.min(shp.minX, x);
                            shp.minY = Math.min(shp.minY, y);
                            shp.minZ = Math.min(shp.minZ, z);
                            shp.maxX = Math.max(shp.maxX, x);
                            shp.maxY = Math.max(shp.maxY, y);
                            shp.maxZ = Math.max(shp.maxZ, z);

                            //If you are using this as a general .vox parser, use the following line only:
                            if(GENERAL)
                                voxelData[x][y][z] = color;
                            else {
                                //If you are using this as Isonomicon's special parser, also use the following if/else:
                                if ((color & 0xC0) != 0xC0) {
                                    voxelData[x][y][z] = color;
                                } else // The last 64 indices are used for link markers, and don't show.
                                {
                                    float[] ln;
                                    if ((ln = linkage.get(color)) == null) {
                                        ln = new float[]{x, y, z, 1f};
                                    } else {
                                        ln[0] += x;
                                        ln[1] += y;
                                        ln[2] += z;
                                        ln[3]++;
                                    }
                                    linkage.put(color, ln);

                                    LongOrderedSet ls;
                                    if ((ls = markers.get(color & 255)) == null) {
                                        ls = new LongOrderedSet(16);
                                    }
                                    ls.add(((x & 0xFFFFFL) | (y & 0xFFFFFL) << 20 | (z & 0xFFFFFL) << 40) << 1);
                                    markers.put(color & 255, ls);
                                }
                            }
                        }
                        if(GENERAL) {
                            model.grids.add(voxelData);
                        }
                        else {
                            model.grids.add(Tools3D.scaleAndSoak(voxelData));
                            for (IntObjectMap.Entry<float[]> e : linkage) {
                                float div = 2f / e.value[3];
                                e.value[0] *= div;
                                e.value[1] *= div;
                                e.value[2] *= div;
                            }
                            model.links.add(linkage);
                            model.markers.add(markers);
                        }
                    } else if (chunkName.equals("RGBA")) {
                        for (int i = 1; i < 256; i++) {
                            lastPalette[i] = Integer.reverseBytes(stream.readInt());
                        }
                        System.arraycopy(lastPalette, 0, model.palette, 0, 256);
                        stream.readInt();
                    } else if(chunkName.equals("MATL")){ // remove this block if you don't handle materials
                        int materialID = stream.readInt();
                        int dictSize = stream.readInt();
                        for (int i = 0; i < dictSize; i++) {
                            int keyLen = stream.readInt();
                            stream.read(key, 0, keyLen);
                            int valLen = stream.readInt();
                            stream.read(val, 0, valLen);
                            VoxMaterial vm;
                            if ((vm = lastMaterials.getOrDefault(materialID, null)) == null) {
                                lastMaterials.put(materialID, vm = new VoxMaterial());
                            }
                            String ks = new String(key, 0, keyLen, StandardCharsets.ISO_8859_1);
                            String vs = new String(val, 0, valLen, StandardCharsets.ISO_8859_1);
                            vm.putTrait(ks, vs);
                        }
                    } else if (chunkName.equals("nTRN")) {
                        int chunkID = stream.readInt();
                        String[][] attributes = readStringPairs(stream);
                        int childID = stream.readInt();
                        int reservedID = stream.readInt();
                        int layerID = stream.readInt();
                        int frameCount = stream.readInt();
                        String[][][] frames = new String[frameCount][][];
                        for (int i = 0; i < frameCount; i++) {
                            frames[i] = readStringPairs(stream);
                        }
                        latest = new TransformChunk(chunkID, attributes, childID, reservedID, layerID, frames);
                        latest.translation.z -= sizeZ * 0.5f;
                        model.transformChunks.put(chunkID, latest);
                    } else if (chunkName.equals("nGRP")) {
                        int chunkID = stream.readInt();
                        String[][] attributes = readStringPairs(stream);
                        int childCount = stream.readInt();
                        int[] childIds = new int[childCount];
                        for (int i = 0; i < childCount; i++) {
                            try {
                                childIds[i] = stream.readInt();
                            } catch (Exception ignored) {}
                        }
                        model.groupChunks.put(chunkID, new GroupChunk(chunkID, attributes, childIds));
                    } else if (chunkName.equals("nSHP")) {
                        int chunkID = stream.readInt();
                        String[][] attributes = readStringPairs(stream);
                        int modelCount = stream.readInt();
                        ShapeModel[] models = new ShapeModel[modelCount];
                        for (int i = 0; i < modelCount; i++) {
                            int shapeID = stream.readInt();
                            String[][] ps = readStringPairs(stream);
                            if(shapes.containsKey(shapeID))
                                models[i] = shapes.get(shapeID);
                            else
                                models[i] = new ShapeModel(shapeID, ps);
                            models[i].offsetX = Math.round(latest.translation.x);
                            models[i].offsetY = Math.round(latest.translation.y);
                            models[i].offsetZ = Math.round(latest.translation.z);
                            minX = Math.min(minX, models[i].minX + models[i].offsetX);
                            minY = Math.min(minY, models[i].minY + models[i].offsetY);
                            minZ = Math.min(minZ, models[i].minZ + models[i].offsetZ);
                            maxX = Math.max(maxX, models[i].maxX + models[i].offsetX);
                            maxY = Math.max(maxY, models[i].maxY + models[i].offsetY);
                            maxZ = Math.max(maxZ, models[i].maxZ + models[i].offsetZ);
                        }
                        model.shapeChunks.put(chunkID, new ShapeChunk(chunkID, attributes, models));
                    } else stream.skipBytes(chunkSize);   // read any excess bytes
                }

            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(GENERAL) {
            for (byte[][][] grid : model.grids) {
                Tools3D.soakInPlace(grid, lastMaterials);
            }
        }
        model.materials.putAll(lastMaterials);
        return model;
    }

    public static byte[][][] mergeModel(VoxModel model) {
        int xChange = 0, yChange = 0;
        if(minX < 0) {
            xChange = -minX;
            maxX += xChange;
            minX = 0;
        }
        if(minY < 0) {
            yChange = -minY;
            maxY += yChange;
            minY = 0;
        }

        int size = 1;
        for (GroupChunk gc : model.groupChunks.values()) {
            for (int ch : gc.childIds) {
                TransformChunk tc = model.transformChunks.get(ch);
                if (tc != null) {
                    for (ShapeModel sm : model.shapeChunks.get(tc.childId).models) {
                        byte[][][] g = model.grids.get(sm.id);
                        size = Math.max(size, Math.round(tc.translation.x + g.length));
                        size = Math.max(size, Math.round(tc.translation.y + g[0].length));
                        size = Math.max(size, Math.round(tc.translation.z + g[0][0].length * 0.5f));
                    }
                }
            }
        }

        byte[][][] voxels = new byte[size][size][size];
        for (GroupChunk gc : model.groupChunks.values()) {
            for (int ch : gc.childIds) {
                TransformChunk tc = model.transformChunks.get(ch);
                if (tc != null) {
                    for (ShapeModel sm : model.shapeChunks.get(tc.childId).models) {
                        byte[][][] g = model.grids.get(sm.id);
                        Tools3D.translateCopyInto(g, voxels, Math.round(tc.translation.x + xChange), Math.round(tc.translation.y + yChange), Math.round(tc.translation.z));
                    }
                }
            }
        }
        return voxels;
    }

    private static void writeInt(DataOutputStream bin, int value) throws IOException
    {
        bin.writeInt(Integer.reverseBytes(value));
    }

    public static void writeVOX(String filename, byte[][][] voxelData, int[] palette) {
        writeVOX(filename, voxelData, palette, null);
    }

    public static void writeVOX(String filename, byte[][][] voxelData, int[] palette, IntObjectMap<VoxMaterial> materials) {
        // check out https://github.com/ephtracy/voxel-model/blob/master/MagicaVoxel-file-format-vox.txt for the file format used below
        try {
            int xSize = voxelData.length, ySize = voxelData[0].length, zSize = voxelData[0][0].length;
            Files.createDirectories(Path.of(filename).getParent());
            FileOutputStream fos = new FileOutputStream(filename);
            DataOutputStream bin = new DataOutputStream(fos);
            ByteArrayOutputStream voxelsRaw = new ByteArrayOutputStream(0);
            int cc;
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        cc = voxelData[x][y][z];
                        if(cc == 0) continue;
                        voxelsRaw.write(x);
                        voxelsRaw.write(y);
                        voxelsRaw.write(z);
                        voxelsRaw.write(cc);
                    }
                }
            }

            // a MagicaVoxel .vox file starts with a 'magic' 4 character 'VOX ' identifier
            bin.writeBytes("VOX ");
            // current version
            writeInt(bin, 150);

            bin.writeBytes("MAIN");
            writeInt(bin, 0);
            writeInt(bin, 12 + 12 + 12 + 4 + voxelsRaw.size() + 12 + 1024);

            bin.writeBytes("SIZE");
            writeInt(bin, 12);
            writeInt(bin, 0);
            writeInt(bin, xSize);
            writeInt(bin, ySize);
            writeInt(bin, zSize);

            bin.writeBytes("XYZI");
            writeInt(bin, 4 + voxelsRaw.size());
            writeInt(bin, 0);
            writeInt(bin, voxelsRaw.size() >> 2);
            bin.write(voxelsRaw.toByteArray());

            bin.writeBytes("RGBA");
            writeInt(bin, 1024);
            writeInt(bin, 0);
            int i = 1;
            for (; i < 256 && i < palette.length; i++) {
                bin.writeInt(palette[i]);
            }
            // if the palette is smaller than 256 colors, this fills the rest with lastPalette's colors
            for (; i < 256; i++) {
                bin.writeInt(lastPalette[i]);
            }
            writeInt(bin,  0);
            if(materials != null && materials.notEmpty()) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(128);
                DataOutputStream dos = new DataOutputStream(bytes);
                for(IntObjectMap.Entry<VoxMaterial> ent : materials) {
                    bin.writeBytes("MATL");
                    dos.flush();
                    bytes.reset();
                    // here we write to dos, which writes to bytes, so we know the length of the chunk.
                    writeInt(dos, ent.key);
                    writeInt(dos, ent.value.traits.size() + 1);
                    writeInt(dos, 5);
                    dos.writeBytes("_type");
                    String term = ent.value.type.name();
                    writeInt(dos, term.length());
                    dos.writeBytes(term);
                    PrimitiveIterator.OfInt it = ent.value.traits.keySet().iterator();
                    for(int k; it.hasNext();) {
                        k = it.nextInt();
                        if(k > 9) continue;
                        VoxMaterial.MaterialTrait mt = VoxMaterial.ALL_TRAITS[k];
                        float v = ent.value.traits.get(k);
                        term = mt.name();
                        writeInt(dos, term.length());
                        dos.writeBytes(term);
                        term = Float.toString(v);
                        if(term.length() > 8) term = term.substring(0, 8);
                        writeInt(dos, term.length());
                        dos.writeBytes(term);
                    }
                    writeInt(bin, bytes.size());
                    writeInt(bin, 0);
                    bytes.writeTo(bin);
                }
            }
            bin.flush();
            bin.close();
            fos.flush();
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
