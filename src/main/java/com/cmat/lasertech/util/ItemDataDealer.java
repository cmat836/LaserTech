package com.cmat.lasertech.util;

import net.minecraft.world.item.ItemStack;

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

    public static void setBoolean(ItemStack item, String dataname, boolean data) {
        item.getTag().putBoolean(dataname, data);
    }

    public static boolean getBoolean(ItemStack item, String dataname) {
        return item.getTag().getBoolean(dataname);
    }

    public static void toggleBoolean(ItemStack item, String dataname) {
        setBoolean(item, dataname, !getBoolean(item, dataname));
    }
}
