package isonomicon.visual;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.IntIntMap;
import squidpony.ArrayTools;

/**
 * Created by Tommy Ettinger on 12/16/2018.
 */
public class VoxelPixmapRenderer {
    protected Pixmap pixmap;
    public int[][] depths, working, render, outlines;
    protected Colorizer color = Colorizer.ManosColorizer;
    public boolean easing = false, outline = true;

    public Pixmap pixmap() {
        return pixmap;
    }

    public VoxelPixmapRenderer pixmap (Pixmap pixmap) {
        this.pixmap = pixmap;
        working = new int[pixmap.getWidth()][pixmap.getHeight()];
        render = new int[pixmap.getWidth()][pixmap.getHeight()];
        depths = new int[pixmap.getWidth()][pixmap.getHeight()];
        outlines = new int[pixmap.getWidth()][pixmap.getHeight()];
        return this;
    }

    public Colorizer colorizer () {
        return color;
    }

    public VoxelPixmapRenderer colorizer (Colorizer color) {
        this.color = color;
        return this;
    }
    
    public void select(int px, int py, byte voxel, double[] shape, int depth) {
        for (int x = 0; x < 4 && px + x < working.length; x++) {
            for (int y = 0; y < 4 && py + y < working[0].length; y++) {
                if(shape[3-y << 2 | x] >= 6.0) continue;
                working[px+x][py+y] = color.dimmer((int)(shape[3-y << 2 | x] + 0.5*(px+x+py+y & 1) + 0.4*(px+x & py+y+1 & 1)), voxel);
                depths[px+x][py+y] = depth;
                outlines[px+x][py+y] = color.dark(voxel);
            }
        }
    }
    
    public Colorizer color() {
        return color;
    }

    public VoxelPixmapRenderer clear() {
        pixmap.setColor(0);
        pixmap.fill();
        ArrayTools.fill(working, 0);
        ArrayTools.fill(depths, 0);
        ArrayTools.fill(outlines, 0);
        return this;
    }

    private int lightness(int color) {
        return (color >>> 24) + (color >>> 16 & 0xFF) + (color >>> 8 & 0xFF);
    }

    private static final IntIntMap counts = new IntIntMap(9);

    public Pixmap blit(int threshold, int pixelWidth, int pixelHeight) {
        pixmap.setColor(0);
        pixmap.fill();
        int xSize = Math.min(pixelWidth, working.length) - 1, ySize = Math.min(pixelHeight, working[0].length) - 1, depth;
        for (int x = 0; x <= xSize; x++) {
            System.arraycopy(working[x], 0, render[x], 0, ySize);
        }
        if (outline) {
            int o;
            for (int x = 1; x < xSize; x++) {
                for (int y = 1; y < ySize; y++) {
                    if ((o = outlines[x][y]) != 0) {
                        depth = depths[x][y];
                        if (outlines[x - 1][y] == 0 && outlines[x][y - 1] == 0) {
                            render[x - 1][y] = o;
                            render[x][y - 1] = o;
                            render[x][y] = o;
                        } else if (outlines[x + 1][y] == 0 && outlines[x][y - 1] == 0) {
                            render[x + 1][y] = o;
                            render[x][y - 1] = o;
                            render[x][y] = o;
                        } else if (outlines[x - 1][y] == 0 && outlines[x][y + 1] == 0) {
                            render[x - 1][y] = o;
                            render[x][y + 1] = o;
                            render[x][y] = o;
                        } else if (outlines[x + 1][y] == 0 && outlines[x][y + 1] == 0) {
                            render[x + 1][y] = o;
                            render[x][y + 1] = o;
                            render[x][y] = o;
                        } else {
                            if (outlines[x - 1][y] == 0 || depths[x - 1][y] < depth - threshold) {
                                render[x - 1][y] = o;
                            }
                            if (outlines[x + 1][y] == 0 || depths[x + 1][y] < depth - threshold) {
                                render[x + 1][y] = o;
                            }
                            if (outlines[x][y - 1] == 0 || depths[x][y - 1] < depth - threshold) {
                                render[x][y - 1] = o;
                            }
                            if (outlines[x][y + 1] == 0 || depths[x][y + 1] < depth - threshold) {
                                render[x][y + 1] = o;
                            }
                        }
                    }
                }
            }
        }

        final int pmh = pixmap.getHeight() - 1;
        for (int x = 0; x <= xSize; x++) {
            for (int y = 0; y <= ySize; y++) {
                if (render[x][y] != 0) {
                    pixmap.drawPixel(x, pmh - y, render[x][y]);
//                    pixmap.setColor(render[x][y]);
//                    pixmap.fillRectangle(x * scaleX + offsetX, pmh - (y * scaleY) + offsetY, scaleX, scaleY);
                }
            }
        }

        if (easing) {
            int o, a, b, c, d, e, f, g, h;
            int tgt, lo;
            for (int x = 1; x < xSize; x++) {
                for (int y = 1; y < ySize; y++) {
                    o = render[x][y];
                    if (o != 0) {
                        counts.clear();
                        //counts.put(o, 2);
                        counts.getAndIncrement(a = render[x - 1][y - 1], 0, 1);
                        counts.getAndIncrement(b = render[x + 1][y - 1], 0, 1);
                        counts.getAndIncrement(c = render[x - 1][y + 1], 0, 1);
                        counts.getAndIncrement(d = render[x + 1][y + 1], 0, 1);
                        counts.getAndIncrement(e = render[x - 1][y], 0, 1);
                        counts.getAndIncrement(f = render[x + 1][y], 0, 1);
                        counts.getAndIncrement(g = render[x][y - 1], 0, 1);
                        counts.getAndIncrement(h = render[x][y + 1], 0, 1);
                        if (a != 0 && b != 0 && c != 0 && d != 0 && e != 0 && f != 0 && g != 0 && h != 0) {
                            tgt = 0;
                            lo = lightness(o);
                            if (counts.get(a, 0) >= 4 && lightness(a) < lo)
                                tgt = a;
                            else if (counts.get(b, 0) >= 4 && lightness(b) < lo)
                                tgt = b;
                            else if (counts.get(c, 0) >= 4 && lightness(c) < lo)
                                tgt = c;
                            else if (counts.get(d, 0) >= 4 && lightness(d) < lo)
                                tgt = d;
                            if (tgt != 0) {
                                pixmap.drawPixel(x, pmh - y, tgt);
//                                pixmap.setColor(tgt);
//                                pixmap.fillRectangle(x * scaleX + offsetX, pmh - (y * scaleY) + offsetY, scaleX, scaleY);
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
        return pixmap;
    }
}
