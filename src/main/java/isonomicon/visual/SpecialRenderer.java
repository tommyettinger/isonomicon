package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntMap;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import isonomicon.io.extended.GroupChunk;
import isonomicon.io.extended.ShapeModel;
import isonomicon.io.extended.TransformChunk;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;
import squidpony.squidmath.BlueNoise;
import squidpony.squidmath.FastNoise;
import squidpony.squidmath.IntPointHash;

import java.util.ArrayList;
import java.util.Arrays;

import static squidpony.ArrayTools.fill;

/**
 * Renders {@code byte[][][]} voxel models to pairs of {@link Pixmap}s, one using normal RGBA colors and one using an
 * unusual technique that stores a palette index in the R channel and a lightness adjustment in the G channel.
 */
public class SpecialRenderer {
    public final Stuff[] stuffs;
    public Pixmap palettePixmap;
    public int[][] depths, voxels, render, outlines;
    public VoxMaterial[][] materials;
    public float[][] shadeX, shadeZ, shading, outlineShading, saturation;
    public byte[][] indices, outlineIndices;
    public int[] palette;
    public float[] paletteL, paletteA, paletteB;
    public boolean outline = true;
    public boolean variance = true;
    public int size;
    public static int shrink = 1;
    public float neutral = 1f;

    public static final FastNoise noise = new FastNoise(0x1337BEEF, 0.0125f, FastNoise.SIMPLEX_FRACTAL, 2);

    protected SpecialRenderer() {
        this(64);
    }

    public SpecialRenderer(final int size) {
        this(size, Stuff.STUFFS);
    }

    public SpecialRenderer(final int size, Stuff[] stuffs) {
        this.size = size;
        final int w = size * 4 + 4, h = size * 5 + 4;
        palettePixmap = new Pixmap(w>>>shrink, h>>>shrink, Pixmap.Format.RGBA8888);
        render =   new int[w][h];
        outlines = new int[w][h];
        depths =   new int[w][h];
        indices =  new byte[w][h];
        outlineIndices =  new byte[w][h];
        shading =  new float[w][h];
        saturation =  new float[w][h];
        outlineShading = new float[w][h];
        materials = new VoxMaterial[w][h];
        voxels = fill(-1, w, h);
        shadeX = fill(-1f, size * 4, size * 4);
        shadeZ = fill(-1f, size * 4, size * 4);
        this.stuffs = stuffs;
    }
    
    protected float bn(int x, int y, int seed) {
        return (BlueNoise.getSeededTriOmniTiling(x, y, seed) + 128) * 0x1p-8f;
    }

    protected float bnBlocky(int x, int y, int seed) {
        return (BlueNoise.TILE_NOISE[seed & 63][(x & 63) | (y & 63) << 6] & 0x80) * 0x3p-10f;
    }

    public static float sin_(float turns)
    {
        return MathUtils.sinDeg(turns * 360f);
    }

    public static float cos_(float turns)
    {
        return MathUtils.cosDeg(turns * 360f);
    }

    /**
     * Takes a modifier between -1f and 0.5f, and adjusts how this changes saturation accordingly.
     * Negative modifiers will decrease saturation, while positive ones increase it. If positive, any
     * changes are incredibly sensitive, and 0.05 will seem very different from 0.0. If negative, changes
     * are not as sensitive, but most of the noticeable effect will happen close to -0.1.
     * @param saturationModifier a float between -0.5f and 0.2f; negative decreases saturation, positive increases
     * @return this, for chaining
     */
    public SpecialRenderer saturation(float saturationModifier) {
        neutral = 1f + MathUtils.clamp(saturationModifier, -1f, 0.5f);
        return this;
    }

    public int[] palette() {
        return palette;
    }

    public SpecialRenderer palette(PaletteReducer color) {
        return palette(color.paletteArray, color.colorCount);
    }

