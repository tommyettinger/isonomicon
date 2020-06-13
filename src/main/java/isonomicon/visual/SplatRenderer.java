package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import squidpony.ArrayTools;

/**
 * Created by Tommy Ettinger on 12/16/2018.
 */
public class SplatRenderer {
    public Pixmap pixmap;
    public int[][] depths, working, render, outlines;
    public int[][][] v2sx, v2sy;
    public Colorizer color = Colorizer.ManosColorizer;
    public boolean easing = false, outline = true;

    public Pixmap pixmap() {
        return pixmap;
    }

    public SplatRenderer (final int size) {
        final int w = size * 4, h = size * 5;
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        working =  new int[w][h];
        render =   new int[w][h];
        depths =   new int[w][h];
        outlines = new int[w][h];
        v2sx = new int[size][size][size];
        v2sy = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            ArrayTools.fill(v2sx[i], -1);
            ArrayTools.fill(v2sy[i], -1);
        }
    }

    public Colorizer colorizer () {
        return color;
    }

    public SplatRenderer colorizer (Colorizer color) {
        this.color = color;
        return this;
    }
    
    public void splat(int xPos, int yPos, int zPos, byte voxel) {
        final int size = v2sx.length,
                xx = Math.max(1, (size + yPos - xPos) * 2 - 1),
                yy = Math.max(1, (zPos * 3 + size + size - xPos - yPos) - 1),
                depth = (xPos + yPos) * 2 + zPos * 3;

        for (int x = -1, ax = xx; x < 3 && ax < working.length; x++, ax++) {
            for (int y = -1, ay = yy; y < 3 && ay < working[0].length; y++, ay++) {
                working[ax][ay] = color.medium(voxel);
                depths[ax][ay] = depth;
                outlines[ax][ay] = color.dark(voxel);
                v2sx[xPos][yPos][zPos] = ax;
                v2sy[xPos][yPos][zPos] = ay;
            }
        }
    }
    
    public SplatRenderer clear() {
        pixmap.setColor(0);
        pixmap.fill();
        ArrayTools.fill(working, 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, 0);
        for (int i = 0; i < v2sx.length; i++) {
            ArrayTools.fill(v2sx[i], -1);
            ArrayTools.fill(v2sy[i], -1);
        }
        return this;
    }
    
    public Pixmap blit() {
        final int threshold = 9;
        final int size = v2sx.length;

        pixmap.setColor(0);
        pixmap.fill();
        int xSize = Math.min(pixmap.getWidth(), working.length) - 1, ySize = Math.min(pixmap.getHeight(), working[0].length) - 1, depth;
        for (int x = 0; x <= xSize; x++) {
            System.arraycopy(working[x], 0, render[x], 0, ySize);
        }
        
        for (int x = 0; x <= xSize; x++) {
            for (int y = 0; y <= ySize; y++) {
                if (render[x][y] != 0) {
                    pixmap.drawPixel(x, y, render[x][y]);
                }
            }
        }

        if (outline) {
            int o;
            for (int x = 1; x < xSize; x++) {
                for (int y = 1; y < ySize; y++) {
                    if ((o = outlines[x][y]) != 0) {
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

        ArrayTools.fill(render, 0);
        ArrayTools.fill(working, 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, 0);
        for (int i = 0; i < v2sx.length; i++) {
            ArrayTools.fill(v2sx[i], -1);
            ArrayTools.fill(v2sy[i], -1);
        }
        return pixmap;
    }
}
