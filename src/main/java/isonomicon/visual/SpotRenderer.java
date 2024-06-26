package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.ds.IntObjectMap;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.*;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.util.Arrays;

import static com.github.tommyettinger.colorful.oklab.ColorTools.getRawGamutValue;
import static com.github.tommyettinger.digital.ArrayTools.fill;
import static com.github.tommyettinger.digital.TrigTools.*;

/**
 * Renders {@code byte[][][]} voxel models to {@link Pixmap}s with arbitrary rotation; this version is from SpotVox.
 */
public class SpotRenderer {
    public Pixmap pixmap;
    public int[][] depths;
    public int[][] voxels;
    public int[][] outlines;
    public VoxMaterial[][] materials;
    public float[][] shadeX, shadeZ, colorL, colorA, colorB, midShading;
    public int[] palette;
    public float[] paletteL, paletteA, paletteB;
    public int outline = 2;
    public int size;
    public int shrink = 2;
    public float neutral = 1f;
    public IntObjectMap<VoxMaterial> materialMap;

    protected SpotRenderer() {

    }
    public SpotRenderer(final int size) {
        this.size = size;
    }

    public void init(){
        final int w = size * 4 + 4, h = size * 5 + 4;
        pixmap = new Pixmap(w>>>shrink, h>>>shrink, Pixmap.Format.RGBA8888);
        outlines = new int[w][h];
        depths =   new int[w][h];
        materials = new VoxMaterial[w][h];
        voxels = fill(-1, w, h);
        shadeX = fill(-1f, size * 4, size * 4);
        shadeZ = fill(-1f, size * 4, size * 4);
        colorL = fill(-1f, w, h);
        colorA = fill(-1f, w, h);
        colorB = fill(-1f, w, h);
        midShading = fill(0f, w, h);
    }
    public static float limitToGamut(float L, float A, float B, float alpha) {
        L = Math.min(Math.max(L, 0f), 1f);
        A = Math.min(Math.max(A, 0f), 1f);
        B = Math.min(Math.max(B, 0f), 1f);
        alpha = Math.min(Math.max(alpha, 0f), 1f);
        final float A2 = (A - 0.5f);
        final float B2 = (B - 0.5f);
        final float hue = atan2Turns(B2, A2);
        final int idx = (int) (L * 255.999f) << 8 | (int)(256f * hue);
        final float dist = getRawGamutValue(idx) * 0.5f;
        if(dist * dist * 0x1p-16f >= (A2 * A2 + B2 * B2))
            return ColorTools.oklab(L, A, B, alpha);
        return Float.intBitsToFloat(
                (int) (alpha * 127.999f) << 25 |
                        (int) (sinTurns(hue) * dist + 128f) << 16 |
                        (int) (cosTurns(hue) * dist + 128f) << 8 |
                        (int) (L * 255f));
    }

//    /**
//     * This one's weird; unlike {@link #atan2_(float, float)}, it can return negative results.
//     * @param v any finite float
//     * @return between -0.25 and 0.25
//     */
//    private static float atn_(final float v) {
//        final float n = Math.abs(v);
//        final float c = (n - 1f) / (n + 1f);
//        final float c2 = c * c;
//        final float c3 = c * c2;
//        final float c5 = c3 * c2;
//        final float c7 = c5 * c2;
//        return Math.copySign(0.125f + 0.1590300064615682f * c - 0.051117687016646825f * c3 + 0.02328064394867594f * c5
//                - 0.006205912780487965f * c7, v);
//    }
//
//    /**
//     * Altered-range approximation of the frequently-used trigonometric method atan2, taking y and x positions as floats
//     * and returning an angle measured in turns from 0.0f to 1.0f, with one cycle over the range equivalent to 360
//     * degrees or 2PI radians. You can multiply the angle by {@code 6.2831855f} to change to radians, or by {@code 360f}
//     * to change to degrees. Takes y and x (in that unusual order) as floats. Will never return a negative number, which
//     * may help avoid costly floating-point modulus when you actually want a positive number.
//     * <br>
//     * Credit for this goes to the 1955 research study "Approximations for Digital Computers," by RAND Corporation. This
//     * is sheet 9's algorithm, which is the second-fastest and second-least precise. The algorithm on sheet 8 is faster,
//     * but only by a very small degree, and is considerably less precise. That study provides an atan()
//     * method, and the small code to make that work as atan2_() was worked out from Wikipedia.
//     * @param y y-component of the point to find the angle towards; note the parameter order is unusual by convention
//     * @param x x-component of the point to find the angle towards; note the parameter order is unusual by convention
//     * @return the angle to the given point, as a float from 0.0f to 1.0f, inclusive
//     */
//    public static float atan2_(final float y, float x) {
//        float n = y / x;
//        if(n != n) n = (y == x ? 1f : -1f); // if both y and x are infinite, n would be NaN
//        else if(n - n != n - n) x = 0f; // if n is infinite, y is infinitely larger than x.
//        if(x > 0) {
//            if(y >= 0)
//                return atn_(n);
//            else
//                return atn_(n) + 1f;
//        }
//        else if(x < 0) {
//            return atn_(n) + 0.5f;
//        }
//        else if(y > 0) return x + 0.25f;
//        else if(y < 0) return x + 0.75f;
//        else return x + y; // returns 0 for 0,0 or NaN if either y or x is NaN
//    }
//
//    public static float sin_(float turns)
//    {
//        return MathUtils.sinDeg(turns * 360f);
//    }
//
//    public static float cos_(float turns)
//    {
//        return MathUtils.cosDeg(turns * 360f);
//    }

//    public static float sin_(float turns)
//    {
//        turns *= 4f;
//        final long floor = (turns >= 0.0 ? (long) turns : (long) turns - 1L) & -2L;
//        turns -= floor;
//        turns *= 2f - turns;
//        return turns * (-0.775f - 0.225f * turns) * ((floor & 2L) - 1L);
//    }
//
//    public static float cos_(float turns)
//    {
//        turns = turns * 4f + 1f;
//        final long floor = (turns >= 0.0 ? (long) turns : (long) turns - 1L) & -2L;
//        turns -= floor;
//        turns *= 2f - turns;
//        return turns * (-0.775f - 0.225f * turns) * ((floor & 2L) - 1L);
//    }

