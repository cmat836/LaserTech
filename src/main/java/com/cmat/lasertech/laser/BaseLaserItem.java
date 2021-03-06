package com.cmat.lasertech.laser;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.laser.ILaserMode;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.filter.ChatFilterClient;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BaseLaserItem extends Item implements IEnergyStorage {
    private int maxCharge;
    private int chargeRemaining;

    protected BaseLaserItem(int MaxCharge) {
        super(new Properties().group(LaserTech.LaserTechCreativeTab).maxStackSize(1).defaultMaxDamage(MaxCharge));
    }

    public void changeLaserMode(PlayerEntity player, ItemStack stack) {
        incrementLaserMode(stack);
        player.sendStatusMessage(new StringTextComponent("Laser Mode: " + getLaserMode(stack).getName()), false);
    }

    public void changeFireMode(PlayerEntity player, ItemStack stack) {
        incrementFireMode(stack);
        player.sendStatusMessage(new StringTextComponent("Fire Mode: " + getFireMode(stack).getName()), false);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        fireLaser(worldIn, playerIn, handIn);

        return ActionResult.resultFail(stack);
    }

    public abstract void fireLaser(World worldIn, PlayerEntity playerIn, Hand handIn);

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
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

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
