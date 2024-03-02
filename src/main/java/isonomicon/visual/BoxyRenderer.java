package isonomicon.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.digital.ArrayTools;
import com.github.tommyettinger.ds.IntObjectMap;
import com.github.yellowstonegames.grid.BlueNoise;
import com.github.yellowstonegames.grid.CyclicNoise;
import com.github.yellowstonegames.grid.IntPointHash;
import com.github.yellowstonegames.grid.Noise;
import isonomicon.io.extended.GroupChunk;
import isonomicon.io.extended.ShapeModel;
import isonomicon.io.extended.TransformChunk;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.physical.VoxMaterial;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tommyettinger.digital.ArrayTools.fill;
import static com.github.tommyettinger.digital.TrigTools.cosTurns;
import static com.github.tommyettinger.digital.TrigTools.sinTurns;

/**
 * Renders {@code byte[][][]} voxel models to pairs of {@link Pixmap}s, one using normal RGBA colors and one using an
 * unusual technique that stores a palette index in the R channel and a lightness adjustment in the G channel.
 */
public class BoxyRenderer {
    public static int shrink = 0;
    public static int distortHXY = 2, distortVXY = 1, distortVZ = 2; // ground truth for isometric

    public final Stuff[] stuffs;
    public Pixmap palettePixmap;
    public ByteBuffer buffer;
    public int[][] depths, voxels, render;
    public byte[][] outlines;
    public VoxMaterial[][] materials;
    public float[][] shadeX, shadeZ, shading, midShading, outlineShading, saturation;
    public byte[][] indices, outlineIndices, lightIndices;
    public int[] palette;
    public float[] paletteL, paletteA, paletteB;
    public int outline = 4;
    public boolean variance = true;
    public boolean lighting = true;
    public boolean shadows = Tools3D.SHADOWS;
    public int size;
    public float neutral = 1f;

    public static final byte DARKEN = (byte) 128;
    public static final byte LIGHTEN = (byte) 135;

    public static final Noise noise = new Noise(0x1337BEEF, 0.0125f, Noise.SIMPLEX_FRACTAL, 2);
    public static final CyclicNoise swirlNoise = new CyclicNoise(0xDEADBEEFBA77L, 6, 0.03f);

    public Pixmap normalMap;
    public boolean computeNormals;
    public double blurSigma = 0.9;
    public GaussianBlur blur;
    public float[][] normals;
    private final Vector3 out = new Vector3();

    protected BoxyRenderer() {
        this(64);
    }

    public BoxyRenderer(final int size) {
        this(size, Stuff.STUFFS);
    }

    public BoxyRenderer(final int size, Stuff[] stuffs) {
        this.size = size;
        final int w = MathUtils.ceil(size * distortHXY * 2 + 4), h = MathUtils.ceil(size * (distortVZ + distortVXY * 2) + 4);
        palettePixmap = new Pixmap(w>>>shrink, h>>>shrink, Pixmap.Format.RGBA8888);
        palettePixmap.setBlending(Pixmap.Blending.None);
        buffer = palettePixmap.getPixels();
        render =   new int[w][h];
        outlines = new byte[w][h];
        depths =   new int[w][h];
        indices =  new byte[w][h];
        outlineIndices =  new byte[w][h];
        lightIndices =  new byte[w][h];
        shading =  new float[w][h];
        midShading =  new float[w][h];
        saturation =  new float[w][h];
        outlineShading = new float[w][h];
        materials = new VoxMaterial[w][h];
        voxels = fill(-1, w, h);
        shadeX = fill(-1f, size * 4, size * 4);
        shadeZ = fill(-1f, size * 4, size * 4);
        this.stuffs = stuffs;

        if(computeNormals)
        {
            normals = new float[4][(w>>>shrink)*(h>>>shrink)];
            normalMap = new Pixmap(w>>>shrink, h>>>shrink, Pixmap.Format.RGBA8888);
            blur = new GaussianBlur(Math.max(blurSigma, 0.0));
        }

    }

