package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.blocks.machines.BlockCrystalCrusher;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileCrystalCrusher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;


public class RenderTileCrystalCrusher extends TileEntitySpecialRenderer<TileCrystalCrusher>
{
    @Override
    public void render(TileCrystalCrusher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te==null)
        {
            return;
        }

        else if(te.getBlockType() instanceof BlockCrystalCrusher)
        {
            ItemStack crusherComponents = new ItemStack(ItemRegistry.crushingComponents);
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            if(te.isBurning)
            {
                renderItemRotating(itemRenderer,crusherComponents,0.40f,0.48f,0.55f);
                renderItemRotating(itemRenderer,crusherComponents,0.60f,0.48f,0.45f);
            }
            else
            {
                renderItem(itemRenderer,crusherComponents,0.40f,0.48f,0.55f,0,0,0,0);
                renderItem(itemRenderer,crusherComponents,0.60f,0.48f,0.45f,0,0,0,0);
            }


            GlStateManager.popMatrix();
        }

    }

    public static void renderItemRotating(RenderItem itemRenderer, ItemStack itemStack, float x,float y,float z)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(1f,1f,1f);
        double boop = Minecraft.getSystemTime()/800D;
        GlStateManager.translate(0D, 0D, 0D);
        GlStateManager.rotate((float)(((boop*40D)%360)), 0f, 0f, 1f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }

        GlStateManager.popMatrix();
    }

    public static void renderItemRotatingOpaque(RenderItem itemRenderer, ItemStack itemStack, float x,float y,float z)
    {
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(1f,1f,1f);
        double boop = Minecraft.getSystemTime()/800D;
        GlStateManager.translate(0D, 0D, 0D);
        GlStateManager.rotate((float)(((boop*40D)%360)), 0f, 0f, 1f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(0.5f,0.5f,0.5f);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}