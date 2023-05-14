package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.entity.ExplosiveLaserProjectile;
import com.cmat.lasertech.laser.FireMode;
import com.cmat.lasertech.laser.IFireMode;
import com.cmat.lasertech.laser.ILaserMode;
import com.cmat.lasertech.laser.MiningLaserMode;
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

public class MiningLaserItem extends BaseLaserItem {

    public MiningLaserItem() {
        super(100);
    }

    @Override
    public void fireLaser(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        MiningLaserMode m = (MiningLaserMode)getLaserMode(stack);

        BaseLaserProjectile laser;

        if (m == MiningLaserMode.EXPLOSIVE) {
            laser = new ExplosiveLaserProjectile(ModEntities.EXPLOSIVELASERPROJECTILE.get(), playerIn, worldIn);
            laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), 4, m.getDamage(), m.getPiercePower());
        } else {
            laser = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
            laser.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage() , 0, m.getPiercePower());
        }

        if (getFireMode(stack) == FireMode.HORIZONTAL) {
            Vec3 newPos = laser.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
            HitResult r = LTHelper.getBlockLookingAt(worldIn, playerIn);
            if (r.getType() == HitResult.Type.ENTITY || r.getType() == HitResult.Type.MISS) {
                laser.setPos(newPos.x, newPos.y, newPos.z);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.getYRot()), 0.0f, m.getVelocity(), 0.0f);
            } else {
                BlockHitResult b = (BlockHitResult) r;
                float theta = LTHelper.roundToRightAngle(playerIn.getYRot());
                int x0 = Math.abs(Math.round(-Mth.sin(theta * 0.017453292F)));
                int z0 = Math.abs(Math.round(Mth.cos(theta * 0.017453292F)));
                laser.setPos(b.getLocation().x() * z0 + newPos.x * x0, b.getLocation().y(), b.getLocation().y() * x0 + newPos.z * z0);
                laser.fireLaser(playerIn, 0, LTHelper.roundToRightAngle(playerIn.getYRot()), 0.0f, m.getVelocity(), 0.0f);
            }
        } else {
            Vec3 newPos = laser.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
            laser.setPos(newPos.x, newPos.y, newPos.z);
            laser.fireLaser(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, m.getVelocity(), 0.0f);
        }
        worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.LASER_STANDARD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        playerIn.getCooldowns().addCooldown(this, m.getCoolDown());

        if (!worldIn.isClientSide) {
            if (getFireMode(stack) != FireMode.SAFE) {
                worldIn.addFreshEntity(laser);
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
