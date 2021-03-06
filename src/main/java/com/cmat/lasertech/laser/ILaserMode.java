package com.cmat.lasertech.laser;

public interface ILaserMode {
    /**
     * Cooldown after the laser is fired
     * @return
     */
    public int getCoolDown();
    public int getEnergyCost();

    /**
     * How many ticks the laser lasts for
     * @return
     */
    public int getLifeTime();

    /**
     * Total material hardness the laser can penetrate
     * @return
     */
    public float getBreakPower();

    /**
     * The highest hardness the laser can break
     * will also break blocks of this hardness even if insufficient break power remains
     * @return
     */
    public float getMinimumBreak();
    public int getDamage();
    public float getVelocity();

    public String getName();

    public ILaserMode getFromString(String name);
    public ILaserMode getNextMode(ILaserMode mode);
}
