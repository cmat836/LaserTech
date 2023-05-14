package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.laser.*;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MiningLaserBasicItem extends BaseLaserItem {

    public MiningLaserBasicItem() {
        super(100);
    }

    @Override
    public void fireLaser(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        MiningLaserModeBasic m = (MiningLaserModeBasic)getLaserMode(stack);

        BaseLaserProjectile laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
        Vec3 newPos = laser.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
        laser.setPos(newPos.x, newPos.y, newPos.z);
        laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 0, m.getPiercePower());
        laser.fireLaser(playerIn, playerIn.getXRot(), playerIn.getYRot(),0.0f, m.getVelocity(), 0.0f);

        playerIn.getCooldowns().addCooldown(this, m.getCoolDown());

        if (!worldIn.isClientSide) {
            if (getFireMode(stack) != FireMode.SAFE) {
                worldIn.addFreshEntity(laser);
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
