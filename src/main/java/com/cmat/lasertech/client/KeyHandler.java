package com.cmat.lasertech.client;

import com.cmat.lasertech.laser.BaseLaserItem;
import com.cmat.lasertech.network.ModeChangePacket;
import com.cmat.lasertech.network.PacketHandler;
import com.cmat.lasertech.util.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.BitSet;

public class KeyHandler {
    private KeyBinding[] keyBindings;
    private BitSet keyDown;
    private BitSet repeatings;

    public KeyBinding laserModeSwitch = new KeyBinding("modeswitch", 222, Strings.ModID);
    public KeyBinding fireModeSwitch = new KeyBinding("fireswitch", 223, Strings.ModID);

    public KeyHandler() {
        keyBindings = new KeyBinding[] {
                laserModeSwitch,
                fireModeSwitch
        };
        keyDown = new BitSet();
        repeatings = new BitSet();
        repeatings.set(0, false);
        repeatings.set(1, false);
        for (KeyBinding k : keyBindings) {
            ClientRegistry.registerKeyBinding(k);
        }

        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    private void onTick(InputEvent.KeyInputEvent event) {
        for(int i = 0; i < this.keyBindings.length; ++i) {
            KeyBinding keyBinding = this.keyBindings[i];
            boolean state = keyBinding.isKeyDown();
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

    private void keyUp(KeyBinding keyBinding) {

    }

    private void keyDown(KeyBinding keyBinding, boolean lastState) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (keyBinding == laserModeSwitch) {
            if (player.getHeldItemMainhand().getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.LASERMODE));
            }
        } else if (keyBinding == fireModeSwitch) {
            if (player.getHeldItemMainhand().getItem() instanceof BaseLaserItem) {
                PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(ModeChangePacket.ModeChangeType.FIREMODE));
            }
        }
    }
}
