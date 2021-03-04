package com.cmat.lasertech;

import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.util.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Strings.ModID);

    public static final RegistryObject<EntityType<BaseLaserProjectile>> BASELASERPROJECTILE =
            register("baselaserprojectile", ModEntities::baselaserprojectile);

    private static <E extends Entity> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<BaseLaserProjectile> baselaserprojectile() {
        return EntityType.Builder.<BaseLaserProjectile>create(BaseLaserProjectile::new, EntityClassification.MISC)
                .size(0.5f, 0.5f)
                .immuneToFire()
                .setTrackingRange(5)
                .setUpdateInterval(1)
                .setCustomClientFactory((spawnEntity, world) -> ModEntities.BASELASERPROJECTILE.get().create(world))
                .setShouldReceiveVelocityUpdates(true);
    }
}
