package com.cmat.lasertech.laser;

public interface ILaserMode {
    /**
     * Cooldown after the laser is fired
     * @return
     */
    int getCoolDown();
    int getEnergyCost();

    /**
     * How many ticks the laser lasts for
     * @return
     */
    int getLifeTime();

    /**
     * Total material hardness the laser can penetrate
     * @return
     */
    float getBreakPower();

    /**
     * The highest hardness the laser can break
     * will also break blocks of this hardness even if insufficient break power remains
     * @return
     */
    float getMinimumBreak();
    int getDamage();
    float getVelocity();
    int getPiercePower();


    String getName();

    ILaserMode getFromString(String name);
    ILaserMode getNextMode(ILaserMode mode);
}
