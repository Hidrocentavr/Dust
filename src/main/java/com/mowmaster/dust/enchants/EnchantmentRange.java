package com.mowmaster.dust.enchants;

import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantmentRange extends Enchantment
{
    public EnchantmentRange() {
        super(Rarity.COMMON, EnchantmentRegistry.COINUPGRADE, new EquipmentSlotType[]{
                EquipmentSlotType.OFFHAND
        });
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 11;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canApply(ItemStack stack) {
        boolean canApplyToUpgrade = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApplyToUpgrade = ((ItemUpgradeBase) coin).canAcceptRange();
        }
        return stack.getItem() instanceof ItemUpgradeBase && canApplyToUpgrade;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        boolean canApplyToUpgrade = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApplyToUpgrade = ((ItemUpgradeBase) coin).canAcceptRange();
        }

        return stack.getItem() instanceof ItemUpgradeBase && canApplyToUpgrade;
    }

    public boolean isAllowedOnBooks() {
        return true;
    }
}

