package com.cmat.lasertech.laser;

public enum AdvancedCombatLaserMode implements ILaserMode {
    COMBAT("COMBAT", 5000, 10, 8, 0.6f, 4.5f, 16, 4.0f, 2),
    SPEED("SPEED", 2000, 2, 5, 0.1f, 0.6f, 4, 3.0f, 1),
    SNIPER("SNIPER",  10000, 40, 25, 3.0f, 4.5f, 40, 6.0f, 4),
    SCATTER( "SCATTER", 15000, 40, 5, 0.1f, 1.5f, 4, 4.0f, 1),
    KABOOM("KABOOM", 5000, 60, 30, 4.5f, 100f, 10, 2.5f, 1),
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

    AdvancedCombatLaserMode(String name, int energycost, int cooldown, int life, float breakpower, float minbreak, int dmg, float vel, int piercepower) {
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
        return MiningLaserMode.valueOf(name);
    }

    public ILaserMode getNextMode(ILaserMode mode) {
        AdvancedCombatLaserMode[] modes = AdvancedCombatLaserMode.values();
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
