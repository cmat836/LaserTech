package com.cmat.lasertech.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraftforge.common.util.Constants;

public class ItemDataDealer {
    public static void setInt(ItemStack item, String dataname, int data) {
        item.getTag().putInt(dataname, data);
    }

    public static int getInt(ItemStack item, String dataname) {
        return item.getTag().getInt(dataname);
    }

    public static void incrementInt(ItemStack item, String dataname, int increment) {
        setInt(item, dataname, getInt(item, dataname) + increment);
    }

    public static void incrementInt(ItemStack item, String dataname) {
        incrementInt(item, dataname, 1);
    }

    public static void setString(ItemStack item, String dataname, String data) {
        item.getTag().putString(dataname, data);
    }

    public static String getString(ItemStack item, String dataname) {
        return item.getTag().getString(dataname);
    }
}
