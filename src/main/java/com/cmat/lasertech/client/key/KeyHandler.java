package com.cmat.lasertech.client.key;

import com.cmat.lasertech.item.BaseLaserItem;
import com.cmat.lasertech.network.ModeChangePacket;
import com.cmat.lasertech.network.PacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "lasertech", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyHandler {

    public static final KeyMapping laserModeSwitch = new LTKeyBindingBuilder().description("Mode").conflictInGame().keyCode(GLFW.GLFW_KEY_M).onKeyDown((kb, isRepeat) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.LASERMODE));
            }
        }

    }).build();

    public static final KeyMapping fireModeSwitch = new LTKeyBindingBuilder().description("Mode").conflictInGame().keyCode(GLFW.GLFW_KEY_N).onKeyDown((kb, isRepeat) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.FIREMODE));
            }
        }

    }).build();

    public KeyHandler() {
        /*keyBindings = new KeyMapping[] {
                laserModeSwitch,
                fireModeSwitch
        };*/
        /*for (KeyMapping k : keyBindings) {
            ClientRegistry.registerKeyBinding(k);
        }*/


        //MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
        KeyMapping[] keyBindings;
        keyBindings = new KeyMapping[] {
                laserModeSwitch,
                fireModeSwitch
        };

        for (KeyMapping k : keyBindings) {
            event.register(k);
        }
    }

    /*
    private void onTick(InputEvent.Key event) {
        for(int i = 0; i < this.keyBindings.length; ++i) {
            KeyMapping keyBinding = this.keyBindings[i];
            boolean state = keyBinding.isDown();
            boolean lastState = this.keyDown.get(i);
            if (state != lastState || state && this.repeatings.get(i)) {
                if (state) {
                    this.keyDown(keyBinding, lastState);
                } else {
                    this.keyUp(keyBinding);
                }

                this.keyDown.set(i, state);
            }
        }
    }

    private void keyUp(KeyMapping keyBinding) {

    }
     */
    private void keyDown(KeyMapping keyBinding, boolean lastState) {
        Player player = Minecraft.getInstance().player;
        if (keyBinding == laserModeSwitch) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.LASERMODE));
            }
        } else if (keyBinding == fireModeSwitch) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.FIREMODE));
            }
        }
    }


}
