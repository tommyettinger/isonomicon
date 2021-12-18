package isonomicon.physical;

import com.github.tommyettinger.ds.IntObjectMap;
import squidpony.squidmath.HastyPointHash;
import squidpony.squidmath.WeightedTable;

/**
 * Created by Tommy Ettinger on 5/2/2020.
 */
public class Stuff {
    public final String name;
    /**
     * The Material's index in the static array of Materials (if positive); this also carries some semantic information.
     * If id is 192 or greater (as an unsigned byte, so the range of 192 to 255 inclusive), then this is an invisible
     * marker voxel, used for some purpose by non-rendering code.
     */
    public final byte id;

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

    public final VoxMaterial material;

    /**
     * Determines which id of Stuff this looks like. This is mostly relevant for id numbers between 128 and 191, since
     * those usually don't have an assigned palette color and use the default unless they appear as a different Stuff.
     */
    public final int appearsAs;
    
    public Stuff(String name, int id, String traits, int... transitions){
        this(name, id, "Diffuse", traits, transitions);
    }
    public Stuff(String name, int id, String type, String traits, int... transitions){
        this(name, id, id, type, traits, transitions);
    }
    public Stuff(String name, int id, int appearsAs, String traits, int... transitions) {
        this(name, id, appearsAs, "Diffuse", traits, transitions);
    }
    public Stuff(String name, int id, int appearsAs, String type, String traits, int... transitions){
        this.name = name;
        this.id = (byte)id;
        this.material = new VoxMaterial(type, traits);
        this.appearsAs = appearsAs;
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
            new Stuff("transparent", 0, "Glass", "Transparency 1.0"),
            new Stuff("dull metal", 1, "Metal", "Reflection 0.2, Roughness 0.2"),
            new Stuff("char", 2, "Reflection 0.0"),
            new Stuff("wet dirt", 3, "Reflection 0.05 Roughness 0.1"),
            new Stuff("dry dirt", 4, "Reflection 0.0 Roughness 0.85 Dapple -0.05"),
            new Stuff("natural stone", 5, "Reflection 0.15 Roughness 0.7"),
            new Stuff("cut stone", 6, "Reflection 0.3 Roughness 0.45"),
            new Stuff("polished metal", 7, "Metal", "Reflection 0.8 Roughness 0.2", 101, 1, 7, 16),
            new Stuff("cloud", 8, "Cloud", "Roughness 0.9 Reflection 0.25 Missing 0.06"),
            new Stuff("downy feather", 9, "Roughness 0.95 Reflection 0.15"),
            new Stuff("bone", 10, "Roughness 0.4 Reflection 0.4"),
            new Stuff("blood", 11, "Roughness 0.04 Reflection 0.6"),
            new Stuff("guts", 12, "Roughness 0.4 Reflection 0.2"),
            new Stuff("meat", 13, "Roughness 0.5 Reflection 0.1"),
            new Stuff("brick", 14, "Roughness 0.8 Reflection 0.02"),
            new Stuff("base hair", 15, "Roughness 0.4 Reflection 0.3"),
            new Stuff("accented hair", 16, "Roughness 0.3 Reflection 0.6"),
            new Stuff("base fur", 17, "Roughness 0.5 Reflection 0.45"),
            new Stuff("polished wood", 18, "Roughness 0.3 Reflection 0.7 Dapple -0.2"),
            new Stuff("flush skin", 19, "Roughness 0.4 Reflection 0.08"),
            new Stuff("ripe fruit", 20, "Roughness 0.2 Reflection 0.55"),
            new Stuff("base skin", 21, "Roughness 0.4 Reflection 0.08"),
            new Stuff("rough wood", 22, "Roughness 0.7 Reflection 0.04 Dapple 0.1 Vary -0.05"),
            new Stuff("base cloth", 23, "Roughness 0.6 Reflection 0.04"),
            new Stuff("ceramic", 24, "Roughness 0.2 Reflection 0.45"),
            new Stuff("dull feathers", 25, "Roughness 0.9 Reflection 0.02"),
            new Stuff("unripe fruit", 26, "Roughness 0.6 Reflection 0.3"),
            new Stuff("base scales", 27, "Roughness 0.05 Reflection 0.55"),
            new Stuff("accented scales", 28, "Roughness 0.05 Reflection 0.65"),
            new Stuff("mold", 29, "Roughness 0.85 Reflection 0.01"),
            new Stuff("mud", 30, "Roughness 0.7 Reflection 0.1"),
            new Stuff("acid", 31, "Roughness 0.2 Reflection 0.4 Missing 0.15"),
            new Stuff("vibrant leaf", 32, "Roughness 0.1 Reflection 0.85 Dapple -0.3"),
            new Stuff("moss", 33, "Roughness 0.9 Reflection 0.03"),
            new Stuff("fine scales", 34, "Roughness 0.15 Reflection 0.4"),
            new Stuff("old leaf", 35, "Roughness 0.2 Reflection 0.25 Vary -0.3"),
            new Stuff("gas", 36, "Roughness 0.25 Reflection 0.0 Flow 0.4 Rise 0.1"),
            new Stuff("dull protection", 37, "Roughness 0.55 Reflection 0.4"),
            new Stuff("accented protection", 38, "Roughness 0.45 Reflection 0.6"),
            new Stuff("slow water", 39, "Roughness 0.1 Reflection 0.8 Missing 0.05"),
            new Stuff("shining marks", 40, "Roughness 0.25 Reflection 0.9"),
            new Stuff("ice", 41, "Roughness 0.05 Reflection 0.95"),
            new Stuff("accented marks", 42, "Roughness 0.45 Reflection 0.8"),
            new Stuff("fast water", 43, "Roughness 0.2 Reflection 0.75 Missing 0.15 Flow 0.7"),
            new Stuff("base marks", 44, "Roughness 0.5 Reflection 0.65"),
            new Stuff("base protection", 45, "Roughness 0.5 Reflection 0.55"),
            new Stuff("less obscured", 46, "Roughness 0.4 Reflection 0.0"),
            new Stuff("faded paint", 47, "Roughness 0.55 Reflection 0.3"),
            new Stuff("bold paint", 48, "Roughness 0.4 Reflection 0.55"),
            new Stuff("deep water", 49, "Roughness 0.3 Reflection 0.5 Flow 0.1"),
            new Stuff("more obscured", 50, "Roughness 0.2 Reflection 0.0"),
            new Stuff("eerie matter", 51, "Roughness 0.55 Reflection 0.4 Missing 0.15 Emission 0.05 Flow 0.1"),
            new Stuff("eerie slime", 52, "Roughness 0.2 Reflection 0.7 Missing 0.25 Emission 0.2 Flow 0.35"),
            new Stuff("eerie device", 53, "Roughness 0.1 Reflection 0.6 Emission 0.35"),
            new Stuff("phantasm", 54, "Roughness 0.85 Reflection 0.2 Missing 0.4 Emission 0.1 Rise 0.2"),
            new Stuff("tentacle", 55, "Roughness 0.45 Reflection 0.6 Flow 0.1"),
            new Stuff("drink", 56, "Roughness 0.3 Reflection 0.5"),
            new Stuff("magic cloth", 57, "Roughness 0.65 Reflection 0.7 Emission 0.25"),
            new Stuff("magic crystal", 58, "Roughness 0.05 Reflection 0.85 Emission 0.35", 58, 7, 73, 1),
            new Stuff("magic mark", 59, "Roughness 0.25 Reflection 0.65 Emission 0.3"),
            new Stuff("accented feather", 60, "Roughness 0.7 Reflection 0.4"),
            new Stuff("decorated cloth", 61, "Roughness 0.6 Reflection 0.4"),
            new Stuff("attachment", 62, "Roughness 0.4 Reflection 0.3"),
            new Stuff("beak or claw", 63, "Roughness 0.6 Reflection 0.45"),

            new Stuff("dark glass", 64, "Transparency 0.3 Roughness 0.0 Reflection 0.4"),
            new Stuff("dark eye", 65, "Roughness 0.0 Reflection 0.0"),
            new Stuff("smoke", 66, "Roughness 0.5 Reflection 0.0 Missing 0.1 Rise 0.4", 0, 2, 66, 7),
            new Stuff("shadow", 67, "Roughness 0.0 Reflection 0.0"),
            new Stuff("fly", 68, "Roughness 0.2 Reflection 0.0 Missing 0.5", 82, 10),
            new Stuff("gray glass", 69, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("dead eye", 70, "Roughness 0.5 Reflection 0.0"),
            new Stuff("steam", 71, "Transparency 0.5 Roughness 0.3 Missing 0.2 Flow 0.3 Rise 0.7", 0, 3, 71, 5),
            new Stuff("light eye", 72, "Roughness 1.0 Reflection 1.0"),
            new Stuff("crystal sparkle", 73, "Roughness 0.0 Reflection 0.95 Emission 0.75", 73, 1, 58, 5),
            new Stuff("pure light", 74, "Roughness 1.0 Reflection 1.0 Emission 1.0"),
            new Stuff("evil eye", 75, "Roughness 0.75 Reflection 0.5 Emission 0.15"),
            new Stuff("curse fading", 76, "Roughness 0.25 Reflection 0.0 Emission 0.25 Rise 0.3 Missing 0.4", 0, 3, 76, 8),
            new Stuff("red glass", 77, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("curse active", 78, "Roughness 0.35 Reflection 0.0 Emission 0.6 Rise -0.4 Missing 0.1", 78, 7, 76, 4),
            new Stuff("orange glass", 79, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("flying fur", 80, "Roughness 0.4 Reflection 0.1 Missing 0.2", 80, 4, 0, 1),
            new Stuff("ember", 81, "Roughness 0.0 Reflection 0.2 Emission 0.45 Missing 0.3 Rise 0.3 Flow 0.1", 81, 8, 126, 7, 66, 1),
            new Stuff("fly spawner", 82, "Transparency 1.0", 82, 15, 68, 1),
            new Stuff("flying feathers", 83, "Roughness 0.8 Reflection 0.05 Missing 0.15", 83, 7, 0, 1),
            new Stuff("hot fire", 84, "Roughness 0.0 Reflection 0.1 Emission 0.6 Missing 0.15 Flow 0.1", 84, 11, 87, 8, 66, 3, 81, 2),
            new Stuff("shredded cloth", 85, "Roughness 0.7 Reflection 0.05 Missing 0.1", 85, 9, 0, 1),
            new Stuff("wax", 86, "Roughness 1.0 Reflection 0.1"),
            new Stuff("bright fire", 87, "Roughness 0.0 Reflection 0.0 Emission 0.8 Missing 0.1 Flow 0.15", 84, 7, 87, 8, 81, 3),
            new Stuff("yellow glass", 88, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("brown glass", 89, "Transparency 0.35 Roughness 0.0 Reflection 0.65"),
            new Stuff("miasma spawner", 90, "Transparency 1.0", 90, 7, 93, 1),
            new Stuff("radioactive glow", 91, "Roughness 0.0 Reflection 0.0 Emission 0.6 Flow 0.3"),
            new Stuff("green glass", 92, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("miasma", 93, "Roughness 0.3 Reflection 0.0 Flow 0.5 Rise -0.1 Missing 0.05", 90, 2, 93, 5),
            new Stuff("wood shrapnel", 94, "Roughness 0.9 Reflection 0.05 Missing 0.05", 94, 8, 0, 1),
            new Stuff("vigor active", 95, "Roughness 0.0 Reflection 0.0 Emission 0.4", 95, 9, 97, 4, 0, 1),
            new Stuff("vigor spawner", 96, "Transparency 1.0", 95, 1, 96, 6),
            new Stuff("vigor fading", 97, "Roughness 0.0 Reflection 0.0 Emission 0.2 Missing 0.3", 0, 4, 97, 7, 95, 1),
            new Stuff("confirm particle", 98, "Roughness 0.0 Reflection 0.0 Emission 0.7 Rise 0.9", 98, 1, 99, 1, 0, 1),
            new Stuff("confirm spawner", 99, "Transparency 1.0", 98, 1, 99, 4),
            new Stuff("chaos spawner", 100, "Transparency 1.0", 78, 1, 94, 1, 118, 1, 100, 5),
            new Stuff("metal glint", 101, "Roughness 0.2 Reflection 1.0 Emission 0.5", 101, 1, 7, 14),
            new Stuff("darkened water", 102, "Roughness 0.0 Reflection 0.0 Flow 0.1"),
            new Stuff("chill spawner", 103, "Transparency 1.0", 104, 2, 103, 5),
            new Stuff("chill", 104, "Roughness 0.8 Reflection 0.0 Missing 0.2 Transparency 0.5 Rise -0.1", 104, 3, 103, 2),
            new Stuff("clear glass", 105, "Transparency 0.25 Roughness 0.0 Reflection 0.65"),
            new Stuff("splash ascent", 106, "Roughness 0.6 Reflection 0.6 Flow 0.4 Rise 0.1", 106, 2, 107, 6, 109, 1),
            new Stuff("splash apex", 107, "Roughness 0.7 Reflection 0.95 Flow 0.6", 107, 2, 108, 6, 106, 1),
            new Stuff("splash descent", 108, "Roughness 0.5 Reflection 0.5 Flow 0.5 Rise -0.1", 108, 2, 109, 6, 107, 1),
            new Stuff("splash nadir", 109, "Roughness 0.4 Reflection 0.3 Flow 0.7", 109, 2, 106, 6, 108, 1),
            new Stuff("mote spawner", 110, "Transparency 1.0", 112, 1, 123, 1, 110, 9),
            new Stuff("blue glass", 111, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("subtle mote", 112, "Emission 0.2 Roughness 0.0 Reflection 0.0 Transparency 0.9", 112, 5, 110, 3),
            new Stuff("dread spawner", 113, "Transparency 1.0", 114, 1, 113, 11),
            new Stuff("dread", 114, "Emission -0.4 Roughness 0.0 Reflection 0.0 Transparency 0.7 Missing 0.1", 114, 4, 113, 3),
            new Stuff("unknown energy", 115, "Emission 0.4 Roughness 0.3 Reflection 0.0 Flow 0.7 Missing 0.4", 115, 5, 117, 1, 0, 1),
            new Stuff("purple glass", 116, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("spark spawner", 117, "Transparency 1.0", 118, 1, 117, 14),
            new Stuff("spark", 118, "Emission 0.6 Roughness 0.4 Reflection 0.0 Missing 0.6", 118, 2, 117, 3),
            new Stuff("sludge bubble", 119, "Roughness 0.6 Reflection 0.2", 120, 1),
            new Stuff("sludge", 120, "Roughness 0.6 Reflection 0.2 Flow 0.15", 120, 6, 119, 1),
            new Stuff("deny spawner", 121, "Transparency 1.0", 125, 1, 121, 4),
            new Stuff("love spawner", 122, "Transparency 1.0", 124, 1, 122, 5),
            new Stuff("powerful mote", 123, "Emission 0.6 Roughness 0.0 Reflection 0.0 Transparency 0.8", 123, 5, 110, 3),
            new Stuff("love", 124, "Emission 0.1 Roughness 0.5 Reflection 0.4 Rise 0.3", 124, 2, 122, 1),
            new Stuff("deny particle", 125, "Emission -0.15 Roughness 0.3 Reflection 0.2 Rise -0.4", 125, 1, 121, 1, 0, 1),
            new Stuff("ember spawner", 126, "Transparency 1.0", 81, 1, 126, 9),
            new Stuff("violent flash", 127, "Emission 0.9 Roughness 0.0 Reflection 0.0 Missing 0.2", 127, 1, 0, 5),
    };

    /**
     * Uses the Betts-64 palette instead of Manos-64 for the default colors. This is meant to be a rework of the
     * original STUFFS, with things that don't generally appear on one model merged (fur and feathers), and some things
     * that didn't have enough variety expanded (earth/dirt/mud/rock/sand, metal, and plant materials).
     */
    public static final Stuff[] STUFFS_B = new Stuff[]{
            new Stuff("transparent", 0, "Glass", "Transparency 1.0"),
            new Stuff("glossy leather", 1, "Reflection 0.7, Roughness 0.05"), /*black*/
            new Stuff("matte leather", 2, "Reflection 0.05 Roughness 0.6"),
            new Stuff("rusted metal", 3, "Metal", "Reflection 0.04 Roughness 0.4 Vary -0.4"),
            new Stuff("dull metal", 4, "Metal", "Reflection 0.15 Roughness 0.8"),
            new Stuff("scratched metal", 5, "Metal", "Reflection 0.5 Roughness 0.65"),
            new Stuff("polished metal", 6, "Metal", "Reflection 0.8 Roughness 0.2"),
            new Stuff("snow", 7, "Roughness 0.9 Reflection 0.4"), /*white*/
            new Stuff("sickly leaf", 8, "Roughness 0.2 Reflection 0.25 Vary -0.35"), /*light lime*/
            new Stuff("unripe fruit or bud", 9, "Roughness 0.6 Reflection 0.3 Dapple -0.1"),
            new Stuff("acid", 10, "Roughness 0.5 Reflection 0.7 Flow 0.6"),
            new Stuff("mold", 11, "Roughness 0.9 Reflection 0.0 Vary -0.5"), /*dark lime*/
            new Stuff("moss", 12, "Roughness 0.9 Reflection 0.03"), /*dark green*/
            new Stuff("cactus plant", 13, "Roughness 0.4 Reflection 0.2"),
            new Stuff("matte leaf", 14, "Roughness 0.4 Reflection 0.05 Dapple -0.35"),
            new Stuff("glossy leaf", 15, "Roughness 0.1 Reflection 0.85 Dapple -0.1"),
            new Stuff("succulent plant", 16, "Roughness 0.8 Reflection 0.04"),
            new Stuff("gas", 17, "Roughness 0.65 Reflection 0.0 Flow 0.4 Rise 0.1"), /*light green*/
            new Stuff("fine scales", 18, "Roughness 0.15 Reflection 0.4"), /*light cyan*/
            new Stuff("base scales", 19, "Roughness 0.05 Reflection 0.55"),
            new Stuff("accented scales", 20, "Roughness 0.05 Reflection 0.65"), /*dark cyan*/
            new Stuff("deep water", 21, "Roughness 0.3 Reflection 0.5"), /*dark azure*/
            new Stuff("slow water", 22, "Roughness 0.1 Reflection 0.8 Flow 0.1"),
            new Stuff("fast water", 23, "Roughness 0.2 Reflection 0.75 Flow 0.7"),
            new Stuff("wet stone", 24, "Roughness 0.5 Reflection 0.2"),
            new Stuff("ice", 25, "Roughness 0.05 Reflection 0.95", 25, 8, 82, 1), /*light azure*/
            new Stuff("shining mark", 26, "Roughness 0.25 Reflection 0.9"), /*light blue*/
            new Stuff("accented mark", 27, "Roughness 0.4 Reflection 0.5"),
            new Stuff("bold mark", 28, "Roughness 0.4 Reflection 0.5"),
            new Stuff("deep mark", 29, "Roughness 0.4 Reflection 0.5"), /*dark blue*/
            new Stuff("coal", 30, "Roughness 0.1 Reflection 0.0"), /*dark violet*/
            new Stuff("ore", 31, "Roughness 0.7 Reflection 0.1 Vary -0.3 Dapple 0.1"),
            new Stuff("raw stone", 32, "Roughness 0.55 Reflection 0.15 Vary -0.1"),
            new Stuff("worked stone", 33, "Roughness 0.25 Reflection 0.2"),
            new Stuff("chipped stone", 34, "Roughness 0.8 Reflection 0.05"), /*light violet*/
            new Stuff("magic crystal", 35, "Roughness 0.05 Reflection 0.85 Emission 0.35", 35, 7, 98, 1), /*light purple*/
            new Stuff("ectoplasm", 36, "Roughness 0.2 Reflection 0.4 Flow 0.7 Emission 0.1"),
            new Stuff("tentacle", 37, "Roughness 0.5 Reflection 0.02 Dapple 0.2"),
            new Stuff("apparition", 38, "Roughness 0.6 Reflection 0.0 Flow 0.1 Emission -0.2"), /*dark purple*/
            new Stuff("drink", 39, "Roughness 0.3 Reflection 0.5"), /*burgundy*/
            new Stuff("marked cloth", 40, "Roughness 0.1 Reflection 0.04"), /*medium magenta*/
            new Stuff("base cloth", 41, "Roughness 0.25 Reflection 0.06"),
            new Stuff("highlight cloth", 42, "Roughness 0.15 Reflection 0.1"),
            new Stuff("shiny cloth", 43, "Roughness 0.4 Reflection 0.75"), /*light pink*/
            new Stuff("sand", 44, "Roughness 0.7 Reflection 0.0"), /*light tan*/
            new Stuff("rubble", 45, "Roughness 0.55 Reflection 0.0 Dapple -0.4 Vary -0.3"),
            new Stuff("dry dirt", 46, "Roughness 0.85 Reflection 0.0 Dapple -0.05"),
            new Stuff("wet dirt or mud", 47, "Roughness 0.1 Reflection 0.05"), /*dark tan*/
            new Stuff("marked fluff", 48, "Roughness 0.6 Reflection 0.15 Dapple -0.16"), /*dark orange*/
            new Stuff("base fluff", 49, "Roughness 0.5 Reflection 0.4"),                        // Dapple -0.08
            new Stuff("highlight fluff", 50, "Roughness 0.4 Reflection 0.75"), /*hot orange*/   // Dapple -0.04
            new Stuff("scar", 51, "Roughness 0.7 Reflection 0.05"), /*light skin*/
            new Stuff("base skin", 52, "Roughness 0.6 Reflection 0.2"),
            new Stuff("nose", 53, "Roughness 0.6 Reflection 0.3"),
            new Stuff("freckle", 54, "Roughness 0.6 Reflection 0.2 Vary -0.1"),
            new Stuff("ears", 55, "Roughness 0.5 Reflection 0.1"),
            new Stuff("overripe fruit or rot", 56, "Roughness 0.2 Reflection 0.02 Dapple -0.15 Vary -0.1"), /*dark skin*/
            new Stuff("lips blood gore", 57, "Roughness 0.04 Reflection 0.4"), /*gore*/
            new Stuff("knotted wood", 58, "Roughness 0.5 Reflection 0.1 Dapple -0.1"), /*dark brown*/
            new Stuff("old wood", 59, "Roughness 0.6 Reflection 0.0 Dapple -0.25 Vary -0.35"),
            new Stuff("fresh wood", 60, "Roughness 0.4 Reflection 0.0 Dapple -0.15 Vary -0.1"), /*medium brown*/
            new Stuff("gold", 61, "Roughness 0.5 Reflection 0.85", 126, 1, 61, 12), /*dark yellow*/
            new Stuff("ripe fruit or flower", 62, "Roughness 0.2 Reflection 0.55 Dapple -0.2"),
            new Stuff("bone beak claw", 63, "Roughness 0.4 Reflection 0.4"), /*light yellow*/

            new Stuff("dark glass", 64, "Transparency 0.3 Roughness 0.0 Reflection 0.4"),
            new Stuff("dark eye", 65, "Roughness 0.0 Reflection 0.1"), /*black*/
            new Stuff("shadow", 66, "Roughness 0.0 Reflection 0.0 Emission -0.25"),
            new Stuff("smoke", 67, "Roughness 0.5 Reflection 0.0 Missing 0.1 Rise 0.4 Dapple -0.15", 0, 2, 67, 7),
            new Stuff("gray glass", 68, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("dead eye", 69, "Roughness 0.5 Reflection 0.0"),
            new Stuff("steam", 70, "Transparency 0.5 Roughness 0.3 Missing 0.2 Flow 0.3 Rise 0.7", 0, 3, 70, 5),
            new Stuff("light eye", 71, "Roughness 1.0 Reflection 1.0"), /*white*/
            new Stuff("radioactive glow", 72, "Roughness 0.0 Reflection 0.0 Emission 0.6 Transparency 1.0"), /*light lime*/
            new Stuff("miasma", 73, "Roughness 0.3 Reflection 0.0 Flow 0.5 Rise -0.1 Missing 0.05", 75, 2, 73, 5),
            new Stuff("rustling leaf", 74, "Roughness 0.4 Reflection 0.05 Dapple -0.35 Missing 0.2"),
            new Stuff("miasma spawner", 75, "Transparency 1.0", 75, 7, 73, 1), /*dark lime*/
            new Stuff("strobe off", 76, "Roughness 0.03 Reflection 0.5 Rate 0.5", 80, 1), /*dark green*/
            new Stuff("vigor spawner", 77, "Transparency 1.0", 79, 1, 77, 4),
            new Stuff("green glass", 78, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("vigor particle", 79, "Roughness 0.0 Reflection 0.0 Emission 0.45 Rise 0.9", 79, 1, 77, 1, 0, 1),
            new Stuff("strobe on", 80, "Roughness 0.03 Reflection 0.05 Emission 0.75 Rate 0.5", 76, 1),
            new Stuff("constant light", 81, "Roughness 0.0 Reflection 0.0 Emission 0.8"), /*light green*/
            new Stuff("ice glint", 82, "Roughness 0.4 Reflection 0.95 Emission 0.4", 82, 1, 25, 20), /*light cyan*/
            new Stuff("chill particle", 83, "Roughness 0.8 Reflection 0.0 Missing 0.2 Transparency 0.5 Rise -0.1", 83, 7, 84, 1),
            new Stuff("chill spawner", 84, "Transparency 1.0", 84, 5, 83, 2), /*dark cyan*/
            new Stuff("splash lowest", 85, "Roughness 0.4 Reflection 0.3 Flow 0.7", 85, 3, 86, 5), /*dark azure*/
            new Stuff("splash rise", 86, "Roughness 0.6 Reflection 0.6 Flow 0.4 Rise 0.1", 86, 2, 87, 6, 85, 1),
            new Stuff("splash highest", 87, "Roughness 0.7 Reflection 0.95 Flow 0.6", 87, 2, 88, 6, 86, 1),
            new Stuff("splash curl", 88, "Roughness 0.5 Reflection 0.5 Flow 0.5 Rise -0.1", 88, 2, 85, 6, 87, 1),
            new Stuff("metal glint", 89, "Roughness 0.2 Reflection 1.0 Emission 0.5", 89, 1, 146, 14), /*light azure*/
            new Stuff("blue glass", 90, "Transparency 0.5 Roughness 0.0 Reflection 0.65"), /*light blue*/
            new Stuff("speech particle", 91, "Roughness 0.0 Reflection 0.0 Flow 0.2 Rise 0.6 Missing 0.08", 91, 1, 92, 1),
            new Stuff("speech spawner", 92, "Transparency 1.0", 92, 3, 91, 4),
            new Stuff("dread spawner", 93, "Transparency 1.0", 94, 1, 93, 11), /*dark blue*/
            new Stuff("dread particle", 94, "Emission -0.4 Roughness 0.0 Reflection 0.0 Transparency 0.7 Missing 0.1", 94, 4, 93, 3), /*dark violet*/
            new Stuff("stored energy", 95, "Emission 0.4 Roughness 0.3 Reflection 0.0 Flow 0.7 Missing 0.8", 95, 5, 96, 1, 0, 1),
            new Stuff("shock spawner", 96, "Transparency 1.0", 97, 1, 96, 14),
            new Stuff("shock particle", 97, "Emission 0.6 Roughness 0.4 Reflection 0.0 Missing 0.6", 97, 2, 96, 3),
            new Stuff("crystal glint", 98, "Roughness 0.1 Reflection 1.0 Emission 0.65", 98, 1, 35, 8), /*light violet*/
            new Stuff("sludge bubble", 99, "Roughness 0.6 Reflection 0.2 Vary -0.3 Dapple 0.15", 101, 1), /*light purple*/
            new Stuff("purple glass", 100, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("toxic sludge", 101, "Roughness 0.6 Reflection 0.2 Flow 0.15 Vary -0.3 Dapple 0.15", 101, 6, 99, 1),
            new Stuff("void", 102, "Roughness 0.0 Reflection 0.0 Emission -0.9 Transparency 1.0"), /*dark purple*/
            new Stuff("evil eye", 103, "Roughness 0.75 Reflection 0.5 Emission 0.15"), /*burgundy*/
            new Stuff("red glass", 104, "Transparency 0.5 Roughness 0.0 Reflection 0.65"), /*medium magenta*/
            new Stuff("love spawner", 105, "Transparency 1.0", 106, 1, 105, 5),
            new Stuff("love particle", 106, "Emission 0.1 Roughness 0.5 Reflection 0.4 Rise 0.3", 106, 2, 105, 1),
            new Stuff("flower petals", 107, "Roughness 0.4 Reflection 0.0 Rise -0.1", 107, 2, 108, 6, 106, 1), /*light pink*/
            new Stuff("clear glass", 108, "Transparency 0.25 Roughness 0.0 Reflection 0.65"), /*light tan*/
            new Stuff("sand particle", 109, "Roughness 0.6 Reflection 0.05 Flow 0.2 Rise 0.1", 109, 3, 110, 1),
            new Stuff("sand spawner", 110, "Transparency 1.0", 109, 1, 110, 5),
            new Stuff("dirt splatter", 111, "Roughness 0.4 Reflection 0.0 Rise -0.1"), /*dark tan*/
            new Stuff("lava", 112, "Emission 0.2 Roughness 0.6 Reflection 0.0 Flow 0.05 Dapple -0.5"), /*dark orange*/
            new Stuff("ember", 113, "Roughness 0.0 Reflection 0.2 Emission 0.45 Missing 0.3 Rise 0.3 Dapple 0.3", 113, 8, 119, 7, 67, 1),
            new Stuff("hot fire", 114, "Roughness 0.0 Reflection 0.0 Emission 0.6 Missing 0.15 Dapple 0.15 Flow 0.18 Rise 0.06", 114, 11, 115, 8, 67, 3, 113, 2), /*hot orange*/
            new Stuff("bright fire", 115, "Roughness 0.0 Reflection 0.0 Emission 0.8 Missing 0.1 Flow 0.15 Rise 0.08", 114, 7, 115, 8, 113, 3), /*light skin*/
            new Stuff("orange glass", 116, "Transparency 0.5 Roughness 0.0 Reflection 0.65"),
            new Stuff("fluff pieces", 117, "Roughness 0.8 Reflection 0.05 Missing 0.15", 117, 7, 0, 1),
            new Stuff("flailing limbs", 118, "Roughness 0.6 Reflection 0.2 Missing 0.3"),
            new Stuff("ember spawner", 119, "Transparency 1.0", 113, 1, 119, 9),
            new Stuff("brown glass", 120, "Transparency 0.35 Roughness 0.0 Reflection 0.65"), /*dark skin*/
            new Stuff("gore splatter", 121, "Roughness 0.45 Reflection 0.15 Rise -0.1"), /*gore*/
            new Stuff("wood pieces", 122, "Roughness 0.6 Reflection 0.0 Dapple -0.25 Vary -0.35 Missing 0.5 Rise -0.2", 0, 1, 122, 3), /*dark brown*/
            new Stuff("stone pieces", 123, "Roughness 0.8 Reflection 0.05 Missing 0.45 Rise -0.25", 0, 2, 123, 7),
            new Stuff("cloth pieces", 124, "Roughness 0.25 Reflection 0.06 Missing 0.4 Rise -0.05", 0, 2, 124, 5), /*medium brown*/
            new Stuff("yellow glass", 125, "Transparency 0.5 Roughness 0.0 Reflection 0.65"), /*dark yellow*/
            new Stuff("gold glint", 126, "Roughness 0.2 Reflection 1.0 Emission 0.2", 126, 1, 61, 20),
            new Stuff("fiery spark", 127, "Emission 0.9 Roughness 0.4 Reflection 0.0 Missing 0.6", 127, 2, 0, 3), /*light yellow*/

            new Stuff("vanishing shadow", 128, "Emission -0.5 Transparency 1.0", 0, 1),
            new Stuff("matte leather swap 0", 129, 2, "Reflection 0.05 Roughness 0.6 Frame 0.0"), /*black*/
            new Stuff("matte leather swap 1", 130, 2, "Reflection 0.05 Roughness 0.6 Frame 1.0"),
            new Stuff("dull metal swap 0", 131, 4, "Metal", "Reflection 0.15 Roughness 0.8 Frame 0.0"),
            new Stuff("dull metal swap 1", 132, 4, "Metal", "Reflection 0.15 Roughness 0.8 Frame 1.0"),
            new Stuff("scratched metal swap 0", 133, 5, "Metal", "Reflection 0.5 Roughness 0.65 Frame 0.0"),
            new Stuff("scratched metal swap 1", 134, 5, "Metal", "Reflection 0.5 Roughness 0.65 Frame 1.0"),
            new Stuff("vanishing flash", 135, "Emission 0.8 Transparency 1.0", 0, 1), /*white*/
            new Stuff("radiation pulse 0", 136, 72, "Roughness 0.0 Reflection 0.0 Emission 0.2 Transparency 0.9", 137, 1), /*light lime*/
            new Stuff("radiation pulse 1", 137, 72, "Roughness 0.0 Reflection 0.0 Emission 0.4 Transparency 0.9", 138, 1),
            new Stuff("radiation pulse 2", 138, 72, "Roughness 0.0 Reflection 0.0 Emission 0.6 Transparency 0.9", 139, 1),
            new Stuff("radiation pulse 3", 139, 72, "Roughness 0.0 Reflection 0.0 Emission 0.4 Transparency 0.9", 136, 1), /*dark lime*/
            new Stuff("flickering light off", 140, 76, "Roughness 0.03 Reflection 0.5", 140, 14, 141, 1), /*dark green*/
            new Stuff("flickering light on", 141, 81, "Roughness 0.0 Reflection 0.0 Emission 0.5", 141, 11, 140, 1),
            new Stuff("unused 142", 142, "Transparency 1.0"),
            new Stuff("unused 143", 143, "Transparency 1.0"),
            new Stuff("unused 144", 144, "Transparency 1.0"),
            new Stuff("unused 145", 145, "Transparency 1.0"),/*light green*/
            new Stuff("gleaming metal", 146, 6, "Metal", "Reflection 0.9 Roughness 0.15", 89, 1, 146, 16),/*light cyan*/
            new Stuff("unused 147", 147, "Transparency 1.0"),
            new Stuff("unused 148", 148, "Transparency 1.0"),
            new Stuff("unused 149", 149, "Transparency 1.0"),
            new Stuff("unused 150", 150, "Transparency 1.0"),
            new Stuff("unused 151", 151, "Transparency 1.0"),
            new Stuff("unused 152", 152, "Transparency 1.0"),
            new Stuff("unused 153", 153, "Transparency 1.0"),
            new Stuff("unused 154", 154, "Transparency 1.0"),
            new Stuff("unused 155", 155, "Transparency 1.0"),
            new Stuff("unused 156", 156, "Transparency 1.0"),
            new Stuff("unused 157", 157, "Transparency 1.0"),
            new Stuff("unused 158", 158, "Transparency 1.0"),
            new Stuff("unused 159", 159, "Transparency 1.0"),
            new Stuff("unused 160", 160, "Transparency 1.0"),
            new Stuff("unused 161", 161, "Transparency 1.0"),
            new Stuff("unused 162", 162, "Transparency 1.0"),
            new Stuff("unused 163", 163, "Transparency 1.0"),
            new Stuff("unused 164", 164, "Transparency 1.0"),
            new Stuff("unused 165", 165, "Transparency 1.0"),
            new Stuff("unused 166", 166, "Transparency 1.0"),
            new Stuff("unused 167", 167, "Transparency 1.0"),
            new Stuff("unused 168", 168, "Transparency 1.0"),
            new Stuff("unused 169", 169, "Transparency 1.0"),
            new Stuff("unused 170", 170, "Transparency 1.0"),
            new Stuff("unused 171", 171, "Transparency 1.0"),
            new Stuff("unused 172", 172, "Transparency 1.0"),
            new Stuff("unused 173", 173, "Transparency 1.0"),
            new Stuff("unused 174", 174, "Transparency 1.0"),
            new Stuff("unused 175", 175, "Transparency 1.0"),
            new Stuff("unused 176", 176, "Transparency 1.0"),
            new Stuff("unused 177", 177, "Transparency 1.0"),
            new Stuff("unused 178", 178, "Transparency 1.0"),
            new Stuff("unused 179", 179, "Transparency 1.0"),
            new Stuff("unused 180", 180, "Transparency 1.0"),
            new Stuff("unused 181", 181, "Transparency 1.0"),
            new Stuff("unused 182", 182, "Transparency 1.0"),
            new Stuff("unused 183", 183, "Transparency 1.0"),
            new Stuff("unused 184", 184, "Transparency 1.0"),
            new Stuff("unused 185", 185, "Transparency 1.0"),
            new Stuff("unused 186", 186, "Transparency 1.0"),
            new Stuff("unused 187", 187, "Transparency 1.0"),
            new Stuff("unused 188", 188, "Transparency 1.0"),
            new Stuff("unused 189", 189, "Transparency 1.0"),
            new Stuff("unused 190", 190, "Transparency 1.0"),
            new Stuff("unused 191", 191, "Transparency 1.0"),
            
            new Stuff("unused 192", 192, "Transparency 1.0"),
            new Stuff("head to neck", 193, "Transparency 1.0"),
            new Stuff("unused 194", 194, "Transparency 1.0"),
            new Stuff("unused 195", 195, "Transparency 1.0"),
            new Stuff("unused 196", 196, "Transparency 1.0"),
            new Stuff("unused 197", 197, "Transparency 1.0"),
            new Stuff("unused 198", 198, "Transparency 1.0"),
            new Stuff("unused 199", 199, "Transparency 1.0"),
            new Stuff("unused 200", 200, "Transparency 1.0"),
            new Stuff("primary weapon front", 201, "Transparency 1.0"),
            new Stuff("primary weapon rear", 202, "Transparency 1.0"),
            new Stuff("unused 203", 203, "Transparency 1.0"),
            new Stuff("unused 204", 204, "Transparency 1.0"),
            new Stuff("unused 205", 205, "Transparency 1.0"),
            new Stuff("unused 206", 206, "Transparency 1.0"),
            new Stuff("unused 207", 207, "Transparency 1.0"),
            new Stuff("unused 208", 208, "Transparency 1.0"),
            new Stuff("secondary weapon front", 209, "Transparency 1.0"),
            new Stuff("secondary weapon rear", 210, "Transparency 1.0"),
            new Stuff("unused 211", 211, "Transparency 1.0"),
            new Stuff("unused 212", 212, "Transparency 1.0"),
            new Stuff("unused 213", 213, "Transparency 1.0"),
            new Stuff("unused 214", 214, "Transparency 1.0"),
            new Stuff("unused 215", 215, "Transparency 1.0"),
            new Stuff("unused 216", 216, "Transparency 1.0"),
            new Stuff("unused 217", 217, "Transparency 1.0"),
            new Stuff("unused 218", 218, "Transparency 1.0"),
            new Stuff("unused 219", 219, "Transparency 1.0"),
            new Stuff("unused 220", 220, "Transparency 1.0"),
            new Stuff("unused 221", 221, "Transparency 1.0"),
            new Stuff("unused 222", 222, "Transparency 1.0"),
            new Stuff("unused 223", 223, "Transparency 1.0"),
            new Stuff("unused 224", 224, "Transparency 1.0"),
            new Stuff("unused 225", 225, "Transparency 1.0"),
            new Stuff("unused 226", 226, "Transparency 1.0"),
            new Stuff("unused 227", 227, "Transparency 1.0"),
            new Stuff("unused 228", 228, "Transparency 1.0"),
            new Stuff("unused 229", 229, "Transparency 1.0"),
            new Stuff("unused 230", 230, "Transparency 1.0"),
            new Stuff("unused 231", 231, "Transparency 1.0"),
            new Stuff("unused 232", 232, "Transparency 1.0"),
            new Stuff("unused 233", 233, "Transparency 1.0"),
            new Stuff("unused 234", 234, "Transparency 1.0"),
            new Stuff("unused 235", 235, "Transparency 1.0"),
            new Stuff("unused 236", 236, "Transparency 1.0"),
            new Stuff("unused 237", 237, "Transparency 1.0"),
            new Stuff("unused 238", 238, "Transparency 1.0"),
            new Stuff("unused 239", 239, "Transparency 1.0"),
            new Stuff("unused 240", 240, "Transparency 1.0"),
            new Stuff("unused 241", 241, "Transparency 1.0"),
            new Stuff("unused 242", 242, "Transparency 1.0"),
            new Stuff("unused 243", 243, "Transparency 1.0"),
            new Stuff("unused 244", 244, "Transparency 1.0"),
            new Stuff("unused 245", 245, "Transparency 1.0"),
            new Stuff("unused 246", 246, "Transparency 1.0"),
            new Stuff("unused 247", 247, "Transparency 1.0"),
            new Stuff("unused 248", 248, "Transparency 1.0"),
            new Stuff("unused 249", 249, "Transparency 1.0"),
            new Stuff("unused 250", 250, "Transparency 1.0"),
            new Stuff("unused 251", 251, "Transparency 1.0"),
            new Stuff("unused 252", 252, "Transparency 1.0"),
            new Stuff("unused 253", 253, "Transparency 1.0"),
            new Stuff("unused 254", 254, "Transparency 1.0"),
            new Stuff("connector sentinel", 255, "Transparency 1.0"),
//            new Stuff("matte leaf", 14, 14, 0L, "Roughness 0.4 Reflection 0.05 Dapple -0.35"),
//            new Stuff("glossy leaf", 15, 15, 512L, "Roughness 0.1 Reflection 0.85 Dapple -0.1"),
//            new Stuff("succulent plant", 16, 16, 512L, "Roughness 0.8 Reflection 0.04"),
//            new Stuff("gas", 17, 17, 4L, "Roughness 0.65 Reflection 0.0 Flow 0.4 Rise 0.1"), /*light green*/
//            new Stuff("fine scales", 18, 18, 32L, "Roughness 0.15 Reflection 0.4"), /*light cyan*/
//            new Stuff("base scales", 19, 19, 4L, "Roughness 0.05 Reflection 0.55"),
//            new Stuff("accented scales", 20, 20, 32L, "Roughness 0.05 Reflection 0.65"), /*dark cyan*/
//            new Stuff("deep water", 21, 21, 4L, "Roughness 0.3 Reflection 0.5"), /*dark azure*/
//            new Stuff("slow water", 22, 22, 32L, "Roughness 0.1 Reflection 0.8 Flow 0.1"),
//            new Stuff("fast water", 23, 23, 24L, "Roughness 0.2 Reflection 0.75 Flow 0.7"),
//            new Stuff("wet stone", 24, 24, 0L, "Roughness 0.5 Reflection 0.2"),
//            new Stuff("ice", 25, 25, 512L, "Roughness 0.05 Reflection 0.95", 25, 8, 82, 1), /*light azure*/
//            new Stuff("shining mark", 26, 26, 32L, "Roughness 0.25 Reflection 0.9"), /*light blue*/
//            new Stuff("accented mark", 27, 27, 4L, "Roughness 0.4 Reflection 0.5"),
//            new Stuff("bold mark", 28, 28, 4L, "Roughness 0.4 Reflection 0.5"),
//            new Stuff("deep mark", 29, 29, -1L, "Roughness 0.4 Reflection 0.5"), /*dark blue*/
//            new Stuff("coal", 30, 30, -1L, "Roughness 0.1 Reflection 0.0"), /*dark violet*/
//            new Stuff("ore", 31, 31, -1L, "Roughness 0.7 Reflection 0.1 Vary -0.3 Dapple 0.1"),
//            new Stuff("raw stone", 32, 32, 32L, "Roughness 0.55 Reflection 0.15 Vary -0.1"),
//            new Stuff("worked stone", 33, 33, 32L, "Roughness 0.25 Reflection 0.2"),
//            new Stuff("chipped stone", 34, 34, 4L, "Roughness 0.8 Reflection 0.05"), /*light violet*/
//            new Stuff("magic crystal", 35, 35, 32L, "Roughness 0.05 Reflection 0.85 Emission 0.35", 35, 7, 98, 1), /*light purple*/
//            new Stuff("ectoplasm", 36, 36, 256L, "Roughness 0.2 Reflection 0.4 Flow 0.7 Emission 0.1"),
//            new Stuff("tentacle", 37, 37, 64L, "Roughness 0.5 Reflection 0.02 Dapple 0.2"),
//            new Stuff("apparition", 38, 38, 64L, "Roughness 0.6 Reflection 0.0 Flow 0.1 Emission -0.2"), /*dark purple*/
//            new Stuff("drink", 39, 39, -1L, "Roughness 0.3 Reflection 0.5"), /*burgundy*/
//            new Stuff("marked cloth", 40, 40, 64L, "Roughness 0.1 Reflection 0.04"), /*medium magenta*/
//            new Stuff("base cloth", 41, 41, -1L, "Roughness 0.25 Reflection 0.06"),
//            new Stuff("highlight cloth", 42, 42, 64L, "Roughness 0.15 Reflection 0.1"),
//            new Stuff("shiny cloth", 43, 43, -1L, "Roughness 0.4 Reflection 0.75"), /*light pink*/
//            new Stuff("sand", 44, 44, 64L, "Roughness 0.7 Reflection 0.0"), /*light tan*/
//            new Stuff("rubble", 45, 45, 64L, "Roughness 0.55 Reflection 0.0 Dapple -0.4 Vary -0.3"),
//            new Stuff("dry dirt", 46, 46, -1L, "Roughness 0.85 Reflection 0.0 Dapple -0.05"),
//            new Stuff("wet dirt or mud", 47, 47, 64L, "Roughness 0.1 Reflection 0.05"), /*dark tan*/
//            new Stuff("marked fluff", 48, 48, 64L, "Roughness 0.6 Reflection 0.15 Dapple -0.16"), /*dark orange*/
//            new Stuff("base fluff", 49, 49, -1L, "Roughness 0.5 Reflection 0.4 Dapple -0.08"),
//            new Stuff("highlight fluff", 50, 50, -1L, "Roughness 0.4 Reflection 0.75 Dapple -0.04"), /*hot orange*/
//            new Stuff("scar", 51, 51, 64L, "Roughness 0.7 Reflection 0.05"), /*light skin*/
//            new Stuff("base skin", 52, 52, -1L, "Roughness 0.6 Reflection 0.2"),
//            new Stuff("nose", 53, 53, 1L, "Roughness 0.6 Reflection 0.3"),
//            new Stuff("freckle", 54, 54, 1L, "Roughness 0.6 Reflection 0.2 Vary -0.1"),
//            new Stuff("ears", 55, 55, 4L, "Roughness 0.5 Reflection 0.1"),
//            new Stuff("overripe fruit or rot", 56, 56, -1L, "Roughness 0.2 Reflection 0.02 Dapple -0.15 Vary -0.1"), /*dark skin*/
//            new Stuff("lips blood gore", 57, 57, 16L, "Roughness 0.04 Reflection 0.4"), /*gore*/
//            new Stuff("knotted wood", 58, 58, 128L, "Roughness 0.5 Reflection 0.1 Dapple -0.1"), /*dark brown*/
//            new Stuff("old wood", 59, 59, 2L, "Roughness 0.6 Reflection 0.0 Dapple -0.25 Vary -0.35"),
//            new Stuff("fresh wood", 60, 60, 512L, "Roughness 0.4 Reflection 0.0 Dapple -0.15 Vary -0.1"), /*medium brown*/
//            new Stuff("gold", 61, 61, 24L, "Roughness 0.5 Reflection 0.85", 126, 1, 5, 61), /*dark yellow*/
//            new Stuff("ripe fruit or flower", 62, 62, 128L, "Roughness 0.2 Reflection 0.55 Dapple -0.2"),
//            new Stuff("bone beak claw", 63, 63, 64L, "Roughness 0.4 Reflection 0.4"), /*light yellow*/
    };

    public static final IntObjectMap<VoxMaterial> MATERIALS_A = new IntObjectMap<>(256);
    public static final IntObjectMap<VoxMaterial> MATERIALS_B = new IntObjectMap<>(256);
    static {
        for (int i = 0; i < STUFFS.length; i++) {
            MATERIALS_A.put(i, STUFFS[i].material);
        }
        for (int i = 0; i < STUFFS.length; i++) {
            MATERIALS_B.put(i, STUFFS_B[i].material);
        }
    }

    public static void evolve(byte[][][] model, int frame){
        evolve(STUFFS, model, frame);
    }

    public static void evolve(Stuff[] stuffs, byte[][][] model, int frame){
        for (int x = 0; x < model.length; x++) {
            for (int y = 0; y < model[x].length; y++) {
                for (int z = 0; z < model[x][y].length; z++) {
                    int v = model[x][y][z] & 255;
                    float rate = stuffs[v].material.traits.getOrDefault(VoxMaterial.MaterialTrait._rate.ordinal(), 1f);
                    int rf = (int)(rate * frame);
                    if(rf != (int)(rate * (frame + 1)))
                        model[x][y][z] = stuffs[v].transitionIDs[stuffs[v].transitions.random(HastyPointHash.hash64(x, y, z, rf))];
                }
            }
        }
    }
}
