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
		//0                      1              2                  3              4            5
		_alpha("Transparency"), _d("Density"), _emit("Emission"), _flux("Flux"), _g("Phase"), _ior("Reflection"),
		//6           7                  8                   9
		_ldr("LDR"), _media("Special"), _metal("Metallic"), _rough("Roughness");
		
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
		traits.put(trait.ordinal(), value);
	}
	public void putTrait(String trait, float value){
		traits.put(MaterialTrait.valueOf(trait).ordinal(), value);
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
