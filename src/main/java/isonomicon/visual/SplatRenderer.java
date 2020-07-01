package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import squidpony.ArrayTools;

/**
 * Created by Tommy Ettinger on 12/16/2018.
 */
public class SplatRenderer {
    public Pixmap pixmap, pixmapHalf, pixmapQuarter;
    public int[][] depths, voxels;
    public int[][] shadeX, shadeZ;
    public byte[][] working, render, outlines;
    public Colorizer color = Colorizer.ManosColorizer, alternate = Colorizer.FullGrayColorizer;
    public boolean dither = false, outline = true, useAlternate = false;

    public SplatRenderer (final int size) {
        final int w = size * 4 + 4, h = size * 5 + 4;
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmapHalf = new Pixmap(w>>>1, h>>>1, Pixmap.Format.RGBA8888);
        pixmapQuarter = new Pixmap(w>>>2, h>>>2, Pixmap.Format.RGBA8888);
        working =  new byte[w][h];
        render =   new byte[w][h];
        outlines = new byte[w][h];
        depths =   new int[w][h];
        voxels   = new int[w][h];
        shadeX = ArrayTools.fill(-1, size, size);
        shadeZ = ArrayTools.fill(-1, size, size);
    }

    public Colorizer colorizer () {
        return color;
    }

    public SplatRenderer colorizer (Colorizer color) {
        this.color = color;
        return this;
    }
    
    public void splat(int xPos, int yPos, int zPos, byte voxel) {
        final int size = shadeZ.length,
                xx = Math.max(0, (size + yPos - xPos) * 2 - 1),
                yy = Math.max(0, (zPos * 3 + size + size - xPos - yPos) - 1),
                depth = (xPos + yPos) * 2 + zPos * 3;

        for (int x = 0, ax = xx; x < 4 && ax < working.length; x++, ax++) {
            for (int y = 0, ay = yy; y < 4 && ay < working[0].length; y++, ay++) {
                working[ax][ay] = (byte) ((voxel & 0x3F) | 0xC0);
                depths[ax][ay] = depth;
                outlines[ax][ay] = color.blacken((byte)(voxel & 0x3F));
                voxels[ax][ay] = xPos | yPos << 10 | zPos << 20; 
            }
        }
        shadeZ[xPos][yPos] = Math.max(shadeZ[xPos][yPos], zPos);
        shadeX[yPos][zPos] = Math.max(shadeX[yPos][zPos], xPos);
    }
    
    public SplatRenderer clear() {
        pixmap.setColor(0);
        pixmap.fill();
        ArrayTools.fill(working, (byte) 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, (byte) 0);
        ArrayTools.fill(voxels, -1);
        ArrayTools.fill(shadeX, -1);
        ArrayTools.fill(shadeZ, -1);
        return this;
    }
    
