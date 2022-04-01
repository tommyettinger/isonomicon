package isonomicon.app;

import com.github.tommyettinger.ds.ObjectList;

import java.util.List;

public class ColorGuardData {
    public static class Unit {
        public String name;
        public String primary;
        public String secondary;
        public boolean primaryPose;
        public boolean secondaryPose;
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

        public boolean hasWeapon(String type){
            return type != null && (type.equals(primary) || type.equals(secondary));
        }
    }

    public static List<Unit> units = ObjectList.with(
            new Unit("Infantry", "Machine_Gun", true),
            new Unit("Bazooka", "Handgun", false, "Forward_Missile", true),
            new Unit("Bike", "Machine_Gun"),
            new Unit("Rifle_Sniper", "Handgun", false, "Handgun", true),
            new Unit("Mortar_Sniper", "Arc_Cannon", true),
            new Unit("Missile_Sniper", "Arc_Missile"),
            new Unit("Light_Tank", "Forward_Cannon", "Machine_Gun"),
            new Unit("War_Tank", "Forward_Cannon", "Machine_Gun"),
            new Unit("Scout_Tank", "Forward_Cannon", "Handgun"),
            new Unit("Heavy_Cannon", "Forward_Cannon"),
            new Unit("Recon", "Machine_Gun"),
            new Unit("AA_Gun", "Machine_Gun"),
            new Unit("Flamethrower", "Flame_Wave"),
            new Unit("Light_Artillery", "Arc_Cannon"),
            new Unit("Rocket_Artillery", "Arc_Missile"),
            new Unit("AA_Artillery", "Arc_Missile"),
            new Unit("Supply_Truck"),
            new Unit("Amphi_Transport"),
            new Unit("Build_Rig"),
            new Unit("Jammer", "Hack"),
            new Unit("Comm_Copter", "Hack"),
            new Unit("Jetpack", "Machine_Gun"),
            new Unit("Transport_Copter"),
            new Unit("Blitz_Copter", "Machine_Gun"),
            new Unit("Gunship_Copter", "Machine_Gun", "Forward_Missile"),
            new Unit("Patrol_Boat", "Machine_Gun"),
            new Unit("Battleship", "Arc_Cannon"),
            new Unit("Cruiser", "Arc_Missile", "Torpedo"),
            new Unit("Submarine", "Arc_Missile", "Torpedo"),
            new Unit("Legacy_Plane", "Machine_Gun"),
            new Unit("Fighter_Jet", "Forward_Missile"),
            new Unit("Stealth_Jet", "Forward_Missile"),
            new Unit("Heavy_Bomber", "Bomb_Drop"),
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
            new Unit("Terrain"),
            new Unit("Road_Straight"),
            new Unit("Road_Center")
            );

    public static List<String> terrains = ObjectList.with("Coast", "Desert", "Forest", "Ice", "Jungle", "Mountains",
            "Ocean", "Plains", "River", "Rocky", "Ruins", "Volcano");
}
