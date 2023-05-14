package com.cmat.lasertech.network;

import com.cmat.lasertech.util.Strings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Strings.ModID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private int index = 0;

    public PacketHandler() {

    }


    public void init() {
        INSTANCE.registerMessage(index++, ModeChangePacket.class, ModeChangePacket::encode, ModeChangePacket::decode, ModeChangePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
