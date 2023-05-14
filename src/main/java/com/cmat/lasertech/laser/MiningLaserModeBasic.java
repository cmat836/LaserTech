package com.cmat.lasertech.laser;

public enum MiningLaserModeBasic implements ILaserMode {
    LOW_FOCUS("LOW_FOCUS", 200, 2, 3, 0.6f, 3f, 2, 2f, 1),
    STANDARD( "STANDARD", 2500, 8, 5, 6f, 4.5f, 6, 2f, 1),
    LONG_RANGE("LONG_RANGE",  10000, 12, 20, 30f, 5f, 8, 2f, 1),
    COMBAT("COMBAT", 5000, 20, 10, 1.5f, 1.5f, 12, 4f, 1);

    String modeName;
    int energyCost;
    int coolDown;
    int lifeTime;
    float breakPower;
    float minimumBreak;
    int damage;
    float velocity;
    int piercePower;

    MiningLaserModeBasic(String name, int energycost, int cooldown, int life, float breakpower, float minbreak, int dmg, float vel, int piercepower) {
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
        return MiningLaserModeBasic.valueOf(name);
    }

    public ILaserMode getNextMode(ILaserMode mode) {
        MiningLaserModeBasic[] modes = MiningLaserModeBasic.values();
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
