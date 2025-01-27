package com.mowmaster.dust.DeferredRegistery.Client;

import com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlockEntityRenderer;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntityRenderer;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.DeferredRegistery.ItemModelPropertiesDust;
import com.mowmaster.dust.Items.Filters.FilterEnchantCount;
import com.mowmaster.dust.Items.Filters.FilterRestricted;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mowmaster.dust.References.Constants.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "dust", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry
{

    @SubscribeEvent
    public static void registerItemColor(ColorHandlerEvent.Item event) {

        event.getItemColors().register((stack, color) ->
         {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.COLOR_APPLICATOR.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.COLORED_CRYSTAL.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.COLORED_CRYSTAL_DUST.get());



        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_NODE.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get());

        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_BLOCK.get());

        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE_STAIR.get());


        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ITEM.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ITEMSTACK.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_DURABILITY.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ENCHANTED.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return FilterEnchantCount.getColor(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_FOOD.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_MOD.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return FilterRestricted.getColor(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_RESTRICTED.get());
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterItems.FILTER_TAG.get());



        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.PEDESTAL_UPGRADE_IMPORT.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.PEDESTAL_UPGRADE_EXPORT.get());

        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ITEM.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ITEMSTACK.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_DURABILITY.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ENCHANTED.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_FOOD.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_MOD.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_RESTRICTED.get());
        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.FILTER_TAG.get());

        ItemModelPropertiesDust.dustItemModes(DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get());



        /*
        *
        * TILE ENTITY BLOCKS HERE
        *
         */
        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get());

        event.getItemColors().register((stack, color) ->
        {if (color == 1) {return ColorReference.getColorFromItemStackInt(stack);} else {return -1;}}, DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get());
    }

    @SubscribeEvent
    public static void registerBlockColor(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_NODE.get());

        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get());
        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get());
        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get());
        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get());

        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_BLOCK.get());

        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE.get());
        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get());
        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_STONE_STAIR.get());


        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get());


        /*
         *
         * TILE ENTITY BLOCKS HERE
         *
         */

        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ColorReference.getColorFromStateInt(blockstate);} else {return -1;}}, DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get());


        event.getBlockColors().register((blockstate, blockReader, blockPos, color) ->
        {if (color == 1) {return ((blockReader.getBlockEntity(blockPos))!=null)?(ColorReference.getColorFromTag(((CustomPowderedBlockEntity)blockReader.getBlockEntity(blockPos)).getUpdateTag())):(ColorReference.DEFAULTCOLOR);} else {return -1;}}, DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get());

    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void textureStitchPreEvent(TextureStitchEvent.Pre event)
    {

        ResourceLocation location = event.getAtlas().location();

        if(location.equals(TextureAtlas.LOCATION_BLOCKS))
        {

            event.addSprite(new ResourceLocation(MODID, "util/whiteimage"));

            event.addSprite(new ResourceLocation(MODID, "util/whiteimage1"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage2"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage3"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage4"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage5"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage6"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage7"));
            event.addSprite(new ResourceLocation(MODID, "util/whiteimage8"));

        }

    }

    public static void registerBlockEntityRenderers()
    {
        BlockEntityRenderers.register(DeferredBlockEntityTypes.PEDESTAL.get(), BasePedestalBlockEntityRenderer::new);
        BlockEntityRenderers.register(DeferredBlockEntityTypes.CLUSTER.get(), EffectCrystalClusterBlockEntityRenderer::new);
    }
}