    /**
     * Takes a modifier between -1f and 0.5f, and adjusts how this changes saturation accordingly.
     * Negative modifiers will decrease saturation, while positive ones increase it. If positive, any
     * changes are incredibly sensitive, and 0.05 will seem very different from 0.0. If negative, changes
     * are not as sensitive, but most of the noticeable effect will happen close to -0.1.
     * @param saturationModifier a float between -1f and 0.5f; negative decreases saturation, positive increases
     * @return this, for chaining
     */
    public SpotRenderer saturation(float saturationModifier) {
        neutral = (1f + Math.min(Math.max(saturationModifier,  -1f),  0.5f));
        return this;
    }

    public int[] palette() {
        return palette;
    }

    public SpotRenderer palette(int[] color) {
        return palette(color, 256);
    }
    public SpotRenderer palette(int[] color, int count) {
        this.palette = color;
        count = Math.min(256, count);
        if(paletteL == null) paletteL = new float[256];
        if(paletteA == null) paletteA = new float[256];
        if(paletteB == null) paletteB = new float[256];
        for (int i = 0; i < color.length && i < count; i++) {
            if ((color[i] & 0x80) == 0) {
                paletteL[i] = -1f;
                paletteA[i] = -1f;
                paletteB[i] = -1f;
            } else {
                float lab = ColorTools.fromRGBA8888(color[i]);
                paletteL[i] = ColorTools.channelL(lab);
                paletteA[i] = ColorTools.channelA(lab);
                paletteB[i] = ColorTools.channelB(lab);
            }
        }
        return this;
    }
    
