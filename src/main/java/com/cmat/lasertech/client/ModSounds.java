package com.cmat.lasertech.client;

import com.cmat.lasertech.util.Strings;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Strings.ModID);

    public static final RegistryObject<SoundEvent> LASER_STANDARD = SOUNDS.register("laser_standard", () -> new SoundEvent(new ResourceLocation(Strings.ModID, "laser_standard")));
}
