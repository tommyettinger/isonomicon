package isonomicon.physical;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.digital.MathTools;
import com.github.tommyettinger.digital.TrigTools;
import com.github.tommyettinger.random.ChopRandom;
import com.github.yellowstonegames.grid.IntPointHash;
import com.github.yellowstonegames.grid.LongPointHash;
import com.github.yellowstonegames.grid.Noise;
import isonomicon.io.VoxIO;
import isonomicon.visual.Coloring;

import java.io.InputStream;

import static com.github.tommyettinger.digital.Hasher.randomize3Bounded;
import static com.github.tommyettinger.digital.Hasher.randomize3;
import static com.github.tommyettinger.digital.MathTools.clamp;
import static com.github.yellowstonegames.grid.IntPointHash.hash32;
import static com.github.yellowstonegames.grid.IntPointHash.hashAll;
/**
 * Created by Tommy Ettinger on 11/4/2017.
 */
public class ModelMaker {
    public final boolean RINSED_PALETTE = true;
    public final byte EYE_DARK = RINSED_PALETTE ? 22 : 30;
    public final byte EYE_LIGHT = 17;
    public ChopRandom rng;
    private byte[][][] ship, shipLarge;
    private int xSize, ySize, zSize;

    private PaletteReducer colorizer;

    public ModelMaker()
    {
        this((long)((Math.random() - 0.5) * 0x1.0p52) ^ (long)((Math.random() - 0.5) * -0x1.0p64), new PaletteReducer());
    }
    public ModelMaker(long seed)
    {
        this(seed, new PaletteReducer());
    }
    public ModelMaker(long seed, PaletteReducer colorizer)
    {
        rng = new ChopRandom(seed);
        InputStream is = Gdx.files.internal("ship_12_12_8.vox").read();
        ship = VoxIO.readVox(is);
        if(ship == null) ship = new byte[12][12][8];
        is = Gdx.files.internal("ship_40_40_30.vox").read();
        shipLarge = VoxIO.readVox(is);
        if(shipLarge == null) shipLarge = new byte[40][40][30];
        xSize = ship.length;
        ySize = ship[0].length;
        zSize = ship[0][0].length;
        
        this.colorizer = colorizer;
    }
    public PaletteReducer getColorizer() {
        return colorizer;
    }

    public void setColorizer(PaletteReducer colorizer) {
        this.colorizer = colorizer;
    }
    /**
     * Gets a bounded int point hash of a 3D point (x, y, and z are all longs) and a state/seed as a long. This point
     * hash has just about the best speed of any algorithms tested, and though its quality is almost certainly bad for
     * traditional uses of hashing (such as hash tables), it's sufficiently random to act as a positional RNG.
     * <p>
     * This uses a technique related to the one used by Martin Roberts for his golden-ratio-based sub-random sequences,
     * where each axis is multiplied by a different constant, and the choice of constants depends on the number of axes
     * but is always related to a generalized form of golden ratios, repeatedly dividing 1.0 by the generalized ratio.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">Roberts' article</a>
     * for some more information on how he uses this, but we do things differently because we want random-seeming
     * results instead of separated sub-random results.
     * @param x x position; any long
     * @param y y position; any long
     * @param z z position; any long
     * @param s the state; any long
     * @param bound outer exclusive bound; may be negative
     * @return an int between 0 (inclusive) and bound (exclusive) dependent on the position and state
     */
    public static int hashBounded(long x, long y, long z, long s, int bound)
    {
        return (int)((bound * (LongPointHash.hashAll(x, y, z, s) & 0xFFFFFFFFL)) >> 32);
    }

    /**
     * Gets a bounded int point hash of a 4D point (x, y, z, and w are all longs) and a state/seed as a long. This point
     * hash has just about the best speed of any algorithms tested, and though its quality is almost certainly bad for
     * traditional uses of hashing (such as hash tables), it's sufficiently random to act as a positional RNG.
     * <p>
     * This uses a technique related to the one used by Martin Roberts for his golden-ratio-based sub-random sequences,
     * where each axis is multiplied by a different constant, and the choice of constants depends on the number of axes
     * but is always related to a generalized form of golden ratios, repeatedly dividing 1.0 by the generalized ratio.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">Roberts' article</a>
     * for some more information on how he uses this, but we do things differently because we want random-seeming
     * results instead of separated sub-random results.
     * @param x x position; any long
     * @param y y position; any long
     * @param z z position; any long
     * @param w w position (often time); any long
     * @param s the state; any long
     * @param bound outer exclusive bound; may be negative
     * @return an int between 0 (inclusive) and bound (exclusive) dependent on the position and state
     */
    public static int hashBounded(long x, long y, long z, long w, long s, int bound)
    {
        return (int)((bound * (LongPointHash.hashAll(x, y, z, w, s) & 0xFFFFFFFFL)) >> 32);
    }