    public void splat(float xPos, float yPos, float zPos, int vx, int vy, int vz, byte voxel, int frame) {
        if(xPos <= -1f || yPos <= -1f || zPos <= -1f
                || xPos >= size * 2 || yPos >= size * 2 || zPos >= size * 2)
            return;
        final int 
                xx = (int)(0.5f + Math.max(0, (size + yPos - xPos) * 2 + 1)),
                yy = (int)(0.5f + Math.max(0, (zPos * 3 + size * 3 - xPos - yPos) + 1)),
                depth = (int)(0.5f + (xPos + yPos) * 2 + zPos * 3);
        boolean drawn = false;
        final VoxMaterial m = materialMap.get(voxel & 255);
        if(Tools3D.randomizePointRare(vx, vy, vz, frame) < m.getTrait(VoxMaterial.MaterialTrait._metal))
            return;
        final float emit = m.getTrait(VoxMaterial.MaterialTrait._emit) * 0.75f;
        final float alpha = m.getTrait(VoxMaterial.MaterialTrait._alpha);
        final float hs = size * 0.5f;
        for (int x = 0, ax = xx; x < 4 && ax < depths.length; x++, ax++) {
            for (int y = 0, ay = yy; y < 4 && ay < depths[0].length; y++, ay++) {
                if ((alpha == 0f) && (depth > depths[ax][ay] || (depth == depths[ax][ay] && colorL[ax][ay] < paletteL[voxel & 255]))) {
                    drawn = true;
                    colorL[ax][ay] = paletteL[voxel & 255];
                    colorA[ax][ay] = paletteA[voxel & 255];
                    colorB[ax][ay] = paletteB[voxel & 255];
                    depths[ax][ay] = depth;
                    materials[ax][ay] = m;
                    outlines[ax][ay] = ColorTools.toRGBA8888(limitToGamut(paletteL[voxel & 255] * (0.8f + emit),
                            (paletteA[voxel & 255] - 0.5f) * neutral + 0.5f, (paletteB[voxel & 255] - 0.5f) * neutral + 0.5f, 1f));
//                                Coloring.darken(palette[voxel & 255], 0.375f - emit);
//                                Coloring.adjust(palette[voxel & 255], 0.625f + emit, neutral);
//                    else
//                        outlines[ax][ay] = palette[voxel & 255];
                    voxels[ax][ay] = vx | vy << 10 | vz << 20;
//                    for (int xp = (int)xPos; xp < xPos + 0.5f; xp++) {
//                        for (int yp = (int) yPos; yp < yPos + 0.5f; yp++) {
//                            for (int zp = (int) zPos; zp < zPos + 0.5f; zp++) {
//                                remade[xp][yp][zp] = voxel;
//                            }
//                        }
//                    }
                }
            }
        }
        if(xPos < -hs || yPos < -hs || zPos < -hs || xPos + hs > shadeZ.length || yPos + hs > shadeZ[0].length || zPos + hs > shadeX[0].length)
            System.out.println(xPos + ", " + yPos + ", " + zPos + " is out of bounds");
        else if(drawn) {
            shadeZ[(int) (hs + xPos)][(int) (hs + yPos)] = Math.max(shadeZ[(int) (hs + xPos)][(int) (hs + yPos)], (hs + zPos));
            shadeX[(int) (hs + yPos)][(int) (hs + zPos)] = Math.max(shadeX[(int) (hs + yPos)][(int) (hs + zPos)], (hs + xPos));
        }
    }
    
