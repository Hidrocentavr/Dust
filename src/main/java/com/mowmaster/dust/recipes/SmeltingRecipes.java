package com.mowmaster.dust.recipes;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmeltingRecipes
{
    public static void init()
    {
        GameRegistry.addSmelting(BlockRegistry.logred, new ItemStack(ItemRegistry.dustyCharcoal,1,0),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logblue, new ItemStack(ItemRegistry.dustyCharcoal,1,1),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logyellow, new ItemStack(ItemRegistry.dustyCharcoal,1,2),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logpurple, new ItemStack(ItemRegistry.dustyCharcoal,1,3),0.7f);
        GameRegistry.addSmelting(BlockRegistry.loggreen, new ItemStack(ItemRegistry.dustyCharcoal,1,4),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logorange, new ItemStack(ItemRegistry.dustyCharcoal,1,5),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logwhite, new ItemStack(ItemRegistry.dustyCharcoal,1,6),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logblack, new ItemStack(ItemRegistry.dustyCharcoal,1,7),0.7f);
    }
}