package com.cmat.lasertech.entity;

import com.cmat.lasertech.LaserTech;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class BaseLaserProjectile extends ThrowableProjectile {
    /***
     * Manages storing NBT data so the entity actually syncs properly
     */
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(BaseLaserProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> BREAKPOWER = SynchedEntityData.defineId(BaseLaserProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MINIMUMBREAK = SynchedEntityData.defineId(BaseLaserProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DAMAGE = SynchedEntityData.defineId(BaseLaserProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PIERCEPOWER = SynchedEntityData.defineId(BaseLaserProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(ExplosiveLaserProjectile.class, EntityDataSerializers.FLOAT);

    protected int lifeTime = 5;
    protected float breakPower = 6f;
    protected float minimumBreak = 4.5f;
    protected int damage = 6;
    protected int piercePower = 1;
    protected float radius = 0f;

    protected int piercePowerRemaining;
    protected float breakPowerRemaining;
    protected int lifeTimeRemaining;

    /***
     * Who shot this
     */
    public LivingEntity shooter;

    /***
     * Makes a laser but it dont go anywhere
     * @param type
     * @param worldIn
     */
    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    /***
     * Makes a laser but points it in the right direction
     * @param type
     * @param thrower
     * @param worldIn
     */
    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, LivingEntity thrower, Level worldIn) {
        super(type, thrower, worldIn);
        shooter = thrower;
    }

    /***
     * Set the parameters for this laser
     * @param lifetime how long it lives for (ticks)
     * @param breakpower how many effective blocks can this break
     * @param minimumbreak the minimum effective blocks this can break
     * @param damage damage dealt on hit (at point blank)
     * @param radius explosion radius
     * @param piercepower how many entities can this penetrate
     */
    public void setParams(int lifetime, float breakpower, float minimumbreak, int damage, float radius, int piercepower) {
        lifeTime = lifetime;
        breakPower = breakpower;
        minimumBreak = minimumbreak;
        this.damage = damage;
        piercePower = piercepower;
        breakPowerRemaining = breakpower;
        lifeTimeRemaining = lifetime;
        piercePowerRemaining = piercepower;
        this.radius = radius;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public float getBreakPower() {
        return breakPower;
    }

    public int getDamage() {
        return damage;
    }

    /***
     * Register all the stored data
     */
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(LIFETIME, 5);
        this.getEntityData().define(BREAKPOWER, 6f);
        this.getEntityData().define(MINIMUMBREAK, 4.5f);
        this.getEntityData().define(DAMAGE, 6);
        this.getEntityData().define(PIERCEPOWER, 1);
        this.getEntityData().define(RADIUS, 3f);
    }

    /***
     * Call every time data changes
     * @param key
     */
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (!getLevel().isClientSide()) {
            if (key.equals(LIFETIME)) {
                lifeTime = this.getEntityData().get(LIFETIME);
            } else if (key.equals(BREAKPOWER)) {
                breakPower = this.getEntityData().get(BREAKPOWER);
            } else if (key.equals(MINIMUMBREAK)) {
                minimumBreak = this.getEntityData().get(MINIMUMBREAK);
            } else if (key.equals(DAMAGE)) {
                damage = this.getEntityData().get(DAMAGE);
            } else if (key.equals(PIERCEPOWER)) {
                piercePower = this.getEntityData().get(PIERCEPOWER);
            } else if (key.equals(RADIUS)) {
                radius = this.getEntityData().get(RADIUS);
            }
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tick() {
        Vec3 motion = getDeltaMovement();
        Vec3 pos = position();
        super.tick();
        if (tickCount == 1) {
            if (!getLevel().isClientSide()) {
                this.getEntityData().set(LIFETIME, lifeTime);
                this.getEntityData().set(BREAKPOWER, breakPower);
                this.getEntityData().set(DAMAGE, damage);
                this.getEntityData().set(MINIMUMBREAK, minimumBreak);
                this.getEntityData().set(PIERCEPOWER, piercePower);
                this.getEntityData().set(RADIUS, radius);
            } else {
                //getEntityWorld().playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0f, 0.8f, true);
            }
        }

        if (!getLevel().isClientSide()) {
        HitResult raytraceresult = doRayTrace(pos, motion);

            while (raytraceresult.getType() != HitResult.Type.MISS) {
                raytraceresult = doRayTrace(pos, motion);
                if (raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    HitResult.Type type = raytraceresult.getType();
                    if (type == HitResult.Type.ENTITY) {
                        if (!this.EntityHit((EntityHitResult) raytraceresult)) {
                            break;
                        }
                    } else if (type == HitResult.Type.BLOCK) {
                        if (!this.onBlockHit((BlockHitResult)raytraceresult)) {
                            break;
                        }
                    }
                }
            }
        }


        lifeTimeRemaining--;
        if (lifeTimeRemaining == 0) {
            this.doEndOfLife();
        }

        if (this.isInWater()) {
            setDeltaMovement(getDeltaMovement().scale(1.25));
        } else {
            setDeltaMovement(getDeltaMovement().scale(1 / 0.99));
        }

    }

    protected HitResult doRayTrace(Vec3 pos, Vec3 motion) {
        Vec3 end = pos.add(motion);
        HitResult raytraceresult = level.clip(new ClipContext(pos, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            end = raytraceresult.getLocation();
        }
        HitResult  raytraceresult1 = ProjectileUtil.getEntityHitResult(level, this, pos, end, getBoundingBox().expandTowards(getDeltaMovement()).inflate(4.0D), this::canHitEntity);
        if (raytraceresult1 != null) {
            raytraceresult = raytraceresult1;
        }
        return raytraceresult;
    }

    protected void doEndOfLife() {
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHit(HitResult result) {

    }

    protected boolean EntityHit(EntityHitResult res) {
        res.getEntity().hurt(DamageSource.GENERIC, damage);
        if (res.getEntity() instanceof LivingEntity) {
            ((LivingEntity)res.getEntity()).invulnerableTime = 0;
        }
        piercePowerRemaining--;
        if (piercePowerRemaining <= 0) {
            doEndOfLife();
            return false;
        } else {
            return true;
        }
    }

    protected boolean onBlockHit(BlockHitResult res) {
        // Get block and hardness
        BlockState blockState = getLevel().getBlockState(res.getBlockPos());
        float hardness = blockState.getDestroySpeed(getLevel(), res.getBlockPos());

        // If its bedrock, or too hard for this beam, return
        if (hardness == -1 || hardness > minimumBreak) {
            remove(RemovalReason.DISCARDED);
            return false;
        }

        breakPowerRemaining -= hardness;

        // Return true if the beam continues
        if (breakPowerRemaining > 0) {
            getLevel().destroyBlock(res.getBlockPos(), true, shooter);
            return true;
        } else {
            getLevel().destroyBlock(res.getBlockPos(), true, shooter);
            remove(RemovalReason.DISCARDED);
            return false;
        }
    }

    // shoot()
    public void fireLaser(Entity entityThrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float variance1 = (LaserTech.r.nextInt(11) - 5) * inaccuracy;
        float variance2 = (LaserTech.r.nextInt(11) - 5) * inaccuracy;
        yaw += variance1;
        pitch += variance2;
        float x0 = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        float y0 = -Mth.sin(pitch * 0.017453292F);
        float z0 = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        double f = Math.sqrt(x0 * x0 + y0 * y0 + z0 * z0);
        float x1 = (float) (x0 / f * velocity);
        float y1 = (float) (y0 / f * velocity);
        float z1 = (float) (z0 / f * velocity);
        setDeltaMovement(x1, y1, z1);
        float f1 = Mth.sqrt(x1 * x1 + z1 * z1);
        this.setYRot((float)(Mth.atan2(x1, z1) * (180D / Math.PI)));
        this.setXRot((float)(Mth.atan2(y1, f1) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        setDeltaMovement(getDeltaMovement().add(entityThrower.getDeltaMovement().x, 0, entityThrower.getDeltaMovement().z));
    }

    @Override
    protected float getGravity() {
        return 0f;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
