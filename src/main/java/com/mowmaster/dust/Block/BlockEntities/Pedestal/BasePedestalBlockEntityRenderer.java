package com.mowmaster.dust.Block.BlockEntities.Pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlock.FACING;
import static com.mowmaster.dust.References.Constants.MODID;

public class BasePedestalBlockEntityRenderer implements BlockEntityRenderer<BasePedestalBlockEntity>
{

    public BasePedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BasePedestalBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (!p_112307_.isRemoved()) {
            Direction facing = p_112307_.getBlockState().getValue(FACING);
            ItemStack stack = p_112307_.getItemInPedestal();
            ItemStack coin = p_112307_.getCoinOnPedestal();
            Level world = p_112307_.getLevel();
            // 0 - No Particles
            // 1 - No Render Item
            // 2 - No Render Upgrade
            // 3 - No Particles/No Render Item
            // 4 - No Particles/No Render Upgrade
            // 5 - No Render Item/No Render Upgrade
            // 6 - No Particles/No Render Item/No Render Upgrade
            // 7 - No Augment exists and thus all rendering is fine.
            int renderAugmentType = p_112307_.getRendererType();

            if(renderAugmentType !=6)
            {
                if(facing== Direction.UP)//when placed on ground
                {
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.DOWN) {
                    //p_112309_.rotate(new Quaternion(0, 0, 1,180));
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(180));
                    p_112309_.translate(0, -1, 0);
                    p_112309_.translate(-1, 0, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.NORTH) {
                    //p_112309_.rotate(new Quaternion(1, 0, 0,270));
                    p_112309_.mulPose(Vector3f.XP.rotationDegrees(270));
                    p_112309_.translate(0, -1, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.EAST) {
                    //p_112309_.mulPose(270, 0, 0, 1);
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(270));
                    p_112309_.translate(-1, 0, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.SOUTH) {
                    //p_112309_.mulPose(90, 1, 0, 0);
                    p_112309_.mulPose(Vector3f.XP.rotationDegrees(90));
                    p_112309_.translate(0, 0, -1);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.WEST) {
                    //p_112309_.mulPose(90, 0, 0, 1);
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(90));
                    p_112309_.translate(0, -1, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
            }

            if(p_112307_.getRenderRange())
            {
                int range = p_112307_.getLinkingRange();
                BlockPos pos = p_112307_.getPos();
                //You have to client register this too!!!
                @SuppressWarnings("deprecation")
                TextureAtlasSprite whiteTextureSprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite1 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage1"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite2 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage2"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite3 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage3"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite4 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage4"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite5 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage5"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite6 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage6"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite7 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage7"));
                @SuppressWarnings("deprecation")
                TextureAtlasSprite TextureSprite8 = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(MODID, "util/whiteimage8"));


                AABB aabb = new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range,pos.getX() + range, pos.getY() + range, pos.getZ() + range);
                renderBoundingBox(pos, aabb, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.2f, 0.2f, 1f);
                renderFaces(whiteTextureSprite,pos,aabb,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.2f, 0.2f, 0.5f);

                //Orange Color = 1f, 0.42f, 0f,
                //Light Blue Color = 0f, 0.58f, 1f,
                int locSize = p_112307_.getNumberOfStoredLocations();
                if(locSize>0)
                {
                    List<BlockPos> locations = p_112307_.getLocationList();
                    if(p_112307_.getNumberOfStoredLocations()>0)
                    {
                        for(int i=0;i<locations.size();i++)
                        {
                            AABB aabbl = new AABB(locations.get(i));
                            renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                            //renderFaces(whiteTextureSprite,pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                        }
                    }
                }

                /*if(1<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(0));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite1, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(2<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(1));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite2, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(3<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(2));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite3, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(4<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(3));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite4, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(5<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(4));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite5, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(6<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(5));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite6, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(7<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(6));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite7, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(8<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(7));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite8, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }
                if(9<=locSize)
                {
                    AABB aabbl = new AABB(locations.get(8));
                    renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                    renderFaces(TextureSprite8, pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                }*/

                /*if(p_112307_.getNumberOfStoredSenderLocations()>0)
                {
                    List<BlockPos> locations = p_112307_.getLocationSenderList();
                    for(int i=0;i<locations.size();i++)
                    {
                        AABB aabbl = new AABB(locations.get(i));
                        renderBoundingBox(pos, aabbl, p_112309_, p_112310_.getBuffer(RenderType.lines()), p_112307_, 1f, 0.42f, 0f, 1f);
                        renderFaces(pos,aabbl,p_112309_, p_112310_.getBuffer(Sheets.translucentCullBlockSheet()), p_112307_, 1f, 0.42f, 0f, 0.5f);
                    }
                }*/

            }
        }
    }
    //public static void  renderTile(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack coin, ItemStack item, int p_112311_, int p_112312_, int renderAugmentType)
    public static void  renderTile(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack item, ItemStack coin, int p_112311_, int p_112312_, int renderAugmentType)
    {
        switch (renderAugmentType)
        {
            case 0:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
            break;
            case 1:
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            case 2:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                break;
            case 3:
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            case 4:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                break;
            case 5: break;
            case 6: break;
            case 7:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            default:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;

        }
    }

