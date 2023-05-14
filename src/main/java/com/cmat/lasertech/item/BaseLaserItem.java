package com.cmat.lasertech.item;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.laser.IFireMode;
import com.cmat.lasertech.laser.ILaserMode;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BaseLaserItem extends Item implements IEnergyStorage {
    private int maxCharge;
    private int chargeRemaining;

    protected BaseLaserItem(int MaxCharge) {
        super(new Properties().tab(LaserTech.LaserTechCreativeTab).stacksTo(1).defaultDurability(MaxCharge));
    }

    public void changeLaserMode(Player player, ItemStack stack) {
        incrementLaserMode(stack);
        player.sendSystemMessage(Component.literal("Laser Mode: " + getLaserMode(stack).getName()));
    }

    public void changeFireMode(Player player, ItemStack stack) {
        incrementFireMode(stack);
        player.sendSystemMessage(Component.literal("Fire Mode: " + getFireMode(stack).getName()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        fireLaser(worldIn, playerIn, handIn);

        return InteractionResultHolder.fail(stack);
    }

    public abstract void fireLaser(Level worldIn, Player playerIn, InteractionHand handIn);

    public abstract ILaserMode getLaserMode(ItemStack stack);

    public abstract IFireMode getFireMode(ItemStack stack);

    public void setFireMode(ItemStack stack, IFireMode mode) {
        ItemDataDealer.setString(stack, "firemode", mode.getName());
    }

    public void setLaserMode(ItemStack stack, ILaserMode mode) {
        ItemDataDealer.setString(stack, "mode", mode.getName());
    }

    public void incrementFireMode(ItemStack stack) {
        IFireMode m = getFireMode(stack);
        setFireMode(stack, m.getNextMode(m));
    }

    public void incrementLaserMode(ItemStack stack) {
        ILaserMode m = getLaserMode(stack);
        setLaserMode(stack, m.getNextMode(m));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
