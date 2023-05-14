package com.cmat.lasertech.laser;

public enum FireModeCombat implements IFireMode {
    FIRE("FIRE"),
    SAFE("SAFE"),
    BURST("BURST"),
    SHOTGUN("SHOTGUN");

    String modeName;

    FireModeCombat(String name) {
        this.modeName = name;
    }

    public String getName() {
        return modeName;
    }

    public IFireMode getNextMode(IFireMode mode) {
        FireModeCombat[] modes = FireModeCombat.values();
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
