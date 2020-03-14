package com.mowmaster.dust;

import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.client.ClientDust;
import com.mowmaster.dust.configtabs.CreativeTab;
import com.mowmaster.dust.crafting.Recipes;
import com.mowmaster.dust.item.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;


@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
@Mod(Reference.MODID)
public class dust
{

    public static final ItemGroup ITEM_GROUP = new CreativeTab();

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        ItemRegistry.onItemRegistryReady(event);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        BlockDustStone.onBlockRegistryReady(event);
        BlockPedestal.onBlockRegistryReady(event);
    }

    @SubscribeEvent
    public static void onTileRegistryReady(RegistryEvent.Register<TileEntityType<?>> event)
    {
        TilePedestal.onTileEntityRegistry(event);
    }

    @SubscribeEvent
    public static void onBlockColorsReady(ColorHandlerEvent.Block event)
    {
        BlockDustStone.handleBlockColors(event);
    }

    @SubscribeEvent
    public static void onItemColorsReady(ColorHandlerEvent.Item event)
    {
        ItemRegistry.onItemColorsReady(event);
    }

    @SubscribeEvent
    public static void onDataGatheredReady(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(new Recipes(gen));

    }


}