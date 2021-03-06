package com.cmat.lasertech.laser;

public enum MiningLaserMode implements ILaserMode {
    LOW_FOCUS("LOW_FOCUS", 200, 2, 3, 0.6f, 4.5f, 4, 2.5f),
    STANDARD( "STANDARD", 2500, 6, 5, 8f, 5f, 8, 2.5f),
    LONG_RANGE("LONG_RANGE",  10000, 10, 20, 40f, 22.5f, 10, 2.5f),
    COMBAT("COMBAT", 5000, 16, 10, 1.5f, 1.5f, 16, 4.5f),
    HIGH_FOCUS("HIGH_FOCUS", 5000, 30, 3, 4.5f, 100f, 8, 2f),
    EXPLOSIVE("EXPLOSIVE", 10000, 25, 25, 1.5f, 4.5f, 8, 2f);

    String modeName;
    int energyCost;
    int coolDown;
    int lifeTime;
    float breakPower;
    float minimumBreak;
    int damage;
    float velocity;

    MiningLaserMode(String name, int energycost, int cooldown, int life, float breakpower, float minbreak, int dmg, float vel) {
        modeName = name;
        energyCost = energycost;
        coolDown = cooldown;
        lifeTime = life;
        breakPower = breakpower;
        minimumBreak = minbreak;
        damage = dmg;
        velocity = vel;
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

    public ILaserMode getFromString(String name) {
        return MiningLaserMode.valueOf(name);
    }

    public ILaserMode getNextMode(ILaserMode mode) {
        MiningLaserMode[] modes = MiningLaserMode.values();
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
