package com.cmat.lasertech.laser;

public enum FireMode implements IFireMode {
    FIRE("FIRE"),
    SAFE("SAFE"),
    HORIZONTAL("HORIZONTAL");

    String modeName;

    FireMode(String name) {
        this.modeName = name;
    }

    public String getName() {
        return modeName;
    }

    public IFireMode getNextMode(IFireMode mode) {
        FireMode[] modes = FireMode.values();
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
