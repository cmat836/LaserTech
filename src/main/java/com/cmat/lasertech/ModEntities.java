package com.cmat.lasertech;

import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.entity.ExplosiveLaserProjectile;
import com.cmat.lasertech.util.Strings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Strings.ModID);

    public static final RegistryObject<EntityType<BaseLaserProjectile>> BASELASERPROJECTILE =
            register("baselaserprojectile", ModEntities::baselaserprojectile);

    public static final RegistryObject<EntityType<BaseLaserProjectile>> EXPLOSIVELASERPROJECTILE =
            register("explosivelaserprojectile", ModEntities::explosivelaserprojectile);

    private static <E extends Entity> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<BaseLaserProjectile> baselaserprojectile() {
        return EntityType.Builder.<BaseLaserProjectile>of(BaseLaserProjectile::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .fireImmune()
                .setTrackingRange(5)
                .setUpdateInterval(1)
                .setCustomClientFactory((spawnEntity, world) -> ModEntities.BASELASERPROJECTILE.get().create(world))
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<BaseLaserProjectile> explosivelaserprojectile() {
        return EntityType.Builder.<BaseLaserProjectile>of(ExplosiveLaserProjectile::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .fireImmune()
                .setTrackingRange(5)
                .setUpdateInterval(1)
                .setCustomClientFactory((spawnEntity, world) -> ModEntities.EXPLOSIVELASERPROJECTILE.get().create(world))
                .setShouldReceiveVelocityUpdates(true);
    }
}
