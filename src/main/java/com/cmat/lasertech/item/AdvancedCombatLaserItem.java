package com.cmat.lasertech.item;

import com.cmat.lasertech.ModEntities;
import com.cmat.lasertech.client.ModSounds;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.entity.ExplosiveLaserProjectile;
import com.cmat.lasertech.laser.*;
import com.cmat.lasertech.util.ItemDataDealer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class AdvancedCombatLaserItem extends BaseLaserItem {
    public AdvancedCombatLaserItem() {
        super(100);
    }

    @Override
    public void fireLaser(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        AdvancedCombatLaserMode m = (AdvancedCombatLaserMode)getLaserMode(stack);
        FireModeCombat f = (FireModeCombat)getFireMode(stack);

        ArrayList<BaseLaserProjectile> lasers = new ArrayList<BaseLaserProjectile>();

        boolean scatter = (m == AdvancedCombatLaserMode.SCATTER);
        boolean shotgun = (f == FireModeCombat.SHOTGUN);
        int p = 1;
        if (shotgun)
            p *= 5;
        if (scatter)
            p *= 5;

        for (int i = 0; i < p; i++) {
            BaseLaserProjectile b;
            if (m == AdvancedCombatLaserMode.EXPLOSIVE || m == AdvancedCombatLaserMode.KABOOM) {
                b = new ExplosiveLaserProjectile(ModEntities.EXPLOSIVELASERPROJECTILE.get(), playerIn, worldIn);
                b.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), m == AdvancedCombatLaserMode.EXPLOSIVE ? 4 : 12, m.getPiercePower());
            } else {
                b = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
                b.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 0, m.getPiercePower());
            }

            Vec3 newPos = b.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
            b.setPos(newPos.x, newPos.y, newPos.z);
            b.fireLaser(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, m.getVelocity(), (scatter || shotgun ? 1 : 0) * 1f);
            lasers.add(b);
        }

        if (getFireMode(stack) != FireMode.SAFE) {
            if (worldIn.isClientSide) {
                for (BaseLaserProjectile b : lasers) {
                    worldIn.addFreshEntity(b);
                }
            }

            if (f == FireModeCombat.BURST)
                ItemDataDealer.setInt(stack, "burstremaining", 3);

            worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.LASER_STANDARD.get(), SoundSource.PLAYERS.PLAYERS, 1.0f, 1.0f);
            boolean delay = f == FireModeCombat.BURST || f == FireModeCombat.SHOTGUN;
            playerIn.getCooldowns().addCooldown(this, m.getCoolDown() * (delay ? 3 : 1));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn == null) {
            return;
        }
        if (ItemDataDealer.getInt(stack, "burstremaining") > 0 && entityIn instanceof Player) {
            ArrayList<BaseLaserProjectile> lasers = new ArrayList<BaseLaserProjectile>();
            Player playerIn = (Player)entityIn;
            AdvancedCombatLaserMode m = (AdvancedCombatLaserMode)getLaserMode(stack);
            FireModeCombat f = (FireModeCombat)getFireMode(stack);
            boolean shotgun = (f == FireModeCombat.SHOTGUN);
            int p = 1;
            if (shotgun)
                p *= 5;
            for (int i = 0; i < p; i++) {
                BaseLaserProjectile b;
                if (m == AdvancedCombatLaserMode.EXPLOSIVE || m == AdvancedCombatLaserMode.KABOOM) {
                    b = new ExplosiveLaserProjectile(ModEntities.EXPLOSIVELASERPROJECTILE.get(), playerIn, worldIn);
                    b.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), m == AdvancedCombatLaserMode.EXPLOSIVE ? 4 : 12, m.getPiercePower());
                } else {
                    b = new BaseLaserProjectile(ModEntities.BASELASERPROJECTILE.get(), playerIn, worldIn);
                    b.setParams(m.getLifeTime(), m.getBreakPower(), m.getMinimumBreak(), m.getDamage(), 0, m.getPiercePower());
                }

                Vec3 newPos = b.position();//.add(playerIn.getLookVec().normalize().scale(0.2));
                b.setPos(newPos.x, newPos.y, newPos.z);
                b.fireLaser(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, m.getVelocity(), (shotgun ? 1 : 0) * 1f);
                lasers.add(b);
            }
            if (!worldIn.isClientSide) {
                for (BaseLaserProjectile b : lasers) {
                    worldIn.addFreshEntity(b);
                }
            }
            worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.LASER_STANDARD.get(), SoundSource.PLAYERS.PLAYERS, 1.0f, 1.0f);
            ItemDataDealer.incrementInt(stack, "burstremaining", -1);
        }
    }

    public ILaserMode getLaserMode(ItemStack stack) {
        AdvancedCombatLaserMode m;
        try {
            m = AdvancedCombatLaserMode.valueOf(ItemDataDealer.getString(stack, "mode"));
        } catch (Exception e) {
            m = AdvancedCombatLaserMode.COMBAT;
            setLaserMode(stack, m);
        }
        return m;
    }

    public IFireMode getFireMode(ItemStack stack) {
        FireModeCombat m;
        try {
            m = FireModeCombat.valueOf(ItemDataDealer.getString(stack, "firemode"));
        } catch (Exception e) {
            m = FireModeCombat.SAFE;
            setFireMode(stack, m);
        }
        return m;
    }
}
