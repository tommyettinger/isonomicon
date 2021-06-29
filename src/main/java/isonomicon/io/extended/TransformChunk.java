package isonomicon.io.extended;

import com.badlogic.gdx.math.Vector3;

public class TransformChunk extends AbstractChunk {

    public int childId;
    public int reservedId;
    public int layerId;
    public String[][][] frameAttributes;
    public byte rotation = 0;
    public Vector3 translation = new Vector3();
    public TransformChunk(){
        attributes = new String[0][0];
        frameAttributes = new String[0][0][0];
    }
    public TransformChunk(int id, String[][] attributes, int childId, int reservedId, int layerId,
                          String[][][] frameAttributes){
        this.id = id;
        this.attributes = attributes == null ? new String[0][0] : attributes;
        this.childId = childId;
        this.reservedId = reservedId;
        this.layerId = layerId;
        this.frameAttributes = frameAttributes == null ? new String[0][0][0] : frameAttributes;
        if(this.frameAttributes.length != 0)
        {
            rotation = VoxIOExtended.getRotation(this.frameAttributes[0]);
            VoxIOExtended.getTranslation(translation, this.frameAttributes[0]);
        }
    }
}