    /**
     * Gets a bounded int point hash of a 2D point (x and y are both ints) and a state/seed as an int. This point
     * hash has just about the best speed of any algorithms tested, and though its quality is almost certainly bad for
     * traditional uses of hashing (such as hash tables), it's sufficiently random to act as a positional RNG.
     * <p>
     * This uses a technique related to the one used by Martin Roberts for his golden-ratio-based sub-random sequences,
     * where each axis is multiplied by a different constant, and the choice of constants depends on the number of axes
     * but is always related to a generalized form of golden ratios, repeatedly dividing 1.0 by the generalized ratio.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">Roberts' article</a>
     * for some more information on how he uses this, but we do things differently because we want random-seeming
     * results instead of separated sub-random results.
     * @param x x position; any int
     * @param y y position; any int
     * @param s the state; any int
     * @param bound outer exclusive bound; may be negative
     * @return an int between 0 (inclusive) and bound (exclusive) dependent on the position and state
     */
    public static int hashBounded(int x, int y, int s, int bound) {
        return (int)((bound * (IntPointHash.hashAll(x, y, s) & 0xFFFFFFFFL)) >> 32);
    }


    /**
     * Gets a bounded int point hash of a 3D point (x, y, and z are all ints) and a state/seed as an int. This point
     * hash has just about the best speed of any algorithms tested, and though its quality is almost certainly bad for
     * traditional uses of hashing (such as hash tables), it's sufficiently random to act as a positional RNG.
     * <p>
     * This uses a technique related to the one used by Martin Roberts for his golden-ratio-based sub-random sequences,
     * where each axis is multiplied by a different constant, and the choice of constants depends on the number of axes
     * but is always related to a generalized form of golden ratios, repeatedly dividing 1.0 by the generalized ratio.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">Roberts' article</a>
     * for some more information on how he uses this, but we do things differently because we want random-seeming
     * results instead of separated sub-random results.
     * @param x x position; any int
     * @param y y position; any int
     * @param z z position; any int
     * @param s the state; any int
     * @param bound outer exclusive bound; may be negative
     * @return an int between 0 (inclusive) and bound (exclusive) dependent on the position and state
     */
    public static int hashBounded(int x, int y, int z, int s, int bound)
    {
        return (int)((bound * (IntPointHash.hashAll(x, y, z, s) & 0xFFFFFFFFL)) >> 32);
    }

    /**
     * Gets a bounded int point hash of a 4D point (x, y, z, and w are all ints) and a state/seed as an int. This point
     * hash has just about the best speed of any algorithms tested, and though its quality is almost certainly bad for
     * traditional uses of hashing (such as hash tables), it's sufficiently random to act as a positional RNG.
     * <p>
     * This uses a technique related to the one used by Martin Roberts for his golden-ratio-based sub-random sequences,
     * where each axis is multiplied by a different constant, and the choice of constants depends on the number of axes
     * but is always related to a generalized form of golden ratios, repeatedly dividing 1.0 by the generalized ratio.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">Roberts' article</a>
     * for some more information on how he uses this, but we do things differently because we want random-seeming
     * results instead of separated sub-random results.
     * @param x x position; any int
     * @param y y position; any int
     * @param z z position; any int
     * @param w w position, often time; any int
     * @param s the state; any int
     * @param bound outer exclusive bound; may be negative
     * @return an int between 0 (inclusive) and bound (exclusive) dependent on the position and state
     */
    public static int hashBounded(int x, int y, int z, int w, int s, int bound)
    {
        return (int)((bound * (IntPointHash.hashAll(x, y, z, w, s) & 0xFFFFFFFFL)) >> 32);
    }
    
