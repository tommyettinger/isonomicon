package isonomicon.io.extended;

public class ShapeModel extends AbstractChunk {
    public ShapeModel(){
        attributes = new String[0][0];
    }
    public ShapeModel(int id, String[][] attributes){
        this.id = id;
        this.attributes = attributes == null ? new String[0][0] : attributes;
    }
}