    public Pixmap blit() {
        final int threshold = 9;
        final Colorizer finisher = useAlternate ? alternate : color;
        pixmap.setColor(0);
        pixmap.fill();
        int xSize = Math.min(pixmap.getWidth(), working.length) - 1, ySize = Math.min(pixmap.getHeight(), working[0].length) - 1, depth;
        for (int x = 0; x <= xSize; x++) {
            System.arraycopy(working[x], 0, render[x], 0, ySize);
        }
        int v, vx, vy, vz;
        for (int sx = 0; sx <= xSize; sx++) {
            for (int sy = 0; sy <= ySize; sy++) {
                if((v = voxels[sx][sy]) != -1)
                {
                    vx = v & 0x3FF;
                    vy = v >>> 10 & 0x3FF;
                    vz = v >>> 20 & 0x3FF;
                    if(shadeZ[vx][vy] == vz)
                        render[sx][sy] = (byte) (64 |  color.brighten((byte) (0x3F & working[sx][sy])));
                    else if(shadeX[vy][vz] != vx)
                        render[sx][sy] = (byte) (128 | color.darken((byte) (0x3F & working[sx][sy])));
                }
            }
        }

        for (int x = 0; x <= xSize; x++) {
            for (int y = 0; y <= ySize; y++) {
                if (render[x][y] != 0) {
                    pixmap.drawPixel(x, y, finisher.medium(render[x][y]));
                }
            }
        }
        if (outline) {
            byte ob;
            int o;
            for (int x = 1; x < xSize; x++) {
                for (int y = 1; y < ySize; y++) {
                    if ((ob = outlines[x][y]) != 0) {
                        o = finisher.medium(ob);
                        depth = depths[x][y];
                        if (outlines[x - 1][y] == 0 && outlines[x][y - 1] == 0) {
                            pixmap.drawPixel(x - 1, y    , o);
                            pixmap.drawPixel(x    , y - 1, o);
                            pixmap.drawPixel(x    , y    , o);
                        } else if (outlines[x + 1][y] == 0 && outlines[x][y - 1] == 0) {
                            pixmap.drawPixel(x + 1, y    , o);
                            pixmap.drawPixel(x    , y - 1, o);
                            pixmap.drawPixel(x    , y    , o); 
                        } else if (outlines[x - 1][y] == 0 && outlines[x][y + 1] == 0) {
                            pixmap.drawPixel(x - 1, y    , o);
                            pixmap.drawPixel(x    , y + 1, o);
                            pixmap.drawPixel(x    , y    , o);
                        } else if (outlines[x + 1][y] == 0 && outlines[x][y + 1] == 0) {
                            pixmap.drawPixel(x + 1, y    , o);
                            pixmap.drawPixel(x    , y + 1, o);
                            pixmap.drawPixel(x    , y    , o);
                        } else {
                            if (outlines[x - 1][y] == 0 || depths[x - 1][y] < depth - threshold) {
                                pixmap.drawPixel(x - 1, y    , o);
                            }
                            if (outlines[x + 1][y] == 0 || depths[x + 1][y] < depth - threshold) {
                                pixmap.drawPixel(x + 1, y    , o);
                            }
                            if (outlines[x][y - 1] == 0 || depths[x][y - 1] < depth - threshold) {
                                pixmap.drawPixel(x    , y - 1, o);
                            }
                            if (outlines[x][y + 1] == 0 || depths[x][y + 1] < depth - threshold) {
                                pixmap.drawPixel(x    , y + 1, o);
                            }
                        }
                    }
                }
            }
        }
        if(dither) {
//        Colorizer.AuroraColorizer.reducer.setDitherStrength(0.375f);
//        Colorizer.AuroraColorizer.reducer.reduceKnollRoberts(pixmap);
            finisher.reducer.setDitherStrength(1.5f);
            finisher.reducer.reduceKnollRoberts(pixmap);
        }

        ArrayTools.fill(render, (byte) 0);
        ArrayTools.fill(working, (byte) 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, (byte) 0);
        ArrayTools.fill(voxels, -1);
        ArrayTools.fill(shadeX, -1);
        ArrayTools.fill(shadeZ, -1);
        return pixmap;
    }

