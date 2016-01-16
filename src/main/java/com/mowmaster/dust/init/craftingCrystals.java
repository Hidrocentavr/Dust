package com.mowmaster.dust.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class craftingCrystals {

    public static void init(){

// Recipies for the Crystals, Will be removed when Ore Gen is a thing... Maybe #Config?
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalRed, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalBlue, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalYellow, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 11));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalGreen, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalOrange, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 14));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalPurple, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 5));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalBlack, 1), new ItemStack(Items.diamond), new ItemStack(Items.dye, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalWhite, 1), new ItemStack(Items.diamond), new ItemStack(Items.dye, 1, 15));

// Recipies for Swords and Tools
// Swords
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordRed, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalRed, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordBlue, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalBlue, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordYellow, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalYellow, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordGreen, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalGreen, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordOrange, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalOrange, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordPurple, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalPurple, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordBlack, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalBlack, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalSwordWhite, 1), new Object[]{" X "," X "," Y ", 'X', ModItems.ItemCrystalWhite, 'Y', Items.stick});
// Axes Made Left
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeRed, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalRed, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeBlue, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalBlue, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeYellow, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalYellow, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeGreen, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalGreen, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeOrange, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalOrange, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxePurple, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalPurple, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeBlack, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalBlack, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeWhite, 1), new Object[]{"XX ","XY "," Y ", 'X', ModItems.ItemCrystalWhite, 'Y', Items.stick});
// Made Right
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeRed, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalRed, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeBlue, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalBlue, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeYellow, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalYellow, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeGreen, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalGreen, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeOrange, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalOrange, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxePurple, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalPurple, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeBlack, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalBlack, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalAxeWhite, 1), new Object[]{" XX"," YX"," Y ", 'X', ModItems.ItemCrystalWhite, 'Y', Items.stick});
// Pickaxes
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeRed, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalRed, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeBlue, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalBlue, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeYellow, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalYellow, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeGreen, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalGreen, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeOrange, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalOrange, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxePurple, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalPurple, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeBlack, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalBlack, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalPickaxeWhite, 1), new Object[]{"XXX"," Y "," Y ", 'X', ModItems.ItemCrystalWhite, 'Y', Items.stick});
// "Shovels"
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelRed, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalRed, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelBlue, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalBlue, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelYellow, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalYellow, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelGreen, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalGreen, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelOrange, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalOrange, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelPurple, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalPurple, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelBlack, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalBlack, 'Y', Items.stick});
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.crystalShovelWhite, 1), new Object[]{" X "," Y "," Y ", 'X', ModItems.ItemCrystalWhite, 'Y', Items.stick});



    }
}