package isonomicon.physical;

import squidpony.squidmath.WeightedTable;

/**
 * Created by Tommy Ettinger on 5/2/2020.
 */
public class Material {
    public final String name;
    /**
     * The Material's index in the static array of Materials (if positive); this also carries some semantic information.
     * If id is greater than 63 (as a signed byte, so the range of 64 to 127 inclusive), then this marks a non-sided
     * Material, that is, one that ignores most of the {@link SlopeBox#SIDES} information. In that case, transparent
     * side values (0) remain transparent, but all other values are considered 1.
     */
    public final byte id;
    /**
     * The ID of the Material that this will use for an outline; for physical, non-radiant Materials this usually equals
     * {@link #id}, but for radiant particles this could be 0 (no outline), or a very bright Material to add a halo.
     */
    public final byte outlineID;

    /**
     * A storage for the probability weights for what this Material may change into over the course of an animation; the
     * actual Material IDs are stored in {@link #transitionIDs}. This is often not initialized explicitly, in which case
     * this will store a simple WeightedTable that always returns index 0 (matching the index of the only ID in
     * transitionIDs under the same circumstance, which is almost always this Material's ID).
     */
    public final WeightedTable transitions;
    /**
     * A storage for the Material IDs that this may change into at each frame of an animation. This is often not
     * initialized explicitly, in which case this will just store an array containing only this Material's ID.
     */
    public final byte[] transitionIDs;
    /**
     * If two voxels have different Materials but those Materials have compatible meld fields, then a slope can be drawn
     * connecting them; otherwise, slopes should be blocked from forming. Two Materials are compatible to meld if any
     * bits are shared between this field on both Materials; that is, {@code (materialA.meld & materialB.meld) != 0}.
     * This helps prevent the appearance of a person "melting onto their clothes" or other problematic slopes. Using a
     * meld value of {@code -1L} will form slopes with any voxel that can meld at all (its meld must be non-0). 
     */
    public final long meld;
    
    public Material(String name, int id, int outlineID, long meldBits, int... transitions){
        this.name = name;
        this.id = (byte)id;
        this.outlineID = (byte)outlineID;
        this.meld = meldBits;
        if(transitions != null && transitions.length >= 2){
            double[] weights = new double[transitions.length >>> 1];
            transitionIDs = new byte[weights.length];
            for (int i = 0, t = 0; i < weights.length; i++) {
                transitionIDs[i] = (byte)transitions[t++];
                weights[i] = transitions[t++];
            }
            this.transitions = new WeightedTable(weights);
        }
        else {
            this.transitionIDs = new byte[]{this.id};
            this.transitions = new WeightedTable();
        }
    }
    
    public static final Material[] MATERIALS = new Material[]{
            new Material("transparent", 0, 0, 0L),
            new Material("dull metal", 1, 1, 1L),
            new Material("char", 2, 2, -1L),
            new Material("bog mud", 3, 3, -1L),
            new Material("wilted plant", 4, 4, 32L),
            new Material("natural stone", 5, 5, 2L),
            new Material("cut stone", 6, 6, 2L),
            new Material("polished metal", 7, 7, 1L),
            new Material("cloud", 8, 0, 256L),
            new Material("inert crystal", 9, 9, 2L),
            new Material("brightest cloth", 10, 10, 24L),
            new Material("blood", 11, 0, 8L),
            new Material("guts", 12, 12, 12L),
            new Material("meat", 13, 11, 12L),
            new Material("brick", 14, 14, 0L),
            new Material("decorated cloth", 15, 15, 24L),
            new Material("rough wood", 16, 16, 32L),
            new Material("base fur", 17, 17, 12L),
            new Material("polished wood", 18, 18, 32L),
            new Material("flush skin", 19, 19, 12L),
            new Material("ripe fruit", 20, 20, 32L),
            new Material("base skin", 21, 21, 12L),
            new Material("growing wood", 22, 22, 32L),
            new Material("base cloth", 23, 23, 24L),
            new Material("finished clay", 24, 24, 0L),
            new Material("dirty mud", 25, 25, -1L),
            new Material("unripe fruit", 26, 26, 32L),
            new Material("base scales", 27, 27, 12L),
            new Material("accented scales", 28, 28, 12L),
            new Material("mold", 29, 29, -1L),
            new Material("jungle mud", 30, 30, -1L),
            new Material("vibrant leaf", 31, 31, 32L)
    };
}
