package thut.bling.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderState.TextureState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thut.bling.ThutBling;
import thut.core.client.render.model.IModel;

public class Eye
{
    private static final RenderType TYPE = RenderType.makeType("thuttech:font", DefaultVertexFormats.POSITION_COLOR_TEX,
            7, 256, false, true, RenderType.State.getBuilder().texture(new TextureState(new ResourceLocation(
                    ThutBling.MODID, "textures/items/eye.png"), false, false)).transparency(
                            new RenderState.TransparencyState("translucent_transparency", () ->
                            {
                                RenderSystem.enableBlend();
                            }, () ->
                            {
                                RenderSystem.disableBlend();
                                RenderSystem.defaultBlendFunc();
                            })).writeMask(new RenderState.WriteMaskState(true, false)).build(false));

    public static void renderEye(final MatrixStack mat, final IRenderTypeBuffer buff, final LivingEntity wearer,
            final ItemStack stack, final IModel model, final ResourceLocation[] textures, final int brightness,
            final int overlay)
    {
        // TODO eye by model instead of texture.
        mat.push();
        mat.translate(-0.26, -0.175, -0.251);

        final double height = 0.5;
        final double width = 0.5;
        final IVertexBuilder vertexbuffer = buff.getBuffer(Eye.TYPE);
        vertexbuffer.pos(0.0D, height, 0.0D).color(255, 255, 255, 255).tex(0, 1).endVertex();
        vertexbuffer.pos(width, height, 0.0D).color(255, 255, 255, 255).tex(1, 1).endVertex();
        vertexbuffer.pos(width, 0.0D, 0.0D).color(255, 255, 255, 255).tex(1, 0).endVertex();
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, 255).tex(0, 0).endVertex();
        mat.pop();
    }
}
