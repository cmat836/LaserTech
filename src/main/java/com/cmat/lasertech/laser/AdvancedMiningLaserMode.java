package com.cmat.lasertech.laser;

public enum AdvancedMiningLaserMode implements ILaserMode {
    LOW_FOCUS("LOW_FOCUS", 200, 1, 3, 0.6f, 4.5f, 4, 2.5f, 1),
    STANDARD( "STANDARD", 2500, 5, 6, 10f, 30f, 8, 3f, 1),
    LONG_RANGE("LONG_RANGE",  10000, 8, 25, 50f, 50f, 14, 3f, 2),
    HIGH_FOCUS("HIGH_FOCUS", 5000, 20, 2, 4.5f, 100f, 10, 2.5f, 5),
    THREEBYTHREE("THREEBYTHREE", 15000, 12, 5, 8f, 5f, 8, 2.5f, 1),
    EXPLOSIVE("EXPLOSIVE", 10000, 25, 25, 1.5f, 4.5f, 8, 2f, 1);

    String modeName;
    int energyCost;
    int coolDown;
    int lifeTime;
    float breakPower;
    float minimumBreak;
    int damage;
    float velocity;
    int piercePower;

    AdvancedMiningLaserMode(String name, int energycost, int cooldown, int life, float breakpower, float minbreak, int dmg, float vel, int piercepower) {
        modeName = name;
        energyCost = energycost;
        coolDown = cooldown;
        lifeTime = life;
        breakPower = breakpower;
        minimumBreak = minbreak;
        damage = dmg;
        velocity = vel;
        piercePower = piercepower;
    }

    public int getCoolDown() {
        return coolDown;
    }
    public int getEnergyCost() { return energyCost; }
    public float getBreakPower() { return breakPower; }
    public float getMinimumBreak() { return minimumBreak; }
    public int getLifeTime() { return lifeTime; }
    public float getVelocity() { return velocity; }
    public int getDamage() { return damage; }
    public String getName() {
        return modeName;
    }
    public int getPiercePower() { return piercePower; }

    public ILaserMode getFromString(String name) {
        return AdvancedMiningLaserMode.valueOf(name);
    }

    public ILaserMode getNextMode(ILaserMode mode) {
        AdvancedMiningLaserMode[] modes = AdvancedMiningLaserMode.values();
        for (int i = 0; i < modes.length; i++) {
            if (modes[i] == mode) {
                if (i == (modes.length - 1)) {
                    return modes[0];
                } else {
                    return modes[i + 1];
                }
            }
        }
        return mode;
    }
}
