package com.cmat.lasertech.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosiveLaserProjectile extends BaseLaserProjectile {
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.FLOAT);

    protected float radius = 3f;

    public ExplosiveLaserProjectile(EntityType<BaseLaserProjectile> type, LivingEntity thrower, World worldIn) {
        super(type, thrower, worldIn);
    }

    public ExplosiveLaserProjectile(EntityType<BaseLaserProjectile> explosiveLaserProjectileEntityType, World world) {
        super(explosiveLaserProjectileEntityType, world);
    }

    public void setParams(int lifetime, float breakpower, float minimumbreak, int damage, float radius) {
        super.setParams(lifetime, breakpower, minimumbreak, damage);
        this.radius = radius;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(RADIUS, 3f);
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (getEntityWorld().isRemote) {
            if (key.equals(RADIUS)) {
                radius = dataManager.get(RADIUS);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult res) {
        res.getEntity().attackEntityFrom(DamageSource.GENERIC, damage);
        getEntityWorld().createExplosion(this, getPosX(), getPosY(), getPosZ(), radius, Explosion.Mode.BREAK);
        remove();
    }

    @Override
    protected boolean onBlockHit(BlockRayTraceResult res) {
        getEntityWorld().createExplosion(this, getPosX(), getPosY(), getPosZ(), radius, Explosion.Mode.BREAK);
        remove();
        return false;
    }
}
