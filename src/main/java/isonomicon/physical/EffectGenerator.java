package isonomicon.physical;

import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.ds.LongList;
import com.github.tommyettinger.ds.LongOrderedSet;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import com.github.tommyettinger.ds.support.EnhancedRandom;
import com.github.tommyettinger.ds.support.FourWheelRandom;
import isonomicon.io.extended.VoxModel;

import static com.badlogic.gdx.math.MathUtils.*;

public class EffectGenerator {

    @FunctionalInterface
    public interface Effect {
        VoxModel[] runEffect(VoxModel[] frames, int which);
    }

    public static final ObjectObjectOrderedMap<String, Effect> KNOWN_EFFECTS = new ObjectObjectOrderedMap<>(
            new String[]{"Handgun", "Machine_Gun", "Forward_Cannon"},
            new Effect[]{EffectGenerator::handgunAnimation, EffectGenerator::machineGunAnimation, EffectGenerator::forwardCannonAnimation}
    );

    public static final EnhancedRandom r = new FourWheelRandom(123456789L);

    public static final int smoke = 67;
    public static final int hotFire = 114;
    public static final int yellowFire = 115;
    public static final int sparks = 127;

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
            final float magnitude = SPREAD * 0.04f / (fr * fr);
            final float LIMIT = xSize * 0.1f;

            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        byte color = vls[x][y][z];
                        if ((color - 1 & 255) < 191) {
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
            case 6:
            case 7:
                return 114;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return 115;
            default:
                return 67;
        }
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
            LongOrderedSet ls = next[0].markers.get(g).get(201 + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            Choice majorLimit = ((x, y, z) -> r.nextInt(10) > 2);
            Choice minorLimit = ((x, y, z) -> r.nextInt(10) > 1);
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f].grids.get(g);
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
    //// from PixVoxelAssets
    /*
        public static MagicaVoxelData[][] MachineGunAnimationLarge(MagicaVoxelData[][] parsedFrames, int unit, int which)
        {
            MagicaVoxelData[][] voxelFrames = new MagicaVoxelData[parsedFrames.Length][];
            voxelFrames[0] = new MagicaVoxelData[parsedFrames[0].Length];
            voxelFrames[parsedFrames.Length - 1] = new MagicaVoxelData[parsedFrames[parsedFrames.Length - 1].Length];
            parsedFrames[0].CopyTo(voxelFrames[0], 0);
            parsedFrames[parsedFrames.Length - 1].CopyTo(voxelFrames[parsedFrames.Length - 1], 0);
            List<MagicaVoxelData> launchers = new List<MagicaVoxelData>(40);
            List<MagicaVoxelData>[] extra = new List<MagicaVoxelData>[voxelFrames.Length - 2];

            List<int> known = new List<int>(30);
            foreach(MagicaVoxelData mvd in voxelFrames[0])
            {
                if(mvd.color == emitter0 - which * 8)
                {
                    launchers.Add(mvd);
                }
            }

            for(int f = 0; f < voxelFrames.Length - 2; f++)
            {

                int currentlyFiring = f % (launchers.Count / 4 + 1);
                extra[f] = new List<MagicaVoxelData>(1024);
                if(currentlyFiring % 2 == 0)
                {

                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        if(currentlyFiring != 0)
                        {
                            currentlyFiring = (currentlyFiring + 1) % (launchers.Count / 4 + 1);
                            continue;
                        }
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 7), y = launcher.y, z = launcher.z, color = (byte)(yellow_fire) }, yellow_fire));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = launcher.y, z = (byte)(launcher.z + 2), color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = launcher.y, z = (byte)(launcher.z - 2), color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 2), z = launcher.z, color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 2), z = launcher.z, color = yellow_fire }, orange_fire));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y + 2), z = (byte)(launcher.z + 2), color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y - 2), z = (byte)(launcher.z + 2), color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y + 2), z = (byte)(launcher.z - 2), color = yellow_fire }, orange_fire));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y - 2), z = (byte)(launcher.z - 2), color = yellow_fire }, orange_fire));

                    }
                    extra[f] = extra[f].Where(v => r.Next(10) > 1).ToList();

                }
                else
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        if(currentlyFiring < 2 && launchers.Count > 8)
                        {
                            currentlyFiring = (currentlyFiring + 1) % (launchers.Count / 4 + 1);
                            continue;
                        }
                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = launcher.z, color = orange_fire }
                            , new MagicaVoxelData { x = (byte)(launcher.x + 9), y = launcher.y, z = launcher.z, color = orange_fire }, orange_fire));

                        currentlyFiring = (currentlyFiring + 1) % (launchers.Count / 4 + 1);
                    }
                    extra[f] = extra[f].Where(v => r.Next(10) > 2).ToList();

                }

            }
            for(int f = 1; f < voxelFrames.Length - 1; f++)
            {
                List<MagicaVoxelData> working = new List<MagicaVoxelData>(parsedFrames[f]);
                working.AddRange(extra[f - 1]);
                voxelFrames[f] = working.ToArray();
            }
            return voxelFrames;
        }
     */
    public static VoxModel[] handgunAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(201 + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f].grids.get(g);
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

