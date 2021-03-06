package com.cmat.lasertech.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class LTHelper {
    public static float roundToRightAngle(float angle) {
        angle = MathHelper.wrapDegrees(angle);
        float ang = 0;
        float smallest = Math.abs(angle);
        ang = (smallest < Math.abs(angle - 90)) ? ang : 90;
        smallest = (smallest < Math.abs(angle - 90)) ? smallest : Math.abs(angle - 90);
        ang = (smallest < Math.abs(angle - 180)) ? ang : 180;
        smallest = (smallest < Math.abs(angle - 180)) ? smallest : Math.abs(angle - 180);
        ang = (smallest < Math.abs(angle + 90)) ? ang : -90;
        smallest = (smallest < Math.abs(angle + 90)) ? smallest : Math.abs(angle + 90);
        ang = (smallest < Math.abs(angle + 180)) ? ang : -180;
        return ang;
    }

    public static RayTraceResult getBlockLookingAt(World worldIn, PlayerEntity playerIn) {
        Vector3d eyepos = playerIn.getEyePosition(1.0f);
        Vector3d look = playerIn.getLook(1.0f);
        double d = 4;
        Vector3d end = eyepos.add(look.x * d, look.y * d, look.z * d);
        RayTraceResult raytraceresult = worldIn.rayTraceBlocks(new RayTraceContext(eyepos, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, playerIn));
        return raytraceresult;
    }
}
