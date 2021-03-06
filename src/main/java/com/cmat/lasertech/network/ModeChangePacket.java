package com.cmat.lasertech.network;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.laser.BaseLaserItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.jws.WebParam;
import java.util.function.Supplier;

public class ModeChangePacket {
    ModeChangeType type;

    public ModeChangePacket(ModeChangeType type) {
        this.type = type;
    }

    public static void encode(ModeChangePacket msg, PacketBuffer packetBuffer) {
        packetBuffer.writeEnumValue(msg.type);
    }

    public static ModeChangePacket decode(PacketBuffer packetBuffer) {
        return new ModeChangePacket(packetBuffer.readEnumValue(ModeChangeType.class));
    }

    public static void handle(ModeChangePacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)contextSupplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();
            if (player != null) {
                ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
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
