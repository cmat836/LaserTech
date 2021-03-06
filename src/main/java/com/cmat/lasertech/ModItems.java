package com.cmat.lasertech;

import com.cmat.lasertech.item.MiningLaserBasicItem;
import com.cmat.lasertech.item.MiningLaserItem;
import com.cmat.lasertech.util.Strings;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Strings.ModID);

    public static final RegistryObject<Item> BASICMININGLASER =
            ITEMS.register(Strings.BasicMiningLaserName, () -> new MiningLaserBasicItem());
    public static final RegistryObject<Item> MININGLASER =
            ITEMS.register(Strings.MiningLaserName, () -> new MiningLaserItem());
}
