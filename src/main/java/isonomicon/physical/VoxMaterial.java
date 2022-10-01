package isonomicon.physical;

import com.github.tommyettinger.ds.IntFloatMap;
import com.github.tommyettinger.ds.ObjectIntMap;

/**
 * Represents the physical qualities of a given material, like how reflective it is or how much light it emits.
 * Each material is associated with one palette index.
 * <br>
 * Created by Tommy Ettinger on 8/17/2020.
 */
public class VoxMaterial {
	public enum MaterialType {
		//0
		_diffuse("Diffuse"),
		//1
		_metal("Metal"),
		//2
		_emit("Emit"),
		//3
		_glass("Glass"),
		//4
		_blend("Blend"),
		//5
		_media("Cloud");
		public String name;
		MaterialType(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	public enum MaterialTrait {
		//0 lower is more opaque, higher is more transparent
		_alpha("Transparency"),
		//1 currently unused
		_d("Density"),
		//2 brightness of light emitted
		_emit("Emission"),
		//3 for emissive materials, this is called "strength," here it may get used for lighting range?
		_flux("Flux"),
		//4 affects scatter media? unused
		_g("Phase"),
		//5 affects extra lightness applied when both the front and top are lit
		_ior("Reflection"),
		//6 when higher, adds a chance the voxel will not be rendered
		_ldr("LDR"),
		//7 not related to the "Cloud" _media MaterialType; unused
		_media("Special"),
		//8 when higher, adds a chance the voxel will not be rendered
		_metal("Missing"),
		//9 determines how much lighting affects the color of a surface
		_rough("Roughness"),
		//10 used with FastNoise; affects stretching of a voxel over time and space
		_flow("Flow"),
		//11 affects vertical movement of a voxel over time; may be negative
		_rise("Rise"),
		//12 causes random per-voxel lightening (or darkening, if negative)
		_dapple("Dapple"),
		//13 causes random per-voxel increases to saturation (or decreases, if negative).
		_vary("Vary"),
		//14 can be 0.0 or 1.0, and causes this material to not be rendered on even or odd frames, respectively.
		_frame("Frame"),
		//15 Typically 0.5 or 0.25; multiplied with the frame rate when determining transitions
		_rate("Rate"),
		//16 Parsed as a float but cast to a byte, referring to an index in the Stuff table that this changes to when an effect damages it.
		_damage("Damage"),
		//17 Used to indicate how much the voxel moves randomly outward, as well as downward (if negative) or upward (if positive)
		_disperse("Disperse"),
		//18 used in newer MV instead of a separate MaterialType
		_type("Type")
		;
		
		public String name;
		MaterialTrait(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final MaterialType[] ALL_TYPES = MaterialType.values();
	public static final MaterialTrait[] ALL_TRAITS = MaterialTrait.values();

	public static final ObjectIntMap<String> TYPE_MAP = new ObjectIntMap<>(6), TRAIT_MAP = new ObjectIntMap<>(10);

	static {
		for(MaterialType t : ALL_TYPES) {
			TYPE_MAP.put(t.name, t.ordinal());
		}
		for(MaterialTrait t : ALL_TRAITS) {
			TRAIT_MAP.put(t.name, t.ordinal());
		}
	}
	public MaterialType type;
	public final IntFloatMap traits = new IntFloatMap(16);
	
	public VoxMaterial(){
		type = MaterialType._diffuse;
		traits.put(9, 0.1f);
		traits.put(5, 0.3f);
		traits.put(4, 0.41f);
		traits.put(14, 0.5f);
	}
	public VoxMaterial(String typeCode){
		type = MaterialType.valueOf(typeCode);
		traits.put(9, 0.1f);
		traits.put(5, 0.3f);
		traits.put(4, 0.41f);
		traits.put(14, 0.5f);
		if(type == MaterialType._media)
			traits.put(0, 0.6f); // cloud materials are always partly transparent
	}

	public VoxMaterial(String typeName, String traitMap) {
		type = ALL_TYPES[TYPE_MAP.getOrDefault(typeName, 0)];
		traits.put(9, 0.1f);
		traits.put(5, 0.3f);
		traits.put(4, 0.41f);
		traits.put(14, 0.5f);
		String[] split = traitMap.split("[ ,;]+");
		for (int i = 1; i < (split.length & -2); i+=2) {
			traits.put(TRAIT_MAP.getOrDefault(split[i-1], -1), Float.parseFloat(split[i]));
		}
	}

	public float getTrait(MaterialTrait trait){
		return traits.getOrDefault(trait.ordinal(), 0.0f);
	}
	public void putTrait(MaterialTrait trait, float value){
		int ord = trait.ordinal();
		if(ord == 6) traits.put(8, value);
		traits.put(ord, value);
	}
	public void putTrait(String trait, String value){
		try {
			int ord = MaterialTrait.valueOf(trait).ordinal();
			if ("_type".equals(trait)) {
				int t = MaterialType.valueOf(value).ordinal();
				traits.put(ord, t);
				type = ALL_TYPES[t];
			} else {
				float f = Float.parseFloat(value);
				traits.put(ord, f);
				if(ord == 6) traits.put(8, f);
			}
		}catch (IllegalArgumentException ignored){
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("VoxMaterial{").append(type.name).append(": ");
		for(IntFloatMap.Entry e : traits.entrySet())
			sb.append(ALL_TRAITS[e.key]).append('=').append(e.value).append(", ");
		sb.setLength(sb.length() - 1);
		sb.setCharAt(sb.length() - 1, '}');
		return sb.toString();
	}

	public static final VoxMaterial DEFAULT_MATERIAL = new VoxMaterial();
}
