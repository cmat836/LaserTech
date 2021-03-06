package com.cmat.lasertech;

import com.cmat.lasertech.client.KeyHandler;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.client.render.entity.RenderBaseLaserProjectile;
import com.cmat.lasertech.item.MiningLaserBasicItem;
import com.cmat.lasertech.network.PacketHandler;
import com.cmat.lasertech.util.FluidRegisterHandle;
import com.cmat.lasertech.util.Strings;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
    public static final Logger LOGGER = LogManager.getLogger();

    public static final PacketHandler packetHandler = new PacketHandler();

    public static final ItemGroup LaserTechCreativeTab = new ItemGroup(Strings.ModID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.BASICMININGLASER.get());
        }
    };

    public LaserTech() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        packetHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BASELASERPROJECTILE.get(), RenderBaseLaserProjectile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVELASERPROJECTILE.get(), RenderBaseLaserProjectile.FACTORY);
        new KeyHandler();
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
