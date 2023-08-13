package isonomicon.physical;

import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.random.EnhancedRandom;
import com.github.tommyettinger.digital.TrigTools;
import com.github.tommyettinger.random.WhiskerRandom;
import com.github.yellowstonegames.grid.IntPointHash;
import isonomicon.io.extended.VoxModel;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.floor;
import static isonomicon.io.VoxIO.lastMaterials;

public class EffectGenerator {

    @FunctionalInterface
    public interface Effect {
        VoxModel[] runEffect(VoxModel[] frames, int which);
    }

    @FunctionalInterface
    public interface ReceiveEffect {
        VoxModel[] runEffect(int size, int frames, int strength);
    }

    public static final ObjectObjectOrderedMap<String, Effect> KNOWN_EFFECTS = new ObjectObjectOrderedMap<>(
            new String[]{"Handgun", "Machine_Gun", "Forward_Cannon", "Arc_Cannon",
                    "Forward_Missile", "Arc_Missile", "Torpedo", "Flame_Wave", "Bomb_Drop", "Hack", "Debug"},
            new Effect[]{EffectGenerator::handgunAnimation, EffectGenerator::machineGunAnimation,
                    EffectGenerator::forwardCannonAnimation, EffectGenerator::arcCannonAnimation,
                    EffectGenerator::forwardMissileAnimation, EffectGenerator::arcMissileAnimation,
                    EffectGenerator::torpedoAnimation, EffectGenerator::flameWaveAnimation,
                    EffectGenerator::bombDropAnimation, EffectGenerator::hackAnimation,
//                    EffectGenerator::debugAnimation
            }
    );

    public static final ObjectObjectOrderedMap<String, ReceiveEffect> KNOWN_RECEIVE_EFFECTS = new ObjectObjectOrderedMap<>(
            new String[]{"Handgun", "Machine_Gun", "Forward_Cannon", "Arc_Cannon", "Forward_Missile", "Arc_Missile",
                    "Torpedo", "Flame_Wave", "Bomb_Drop", "Hack",
                    "Debug"},
            new ReceiveEffect[]{EffectGenerator::handgunReceiveAnimation, EffectGenerator::machineGunReceiveAnimation,
                    EffectGenerator::forwardCannonReceiveAnimation, EffectGenerator::arcCannonReceiveAnimation,
                    EffectGenerator::forwardMissileReceiveAnimation, EffectGenerator::arcMissileReceiveAnimation,
                    EffectGenerator::torpedoReceiveAnimation, EffectGenerator::flameWaveReceiveAnimation,
                    EffectGenerator::bombDropReceiveAnimation, EffectGenerator::hackReceiveAnimation,
//                    EffectGenerator::debugReceiveAnimation
            }
    );

    public static final WhiskerRandom r = new WhiskerRandom(123456789L);

    public static final int missileBody = 5;
    public static final int missileHead = 27;
    public static final int shadow = 66;
    public static final int smoke = 67;
    public static final int shock = 97;
    public static final int ember = 113;
    public static final int hotFire = 114;
    public static final int yellowFire = 115;
    public static final int sparks = 127;
    public static final int flicker = 140;
    public static final int launch = 201;
    public static final int trail = 202;
    public static final int glow = 81;
    public static final int red = 103;
    public static final int terminal = 163;


