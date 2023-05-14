package com.cmat.lasertech;

import com.cmat.lasertech.item.AdvancedCombatLaserItem;
import com.cmat.lasertech.item.AdvancedMiningLaserItem;
import com.cmat.lasertech.item.MiningLaserBasicItem;
import com.cmat.lasertech.item.MiningLaserItem;
import com.cmat.lasertech.util.Strings;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Strings.ModID);

    public static final RegistryObject<Item> BASICMININGLASER =
            ITEMS.register(Strings.BasicMiningLaserName, () -> new MiningLaserBasicItem());
    public static final RegistryObject<Item> MININGLASER =
            ITEMS.register(Strings.MiningLaserName, () -> new MiningLaserItem());
    public static final RegistryObject<Item> ADVANCEDMININGLASER =
            ITEMS.register(Strings.AdvancedMiningLaserName, () -> new AdvancedMiningLaserItem());
    public static final RegistryObject<Item> ADVANCEDCOMBATLASER =
            ITEMS.register(Strings.AdvancedCombatLaserName, () -> new AdvancedCombatLaserItem());
}
