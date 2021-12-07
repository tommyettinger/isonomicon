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

    public static final ObjectObjectOrderedMap<String, Effect> KNOWN_EFFECTS = ObjectObjectOrderedMap.with(
            "Machine_Gun", EffectGenerator::machineGunAnimation
    );

    public static final EnhancedRandom r = new FourWheelRandom(123456789L);
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
    //// from PixVoxelAssets
    /*
            public static MagicaVoxelData[][] FireballSwitchable(MagicaVoxelData[] voxels, int blowback, int maxFrames, int trimLevel, int xSize, int ySize, int zSize)
        {
            MagicaVoxelData[][] voxelFrames = new MagicaVoxelData[maxFrames + 1][];
            voxelFrames[0] = new MagicaVoxelData[voxels.Length];
            voxels.CopyTo(voxelFrames[0], 0);

            for(int f = 1; f <= maxFrames; f++)
            {
                List<MagicaVoxelData> altered = new List<MagicaVoxelData>(voxelFrames[f - 1].Length), working = new List<MagicaVoxelData>(voxelFrames[f - 1].Length * 2);
                MagicaVoxelData[] vls = new MagicaVoxelData[voxelFrames[f - 1].Length]; //.OrderBy(v => v.x * 32 - v.y + v.z * 32 * 128)
                voxelFrames[f - 1].CopyTo(vls, 0);
                if(vls.Count() == 0)
                {
                    voxelFrames[f] = new MagicaVoxelData[0];
                    continue;
                }


                int xLimitLow = vls.Min(v => v.x * (v.color <= emitter0 ? 1000 : 1)),
                    xLimitHigh = vls.Max(v => v.x * (v.color <= emitter0 ? 0 : 1)),
                    xMiddle = (xLimitHigh + xLimitLow) / 2,
                    xRange = xLimitHigh - xLimitLow,
                    yLimitLow = vls.Min(v => v.y * (v.color <= emitter0 ? 1000 : 1)),
                    yLimitHigh = vls.Max(v => v.y * (v.color <= emitter0 ? 0 : 1)),
                    yMiddle = (yLimitHigh + yLimitLow) / 2,
                    yRange = yLimitHigh - yLimitLow,
                    zLimitLow = vls.Min(v => v.z * (v.color <= emitter0 ? 1000 : 1)),
                    zLimitHigh = vls.Max(v => v.z * (v.color <= emitter0 ? 0 : 1)),
                    zMiddle = (zLimitHigh + zLimitLow) / 2,
                    zRange = zLimitHigh - zLimitLow;

                int[] minX = new int[zSize];
                int[] maxX = new int[zSize];
                float[] midX = new float[zSize];
                for(int level = 0; level < zSize; level++)
                {
                    minX[level] = vls.Min(v => v.x * ((v.z != level || v.color < VoxelLogic.clear) ? 1000 : 1));
                    maxX[level] = vls.Max(v => v.x * ((v.z != level || v.color < VoxelLogic.clear) ? 0 : 1));
                    midX[level] = (maxX[level] + minX[level]) / 2F;
                }

                int[] minY = new int[zSize];
                int[] maxY = new int[zSize];
                float[] midY = new float[zSize];
                for(int level = 0; level < zSize; level++)
                {
                    minY[level] = vls.Min(v => v.y * ((v.z != level || v.color < VoxelLogic.clear) ? 1000 : 1));
                    maxY[level] = vls.Max(v => v.y * ((v.z != level || v.color < VoxelLogic.clear) ? 0 : 1));
                    midY[level] = (maxY[level] + minY[level]) / 2F;
                }

                int minZ = vls.Min(v => v.z * ((v.color < VoxelLogic.clear) ? 1000 : 1));
                int maxZ = vls.Max(v => v.z * ((v.color < VoxelLogic.clear) ? 0 : 1));
                float midZ = (maxZ + minZ) / 2F;

                foreach(MagicaVoxelData v in vls)
                {
                    MagicaVoxelData mvd = new MagicaVoxelData();
                    int c = ((255 - v.color) % 4 == 0) ? (255 - v.color) / 4 + VoxelLogic.wcolorcount : (253 - v.color) / 4;
                    if(c == 8 || c == 9) //flesh
                        mvd.color = (byte)((r.Next(f) == 0) ? 253 - 34 * 4 : (r.Next(6) == 0 && f < 10) ? 253 - 19 * 4 : v.color); //random transform to guts
                    else if(c == 34) //guts
                        mvd.color = (byte)((r.Next(20) == 0 && f < 10) ? 253 - 19 * 4 : v.color); //random transform to orange fire
                    else if(c == VoxelLogic.wcolorcount - 1) //clear and markers
                        mvd.color = (byte)VoxelLogic.clear; //clear stays clear
                    else if(c == 16)
                        mvd.color = VoxelLogic.clear; //clear inner shadow
                    else if(c == 25)
                        mvd.color = 253 - 25 * 4; //shadow stays shadow
                    else if(c == 27)
                        mvd.color = 253 - 27 * 4; //water stays water
                    else if(c >= VoxelLogic.wcolorcount && c < VoxelLogic.wcolorcount + 5)
                        mvd.color = (byte)(255 - (c - VoxelLogic.wcolorcount) * 4); // falling water stays falling water
                    else if(c == 40)
                        mvd.color = 253 - 20 * 4; //flickering sparks become normal sparks
                    else if(c >= 21 && c <= 24) //lights
                        mvd.color = 253 - 35 * 4; //glass color for broken lights
                    else if(c == 35) //windows
                        mvd.color = (byte)((r.Next(3) == 0) ? VoxelLogic.clear : v.color); //random transform to clear
                    else if(c == 36) //rotor contrast
                        mvd.color = 253 - 0 * 4; //"foot contrast" color for broken rotors contrast
                    else if(c == 37) //rotor
                        mvd.color = 253 - 1 * 4; //"foot" color for broken rotors
                    else if(c == 38 || c == 39)
                        mvd.color = VoxelLogic.clear; //clear non-active rotors
                    else if(c == 19) //orange fire
                        mvd.color = (byte)((r.Next(9) + 2 <= f) ? 253 - 17 * 4 : ((r.Next(3) <= 1) ? 253 - 18 * 4 : ((r.Next(3) == 0) ? 253 - 17 * 4 : v.color))); //random transform to yellow fire or smoke
                    else if(c == 18) //yellow fire
                        mvd.color = (byte)((r.Next(9) + 1 <= f) ? 253 - 17 * 4 : ((r.Next(3) <= 1) ? 253 - 19 * 4 : ((r.Next(4) == 0) ? 253 - 17 * 4 : ((r.Next(4) == 0) ? 253 - 20 * 4 : v.color)))); //random transform to orange fire, smoke, or sparks
                    else if(c == 20) //sparks
                        mvd.color = (byte)((r.Next(4) > 0 && r.Next(12) > f) ? v.color : VoxelLogic.clear); //random transform to clear
                    else if(c == 17) //smoke
                        mvd.color = (byte)((r.Next(10) + 3 <= f) ? VoxelLogic.clear : 253 - 17 * 4); //random transform to clear
                    else
                        mvd.color = (byte)((r.Next(f * 4) <= 6) ? 253 - ((r.Next(4) == 0) ? 18 * 4 : 19 * 4) : v.color); //random transform to orange or yellow fire

                    float xMove = 0, yMove = 0, zMove = 0;
                    if(mvd.color == orange_fire || mvd.color == yellow_fire || mvd.color == smoke)
                    {
                        zMove = f * 1.1f;
                        xMove = (float)(r.NextDouble() * 2.0 - 1.0);
                        yMove = (float)(r.NextDouble() * 2.0 - 1.0);
                    }
                    else
                    {
                        if(v.x > midX[v.z])
                            xMove = ((blowback * 0.3f - r.Next(3) + (v.x - midX[v.z])) / (f + 8) * 25F * ((v.z - minZ + 1) / (maxZ - minZ + 1F)));
                        else if(v.x < midX[v.z])
                            xMove = ((blowback * 0.3f + r.Next(3) - midX[v.z] + v.x) / (f + 8) * 25F * ((v.z - minZ + 1) / (maxZ - minZ + 1F)));
                        if(v.y > midY[v.z])
                            yMove = ((0 - r.Next(3) + (v.y - midY[v.z])) / (f + 8) * 25F * ((v.z - minZ + 1) / (maxZ - minZ + 1F)));
                        else if(v.y < midY[v.z])
                            yMove = ((0 + r.Next(3) - midY[v.z] + v.y) / (f + 8) * 25F * ((v.z - minZ + 1) / (maxZ - minZ + 1F)));

                        if(mvd.color == 253 - 20 * 4)
                        {
                            zMove = 0.1f;
                            xMove *= 2;
                            yMove *= 2;
                        }
                        else if(mvd.color == orange_fire || mvd.color == yellow_fire || mvd.color == smoke)
                            zMove = f * 0.55F;
                        else if(f < (maxFrames - 4) && minZ <= 1)
                            zMove = (v.z / ((maxZ + 1) * (0.3F))) * ((maxFrames - 3) - f) * 0.8F;
                        else
                            zMove = (1 - f * 2.1F);
                    }
                    float magnitude = (float)Math.Sqrt(xMove * xMove + yMove * yMove);

                    if(xMove > 0)
                    {
                        //float nv = (v.x + (xMove / (0.2f * (f + 4)))) - Math.Abs((yMove / (0.5f * (f + 3))));
                        float nv = v.x + (float)r.NextDouble() * ((xMove / magnitude) * 35F / f) + (float)(r.NextDouble() * 8.0 - 4.0);
                        if(nv < 1) nv = 1;
                        if(nv > xSize - 2) nv = xSize - 2;
                        mvd.x = (byte)((blowback <= 0) ? Math.Floor(nv) : (Math.Ceiling(nv)));
                    }
                    else if(xMove < 0)
                    {
                        //float nv = (v.x + (xMove / (0.2f * (f + 4)))) + Math.Abs((yMove / (0.5f * (f + 3))));
                        float nv = v.x - (float)r.NextDouble() * ((xMove / magnitude) * -35F / f) + (float)(r.NextDouble() * 8.0 - 4.0);

                        if(nv < 1) nv = 1;
                        if(nv > xSize - 2) nv = xSize - 2;
                        mvd.x = (byte)((blowback > 0) ? Math.Floor(nv) : (Math.Ceiling(nv)));
                    }
                    else
                    {
                        if(v.x < 1) mvd.x = 1;
                        if(v.x > xSize - 2) mvd.x = (byte)(xSize - 2);
                        else mvd.x = v.x;
                    }
                    if(yMove > 0)
                    {
                        //float nv = (v.y + (yMove / (0.2f * (f + 4)))) - Math.Abs((xMove / (0.5f * (f + 3))));
                        float nv = v.y + (float)r.NextDouble() * ((yMove / magnitude) * 35F / f) + (float)(r.NextDouble() * 8.0 - 4.0);

                        if(nv < 1) nv = 1;
                        if(nv > ySize - 2) nv = ySize - 2;
                        mvd.y = (byte)(Math.Floor(nv));
                    }
                    else if(yMove < 0)
                    {
                        //float nv = (v.y + (yMove / (0.2f * (f + 4)))) + Math.Abs((xMove / (0.5f * (f + 3))));
                        float nv = v.y - (float)r.NextDouble() * ((yMove / magnitude) * -35F / f) + (float)(r.NextDouble() * 8.0 - 4.0);

                        if(nv < 1) nv = 1;
                        if(nv > ySize - 2) nv = ySize - 2;
                        mvd.y = (byte)(Math.Ceiling(nv));
                    }
                    else
                    {
                        mvd.y = v.y;
                    }
                    if(zMove != 0)
                    {
                        float nv = (v.z + (zMove / (0.35f + 0.14f * (f + 3))));

                        if(nv <= 0 && f < maxFrames && !(mvd.color == orange_fire || mvd.color == yellow_fire || mvd.color == smoke)) nv = r.Next(2); //bounce
                        else if(nv < 0) nv = 0;

                        if(nv > zSize - 1)
                        {
                            nv = zSize - 1;
                            mvd.color = VoxelLogic.clear;
                        }
                        mvd.z = (byte)Math.Round(nv);
                    }
                    else
                    {
                        mvd.z = v.z;
                    }
                    working.Add(mvd);
                    if(r.Next(maxFrames) > f + maxFrames / 6 && r.Next(maxFrames) > f + 2) working.AddRange(VoxelLogic.Adjacent(mvd, new int[] { orange_fire, yellow_fire, orange_fire, yellow_fire, smoke }));
                }
                working = working.Where(mvd => r.Next(9) < 9f - trimLevel && r.Next(12) < 13f
                - Math.Abs(mvd.x - xMiddle) * 1.5f / xRange
                - Math.Abs(mvd.y - yMiddle) * 1.5f / yRange
                - Math.Abs(mvd.z - zMiddle) * 1.5f / zRange).ToList();
                voxelFrames[f] = new MagicaVoxelData[working.Count];
                working.CopyTo(voxelFrames[f], 0);
            }
            MagicaVoxelData[][] frames = new MagicaVoxelData[maxFrames][];

            for(int f = 1; f <= maxFrames; f++)
            {
                frames[f - 1] = new MagicaVoxelData[voxelFrames[f].Length];
                voxelFrames[f].CopyTo(frames[f - 1], 0);
            }
            return frames;
        }
     */

    public static VoxModel[] machineGunAnimation(VoxModel[] frames, int which){
        int count = frames.length;
        VoxModel[] next = new VoxModel[count];
        for (int i = 0; i < count; i++) {
            next[i] = frames[i].copy();
        }
        LongOrderedSet ls = next[0].markers.get(0).get(201 + which * 8);
        if(ls == null)
            return null;
        LongList launchers = ls.order();
        Choice majorLimit = ((x, y, z) -> r.nextInt(10) > 2);
        Choice minorLimit = ((x, y, z) -> r.nextInt(10) > 1);
        for(int f = 0; f < count - 2; f++)
        {
            byte[][][] grid = next[f].grids.get(0);
            int currentlyFiring = f % ((launchers.size() >>> 2) + 1);
            if((currentlyFiring & 1) == 0)
            {
                for (int ln = 0; ln < launchers.size(); ln++) {
                    long launcher = launchers.get(ln);
                    if(currentlyFiring != 0)
                    {
                        currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                        continue;
                    }
                    int lx = ((int)(launcher) & 0xFFFFF), ly = ((int)(launcher >>> 20) & 0xFFFFF), lz = (int)(launcher >>> 40) & 0xFFFFF;
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 7, ly, lz, 127, minorLimit);

                    ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly    , lz + 2, 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly    , lz - 2, 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly + 2, lz    , 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 5, ly - 2, lz    , 127, minorLimit);

                    ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly + 2, lz + 2, 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly - 2, lz + 2, 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly + 2, lz - 2, 127, minorLimit);
                    ShapeGenerator.line(grid, lx, ly, lz, lx + 3, ly - 2, lz - 2, 127, minorLimit);
                }
            }
            else
            {
                for (int ln = 0; ln < launchers.size(); ln++) {
                    long launcher = launchers.get(ln);
                    if(currentlyFiring < 2 && launchers.size() > 8)
                    {
                        currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                        continue;
                    }
                    int lx = ((int)(launcher) & 0xFFFFF), ly = ((int)(launcher >>> 20) & 0xFFFFF), lz = (int)(launcher >>> 40) & 0xFFFFF;
                    ShapeGenerator.line(grid, lx + 2, ly, lz, lx + 9, ly, lz, 115, majorLimit);

                    currentlyFiring = (currentlyFiring + 1) % ((launchers.size() >>> 2) + 1);
                }
            }

        }


        return next;
    }
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
}
