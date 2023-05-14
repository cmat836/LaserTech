package com.cmat.lasertech.client.render.entity;

import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.util.Strings;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderBaseLaserProjectile extends EntityRenderer<BaseLaserProjectile> {
    public static final EntityRendererProvider<BaseLaserProjectile> FACTORY = RenderBaseLaserProjectile::new;

    private RenderBaseLaserProjectile(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(BaseLaserProjectile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();

        // Rotations
        matrixStackIn.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F, true));
        matrixStackIn.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F), Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) - 180.0F, true));
        matrixStackIn.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), 0, true));

        // Scale
        float scaleFactor = 0.5F;
        matrixStackIn.scale(0.05625F * scaleFactor, 0.05625F * scaleFactor, 0.05625F * scaleFactor);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);

        // Texture
        VertexConsumer builder = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        Matrix4f matrix4f = matrixStackIn.last().pose();
        Matrix3f matrix3f = matrixStackIn.last().normal();
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);

        for (int j = 0; j < 4; ++j) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            vertex(matrix4f, matrix3f, builder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }

        matrixStackIn.popPose();
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, float x, float y, float z, float u, float v, float nx, float ny, float nz, int lightmap) {
        builder.vertex(matrix4f, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightmap)
                .normal(matrix3f, nx, nz, ny)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BaseLaserProjectile entity) {
        return new ResourceLocation(Strings.ModID, "textures/entity/baselaserprojectile.png");
    }
}
