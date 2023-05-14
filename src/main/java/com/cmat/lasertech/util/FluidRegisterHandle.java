package com.cmat.lasertech.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;

public class FluidRegisterHandle<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends FlowingFluid, BUCKET extends BucketItem> {
    private RegistryObject<STILL> stillRO;
    private RegistryObject<FLOWING> flowingRO;
    private RegistryObject<BLOCK> blockRO;
    private RegistryObject<BUCKET> bucketRO;

    public FluidRegisterHandle(String modid, String name) {
        this.stillRO = RegistryObject.create(new ResourceLocation(modid, name), ForgeRegistries.FLUIDS);
        this.flowingRO = RegistryObject.create(new ResourceLocation(modid, name + "_flowing"), ForgeRegistries.FLUIDS);
        //his.blockRO = RegistryObject.create(new ResourceLocation(modid, name), ForgeRegistries.BLOCKS);
        this.bucketRO = RegistryObject.create(new ResourceLocation(modid, name + "_bucket"), ForgeRegistries.ITEMS);
    }

    public STILL getStillFluid() {
        return stillRO.get();
    }

    public FLOWING getFlowingFluid() {
        return flowingRO.get();
    }

    public BLOCK getBlock() {
        return blockRO.get();
    }

    public BUCKET getBucket() {
        return bucketRO.get();
    }

    //Make sure these update methods are package local as only the FluidDeferredRegister should be messing with them
    void updateStill(RegistryObject<STILL> stillRO) {
        this.stillRO = Objects.requireNonNull(stillRO);
    }

    void updateFlowing(RegistryObject<FLOWING> flowingRO) {
        this.flowingRO = Objects.requireNonNull(flowingRO);
    }

    void updateBlock(RegistryObject<BLOCK> blockRO) {
        this.blockRO = Objects.requireNonNull(blockRO);
    }

    void updateBucket(RegistryObject<BUCKET> bucketRO) {
        this.bucketRO = Objects.requireNonNull(bucketRO);
    }

    @Nonnull
    public STILL getFluid() {
        return this.getStillFluid();
    }
}

