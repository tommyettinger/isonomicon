package isonomicon.physical;

public class ShapeGenerator {

    public static void writeIfEmpty(byte[][][] into, int x, int y, int z, byte color, Choice choice){
        if(x >= 0 && y >= 0 && z >= 0 && x < into.length && y < into[x].length && z < into[x][y].length
                && (color == 0) != (into[x][y][z] == 0) && choice.choose(x, y, z))
            into[x][y][z] = color;
    }
    public static void writeWideIfEmpty(byte[][][] into, int x, int y, int z, byte color, Choice choice) {
        writeIfEmpty(into, x, y, z, color, choice);
        writeIfEmpty(into, x + 1, y, z, color, choice);
        writeIfEmpty(into, x, y + 1, z, color, choice);
        writeIfEmpty(into, x, y, z + 1, color, choice);
        writeIfEmpty(into, x + 1, y + 1, z, color, choice);
        writeIfEmpty(into, x, y + 1, z + 1, color, choice);
        writeIfEmpty(into, x + 1, y, z + 1, color, choice);
        writeIfEmpty(into, x + 1, y + 1, z + 1, color, choice);
    }

    public static byte[][][] line(byte[][][] into, int startX, int startY, int startZ, int endX, int endY, int endZ, int color) {
        return line(into, startX, startY, startZ, endX, endY, endZ, color, (x, y, z) -> true);
    }
    public static byte[][][] line(byte[][][] into, int startX, int startY, int startZ, int endX, int endY, int endZ, int color, Choice choice) {
        byte c = (byte) color;
        int dx = endX - startX, dy = endY - startY, dz = endZ - startZ,
                nx = Math.abs(dx), ny = Math.abs(dy), nz = Math.abs(dz);
        int signX = dx >> 31 | 1, signY = dy >> 31 | 1, signZ = dz >> 31 | 1,
                workX = startX, workY = startY, workZ = startZ;
        writeIfEmpty(into, workX, workY, workZ, c, choice);
        for (int i = 1, ix = 0, iy = 0, iz = 0; ix < nx || iy < ny || iz < nz; i++) {
            float x = (0.5f + ix) / nx, y = (0.5f + iy) / ny, z = (0.5f + iz) / nz;
            if (x < y && x < z) {
                workX += signX;
                ix++;
            } else if (y <= x && y < z) {
                workY += signY;
                iy++;
            } else {
                workZ += signZ;
                iz++;
            }
            writeIfEmpty(into, workX, workY, workZ, c, choice);
        }
        return into;
    }

    public static byte[][][] wideLine(byte[][][] into, int startX, int startY, int startZ, int endX, int endY, int endZ, int color) {
        return wideLine(into, startX, startY, startZ, endX, endY, endZ, color, (x, y, z) -> true);
    }
    public static byte[][][] wideLine(byte[][][] into, int startX, int startY, int startZ, int endX, int endY, int endZ, int color, Choice choice) {
        byte c = (byte) color;
        int dx = endX - startX, dy = endY - startY, dz = endZ - startZ,
                nx = Math.abs(dx), ny = Math.abs(dy), nz = Math.abs(dz);
        int signX = dx >> 31 | 1, signY = dy >> 31 | 1, signZ = dz >> 31 | 1,
                workX = startX, workY = startY, workZ = startZ;
        writeWideIfEmpty(into, workX, workY, workZ, c, choice);
        for (int i = 1, ix = 0, iy = 0, iz = 0; ix < nx || iy < ny || iz < nz; i++) {
            float x = (0.5f + ix) / nx, y = (0.5f + iy) / ny, z = (0.5f + iz) / nz;
            if (x < y && x < z) {
                workX += signX;
                ix++;
            } else if (y <= x && y < z) {
                workY += signY;
                iy++;
            } else {
                workZ += signZ;
                iz++;
            }
            writeWideIfEmpty(into, workX, workY, workZ, c, choice);
        }
        return into;
    }

    public static byte[][][] ball(byte[][][] into, int centerX, int centerY, int centerZ, double radius, int color) {
        return ball(into, centerX, centerY, centerZ, radius, color, (x, y, z) -> true);
    }
    public static byte[][][] ball(byte[][][] into, int centerX, int centerY, int centerZ, double radius, int color, Choice choice) {
        byte c = (byte) color;
        int ceiling = (int)Math.ceil(radius);
        radius *= radius;
        for (int x = Math.max(0, centerX - ceiling); x <= Math.min(into.length - 1, centerX + ceiling); x++) {
            for (int y = Math.max(0, centerY - ceiling); y <= Math.min(into[x].length - 1, centerY + ceiling); y++) {
                for (int z = Math.max(0, centerZ - ceiling); z <= Math.min(into[x][y].length - 1, centerZ + ceiling); z++) {
                    int dx = centerX - x, dy = centerY - y, dz = centerZ - z;
                    if(dx * dx + dy * dy + dz * dz <= radius)
                        writeIfEmpty(into, x, y, z, c, choice);
                }
            }
        }
        return into;
    }
}