    public static void renderItemRotating(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_)
    {
        if (!itemStack.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(0.5, 1.0, 0.5);
            //p_112309_.translate(0, MathHelper.sin((worldIn.getGameTime()) / 10.0F) * 0.1 + 0.1, 0); BOBBING ITEM
            p_112309_.scale(0.75F, 0.75F, 0.75F);
            long time = System.currentTimeMillis();
            float angle = (time/25) % 360;
            //float angle = (worldIn.getGameTime()) / 20.0F * (180F / (float) Math.PI);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel baked = renderer.getModel(itemStack,worldIn,null,0);
            renderer.render(itemStack, ItemTransforms.TransformType.GROUND,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);

            //Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND, p_112311_, p_112312_, p_112309_, p_112310_);
            p_112309_.popPose();
        }
    }
    public static void renderCoin(Level worldIn,ItemStack itemCoin, PoseStack p_112309_, MultiBufferSource p_112310_, float x, float y, float z, float angle, float xr, float yr, float zr, int p_112311_, int p_112312_) {
        if (!itemCoin.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(x, y, z);
            p_112309_.scale(0.1875f, 0.1875f, 0.1875f);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel baked = renderer.getModel(itemCoin,worldIn,null,0);
            renderer.render(itemCoin,ItemTransforms.TransformType.FIXED,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);
            //Minecraft.getInstance().getItemRenderer().renderItem(itemCoin, ItemCameraTransforms.TransformType.FIXED, p_112311_, p_112312_, p_112309_, p_112310_);
            p_112309_.popPose();
        }
    }




    //https://github.com/StanCEmpire/RegionProtection/blob/1.18.x/src/main/java/stancempire/stancempiresregionprotection/blockentities/RegionBlockBER.java
    public void renderBoundingBox(BlockPos pos, AABB aabb, PoseStack matrixStack, VertexConsumer buffer, BasePedestalBlockEntity blockEntity, float red, float green, float blue, float alpha)
    {
        Matrix4f matrix4f = matrixStack.last().pose();
        Matrix3f matrix3f = matrixStack.last().normal();

        float minX = (float)(aabb.minX - pos.getX());
        float minY = (float)(aabb.minY - pos.getY());
        float minZ = (float)(aabb.minZ - pos.getZ());

        float maxX = (float)(aabb.maxX - pos.getX());
        float maxY = (float)(aabb.maxY - pos.getY());
        float maxZ = (float)(aabb.maxZ - pos.getZ());

        //Bottom
        buffer.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();

        buffer.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();

        buffer.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();
        buffer.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();

        buffer.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, -1, 0).endVertex();

        //Top
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();

        buffer.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();

        buffer.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();