    //// from PixVoxelAssets
    /*
            public static MagicaVoxelData[][] HandgunAnimationLarge(MagicaVoxelData[][] parsedFrames, int unit, int which)
        {
            MagicaVoxelData[][] voxelFrames = new MagicaVoxelData[parsedFrames.Length][];
            voxelFrames[0] = new MagicaVoxelData[parsedFrames[0].Length];
            voxelFrames[parsedFrames.Length - 1] = new MagicaVoxelData[parsedFrames[parsedFrames.Length - 1].Length];
            parsedFrames[0].CopyTo(voxelFrames[0], 0);
            parsedFrames[parsedFrames.Length - 1].CopyTo(voxelFrames[parsedFrames.Length - 1], 0);
            List<MagicaVoxelData> launchers = new List<MagicaVoxelData>(4);
            List<MagicaVoxelData>[] extra = new List<MagicaVoxelData>[voxelFrames.Length - 2];
            foreach(MagicaVoxelData mvd in voxelFrames[0])
            {
                if(mvd.color == emitter0 - which * 8)
                {
                    launchers.Add(mvd);
                }
            }
            for(int f = 0; f < voxelFrames.Length - 2; f++) //going only through the middle
            {
                int currentlyFiring = f & 3;
                extra[f] = new List<MagicaVoxelData>(20);

                if(currentlyFiring < launchers.Count)
                {
                    extra[f].AddRange(VoxelLogic.generateBox(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring].x), y = (byte)(launchers[currentlyFiring].y), z = (byte)(launchers[currentlyFiring].z), color = orange_fire }, 8, 2, 2, yellow_fire));
                    extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring].x + 2), y = (byte)(launchers[currentlyFiring].y + 2), z = (byte)(launchers[currentlyFiring].z), color = yellow_fire }, orange_fire));
                    extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring].x + 2), y = (byte)(launchers[currentlyFiring].y - 2), z = (byte)(launchers[currentlyFiring].z), color = yellow_fire }, orange_fire));
                    extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring].x + 2), y = (byte)(launchers[currentlyFiring].y), z = (byte)(launchers[currentlyFiring].z + 2), color = yellow_fire }, orange_fire));
                    extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring].x + 2), y = (byte)(launchers[currentlyFiring].y), z = (byte)(launchers[currentlyFiring].z - 2), color = yellow_fire }, orange_fire));
                }
                if(currentlyFiring <= launchers.Count && currentlyFiring > 0)
                {
                    extra[f].AddRange(VoxelLogic.generateBox(new MagicaVoxelData { x = (byte)(launchers[currentlyFiring - 1].x + 4), y = (byte)(launchers[currentlyFiring - 1].y), z = (byte)(launchers[currentlyFiring - 1].z), color = orange_fire }, 6, 2, 2, yellow_fire));
                }
            }
            for(int f = 1; f < voxelFrames.Length - 1; f++)
            {
                List<MagicaVoxelData> working = new List<MagicaVoxelData>(parsedFrames[f]);
                working.AddRange(extra[f - 1]);
                voxelFrames[f] = working.ToArray();
            }
            return voxelFrames;
        }

     */

