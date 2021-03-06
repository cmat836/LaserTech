package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.laser.*;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MiningLaserBasicItem extends BaseLaserItem {

    public MiningLaserBasicItem() {
        super(100);
    }

    @Override
    public void fireLaser(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        MiningLaserModeBasic m = (MiningLaserModeBasic)getLaserMode(stack);

        BaseLaserProjectile laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
        Vector3d newPos = laser.getPositionVec();//.add(playerIn.getLookVec().normalize().scale(0.2));
        laser.setPosition(newPos.x, newPos.y, newPos.z);
        laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage());
        laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, m.getVelocity(), 0.0f);

        playerIn.getCooldownTracker().setCooldown(this, m.getCoolDown());

        if (!worldIn.isRemote) {
            if (getFireMode(stack) != FireMode.SAFE) {
                worldIn.addEntity(laser);
            }
        }
    }

    public ILaserMode getLaserMode(ItemStack stack) {
        MiningLaserModeBasic m;
        try {
            m = MiningLaserModeBasic.valueOf(ItemDataDealer.getString(stack, "mode"));
        } catch (Exception e) {
            m = MiningLaserModeBasic.LOW_FOCUS;
            setLaserMode(stack, m);
        }
        return m;
    }

    public IFireMode getFireMode(ItemStack stack) {
        FireModeBasic m;
        try {
            m = FireModeBasic.valueOf(ItemDataDealer.getString(stack, "firemode"));
        } catch (Exception e) {
            m = FireModeBasic.SAFE;
            setFireMode(stack, m);
        }
        return m;
    }
}
