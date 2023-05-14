package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.entity.ExplosiveLaserProjectile;
import com.cmat.lasertech.laser.AdvancedMiningLaserMode;
import com.cmat.lasertech.laser.FireMode;
import com.cmat.lasertech.laser.IFireMode;
import com.cmat.lasertech.laser.ILaserMode;
import com.cmat.lasertech.util.ItemDataDealer;
import com.cmat.lasertech.util.LTHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class AdvancedMiningLaserItem extends BaseLaserItem {

    public AdvancedMiningLaserItem() {
        super(100);
    }

    @Override
    public void fireLaser(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        AdvancedMiningLaserMode m = (AdvancedMiningLaserMode)getLaserMode(stack);

        ArrayList<BaseLaserProjectile> lasers = new ArrayList<BaseLaserProjectile>();
        BaseLaserProjectile laser;

        if (m == AdvancedMiningLaserMode.EXPLOSIVE) {
            laser = new ExplosiveLaserProjectile(ModEntities.EXPLOSIVELASERPROJECTILE.get(), playerIn, worldIn);
            laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 4, m.getPiercePower());
        } else {
            laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
            laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 0, m.getPiercePower());
        }

        Vec3 newPos = laser.position();//.add(playerIn.getLookVec().normalize().scale(0.2));



        if (getFireMode(stack) == FireMode.HORIZONTAL || m == AdvancedMiningLaserMode.THREEBYTHREE) {
            HitResult r = LTHelper.getBlockLookingAt(worldIn, playerIn);
            float theta = LTHelper.roundToRightAngle(playerIn.getYRot());
            int x0 = Math.abs(Math.round(-Mth.sin(theta * 0.017453292F)));
            int z0 = Math.abs(Math.round(Mth.cos(theta * 0.017453292F)));
            if (r.getType() == HitResult.Type.ENTITY || r.getType() == HitResult.Type.MISS) {
                laser.setPos(newPos.x, newPos.y, newPos.z);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.getYRot()), 0.0f, m.getVelocity(), 0.0f);
            } else {
                BlockHitResult b = (BlockHitResult) r;
                laser.setPos(b.getLocation().x() * z0 + newPos.x * x0, b.getLocation().y(), b.getLocation().z() * x0 + newPos.z * z0);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.getYRot()), 0.0f, m.getVelocity(), 0.0f);
            }
            if (m == AdvancedMiningLaserMode.THREEBYTHREE) {
                BaseLaserProjectile[] bs = new BaseLaserProjectile[8];
                for (int i = 0; i < 8; i++) {
                    bs[i] = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
                    bs[i].setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 0, m.getPiercePower());
                    bs[i].setPos(laser.getX(), laser.getY(), laser.getZ());
                    bs[i].setDeltaMovement(laser.getDeltaMovement());

                    bs[i].setXRot(laser.getXRot());
                    bs[i].xRotO = laser.getXRot();
                    bs[i].setYRot(laser.getYRot());
                    bs[i].yRotO = laser.getYRot();
                }
                bs[0].setPos(bs[0].getX(), bs[0].getY() + 1, bs[0].getZ());
                bs[1].setPos(bs[1].getX(), bs[1].getY() - 1, bs[1].getZ());
            }
        } else {
            newPos = laser.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
            laser.setPos(newPos.x, newPos.y, newPos.z);
            laser.fireLaser(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, m.getVelocity(), 0.0f);
        }
        lasers.add(laser);

        worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.LASER_STANDARD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        playerIn.getCooldowns().addCooldown(this, m.getCoolDown());

        if (!worldIn.isClientSide) {
            if (getFireMode(stack) != FireMode.SAFE) {
                worldIn.addFreshEntity(laser);
            }
        }
    }

    public ILaserMode getLaserMode(ItemStack stack) {
        AdvancedMiningLaserMode m;
        try {
            m = AdvancedMiningLaserMode.valueOf(ItemDataDealer.getString(stack, "mode"));
        } catch (Exception e) {
            m = AdvancedMiningLaserMode.LOW_FOCUS;
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
