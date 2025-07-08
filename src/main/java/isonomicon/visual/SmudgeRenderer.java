package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.ds.IntObjectMap;
import isonomicon.app.AppConfig;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.*;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.util.Arrays;

import static com.github.tommyettinger.colorful.oklab.ColorTools.getRawGamutValue;
import static com.github.tommyettinger.digital.ArrayTools.fill;
import static com.github.tommyettinger.digital.TrigTools.*;

/**
 * Renders {@code byte[][][]} voxel models to {@link Pixmap}s with arbitrary yaw rotation.
 */
public class SmudgeRenderer {
    public Pixmap pixmap;
    public int[][] depths, voxels, render, outlines;
    public VoxMaterial[][] materials;
    public float[][] shadeX, shadeZ, colorL, colorA, colorB, midShading;
    public PaletteReducer reducer = new com.github.tommyettinger.anim8.QualityPalette();
    public int[] palette;
    public float[] paletteL, paletteA, paletteB;
    public boolean dither = false, outline = true;
    public int size;
    public int shrink = 0;
    public float neutral = 1f;
    public IntObjectMap<VoxMaterial> materialMap;
//    public long seed;

    public static final float fidget = 0f;

    protected SmudgeRenderer() {

    }
    public SmudgeRenderer(final int size) {
        this.size = size;
        final int w = size * 4 + 4, h = size * 5 + 4;
//        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap = new Pixmap(w>>>shrink, h>>>shrink, Pixmap.Format.RGBA8888);
        render =   new int[w][h];
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
//        remade = new byte[size << 1][size << 1][size << 1];
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

//    protected float bn(int x, int y) {
////        final float result = (((x | ~y) & 1) + (x + ~y & 1) + (x & y & 1)) * 0.333f;
////        System.out.println(result);
////        return result;
////        return (((x | ~y) & 1) + (x + ~y & 1) + (x & y & 1)) * 0.333f;
//        return (PaletteReducer.TRI_BLUE_NOISE[(x & 63) | (y & 63) << 6] + 128) * 0x1p-8f;
//    }

    /**
     * Takes a modifier between -1f and 0.5f, and adjusts how this changes saturation accordingly.
     * Negative modifiers will decrease saturation, while positive ones increase it. If positive, any
     * changes are incredibly sensitive, and 0.05 will seem very different from 0.0. If negative, changes
     * are not as sensitive, but most of the noticeable effect will happen close to -0.1.
     * @param saturationModifier a float between -0.5f and 0.2f; negative decreases saturation, positive increases
     * @return this, for chaining
     */
    public SmudgeRenderer saturation(float saturationModifier) {
        neutral = (1f + Math.min(Math.max(saturationModifier,  -1f),  0.5f));
        return this;
    }

    public int[] palette() {
        return palette;
    }

    public SmudgeRenderer palette(PaletteReducer color) {
        return palette(color.paletteArray, color.colorCount);
    }

    public SmudgeRenderer palette(int[] color) {
        return palette(color, 256);
    }
    public SmudgeRenderer palette(int[] color, int count) {
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
        xPos += fidget;
        yPos += fidget;
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
        for (int x = 0, ax = xx; x < 4 && ax < render.length; x++, ax++) {
            for (int y = 0, ay = yy; y < 4 && ay < render[0].length; y++, ay++) {
                if ((depth > depths[ax][ay] || (depth == depths[ax][ay] &&
//                        colorL[ax][ay] < paletteL[voxel & 255]
                        (materials[ax][ay] == null || materials[ax][ay].getTrait(VoxMaterial.MaterialTrait._priority)
                                <= m.getTrait(VoxMaterial.MaterialTrait._priority))
                )) && (alpha == 0f)) {
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
    
    public SmudgeRenderer clear() {
        pixmap.setColor(0);
        pixmap.fill();
        fill(depths, 0);
        fill(render, 0);
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
     * Compiles all of the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param turns yaw in turns; like turning your head or making a turn in a car
     * @return {@link #pixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float turns, int frame) {
        return blit(turns, 0f, 0f, frame);
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
        int xSize = render.length - 1, ySize = render[0].length - 1, depth;
        int v, vx, vy, vz, fx, fy, fz;
        float hs = (size) * 0.5f, hsp = hs - fidget, ox, oy, oz, tx, ty, tz;
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
                    ox = vx - hsp;
                    oy = vy - hsp;
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
                    // + (PaletteReducer.TRI_BLUE_NOISE[(sx & 63) + (sy << 6) + (fx + fy + fz >>> 2) & 4095] + 0.5) * 0x1p-7;
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
//                if (colorA[x][y] >= 0f) {
//                    float maxL = 0f, minL = 1f, avgL = 0f,
//                            maxA = 0f, minA = 1f, avgA = 0f,
//                            maxB = 0f, minB = 1f, avgB = 0f,
//                            div = 0f, current;
//                    for (int xx = -distance; xx <= distance; xx++) {
//                        if (x + xx < 0 || x + xx > xSize) continue;
//                        for (int yy = -distance; yy <= distance; yy++) {
//                            if ((xx & yy) != 0 || y + yy < 0 || y + yy > ySize || colorA[x + xx][y + yy] < 0f)
//                                continue;
//                            current = colorL[x + xx][y + yy];
//                            maxL = Math.max(maxL, current);
//                            minL = Math.min(minL, current);
//                            avgL += current;
//                            current = colorA[x + xx][y + yy];
//                            maxA = Math.max(maxA, current);
//                            minA = Math.min(minA, current);
//                            avgA += current;
//                            current = colorB[x + xx][y + yy];
//                            maxB = Math.max(maxB, current);
//                            minB = Math.min(minB, current);
//                            avgB += current;
//                            div++;
//                        }
//                    }
////                    avg = avg / div + (x + y & 1) * 0.05f - 0.025f;
////                    pixmap.drawPixel(x, y, render[x][y] = ColorTools.toRGBA8888(ColorTools.limitToGamut(
////                            Math.min(Math.max(((avg - minL) < (maxL - avg) ? minL : maxL) - 0.15625f, 0f), 1f),
////                            (colorA[x][y] - 0.5f) * neutral + 0.5f,
////                            (colorB[x][y] - 0.5f) * neutral + 0.5f, 1f)));
////                    avgL = avgL / div + (x + y & 2) * 0.004f - 0.004f;
//                    avgL /= div;
//                    avgA /= div;
//                    avgB /= div;
//                    render[x][y] = ColorTools.toRGBA8888(limitToGamut(
//                            Math.min(Math.max(((avgL - minL) < (maxL - avgL) ? minL : maxL) - 0.15625f, 0f), 1f),
//                            (avgA - 0.5f) * neutral + 0.5f,
//                            (avgB - 0.5f) * neutral + 0.5f, 1f));
////                    avg /= div;
////                    colorL[x][y] = Math.min(Math.max(((avg - minL) < (maxL - avg) ? minL : maxL) - 0.15625f, 0f), 1f);
////                    if (neutral != 1f) {
////                        colorA[x][y] = (colorA[x][y] - 0.5f) * neutral + 0.5f;
////                        colorB[x][y] = (colorB[x][y] - 0.5f) * neutral + 0.5f;
////                    }
//                }
            }
        }
//        for (int x = 0; x <= xSize; x++) {
//            for (int y = 0; y <= ySize; y++) {
//                if (colorA[x][y] >= 0f) {
//                    pixmap.drawPixel(x >>> 1, y >>> 1, render[x][y] = ColorTools.toRGBA8888(ColorTools.limitToGamut(
//                            Math.min(Math.max(colorL[x][y] - 0.125f, 0f), 1f),
//                            (colorA[x][y] - 0.5f) * neutral + 0.5f,
//                            (colorB[x][y] - 0.5f) * neutral + 0.5f, 1f)));
//                }
//            }
//        }
//        for (int x = xSize; x >= 0; x--) {
//            for (int y = ySize; y >= 0; y--) {
//                if (colorA[x][y] >= 0f) {
//                    pixmap.drawPixel(x >>> shrink, y >>> shrink, render[x][y]);
//                }
//            }
//        }
        if (outline) {
            int o;
            for (int x = step; x <= xSize - step; x+= step) {
//                final int hx = x;
                final int hx = x >>> shrink;
                for (int y = step; y <= ySize - step; y+= step) {
//                    final int hy = y;
                    int hy = y >>> shrink;
                    if ((o = outlines[x][y]) != 0) {
                        depth = depths[x][y];
                        if (outlines[x - step][y] == 0) {
                            pixmap.drawPixel(hx - 1, hy    , o);
                        }
                        else if (depths[x - step][y] < depth - threshold) {
                            pixmap.drawPixel(hx - 1, hy    , o);
                        }
                        if (outlines[x + step][y] == 0) {
                            pixmap.drawPixel(hx + 1, hy    , o);
                        }
                        else if (depths[x + step][y] < depth - threshold) {
                            pixmap.drawPixel(hx + 1, hy    , o);
                        }
                        if (outlines[x][y - step] == 0) {
                            pixmap.drawPixel(hx    , hy - 1, o);
                        }
                        else if (depths[x][y - step] < depth - threshold) {
                            pixmap.drawPixel(hx    , hy - 1, o);
                        }
                        if (outlines[x][y + step] == 0) {
                            pixmap.drawPixel(hx    , hy + 1, o);
                        }
                        else if (depths[x][y + step] < depth - threshold) {
                            pixmap.drawPixel(hx    , hy + 1, o);
                        }

                        if (outlines[x - step][y - step] == 0) {
                            pixmap.drawPixel(hx - 1, hy - 1, o);
                        }
                        if (outlines[x + step][y - step] == 0) {
                            pixmap.drawPixel(hx + 1, hy - 1, o);
                        }
                        if (outlines[x - step][y + step] == 0) {
                            pixmap.drawPixel(hx - 1, hy + 1, o);
                        }
                        if (outlines[x + step][y + step] == 0) {
                            pixmap.drawPixel(hx + 1, hy + 1, o);
                        }

                    }
                }
            }
        }
        if(dither) {
            reducer.setDitherStrength(AppConfig.STRENGTH);
            reducer.reduce(pixmap, AppConfig.DITHER);
        }

        fill(depths, 0);
        fill(render, 0);
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
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
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
        return blit(angleTurns, frame);
    }

    public Pixmap drawSplats(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                             float translateX, float translateY, float translateZ,
                             IntObjectMap<VoxMaterial> materialMap) {
        this.materialMap = materialMap;
        splatOnly(colors, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        return blit(yaw, pitch, roll, frame);
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
                        ox = x - hs + translateX + fidget;
                        oy = y - hs + translateY + fidget;
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
                        splatOnly(g, yaw, pitch, roll, frame,
//                                translateX, translateY, translateZ
                                translateX + tc.translation.x, translateY + tc.translation.y, translateZ + tc.translation.z - g[0][0].length * 0.5f
                        );
                    }
                }
            }
        }
        return blit(yaw, pitch, roll, frame);
    }

    public Pixmap drawSplats(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                             float translateX, float translateY, float translateZ) {
        materialMap = VoxIO.lastMaterials;
        splatOnly(colors, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        return blit(yaw, pitch, roll, frame);
    }
}
