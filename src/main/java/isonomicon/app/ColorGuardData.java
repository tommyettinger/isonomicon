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
            new Unit("Bazooka", "Handgun", false, "Forward_Rocket", true),
            new Unit("Bike", "Machine_Gun"),
            new Unit("Rifle_Sniper", "Handgun", false, "Handgun", true),
            new Unit("Mortar_Sniper", "Arc_Cannon", true),
            new Unit("Missile_Sniper", "Arc_Missile"),
            new Unit("Light_Tank", "Forward_Cannon", "Machine_Gun"),
            new Unit("War_Tank", "Forward_Cannon", "Machine_Gun"),
            new Unit("Heavy_Cannon", "Forward_Cannon"),
    };
}