    public SpecialRenderer palette(int[] color) {
        return palette(color, 256);
    }
    public SpecialRenderer palette(int[] color, int count) {
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
        final Stuff stuff = stuffs[Math.min(voxel & 255, stuffs.length - 1)];
        final VoxMaterial m = stuff.material;
        final float alpha = m.getTrait(VoxMaterial.MaterialTrait._alpha);
        if(alpha >= 1f) return;
        voxel = (byte) stuff.appearsAs;
        final float flip = m.getTrait(VoxMaterial.MaterialTrait._frame);
        if(Tools3D.randomizePointRare(vx, vy, vz, frame) < m.getTrait(VoxMaterial.MaterialTrait._metal) || (frame & 1) == flip)
            return;
        final float rise = m.getTrait(VoxMaterial.MaterialTrait._rise) * (1.25f + IntPointHash.hash256(vx, vy, vz, 12345) * 0x1.Cp-8f);
        final float flow = m.getTrait(VoxMaterial.MaterialTrait._flow);
        final float emit = m.getTrait(VoxMaterial.MaterialTrait._emit) * 0.75f;
        int lowX = 0, highX = 4, lowY = 0, highY = 4;
//        if(emit != 0f) {
//            lowX = lowY = 1;
//            highX = highY = 3;
//        } else
            if(flow != 0f){
            float ns = noise.getConfiguredNoise(xPos, yPos, zPos, frame * flow);
            if(ns > 0) highX = (int)(4.5 + ns * (3 << shrink));
            else if(ns < 0) lowX = Math.round(lowX + ns * (3 << shrink));
        }
        final int
                xx = (int)(0.5f + Math.max(0, (size + yPos - xPos) * 2 + 1)),
                yy = (int)(0.5f + Math.max(0, (zPos * 3 + size * 3 - xPos - yPos) + 1 + rise * frame)),
                depth = (int)(0.5f + (xPos + yPos) * 2 + zPos * 3);
        boolean drawn = false;
        final float hs = size * 0.5f;
        for (int x = lowX, ax = xx; x < highX && ax < render.length; x++, ax++) {
            if (ax < 0) continue;
            for (int y = lowY, ay = yy; y < highY && ay < render[0].length; y++, ay++) {
                if ((depth > depths[ax][ay] || (depth == depths[ax][ay] && (indices[ax][ay] & 255) < (voxel & 255)))) {
                    drawn = true;
                    indices[ax][ay] = voxel;
                    depths[ax][ay] = depth;
                    materials[ax][ay] = m;
                    outlines[ax][ay] = 1;
                    outlineShading[ax][ay] = paletteL[voxel & 255] * (0.625f + emit * 2.5f);
                    outlineIndices[ax][ay] = voxel;
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
    
    public SpecialRenderer clear() {
        palettePixmap.setColor(0);
        palettePixmap.fill();
        fill(depths, 0);
        fill(render, 0);
        fill(outlines, (byte) 0);
        fill(indices, (byte) 0);
        fill(outlineIndices, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        fill(shading, 0f);
        fill(saturation, 0f);
        fill(outlineShading, -1f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return this;
    }

    /**
     * Compiles all of the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param turns yaw in turns; like turning your head or making a turn in a car
     * @return {@link #palettePixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
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
     * @return {@link #palettePixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float yaw, float pitch, float roll, int frame) {
        final int threshold = 13;
        palettePixmap.setColor(0);
        palettePixmap.fill();
        int xSize = render.length - 1, ySize = render[0].length - 1, depth;
        int v, vx, vy, vz, fx, fy, fz;
        float hs = (size) * 0.5f, ox, oy, oz, tx, ty, tz;
        final float cYaw = cos_(yaw), sYaw = sin_(yaw);
        final float cPitch = cos_(pitch), sPitch = sin_(pitch);
        final float cRoll = cos_(roll), sRoll = sin_(roll);
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
                    final float rough = m.getTrait(VoxMaterial.MaterialTrait._rough);
                    final float emit = m.getTrait(VoxMaterial.MaterialTrait._emit);
                    if(variance) {
                        final float dapple = m.getTrait(VoxMaterial.MaterialTrait._dapple);
                        final float vary = m.getTrait(VoxMaterial.MaterialTrait._vary) * 10f;
                        if (dapple != 0f) {
                            final float d = dapple * bnBlocky(vx, vy, vz);
                            shading[sx][sy] += d;
                        }
                        if (vary != 0f) {
                            saturation[sx][sy] = Math.min(Math.max(vary * bnBlocky(vy, vz, vx), -1f), 1f);
                        }
                    }
                    float limit = 2;
                    if (Math.abs(shadeX[fy][fz] - tx) <= limit || ((fy > 1 && Math.abs(shadeX[fy - 2][fz] - tx) <= limit) || (fy < shadeX.length - 2 && Math.abs(shadeX[fy + 2][fz] - tx) <= limit))) {
                        float spread = MathUtils.lerp(0.0025f, 0.001f, rough);
                        if (Math.abs(shadeZ[fx][fy] - tz) <= limit) {
                            spread *= 2f;
                            float change = m.getTrait(VoxMaterial.MaterialTrait._ior) * 0.2f;
                            shading[sx][sy] += change;
                        }
                        int dist;
                        for (int i = -3, si = sx + i; i <= 3; i++, si++) {
                            for (int j = -3, sj = sy + j; j <= 3; j++, sj++) {
                                if((dist = i * i + j * j) > 9 || si < 0 || sj < 0 || si > xSize || sj > ySize) continue;
                                float change = spread * (4 - (float)Math.sqrt(dist));
                                shading[si][sj] += change;
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
                                shading[si][sj] += change;
                            }
                        }
                    }
                    if (emit > 0) {
                        float spread = emit * 0.0015f;
                        for (int i = -12, si = sx + i; i <= 12; i++, si++) {
                            for (int j = -12, sj = sy + j; j <= 12; j++, sj++) {
                                final int dist = i * i + j * j;
                                if(dist > 144 || si < 0 || sj < 0 || si > xSize || sj > ySize) continue;
                                float change = spread * (12f - (float) Math.sqrt(dist));
                                shading[si][sj] += change;
                            }
                        }
                    }
                }
            }
        }
//        final int distance = 1;
//        for (int x = 0; x <= xSize; x++) {
//            for (int y = 0; y <= ySize; y++) {
//                if (colorA[x][y] >= 0f) {
//                    float maxL = 0f, minL = 1f, avgL = 0f,
//                            maxA = 0f, minA = 1f, avgA = 0f,
//                            maxB = 0f, minB = 1f, avgB = 0f,
//                            div = 0f, current;
//                    for (int xx = -distance; xx <= distance; xx++) {
//                        if (x + xx < 0 || x + xx > xSize) continue;
//                        for (int yy = -distance; yy <= distance; yy++) {
//                            if ((xx & yy) != 0 || y + yy < 0 || y + yy > ySize || colorA[x + xx][y + yy] <= 0f)
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
//                    avgL /= div;
//                    avgA /= div;
//                    avgB /= div;
//                    render[x][y] = ColorTools.toRGBA8888(ColorTools.limitToGamut(
//                            Math.min(Math.max(((avgL - minL) < (maxL - avgL) ? minL : maxL) - 0.15625f, 0f), 1f),
//                            (avgA - 0.5f) * neutral + 0.5f,
//                            (avgB - 0.5f) * neutral + 0.5f, 1f));
//                }
//            }
//        }

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
        for (int x = xSize; x >= 0; x--) {
            for (int y = ySize; y >= 0; y--) {
                if (indices[x][y] != 0) {
                    float shade = Math.min(Math.max((shading[x][y]) * 0.625f + 0.1328125f, 0f), 1f);
//                    float shade = Math.min(Math.max((shading[x][y]) * 0.625f + 0.125f, 0f), 1f);
                    float sat = Math.min(Math.max((saturation[x][y]) * 0.5f + 0.5f, 0f), 1f);
                    palettePixmap.drawPixel(x >>> shrink, y >>> shrink, (indices[x][y] & 255) << 24 |
                            (int)(shade * 255.999f) << 16 |
                            (int)(sat * 255.999f) << 8 | 255);
                }
            }
        }
//        for (int x = xSize; x >= 0; x--) {
//            for (int y = ySize; y >= 0; y--) {
//                if (colorA[x][y] >= 0f) {
//                    pixmap.drawPixel(x >>> shrink, y >>> shrink, render[x][y]);
//                }
//            }
//        }
        if (outline) {
            int po;
            for (int x = step; x <= xSize - step; x+= step) {
//                final int hx = x;
                final int hx = x >>> shrink;
                for (int y = step; y <= ySize - step; y+= step) {
//                    final int hy = y;
                    int hy = y >>> shrink;
                    if ((outlines[x][y]) != 0) {
                        depth = depths[x][y];
                        po = (outlineIndices[x][y] & 255) << 24 | (int)MathUtils.clamp(64f * outlineShading[x][y], 0f, 255f) << 16 | 64 << 8 | 255;
                        if (outlines[x - step][y] == 0 || depths[x - step][y] < depth - threshold) {
                            palettePixmap.drawPixel(hx - 1, hy    , po);
                        }
                        if (outlines[x + step][y] == 0 || depths[x + step][y] < depth - threshold) {
                            palettePixmap.drawPixel(hx + 1, hy    , po);
                        }
                        if (outlines[x][y - step] == 0 || depths[x][y - step] < depth - threshold) {
                            palettePixmap.drawPixel(hx, hy - 1, po);
                        }
                        if (outlines[x][y + step] == 0 || depths[x][y + step] < depth - threshold) {
                            palettePixmap.drawPixel(hx, hy + 1, po);
                        }
                    }
                }
            }
        }

        fill(depths, 0);
        fill(render, 0);
        fill(shading, 0f);
        fill(saturation, 0f);
        fill(outlineShading, 0f);
        fill(outlines, (byte) 0);
        fill(indices, (byte) 0);
        fill(outlineIndices, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return palettePixmap;
    }

    // To move one x+ in voxels is x + 2, y - 1 in pixels.
    // To move one x- in voxels is x - 2, y + 1 in pixels.
    // To move one y+ in voxels is x - 2, y - 1 in pixels.
    // To move one y- in voxels is x + 2, y + 1 in pixels.
    // To move one z+ in voxels is y + 3 in pixels.
    // To move one z- in voxels is y - 3 in pixels.

    public Pixmap drawSplats(byte[][][] colors, float angleTurns, int frame) {
        final int size = colors.length;
        final float hs = size * 0.5f;
        final float c = cos_(angleTurns), s = sin_(angleTurns);
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
    public void splatOnly(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                          float translateX, float translateY, float translateZ) {
        final int size = colors.length;
        final float hs = size * 0.5f;
        float ox, oy, oz; // offset x,y,z
        final float cYaw = cos_(yaw), sYaw = sin_(yaw);
        final float cPitch = cos_(pitch), sPitch = sin_(pitch);
        final float cRoll = cos_(roll), sRoll = sin_(roll);
        final float x_x = cYaw * cPitch, y_x = cYaw * sPitch * sRoll - sYaw * cRoll, z_x = cYaw * sPitch * cRoll + sYaw * sRoll;
        final float x_y = sYaw * cPitch, y_y = sYaw * sPitch * sRoll + cYaw * cRoll, z_y = sYaw * sPitch * cRoll - cYaw * sRoll;
        final float x_z = -sPitch, y_z = cPitch * sRoll, z_z = cPitch * cRoll;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if(v != 0)
                    {
                        ox = x - hs;
                        oy = y - hs;
                        oz = z - hs;
                        splat(  ox * x_x + oy * y_x + oz * z_x + size + translateX,
                                ox * x_y + oy * y_y + oz * z_y + size + translateY,
                                ox * x_z + oy * y_z + oz * z_z + hs + translateZ, x, y, z, v, frame);
                    }
                }
            }
        }
    }

    public Pixmap drawSplats(byte[][][] colors, float yaw, float pitch, float roll, int frame,
                             float translateX, float translateY, float translateZ) {
        splatOnly(colors, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        return blit(yaw, pitch, roll, frame);
    }

    public Pixmap drawModel(VoxModel model, float yaw, float pitch, float roll, int frame,
                            float translateX, float translateY, float translateZ){

        for(GroupChunk gc : model.groupChunks.values()) {
            for(int ch : gc.childIds) {
                TransformChunk tc = model.transformChunks.get(ch);
                if (tc != null) {
                    for (ShapeModel sm : model.shapeChunks.get(tc.childId).models) {
                        byte[][][] g = model.grids.get(sm.id);
                        splatOnly(g, yaw, pitch, roll, frame, translateX, translateY, translateZ);
                    }
                }
            }
        }
        return blit(yaw, pitch, roll, frame);
    }

    protected void subDraw(ArrayList<byte[][][]> grids, ArrayList<IntMap<float[]>> links,
                           byte[][][] g, IntMap<float[]> link, float yaw, float pitch, float roll, int frame,
                           float translateX, float translateY, float translateZ){
        splatOnly(g, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        final int size = g.length;
        final float hs = size * 0.5f;
        float ox, oy, oz; // offset x,y,z
        final float cYaw = cos_(yaw), sYaw = sin_(yaw);
        final float cPitch = cos_(pitch), sPitch = sin_(pitch);
        final float cRoll = cos_(roll), sRoll = sin_(roll);
        final float x_x = cYaw * cPitch, y_x = cYaw * sPitch * sRoll - sYaw * cRoll, z_x = cYaw * sPitch * cRoll + sYaw * sRoll;
        final float x_y = sYaw * cPitch, y_y = sYaw * sPitch * sRoll + cYaw * cRoll, z_y = sYaw * sPitch * cRoll - cYaw * sRoll;
        final float x_z = -sPitch, y_z = cPitch * sRoll, z_z = cPitch * cRoll;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = g[x][y][z];
                    if(v != 0)
                    {
                        ox = x - hs;
                        oy = y - hs;
                        oz = z - hs;
                        splat(  ox * x_x + oy * y_x + oz * z_x + size + translateX,
                                ox * x_y + oy * y_y + oz * z_y + size + translateY,
                                ox * x_z + oy * y_z + oz * z_z + hs + translateZ, x, y, z, v, frame);
                    }
                }
            }
        }

        for(IntMap.Entry<float[]> ent : link) {
            if(ent.key == -1) continue;
            for (int j = 0; j < links.size(); j++) {
                float[] got;
                if((got = links.get(j).get(ent.key)) != null) {
                    ox = ent.value[0] - got[0];
                    oy = ent.value[1] - got[1];
                    oz = ent.value[2] - got[2];

                    subDraw(grids, links, grids.remove(j), links.remove(j), yaw, pitch, roll, frame,
                            translateX + (ox * x_x + oy * y_x + oz * z_x),
                            translateY + (ox * x_y + oy * y_y + oz * z_y),
                            translateZ + (ox * x_z + oy * y_z + oz * z_z));
                }
            }
        }

    }

    public Pixmap drawModelSimple(VoxModel model, float yaw, float pitch, float roll, int frame,
                                  float translateX, float translateY, float translateZ){
        boolean foundAnything = false;
        ArrayList<byte[][][]> grids = new ArrayList<>(model.grids.size());
        ArrayList<IntMap<float[]>> links = new ArrayList<>(model.links.size());
        for (int i = 0; i < model.grids.size(); i++) {
            grids.add(Tools3D.deepCopy(model.grids.get(i)));
            links.add(new IntMap<>(model.links.get(i)));
        }
        for (int i = 0; i < grids.size(); i++) {
            IntMap<float[]> link = links.get(i);
            if(link.containsKey(-1))
            {
                //root grid
                subDraw(grids, links, grids.remove(i), links.remove(i), yaw, pitch, roll, frame,
                        translateX, translateY, translateZ);
                foundAnything = true;
                break;
            }
        }
        if(!foundAnything){
            subDraw(grids, links, grids.remove(0), links.remove(0), yaw, pitch, roll, frame,
                    translateX, translateY, translateZ);
        }
        return blit(yaw, pitch, roll, frame);
    }
}
