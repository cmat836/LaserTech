package com.cmat.lasertech.laser;

public interface IFireMode {

    public String getName();
    public IFireMode getNextMode(IFireMode mode);
}