    /**
     * A relatively close approximation of {@code Math.exp(-x * x - y * y)}, the Gaussian function in 2D. This returns
     * 1.0f when x and y are both 0, and goes as low as 0.0051585725 when either or both of x or y are very significant.
     * The Gaussian function is useful for making circular "bump" shapes, among lots of other things.
     * <br>
     * This uses the 2/2 Padé approximant to {@code Math.exp(-x*x-y*y)}, but halves the argument to exp() before
     * approximating, ensures that that argument is at least -3.5f (to avoid major issues with larger inputs), runs the
     * approximation, and squares the result. The halving/squaring keeps the calculation in a more precise span for a
     * larger domain.
     *
     * @param x typically the difference between two x positions; will be squared, so may be positive or negative
     * @param y typically the difference between two y positions; will be squared, so may be positive or negative
     * @return the positive result of the Gaussian function in 2D for the given parameters; between 0.0051585725 and 1.0
     */
    public static float gaussian2D(float x, float y) {
        x = Math.max(-3.5f, (x * x + y * y) * -0.5f);
        x = (12 + x * (6 + x))/(12 + x * (-6 + x));
        return x * x;
    }

    /**
     * The same as {@link #gaussian2D(float, float)} except that its minimum is reduced to 0.0, while its maximum is
     * still 1.0 . This is not precisely a Gaussian function, because it won't keep slowly decreasing given rising
     * inputs, but being able to know the outside is 0 may be useful for various tasks.
     * @param x typically the difference between two x positions; will be squared, so may be positive or negative
     * @param y typically the difference between two y positions; will be squared, so may be positive or negative
     * @return the positive result of the "Gaussian" function in 2D for the given parameters; between 0.0 and 1.0
     */
    public static float gaussianFinite2D(float x, float y) {
        x = Math.max(-3.4060497f, (x * x + y * y) * -0.5f);
        x = (12 + x * (6 + x))/(12 + x * (-6 + x));
        return (x * x - 0.00516498f) * 1.0051918f;
    }

    protected float bn(int x, int y, int seed) {
        return (BlueNoise.getSeededTriangular(x, y, seed) + 128) * 0x1p-8f;
    }

    protected float bnBlocky(int x, int y, int seed) {
        return (BlueNoise.TILE_NOISE[seed & 63][(x & 63) | (y & 63) << 6] & 0x80) * 0x3p-10f;
    }

    /**
     * Takes a modifier between -1f and 0.5f, and adjusts how this changes saturation accordingly.
     * Negative modifiers will decrease saturation, while positive ones increase it. If positive, any
     * changes are incredibly sensitive, and 0.05 will seem very different from 0.0. If negative, changes
     * are not as sensitive, but most of the noticeable effect will happen close to -0.1.
     * @param saturationModifier a float between -0.5f and 0.2f; negative decreases saturation, positive increases
     * @return this, for chaining
     */
    public BoxyRenderer saturation(float saturationModifier) {
        neutral = 1f + Math.min(Math.max(saturationModifier,  -1f),  0.5f);
        return this;
    }

    public int[] palette() {
        return palette;
    }

    public BoxyRenderer palette(PaletteReducer color) {
        return palette(color.paletteArray, color.colorCount);
    }

