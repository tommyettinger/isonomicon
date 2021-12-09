package isonomicon.app;

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
    }
    public static final String[] allVoxModels = {
            "Light_Tank.vox",
            "War_Tank.vox",
            "Scout_Tank.vox",
            "Heavy_Cannon.vox",
            "AA_Gun.vox",
            "Flamethrower.vox",
            "Light_Artillery.vox",
            "Rocket_Artillery.vox",
            "AA_Artillery.vox",
            "Recon.vox",
            "Supply_Truck.vox",
            "Amphi_Transport.vox",
            "Build_Rig.vox",
            "Jammer.vox",
            "Jetpack.vox",
            "Transport_Copter.vox",
            "Blitz_Copter.vox",
            "Gunship_Copter.vox",
            "Comm_Copter.vox",
            "Patrol_Boat.vox",
            "Battleship.vox",
            "Submarine.vox",
            "Cruiser.vox",
            "Fighter_Jet.vox",
            "Stealth_Jet.vox",
            "Legacy_Plane.vox",
            "Heavy_Bomber.vox",
            "Infantry.vox",
            "Infantry_Firing.vox",
            "Bazooka.vox",
            "Bazooka_Firing.vox",
            "Bike.vox",
            "Rifle_Sniper.vox",
            "Rifle_Sniper_Firing.vox",
            "Mortar_Sniper.vox",
            "Mortar_Sniper_Firing.vox",
            "Missile_Sniper.vox",
            "City.vox",
            "Mansion.vox",
            "Fort.vox",
            "Factory.vox",
            "Airport.vox",
            "Farm.vox",
            "Mining_Outpost.vox",
            "Oil_Well.vox",
            "Laboratory.vox",
            "Hospital.vox",
    };
    public static Unit[] units = {
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
            new Unit("City.vox"),
            new Unit("Mansion.vox"),
            new Unit("Fort.vox"),
            new Unit("Factory.vox"),
            new Unit("Airport.vox"),
            new Unit("Farm.vox"),
            new Unit("Mining_Outpost.vox"),
            new Unit("Oil_Well.vox"),
            new Unit("Laboratory.vox"),
            new Unit("Hospital.vox"),
    };
}