    public static VoxModel[] forwardCannonAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        final int gridLimit = next[0].grids.size();
        boolean foundAny = false;
        Choice choose3of4 = ((x, y, z) -> r.next(2) != 0);
        Choice choose1of3 = ((x, y, z) -> r.nextInt(3) == 0);
        Choice choose1of6 = ((x, y, z) -> r.nextInt(6) == 0);
        Choice choose1of8 = ((x, y, z) -> r.next(3) == 0);
        Choice choose1of16 = ((x, y, z) -> r.next(4) == 0);
        Choice choose1of2 = ((x, y, z) -> r.nextLong() < 0L);

        for (int g = 0; g < gridLimit; g++) {
            LongOrderedSet ls = next[0].markers.get(g).get(201 + which * 8);
            if (ls == null)
                continue;
            foundAny = true;
            LongList launchers = ls.order();
            for (int f = 0; f < count - 2; f++) {
                byte[][][] grid = next[f].grids.get(g);

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
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz + 2, smoke, choose1of2);
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz, smoke, choose1of2);
                    }
                }
                else if(f == 4) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly, lz + 2, smoke, choose1of3);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + 2, smoke, choose1of3);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly + 2, lz + 4, smoke, choose1of3);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + 2, smoke, choose1of3);
                        ShapeGenerator.line(grid, lx + 2, ly, lz + 1, lx + 5, ly - 2, lz + 4, smoke, choose1of3);
                    }
                }
                else if(f == 5) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 3, ly + 5, lz + 5, 1.45, smoke, choose1of6);
                        ShapeGenerator.ball(grid, lx + 3, ly - 5, lz + 5, 1.45, smoke, choose1of6);
                        ShapeGenerator.ball(grid, lx + 5, ly + 5, lz + 5, 1.45, smoke, choose1of6);
                        ShapeGenerator.ball(grid, lx + 5, ly - 5, lz + 5, 1.45, smoke, choose1of6);
                    }
                }
                else if(f == 6) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + 5, 1.45, smoke, choose1of8);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + 5, 1.45, smoke, choose1of8);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 7), lz + 9, 1.45, smoke, choose1of8);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 7), lz + 9, 1.45, smoke, choose1of8);
                    }
                }
                else if(f == 7) {
                    for (int ln = 0; ln < launchers.size(); ln++) {
                        long launcher = launchers.get(ln);
                        int lx = ((int) (launcher) & 0xFFFFF), ly = ((int) (launcher >>> 20) & 0xFFFFF), lz = (int) (launcher >>> 40) & 0xFFFFF;
                        ShapeGenerator.ball(grid, lx + 5, ly + 7, lz + 6, 1.45, smoke, choose1of16);
                        ShapeGenerator.ball(grid, lx + 5, ly - 7, lz + 6, 1.45, smoke, choose1of16);
                        ShapeGenerator.ball(grid, lx + 5, ly + r.nextInt(3, 9), lz + 10, 1.45, smoke, choose1of16);
                        ShapeGenerator.ball(grid, lx + 5, ly - r.nextInt(3, 9), lz + 10, 1.45, smoke, choose1of16);
                    }
                }
                if(f == 2){
                    next[f].grids.set(g, Tools3D.translateCopy(grid, -3, 0, 0));
                    for(float[] fa : next[f].links.get(g).values()){
                        fa[0] -= 3f;
                    }
                }
                else if(f == 3){
                    next[f].grids.set(g, Tools3D.translateCopy(grid, -2, 0, 0));
                    for(float[] fa : next[f].links.get(g).values()){
                        fa[0] -= 2f;
                    }
                }
                else if(f == 4){
                    next[f].grids.set(g, Tools3D.translateCopy(grid, -1, 0, 0));
                    for(float[] fa : next[f].links.get(g).values()){
                        fa[0] -= 1f;
                    }
                }
            }
        }
        if(!foundAny) return null;

        return next;
    }


    /*
            public static MagicaVoxelData[][] CannonAnimationLarge(MagicaVoxelData[][] parsedFrames, int unit, int which)
        {
            MagicaVoxelData[][] voxelFrames = new MagicaVoxelData[parsedFrames.Length][];
            voxelFrames[0] = new MagicaVoxelData[parsedFrames[0].Length];
            voxelFrames[parsedFrames.Length - 1] = new MagicaVoxelData[parsedFrames[parsedFrames.Length - 1].Length];
            parsedFrames[0].CopyTo(voxelFrames[0], 0);
            parsedFrames[parsedFrames.Length - 1].CopyTo(voxelFrames[parsedFrames.Length - 1], 0);
            List<MagicaVoxelData> launchers = new List<MagicaVoxelData>(40);
            List<MagicaVoxelData>[] extra = new List<MagicaVoxelData>[voxelFrames.Length - 2];

            List<int> known = new List<int>(30);
            foreach(MagicaVoxelData mvd in voxelFrames[0])
            {
                if(mvd.color == emitter0 - which * 8)
                {
                    launchers.Add(mvd);
                }
            }
            List<MagicaVoxelData>[] halves = { launchers.ToList(), launchers.ToList() };

            for(int f = 0; f < voxelFrames.Length - 2; f++) //going only through the middle
            {
                extra[f] = new List<MagicaVoxelData>(1024);

                if(f == 0 || f == 1)
                {
                    foreach(MagicaVoxelData launcher in halves[0])
                    {
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 12), y = launcher.y, z = launcher.z, color = (byte)(yellow_fire) }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = launcher.y, z = (byte)(launcher.z + 8), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = launcher.y, z = (byte)(launcher.z - 8), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 8), z = launcher.z, color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 8), z = launcher.z, color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 5), z = (byte)(launcher.z + 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 5), z = (byte)(launcher.z - 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 5), z = (byte)(launcher.z + 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 5), z = (byte)(launcher.z - 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 3), z = (byte)(launcher.z + 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 3), z = (byte)(launcher.z - 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 3), z = (byte)(launcher.z + 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 3), z = (byte)(launcher.z - 5), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 5), z = (byte)(launcher.z + 3), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y + 5), z = (byte)(launcher.z - 3), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 5), z = (byte)(launcher.z + 3), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = (byte)(launcher.y - 5), z = (byte)(launcher.z - 3), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f] = extra[f].Where(v => r.Next(10) > 0).ToList();

                    }
                }
                else if(f == 1 || f == 2)
                {

                    foreach(MagicaVoxelData launcher in halves[1])
                    {

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 10), y = launcher.y, z = launcher.z, color = (byte)(yellow_fire) }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = launcher.y, z = (byte)(launcher.z + 6), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = launcher.y, z = (byte)(launcher.z - 6), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 6), z = launcher.z, color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 6), z = launcher.z, color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 4), z = (byte)(launcher.z + 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 4), z = (byte)(launcher.z - 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 4), z = (byte)(launcher.z + 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 4), z = (byte)(launcher.z - 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 2), z = (byte)(launcher.z + 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 2), z = (byte)(launcher.z - 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 2), z = (byte)(launcher.z + 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 2), z = (byte)(launcher.z - 4), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 4), z = (byte)(launcher.z + 2), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y + 4), z = (byte)(launcher.z - 2), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 4), z = (byte)(launcher.z + 2), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 8), y = (byte)(launcher.y - 4), z = (byte)(launcher.z - 2), color = yellow_fire }, (yellow_fire - (r.Next(2) * 4))));

                        extra[f] = extra[f].Where(v => r.Next(10) > 0).ToList();

                    }
                }
                else if(f == 3)
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = launcher.y, z = launcher.z, color = (byte)(yellow_fire) }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 2), z = (byte)(launcher.z + 2), color = yellow_fire }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 2), z = launcher.z, color = yellow_fire }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 2), z = (byte)(launcher.z + 2), color = yellow_fire }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(launcher, new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 2), z = launcher.z, color = yellow_fire }, smoke));
                    }
                    extra[f] = extra[f].Where(v => r.Next(2) != 0).ToList();

                }
                else if(f == 4)
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = (byte)(launcher.z + 2), color = (byte)(smoke) },
                            new MagicaVoxelData { x = (byte)(launcher.x + 5), y = launcher.y, z = launcher.z, color = (byte)(yellow_fire) }, smoke));

                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = (byte)(launcher.z + 2), color = (byte)(smoke) },
                            new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 2), z = (byte)(launcher.z + 4), color = (byte)(yellow_fire) }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = (byte)(launcher.z + 2), color = (byte)(smoke) },
                            new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 2), z = (byte)(launcher.z + 2), color = (byte)(yellow_fire) }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = (byte)(launcher.z + 2), color = (byte)(smoke) },
                            new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 2), z = (byte)(launcher.z + 4), color = (byte)(yellow_fire) }, smoke));
                        extra[f].AddRange(VoxelLogic.generateStraightLine(new MagicaVoxelData { x = (byte)(launcher.x + 2), y = launcher.y, z = (byte)(launcher.z + 2), color = (byte)(smoke) },
                            new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 2), z = (byte)(launcher.z + 2), color = (byte)(yellow_fire) }, smoke));

                    }
                    extra[f] = extra[f].Where(v => r.Next(2) != 0 && r.Next(3) != 0).ToList();

                }
                else if(f == 5)
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y + 5), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 3), y = (byte)(launcher.y - 5), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 5), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 5), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                    }
                    extra[f] = extra[f].Where(v => r.Next(3) == 0 && r.Next(1) == 0).ToList();

                }
                else if(f == 6)
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 7), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 7), z = (byte)(launcher.z + 5), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 3 + r.Next(6)), z = (byte)(launcher.z + 9), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 3 - r.Next(6)), z = (byte)(launcher.z + 9), color = smoke }, smoke));
                    }
                    extra[f] = extra[f].Where(v => r.Next(6) == 0 && r.Next(3) == 0).ToList();
                }
                else if(f == 7)
                {
                    foreach(MagicaVoxelData launcher in launchers)
                    {

                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 7), z = (byte)(launcher.z + 6), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 7), z = (byte)(launcher.z + 6), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y + 3 + r.Next(6)), z = (byte)(launcher.z + 10), color = smoke }, smoke));
                        extra[f].AddRange(VoxelLogic.generateFatVoxel(new MagicaVoxelData { x = (byte)(launcher.x + 5), y = (byte)(launcher.y - 3 - r.Next(6)), z = (byte)(launcher.z + 10), color = smoke }, smoke));
                    }
                    extra[f] = extra[f].Where(v => r.Next(7) > 5 && r.Next(6) == 0).ToList();

                }
            }
            for(int f = 1; f < voxelFrames.Length - 1; f++)
            {
                List<MagicaVoxelData> working = new List<MagicaVoxelData>(parsedFrames[f]);
                if(f == 2 || f == 4)
                {
                    for(int i = 0; i < working.Count; i++)
                    {
                        working[i] = new MagicaVoxelData { x = (byte)(working[i].x - 1), y = working[i].y, z = working[i].z, color = working[i].color };
                    }
                }
                else if(f == 3)
                {
                    for(int i = 0; i < working.Count; i++)
                    {
                        working[i] = new MagicaVoxelData { x = (byte)(working[i].x - 2), y = working[i].y, z = working[i].z, color = working[i].color };
                    }
                }
                working.AddRange(extra[f - 1]);
                voxelFrames[f] = working.ToArray();
            }
            return voxelFrames;
        }

     */
}
