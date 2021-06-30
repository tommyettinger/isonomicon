package isonomicon.io.extended;

public class ShapeChunk extends AbstractChunk {
    public ShapeModel[] models;
    public ShapeChunk(){
        attributes = new String[0][0];
        models = new ShapeModel[0];
    }
    public ShapeChunk(int id, String[][] attributes, ShapeModel[] models) {
        this.id = id;
        this.attributes = attributes == null ? new String[0][0] : attributes;
        this.models = models == null ? new ShapeModel[0] : models;
    }
}
