package isonomicon.io.extended;

public class GroupChunk extends AbstractChunk {
    public int[] childIds;

    public GroupChunk() {
        attributes = new String[0][0];
        childIds = new int[0];
    }

    public GroupChunk(int id, String[][] attributes, int[] childIds) {
        this.id = id;
        this.attributes = attributes;
        this.childIds = childIds;
    }
}
