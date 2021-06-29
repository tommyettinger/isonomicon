package isonomicon.io.extended;

public class TransformChunk extends AbstractChunk {

    public int childId;
    public int reservedId;
    public int layerId;
    public String[][][] frameAttributes;
    public TransformChunk(int id, String[][] attributes, int childId, int reservedId, int layerId,
                          String[][][] frameAttributes){
        this.id = id;
        this.attributes = attributes == null ? new String[0][0] : attributes;
        this.childId = childId;
        this.reservedId = reservedId;
        this.layerId = layerId;
        this.frameAttributes = frameAttributes == null ? new String[0][0][0] : frameAttributes;
    }
}