    public BoxyRenderer palette(int[] color) {
        return palette(color, 256);
    }
    public BoxyRenderer palette(int[] color, int count) {
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

    /**
     * Applies a Scharr filter to a given x,y point in the already-computed depths 2D array, assigning floats to
     * {@link #normals}. The blue channel of the color represents the axis of the normal vector that points toward
     * the camera, the green channel up, and the red channel right.
     * <a href="https://forum.unity.com/threads/sobel-operator-height-to-normal-map-on-gpu.33159/">Thanks to apple_motion for writing the initial basis for this</a>,
     * and <a href="https://gamedev.stackexchange.com/q/165575">Jarrett on the game dev StackExchange for providing a working solution.</a>
     * @param x x position, looked up in depths
     * @param y y position, looked up in depths
     */
    public void scharr(int x, int y) {
        // if there is nothing here, don't bother computing anything.
        if(indices[x][y] == 0) return;
        int[][] data = this.depths;
        // for other usage, this calculation will have to be different.
        float maxDepth = 1.5f * (0.5f + (size + size) * distortHXY + size * distortVZ);
        float invMaxDepth = 1f / maxDepth;
        // how many pixels away from (x,y) each direction will move per step.
        final int u = 1 << shrink;

        float tl = (x < u || y < u) ? 0 : (data[x-u][y-u]) * invMaxDepth;                    // top left
        float  l = (x < u) ? 0 : (data[x-u][y]) * invMaxDepth;                               // left
        float bl = (x < u || y >= data[0].length - u) ? 0 : (data[x-u][y+u]) * invMaxDepth;  // bottom left
        float  t = (y < u) ? 0 : (data[x][y-u]) * invMaxDepth;                               // top
        float  b = (data[x][y]) * invMaxDepth;                                               // bottom
        float tr = (y >= data[0].length - u) ? 0 : (data[x][y+u]) * invMaxDepth;             // top right
        float  r = (x >= data.length - u || y < u) ? 0 : (data[x+u][y-u]) * invMaxDepth;     // right
        float br = (x >= data.length - u) ? 0 : (data[x+u][y]) * invMaxDepth;                // bottom right

        // Scharr operator
        float cx = ((tl + bl - tr - br) * 47 + (l - r) * 162);
        float cy = ((tl + tr - bl - br) * 47 + (t - b) * 162);
        float cz = 12f; // adjustable

        out.set(cx, cy, cz).nor().scl(0.5f).add(0.5f);
        int xx = x >>> shrink, yy = y >>> shrink, w = palettePixmap.getWidth(), h = palettePixmap.getHeight(), i = xx + yy * w;
        normals[0][i] = out.x;
        normals[1][i] = out.y;
        normals[2][i] = out.z;
        normals[3][i] = 1f;

        for (int px = -4, ax = xx + px; px <= 4; px++, ax++) {
            if(ax >= 0 && ax < w) {
                for (int py = -4, ay = yy + py; py <= 4; py++, ay++) {
                    if(ay >= 0 && ay < h && normals[3][i = ax + ay * w] == 0f){
                        normals[0][i] = out.x;
                        normals[1][i] = out.y;
                        normals[2][i] = out.z;
                    }
                }
            }
        }
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
        final float swirl = m.getTrait(VoxMaterial.MaterialTrait._swirl) + 1f;
        if(swirl != 1f) {
            float ns = swirlNoise.getNoise(vx, vy, vz, cosTurns(frame * 0x1p-7f) * 0.625f / swirlNoise.getFrequency(), sinTurns(frame * 0x1p-7f) * 0.625f / swirlNoise.getFrequency()) * 2f;
            if(ns > swirl) return;
        }
        final float emit = m.getTrait(VoxMaterial.MaterialTrait._emit) * 0.75f;
        int lowX = 0, highX = 4, lowY = 0, highY = 4;
//        if(emit != 0f) {
//            lowX = lowY = 1;
//            highX = highY = 3;
//        } else
            if(flow != 0f) {
                float ns = noise.getConfiguredNoise(xPos, yPos, zPos, frame * flow);
                if (ns > 0) highX = (int) (4.5 + ns * (3 << shrink));
                else if (ns < 0) lowX = Math.round(lowX + ns * (3 << shrink));
            }
        final int
                xx = (int)(0.5f + Math.max(0, (size + yPos - xPos) * distortHXY + 1)),
                yy = (int)(0.5f + Math.max(0, (zPos * distortVZ + size * ((distortVXY) * 3) - distortVXY * (xPos + yPos)) + 1 + rise * frame)),
                depth = (int)(0.5f + (xPos + yPos) * distortHXY + zPos * distortVZ);
        boolean drawn = false;
        final float hs = size * 0.5f;
        for (int x = lowX, ax = xx; x < highX && ax < render.length; x++, ax++) {
            if (ax < 0) continue;
            for (int y = lowY, ay = yy; y < highY && ay < render[0].length; y++, ay++) {
                if ((depth > depths[ax][ay] || (depth == depths[ax][ay] && (indices[ax][ay] & 255) > (voxel & 255)))) {
                    drawn = true;
                    depths[ax][ay] = depth;
                    materials[ax][ay] = m;
                    if(voxel != 0) {
                        indices[ax][ay] = voxel;
                        if (emit == 0f) {
                            outlines[ax][ay] = 1;
                            outlineShading[ax][ay] = paletteL[voxel & 255] * 0.625f;
                            outlineIndices[ax][ay] = voxel;
                        }
                        else { //else if(outlineIndices[ax][ay] == 0) {
                            outlines[ax][ay] = -1;
                            outlineShading[ax][ay] = paletteL[voxel & 255] * (1f + emit * 2.5f);
//                            outlineIndices[ax][ay] = 0;
                        }
                    }
                    else {
                        indices[ax][ay] = -16;
                    }
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
        else if(drawn && emit == 0f) {
            shadeZ[(int) (hs + xPos)][(int) (hs + yPos)] = Math.max(shadeZ[(int) (hs + xPos)][(int) (hs + yPos)], (hs + zPos));
            shadeX[(int) (hs + yPos)][(int) (hs + zPos)] = Math.max(shadeX[(int) (hs + yPos)][(int) (hs + zPos)], (hs + xPos));
        }
    }
    
    public BoxyRenderer clear() {
        palettePixmap.setColor(0);
        palettePixmap.fill();
        fill(depths, 0);
        fill(render, 0);
        fill(outlines, (byte) 0);
        fill(indices, (byte) 0);
        fill(outlineIndices, (byte) 0);
        fill(lightIndices, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        fill(shading, 0f);
        fill(midShading, 0f);
        fill(saturation, 0f);
        fill(outlineShading, -1f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return this;
    }

    /**
     * Compiles all the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param turns yaw in turns; like turning your head or making a turn in a car
     * @return {@link #palettePixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float turns, int frame) {
        return blit(turns, 0f, 0f, frame);
    }

    /**
     * Compiles all the individual voxels drawn with {@link #splat(float, float, float, int, int, int, byte, int)} into a
     * single Pixmap and returns it.
     * @param yaw in turns; like turning your head or making a turn in a car
     * @param pitch in turns; like looking up or down or making a nosedive in a plane
     * @param roll in turns; like tilting your head to one side or doing a barrel roll in a starship
     * @return {@link #palettePixmap}, edited to contain the render of all the voxels put in this with {@link #splat(float, float, float, int, int, int, byte, int)}
     */
    public Pixmap blit(float yaw, float pitch, float roll, int frame) {
        final int threshold = 10+shrink*3;//13;
        palettePixmap.setColor(0);
        palettePixmap.fill();

        if(computeNormals) {
            ArrayTools.fill(normals, 0f);
            normalMap.setColor(0);
            normalMap.fill();
        }

        int xSize = render.length - 1, ySize = render[0].length - 1, depth;
        int v, vx, vy, vz, fx, fy, fz;
        float hs = (size) * 0.5f, hsp = hs, ox, oy, oz, tx, ty, tz;
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
                    if(lighting) {
                        float limit = 2;
                        if (Math.abs(shadeX[fy][fz] - tx) <= limit || ((fy > 1 && Math.abs(shadeX[fy - 2][fz] - tx) <= limit) || (fy < shadeX.length - 2 && Math.abs(shadeX[fy + 2][fz] - tx) <= limit))) {
                            float spread = MathUtils.lerp(0.0025f, 0.001f, rough);
                            if (Math.abs(shadeZ[fx][fy] - tz) <= limit) {
                                spread *= 2f;
                                float change = m.getTrait(VoxMaterial.MaterialTrait._ior) * 0.2f;
                                shading[sx][sy] += change;
                            }
                            int dist;
                            for (int i = -4, si = sx + i; i <= 4; i++, si++) {
                                for (int j = -4, sj = sy + j; j <= 4; j++, sj++) {
                                    if ((dist = i * i + j * j) > 9 || si < 0 || sj < 0 || si > xSize || sj > ySize)
                                        continue;
                                    float change = spread * (4 - (float) Math.sqrt(dist));
                                    shading[si][sj] += change;
                                }
                            }
                        } else if (Math.abs(shadeZ[fx][fy] - tz) <= limit) {
                            float spread = MathUtils.lerp(0.005f, 0.002f, rough);
                            float dist;
                            for (int i = -4, si = sx + i; i <= 4; i++, si++) {
                                for (int j = -4, sj = sy + j; j <= 4; j++, sj++) {
                                    if ((dist = i * i + j * j) > 9 || si < 0 || sj < 0 || si > xSize || sj > ySize)
                                        continue;
                                    float change = spread * (4 - (float) Math.sqrt(dist));
                                    shading[si][sj] += change;
                                }
                            }
                        }
                        if (emit != 0) {
                            float spread = emit * 0.08f;
                            final int radius = 3 * (1 + shrink);
                            for (int i = -radius, si = sx + i; i <= radius; i++, si++) {
                                for (int j = -radius, sj = sy + j; j <= radius; j++, sj++) {
                                    final int dist = i * i + j * j;
                                    if (dist > radius * radius || si < 0 || sj < 0 || si > xSize || sj > ySize)
                                        continue;
                                    float change = spread * (radius - (float) Math.sqrt(dist));
                                    midShading[si][sj] += change;
                                    lightIndices[si][sj] = (byte)Math.max(lightIndices[si][sj] & 255, indices[sx][sy] & 255);
                                }
                            }
                        }
                        if(shadows){
                            if(indices[sx][sy] == -16 && shadeZ[fx][fy] <= hs + 0.5f)
//                            if(indices[sx][sy] == -16 && (vx <= step * 4 || vy <= step * 4 || vx >= xSize - step * 4 || vy >= ySize - step * 4))
                                shading[sx][sy] = 1024f;
                        }
                    }
                }
            }
        }
        byte index;
        float sh;
        for (int x = xSize; x >= 0; x--) {
            for (int y = ySize; y >= 0; y--) {
                if ((index = indices[x][y]) != 0) {
                    sh = shading[x][y];
                    if(sh >= 1000f)
                        continue;
                    byte shade = (byte) (Math.min(Math.max((sh + midShading[x][y]) * 0.625f + 0.1328125f, 0f), 1f) * 255.999f);
                    byte sat = (byte) (Math.min(Math.max((saturation[x][y]) * 0.5f + 0.5f, 0f), 1f) * 255.999f);
//                    palettePixmap.drawPixel(x >>> shrink, y >>> shrink, (indices[x][y] & 255) << 24 |
//                            shade << 16 |
//                            sat << 8 | 255);
                    int idx = (y >>> shrink) * palettePixmap.getWidth() + (x >>> shrink) << 2;
                    if (shadows && index == -16) {
                        buffer.put(idx, (byte) 67); // shadow stuff
                        buffer.put(idx + 1, (byte) ((shade & 255) >>> 1));
                        buffer.put(idx + 2, (byte) 0);
//                        buffer.put(idx + 3, (byte) (255 - shade));
                        buffer.put(idx + 3, (byte) Math.min(Math.max(480 - (shade & 255) * 8, 0), 255));
                    } else {
                        buffer.put(idx, index);
                        buffer.put(idx + 1, shade);
                        buffer.put(idx + 2, sat);
                        buffer.put(idx + 3, (byte) 255);
                    }
                }
                else if(midShading[x][y] > 0f) {
                    int shade = (int) (Math.min(Math.max((shading[x][y] + midShading[x][y]) * 0.625f + 0.1328125f, 0f), 1f) * 255.999f);
                    int idx = (y >>> shrink) * palettePixmap.getWidth() + (x >>> shrink) << 2;
                    if ((buffer.get(idx+3) & 255) < shade) {
//                        palettePixmap.drawPixel(x >>> shrink, y >>> shrink, LIGHTEN << 24 |
//                                128 << 16 |
//                                128 << 8 | shade);
                        buffer.put(idx, lightIndices[x][y]);
                        buffer.put(idx+1, (byte) (shade + 256 >> 1));
                        buffer.put(idx+2, (byte) 64);
                        buffer.put(idx+3, (byte) shade);
                        outlineIndices[x][y] = 0;
                    }
                }
                else if(midShading[x][y] < 0f) {
                    int shade = (int) ((1f - Math.min(Math.max((shading[x][y] + midShading[x][y]) * 0.625f + 0.1328125f, 0f), 1f)) * 255.999f);
                    int idx = (y >>> shrink) * palettePixmap.getWidth() + (x >>> shrink) << 2;
                    if ((buffer.get(idx+3) & 255) < shade) {
//                        palettePixmap.drawPixel(x >>> shrink, y >>> shrink, DARKEN << 24 |
//                                128 << 16 |
//                                128 << 8 | shade);
                        buffer.put(idx, lightIndices[x][y]);
                        buffer.put(idx+1, (byte) (255 - shade >> 1));
                        buffer.put(idx+2, (byte) 64);
                        buffer.put(idx+3, (byte) shade);
                        outlineIndices[x][y] = 0;
                    }
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
        if (outline > 0) {
            for (int x = step; x <= xSize - step; x+= step) {
//                final int hx = x;
                final int hx = x >>> shrink;
                for (int y = step; y <= ySize - step; y+= step) {
//                    final int hy = y;
                    int hy = y >>> shrink;
                    if ((outlines[x][y]) == 1) {
                        depth = depths[x][y];
//                        int inner = 0x3880FFFF;
                        int inner = (outlineIndices[x][y] & 255) << 24 | (int) Math.min(Math.max(64f * outlineShading[x][y],  0f),  255f) << 16 | 64 << 8 | 255;
                        int outer = (outline >= 4) ? 0x010000FF : inner;
                        if (outlines[x - step][y] == 0) {
                            palettePixmap.drawPixel(hx - 1, hy, outer);
                        } else if (depths[x - step][y] < depth - threshold) {
                            palettePixmap.drawPixel(hx - 1, hy, inner);
                        }
                        if (outlines[x + step][y] == 0) {
                            palettePixmap.drawPixel(hx + 1, hy, outer);
                        } else if (depths[x + step][y] < depth - threshold) {
                            palettePixmap.drawPixel(hx + 1, hy, inner);
                        }
                        if (outlines[x][y - step] == 0) {
                            palettePixmap.drawPixel(hx, hy - 1, outer);
                        } else if (depths[x][y - step] < depth - threshold) {
                            palettePixmap.drawPixel(hx, hy - 1, inner);
                        }
                        if (outlines[x][y + step] == 0) {
                            palettePixmap.drawPixel(hx, hy + 1, outer);
                        } else if (depths[x][y + step] < depth - threshold) {
                            palettePixmap.drawPixel(hx, hy + 1, inner);
                        }

                        if (outlines[x - step][y - step] == 0) {
                            palettePixmap.drawPixel(hx - 1, hy - 1, outer);
                        }
                        if (outlines[x + step][y - step] == 0) {
                            palettePixmap.drawPixel(hx + 1, hy - 1, outer);
                        }
                        if (outlines[x - step][y + step] == 0) {
                            palettePixmap.drawPixel(hx - 1, hy + 1, outer);
                        }
                        if (outlines[x + step][y + step] == 0) {
                            palettePixmap.drawPixel(hx + 1, hy + 1, outer);
                        }

                    }
                }
            }
        }
        if(computeNormals) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    scharr(x, y);
                }
            }
            blur.filter(normals[0], normalMap.getWidth(), normalMap.getHeight());
            blur.filter(normals[1], normalMap.getWidth(), normalMap.getHeight());
            blur.filter(normals[2], normalMap.getWidth(), normalMap.getHeight());
            int color;
            for (int y = 0, h = normalMap.getHeight(); y < h; y++) {
                for (int x = 0, w = normalMap.getWidth(); x < w; x++) {
                    int idx = x + y * w;
                    color = Color.rgba8888(normals[0][idx], normals[1][idx], normals[2][idx], normals[3][idx]);
                    normalMap.drawPixel(x, y, color);
                }
            }
        }

        fill(depths, 0);
        fill(render, 0);
        fill(shading, 0f);
        fill(midShading, 0f);
        fill(saturation, 0f);
        fill(outlineShading, 0f);
        fill(outlines, (byte) 0);
        fill(indices, (byte) 0);
        fill(outlineIndices, (byte) 0);
        fill(lightIndices, (byte) 0);
        fill(voxels, -1);
        fill(shadeX, -1f);
        fill(shadeZ, -1f);
        for (int i = 0; i < materials.length; i++) {
            Arrays.fill(materials[i], null);
        }
        return palettePixmap;
    }

    public static void monoAlpha(Pixmap pm) {
        final ByteBuffer buffer = pm.getPixels();
        final int limit = buffer.limit();
        int alpha, rgba;
        for (int i = 3; i < limit; i += 4) {
            if((alpha = buffer.get(i)) < -1) {
                rgba = buffer.getInt(i - 3);
                buffer.putInt(i - 3, Coloring.lerp(0xC0C0C0FF, rgba | 255, (alpha & 255) / 255f));
            }
        }
    }

    public static void monoAlpha(Array<Pixmap> pms) {
        for(Pixmap pm : pms) {
            monoAlpha(pm);
        }
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
        if (shadows) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    ox = x - hs;
                    oy = y - hs;
                    oz = -hs;
                    splat(ox * x_x + oy * y_x + oz * z_x + size + translateX,
                            ox * x_y + oy * y_y + oz * z_y + size + translateY,
                            0, x, y, 0, (byte) -16, frame);
                }
            }
        }
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if (v != 0) {
                        ox = x - hs;
                        oy = y - hs;
                        oz = z - hs;
                        splat(ox * x_x + oy * y_x + oz * z_x + size + translateX,
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

    protected void subDraw(ArrayList<byte[][][]> grids, ArrayList<IntObjectMap<float[]>> links,
                           byte[][][] g, IntObjectMap<float[]> link, float yaw, float pitch, float roll, int frame,
                           float translateX, float translateY, float translateZ){
        splatOnly(g, yaw, pitch, roll, frame, translateX, translateY, translateZ);
        final int size = g.length;
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

        for(IntObjectMap.Entry<float[]> ent : link) {
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
        ArrayList<IntObjectMap<float[]>> links = new ArrayList<>(model.links.size());
        for (int i = 0; i < model.grids.size(); i++) {
            grids.add(Tools3D.deepCopy(model.grids.get(i)));
            if(i < model.links.size())
                links.add(new IntObjectMap<>(model.links.get(i)));
        }
        if(links.isEmpty()) {
            subDraw(grids, links, grids.remove(0), new IntObjectMap<>(0), yaw, pitch, roll, frame,
                    translateX, translateY, translateZ);
            return blit(yaw, pitch, roll, frame);
        }
        for (int i = 0; i < grids.size(); i++) {
            IntObjectMap<float[]> link = links.get(i);
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