    public Pixmap blitHalf() {
        final int threshold = 8;
        final Colorizer finisher = useAlternate ? alternate : color;
        pixmapHalf.setColor(0);
        pixmapHalf.fill();
        int xSize = working.length - 1, ySize = working[0].length - 1, depth;
        for (int x = 0; x <= xSize; x++) {
            System.arraycopy(working[x], 0, render[x], 0, ySize);
        }
        int v, vx, vy, vz;
        for (int sx = 0; sx <= xSize; sx++) {
            for (int sy = 0; sy <= ySize; sy++) {
                if((v = voxels[sx][sy]) != -1)
                {
                    vx = v & 0x3FF;
                    vy = v >>> 10 & 0x3FF;
                    vz = v >>> 20 & 0x3FF;
                    if(shadeZ[vx][vy] == vz)
                        render[sx][sy] = (byte) (64 |  color.brighten((byte) (0x3F & working[sx][sy])));
                    else if(shadeX[vy][vz] != vx)
                        render[sx][sy] = (byte) (128 | color.darken((byte) (0x3F & working[sx][sy])));
                }
            }
        }

        for (int x = 0; x <= xSize; x++) {
//            final int choice = (x & 2) >>> 1 | (x & 1) << 1;
            for (int y = 0; y <= ySize; y++) {
//                if((y & 3) != choice) continue;
                if (render[x][y] != 0) {
                    pixmapHalf.drawPixel(x >>> 1, y >>> 1, finisher.medium(render[x][y]));
                }
            }
        }
        if (outline) {
            byte ob;
            int o;
            for (int x = 1; x < xSize; x++) {
//                final int choice = (x & 2) >>> 1 | (x & 1) << 1;
                final int hx = x >>> 1;
                for (int y = 1; y < ySize; y++) {
//                    if((y & 3) != choice) continue;
                    int hy = y >>> 1;
                    if ((ob = outlines[x][y]) != 0) {
                        o = finisher.medium(ob);
                        depth = depths[x][y];
//                        if (outlines[x - 1][y] == 0 && outlines[x][y - 1] == 0) {
//                            pixmapHalf.drawPixel(hx - 1, hy    , o);
//                            pixmapHalf.drawPixel(hx    , hy - 1, o);
//                            pixmapHalf.drawPixel(hx    , hy    , o);
//                        } else if (outlines[x + 1][y] == 0 && outlines[x][y - 1] == 0) {
//                            pixmapHalf.drawPixel(hx + 1, hy    , o);
//                            pixmapHalf.drawPixel(hx    , hy - 1, o);
//                            pixmapHalf.drawPixel(hx    , hy    , o); 
//                        } else if (outlines[x - 1][y] == 0 && outlines[x][y + 1] == 0) {
//                            pixmapHalf.drawPixel(hx - 1, hy    , o);
//                            pixmapHalf.drawPixel(hx    , hy + 1, o);
//                            pixmapHalf.drawPixel(hx    , hy    , o);
//                        } else if (outlines[x + 1][y] == 0 && outlines[x][y + 1] == 0) {
//                            pixmapHalf.drawPixel(hx + 1, hy    , o);
//                            pixmapHalf.drawPixel(hx    , hy + 1, o);
//                            pixmapHalf.drawPixel(hx    , hy    , o);
//                        } else {
                        if (outlines[x - 1][y] == 0 || depths[x - 1][y] < depth - threshold) {
                            pixmapHalf.drawPixel(hx - 1, hy    , o);
                        }
                        if (outlines[x + 1][y] == 0 || depths[x + 1][y] < depth - threshold) {
                            pixmapHalf.drawPixel(hx + 1, hy    , o);
                        }
                        if (outlines[x][y - 1] == 0 || depths[x][y - 1] < depth - threshold) {
                            pixmapHalf.drawPixel(hx    , hy - 1, o);
                        }
                        if (outlines[x][y + 1] == 0 || depths[x][y + 1] < depth - threshold) {
                            pixmapHalf.drawPixel(hx    , hy + 1, o);
                        }
//                        }
                    }
                }
            }
        }
        if(dither) {
            finisher.reducer.setDitherStrength(1.5f);
            finisher.reducer.reduceKnollRoberts(pixmapHalf);
        }

        ArrayTools.fill(render, (byte) 0);
        ArrayTools.fill(working, (byte) 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, (byte) 0);
        ArrayTools.fill(voxels, -1);
        ArrayTools.fill(shadeX, -1);
        ArrayTools.fill(shadeZ, -1);
        return pixmapHalf;
    }