    public byte[][][] combine(byte[][][] start, byte[][][]... additional)
    {
        final int xSize = start.length, ySize = start[0].length, zSize = start[0][0].length;
        byte[][][] next = Tools3D.deepCopy(start);
        int[] startConn = new int[16], nextConn = new int[16];
        int[][] actualConn = new int[3][16];
        int nx = -1, ny = -1, nz = -1;
        Tools3D.findConnectors(start, startConn);
        for (int i = 0; i < 16; i++) {
            int c = startConn[i];
            if (c < 0)
                actualConn[0][i] = actualConn[1][i] = actualConn[2][i] = -1;
            else {
                actualConn[0][i] = c / (ySize * zSize);
                actualConn[1][i] = (c / zSize) % ySize;
                actualConn[2][i] = c % zSize;
                next[actualConn[0][i]][actualConn[1][i]][actualConn[2][i]] =  0;
            }
        }
        for(byte[][][] n : additional)
        {
            Tools3D.findConnectors(n, nextConn);
            for (int i = 0; i < 16; i++) {
                int c = nextConn[i];
                if (c >= 0 && actualConn[0][i] != -1)
                {
                    nx = c / (ySize * zSize);
                    ny = (c / zSize) % ySize;
                    nz = c % zSize;
                    Tools3D.translateCopyInto(n, next, actualConn[0][i] - nx, actualConn[1][i] - ny, actualConn[2][i] - nz);
                    next[actualConn[0][i]][actualConn[1][i]][actualConn[2][i]] = 0;
                    break;
                }
            }
        }
        return next;
    }
    
    public byte[][][] fullyRandom(boolean large)
    {
        final int side = large ? shipLarge.length : ship.length,
                high = large ? shipLarge[0][0].length : ship[0][0].length;
        byte[][][] voxels = new byte[side][side][high];
        byte mainColor = colorizer.randomColorIndex(rng),
                highlightColor = colorizer.reduceIndex(Coloring.lighten(colorizer.randomColor(rng), (rng.nextInt(5) - 2) * 0.15f));
        for (int x = 0; x < side; x++) {
            for (int y = 0; y < side; y++) {
                for (int z = 0; z < high; z++) {
                    voxels[x][y][z] = (rng.next(5) == 0) ? highlightColor : mainColor; //(rng.next(4) < 7) ? 0 :
                }
            }
        }
        return voxels;
    }
    public byte[][][] fishRandom()
    {
        byte[][][] voxels = new byte[12][12][8];
        int ctr;
        int current;
        final byte mainColor = colorizer.randomColorIndex(rng),
                highlightColor = colorizer.reduceIndex(Coloring.lighten(colorizer.randomColor(rng), (rng.nextInt(5) - 2) * 0.15f));
        do {
            final int seed = rng.nextInt();
            ctr = 0;
            for (int x = 0; x < 12; x++) {
                for (int y = 1; y < 6; y++) {
                    for (int z = 0; z < 8; z++) {
                        if (y > (Math.abs(x - 6) < 2 ? 4 - (seed >>> (31 - (seed & 1))) : 3)) {
                            //current = hashAll(x >> 1, y >> 1, z >> 1, seed);
                            current = hashAll(x, y, z, seed);
                            if ((voxels[x][11 - y][z] = voxels[x][y][z] =
                                    // + (60 - (x + 1) * (12 - x) + 6 - y) * 47
                                    (randomize3Bounded(current, //(11 - y * 2) * 23 +
                                            (Math.abs(x - 6) + 1) * (1 + Math.abs(z - 4)) * 15 +
                                                    (5 - y) * 355 +
                                            ((Math.abs(x - 7) + 3) * (Math.abs(z - 4) + 2) * (7 - y)) * 21) < 555) ?
                                            ((current & 0x3F) < 11 ? highlightColor : mainColor)
                                            : 0) != 0) ctr++;
                        } else {
                            voxels[x][11 - y][z] = voxels[x][y][z] = 0;
                        }
                    }
                }
            }
        }while (ctr < 45);
        voxels = Tools3D.largestPart(Tools3D.runCA(voxels, 1));
        for (int x = 10; x >= 5; x--) {
            for (int z = 7; z >= 2; z--) {
                for (int y = 1; y < 5; y++) {
                    if(voxels[x][y - 1][z] != 0) break;
                    if (voxels[x][y][z] != 0) {
                        voxels[x][12 - y][z] = voxels[x][y - 1][z] = EYE_DARK;
                        voxels[x][11 - y][z] = voxels[x][y    ][z] = EYE_DARK;
                        voxels[x + 1][12 - y][z] = voxels[x + 1][y    ][z] = EYE_DARK;     // intentionally asymmetrical
                        voxels[x + 1][11 - y][z] = voxels[x + 1][y - 1][z] = 4; // intentionally asymmetrical
                        if(x <= 9) {
                            voxels[x + 2][12 - y][z] = voxels[x + 2][y - 1][z] = 0;
                            voxels[x + 2][11 - y][z] = voxels[x + 2][y    ][z] = 0;
                        }
                        for (int i = z + 1; i < 8; i++) {
                            voxels[x][12 - y][i] = voxels[x][y - 1][i] = 0;
                            voxels[x][11 - y][i] = voxels[x][y    ][i] = 0;
                            voxels[x + 1][12 - y][i] = voxels[x + 1][y - 1][i] = 0;
                            voxels[x + 1][11 - y][i] = voxels[x + 1][y    ][i] = 0;
                            if(x <= 9) {
                                voxels[x + 2][12 - y][i] = voxels[x + 2][y - 1][i] = 0;
                                voxels[x + 2][11 - y][i] = voxels[x + 2][y    ][i] = 0;
                            }
                        }
                        for (int z2 = z - 2; z2 > 0; z2--) {
                            for (int x2 = 10; x2 >= x - 1; x2--) {
                                if (voxels[x2][6][z2] != 0) {
                                    voxels[x2+1][6][z2] = voxels[x2+1][5][z2] = voxels[x2][6][z2] = voxels[x2][5][z2] = highlightColor;
                                    return voxels;
                                }
                            }
                        }
                        return voxels;
                    }
                }
            }
        }
        return voxels;
    }

