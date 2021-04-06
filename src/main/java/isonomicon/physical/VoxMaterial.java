package isonomicon.physical;

import com.badlogic.gdx.utils.IntFloatMap;

/**
 * Represents the physical qualities of a given material, like how reflective it is or how much light it emits.
 * Each material is associated with one palette index.
 * <br>
 * Created by Tommy Ettinger on 8/17/2020.
 */
public class VoxMaterial {
	public enum MaterialType {
		//0                   1                2              3                4                5
		_diffuse("Diffuse"), _metal("Metal"), _emit("Emit"), _glass("Glass"), _blend("Blend"), _media("Cloud");
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
		//3 for emissive materials, this is called "strength," here it may be used for transitions.
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
		_metal("Metallic"),
		//9 determines how much lighting affects the color of a surface
		_rough("Roughness");
		
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

	public final MaterialType type;
	public final IntFloatMap traits = new IntFloatMap(16);
	
	public VoxMaterial(){
		type = MaterialType._diffuse;
		traits.put(9, 0.1f);
		traits.put(5, 0.3f);
		traits.put(4, 0.41f);
	}
	public VoxMaterial(String typeName){
		type = MaterialType.valueOf(typeName);
		traits.put(9, 0.1f);
		traits.put(5, 0.3f);
		traits.put(4, 0.41f);
		if(type == MaterialType._media)
			traits.put(0, 0.6f); // cloud materials are always partly transparent
	}

	public float getTrait(MaterialTrait trait){
		return traits.get(trait.ordinal(), 0.0f);
	}
	public void putTrait(MaterialTrait trait, float value){
		int ord = trait.ordinal();
		if(ord == 6) traits.put(8, value);
		traits.put(ord, value);
	}
	public void putTrait(String trait, float value){
		int ord = MaterialTrait.valueOf(trait).ordinal();
		if(ord == 6) traits.put(8, value);
		traits.put(ord, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("VoxMaterial{").append(type.name).append(": ");
		for(IntFloatMap.Entry e : traits.entries())
			sb.append(ALL_TRAITS[e.key]).append('=').append(e.value).append(", ");
		sb.setLength(sb.length() - 1);
		sb.setCharAt(sb.length() - 1, '}');
		return sb.toString();
	}
}
