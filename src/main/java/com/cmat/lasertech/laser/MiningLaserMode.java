package com.cmat.lasertech.laser;

public enum MiningLaserMode implements ILaserMode {
    LOW_FOCUS("LOW_FOCUS", 200, 2),
    STANDARD( "STANDARD", 2500, 8),
    LONG_RANGE("LONG_RANGE",  10000, 12),
    COMBAT("COMBAT", 5000, 20);

    String modeName;
    int energyCost;
    int coolDown;

    MiningLaserMode(String name, int energycost, int cooldown) {
        modeName = name;
        energyCost = energycost;
        coolDown = cooldown;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    public String getName() {
        return modeName;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
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
