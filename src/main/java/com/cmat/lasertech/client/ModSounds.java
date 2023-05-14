package com.cmat.lasertech.client;

import com.cmat.lasertech.util.Strings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Strings.ModID);

    public static final RegistryObject<SoundEvent> LASER_STANDARD = SOUNDS.register("laser_standard", () -> new SoundEvent(new ResourceLocation(Strings.ModID, "laser_standard")));
}
