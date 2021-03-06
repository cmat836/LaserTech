package com.cmat.lasertech.item;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.entity.ExplosiveLaserProjectile;
import com.cmat.lasertech.laser.*;
import com.cmat.lasertech.util.ItemDataDealer;
import com.cmat.lasertech.util.LTHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
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

        BaseLaserProjectile laser;

        if (m == MiningLaserMode.EXPLOSIVE) {
            laser = new ExplosiveLaserProjectile(ModEntities.EXPLOSIVELASERPROJECTILE.get(), playerIn, worldIn);
            ((ExplosiveLaserProjectile)laser).setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 4);
        } else {
            laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
            laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage());
        }

        if (getFireMode(stack) == FireMode.HORIZONTAL) {
            Vector3d newPos = laser.getPositionVec();//.add(playerIn.getLookVec().normalize().scale(0.2));
            RayTraceResult r = LTHelper.getBlockLookingAt(worldIn, playerIn);
            if (r.getType() == RayTraceResult.Type.ENTITY || r.getType() == RayTraceResult.Type.MISS) {
                laser.setPosition(newPos.x, newPos.y, newPos.z);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.rotationYaw), 0.0f, m.getVelocity(), 0.0f);
            } else {
                BlockRayTraceResult b = (BlockRayTraceResult)r;
                float theta = LTHelper.roundToRightAngle(playerIn.rotationYaw);
                int x0 = Math.abs(Math.round(-MathHelper.sin(theta * 0.017453292F)));
                int z0 = Math.abs(Math.round(MathHelper.cos(theta * 0.017453292F)));
                laser.setPosition(b.getPos().getX() * z0 + newPos.x * x0, b.getPos().getY(), b.getPos().getZ() * x0 + newPos.z * z0);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.rotationYaw), 0.0f, m.getVelocity(), 0.0f);
            }
        } else {
            Vector3d newPos = laser.getPositionVec();//.add(playerIn.getLookVec().normalize().scale(0.2));
            laser.setPosition(newPos.x, newPos.y, newPos.z);
            laser.fireLaser(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, m.getVelocity(), 0.0f);
        }
        worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), ModSounds.LASER_STANDARD.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
        playerIn.getCooldownTracker().setCooldown(this, m.getCoolDown());

        if (!worldIn.isRemote) {
            if (getFireMode(stack) != FireMode.SAFE) {
                worldIn.addEntity(laser);
            }
        }
    }

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

    public IFireMode getFireMode(ItemStack stack) {
        FireMode m;
        try {
            m = FireMode.valueOf(ItemDataDealer.getString(stack, "firemode"));
        } catch (Exception e) {
            m = FireMode.SAFE;
            setFireMode(stack, m);
        }
        return m;
    }
}
