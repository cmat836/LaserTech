package com.cmat.lasertech.laser;

public interface ILaserMode {
    public int getCoolDown();

    public String getName();

    public void setEnergyCost(int energyCost);

    public ILaserMode getNextMode(ILaserMode mode);
}
