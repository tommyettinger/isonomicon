package isonomicon.physical;

import squidpony.squidmath.WeightedTable;

/**
 * Created by Tommy Ettinger on 5/2/2020.
 */
public class Stuff {
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

    public final VoxMaterial material;
    
    public Stuff(String name, int id, int outlineID, long meldBits, int... transitions){
        this(name, id, outlineID, meldBits, "", transitions);
    }
    public Stuff(String name, int id, int outlineID, long meldBits, String traits, int... transitions){
        this(name, id, outlineID, meldBits, "Diffuse", traits, transitions);
    }
    public Stuff(String name, int id, int outlineID, long meldBits, String type, String traits, int... transitions){
        this.name = name;
        this.id = (byte)id;
        this.outlineID = outlineID == 0 ? 0 : this.id;
        this.meld = meldBits;
        this.material = new VoxMaterial(type, traits);
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
    
    public static final Stuff[] STUFFS = new Stuff[]{
            new Stuff("transparent", 0, 0, 0L, "Glass", "Transparency 1.0"),
            new Stuff("dull metal", 1, 1, 1L, "Metal", "Reflection 0.2, Roughness 0.2"),
            new Stuff("char", 2, 2, -1L, "Reflection 0.0"),
            new Stuff("bog mud", 3, 3, -1L, "Reflection 0.05 Roughness 0.1"),
            new Stuff("wilted plant", 4, 4, 32L, "Reflection 0.0 Roughness 0.6"),
            new Stuff("natural stone", 5, 5, 2L, "Reflection 0.15 Roughness 0.7"),
            new Stuff("cut stone", 6, 6, 2L, "Reflection 0.3 Roughness 0.45"),
            new Stuff("polished metal", 7, 7, 1L, "Metal", "Reflection 0.8 Roughness 0.2", 101, 1, 7, 16),
            new Stuff("cloud", 8, 0, 256L, "Cloud", "Roughness 0.9 Reflection 0.25 Missing 0.06"),
            new Stuff("downy feather", 9, 10, 512L, "Roughness 0.95 Reflection 0.15"),
            new Stuff("bone", 10, 10, 8L, "Roughness 0.4 Reflection 0.4"),
            new Stuff("blood", 11, 0, 8L),
            new Stuff("guts", 12, 12, 12L),
            new Stuff("meat", 13, 11, 12L),
            new Stuff("brick", 14, 14, 0L),
            new Stuff("base hair", 15, 15, 512L),
            new Stuff("accented hair", 16, 16, 512L),
            new Stuff("base fur", 17, 17, 4L),
            new Stuff("polished wood", 18, 18, 32L),
            new Stuff("flush skin", 19, 19, 4L),
            new Stuff("ripe fruit", 20, 20, 32L),
            new Stuff("base skin", 21, 21, 4L),
            new Stuff("rough wood", 22, 22, 32L),
            new Stuff("base cloth", 23, 23, 24L),
            new Stuff("ceramic", 24, 24, 0L),
            new Stuff("dull feathers", 25, 25, 512L),
            new Stuff("unripe fruit", 26, 26, 32L),
            new Stuff("base scales", 27, 27, 4L),
            new Stuff("accented scales", 28, 28, 4L),
            new Stuff("mold", 29, 29, -1L),
            new Stuff("jungle mud", 30, 30, -1L),
            new Stuff("acid", 31, 31, -1L),
            new Stuff("vibrant leaf", 32, 32, 32L),
            new Stuff("moss", 33, 33, 32L),
            new Stuff("fine scales", 34, 34, 4L),
            new Stuff("old leaf", 35, 35, 32L),
            new Stuff("gas", 36, 0, 256L),
            new Stuff("dull protection", 37, 37, 64L),
            new Stuff("accented protection", 38, 38, 64L),
            new Stuff("slow water", 39, 0, -1L),
            new Stuff("shining marks", 40, 40, 64L),
            new Stuff("ice", 41, 41, -1L),
            new Stuff("accented marks", 42, 42, 64L),
            new Stuff("fast water", 43, 0, -1L),
            new Stuff("base marks", 44, 44, 64L),
            new Stuff("base protection", 45, 45, 64L),
            new Stuff("less obscured", 46, 46, -1L),
            new Stuff("faded paint", 47, 47, 64L),
            new Stuff("bold paint", 48, 48, 64L),
            new Stuff("deep water", 49, 0, -1L),
            new Stuff("more obscured", 50, 50, -1L),
            new Stuff("eerie matter", 51, 51, 64L),
            new Stuff("eerie slime", 52, 0, -1L),
            new Stuff("eerie device", 53, 53, 1L),
            new Stuff("phantasm", 54, 8, -1L),
            new Stuff("tentacle", 55, 55, 4L),
            new Stuff("drink", 56, 0, -1L),
            new Stuff("magic cloth", 57, 57, 16L),
            new Stuff("magic crystal", 58, 0, 128L, 58, 7, 73, 1),
            new Stuff("magic rune", 59, 0, 2L),
            new Stuff("accented feather", 60, 60, 512L),
            new Stuff("decorated cloth", 61, 61, 24L),
            new Stuff("attachment", 62, 62, 128L),
            new Stuff("beak or claw", 63, 63, 64L),

            new Stuff("dark glass", 64, 69, 0L),
            new Stuff("dark eye", 65, 65, 0L),
            new Stuff("smoke", 66, 66, 0L, 0, 2, 66, 7),
            new Stuff("shadow", 67, 0, 0L),
            new Stuff("fly", 68, 0, 0L, 82, 15),
            new Stuff("gray glass", 69, 71, 0L),
            new Stuff("dead eye", 70, 70, 0L),
            new Stuff("steam", 71, 71, 0L, 0, 3, 71, 5),
            new Stuff("light eye", 72, 72, 0L),
            new Stuff("crystal sparkle", 73, 0, 128L, 73, 1, 58, 5),
            new Stuff("pure light", 74, 0, 0L),
            new Stuff("evil eye", 75, 75, 0L),
            new Stuff("curse fading", 76, 0, 0L, 0, 3, 76, 8),
            new Stuff("red glass", 77, 73, 0L),
            new Stuff("curse active", 78, 0, 0L, 78, 7, 76, 4),
            new Stuff("orange glass", 79, 84, 0L),
            new Stuff("flying fur", 80, 80, 0L, 80, 4, 0, 1),
            new Stuff("ember", 81, 0, 0L, 81, 8, 126, 7, 66, 1),
            new Stuff("fly spawner", 82, 0, 0L, 82, 15, 68, 1),
            new Stuff("flying feathers", 83, 0, 0L, 83, 7, 0, 1),
            new Stuff("hot fire", 84, 0, 0L, 84, 11, 87, 8, 66, 3, 81, 2),
            new Stuff("shredded cloth", 85, 0, 0L, 85, 9, 0, 1),
            new Stuff("wax", 86, 89, 0L),
            new Stuff("bright fire", 87, 0, 0L, 84, 7, 87, 8, 81, 3),
            new Stuff("yellow glass", 88, 74, 0L),
            new Stuff("brown glass", 89, 79, 0L),
            new Stuff("miasma spawner", 90, 0, 0L, 90, 7, 93, 1),
            new Stuff("radioactive glow", 91, 0, 0L),
            new Stuff("green glass", 92, 72, 0L),
            new Stuff("miasma", 93, 0, 0L, 90, 2, 93, 5),
            new Stuff("wood shrapnel", 94, 94, 0L, 94, 8, 0, 1),
            new Stuff("vigor active", 95, 0, 0L, 95, 9, 76, 4, 0, 1),
            new Stuff("vigor spawner", 96, 0, 0L, 95, 1, 96, 6),
            new Stuff("vigor fading", 97, 0, 0L, 0, 4, 97, 7, 95, 1),
            new Stuff("confirm particle", 98, 0, 0L, 98, 1, 99, 1, 0, 1),
            new Stuff("confirm spawner", 99, 0, 0L, 98, 1, 99, 4),
            new Stuff("chaos spawner", 100, 0, 0L, 78, 1, 94, 1, 118, 1, 100, 5),
            new Stuff("metal glint", 101, 0, 0L, 101, 1, 7, 14),
            new Stuff("darkened pool", 102, 0, -1L),
            new Stuff("chill spawner", 103, 0, 0L, 104, 2, 103, 5),
            new Stuff("chill", 104, 0, 0L, 104, 3, 103, 2),
            new Stuff("clear glass", 105, 72, 0L),
            new Stuff("splash ascent", 106, 0, -1L, 106, 2, 107, 6, 109, 1),
            new Stuff("splash apex", 107, 0, -1L, 107, 2, 108, 6, 106, 1),
            new Stuff("splash descent", 108, 0, -1L, 108, 2, 109, 6, 107, 1),
            new Stuff("splash nadir", 109, 0, -1L, 109, 2, 106, 6, 108, 1),
            new Stuff("mote spawner", 110, 0, 0L, 112, 1, 123, 1, 110, 9),
            new Stuff("blue glass", 111, 72, 0L),
            new Stuff("subtle mote", 112, 0, 0L, 112, 5, 110, 3),
            new Stuff("dread spawner", 113, 0, 0L, 114, 1, 113, 11),
            new Stuff("dread", 114, 120, 0L, 114, 4, 113, 3),
            new Stuff("unknown energy", 115, 0, 0L, 115, 5, 117, 1, 0, 1),
            new Stuff("purple glass", 116, 73, 0L),
            new Stuff("spark spawner", 117, 0, 0L, 118, 1, 117, 14),
            new Stuff("spark", 118, 0, 0L, 118, 2, 117, 3),
            new Stuff("sludge bubble", 119, 119, -1L, 120, 1),
            new Stuff("sludge", 120, 119, -1L, 120, 6, 119, 1),
            new Stuff("deny spawner", 121, 0, 0L, 125, 1, 121, 4),
            new Stuff("love spawner", 122, 0, 0L, 125, 1, 122, 5),
            new Stuff("powerful mote", 123, 0, 0L, 123, 5, 110, 3),
            new Stuff("love", 124, 0, 0L, 124, 2, 122, 1),
            new Stuff("deny particle", 125, 0, 0L, 125, 1, 121, 1, 0, 1),
            new Stuff("ember spawner", 126, 0, 0L, 81, 1, 126, 9),
            new Stuff("violent flash", 127, 0, 0L, 127, 1, 0, 5),
    };
}