    public Pixmap blitQuarter() {
        final int threshold = 12;
        final Colorizer finisher = useAlternate ? alternate : color;
        pixmapQuarter.setColor(0);
        pixmapQuarter.fill();
        int xSize = working.length - 1, ySize = working[0].length - 1, depth;
        for (int x = 0; x <= xSize; x++) {
            System.arraycopy(working[x], 0, render[x], 0, ySize);
        }
        int v, vx, vy, vz;
        for (int sx = 0; sx <= xSize; sx++) {
            for (int sy = 0; sy <= ySize; sy++) {
                if((v = voxels[sx][sy]) != -1)
                {
                    vx = v & 0x3FF;
                    vy = v >>> 10 & 0x3FF;
                    vz = v >>> 20 & 0x3FF;
                    if(shadeZ[vx][vy] == vz)
                        render[sx][sy] = (byte) (64 |  color.brighten((byte) (0x3F & working[sx][sy])));
                    else if(shadeX[vy][vz] != vx)
                        render[sx][sy] = (byte) (128 | color.darken((byte) (0x3F & working[sx][sy])));
                }
            }
        }

        for (int x = 0; x <= xSize; x++) {
//            final int choice = (x & 2) >>> 1 | (x & 1) << 1;
            for (int y = 0; y <= ySize; y++) {
//                if((y & 3) != choice) continue;
                if (render[x][y] != 0) {
                    pixmapQuarter.drawPixel(x >>> 2, y >>> 2, finisher.medium(render[x][y]));
                }
            }
        }
        if (outline) {
            byte ob;
            int o;
            for (int x = 1; x < xSize; x++) {
//                final int choice = (0xC >>> (x >>> 2 & 3) & 3);
                final int qx = x >>> 2;
                for (int y = 1; y < ySize; y++) {
                    //if((y & 3) != choice || (x & 3) != (0xC >>> (y >>> 2 & 3) & 3)) continue;
                    int qy = y >>> 2;
                    if ((ob = outlines[x][y]) != 0) {
                        o = finisher.medium(ob);
                        depth = depths[x][y];
                        if (outlines[x - 1][y] == 0 || depths[x - 1][y] < depth - threshold) {
                            pixmapQuarter.drawPixel(qx - 1, qy    , o);
                        }
                        if (outlines[x + 1][y] == 0 || depths[x + 1][y] < depth - threshold) {
                            pixmapQuarter.drawPixel(qx + 1, qy    , o);
                        }
                        if (outlines[x][y - 1] == 0 || depths[x][y - 1] < depth - threshold) {
                            pixmapQuarter.drawPixel(qx    , qy - 1, o);
                        }
                        if (outlines[x][y + 1] == 0 || depths[x][y + 1] < depth - threshold) {
                            pixmapQuarter.drawPixel(qx    , qy + 1, o);
                        }
                    }
                }
            }
        }
        if(dither) {
            finisher.reducer.setDitherStrength(1.5f);
            finisher.reducer.reduceKnollRoberts(pixmapQuarter);
        }

        ArrayTools.fill(render, (byte) 0);
        ArrayTools.fill(working, (byte) 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, (byte) 0);
        ArrayTools.fill(voxels, -1);
        ArrayTools.fill(shadeX, -1);
        ArrayTools.fill(shadeZ, -1);
        return pixmapQuarter;
    }

    public Pixmap drawSplats(byte[][][] colors) {
        // To move one x+ in voxels is x + 2, y - 1 in pixels.
        // To move one x- in voxels is x - 2, y + 1 in pixels.
        // To move one y+ in voxels is x - 2, y - 1 in pixels.
        // To move one y- in voxels is x + 2, y + 1 in pixels.
        // To move one z+ in voxels is y + 3 in pixels.
        // To move one z- in voxels is y - 3 in pixels.
        final int size = colors.length;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if(v != 0)
                        splat(x, y, z, v);
                }
            }
        }
        return blit();
    }

    public Pixmap drawSplatsHalf(byte[][][] colors) { 
        final int size = colors.length;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if(v != 0)
                        splat(x, y, z, v);
                }
            }
        }
        return blitHalf();
    }

    public Pixmap drawSplatsQuarter(byte[][][] colors) { 
        final int size = colors.length;
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final byte v = colors[x][y][z];
                    if(v != 0)
                        splat(x, y, z, v);
                }
            }
        }
        return blitQuarter();
    }

}
