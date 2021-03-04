package com.cmat.lasertech.network;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.laser.BaseLaserItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ModeChangePacket {

    public ModeChangePacket() {
    }

    public static void encode(ModeChangePacket msg, PacketBuffer packetBuffer) {
    }

    public static ModeChangePacket decode(PacketBuffer packetBuffer) {
        return new ModeChangePacket();
    }

    public static void handle(ModeChangePacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)contextSupplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();
            if (player != null) {
                ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
                if (!stack.isEmpty() && stack.getItem() instanceof BaseLaserItem) {
                    ((BaseLaserItem)stack.getItem()).changeMode(player, stack);
                }
            }

        });
        ctx.setPacketHandled(true);
    }
}
