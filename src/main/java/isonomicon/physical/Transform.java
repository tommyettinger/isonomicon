package isonomicon.physical;

import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.lerp;
import static com.badlogic.gdx.math.MathUtils.round;

/**
 * Stores the components of a transformation that can be applied to a voxel model (often a SlopeBox).
 * <br>
 * Created by Tommy Ettinger on 6/4/2019.
 */
public class Transform {
//    /**
//     * Rotation around x-axis in brads, typically 0-255.
//     */
//    public int roll = 0;
//    /**
//     * Rotation around y-axis in brads, typically 0-255.
//     */
//    public int pitch = 0;
//    /**
//     * Rotation around z-axis in brads, typically 0-255.
//     */
//    public int yaw = 0;

    
    public TurnQuaternion rotation;
    public int moveX = 0;
    public int moveY = 0;
    public int moveZ = 0;
    /**
     * Multiplier for stretching the model on its x-axis.
     */
    public float stretchX = 1f;
    /**
     * Multiplier for stretching the model on its y-axis.
     */
    public float stretchY = 1f;
    /**
     * Multiplier for stretching the model on its z-axis.
     */
    public float stretchZ = 1f;
    
    private final Vector3 temp = new Vector3();
    
    public Transform()
    {
        rotation = new TurnQuaternion();
    }

    /**
     * 
     * @param roll in brads, 0-255
     * @param pitch in brads, 0-255
     * @param yaw in brads, 0-255
     * @param moveX translation amount in voxel units
     * @param moveY translation amount in voxel units
     * @param moveZ translation amount in voxel units
     */
    public Transform(int roll, int pitch, int yaw, int moveX, int moveY, int moveZ)
    {
//        this.roll = roll;
//        this.pitch = pitch;
//        this.yaw = yaw;
        this.rotation = new TurnQuaternion().setEulerAnglesBrad(roll, pitch, yaw);
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
    }

    /**
     *
     * @param roll in brads, 0-255
     * @param pitch in brads, 0-255
     * @param yaw in brads, 0-255
     * @param moveX translation amount in voxel units
     * @param moveY translation amount in voxel units
     * @param moveZ translation amount in voxel units
     * @param stretchX how much to stretch as a multiplier for the x-axis
     * @param stretchY how much to stretch as a multiplier for the y-axis
     * @param stretchZ how much to stretch as a multiplier for the z-axis
     */
    public Transform(int roll, int pitch, int yaw, int moveX, int moveY, int moveZ,
                     float stretchX, float stretchY, float stretchZ)
    {
        this(roll, pitch, yaw, moveX, moveY, moveZ);
        this.stretchX = stretchX;
        this.stretchY = stretchY;
        this.stretchZ = stretchZ;
    }

    /**
     *
     * @param rotation a TurnQuaternion specifying the axis and angle of rotation
     * @param moveX translation amount in voxel units
     * @param moveY translation amount in voxel units
     * @param moveZ translation amount in voxel units
     * @param stretchX how much to stretch as a multiplier for the x-axis
     * @param stretchY how much to stretch as a multiplier for the y-axis
     * @param stretchZ how much to stretch as a multiplier for the z-axis
     */
    public Transform(TurnQuaternion rotation, int moveX, int moveY, int moveZ,
                     float stretchX, float stretchY, float stretchZ)
    {
        this.rotation = rotation;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
        this.stretchX = stretchX;
        this.stretchY = stretchY;
        this.stretchZ = stretchZ;
    }

    public Transform interpolate(Transform end, float alpha)
    {
        return new Transform(rotation.cpy().slerp(end.rotation, alpha),
                round(lerp(moveX, end.moveX, alpha)),
                round(lerp(moveY, end.moveY, alpha)),
                round(lerp(moveZ, end.moveZ, alpha)),
                lerp(stretchX, end.stretchX, alpha),
                lerp(stretchY, end.stretchY, alpha),
                lerp(stretchZ, end.stretchZ, alpha));
    }

    public Transform interpolateInto(Transform end, float alpha, Transform receiver)
    {
        receiver.rotation.set(rotation).slerp(end.rotation, alpha);
        receiver.moveX = round(lerp(moveX, end.moveX, alpha));
        receiver.moveY = round(lerp(moveY, end.moveY, alpha));
        receiver.moveZ = round(lerp(moveZ, end.moveZ, alpha));
        receiver.stretchX = lerp(stretchX, end.stretchX, alpha);
        receiver.stretchY = lerp(stretchY, end.stretchY, alpha);
        receiver.stretchZ = lerp(stretchZ, end.stretchZ, alpha);
        return receiver;
    }

    /**
     * Given a SlopeBox to use as a basis and a 3D point to rotate around like a socket joint, this makes a new SlopeBox
     * that may be rotated, translated, and/or stretched from its original location.
     * @param start a SlopeBox that will not be modified
     * @param jointX the x-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @param jointY the y-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @param jointZ the z-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @return a new SlopeBox that will contain a transformed copy of start 
     */
    public SlopeBox transform(SlopeBox start, float jointX, float jointY, float jointZ)
    {
        return transformInto(start, new SlopeBox(new byte[start.sizeX()][start.sizeY()][start.sizeZ()]),
                jointX, jointY, jointZ);
    }
    
    /**
     * Given a SlopeBox {@code start} to use as a basis, a (usually empty) SlopeBox {@code next} to fill with voxels,
     * and a 3D point to rotate around like a socket joint, this inserts voxels into {@code next} that may be rotated,
     * translated, and/or stretched from its original location.
     * @param start a SlopeBox that will not be modified
     * @param next a SlopeBox that will be modified, but won't be cleared (voxels will be added to its current content)
     * @param jointX the x-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @param jointY the y-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @param jointZ the z-coordinate of the joint to rotate around, which may be in-between voxel coordinates
     * @return {@code next}, with the added voxels, for chaining
     */
    public SlopeBox transformInto(SlopeBox start, SlopeBox next, float jointX, float jointY, float jointZ)
    {
        final int limX = next.sizeX(), limY = next.sizeY(), limZ = next.sizeZ();
        byte v;
        for (int x = 0; x < limX; x++) {
            for (int y = 0; y < limY; y++) {
                for (int z = 0; z < limZ; z++) {
                    temp.set(x - jointX, y - jointY, z - jointZ);
                    rotation.transform(temp).add(jointX + moveX, jointY + moveY, jointZ + moveZ);
                    if(temp.x < 0 || temp.y < 0 || temp.z < 0 || temp.x >= limX || temp.y >= limY || temp.z >= limZ)
                        continue;
                    v = start.color(x, y, z);
                    if(v == 0 || start.slope(x, y, z) != -1)
                        continue;
                    temp.scl(stretchX, stretchY, stretchZ).add(-0.5f, -0.5f, -0.5f);
                    for (int sx = 0; sx <= stretchX; sx++) {
                        for (int sy = 0; sy <= stretchY; sy++) {
                            for (int sz = 0; sz <= stretchZ; sz++) {
                                next.set((int) (temp.x + sx),
                                        (int) (temp.y + sy),
                                        (int) (temp.z + sz), v, -1);
                            }
                        }
                    }
                }
            }
        }
        return next.putSlopes();
    }

}
