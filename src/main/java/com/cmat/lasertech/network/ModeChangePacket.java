package com.cmat.lasertech.network;

import com.cmat.lasertech.item.BaseLaserItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModeChangePacket {
    ModeChangeType type;

    public ModeChangePacket(ModeChangeType type) {
        this.type = type;
    }

    public static void encode(ModeChangePacket msg, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeEnum(msg.type);
    }

    public static ModeChangePacket decode(FriendlyByteBuf packetBuffer) {
        return new ModeChangePacket(packetBuffer.readEnum(ModeChangeType.class));
    }

    public static void handle(ModeChangePacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)contextSupplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            if (player != null) {
                ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                if (!stack.isEmpty() && stack.getItem() instanceof BaseLaserItem) {
                    if (msg.type == ModeChangeType.LASERMODE) {
                        ((BaseLaserItem)stack.getItem()).changeLaserMode(player, stack);
                    } else if (msg.type == ModeChangeType.FIREMODE) {
                        ((BaseLaserItem)stack.getItem()).changeFireMode(player, stack);
                    }
                }
            }

        });
        ctx.setPacketHandled(true);
    }

    public enum ModeChangeType {
        LASERMODE,
        FIREMODE
    }
}