        buffer.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 1, 0).endVertex();

        //Sides
        buffer.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, -1).endVertex();
        buffer.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, -1).endVertex();

        buffer.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 1).endVertex();

        buffer.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 1).endVertex();
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 1).endVertex();

        buffer.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, -1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, -1).endVertex();

    }

    public void renderFaces(TextureAtlasSprite sprite, BlockPos pos, AABB aabb, PoseStack matrixStack, VertexConsumer buffer, BasePedestalBlockEntity blockEntity, float red, float green, float blue, float alpha)
    {

        Matrix4f matrix4f = matrixStack.last().pose();

        //new ResourceLocation(MODID, "textures/util/whiteimage.png") --- Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply()

        float minX = (float)(aabb.minX - pos.getX());
        float minY = (float)(aabb.minY - pos.getY());
        float minZ = (float)(aabb.minZ - pos.getZ());


        float maxX = (float)(aabb.maxX - pos.getX());
        float maxY = (float)(aabb.maxY - pos.getY());
        float maxZ = (float)(aabb.maxZ - pos.getZ());


        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        int uvBrightness = 240;

        //West
        buffer.vertex(matrix4f, minX - 0.01f, minY, maxZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX - 0.01f, maxY, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX - 0.01f, maxY, minZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX - 0.01f, minY, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();

        buffer.vertex(matrix4f, minX + 0.01f, minY, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX + 0.01f, maxY, minZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX + 0.01f, maxY, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, minX + 0.01f, minY, maxZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();


        //East
        buffer.vertex(matrix4f, maxX + 0.01f, minY, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX + 0.01f, maxY, minZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX + 0.01f, maxY, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX + 0.01f, minY, maxZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(1, 0, 0).endVertex();

        buffer.vertex(matrix4f, maxX - 0.01f, minY, maxZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX - 0.01f, maxY, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX - 0.01f, maxY, minZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix4f, maxX - 0.01f, minY, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(-1, 0, 0).endVertex();


        //North
        buffer.vertex(matrix4f, minX, maxY, minZ - 0.01f).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, minZ - 0.01f).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, maxX, minY, minZ - 0.01f).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, minX, minY, minZ - 0.01f).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();

        buffer.vertex(matrix4f, minX, minY, minZ + 0.01f).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, maxX, minY, minZ + 0.01f).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, minZ + 0.01f).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, minX, maxY, minZ + 0.01f).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();


        //South
        buffer.vertex(matrix4f, minX, minY, maxZ + 0.01f).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, maxX, minY, maxZ + 0.01f).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, maxZ + 0.01f).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix4f, minX, maxY, maxZ + 0.01f).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, 1).endVertex();

        buffer.vertex(matrix4f, minX, maxY, maxZ - 0.01f).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, maxX, maxY, maxZ - 0.01f).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, maxX, minY, maxZ - 0.01f).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix4f, minX, minY, maxZ - 0.01f).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 0, -1).endVertex();


        //Bottom
        buffer.vertex(matrix4f, maxX, minY - 0.01f, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY - 0.01f, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, minX, minY - 0.01f, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, minX, minY - 0.01f, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();

        buffer.vertex(matrix4f, minX, minY + 0.01f, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, minX, minY + 0.01f, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY + 0.01f, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, minY + 0.01f, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();


        //Top
        buffer.vertex(matrix4f, minX, maxY + 0.01f, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, minX, maxY + 0.01f, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY + 0.01f, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY + 0.01f, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, 1, 0).endVertex();

        buffer.vertex(matrix4f, maxX, maxY - 0.01f, minZ).color(red, green, blue, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, maxX, maxY - 0.01f, maxZ).color(red, green, blue, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, minX, maxY - 0.01f, maxZ).color(red, green, blue, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
        buffer.vertex(matrix4f, minX, maxY - 0.01f, minZ).color(red, green, blue, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uvBrightness).normal(0, -1, 0).endVertex();
    }

    @Override
    public boolean shouldRenderOffScreen(BasePedestalBlockEntity p_112306_) {
        return true;
    }


}