    public SpotRenderer clear() {
        pixmap.setColor(0);
        pixmap.fill();
        fill(depths, 0);
        fill(outlines, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        fill(colorL, -1f);
        fill(colorA, -1f);
        fill(colorB, -1f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return this;
    }

    /**
     * Compiles all the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param turns yaw in turns; like turning your head or making a turn in a car
     * @return {@link #pixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float turns) {
        return blit(turns, 0f, 0f, 0);
    }

    /**
     * Compiles all of the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param yaw in turns; like turning your head or making a turn in a car
     * @param pitch in turns; like looking up or down or making a nosedive in a plane
     * @param roll in turns; like tilting your head to one side or doing a barrel roll in a starship
     * @return {@link #pixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float yaw, float pitch, float roll, int frame) {
        final int threshold = 13;
        pixmap.setColor(0);
        pixmap.fill();
        int xSize = depths.length - 1, ySize = depths[0].length - 1, depth;
        int v, vx, vy, vz, fx, fy, fz;
        float hs = (size) * 0.5f, ox, oy, oz, tx, ty, tz;
        final float cYaw = cosTurns(yaw), sYaw = sinTurns(yaw);
        final float cPitch = cosTurns(pitch), sPitch = sinTurns(pitch);
        final float cRoll = cosTurns(roll), sRoll = sinTurns(roll);
        final float x_x = cYaw * cPitch, y_x = cYaw * sPitch * sRoll - sYaw * cRoll, z_x = cYaw * sPitch * cRoll + sYaw * sRoll;
        final float x_y = sYaw * cPitch, y_y = sYaw * sPitch * sRoll + cYaw * cRoll, z_y = sYaw * sPitch * cRoll - cYaw * sRoll;
        final float x_z = -sPitch, y_z = cPitch * sRoll, z_z = cPitch * cRoll;
        VoxMaterial m;
        final int step = 1 << shrink;
        for (int sx = 0; sx <= xSize; sx++) {
            for (int sy = 0; sy <= ySize; sy++) {
                if((v = voxels[sx][sy]) != -1) {
                    vx = v & 0x3FF;
                    vy = v >>> 10 & 0x3FF;
                    vz = v >>> 20 & 0x3FF;
                    ox = vx - hs;
                    oy = vy - hs;
                    oz = vz - hs;
                    tx = ox * x_x + oy * y_x + oz * z_x + size + hs;
                    fx = (int)(tx);
                    ty = ox * x_y + oy * y_y + oz * z_y + size + hs;
                    fy = (int)(ty);
                    tz = ox * x_z + oy * y_z + oz * z_z + hs + hs;
                    fz = (int)(tz);
                    m = materials[sx][sy];
                    float rough = m.getTrait(VoxMaterial.MaterialTrait._rough);
                    float emit = m.getTrait(VoxMaterial.MaterialTrait._emit);
                    float limit = 2;
                    if (Math.abs(shadeX[fy][fz] - tx) <= limit || ((fy > 1 && Math.abs(shadeX[fy - 2][fz] - tx) <= limit) ||
                            (fy < shadeX.length - 2 && Math.abs(shadeX[fy + 2][fz] - tx) <= limit))) {
                        float spread = MathUtils.lerp(0.0025f, 0.001f, rough);
                        if (Math.abs(shadeZ[fx][fy] - tz) <= limit) {
                            spread *= 2f;
                            colorL[sx][sy] += m.getTrait(VoxMaterial.MaterialTrait._ior) * 0.2f;
                        }
                        int dist;
                        for (int i = -3, si = sx + i; i <= 3; i++, si++) {
                            for (int j = -3, sj = sy + j; j <= 3; j++, sj++) {
                                if((dist = i * i + j * j) > 9 || si < 0 || sj < 0 || si > xSize || sj > ySize) continue;
                                colorL[si][sj] += spread * (4 - (float)Math.sqrt(dist));
                            }
                        }
                    }
                    else if (Math.abs(shadeZ[fx][fy] - tz) <= limit) {
                        float spread = MathUtils.lerp(0.005f, 0.002f, rough);
                        float dist;
                        for (int i = -3, si = sx + i; i <= 3; i++, si++) {
                            for (int j = -3, sj = sy + j; j <= 3; j++, sj++) {
                                if((dist = i * i + j * j) > 9 || si < 0 || sj < 0 || si > xSize || sj > ySize) continue;
                                float change = spread * (4 - (float)Math.sqrt(dist));
                                colorL[si][sj] += change;
                            }
                        }
                    }
                    if (emit > 0) {
                        float spread = emit * 0.003f;
                        final int radius = 14;
                        for (int i = -radius, si = sx + i; i <= radius; i++, si++) {
                            for (int j = -radius, sj = sy + j; j <= radius; j++, sj++) {
                                final int dist = i * i + j * j;
                                if(dist > radius * radius || si < 0 || sj < 0 || si > xSize || sj > ySize) continue;
                                float change = spread * (radius - (float) Math.sqrt(dist));
                                midShading[si][sj] = Math.min(midShading[si][sj] + change, 0.3f);
                            }
                        }
                    }
                }
            }
        }
        for (int x = xSize; x >= 0; x--) {
            for (int y = ySize; y >= 0; y--) {
                if (colorA[x][y] >= 0f) {
                    pixmap.drawPixel(x >>> shrink, y >>> shrink, ColorTools.toRGBA8888(ColorTools.oklab(
                            Math.min(Math.max(colorL[x][y] - 0.1f + midShading[x][y], 0f), 1f),
                                                        (colorA[x][y] - 0.5f) * neutral + 0.5f,
                            (colorB[x][y] - 0.5f) * neutral + 0.5f, 1f)));
                }
            }
        }
        if (outline != 0) {
            int inner, outer = 0x000000FF;
            if(outline <= 1) outer = 0;
            for (int x = step; x <= xSize - step; x+= step) {
//                final int hx = x;
                final int hx = x >>> shrink;
                for (int y = step; y <= ySize - step; y+= step) {
//                    final int hy = y;
                    int hy = y >>> shrink;
                    inner = outlines[x][y];
                    if (inner != 0) {
                        if (outline == 2) outer = inner;
                        depth = depths[x][y];
                        if (outlines[x - step][y] == 0) {
                            pixmap.drawPixel(hx - 1, hy, outer);
                        } else if (depths[x - step][y] < depth - threshold) {
                            pixmap.drawPixel(hx - 1, hy, inner);
                        }
                        if (outlines[x + step][y] == 0) {
                            pixmap.drawPixel(hx + 1, hy, outer);
                        } else if (depths[x + step][y] < depth - threshold) {
                            pixmap.drawPixel(hx + 1, hy, inner);
                        }
                        if (outlines[x][y - step] == 0) {
                            pixmap.drawPixel(hx, hy - 1, outer);
                        } else if (depths[x][y - step] < depth - threshold) {
                            pixmap.drawPixel(hx, hy - 1, inner);
                        }
                        if (outlines[x][y + step] == 0) {
                            pixmap.drawPixel(hx, hy + 1, outer);
                        } else if (depths[x][y + step] < depth - threshold) {
                            pixmap.drawPixel(hx, hy + 1, inner);
                        }


                        if (outlines[x - step][y - step] == 0) {
                            pixmap.drawPixel(hx - 1, hy - 1, outer);
                        }
                        if (outlines[x + step][y - step] == 0) {
                            pixmap.drawPixel(hx + 1, hy - 1, outer);
                        }
                        if (outlines[x - step][y + step] == 0) {
                            pixmap.drawPixel(hx - 1, hy + 1, outer);
                        }
                        if (outlines[x + step][y + step] == 0) {
                            pixmap.drawPixel(hx + 1, hy + 1, outer);
                        }
                    }
                }
            }
        }

        fill(depths, 0);
        fill(outlines, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        fill(colorL, -1f);
        fill(colorA, -1f);
        fill(colorB, -1f);
        fill(midShading, 0f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return pixmap;
    }

    // To move one x+ in voxels is x + 2, y - 1 in pixels.
    // To move one x- in voxels is x - 2, y + 1 in pixels.
    // To move one y+ in voxels is x - 2, y - 1 in pixels.
    // To move one y- in voxels is x + 2, y + 1 in pixels.
    // To move one z+ in voxels is y + 3 in pixels.
    // To move one z- in voxels is y - 3 in pixels.

    public Pixmap drawSplats(byte[][][] colors, float angleTurns, int frame, IntObjectMap<VoxMaterial> materialMap) {
        this.materialMap = materialMap;
        final int size = colors.length;
        final float hs = (size) * 0.5f;
        final float c = cosTurns(angleTurns), s = sinTurns(angleTurns);
        for (int z = VoxIOExtended.minZ; z <= VoxIOExtended.maxZ; z++) {
            for (int x = VoxIOExtended.minX; x <= VoxIOExtended.maxX; x++) {
                for (int y = VoxIOExtended.minY; y <= VoxIOExtended.maxY; y++) {
                    final byte v = colors[x][y][z];
                    if(v != 0)
                    {
                        final float xPos = (x-hs) * c - (y-hs) * s + size;
                        final float yPos = (x-hs) * s + (y-hs) * c + size;
                        splat(xPos, yPos, z, x, y, z, v, frame);
                    }
                }
            }
        }
        return blit(angleTurns);
    }

    public void splatOnly(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                          float translateX, float translateY, float translateZ) {
        final int size = colors.length;
        final float hs = size * 0.5f;
        float ox, oy, oz; // offset x,y,z
        final float cYaw = cosTurns(yaw), sYaw = sinTurns(yaw);
        final float cPitch = cosTurns(pitch), sPitch = sinTurns(pitch);
        final float cRoll = cosTurns(roll), sRoll = sinTurns(roll);
        final float x_x = cYaw * cPitch, y_x = cYaw * sPitch * sRoll - sYaw * cRoll, z_x = cYaw * sPitch * cRoll + sYaw * sRoll;
        final float x_y = sYaw * cPitch, y_y = sYaw * sPitch * sRoll + cYaw * cRoll, z_y = sYaw * sPitch * cRoll - cYaw * sRoll;
        final float x_z = -sPitch, y_z = cPitch * sRoll, z_z = cPitch * cRoll;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if (v != 0) {
                        ox = x - hs + translateX;
                        oy = y - hs + translateY;
                        oz = z - hs + translateZ;
                        splat(  ox * x_x + oy * y_x + oz * z_x + size,
                                ox * x_y + oy * y_y + oz * z_y + size,
                                ox * x_z + oy * y_z + oz * z_z + hs  , x, y, z, v, frame);
                    }
                }
            }
        }
    }

    public Pixmap drawModel(VoxModel model, float yaw, float pitch, float roll, int frame,
                            float translateX, float translateY, float translateZ){
        materialMap = VoxIO.lastMaterials;
        for(GroupChunk gc : model.groupChunks.values()) {
            for(int ch : gc.childIds) {
                TransformChunk tc = model.transformChunks.get(ch);
                if (tc != null) {
                    for (ShapeModel sm : model.shapeChunks.get(tc.childId).models) {
                        byte[][][] g = model.grids.get(sm.id);
                        VoxIOExtended.minX = sm.minX;
                        VoxIOExtended.maxX = sm.maxX;
                        VoxIOExtended.minY = sm.minY;
                        VoxIOExtended.maxY = sm.maxY;
                        VoxIOExtended.minZ = sm.minZ;
                        VoxIOExtended.maxZ = sm.maxZ;
                        splatOnly(g, yaw, pitch, roll, frame,
                                translateX + tc.translation.x,// - g.length * 0.5f,
                                translateY + tc.translation.y,// - g[0].length * 0.5f,
                                translateZ + tc.translation.z - g[0][0].length * 0.5f
                        );
                    }
                }
            }
        }
        return blit(yaw, pitch, roll, frame);
    }

    public Pixmap drawSplats(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                             float translateX, float translateY, float translateZ,
                             IntObjectMap<VoxMaterial> materialMap) {
        this.materialMap = materialMap;
        splatOnly(colors, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        return blit(yaw, pitch, roll, frame);
    }
}