    public static byte[][][][] fireballAnimation(byte[][][] initial, int frames, int trimLevel, int blowback){
        final int xSize = initial.length, ySize = initial[0].length, zSize = initial[0][0].length;
        byte[][][][] result = new byte[frames][xSize][ySize][zSize];
        for (int f = 0, f1 = 1; f < frames; f++, f1++) {
            final float fr = f1 / (float)frames;
            byte[][][] vls;
            if(f == 0)
                vls = initial;
            else
                vls = result[f-1];
            byte[][][] working = result[f];
            int xLimitLow = Integer.MAX_VALUE, xLimitHigh = Integer.MIN_VALUE;
            int yLimitLow = Integer.MAX_VALUE, yLimitHigh = Integer.MIN_VALUE;
            int zLimitLow = Integer.MAX_VALUE, zLimitHigh = Integer.MIN_VALUE;
            OUTER:
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            xLimitLow = x;
                            break OUTER;
                        }
                    }
                }
            }
            OUTER:
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            yLimitLow = y;
                            break OUTER;
                        }
                    }
                }
            }
            OUTER:
            for (int z = 0; z < zSize; z++) {
                for (int y = 0; y < ySize; y++) {
                    for (int x = 0; x < xSize; x++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            zLimitLow = z;
                            break OUTER;
                        }
                    }
                }
            }

            OUTER:
            for (int x = xSize - 1; x >= 0; x--) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            xLimitHigh = x;
                            break OUTER;
                        }
                    }
                }
            }

            OUTER:
            for (int y = ySize - 1; y >= 0; y--) {
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            yLimitHigh = y;
                            break OUTER;
                        }
                    }
                }
            }
            OUTER:
            for (int z = zSize - 1; z>= 0; z--) {
                for (int x = 0; x < xSize; x++) {
                    for (int y = 0; y < ySize; y++) {
                        if((vls[x][y][z] - 1 & 255) < 191){
                            zLimitHigh = z;
                            break OUTER;
                        }
                    }
                }
            }

            float xMiddle = (xLimitHigh + xLimitLow) * 0.5f;
            float yMiddle = (yLimitHigh + yLimitLow) * 0.5f;
            float zMiddle = (zLimitHigh + zLimitLow) * 0.5f;
            float xRange = 1.5f / (xLimitHigh - xLimitLow);
            float yRange = 1.5f / (yLimitHigh - yLimitLow);
            float zRange = 1.5f / (zLimitHigh - zLimitLow);

            final float SPREAD = 42f;
            final float magnitude = SPREAD * 0.2f / (fr * fr);
            final float LIMIT = xSize * 0.15f;

            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        byte color = vls[x][y][z];
                        if ((color - 1 & 255) < 191) {
                            if((color == hotFire || color == yellowFire || color == smoke) && r.nextInt(f1 + f1) >= frames) {
                                color = smoke;
                            }
                            float zMove = f1 * ((r.nextFloat() + r.nextFloat()) * 0.8f + 0.3f);
                            float xMove = r.nextFloat(2f) - 1f;
                            float yMove = r.nextFloat(2f) - 1f;
//                            final float magnitude = (float)Math.sqrt(xMove * xMove + yMove * yMove);
                            int usedX = x, usedY = y, usedZ = z;
                            if(xMove > 0)
                            {
                                float nv = x + r.nextFloat(Math.min(LIMIT, xMove * magnitude)) + (r.nextFloat(3f) - 1.5f);
                                if(nv < 1) nv = 1;
                                else if(nv > xSize - 2) nv = xSize - 2;
                                usedX = ((blowback <= 0) ? floor(nv) : ceil(nv));
                            }
                            else if(xMove < 0)
                            {
                                float nv = x - r.nextFloat(Math.min(LIMIT, xMove * -magnitude)) + (r.nextFloat(3f) - 1.5f);
                                if(nv < 1) nv = 1;
                                else if(nv > xSize - 2) nv = xSize - 2;
                                usedX = ((blowback > 0) ? floor(nv) : ceil(nv));
                            }
                            else
                            {
                                if(x < 1) usedX = 1;
                                else if(x > xSize - 2) usedX = xSize - 2;
                            }
                            if(yMove > 0)
                            {
                                float nv = y + r.nextFloat(Math.min(LIMIT, yMove * magnitude)) + (r.nextFloat(3f) - 1.5f);
                                if(nv < 1) nv = 1;
                                else if(nv > ySize - 2) nv = ySize - 2;
                                usedY = ((blowback <= 0) ? floor(nv) : ceil(nv));
                            }
                            else if(yMove < 0)
                            {
                                float nv = y - r.nextFloat(Math.min(LIMIT, yMove * -magnitude)) + (r.nextFloat(3f) - 1.5f);
                                if(nv < 1) nv = 1;
                                else if(nv > ySize - 2) nv = ySize - 2;
                                usedY = ((blowback > 0) ? floor(nv) : ceil(nv));
                            }
                            else
                            {
                                if(y < 1) usedY = 1;
                                else if(y > ySize - 2) usedY = ySize - 2;
                            }
                            if(zMove != 0)
                            {
                                float nv = (z + (zMove / (0.35f + 0.14f * (f1 + 3f))));

                                /*if(nv <= 0 && f1 < frames && NOT_FIRE) nv = r.next(1); //bounce
                                else*/ if(nv < 0) nv = 0;

                                if(nv > zSize - 1)
                                {
                                    continue;
                                }
                                usedZ = MathUtils.roundPositive(nv);
                            }
                            if(randomChoice(r, trimLevel, fr, usedX, usedY, usedZ, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                working[usedX][usedY][usedZ] = color;

                            if(r.nextInt(frames) > f1 + frames / 6 && r.nextInt(frames) > f1 + 2) {
                                if(usedX > 0 && (working[usedX - 1][usedY][usedZ] == 0) && randomChoice(r, trimLevel, fr, usedX - 1, usedY, usedZ, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX - 1][usedY][usedZ] = randomFire(r);
                                if(usedX < xSize - 1 && (working[usedX + 1][usedY][usedZ] == 0) && randomChoice(r, trimLevel, fr, usedX + 1, usedY, usedZ, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX + 1][usedY][usedZ] = randomFire(r);
                                if(usedY > 0 && (working[usedX][usedY - 1][usedZ] == 0) && randomChoice(r, trimLevel, fr, usedX, usedY - 1, usedZ, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX][usedY - 1][usedZ] = randomFire(r);
                                if(usedY < ySize - 1 && (working[usedX][usedY + 1][usedZ] == 0) && randomChoice(r, trimLevel, fr, usedX, usedY + 1, usedZ, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX][usedY + 1][usedZ] = randomFire(r);
                                if(usedZ > 0 && (working[usedX][usedY][usedZ - 1] == 0) && randomChoice(r, trimLevel, fr, usedX, usedY, usedZ - 1, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX][usedY][usedZ - 1] = randomFire(r);
                                if(usedZ < zSize - 1 && (working[usedX][usedY][usedZ + 1] == 0) && randomChoice(r, trimLevel, fr, usedX, usedY, usedZ + 1, xMiddle, yMiddle, zMiddle, xRange, yRange, zRange))
                                    working[usedX][usedY][usedZ + 1] = randomFire(r);
                            }
                        }
                    }
                }
            }
            Stuff.evolve(Stuff.STUFFS_B, working, f);
        }
        return result;
    }

    public static boolean randomChoice(EnhancedRandom r, int trim, float fr, int x, int y, int z,
                                       float midX, float midY, float midZ,
                                       float rangeX, float rangeY, float rangeZ){
        return r.nextInt(9) < 10 - trim && r.nextExclusiveFloat(12f) < 15f * (1.125f - fr * fr)
                - Math.abs(x - midX) * rangeX
                - Math.abs(y - midY) * rangeY
                - Math.abs(z - midZ) * rangeZ;
    }
    //67, 67, 67, 113, 114, 114, 114, 114, 114, 115, 115, 115, 115, 119, 119, 127
    public static byte randomFire(EnhancedRandom r){
        switch (r.next(4)){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return yellowFire;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return hotFire;
            default:
                return smoke;
        }
    }

    public static byte[][][][] burst(byte[][][][] grids, int xInitial, int yInitial, int zInitial, int startFrame, int frames, boolean big) {
        for (int n = 0, runs = r.nextInt(1, 4); n < runs; n++) {
            float angle = r.nextFloat(), yAngle = TrigTools.sinTurns(angle), xAngle = TrigTools.cosTurns(angle),
                    zAngle = TrigTools.acosTurns(r.nextFloat());
            int x = xInitial + r.nextInt(-8, 9);
            int y = yInitial + r.nextInt(-6, 7);
            int z = zInitial + r.nextInt(5);
            for (int f = startFrame, p = 0; p < frames && f < grids.length; f++, p++) {
                if (p == 0) {
                    grids[f][x][y][z] = sparks;
                } else {
                    for (int i = p; i <= p + (big ? p + 3 + n : p); i++) {
                        if(r.next(6) < 50)
                            grids[f]
                                    [Math.min(Math.max(MathUtils.round(x + i * xAngle),  0),  grids[0].length)]
                                    [Math.min(Math.max(MathUtils.round(y + i * yAngle),  0),  grids[0][0].length)]
                                    [Math.min(Math.max(MathUtils.round(z + i * zAngle),  0),  grids[0][0][0].length)] = sparks;
                    }
                }
            }
        }
        return grids;
    }

    public static VoxModel[] machineGunAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            Choice majorLimit = ((x, y, z) -> r.nextInt(10) > 2);
            Choice minorLimit = ((x, y, z) -> r.nextInt(10) > 1);
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);
                int currentlyFiring = f % ((launchers.size() >>> 2) + 1);
                if ((currentlyFiring & 1) == 0) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        if (currentlyFiring != 0) {
                            currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                            continue;
                        }
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly, lz, sparks, minorLimit);

                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz + 2, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz - 2, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz, sparks, minorLimit);

                        ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly + 2, lz + 2, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly - 2, lz + 2, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly + 2, lz - 2, sparks, minorLimit);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly - 2, lz - 2, sparks, minorLimit);
                    }
                } else {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        if (currentlyFiring < 2 && launchers.size() > 8) {
                            currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                            continue;
                        }
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx + 2, ly, lz, lx + 9, ly, lz, yellowFire, majorLimit);

                        currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] handgunAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);
                int currentlyFiring = f & 3;
                if(currentlyFiring < launchers.size())
                {
                    long launcher = launchers.get(currentlyFiring);
                    int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                    ShapeGenerator.box(grid, lx, ly-1, lz-1, lx+8, ly+1, lz+1, yellowFire);
                    ShapeGenerator.box(grid, lx+2, ly+1, lz-1, lx+4, ly+3, lz+1, hotFire);
                    ShapeGenerator.box(grid, lx+2, ly-3, lz-1, lx+4, ly-1, lz+1, hotFire);
                    ShapeGenerator.box(grid, lx+2, ly-1, lz+1, lx+4, ly+1, lz+3, hotFire);
                    ShapeGenerator.box(grid, lx+2, ly-1, lz-3, lx+4, ly+1, lz-1, hotFire);
                }
                if(currentlyFiring <= launchers.size() && currentlyFiring > 0)
                {
                    long launcher = launchers.get(currentlyFiring-1);
                    int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                    ShapeGenerator.box(grid, lx+4, ly-1, lz-1, lx+10, ly+1, lz+1, yellowFire);
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] forwardCannonAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        Choice choose1of80 = ((x, y, z) -> r.nextInt(80) == 0);
        Choice choose1of140 = ((x, y, z) -> r.nextInt(140) == 0);
        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);

                if(f == 0 || f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 18, ly, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly, lz + 8, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly, lz - 8, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 8, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 8, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 5, lz + 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 5, lz - 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 5, lz + 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 5, lz - 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly + 3, lz + 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly + 3, lz - 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly - 3, lz + 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly - 3, lz - 5, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly + 5, lz + 3, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly + 5, lz - 3, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly - 5, lz + 3, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 14, ly - 5, lz - 3, yellowFire - r.next(1), choose3of4);
                    }
                }

                if(f == 1 || f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz + 6, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz - 6, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 6, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 6, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 2, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 2, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 2, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 2, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 4, lz + 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 4, lz - 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 4, lz + 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 4, lz - 2, yellowFire - r.next(1), choose3of4);
                    }
                }
                else if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz + 2, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz + r.next(1), smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz + 2, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz + r.next(1), smoke, choose1of2);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz + 2, smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + r.nextInt(2, 4), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + r.nextInt(4, 6), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + r.nextInt(2, 4), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + r.nextInt(4, 6), smoke, choose1of80);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 3, ly + 5, lz + r.nextInt(5, 8), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 3, ly - 5, lz + r.nextInt(5, 8), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 5, ly + 5, lz + r.nextInt(5, 8), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 5, ly - 5, lz + r.nextInt(5, 8), 1.45, smoke, choose1of140);
                    }
                }
                else if(f == 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + r.nextInt(5, 9), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + r.nextInt(5, 9), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 7), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 7), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                    }
                }
                else if(f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + r.nextInt(6, 10), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + r.nextInt(6, 10), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 9), lz + r.nextInt(9, 16), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 9), lz + r.nextInt(9, 16), 1.45, smoke, choose1of512);
                    }
                }
                if(f == 2){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -3, 0, 0));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 3f;
                    }
                }
                else if(f == 3){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -2, 0, 0));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 2f;
                    }
                }
                else if(f == 4){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -1, 0, 0));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 1f;
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] arcCannonAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        Choice choose1of80 = ((x, y, z) -> r.nextInt(80) == 0);
        Choice choose1of140 = ((x, y, z) -> r.nextInt(140) == 0);
        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                final int fr = f;
                byte[][][] grid = next[f+1].grids.get(g);
                if(f == 0 || f == 1 || f == 2) {
                    Choice chooseFrameBased = ((x, y, z) -> r.nextInt(6) > fr && r.nextInt(5) > 1);
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 18, ly, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly + 6, lz + 12, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly - 6, lz + 12, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly + 7, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly + 7, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly - 7, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly - 7, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly + 6, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly + 6, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly - 6, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly - 6, lz + 18, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 9, ly + 9, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 9, ly + 9, lz + 12, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 9, ly - 9, lz + 6, yellowFire - r.next(1), chooseFrameBased);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 9, ly - 9, lz + 12, yellowFire - r.next(1), chooseFrameBased);
                    }
                }

                if(f == 1 || f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz + 6, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz - 6, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 6, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 6, lz, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 2, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 2, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 2, lz + 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 2, lz - 4, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 4, lz + 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly + 4, lz - 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 4, lz + 2, yellowFire - r.next(1), choose3of4);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 10, ly - 4, lz - 2, yellowFire - r.next(1), choose3of4);
                    }
                }
                else if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly, lz, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly + 3, lz + 1 + r.next(1), smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly + 3, lz + 4 + r.next(1), smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly - 3, lz + 1 + r.next(1), smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly - 3, lz + 4 + r.next(1), smoke, choose1of2);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx + 3, ly, lz + 1 + r.next(1), lx + 7, ly, lz + 4 + r.next(1), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 3, ly, lz + 4 + r.next(1), lx + 7, ly + 3, lz + 7 + r.next(1), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 3, ly, lz + 3, lx + 7, ly + 3, lz + 6, smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 3, ly, lz + 4 + r.next(1), lx + 7, ly - 3, lz + 7 + r.next(1), smoke, choose1of80);
                        ShapeGenerator.line(grid, lx + 3, ly, lz + 3, lx + 7, ly - 3, lz + 6, smoke, choose1of80);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 4 + r.next(1), ly + (7 + r.next(1)), lz + r.nextInt(6, 9), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 4 + r.next(1), ly - (7 + r.next(1)), lz + r.nextInt(6, 9), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 6 + r.next(1), ly + (7 + r.next(1)), lz + r.nextInt(6, 9), 1.45, smoke, choose1of140);
                        ShapeGenerator.ball(grid, lx + 6 + r.next(1), ly - (7 + r.next(1)), lz + r.nextInt(6, 9), 1.45, smoke, choose1of140);
                    }
                }
                else if(f == 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 7 + r.next(1), ly + (7 + r.next(1)), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 7 + r.next(1), ly - (7 + r.next(1)), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 7 + r.next(1), ly + r.nextInt(4, 12), lz + r.nextInt(13, 18), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 7 + r.next(1), ly - r.nextInt(4, 12), lz + r.nextInt(13, 18), 1.45, smoke, choose1of256);
                    }
                }
                else if(f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + r.nextInt(6, 10), ly + (7 + r.next(1)), lz + r.nextInt(10, 17), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + r.nextInt(6, 10), ly - (7 + r.next(1)), lz + r.nextInt(10, 17), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + r.nextInt(6, 10), ly + r.nextInt(4, 12), lz + r.nextInt(15, 21), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + r.nextInt(6, 10), ly - r.nextInt(4, 12), lz + r.nextInt(15, 21), 1.45, smoke, choose1of512);
                    }
                }
                if(f == 2){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -2, 0, -1));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 2f;
                        fa[2] -= 1f;
                    }
                }
                else if(f == 3){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -1, 0, -1));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 1f;
                        fa[2] -= 1f;
                    }
                }
                else if(f == 4){
                    next[f+1].grids.set(g, Tools3D.translateCopy(grid, -1, 0, 0));
                    for(float[] fa : next[f+1].links.get(g).values()){
                        fa[0] -= 1f;
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] forwardMissileAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        Choice choose4of5 = ((x, y, z) -> r.nextInt(5) != 0);
        Choice choose1of140 = ((x, y, z) -> r.nextInt(140) == 0);
        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            LongOrderedSet ts = next[0].markers.get(g).get(trail + which * 8);
            foundAny = true;
            LongList launchers = new LongList(ls.size() >>> 3);
            for (int i = 0; i < ls.size(); i++) {
                long launcher = ls.getAt(i);
                int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                // dependent on whether the model was doubled in resolution or more; this assumes doubled.
                if(((lx | ly | lz) & 1L) == 0) {
                    launchers.add(launcher);
                }
            }
            LongList trails = ts == null ? new LongList() : ts.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);

                if(f == 0) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 3, ly+3, lz+3, missileHead);
                    }
                }
                else if(f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 12, ly, lz, lx + 15, ly + 3, lz + 3, missileHead);
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 11, ly + 3, lz + 3, missileBody);
                    }
                    for (int tr = 0; tr < trails.size(); tr++) {
                        long trail = trails.get(tr);
                        int lx = ((int) (trail) & 0xFFFFF), ly = ((int) (trail >>> 20) & 0xFFFFF), lz = (int) (trail >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx - 8, ly, lz, lx, ly, lz, yellowFire);
                        ShapeGenerator.box(grid, lx - 6, ly-1, lz-1, lx, ly+1, lz+1, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly-2, lz, lx, ly+2, lz, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly, lz-2, lx, ly, lz+2, hotFire);
                    }
                }
                else if(f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 28, ly, lz, lx + 31, ly + 3, lz + 3, missileHead);
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 27, ly + 3, lz + 3, missileBody);
                    }
                    for (int tr = 0; tr < trails.size(); tr++) {
                        long trail = trails.get(tr);
                        int lx = ((int) (trail) & 0xFFFFF), ly = ((int) (trail >>> 20) & 0xFFFFF), lz = (int) (trail >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx - 8, ly, lz, lx - 2, ly, lz, yellowFire);
                        ShapeGenerator.box(grid, lx - 6, ly-1, lz-1, lx, ly+1, lz+1, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly-2, lz, lx-1, ly+2, lz, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly, lz-2, lx-1, ly, lz+2, hotFire);

                        ShapeGenerator.ball(grid, lx - 8, ly + 4, lz, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 8, ly - 4, lz, 1.45, smoke, choose3of4);
                        ShapeGenerator.box(grid, lx - 12, ly - 4, lz - 1, lx - 8, ly + 4, lz + 1, smoke, choose1of2);

                        ShapeGenerator.ball(grid, lx - 4, ly + 4, lz + 2, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 4, ly - 4, lz + 2, 1.45, smoke, choose3of4);
                        ShapeGenerator.box(grid, lx - 12, ly - 4, lz + 1, lx - 4, ly + 4, lz + 3, smoke, choose1of2);
                    }
                }
                else if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 48, ly, lz, lx + 51, ly + 3, lz + 3, missileHead);
                        ShapeGenerator.box(grid, lx + 20, ly, lz, lx + 47, ly + 3, lz + 3, missileBody);
                        ShapeGenerator.box(grid, lx + 8, ly, lz, lx + 19, ly + 3, lz + 3, yellowFire);
                    }
                    for (int tr = 0; tr < trails.size(); tr++) {
                        long trail = trails.get(tr);
                        int lx = ((int) (trail) & 0xFFFFF), ly = ((int) (trail >>> 20) & 0xFFFFF), lz = (int) (trail >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx - 8, ly, lz, lx - 2, ly, lz, yellowFire);
                        ShapeGenerator.box(grid, lx - 6, ly-1, lz-1, lx, ly+1, lz+1, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly-2, lz, lx-1, ly+2, lz, hotFire);
                        ShapeGenerator.box(grid, lx - 4, ly, lz-2, lx-1, ly, lz+2, hotFire);

                        ShapeGenerator.box(grid, lx - 10, ly - 2, lz - 2, lx - 5, ly + 2, lz + 2, hotFire, choose4of5);
                        ShapeGenerator.box(grid, lx - 12, ly - 5, lz - 1, lx - 8, ly + 5, lz + 1, hotFire, choose4of5);
                        ShapeGenerator.box(grid, lx - 12, ly - 1, lz - 5, lx - 8, ly + 1, lz + 5, hotFire, choose4of5);

                        ShapeGenerator.box(grid, lx - 16, ly - 4, lz, lx - 11, ly + 4, lz + 7, smoke, choose4of5);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 48 + 20, ly, lz, lx + 51 + 20, ly + 3, lz + 3, missileHead);
                        ShapeGenerator.box(grid, lx + 20 + 20, ly, lz, lx + 47 + 20, ly + 3, lz + 3, missileBody);
                        ShapeGenerator.box(grid, lx + 8 + 20, ly, lz, lx + 19 + 20, ly + 3, lz + 3, yellowFire);
                    }
                    for (int tr = 0; tr < trails.size(); tr++) {
                        long trail = trails.get(tr);
                        int lx = ((int) (trail) & 0xFFFFF), ly = ((int) (trail >>> 20) & 0xFFFFF), lz = (int) (trail >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx - 12, ly + 4 + r.nextInt(-4, 5), lz, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 12, ly - 4 + r.nextInt(-4, 5), lz, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 12, ly + 4 + r.nextInt(-4, 5), lz + 2, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 12, ly - 4 + r.nextInt(-4, 5), lz + 2, 1.45, smoke, choose3of4);

                        ShapeGenerator.ball(grid, lx - 16, ly + 4 + r.nextInt(-4, 5), lz + 1, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 16, ly - 4 + r.nextInt(-4, 5), lz + 1, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 16, ly + 4 + r.nextInt(-4, 5), lz + 3, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 16, ly - 4 + r.nextInt(-4, 5), lz + 3, 1.45, smoke, choose3of4);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 48 + 20*2, ly, lz, lx + 51 + 20*2, ly + 3, lz + 3, missileHead);
                        ShapeGenerator.box(grid, lx + 20 + 20*2, ly, lz, lx + 47 + 20*2, ly + 3, lz + 3, missileBody);
                        ShapeGenerator.box(grid, lx + 8 + 20*2, ly, lz, lx + 19 + 20*2, ly + 3, lz + 3, yellowFire);
                    }
                    for (int tr = 0; tr < trails.size(); tr++) {
                        long trail = trails.get(tr);
                        int lx = ((int) (trail) & 0xFFFFF), ly = ((int) (trail >>> 20) & 0xFFFFF), lz = (int) (trail >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx - 16, ly + 4 + r.nextInt(-4, 5), lz + 4, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 16, ly - 4 + r.nextInt(-4, 5), lz + 4, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 20, ly + 4 + r.nextInt(-4, 5), lz + 6, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 20, ly - 4 + r.nextInt(-4, 5), lz + 6, 1.45, smoke, choose3of4);
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static void addMissile(byte[][][] grid, int lx, int ly, int lz, int dx, int dy, int dz, int distance){
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        ShapeGenerator.box(grid, lx + (4 + distance) * dx, ly + 1, lz + 4 + distance * dz, lx + (10 + distance) * dx, ly + 6, lz + (10 + distance) * dz, missileHead);
        for (int y = 1; y <= 6; y++) {
            for (int d = 0; d < 6; d++) {
                ShapeGenerator.line(grid, lx + (-22+distance)*dx+d, ly+y, lz+ (-16+distance) * dz -d, lx + (distance + 4) * dx + d, ly + y, lz + (distance + 10) * dz - d, missileBody);
            }
        }
        for (int y = 2; y <= 5; y++) {
            for (int d = 1; d < 5; d++) {
                ShapeGenerator.line(grid, lx + (-34+distance)*dx+d, ly+y, lz + (-28+distance)*dz-d, lx+(-22+distance)*dx+d, ly+y, lz+ (-16+distance) * dz-d, yellowFire);
            }
        }
        for (int y = 1; y <= 6; y++) {
            for (int d = 0; d < 6; d++) {
                ShapeGenerator.line(grid, lx + (-32+distance)*dx+d, ly+y, lz+(-26+distance)*dz-d, lx+(-22+distance)*dx+d, ly+y, lz+(-16+distance) * dz-d, hotFire, choose3of4);
            }
        }

    }

    public static VoxModel[] arcMissileAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        Choice choose4of5 = ((x, y, z) -> r.nextInt(5) != 0);
        Choice choose1of20 = ((x, y, z) -> r.nextInt(20) == 0);
        Choice choose1of5 = ((x, y, z) -> r.nextInt(5) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);
        Choice choose1of4 = ((x, y, z) -> r.next(2) == 0);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = new LongList(ls.size() >>> 3);
            for (int i = 0; i < ls.size(); i++) {
                long launcher = ls.getAt(i);
                int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                // dependent on whether the model was doubled in resolution or more; this assumes doubled.
                if(((lx | ly | lz) & 1L) == 0) {
                    launchers.add(launcher);
                }
            }
            for (int f = 0; f < count - 1; f++) {
                byte[][][] grid = next[f+1].grids.get(g);

                if(f == 0) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        ShapeGenerator.box(grid, lx - 4, ly + 1, lz - 4, lx + 2, ly + 6, lz + 2, missileHead);
                    }
                }
                else if(f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        ShapeGenerator.box(grid, lx + 4, ly + 1, lz + 4, lx + 10, ly + 6, lz + 10, missileHead);
                        for (int y = 1; y <= 6; y++) {
                            for (int d = 0; d < 6; d++) {
                                ShapeGenerator.line(grid, lx-10+d, ly+y, lz-4-d, lx + 4 + d, ly + y, lz + 10 - d, missileBody);
                            }
                        }
                    }
                }
                else if(f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        ShapeGenerator.box(grid, lx + 4 + 16, ly + 1, lz + 4 + 16, lx + 10 + 16, ly + 6, lz + 10 + 16, missileHead);
                        for (int y = 1; y <= 6; y++) {
                            for (int d = 0; d < 6; d++) {
                                ShapeGenerator.line(grid, lx-6+d, ly+y, lz-d, lx + 16 + 4 + d, ly + y, lz + 16 + 10 - d, missileBody);
                            }
                        }
                    }
                }
                else if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        addMissile(grid, lx, ly, lz, 1, 0, 1, 16 * 2);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        addMissile(grid, lx, ly, lz, 1, 0, 1, 16 * 3);
                        ShapeGenerator.ball(grid, lx - 1 + r.next(2), ly - 1 + r.next(2), lz + 2, 1.45, smoke, choose3of4);
                        ShapeGenerator.ball(grid, lx - 3 + r.next(3), ly - 3 + r.next(3), lz + 1, 1.45, smoke, choose3of4);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        addMissile(grid, lx, ly, lz, 1, 0, 1, 16 * 4);
                        ShapeGenerator.ball(grid, lx - 1 + r.next(2), ly - 1 + r.next(2), lz + 4, 1.45, smoke, choose1of2);
                        ShapeGenerator.ball(grid, lx - 3 + r.next(3), ly - 3 + r.next(3), lz + 3, 1.45, smoke, choose1of2);
                        ShapeGenerator.ball(grid, lx - 4 + r.nextInt(10), ly - 4 + r.nextInt(10), lz + 2, 1.45, smoke, choose1of2);
                        ShapeGenerator.ball(grid, lx - 5 + r.nextInt(12), ly - 5 + r.nextInt(12), lz + 1, 1.45, smoke, choose1of2);
                    }
                }
                else if(f == 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF) - 2, ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF) + 2;
                        addMissile(grid, lx, ly, lz, 1, 0, 1, 16 * 5);
                        ShapeGenerator.ball(grid, lx - 2 + r.nextInt(6), ly - 2 + r.nextInt(6), lz + 6, 1.45, smoke, choose1of4);
                        ShapeGenerator.ball(grid, lx - 3 + r.next(3), ly - 3 + r.next(3), lz + 5, 1.45, smoke, choose1of4);
                        ShapeGenerator.ball(grid, lx - 5 + r.nextInt(12), ly - 5 + r.nextInt(12), lz + 3, 1.45, smoke, choose1of4);
                        ShapeGenerator.ball(grid, lx - 6 + r.nextInt(14), ly - 6 + r.nextInt(14), lz + 2, 1.45, smoke, choose1of4);
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] flameWaveAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose10of11 = ((x, y, z) -> r.nextInt(11) != 0);
        Choice choose11of12 = ((x, y, z) -> r.nextInt(12) != 0);
        Choice choose9of10 = ((x, y, z) -> r.nextInt(10) != 0);
        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            byte[][][] previousBonus = null;
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);
                if(f == 2) previousBonus = new byte[grid.length][grid.length][grid.length];

                if(f == 0) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 12, ly, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz + 8, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly, lz - 2, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 6, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 6, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz + 5, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz + 5, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 2, lz + 6, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 2, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 2, lz + 6, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 2, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz + 4, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly + 4, lz, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz + 4, yellowFire - r.next(1), choose11of12);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 8, ly - 4, lz, yellowFire - r.next(1), choose11of12);
                    }
                }
                else if(f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 28, ly, lz + 2 , yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly, lz + 10, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly, lz     , yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 6, lz + 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 6, lz + 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 4, lz + 8, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 4, lz + 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 4, lz + 8, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 4, lz - 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 2, lz + 8, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 2, lz - 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 2, lz + 8, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 2, lz - 2, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 4, lz + 6, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly + 4, lz    , yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 4, lz + 6, yellowFire - r.next(1), choose10of11);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 24, ly - 4, lz    , yellowFire - r.next(1), choose10of11);
                    }
                }
                else if(f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 40, ly, lz + 2 , yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly, lz + 10, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly, lz     , yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 6, lz + 5, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 6, lz + 5, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 4, lz + 10, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 4, lz + 4, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 4, lz + 10, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 4, lz + 4, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 2, lz + 10, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 2, lz + 4, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 2, lz + 10, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 2, lz + 4, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 4, lz + 8, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly + 4, lz + 2, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 4, lz + 8, yellowFire - r.next(1), choose9of10);
                        ShapeGenerator.line(previousBonus, lx, ly, lz, lx + 36, ly - 4, lz + 2, yellowFire - r.next(1), choose9of10);
                    }
                }
                if(f >= 2 && f <= 6 && previousBonus != null){
                    int xs, ys, zs;
                    xs = grid.length;
                    ys = grid[0].length;
                    zs = grid[0][0].length;
                    for (int x = 0; x < xs; x++) {
                        for (int y = 0; y < ys; y++) {
                            for (int z = 0; z < zs; z++) {
                                if (grid[x][y][z] == 0)
                                    grid[x][y][z] = previousBonus[x][y][z];
                            }
                        }
                    }
                    for (int x = 0; x < xs; x++) {
                        for (int y = 0; y < ys; y++) {
                            for (int z = 0; z < zs; z++) {
                                if(previousBonus[x][y][z] != 0 && r.nextInt(10 - f) > 0)
                                {
                                    int xx = x + 12 + r.next(2), zz = z + r.nextInt(3) - 1;
                                    if(xx < xs && zz >= 0 && zz < zs) {
                                        previousBonus[xx][y][zz] = previousBonus[x][y][z];
                                        previousBonus[x][y][z] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz + 2, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz + r.next(1), smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz + 2, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz + r.next(1), smoke, choose1of2);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz + 2, smoke, choose11of12);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + r.nextInt(2, 4), smoke, choose11of12);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + r.nextInt(4, 6), smoke, choose11of12);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + r.nextInt(2, 4), smoke, choose11of12);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + r.nextInt(4, 6), smoke, choose11of12);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 3, ly + 5, lz + r.nextInt(5, 8), 1.45, smoke, choose9of10);
                        ShapeGenerator.ball(grid, lx + 3, ly - 5, lz + r.nextInt(5, 8), 1.45, smoke, choose9of10);
                        ShapeGenerator.ball(grid, lx + 5, ly + 5, lz + r.nextInt(5, 8), 1.45, smoke, choose9of10);
                        ShapeGenerator.ball(grid, lx + 5, ly - 5, lz + r.nextInt(5, 8), 1.45, smoke, choose9of10);
                    }
                }
                else if(f == 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + r.nextInt(5, 9), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + r.nextInt(5, 9), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 7), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 7), lz + r.nextInt(8, 14), 1.45, smoke, choose1of256);
                    }
                }
                else if(f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + r.nextInt(6, 10), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + r.nextInt(6, 10), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 9), lz + r.nextInt(9, 16), 1.45, smoke, choose1of512);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 9), lz + r.nextInt(9, 16), 1.45, smoke, choose1of512);
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] torpedoAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = new LongList(ls.size() >>> 3);
            for (int i = 0; i < ls.size(); i++) {
                long launcher = ls.getAt(i);
                int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                // dependent on whether the model was doubled in resolution or more; this assumes doubled.
                if(((lx | ly | lz) & 1L) == 0) {
                    launchers.add(launcher);
                }
            }
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);

                if(f == 0) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 3, ly+3, lz, shadow);
                    }
                }
                else if(f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 15, ly + 3, lz, shadow);
                    }
                }
                else if(f == 2) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx, ly, lz, lx + 31, ly + 3, lz, shadow);
                    }
                }
                else if(f == 3) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 12, ly, lz, lx + 43, ly + 3, lz, shadow);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 12 + 12, ly, lz, lx + 43 + 12, ly + 3, lz, shadow);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.box(grid, lx + 12 + 12*2, ly, lz, lx + 43 + 12*2, ly + 3, lz, shadow);
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] hackAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;

        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);
                int gs = grid.length;

                if(f == 0 || f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx, ly, lz, 21 - f, shock, choose1of512);
                    }
                }
                if(f == 1) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx, ly, lz, 17, shock, choose1of512);
                        ShapeGenerator.ball(grid, lx, ly, lz, 24, shock, choose1of512);
                    }
                }
                if(f >= 2 && f <= 6) {
                    for (int x = 0; x < gs; x++) {
                        for (int y = 0; y < gs; y++) {
                            for (int z = 0; z < gs; z++) {
                                byte v = grid[x][y][z];
                                if(v != 0 && (v < shock || v > shock + 1)
                                        && (1 - (f & 1) != Stuff.STUFFS_B[v & 255].material.getTrait(VoxMaterial.MaterialTrait._frame))){
                                    int h = IntPointHash.hash256(x>>>1, y>>>1, 12345678);
                                    if(h < 180)
                                        grid[x][y][z] = 76;
                                    else
                                        grid[x][y][z] = (byte) (terminal + (h + (z>>>2) + f & 3));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] hackAnimationOld(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;

        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);
        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f+1].grids.get(g);
                int gs = grid.length;

                if(f == 0 || f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx, ly, lz, 15, shock, choose1of256);
                    }
                }
                else if(f <= 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx, ly, lz, 17, shock, choose1of512);
                        ShapeGenerator.ball(grid, lx, ly, lz, 24, shock, choose1of512);
                    }
                }
                if(f >= 3 && f <= 5) {
                    for (int x = 0; x < gs; x++) {
                        for (int y = 0; y < gs; y++) {
                            for (int z = 0; z < gs; z++) {
                                byte v = grid[x][y][z];
                                if(v != 0 && (v < shock || v > shock + 1)
                                        && (1 - (f & 1) != Stuff.STUFFS_B[v & 255].material.getTrait(VoxMaterial.MaterialTrait._frame))){
                                    int h = IntPointHash.hash256(x, y, z, 1234567);
                                    if(h < f * f * 11)
                                        grid[x][y][z] = (byte) (flicker + (h & 1));
                                }
                            }
                        }
                    }
                }
                else if(f >= 6){
                    for (int x = 0; x < gs; x++) {
                        for (int y = 0; y < gs; y++) {
                            for (int z = 0; z < gs; z++) {
                                byte v = grid[x][y][z];
                                if(v != 0 && (v < shock || v > shock + 1)
                                        && (1 - (f & 1) != Stuff.STUFFS_B[v & 255].material.getTrait(VoxMaterial.MaterialTrait._frame))){
                                    int h = IntPointHash.hash256(x, y, z, 12345), fr = 16 - f - f;
                                    if(h < fr * fr * 11)
                                        grid[x][y][z] = (byte) (flicker + (h & 1));

                                }
                            }
                        }
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] flyover(VoxModel[] frames) {
        int count = frames.length, halfCount = count / 2;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();

        for (int g = 0; g < gridLimit; g++) {
            for (int f = 0; f < halfCount; f++) {
                byte[][][] grid = next[f].grids.get(g);
                next[f + 1].grids.set(g, Tools3D.translateCopy(grid, 0, 0, 3));
                for (float[] fa : next[f + 1].links.get(g).values()) {
                    fa[2] += 3;
                }
            }
            for (int f = halfCount; f < count - 1; f++) {
                byte[][][] grid = next[f].grids.get(g);
                next[f + 1].grids.set(g, Tools3D.translateCopy(grid, 0, 0, -3));
                for (float[] fa : next[f + 1].links.get(g).values()) {
                    fa[2] -= 3;
                }
            }
        }
        return next;
    }

    public static VoxModel[] bombDropAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = flyover(frames);
        final int gridLimit = next[0].grids.size(), gridSize = next[0].grids.get(0).length;
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = new LongList(ls.size() >>> 3);
            for (int i = 0; i < ls.size(); i++) {
                long launcher = ls.getAt(i);
                int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                // dependent on whether the model was doubled in resolution or more; this assumes doubled.
                if (((lx | ly | lz) & 1L) == 0) {
                    launchers.add(launcher);
                }
            }
            int f = 0;
            ALL:
            for (; f < count - 1; f++) {
                byte[][][] grid = next[f + 1].grids.get(g);

                for (int ln = 0; ln < launchers.size(); ln++) {
                    long launcher = launchers.get(ln);
                    int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF);
                    lz -= 6 + r.next(1) + r.next(1) + r.next(1);
                    if (lz - 2 <= 1) {
                        break ALL;
                    }
                    ShapeGenerator.box(grid, lx - 2, ly-1, lz-1, lx + 5, ly + 1, lz + 1, missileHead);
                    launchers.set(ln, (long) lx | (long) ly << 20 | (long) lz << 40);
                }
            }
            byte[][][] fireStart = new byte[gridSize][gridSize][gridSize];
            for (int ln = 0; ln < launchers.size(); ln++) {
                long launcher = launchers.get(ln);
                int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) ((launcher >>> 40) & 0xFFFFF);
                ShapeGenerator.ball(fireStart, lx + 1, ly + 1, lz, 15.5, yellowFire, choose3of4);
            }
            byte[][][][] explosion = fireballAnimation(fireStart, count - 2 - f, 0, 0);
            for (int i = 0; f < count - 1 && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 1].grids.get(g);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, i + i);
            }
        }
        if(!foundAny) return null;

        return next;
    }

    public static VoxModel[] debugAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(launch + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f + 1].grids.get(g);
                final int lim = grid.length - 1;
                for (int ln = 0; ln < launchers.size(); ln++) {
                    long launcher = launchers.get(ln);
                    int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                    ShapeGenerator.line(grid, lx, ly, lz, lim, ly, lz, glow);
                }
                ShapeGenerator.line(grid, 0, 0, 0, lim, 0, 0, glow);
                ShapeGenerator.line(grid, 0, lim, 0, lim, lim, 0, glow);
                ShapeGenerator.line(grid, 0, 0, lim, lim, 0, lim, glow);
                ShapeGenerator.line(grid, 0, lim, lim, lim, lim, lim, glow);

            }
        }
        if(!foundAny) return null;

        return next;
    }



    // RECEIVE

    public static VoxModel[] handgunReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }

        for(int s = 0; s < strength + strength; s++) {
            burst(grids, 65 + r.nextInt(10), 50 + r.nextInt(20), 4 + r.next(2), (s & 1) << 2, 3, s + r.next(2) > strength);
        }
        return next;
    }

    public static VoxModel[] machineGunReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        strength = Math.min(2, strength);

        for (int f = 1; f < frames - 1; f++) {
            for (int s = 0; s < strength; s++) {
                burst(grids, 82, 17 + 28 * s + 5 * f, 0, f, 2, false);
            }
        }
        return next;
    }

    public static VoxModel[] forwardCannonReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < strength; s++) {
            ShapeGenerator.ball(fireStart, 90 - s - s, 52 + r.next(4), 7, 5 + s << 1, yellowFire, ((x, y, z) -> r.next(2) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 6 - s, 0, 3);
            for (int i = 0, f = 0; f < frames - 1 && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 1].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
            }
        }
        return next;
    }

    /**
     *
     * @param size
     * @param frames
     * @param strength the rough number of cannons firing, 1 to 4
     * @return
     */
    public static VoxModel[] arcCannonReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < strength; s++) {
            int xPos = 81 - s * 6 + r.nextInt(18), yPos = 47 + r.next(4);
            ShapeGenerator.box(fireStart, xPos, yPos, 0, xPos + 13, yPos + 10, 33, yellowFire, ((x, y, z) -> r.next(2) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 6 - s, 0, 1);
            for (int i = 0, f = s; f < frames - 1 && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 1].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
            }
        }
        return next;
    }

    public static VoxModel[] forwardMissileReceiveAnimation(int size, int frames, int distance){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < 3; s++) {
            ShapeGenerator.box(fireStart, 90 - r.nextInt(7) - s * 3, 75, 30, 95 + s, 104, 42, yellowFire, ((x, y, z) -> r.next(2) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 3 - s, 1, -2);
            for (int i = 0, f = 0; f < frames - 1 - distance && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 1 + distance].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
            }
        }
        return next;
    }

    public static VoxModel[] arcMissileReceiveAnimation(int size, int frames, int count) {
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int launcher = 0; launcher < count; launcher++) {
            int lx = 110 - (int)((launcher * 0x91E10DA5L + 0xE10DA591L & 0xFFFFFFFFL) * 30 >>> 32);
            int ly = (int)((launcher * 0xC13FA9A9L + 0xA9A9C13FL & 0xFFFFFFFFL) * 80 >>> 32) + 40;
            for (int f = 0; f < 3; f++) {
                byte[][][] grid = next[f].grids.get(0);
                addMissile(grid, lx, ly, 80, -1, 0, -1, 16 * f + 6);
            }
            for (int s = 0; s < 3; s++) {
                ShapeGenerator.box(fireStart, lx - 52 - 3 + s * 3, ly - 5, 30, lx - 44 + s, ly + 5, 42, yellowFire, ((x, y, z) -> r.next(2) != 0));
                byte[][][][] explosion = fireballAnimation(fireStart, 3 - s, 1, 0);
                for (int i = 0, f = 0; f < frames - 3 && i < explosion.length; f++, i++) {
                    byte[][][] grid = next[f + 3].grids.get(0);
                    Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
                }
            }
        }
        return next;
    }

    static final IntIntMap fireToWater = IntIntMap.withPrimitive(hotFire, 88, ember, 87, yellowFire, 88, sparks, 88, smoke, 0);

    public static VoxModel[] torpedoReceiveAnimation(int size, int frames, int distance){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < 3; s++) {
            ShapeGenerator.ball(fireStart, 115 - r.nextInt(7) - s * 3, 87, -1, 20, yellowFire, ((x, y, z) -> r.next(3) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 3 - s, 1, -2);
            for (int i = 0, f = 0; f < frames - 1 - distance && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 1 + distance].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0, fireToWater);
            }
        }
        return next;
    }

    /**
     *
     * @param size
     * @param frames
     * @param strength the scale of the explosion, 1 or higher
     * @return
     */
    public static VoxModel[] flameWaveReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < strength; s++) {
            int xPos = 100 - s * 6 - r.next(2), yPos = 48 + r.next(3);
            ShapeGenerator.box(fireStart, xPos, yPos, 20, xPos + 16, yPos + 16, 40, yellowFire, ((x, y, z) -> r.next(3) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 5 - s, 0, -4);
            for (int i = 0, f = s; f < frames - 2 && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 2].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
            }
        }
        return next;
    }

    /**
     *
     * @param size
     * @param frames
     * @param strength the scale of the explosion, 1 or higher
     * @return
     */
    public static VoxModel[] bombDropReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }
        byte[][][] fireStart = new byte[size][size][size];
        for (int s = 0; s < strength; s++) {
            int xPos = 100 - s * 6 - r.next(2), yPos = 40 + r.next(3);
            ShapeGenerator.box(fireStart, xPos, yPos, 0, xPos + 16, yPos + 32, 32, yellowFire, ((x, y, z) -> r.next(2) != 0));
            byte[][][][] explosion = fireballAnimation(fireStart, 4 - s, 0, -3);
            for (int i = 0, f = s; f < frames - 3 && i < explosion.length; f++, i++) {
                byte[][][] grid = next[f + 3].grids.get(0);
                Tools3D.translateCopyInto(explosion[i], grid, 0, 0, 0);
            }
        }
        return next;
    }

    public static VoxModel[] hackReceiveAnimation(int size, int frames, int strength) {

        Choice choose1of256 = ((x, y, z) -> r.next(8) == 0);
        Choice choose1of512 = ((x, y, z) -> r.next(9) == 0);

        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);
        }

        for(int s = 0; s < strength; s++) {
            int rx = r.nextInt(40, 80), ry = r.nextInt(40, 80), rz = r.nextInt(15, 60);
            for (int f = 6 - s * 3, i = 0; i < 3 && f >= 0; f--, i++) {
                if(i == 2) {
                    ShapeGenerator.ball(grids[f], rx, ry, rz, 20, shock, choose1of256);
                }
                else if(i == 1) {
                    ShapeGenerator.ball(grids[f], rx, ry, rz, 16, shock, choose1of512);
                    ShapeGenerator.ball(grids[f], rx, ry, rz, 25, shock, choose1of512);

                }
            }
        }
        return next;
    }

    public static VoxModel[] debugReceiveAnimation(int size, int frames, int strength){
        VoxModel[] next = new VoxModel[frames];
        byte[][][][] grids = new byte[frames][size][size][size];
        int E = size - 1, L = (size >> 1) - 3, H = (size >> 1) + 2;
        for (int i = 0; i < frames; i++) {
            next[i] = new VoxModel();
            next[i].grids.add(grids[i]);
            next[i].links.add(new IntObjectMap<>(1));
            next[i].materials.putAll(lastMaterials);

            ShapeGenerator.line(grids[i], 0, 0, 0, 0, 0, E, glow);
            ShapeGenerator.line(grids[i], 0, E, 0, 0, E, E, glow);
            ShapeGenerator.line(grids[i], E, 0, 0, E, 0, E, glow);
            ShapeGenerator.line(grids[i], E, E, 0, E, E, E, glow);

            ShapeGenerator.line(grids[i], 0, L, 0, 0, L, E, red);
            ShapeGenerator.line(grids[i], 0, H, 0, 0, H, E, red);
            ShapeGenerator.line(grids[i], E, L, 0, E, L, E, red);
            ShapeGenerator.line(grids[i], E, H, 0, E, H, E, red);

            ShapeGenerator.line(grids[i], 0, 0, L, 0, E, L, red);
            ShapeGenerator.line(grids[i], 0, 0, H, 0, E, H, red);
            ShapeGenerator.line(grids[i], E, 0, L, E, E, L, red);
            ShapeGenerator.line(grids[i], E, 0, H, E, E, H, red);
        }

        return next;
    }
}
