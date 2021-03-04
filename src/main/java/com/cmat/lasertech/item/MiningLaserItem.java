package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.laser.BaseLaserItem;
import com.cmat.lasertech.laser.ILaserMode;
import com.cmat.lasertech.laser.MiningLaserMode;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MiningLaserItem extends BaseLaserItem {

    public MiningLaserItem() {
        super(100);
    }

    @Override
    public void fireLaser(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        MiningLaserMode m = (MiningLaserMode)getLaserMode(stack);

        BaseLaserProjectile laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
        Vector3d newPos = laser.getPositionVec().add(playerIn.getLookVec().normalize());
        laser.setPosition(newPos.x, newPos.y, newPos.z);
        //laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 4f, 0.0f);

        switch (m) {
            case LOW_FOCUS:
                laser.setParams(8, 2, false, 2);
                laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 2f, 0.0f);
                break;
            case STANDARD:
                laser.setParams(20, 10, false, 6);
                laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 2f, 0.0f);
                break;
            case COMBAT:
                laser.setParams(30, 1, false, 10);
                laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 2f, 0.0f);
                break;
            case LONG_RANGE:
                laser.setParams(40, 20, false, 10);
                laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 2f, 0.0f);
                break;
        }

        playerIn.getCooldownTracker().setCooldown(this, m.getCoolDown());

        if (!worldIn.isRemote) {

            worldIn.addEntity(laser);


        }
    }

    @Override
    public ILaserMode getLaserMode(ItemStack stack) {
        MiningLaserMode m;
        try {
            m = MiningLaserMode.valueOf(ItemDataDealer.getString(stack, "mode"));
        } catch (Exception e) {
            m = MiningLaserMode.LOW_FOCUS;
            setLaserMode(stack, m);
        }
        return m;
    }

    @Override
    public void setLaserMode(ItemStack stack, ILaserMode mode) {
        ItemDataDealer.setString(stack, "mode", mode.getName());
    }

    @Override
    public void incrementLaserMode(ItemStack stack) {
        ILaserMode m = getLaserMode(stack);
        setLaserMode(stack, m.getNextMode(m));
    }
}
