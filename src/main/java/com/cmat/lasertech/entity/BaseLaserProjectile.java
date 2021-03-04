package com.cmat.lasertech.entity;

import com.cmat.lasertech.LaserTech;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BaseLaserProjectile extends ThrowableEntity {
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BREAKPOWER = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> EXPLODESONIMPACT = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DAMAGE = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.VARINT);

    private int lifeTime = 30;
    private int breakPower = 10;
    private boolean explodesOnImpact = false;
    private int damage = 6;

    private float breakPowerRemaining;
    private int lifeTimeRemaining;

    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, World worldIn) {
        super(type, worldIn);
    }

    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, LivingEntity thrower, World worldIn) {
        super(type, thrower, worldIn);
    }

    public void setParams(int lifetime, int breakpower, boolean explodesonimpact, int damage) {
        lifeTime = lifetime;
        breakPower = breakpower;
        explodesOnImpact = explodesonimpact;
        damage = damage;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getBreakPower() {
        return breakPower;
    }

    public int getDamage() {
        return damage;
    }

    public boolean getExplodesOnImpact() {
        return explodesOnImpact;
    }

    @Override
    protected void registerData() {
        dataManager.register(LIFETIME, 30);
        dataManager.register(BREAKPOWER, 10);
        dataManager.register(EXPLODESONIMPACT, false);
        dataManager.register(DAMAGE, 6);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (getEntityWorld().isRemote) {
            if (key.equals(LIFETIME)) {
                lifeTime = dataManager.get(LIFETIME);
            } else if (key.equals(BREAKPOWER)) {
                breakPower = dataManager.get(BREAKPOWER);
            } else if (key.equals(EXPLODESONIMPACT)) {
                explodesOnImpact = dataManager.get(EXPLODESONIMPACT);
            } else if (key.equals(DAMAGE)) {
                damage = dataManager.get(DAMAGE);
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (ticksExisted == 1) {
            if (!getEntityWorld().isRemote) {
                breakPowerRemaining = breakPower;
                lifeTimeRemaining = lifeTime;
                dataManager.set(LIFETIME, lifeTime);
                dataManager.set(BREAKPOWER, breakPower);
                dataManager.set(EXPLODESONIMPACT, explodesOnImpact);
                dataManager.set(DAMAGE, damage);
            } else {
                //getEntityWorld().playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0f, 0.8f, true);
            }
        }

        lifeTimeRemaining--;
        if (lifeTimeRemaining == 0) {
            remove();
        }

        if (this.isInWater()) {
            setMotion(getMotion().scale(1.25));
        } else {
            setMotion(getMotion().scale(1 / 0.99));
        }

    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!getEntityWorld().isRemote) {
            RayTraceResult.Type type = result.getType();
            if (type == RayTraceResult.Type.ENTITY) {

                this.onEntityHit((EntityRayTraceResult) result);

            } else if (type == RayTraceResult.Type.BLOCK) {
                this.onBlockHit((BlockRayTraceResult) result);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult res) {
        res.getEntity().attackEntityFrom(DamageSource.GENERIC, damage);
        remove();
    }

    protected void onBlockHit(BlockRayTraceResult res) {
        BlockState blockState = getEntityWorld().getBlockState(res.getPos());
        float hardness = blockState.getBlockHardness(getEntityWorld(), res.getPos());
        breakPowerRemaining -= hardness;
        if (breakPowerRemaining >= 0) {
            getEntityWorld().removeBlock(res.getPos(), false);
        } else {
            remove();
        }
    }

    // shoot()
    public void fireLaser(Entity entityThrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float x0 = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float y0 = -MathHelper.sin(pitch * 0.017453292F);
        float z0 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        double f = Math.sqrt(x0 * x0 + y0 * y0 + z0 * z0);
        double x1 = x0 / f * velocity;
        double y1 = y0 / f * velocity;
        double z1 = z0 / f * velocity;
        setMotion(x1, y1, z1);
        float f1 = MathHelper.sqrt(x1 * x1 + z1 * z1);
        this.rotationYaw = (float)(MathHelper.atan2(x1, z1) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y1, f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        setMotion(getMotion().add(entityThrower.getMotion().x, 0, entityThrower.getMotion().z));
    }

    @Override
    protected float getGravityVelocity() {
        return 0f;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
