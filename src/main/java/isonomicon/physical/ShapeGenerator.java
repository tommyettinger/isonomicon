package isonomicon.physical;

public class ShapeGenerator {
    public static byte[][][] line(byte[][][] into, int startX, int startY, int startZ, int endX, int endY, int endZ, int color){
        byte c = (byte) color;
        int dx = endX - startX, dy = endY - startY, dz = endZ - startZ,
                nx = Math.abs(dx), ny = Math.abs(dy), nz = Math.abs(dz);
        int signX = dx >> 31 | 1, signY = dy >> 31 | 1, signZ = dz >> 31 | 1,
                workX = startX, workY = startY, workZ = startZ;
        if(into[workX][workY][workZ] == 0) into[workX][workY][workZ] = c;
        for (int i = 1, ix = 0, iy = 0, iz = 0; ix < nx || iy < ny || iz < nz; i++) {
            float x = (0.5f + ix) / nx, y = (0.5f + iy) / ny, z = (0.5f + iz) / nz;
            if (x < y && x < z) {
                workX += signX;
                ix++;
            } else if(y <= x && y < z){
                workY += signY;
                iy++;
            } else {
                workZ += signZ;
                iz++;
            }
            if(into[workX][workY][workZ] == 0) into[workX][workY][workZ] = c;
        }
        return into;
    }
}
