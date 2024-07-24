package isonomicon.app;

import com.github.tommyettinger.ds.HolderOrderedSet;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.ds.ObjectOrderedSet;

import java.util.List;

public class ColorGuardData {
    public static class Unit {
        public String name;
        public String primary;
        public String secondary;
        public boolean primaryPose;
        public boolean secondaryPose;
        public int primaryStrength;
        public int secondaryStrength;
        public Unit(String name){
            this.name = name;
        }
        public Unit(String name, String primary){
            this.name = name;
            this.primary = primary;
        }
        public Unit(String name, String primary, boolean primaryPose){
            this.name = name;
            this.primary = primary;
            this.primaryPose = primaryPose;
        }
        public Unit(String name, String primary, boolean primaryPose, int primaryStrength){
            this.name = name;
            this.primary = primary;
            this.primaryPose = primaryPose;
            this.primaryStrength = primaryStrength;
        }
        public Unit(String name, String primary, String secondary){
            this.name = name;
            this.primary = primary;
            this.secondary = secondary;
        }
        public Unit(String name, String primary, boolean primaryPose, String secondary, boolean secondaryPose){
            this.name = name;
            this.primary = primary;
            this.primaryPose = primaryPose;
            this.secondary = secondary;
            this.secondaryPose = secondaryPose;
        }

        public Unit(String name, String primary, boolean primaryPose, int primaryStrength, String secondary, boolean secondaryPose, int secondaryStrength){
            this.name = name;
            this.primary = primary;
            this.primaryPose = primaryPose;
            this.primaryStrength = primaryStrength;
            this.secondary = secondary;
            this.secondaryPose = secondaryPose;
            this.secondaryStrength = secondaryStrength;
        }

        public boolean hasWeapon(String type){
            return type != null && (type.equals(primary) || type.equals(secondary));
        }

        public String getName() {
            return name;
        }
    }

    public static List<Unit> units = ObjectList.with(
            new Unit("Infantry", "Machine_Gun", true, 1),
            new Unit("Bazooka", "Handgun", false, 1, "Forward_Missile", true, 3),
            new Unit("Bike", "Machine_Gun", false, 2),
            new Unit("Rifle_Sniper", "Handgun", false, 1, "Handgun", true, 2),
            new Unit("Mortar_Sniper", "Arc_Cannon", true, 1),
            new Unit("Missile_Sniper", "Arc_Missile", false, 1),
            new Unit("Light_Tank", "Forward_Cannon", false, 1, "Machine_Gun", false, 1),
            new Unit("War_Tank", "Forward_Cannon", false, 2, "Machine_Gun", false, 1),
            new Unit("Scout_Tank", "Forward_Cannon", false, 1, "Handgun", false, 2),
            new Unit("Heavy_Cannon", "Forward_Cannon", false, 2),
            new Unit("Recon", "Machine_Gun", false, 1),
            new Unit("AA_Gun", "Machine_Gun", false, 2),
            new Unit("Flamethrower", "Flame_Wave", false, 1),
            new Unit("Light_Artillery", "Arc_Cannon", false, 1),
            new Unit("Rocket_Artillery", "Arc_Missile", false, 8),
            new Unit("AA_Artillery", "Arc_Missile", false, 1),
            new Unit("Supply_Truck"),
            new Unit("Amphi_Transport"),
            new Unit("Build_Rig"),
            new Unit("Jammer", "Hack", false, 2),
            new Unit("Comm_Copter", "Hack", false, 2),
            new Unit("Jetpack", "Machine_Gun", false, 2),
            new Unit("Transport_Copter"),
            new Unit("Blitz_Copter", "Machine_Gun", false, 2),
            new Unit("Gunship_Copter", "Machine_Gun", false, 2, "Forward_Missile", false, 2),
            new Unit("Patrol_Boat", "Machine_Gun", false, 2),
            new Unit("Battleship", "Arc_Cannon", false, 4),
            new Unit("Cruiser", "Arc_Missile", false, 4, "Torpedo", false, 1),
            new Unit("Submarine", "Arc_Missile", false, 1, "Torpedo", false, 2),
            new Unit("Legacy_Plane", "Machine_Gun", false, 2),
            new Unit("Fighter_Jet", "Forward_Missile", false, 1),
            new Unit("Stealth_Jet", "Forward_Missile", false, 2),
            new Unit("Heavy_Bomber", "Bomb_Drop", false, 3),
            new Unit("Volunteer"),
            new Unit("Medic"),
            new Unit("Spy"),
            new Unit("Engineer"),
            new Unit("City"),
            new Unit("Mansion"),
            new Unit("Fort"),
            new Unit("Factory"),
            new Unit("Airport"),
            new Unit("Dock"),
            new Unit("Farm"),
            new Unit("Mining_Outpost"),
            new Unit("Oil_Well"),
            new Unit("Laboratory"),
            new Unit("Hospital"),

            new Unit("Coast"), new Unit("Desert"), new Unit("Forest"), new Unit("Ice"),
            new Unit("Jungle"), new Unit("Mountains"), new Unit("Ocean"), new Unit("Plains"),
            new Unit("River"), new Unit("Rocky"), new Unit("Ruins"), new Unit("Volcano"),

//            new Unit("Terrain"),
//            new Unit("Terrain_Small"),
            new Unit("Road_Straight"),
            new Unit("Road_Center"),
            new Unit("Valor"),
            new Unit("Oil"),
            new Unit("Metal"),
            new Unit("Food"),
            new Unit("Signal"),
            new Unit("Money"),
            new Unit("Fire"),
            new Unit("Poison")
            );

    public static HolderOrderedSet<Unit, String> byName = new HolderOrderedSet<>(Unit::getName, units);

    public static ObjectOrderedSet<String> terrains = ObjectOrderedSet.with("Coast", "Desert", "Forest", "Ice", "Jungle", "Mountains",
            "Ocean", "Plains", "River", "Rocky", "Ruins", "Volcano");
}
