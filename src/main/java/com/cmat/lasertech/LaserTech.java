package com.cmat.lasertech;

import com.cmat.lasertech.util.FluidRegisterHandle;
import com.cmat.lasertech.util.Strings;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

@Mod("lasertech")
public class LaserTech {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, Strings.ModID);
    private static final DeferredRegister<Fluid> fluidRegister = DeferredRegister.create(ForgeRegistries.FLUIDS, Strings.ModID);
    private static final DeferredRegister<Block> blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, Strings.ModID);
    private static final DeferredRegister<EntityType<?>> entityRegister = DeferredRegister.create(ForgeRegistries.ENTITIES, Strings.ModID);

    public static final LinkedHashMap<String, RegistryObject<Item>> customItems = new LinkedHashMap<String, RegistryObject<Item>>();
    public static final LinkedHashMap<String, Tuple<RegistryObject<Block>, RegistryObject<Item>>> customBlocks = new LinkedHashMap<>();
    public static final LinkedHashMap<String, FluidRegisterHandle> customFluids = new LinkedHashMap<String, FluidRegisterHandle>();
    public static final LinkedHashMap<String, RegistryObject<EntityType<?>>> customEntities = new LinkedHashMap<>();

    public LaserTech() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        itemRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        fluidRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        blockRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        entityRegister.register(FMLJavaModLoadingContext.get().getModEventBus());

        doItemRegister();
        doFluidRegister();
        doBlockRegister();
        doEntityRegister();
    }

    private void doItemRegister() {

    }

    private void doFluidRegister() {

    }

    private void doBlockRegister() {

    }

    private void doEntityRegister() {

    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }
}