    public byte[][][][] animateFish(byte[][][] fish, final int frameCount)
    {
        final int xSize = fish.length, ySize = fish[0].length, zSize = fish[0][0].length;
        byte[][][][] frames = new byte[frameCount][xSize][ySize][zSize];
        float changeAmount = 1f / (frameCount);
        int adjustment;
        for (int f = 0; f < frameCount; f++) {
            for (int x = 0; x < xSize; x++) {
                adjustment = (int) (TrigTools.sinTurns(changeAmount * (f + x * 0.6f)) * 1.5f);
                for (int y = 1; y < ySize - 1; y++) {
                    System.arraycopy(fish[x][y], 0, frames[f][x][y + adjustment], 0, zSize);
                }
            }
        }
        return frames;
    }
    public byte[][][] shipRandom()
    {
        return shipSmoothColorized();
    }

    public byte[][][] shipLargeRandom()
    {
        return shipLargeSmoothColorized();
    }


    /**
     * Uses some simplex noise from {@link Noise} and some curving shapes from "Merlin Noise" to make paint patterns
     * and shapes more "flowing" and less haphazard in their placement. Still uses point hashes for some operations.
     * @return a 12x12x8 3D byte array representing a spaceship
     */
    public byte[][][] shipSmoothColorized()
    {
        return shipSmoothColorized(ship);
    }
    /**
     * Uses some simplex noise from {@link Noise} and some curving shapes from "Merlin Noise" to make paint patterns
     * and shapes more "flowing" and less haphazard in their placement. Still uses point hashes for some operations.
     * @return a larger (60x60x60) 3D byte array representing a spaceship
     */
    public byte[][][] shipLargeSmoothColorized()
    {
        final byte[][][] next = new byte[60][60][60];
        Tools3D.translateCopyInto(Tools3D.largestPart(shipSmoothColorized(shipLarge)), next, 10, 10, 15);
        return next;
    }
    /**
     * Uses some simplex noise from {@link Noise} and some curving shapes from "Merlin Noise" to make paint patterns
     * and shapes more "flowing" and less haphazard in their placement. Still uses point hashes for some operations.
     * @param ship one of the two ships loaded from resources here, probably, {@link #ship} and {@link #shipLarge}
     * @return 3D byte array representing a spaceship
     */
    private byte[][][] shipSmoothColorized(byte[][][] ship)
    {
        xSize = ship.length;
        ySize = ship[0].length;
        zSize = ship[0][0].length;
        byte[][][] nextShip = new byte[xSize][ySize][zSize];
        final int halfY = ySize >> 1, smallYSize = ySize - 1;
        int color;
        int seed = rng.nextInt(), current = seed;
        byte mainColor = (byte) (28), // bottom 15 bits
                //highlightColor = colorizer.brighten(colorizer.getReducer().paletteMapping[seed >>> 17]), // top 15 bits
                cockpitColor = 108,
                thrustColor = (byte)136;
//        byte lightColor = (byte) (colorizer.brighten(colorizer.getReducer().paletteMapping[(seed ^ seed >>> 4 ^ seed >>> 13) & 0x7FFF]) | colorizer.getShadeBit() | colorizer.getWaveBit());
//        for (int i = 0; i < grays.length; i++) {
//            if(highlightColor == grays[i])
//            {
//                highlightColor = colorizer.getReducer().paletteMapping[determineInt(~seed) & 0x7FFF];
//                break;
//            }
//        }
        final Noise noise = new Noise(seed ^ seed >>> 21 ^ seed << 6, 0xBp-2f / xSize);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < halfY; y++) {
                for (int z = 0; z < zSize; z++) {
                    color = (ship[x][y][z] & 255);
                    if (color != 0) {
                        current = (int) (noise.getFoam(x * 0.5f, y * 0.75f, z * 0.666f) * 0x1.5p27f) + 0x8000000;
                        if (color < 8) {
                            // checks sorta-top 4 bits
                            if((current >>> 21 & 15) != 0)
                                nextShip[x][smallYSize - y][z] = nextShip[x][y][z] = cockpitColor;
                        } else {
                            nextShip[x][smallYSize - y][z] = nextShip[x][y][z] =
                                    // checks sorta-top 9 bits, different branch
                                    ((current >>> 15 & 0x3FF) < color * 13)
                                            ? 0
                                            // checks 6 bits of paint
                                            : (merlin3D(x, y, z, seed) == 0) 
                                            ? (byte) ((noise.getFoam(x * 0.125f, y * 0.2f, z * 0.24f) * 0.4f + 0.599f) * 4 + 3)
                                            : (byte) MathTools.clamp(mainColor + (noise.getFoam(x * 0.0625f, y * 0.1f, z * 0.14f) * 2.25f), 26, 29);
                        }
                    }
                }
            }
        }
        current ^= current << 5 ^ current >>> 19;
        for (int y = 0; y < halfY; y++) {
            for (int x = xSize - 1; x > 0; x--) {
                int antennaHash = IntPointHash.hash256(x, y, ~current);
                if (antennaHash < 9) {
                    for (int z = zSize - 2; z >= 0; z--) {
                        if (nextShip[x][y][z] != 0 && nextShip[x][y][z] != cockpitColor) {
                            byte antennaColor = (byte) ((antennaHash & 3) + 3);
                            nextShip[x][smallYSize - y][z] = nextShip[x][y][z] =
                                    nextShip[x][smallYSize - y][z + 1] = nextShip[x][y][z + 1] = antennaColor;
                            if (antennaHash < 5 && z < zSize - 2) {
                                nextShip[x][smallYSize - y][z + 2] = nextShip[x][y][z + 2] = antennaColor;
                            }
                            break;
                        }
                    }
                }
            }             
            for (int z = 1; z < zSize; z++) {
                for (int x = 0; x < xSize; x++) {
                    if (hash32(z * 3 >>> 2, y * 5 + (z >>> 1) >>> 3, current) < 15) {
                        if (nextShip[x][y][z] != 0 && nextShip[x][y][z] != cockpitColor) {
                            nextShip[x - 1][smallYSize - y][z] = nextShip[x - 1][y][z] = thrustColor;
                            break;
                        }
                    }
                }
            }
        }
        return nextShip;
    }
    
    protected static int determine32(int state)
    {
        int z = (state *= 0xB79F5);
        z = (z ^ z >>> 15) * (z | 0xFFF0003D);
        z ^= z >>> 8;
        z = (z ^ z >>> 7) * (state >>> 12 | 1);
        return z ^ z >>> 14;

    }
    
    private static final int resolution = 2;
    private static int lorp(int start, int end, int a) {
        return ((1 << resolution) - a) * start + a * end >>> resolution;
    }

    public static int merlin3D(int x, int y, int z, int state) {
        int xb = (x >> resolution) + state, yb = (y >> resolution) - state, zb = (z >> resolution) + (0x9E3779B9 ^ state),
                xr = x & ~(-1 << resolution), yr = y & ~(-1 << resolution), zr = z & ~(-1 << resolution),
                x0 = determine32(xb), x1 = determine32(xb + 1),
                y0 = determine32(yb), y1 = determine32(yb + 1),
                z0 = determine32(zb), z1 = determine32(zb + 1),
                x0y0z0 = (x0 * y0 * z0 ^ x0 - y0 + (z0 - x0 << 16 | y0 - z0 >>> 16)) >>> resolution, x1y0z0 = (x1 * y0 * z0 ^ x1 - y0 + (z0 - x1 << 16 | y0 - z0 >>> 16)) >>> resolution,
                x0y1z0 = (x0 * y1 * z0 ^ x0 - y1 + (z0 - x0 << 16 | y1 - z0 >>> 16)) >>> resolution, x1y1z0 = (x1 * y1 * z0 ^ x1 - y1 + (z0 - x1 << 16 | y1 - z0 >>> 16)) >>> resolution,
                x0y0z1 = (x0 * y0 * z1 ^ x0 - y0 + (z1 - x0 << 16 | y0 - z1 >>> 16)) >>> resolution, x1y0z1 = (x1 * y0 * z1 ^ x1 - y0 + (z1 - x1 << 16 | y0 - z1 >>> 16)) >>> resolution,
                x0y1z1 = (x0 * y1 * z1 ^ x0 - y1 + (z1 - x0 << 16 | y1 - z1 >>> 16)) >>> resolution, x1y1z1 = (x1 * y1 * z1 ^ x1 - y1 + (z1 - x1 << 16 | y1 - z1 >>> 16)) >>> resolution;

        return lorp(lorp(lorp(x0y0z0, x1y0z0, xr), lorp(x0y1z0, x1y1z0, xr), yr),
                lorp(lorp(x0y0z1, x1y0z1, xr), lorp(x0y1z1, x1y1z1, xr), yr), zr)
                >>> 31 - resolution;
    }

    public byte[][][][] animateShip(byte[][][] spaceship, final int frameCount)
    {
        final int xSize = spaceship.length, ySize = spaceship[0].length, zSize = spaceship[0][0].length;
        byte[][][][] frames = new byte[frameCount][xSize][ySize][zSize];
        float changeAmount = 2f / (frameCount);
        int adjustment;
        for (int f = 0; f < frameCount; f++) {
            adjustment = (int) (MathTools.sway(changeAmount * f + 0.5f) * 1.75f) + 1;
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    System.arraycopy(spaceship[x][y], 1, frames[f][x][y], adjustment, zSize - 2);
                }
            }
        }
        return frames;
    }
    /**
     * Gets the minimum random int between 0 and {@code bound} generated out of {@code trials} generated numbers.
     * Useful for when numbers should have a strong bias toward zero, but all possible values are between 0, inclusive,
     * and bound, exclusive.
     * @param bound the outer exclusive bound; may be negative or positive
     * @param trials how many numbers to generate and get the minimum of
     * @return the minimum generated int between 0 and bound out of the specified amount of trials
     */
    public int minIntOf(final int bound, final int trials)
    {
        int value = rng.nextSignedInt(bound);
        for (int i = 1; i < trials; i++) {
            value = Math.min(value, rng.nextSignedInt(bound));
        }
        return value;
    }
    /**
     * Gets the maximum random int between 0 and {@code bound} generated out of {@code trials} generated numbers.
     * Useful for when numbers should have a strong bias away from zero, but all possible values are between 0,
     * inclusive, and bound, exclusive.
     * @param bound the outer exclusive bound; may be negative or positive
     * @param trials how many numbers to generate and get the maximum of
     * @return the maximum generated int between 0 and bound out of the specified amount of trials
     */
    public int maxIntOf(final int bound, final int trials)
    {
        int value = rng.nextSignedInt(bound);
        for (int i = 1; i < trials; i++) {
            value = Math.max(value, rng.nextSignedInt(bound));
        }
        return value;
    }

}
