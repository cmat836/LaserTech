package com.cmat.lasertech.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ExplosiveLaserProjectile extends BaseLaserProjectile {



    public ExplosiveLaserProjectile(EntityType<BaseLaserProjectile> type, LivingEntity thrower, Level worldIn) {
        super(type, thrower, worldIn);
    }

    public ExplosiveLaserProjectile(EntityType<BaseLaserProjectile> explosiveLaserProjectileEntityType, Level world) {
        super(explosiveLaserProjectileEntityType, world);
    }

    @Override
    protected void doEndOfLife() {
        super.doEndOfLife();
        getLevel().explode(this, getX(), getY(), getZ(), radius, Explosion.BlockInteraction.BREAK);
    }

    @Override
    protected boolean EntityHit(EntityHitResult res) {
        res.getEntity().hurt(DamageSource.GENERIC, damage);
        getLevel().explode(this, getX(), getY(), getZ(), radius, Explosion.BlockInteraction.BREAK);
        remove(RemovalReason.DISCARDED);
        return false;
    }

    @Override
    protected boolean onBlockHit(BlockHitResult res) {
        getLevel().explode(this, getX(), getY(), getZ(), radius, Explosion.BlockInteraction.BREAK);
        remove(RemovalReason.DISCARDED);
        return false;
    }
}
