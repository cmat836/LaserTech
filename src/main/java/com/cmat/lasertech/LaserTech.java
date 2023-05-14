package com.cmat.lasertech;

import com.cmat.lasertech.client.key.KeyHandler;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.client.render.entity.RenderBaseLaserProjectile;
import com.cmat.lasertech.network.PacketHandler;
import com.cmat.lasertech.util.Strings;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod("lasertech")
public class LaserTech {
    public static Random r = new Random();

    public static final Logger LOGGER = LogManager.getLogger();

    public static final PacketHandler packetHandler = new PacketHandler();

    public static final CreativeModeTab LaserTechCreativeTab = new CreativeModeTab(Strings.ModID) {
        @Override
        public ItemStack makeIcon() {
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::renderers);

        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        packetHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BASELASERPROJECTILE.get(), RenderBaseLaserProjectile.FACTORY);
        event.registerEntityRenderer(ModEntities.EXPLOSIVELASERPROJECTILE.get(), RenderBaseLaserProjectile.FACTORY);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        new KeyHandler();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
