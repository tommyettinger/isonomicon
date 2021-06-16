package isonomicon.physical;

import squidpony.squidmath.HastyPointHash;
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
            new Stuff("blood", 11, 0, 8L, "Roughness 0.04 Reflection 0.6"),
            new Stuff("guts", 12, 12, 12L, "Roughness 0.4 Reflection 0.2"),
            new Stuff("meat", 13, 11, 12L, "Roughness 0.5 Reflection 0.1"),
            new Stuff("brick", 14, 14, 0L, "Roughness 0.8 Reflection 0.02"),
            new Stuff("base hair", 15, 15, 512L, "Roughness 0.4 Reflection 0.3"),
            new Stuff("accented hair", 16, 16, 512L, "Roughness 0.3 Reflection 0.6"),
            new Stuff("base fur", 17, 17, 4L, "Roughness 0.5 Reflection 0.45"),
            new Stuff("polished wood", 18, 18, 32L, "Roughness 0.3 Reflection 0.7"),
            new Stuff("flush skin", 19, 19, 4L, "Roughness 0.4 Reflection 0.08"),
            new Stuff("ripe fruit", 20, 20, 32L, "Roughness 0.2 Reflection 0.55"),
            new Stuff("base skin", 21, 21, 4L, "Roughness 0.4 Reflection 0.08"),
            new Stuff("rough wood", 22, 22, 32L, "Roughness 0.7 Reflection 0.04"),
            new Stuff("base cloth", 23, 23, 24L, "Roughness 0.6 Reflection 0.04"),
            new Stuff("ceramic", 24, 24, 0L, "Roughness 0.2 Reflection 0.45"),
            new Stuff("dull feathers", 25, 25, 512L, "Roughness 0.9 Reflection 0.02"),
            new Stuff("unripe fruit", 26, 26, 32L, "Roughness 0.6 Reflection 0.3"),
            new Stuff("base scales", 27, 27, 4L, "Roughness 0.05 Reflection 0.55"),
            new Stuff("accented scales", 28, 28, 4L, "Roughness 0.05 Reflection 0.65"),
            new Stuff("mold", 29, 29, -1L, "Roughness 0.85 Reflection 0.01"),
            new Stuff("jungle mud", 30, 30, -1L, "Roughness 0.7 Reflection 0.1"),
            new Stuff("acid", 31, 31, -1L, "Roughness 0.2 Reflection 0.4 Missing 0.15"),
            new Stuff("vibrant leaf", 32, 32, 32L, "Roughness 0.1 Reflection 0.85"),
            new Stuff("moss", 33, 33, 32L, "Roughness 0.9 Reflection 0.03"),
            new Stuff("fine scales", 34, 34, 4L, "Roughness 0.15 Reflection 0.4"),
            new Stuff("old leaf", 35, 35, 32L, "Roughness 0.2 Reflection 0.25"),
            new Stuff("gas", 36, 0, 256L, "Roughness 0.25 Reflection 0.0 Flow 0.4 Rise 0.1"),
            new Stuff("dull protection", 37, 37, 64L, "Roughness 0.55 Reflection 0.4"),
            new Stuff("accented protection", 38, 38, 64L, "Roughness 0.45 Reflection 0.6"),
            new Stuff("slow water", 39, 0, -1L, "Roughness 0.1 Reflection 0.8 Missing 0.05"),
            new Stuff("shining marks", 40, 40, 64L, "Roughness 0.25 Reflection 0.9"),
            new Stuff("ice", 41, 41, -1L, "Roughness 0.05 Reflection 0.95"),
            new Stuff("accented marks", 42, 42, 64L, "Roughness 0.45 Reflection 0.8"),
            new Stuff("fast water", 43, 0, -1L, "Roughness 0.2 Reflection 0.75 Missing 0.15 Flow 0.7"),
            new Stuff("base marks", 44, 44, 64L, "Roughness 0.5 Reflection 0.65"),
            new Stuff("base protection", 45, 45, 64L, "Roughness 0.5 Reflection 0.55"),
            new Stuff("less obscured", 46, 46, -1L, "Roughness 0.4 Reflection 0.0"),
            new Stuff("faded paint", 47, 47, 64L, "Roughness 0.55 Reflection 0.3"),
            new Stuff("bold paint", 48, 48, 64L, "Roughness 0.4 Reflection 0.55"),
            new Stuff("deep water", 49, 0, -1L, "Roughness 0.3 Reflection 0.5 Flow 0.1"),
            new Stuff("more obscured", 50, 50, -1L, "Roughness 0.2 Reflection 0.0"),
            new Stuff("eerie matter", 51, 51, 64L, "Roughness 0.55 Reflection 0.4 Missing 0.15 Emission 0.05 Flow 0.1"),
            new Stuff("eerie slime", 52, 0, -1L, "Roughness 0.2 Reflection 0.7 Missing 0.25 Emission 0.2 Flow 0.35"),
            new Stuff("eerie device", 53, 53, 1L, "Roughness 0.1 Reflection 0.6 Emission 0.35"),
            new Stuff("phantasm", 54, 8, -1L, "Roughness 0.85 Reflection 0.2 Missing 0.4 Emission 0.1 Rise 0.2"),
            new Stuff("tentacle", 55, 55, 4L, "Roughness 0.45 Reflection 0.6 Flow 0.1"),
            new Stuff("drink", 56, 0, -1L, "Roughness 0.3 Reflection 0.5"),
            new Stuff("magic cloth", 57, 57, 16L, "Roughness 0.65 Reflection 0.7 Emission 0.25"),
            new Stuff("magic crystal", 58, 0, 128L, "Roughness 0.05 Reflection 0.85 Emission 0.35", 58, 7, 73, 1),
            new Stuff("magic mark", 59, 0, 2L, "Roughness 0.25 Reflection 0.65 Emission 0.3"),
            new Stuff("accented feather", 60, 60, 512L, "Roughness 0.7 Reflection 0.4"),
            new Stuff("decorated cloth", 61, 61, 24L, "Roughness 0.6 Reflection 0.4"),
            new Stuff("attachment", 62, 62, 128L, "Roughness 0.4 Reflection 0.3"),
            new Stuff("beak or claw", 63, 63, 64L, "Roughness 0.6 Reflection 0.45"),

            new Stuff("dark glass", 64, 69, 0L, "Transparency 0.3 Roughness 0.0 Reflection 0.4"),
            new Stuff("dark eye", 65, 65, 0L, "Roughness 0.0 Reflection 0.0"),
            new Stuff("smoke", 66, 66, 0L, "Roughness 0.5 Reflection 0.0 Missing 0.1 Rise 0.4", 0, 2, 66, 7),
            new Stuff("shadow", 67, 0, 0L, "Roughness 0.0 Reflection 0.0"),
            new Stuff("fly", 68, 0, 0L, "Roughness 0.2 Reflection 0.0 Missing 0.5", 82, 10),
            new Stuff("gray glass", 69, 71, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("dead eye", 70, 70, 0L, "Roughness 0.5 Reflection 0.0"),
            new Stuff("steam", 71, 71, 0L, "Transparency 0.5 Roughness 0.3 Missing 0.2 Flow 0.3 Rise 0.7", 0, 3, 71, 5),
            new Stuff("light eye", 72, 72, 0L, "Roughness 1.0 Reflection 1.0"),
            new Stuff("crystal sparkle", 73, 0, 128L, "Roughness 0.0 Reflection 0.95 Emission 0.75", 73, 1, 58, 5),
            new Stuff("pure light", 74, 0, 0L, "Roughness 1.0 Reflection 1.0 Emission 1.0"),
            new Stuff("evil eye", 75, 75, 0L, "Roughness 0.75 Reflection 0.5 Emission 0.15"),
            new Stuff("curse fading", 76, 0, 0L, "Roughness 0.25 Reflection 0.0 Emission 0.25 Rise 0.3 Missing 0.4", 0, 3, 76, 8),
            new Stuff("red glass", 77, 73, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("curse active", 78, 0, 0L, "Roughness 0.35 Reflection 0.0 Emission 0.6 Rise -0.4 Missing 0.1", 78, 7, 76, 4),
            new Stuff("orange glass", 79, 84, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("flying fur", 80, 80, 0L, "Roughness 0.4 Reflection 0.1 Missing 0.2", 80, 4, 0, 1),
            new Stuff("ember", 81, 0, 0L, "Roughness 0.0 Reflection 0.2 Emission 0.45 Missing 0.3 Rise 0.3 Flow 0.1", 81, 8, 126, 7, 66, 1),
            new Stuff("fly spawner", 82, 0, 0L, "Transparency 1.0", 82, 15, 68, 1),
            new Stuff("flying feathers", 83, 0, 0L, "Roughness 0.8 Reflection 0.05 Missing 0.15", 83, 7, 0, 1),
            new Stuff("hot fire", 84, 0, 0L, "Roughness 0.0 Reflection 0.1 Emission 0.6 Missing 0.15 Flow 0.1", 84, 11, 87, 8, 66, 3, 81, 2),
            new Stuff("shredded cloth", 85, 0, 0L, "Roughness 0.7 Reflection 0.05 Missing 0.1", 85, 9, 0, 1),
            new Stuff("wax", 86, 89, 0L, "Roughness 1.0 Reflection 0.1"),
            new Stuff("bright fire", 87, 0, 0L, "Roughness 0.0 Reflection 0.0 Emission 0.8 Missing 0.1 Flow 0.15", 84, 7, 87, 8, 81, 3),
            new Stuff("yellow glass", 88, 74, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("brown glass", 89, 79, 0L, "Transparency 0.35 Roughness 0.0 Reflection 0.65"),
            new Stuff("miasma spawner", 90, 0, 0L, "Transparency 1.0", 90, 7, 93, 1),
            new Stuff("radioactive glow", 91, 0, 0L, "Roughness 0.0 Reflection 0.0 Emission 0.6 Flow 0.3"),
            new Stuff("green glass", 92, 72, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("miasma", 93, 0, 0L, "Roughness 0.3 Reflection 0.0 Flow 0.5 Rise -0.1 Missing 0.05", 90, 2, 93, 5),
            new Stuff("wood shrapnel", 94, 94, 0L, "Roughness 0.9 Reflection 0.05 Missing 0.05", 94, 8, 0, 1),
            new Stuff("vigor active", 95, 0, 0L, "Roughness 0.0 Reflection 0.0 Emission 0.4", 95, 9, 76, 4, 0, 1),
            new Stuff("vigor spawner", 96, 0, 0L, "Transparency 1.0", 95, 1, 96, 6),
            new Stuff("vigor fading", 97, 0, 0L, "Roughness 0.0 Reflection 0.0 Emission 0.2 Missing 0.3", 0, 4, 97, 7, 95, 1),
            new Stuff("confirm particle", 98, 0, 0L, "Roughness 0.0 Reflection 0.0 Emission 0.7 Rise 0.9", 98, 1, 99, 1, 0, 1),
            new Stuff("confirm spawner", 99, 0, 0L, "Transparency 1.0", 98, 1, 99, 4),
            new Stuff("chaos spawner", 100, 0, 0L, "Transparency 1.0", 78, 1, 94, 1, 118, 1, 100, 5),
            new Stuff("metal glint", 101, 0, 0L, "Roughness 0.2 Reflection 1.0 Emission 0.5", 101, 1, 7, 14),
            new Stuff("darkened water", 102, 0, -1L, "Roughness 0.0 Reflection 0.0 Flow 0.1"),
            new Stuff("chill spawner", 103, 0, 0L, "Transparency 1.0", 104, 2, 103, 5),
            new Stuff("chill", 104, 0, 0L, "Roughness 0.8 Reflection 0.0 Missing 0.2 Transparency 0.5 Rise -0.1", 104, 3, 103, 2),
            new Stuff("clear glass", 105, 72, 0L, "Transparency 0.25 Roughness 0.0 Reflection 0.65"),
            new Stuff("splash ascent", 106, 0, -1L, "Roughness 0.6 Reflection 0.6 Flow 0.4 Rise 0.1", 106, 2, 107, 6, 109, 1),
            new Stuff("splash apex", 107, 0, -1L, "Roughness 0.7 Reflection 0.95 Flow 0.6", 107, 2, 108, 6, 106, 1),
            new Stuff("splash descent", 108, 0, -1L, "Roughness 0.5 Reflection 0.5 Flow 0.5 Rise -0.1", 108, 2, 109, 6, 107, 1),
            new Stuff("splash nadir", 109, 0, -1L, "Roughness 0.4 Reflection 0.3 Flow 0.7", 109, 2, 106, 6, 108, 1),
            new Stuff("mote spawner", 110, 0, 0L, "Transparency 1.0", 112, 1, 123, 1, 110, 9),
            new Stuff("blue glass", 111, 72, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("subtle mote", 112, 0, 0L, "Emission 0.2 Roughness 0.0 Reflection 0.0 Transparency 0.9", 112, 5, 110, 3),
            new Stuff("dread spawner", 113, 0, 0L, "Transparency 1.0", 114, 1, 113, 11),
            new Stuff("dread", 114, 120, 0L, "Emission -0.4 Roughness 0.0 Reflection 0.0 Transparency 0.7 Missing 0.1", 114, 4, 113, 3),
            new Stuff("unknown energy", 115, 0, 0L, "Emission 0.4 Roughness 0.3 Reflection 0.0 Flow 0.7 Missing 0.4", 115, 5, 117, 1, 0, 1),
            new Stuff("purple glass", 116, 73, 0L, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("spark spawner", 117, 0, 0L, "Transparency 1.0", 118, 1, 117, 14),
            new Stuff("spark", 118, 0, 0L, "Emission 0.6 Roughness 0.4 Reflection 0.0 Missing 0.6", 118, 2, 117, 3),
            new Stuff("sludge bubble", 119, 119, -1L, "Roughness 0.6 Reflection 0.2", 120, 1),
            new Stuff("sludge", 120, 119, -1L, "Roughness 0.6 Reflection 0.2 Flow 0.15", 120, 6, 119, 1),
            new Stuff("deny spawner", 121, 0, 0L, "Transparency 1.0", 125, 1, 121, 4),
            new Stuff("love spawner", 122, 0, 0L, "Transparency 1.0", 125, 1, 122, 5),
            new Stuff("powerful mote", 123, 0, 0L, "Emission 0.6 Roughness 0.0 Reflection 0.0 Transparency 0.8", 123, 5, 110, 3),
            new Stuff("love", 124, 0, 0L, "Emission 0.1 Roughness 0.5 Reflection 0.4 Rise 0.3", 124, 2, 122, 1),
            new Stuff("deny particle", 125, 0, 0L,  "Emission -0.15 Roughness 0.3 Reflection 0.2 Rise -0.4", 125, 1, 121, 1, 0, 1),
            new Stuff("ember spawner", 126, 0, 0L, "Transparency 1.0", 81, 1, 126, 9),
            new Stuff("violent flash", 127, 0, 0L, "Emission 0.9 Roughness 0.0 Reflection 0.0 Missing 0.2", 127, 1, 0, 5),
    };

    public static void evolve(byte[][][] model, int frame){
        for (int x = 0; x < model.length; x++) {
            for (int y = 0; y < model[x].length; y++) {
                for (int z = 0; z < model[x][y].length; z++) {
                    int v = model[x][y][z] & 255;
                    model[x][y][z] = STUFFS[v].transitionIDs[STUFFS[v].transitions.random(HastyPointHash.hash64(x, y, z, frame))];
                }
            }
        }
    }
}
