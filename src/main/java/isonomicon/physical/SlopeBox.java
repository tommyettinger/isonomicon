package isonomicon.physical;

import com.badlogic.gdx.graphics.Pixmap;
import isonomicon.visual.VoxelPixmapRenderer;

import java.util.Arrays;

public class SlopeBox {
    public static final byte[] CW = new byte[256];
    public static final byte[] FLIP = new byte[256];
    public static final byte[] CCW = new byte[256];

    public byte[][][][] data;

    public SlopeBox()
    {
        this(new byte[32][32][32]);
    }
    
    public SlopeBox(byte[][][] colors)
    {
        data = new byte[2][][][];
        data[0] = colors;
        data[1] = new byte[colors.length][colors[0].length][colors[0][0].length];
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                for (int z = 0; z < colors[0][0].length; z++) {
                    if(data[0][x][y][z] != 0) data[1][x][y][z] = -1;
                }
            }
        }
        putSlopes();
    }

    public int sizeX(){
        return data[0].length;
    }

    public int sizeY(){
        return data[0][0].length;
    }

    public int sizeZ(){
        return data[0][0][0].length;
    }

    public byte color(int x, int y, int z)
    {
        if((x|y|z) < 0 || x >= sizeX() || y >= sizeY() || z >= sizeZ())
            return 0;
        return data[0][x][y][z];
    }

    public byte slope(int x, int y, int z)
    {
        if((x|y|z) < 0 || x >= sizeX() || y >= sizeY() || z >= sizeZ() || data[0][x][y][z] == 0)
            return 0;
        return data[1][x][y][z];
    }

    public SlopeBox set(int x, int y, int z, int color, int slope)
    {
        if(!((x|y|z) < 0 || x >= sizeX() || y >= sizeY() || z >= sizeZ()))
        {
            data[0][x][y][z] = (byte)color;
            data[1][x][y][z] = (byte)slope;
        }
        return this;
    }

    public SlopeBox setColor(int x, int y, int z, int color)
    {
        if(!((x|y|z) < 0 || x >= sizeX() || y >= sizeY() || z >= sizeZ()))
        {
            data[0][x][y][z] = (byte)color;
            if(data[1][x][y][z] == 0) data[1][x][y][z] = -1;
        }
        return this;
    }

    public SlopeBox setSlope(int x, int y, int z, int slope)
    {
        if(!((x|y|z) < 0 || x >= sizeX() || y >= sizeY() || z >= sizeZ()) && data[0][x][y][z] != 0)
        {
            data[1][x][y][z] = (byte)slope;
        }
        return this;
    }
    
    public SlopeBox clear(){
        Tools3D.fill(data[0], 0);
        Tools3D.fill(data[1], 0);
        return this;
    }
    
    public SlopeBox putSlopes(){
//        BitSet usedSlopes = new BitSet(256);
        final int limitX = sizeX() - 1;
        final int limitY = sizeY() - 1;
        final int limitZ = sizeZ() - 1;
        byte[][][] nextColors = new byte[limitX+1][limitY+1][limitZ+1];
        byte[][][] nextSlopes = new byte[limitX+1][limitY+1][limitZ+1];
        final int[] neighbors = new int[6];
        for (int x = 0; x <= limitX; x++) {
            for (int y = 0; y <= limitY; y++) {
                PER_CELL:
                for (int z = 0; z <= limitZ; z++) {
                    if(data[0][x][y][z] == 0)
                    {
                        int slope = 0;
                        if((neighbors[0] = x == 0 ? 0 : (data[0][x-1][y][z] & 255)) != 0) slope      |= 0x55;
                        if((neighbors[1] = y == 0 ? 0 : (data[0][x][y-1][z] & 255)) != 0) slope      |= 0x33;
                        if((neighbors[2] = z == 0 ? 0 : (data[0][x][y][z-1] & 255)) != 0) slope      |= 0x0F;
                        if((neighbors[3] = x == limitX ? 0 : (data[0][x+1][y][z] & 255)) != 0) slope |= 0xAA;
                        if((neighbors[4] = y == limitY ? 0 : (data[0][x][y+1][z] & 255)) != 0) slope |= 0xCC;
                        if((neighbors[5] = z == limitZ ? 0 : (data[0][x][y][z+1] & 255)) != 0) slope |= 0xF0;
                        if(Integer.bitCount(slope) < 5) // surrounded by empty or next to only one voxel
                        {
                            nextSlopes[x][y][z] = 0;
                            continue;
                        }
                        int bestIndex = -1;
                        for (int i = 0; i < 6; i++) {
                            if(neighbors[i] == 0) continue;
                            if(bestIndex == -1) bestIndex = i;
                            for (int j = i + 1; j < 6; j++) {
                                if(neighbors[i] == neighbors[j]){
                                    if((i == bestIndex || j == bestIndex) && neighbors[bestIndex] != 0) {
                                        nextColors[x][y][z] = (byte) neighbors[bestIndex];
                                        nextSlopes[x][y][z] = (byte) slope;
//                                        usedSlopes.set(slope);
                                        continue PER_CELL;
                                    }
                                } else if(neighbors[bestIndex] < neighbors[i]) {
                                    bestIndex = i;
                                }
                            }
                        }
                        nextColors[x][y][z] = (byte) neighbors[bestIndex];
                        nextSlopes[x][y][z] = (byte) slope;
//                        usedSlopes.set(slope);
                    }
                    else
                    {
                        nextColors[x][y][z] = data[0][x][y][z];
                        nextSlopes[x][y][z] = -1;
//                        usedSlopes.set(255);
                    }
                }
            }
        }
//        for (int i = usedSlopes.nextSetBit(0), w = 0; i < 256 && i >= 0; i = usedSlopes.nextSetBit(i+1)) {
//            System.out.printf("%02X, ", i);
//            if((++w & 7) == 0) System.out.println();
//        }
//        System.out.println("\n");
        for (int x = 0; x <= limitX; x++) {
            for (int y = 0; y <= limitY; y++) {
                PER_CELL:
                for (int z = 0; z <= limitZ; z++) {
                    if(nextColors[x][y][z] == 0)
                    {
                        int slope = 0;
                        if((neighbors[0] = x == 0 ? 0 : (nextColors[x-1][y][z] & 255)) != 0 && (nextSlopes[x-1][y][z] & 0xAA) != 0xAA) slope      |= (nextSlopes[x-1][y][z] & 0xAA) >>> 1;
                        if((neighbors[1] = y == 0 ? 0 : (nextColors[x][y-1][z] & 255)) != 0 && (nextSlopes[x][y-1][z] & 0xCC) != 0xCC) slope      |= (nextSlopes[x][y-1][z] & 0xCC) >>> 2;
                        if((neighbors[2] = z == 0 ? 0 : (nextColors[x][y][z-1] & 255)) != 0 && (nextSlopes[x][y][z-1] & 0xF0) != 0xF0) slope      |= (nextSlopes[x][y][z-1] & 0xF0) >>> 4;
                        if((neighbors[3] = x == limitX ? 0 : (nextColors[x+1][y][z] & 255)) != 0 && (nextSlopes[x+1][y][z] & 0x55) != 0x55) slope |= (nextSlopes[x+1][y][z] & 0x55) << 1;
                        if((neighbors[4] = y == limitY ? 0 : (nextColors[x][y+1][z] & 255)) != 0 && (nextSlopes[x][y+1][z] & 0x33) != 0x33) slope |= (nextSlopes[x][y+1][z] & 0x33) << 2;
                        if((neighbors[5] = z == limitZ ? 0 : (nextColors[x][y][z+1] & 255)) != 0 && (nextSlopes[x][y][z+1] & 0x0F) != 0x0F) slope |= (nextSlopes[x][y][z+1] & 0x0F) << 4;
                        if(Integer.bitCount(slope) < 4) // surrounded by empty or only one partial face
                        {
                            data[1][x][y][z] = 0;
                            continue;
                        }
                        int bestIndex = -1;
                        for (int i = 0; i < 6; i++) {
                            if(neighbors[i] == 0) continue;
                            if(bestIndex == -1) bestIndex = i;
                            for (int j = i + 1; j < 6; j++) {
                                if(neighbors[i] == neighbors[j]){
                                    if((i == bestIndex || j == bestIndex) && neighbors[bestIndex] != 0) {
                                        data[0][x][y][z] = (byte) neighbors[bestIndex];
                                        data[1][x][y][z] = (byte)slope;
//                                        usedSlopes.set(slope & 255);
                                        continue PER_CELL;
                                    }
                                } else if(neighbors[bestIndex] < neighbors[i]) {
                                    bestIndex = i;
                                }
                            }
                        }
                        data[0][x][y][z] = (byte) neighbors[bestIndex];
                        data[1][x][y][z] = (byte)slope;
//                        usedSlopes.set(slope & 255);
                    }
                    else
                    {
                        data[1][x][y][z] = (data[0][x][y][z] = nextColors[x][y][z]) == 0 ? 0 : nextSlopes[x][y][z];
//                        usedSlopes.set(nextSlopes[x][y][z] & 255);
                    }
                }
            }
        }
//        System.out.println(Tools3D.show(data[1]));
//        System.out.println(usedSlopes.get(0x8E));
//        for (int i = usedSlopes.nextSetBit(0), w = 0; i < 256 && i >= 0; i = usedSlopes.nextSetBit(i+1)) {
//            System.out.printf("%02X, ", i);
//            if((++w & 7) == 0) System.out.println();
//        }
//        System.out.println("\n");

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlopeBox slopeBox = (SlopeBox) o;

        return Arrays.deepEquals(data, slopeBox.data);
    }

    @Override
    public int hashCode() {
        return Tools3D.hash(data[0]) * 421 ^ Tools3D.hash(data[1]) * 307;
    }
    public long hash64() {
        return Tools3D.hash64(data[0]) * 0xBBE0563303A4615FL ^ Tools3D.hash64(data[1]) * 0xC6D1D6C8ED0C9631L;
    }

    /**
     * One shape per slope type, where a shape is a 4x4 grid of doubles ranging from 0.0 (dark) to 4.0 (white) or 7+ for transparent.
     * Cells in a shape can be between integer values, like 1.5, which will dither between 1 (dim) and 2 (normal).
     */
    public static final double[][] SHAPES = new double[256][16];
    /**
     * One side per slope type, where a side is a 4x4 grid of ints that denote what side information part of a shape has (see {@link #SHAPES}).
     * 0 denotes transparent, 1 denotes non-facing (whatever the color is, flipping doesn't change it), 2 denotes the brightly-lit side
     * (the left of the image, before flipping), and 3 denotes the dimly-lit side (the right, before flipping).
     */
    public static final int[][] SIDES = new int[256][16];
    ///////////////////////////////////////  0    1    2    3    4    5    6    7    8    9    A    B    C    D    E    F
    private static final int[] CW_SMALL = {0x0, 0x2, 0x8, 0xA, 0x1, 0x3, 0x9, 0xB, 0x4, 0x6, 0xC, 0xE, 0x5, 0x7, 0xD, 0xF};
    static {
        for (int outer = 0, i = 0, o; outer < 16; outer++) {
            o = CW_SMALL[outer] << 4;
            for (int inner = 0; inner < 16; inner++) {
                CW[i++] = (byte)(o | CW_SMALL[inner]);
            }
        }
        for (int i = 0; i < 256; i++) {
            FLIP[i] = CW[CW[i]&255];
            CCW[i] = CW[CW[CW[i]&255]&255];
        }
        //System.out.printf("0x%02X", CW[0x7F]);
        Arrays.fill(SHAPES, new double[]{ // all default to being empty
            7.0,7.0,7.0,7.0,
            7.0,7.0,7.0,7.0,
            7.0,7.0,7.0,7.0,
            7.0,7.0,7.0,7.0});
        // wedges with two sides solid
        SHAPES[0x77] = new double[]{ //wedge facing camera
                1.5,1.5,1.5,1.5,
                1.5,1.5,1.5,1.5,
                1.5,1.5,1.5,1.5,
                1.5,1.5,1.5,1.5};
        SHAPES[0xBB] = new double[]{ //wedge missing left half
                7.3,7.3,3.0,3.0,
                7.3,7.3,1.0,1.0,
                7.3,7.3,1.0,1.0,
                7.3,7.3,1.0,1.0};
        SHAPES[0xDD] = new double[]{ //wedge missing right half
                3.0,3.0,7.3,7.3,
                2.0,2.0,7.3,7.3,
                2.0,2.0,7.3,7.3,
                2.0,2.0,7.3,7.3};
        SHAPES[0xEE] = new double[]{ //wedge missing back; usually doesn't render
                7.3,7.3,7.3,7.3,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};

        // ramps with the bottom solid
        SHAPES[0x3F] = new double[]{ // ramp from front left up to back right
                7.3,7.3,2.6,2.6,
                7.3,2.6,2.6,2.6,
                2.6,2.6,2.6,1.0,
                2.6,2.6,2.6,1.0};
        SHAPES[0x5F] = new double[]{ // ramp from front right up to back left
                1.4,1.4,7.3,7.3,
                1.4,1.4,1.4,7.3,
                2.0,1.4,1.4,1.4,
                2.0,1.4,1.4,1.4};
        SHAPES[0xAF] = new double[]{ // ramp from back left up to front right
                7.3,7.3,7.3,7.3,
                7.3,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                7.3,2.0,1.0,1.0};
        SHAPES[0xCF] = new double[]{ // ramp from back right up to front left
                7.3,7.3,7.3,7.3,
                2.0,2.0,1.0,7.3,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,7.3};

        // ramps with the top solid
        SHAPES[0xF3] = new double[]{ // ramp from front left down to back right
                3.0,3.0,3.0,3.0,
                7.3,7.3,1.0,1.0,
                7.3,7.3,7.3,1.0,
                7.3,7.3,7.3,7.3};
        SHAPES[0xF5] = new double[]{ // ramp from front right down to back left
                3.0,3.0,3.0,3.0,
                2.0,2.0,7.3,7.3,
                2.0,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xFA] = new double[]{ // ramp from back left down to front right
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                7.3,2.0,1.0,1.0,
                7.3,7.3,1.0,1.0};
        SHAPES[0xFC] = new double[]{ // ramp from back right down to front left
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,7.3,
                2.0,2.0,7.3,7.3};
        // small slopes with four vertices truncated
        SHAPES[0x17] = new double[]{ // small slope centered on back bottom center corner
                7.3,7.3,7.3,7.3,
                7.3,2.4,2.4,7.3,
                2.4,2.4,2.4,2.4,
                2.4,2.4,2.4,2.4};
        SHAPES[0x2B] = new double[]{ // small slope centered on bottom right corner
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,1.0,
                7.3,7.3,1.0,1.0,
                7.3,7.3,1.0,1.0};
        SHAPES[0x4D] = new double[]{ // small slope centered on bottom left corner
                7.3,7.3,7.3,7.3,
                2.0,7.3,7.3,7.3,
                2.0,2.0,7.3,7.3,
                2.0,2.0,7.3,7.3};
        SHAPES[0x8E] = new double[]{ // small slope centered on front bottom center corner
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3,
                7.3,2.0,1.0,7.3,
                2.0,2.0,1.0,1.0};
        SHAPES[0x71] = new double[]{ // small slope centered on back top center corner
                3.0,3.0,3.0,3.0,
                7.3,0.6,0.6,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xB2] = new double[]{ // small slope centered on top right corner
                7.3,7.3,3.0,3.0,
                7.3,7.3,7.3,0.6,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xD4] = new double[]{ // small slope centered on top left corner
                3.0,3.0,7.3,7.3,
                0.6,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xE8] = new double[]{ // small slope centered on front top center corner
                3.0,3.0,3.0,3.0,
                7.3,2.0,1.0,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        // lopsided pyramids with 4 vertices on the bottom and one on top
        SHAPES[0x1F] = new double[]{ // all of bottom to top back center corner
                7.3,7.3,7.3,7.3,
                7.3,2.4,2.4,7.3,
                2.4,2.4,2.4,2.4,
                2.4,2.4,2.4,2.4};
        SHAPES[0x2F] = new double[]{ // all of bottom to top right corner
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,1.0,
                7.3,7.3,1.0,1.0,
                7.3,7.3,1.0,1.0};
        SHAPES[0x4F] = new double[]{ // all of bottom to top left corner
                7.3,7.3,7.3,7.3,
                2.0,7.3,7.3,7.3,
                2.0,2.0,7.3,7.3,
                2.0,2.0,7.3,7.3};
        SHAPES[0x8F] = new double[]{ // all of bottom to top front center corner
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3,
                7.3,2.0,1.0,7.3,
                2.0,2.0,1.0,1.0};
        // lopsided pyramids with 4 vertices on the top and one on the bottom
        SHAPES[0xF1] = new double[]{ // all of top to bottom back center corner
                3.0,3.0,3.0,3.0,
                7.3,0.6,0.6,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xF2] = new double[]{ // all of top to bottom right corner
                3.0,3.0,3.0,3.0,
                7.3,7.3,0.6,0.6,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xF4] = new double[]{ // all of top to bottom left corner
                3.0,3.0,3.0,3.0,
                0.6,0.6,7.3,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        SHAPES[0xF8] = new double[]{ // all of top to bottom front center corner
                3.0,3.0,3.0,3.0,
                7.3,2.0,1.0,7.3,
                7.3,7.3,7.3,7.3,
                7.3,7.3,7.3,7.3};
        // big slopes with one vertex truncated
        SHAPES[0x7F] = new double[]{ // full block minus top front center corner
                3.0,2.4,2.4,3.0,
                2.4,2.4,2.4,2.4,
                2.0,2.4,2.4,1.0,
                2.0,2.0,1.0,1.0};
        SHAPES[0xBF] = new double[]{ // full block minus top left corner
                7.3,3.0,3.0,3.0,
                7.3,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};
        SHAPES[0xDF] = new double[]{ // full block minus top right corner
                3.0,3.0,3.0,7.3,
                2.0,2.0,1.0,7.3,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};
        SHAPES[0xEF] = new double[]{ // full block minus top back center corner
                7.3,7.3,7.3,7.3,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};
        SHAPES[0xF7] = new double[]{ // full block minus bottom front center corner
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                0.6,0.6,0.6,0.6};
        SHAPES[0xFB] = new double[]{ // full block minus bottom left corner
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                0.6,2.0,1.0,1.0,
                7.3,0.6,1.0,1.0};
        SHAPES[0xFD] = new double[]{ // full block minus bottom right corner
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,0.6,
                2.0,2.0,0.6,7.3};
        SHAPES[0xFE] = new double[]{ // full block minus bottom back center corner; the same as full
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};
        // the biggest one
        SHAPES[0xFF] = new double[]{ // standard full block
                3.0,3.0,3.0,3.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0,
                2.0,2.0,1.0,1.0};

        // side info for flip-rotation (to save on atlas space)
//        Arrays.fill(SIDES, new int[]{ // all default to being empty
//            0,0,0,0,
//            0,0,0,0,
//            0,0,0,0,
//            0,0,0,0});
        // wedges with two sides solid
        SIDES[0x77] = new int[]{ //wedge facing camera
            1,1,1,1,
            1,1,1,1,
            1,1,1,1,
            1,1,1,1};
        SIDES[0xBB] = new int[]{ //wedge missing left half
            0,0,1,1,
            0,0,3,3,
            0,0,3,3,
            0,0,3,3};
        SIDES[0xDD] = new int[]{ //wedge missing right half
            1,1,0,0,
            2,2,0,0,
            2,2,0,0,
            2,2,0,0};
        SIDES[0xEE] = new int[]{ //wedge missing back; usually doesn't render
            0,0,0,0,
            2,2,3,3,
            2,2,3,3,
            2,2,3,3};

        // ramps with the bottom solid
        SIDES[0x3F] = new int[]{ // ramp from front left up to back right
            0,0,2,2,
            0,2,2,2,
            2,2,2,3,
            2,2,2,3};
        SIDES[0x5F] = new int[]{ // ramp from front right up to back left
            3,3,0,0,
            3,3,3,0,
            2,3,3,3,
            2,3,3,3};
        SIDES[0xAF] = new int[]{ // ramp from back left up to front right
            0,0,0,0,
            0,2,3,3,
            2,2,3,3,
            0,2,3,3};
        SIDES[0xCF] = new int[]{ // ramp from back right up to front left
            0,0,0,0,
            2,2,3,0,
            2,2,3,3,
            2,2,3,0};

        // ramps with the top solid
        SIDES[0xF3] = new int[]{ // ramp from front left down to back right
            1,1,1,1,
            0,0,3,3,
            0,0,0,3,
            0,0,0,0};
        SIDES[0xF5] = new int[]{ // ramp from front right down to back left
            1,1,1,1,
            2,2,0,0,
            2,0,0,0,
            0,0,0,0};
        SIDES[0xFA] = new int[]{ // ramp from back left down to front right
            1,1,1,1,
            2,2,3,3,
            0,2,3,3,
            0,0,3,3};
        SIDES[0xFC] = new int[]{ // ramp from back right down to front left
            1,1,1,1,
            2,2,3,3,
            2,2,3,0,
            2,2,0,0};
        // small slopes with four vertices truncated
        SIDES[0x17] = new int[]{ // small slope centered on back bottom center corner
            0,0,0,0,
            0,1,1,0,
            1,1,1,1,
            1,1,1,1
        };
        SIDES[0x2B] = new int[]{ // small slope centered on bottom right corner
            0,0,0,0,
            0,0,0,3,
            0,0,3,3,
            0,0,3,3};
        SIDES[0x4D] = new int[]{ // small slope centered on bottom left corner
            0,0,0,0,
            2,0,0,0,
            2,2,0,0,
            2,2,0,0};
        SIDES[0x8E] = new int[]{ // small slope centered on front bottom center corner
            0,0,0,0,
            0,0,0,0,
            0,2,3,0,
            2,2,3,3};
        SIDES[0x71] = new int[]{ // small slope centered on back top center corner
            1,1,1,1,
            0,1,1,0,
            0,0,0,0,
            0,0,0,0};
        SIDES[0xB2] = new int[]{ // small slope centered on top right corner
            0,0,1,1,
            0,0,0,1,
            0,0,0,0,
            0,0,0,0};
        SIDES[0xD4] = new int[]{ // small slope centered on top left corner
            1,1,0,0,
            1,0,0,0,
            0,0,0,0,
            0,0,0,0};
        SIDES[0xE8] = new int[]{ // small slope centered on front top center corner
            1,1,1,1,
            0,2,3,0,
            0,0,0,0,
            0,0,0,0};
        // big slopes with one vertex truncated
        SIDES[0x7F] = new int[]{ // full block minus top front center corner
            1,1,1,1,
            1,1,1,1,
            2,1,1,3,
            2,2,3,3};
        SIDES[0xBF] = new int[]{ // full block minus top left corner
            0,1,1,1,
            0,2,3,3,
            2,2,3,3,
            2,2,3,3};
        SIDES[0xDF] = new int[]{ // full block minus top right corner
            1,1,1,0,
            2,2,3,0,
            2,2,3,3,
            2,2,3,3};
        SIDES[0xEF] = new int[]{ // full block minus top back center corner
            0,0,0,0,
            2,2,3,3,
            2,2,3,3,
            2,2,3,3};
        SIDES[0xF7] = new int[]{ // full block minus bottom front center corner
            1,1,1,1,
            2,2,3,3,
            2,2,3,3,
            1,1,1,1};
        SIDES[0xFB] = new int[]{ // full block minus bottom left corner
            1,1,1,1,
            2,2,3,3,
            1,2,3,3,
            0,1,3,3};
        SIDES[0xFD] = new int[]{ // full block minus bottom right corner
            1,1,1,1,
            2,2,3,3,
            2,2,3,1,
            2,2,1,0};
        SIDES[0xFE] = new int[]{ // full block minus bottom back center corner; the same as full
            1,1,1,1,
            2,2,3,3,
            2,2,3,3,
            2,2,3,3};
        // the biggest one
        SIDES[0xFF] = new int[]{ // standard full block
            1,1,1,1,
            2,2,3,3,
            2,2,3,3,
            2,2,3,3};

    }
    
    public SlopeBox clockwise(){
        final int sizeXY = sizeX()-1, sizeZ = sizeZ()-1, halfSizeXYOdd = sizeXY+2>> 1, halfSizeXYEven = sizeXY+1>>1;
        byte c, s;
//        System.out.println("BEFORE:\n"+Tools3D.show(data[1]));
        for (int z = 0; z <= sizeZ; z++) {
            for (int x = 0; x < halfSizeXYOdd; x++) {
                for (int y = 0; y < halfSizeXYEven; y++) {

                    c = data[0][x][y][z];
                    data[0][x][y][z] = data[0][y][sizeXY - x][z];
                    data[0][y][sizeXY - x][z] = data[0][sizeXY - x][sizeXY - y][z];
                    data[0][sizeXY - x][sizeXY - y][z] = data[0][sizeXY - y][x][z];
                    data[0][sizeXY - y][x][z] = c;
                    s = data[1][x][y][z];
                    data[1][x][y][z] = CW[255 & data[1][y][sizeXY - x][z]];
                    data[1][y][sizeXY - x][z] = CW[255 & data[1][sizeXY - x][sizeXY - y][z]];
                    data[1][sizeXY - x][sizeXY - y][z] = CW[255 & data[1][sizeXY - y][x][z]];
                    data[1][sizeXY - y][x][z] = CW[255 & s];
                }
            }
        }
//        System.out.println("AFTER:\n"+Tools3D.show(data[1]));
        return this;
    }

    public static Pixmap drawIso(SlopeBox seq, VoxelPixmapRenderer renderer) {
        // To move one x+ in voxels is x + 2, y - 1 in pixels.
        // To move one x- in voxels is x - 2, y + 1 in pixels.
        // To move one y+ in voxels is x - 2, y - 1 in pixels.
        // To move one y- in voxels is x + 2, y + 1 in pixels.
        // To move one z+ in voxels is y + 3 in pixels.
        // To move one z- in voxels is y - 3 in pixels.
        final int sizeXY = seq.sizeX(), sizeZ = seq.sizeZ(),
                pixelWidth = sizeXY * 4 + 2,
                pixelHeight = sizeXY * 2 + seq.sizeZ() * 3 + 2;
        for (int z = 0; z < sizeZ; z++) {
            for (int x = 0; x < sizeXY; x++) {
                for (int y = 0; y < sizeXY; y++) {
                    final byte v = seq.color(x, y, z);
                    if(v == 0) continue;
                    final int xPos = (sizeXY + y - x) * 2 - 1,
                            yPos = (z * 3 + sizeXY + sizeXY - x - y) - 1,
                            dep = (x + y + z * 2) * 2 + 256;
                    renderer.select(xPos, yPos, v, SHAPES[CW[255&seq.data[1][x][y][z]] & 255], dep);
                }
            }
        }
        return renderer.blit(12, pixelWidth, pixelHeight);
    }
    
    public static final double[] SPLAT = new double[]{ // standard full block
           7.3,7.3,7.3,7.3,
           7.3,2.0,2.0,7.3,
           7.3,2.0,2.0,7.3,
           7.3,2.0,2.0,7.3};

    public static Pixmap drawSplats(SlopeBox seq, VoxelPixmapRenderer renderer) {
        // To move one x+ in voxels is x + 2, y - 1 in pixels.
        // To move one x- in voxels is x - 2, y + 1 in pixels.
        // To move one y+ in voxels is x - 2, y - 1 in pixels.
        // To move one y- in voxels is x + 2, y + 1 in pixels.
        // To move one z+ in voxels is y + 2 in pixels.
        // To move one z- in voxels is y - 2 in pixels.
        final int sizeXY = seq.sizeX(), sizeZ = seq.sizeZ(),
                pixelWidth = sizeXY * 4,
                pixelHeight = sizeXY * 2 + seq.sizeZ() * 3;
        for (int z = 0; z < sizeZ; z++) {
            for (int x = 0; x < sizeXY; x++) {
                for (int y = 0; y < sizeXY; y++) {
                    final byte v = seq.color(x, y, z);
                    if(v == 0) continue;
                    final int xPos = (sizeXY + y - x) * 2 - 1,
                            yPos = (z * 3 + sizeXY + sizeXY - x - y) - 1,
                            dep = (x + y + z * 3) * 2 + 256;
                    renderer.select(xPos, yPos, v, SPLAT, dep);
                }
            }
        }
        return renderer.blit(12, pixelWidth, pixelHeight);
    }
}
