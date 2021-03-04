package com.cmat.lasertech.client.render.entity;

import com.cmat.lasertech.LaserTech;
import com.cmat.lasertech.entity.BaseLaserProjectile;
import com.cmat.lasertech.util.Strings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderBaseLaserProjectile extends EntityRenderer<BaseLaserProjectile> {
    public static final IRenderFactory<BaseLaserProjectile> FACTORY = RenderBaseLaserProjectile::new;

    private RenderBaseLaserProjectile(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(BaseLaserProjectile entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(45.0F));

        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutout(this.getEntityTexture(entityIn)));
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        Matrix3f matrix3f = matrixStackIn.getLast().getNormal();
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);

        for (int j = 0; j < 4; ++j) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            vertex(matrix4f, matrix3f, builder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }

        matrixStackIn.pop();
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder builder, float x, float y, float z, float u, float v, float nx, float ny, float nz, int lightmap) {
        builder.pos(matrix4f, x, y, z)
                .color(255, 255, 255, 255)
                .tex(u, v).overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(lightmap)
                .normal(matrix3f, nx, nz, ny)
                .endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(BaseLaserProjectile entity) {
        return new ResourceLocation(Strings.ModID, "textures/entity/baselaserprojectile.png");
    }
}
