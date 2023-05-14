package com.cmat.lasertech.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LTHelper {
    public static float roundToRightAngle(float angle) {
        angle = Mth.wrapDegrees(angle);
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

    public static HitResult getBlockLookingAt(Level worldIn, Player playerIn) {
        Vec3 eyepos = playerIn.getEyePosition(1.0f);
        Vec3 look = playerIn.getLookAngle();
        double d = 4;
        Vec3 end = eyepos.add(look.x * d, look.y * d, look.z * d);
        return worldIn.clip(new ClipContext(eyepos, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerIn));
    }
}
