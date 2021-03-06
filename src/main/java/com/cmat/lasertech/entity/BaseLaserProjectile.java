package com.cmat.lasertech.entity;

import com.cmat.lasertech.LaserTech;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BaseLaserProjectile extends ThrowableEntity {
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.VARINT);
    private static final DataParameter<Float> BREAKPOWER = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> MINIMUMBREAK = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DAMAGE = EntityDataManager.createKey(BaseLaserProjectile.class, DataSerializers.VARINT);

    protected int lifeTime = 5;
    protected float breakPower = 6f;
    protected float minimumBreak = 4.5f;
    protected int damage = 6;

    protected float breakPowerRemaining;
    protected int lifeTimeRemaining;

    public LivingEntity shooter;

    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, World worldIn) {
        super(type, worldIn);
    }

    public BaseLaserProjectile(EntityType<BaseLaserProjectile> type, LivingEntity thrower, World worldIn) {
        super(type, thrower, worldIn);
        shooter = thrower;
    }

    public void setParams(int lifetime, float breakpower, float minimumbreak, int damage) {
        lifeTime = lifetime;
        breakPower = breakpower;
        minimumBreak = minimumbreak;
        this.damage = damage;
        breakPowerRemaining = breakpower;
        lifeTimeRemaining = lifetime;
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

    @Override
    protected void registerData() {
        dataManager.register(LIFETIME, 5);
        dataManager.register(BREAKPOWER, 6f);
        dataManager.register(MINIMUMBREAK, 4.5f);
        dataManager.register(DAMAGE, 6);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (getEntityWorld().isRemote) {
            if (key.equals(LIFETIME)) {
                lifeTime = dataManager.get(LIFETIME);
            } else if (key.equals(BREAKPOWER)) {
                breakPower = dataManager.get(BREAKPOWER);
            } else if (key.equals(MINIMUMBREAK)) {
                minimumBreak = dataManager.get(MINIMUMBREAK);
            }else if (key.equals(DAMAGE)) {
                damage = dataManager.get(DAMAGE);
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tick() {
        Vector3d motion = getMotion();
        Vector3d pos = getPositionVec();
        super.tick();
        if (ticksExisted == 1) {
            if (!getEntityWorld().isRemote) {
                dataManager.set(LIFETIME, lifeTime);
                dataManager.set(BREAKPOWER, breakPower);
                dataManager.set(DAMAGE, damage);
                dataManager.set(MINIMUMBREAK, minimumBreak);
            } else {
                //getEntityWorld().playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0f, 0.8f, true);
            }
        }

        if (!getEntityWorld().isRemote) {
        RayTraceResult raytraceresult = doRayTrace(pos, motion);

            while (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                raytraceresult = doRayTrace(pos, motion);
                if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    RayTraceResult.Type type = raytraceresult.getType();
                    if (type == RayTraceResult.Type.ENTITY) {
                        this.onEntityHit((EntityRayTraceResult) raytraceresult);
                        break;
                    } else if (type == RayTraceResult.Type.BLOCK) {
                        if (!this.onBlockHit((BlockRayTraceResult)raytraceresult)) {
                            break;
                        }
                    }
                }
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

    protected RayTraceResult doRayTrace(Vector3d pos, Vector3d motion) {
        Vector3d end = pos.add(motion);
        RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(pos, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            end = raytraceresult.getHitVec();
        }
        RayTraceResult raytraceresult1 = ProjectileHelper.rayTraceEntities(world, this, pos, end, getBoundingBox().expand(getMotion()).grow(4.0D), this::func_230298_a_);
        if (raytraceresult1 != null) {
            raytraceresult = raytraceresult1;
        }
        return raytraceresult;
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    @Override
    protected void onEntityHit(EntityRayTraceResult res) {
        res.getEntity().attackEntityFrom(DamageSource.GENERIC, damage);
        remove();
    }

    protected boolean onBlockHit(BlockRayTraceResult res) {
        // Get block and hardness
        BlockState blockState = getEntityWorld().getBlockState(res.getPos());
        float hardness = blockState.getBlockHardness(getEntityWorld(), res.getPos());

        // If its bedrock, or too hard for this beam, return
        if (hardness == -1 || hardness > minimumBreak) {
            remove();
            return false;
        }

        breakPowerRemaining -= hardness;

        // Return true if the beam continues
        if (breakPowerRemaining > 0) {
            getEntityWorld().destroyBlock(res.getPos(), true, shooter);
            return true;
        } else {
            getEntityWorld().destroyBlock(res.getPos(), true, shooter);
            remove();
            return false;
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
