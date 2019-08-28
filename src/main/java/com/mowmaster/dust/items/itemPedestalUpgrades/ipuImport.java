package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuImport extends ipuBasic
{
    public int transferRate = 0;

    public ipuImport(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if(stack.getItem().equals(ItemRegistry.importUpgrade))
        {
            return super.isBookEnchantable(stack, book);
        }

        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(MobEffects.SPEED,stack))
        {
            case 0:
                transferRate = 1;
                break;
            case 1:
                transferRate=4;
                break;
            case 2:
                transferRate = 8;
                break;
            case 3:
                transferRate = 16;
                break;
            case 4:
                transferRate = 32;
                break;
            case 5:
                transferRate=64;
                break;
            default: transferRate=1;
        }

        return  transferRate;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getTransferRate(stack);

        String tr = "" + s2 + "";
        tooltip.add("Item Stack Import Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
                tooltip.add("Transfer Rate: " + tr);
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
        }
    }

}
