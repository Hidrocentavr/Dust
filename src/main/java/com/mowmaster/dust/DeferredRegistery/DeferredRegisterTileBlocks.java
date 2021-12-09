package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlock;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;
import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class DeferredRegisterTileBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);


    public static final RegistryObject<Block> BLOCK_PEDESTAL = registerBlock("block_pedestal",
            () -> new BasePedestalBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_POWDERED_DUST = registerPowderedBlock("block_powdered_dust", () -> new CustomPowderedBlock(BlockBehaviour.Properties.of(Material.SAND).strength(0.25F).sound(SoundType.SAND)));






    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }


    private static <T extends Block> RegistryObject<T> registerPowderedBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerCustomPowderedItemBlock(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerCustomPowderedItemBlock(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new CustomPowderedBlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
