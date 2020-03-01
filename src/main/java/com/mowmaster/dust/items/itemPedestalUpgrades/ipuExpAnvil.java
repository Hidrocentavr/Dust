package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.google.common.collect.Maps;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpAnvil extends ipuBasicExpUpgrade
{
    public ipuExpAnvil(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getExpBuffer(ItemStack stack)
    {
        int value = 0;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 30;//30
                break;
            case 1:
                value=44;//44
                break;
            case 2:
                value = 58;//58
                break;
            case 3:
                value = 72;//72
                break;
            case 4:
                value = 86;//86
                break;
            case 5:
                value=100;//100
                break;
            default: value=30;
        }

        return  value;
    }

    public int getRepairRate(ItemStack stack)
    {
        int value = 0;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                    value = 5;//10 durability/ operation
                break;
            case 1:
                value=10;//20
                break;
            case 2:
                value = 15;//30
                break;
            case 3:
                value = 20;//40
                break;
            case 4:
                value = 25;//50
                break;
            case 5:
                value= 50;//100
                break;
            default: value=1;
        }

        return  value;
    }

    private ArrayList<ItemStack> doSorroundingPedestalsHaveItemsToCombine(World world, BlockPos pedestalPos)
    {
        ArrayList<ItemStack> stackOnSorroundingPedestal = new ArrayList<ItemStack>();

        ArrayList<BlockPos> posSorroundingPedestals = new ArrayList<BlockPos>();
        posSorroundingPedestals.add(pedestalPos.add(2,0,0));
        posSorroundingPedestals.add(pedestalPos.add(0,0,2));
        posSorroundingPedestals.add(pedestalPos.add(-2,0,0));
        posSorroundingPedestals.add(pedestalPos.add(0,0,-2));

        for(int i=0; i< posSorroundingPedestals.size();i++)
        {
            if(!world.getBlockState(posSorroundingPedestals.get(i)).getBlock().equals(Blocks.AIR))
            {
                TileEntity tile = world.getTileEntity(posSorroundingPedestals.get(i));
                if(tile instanceof TilePedestal)
                {
                    TilePedestal tilePedestal = (TilePedestal)tile;
                    ItemStack stack = tilePedestal.getItemInPedestal();
                    if(stack.isItemEnchanted() || stack.getItem().equals(Items.ENCHANTED_BOOK) || stack.getItem().equals(Items.NAME_TAG) || stack.getItem().equals(ItemRegistry.crystal))
                    {
                        stackOnSorroundingPedestal.add(stack);
                    }
                }
            }
        }

        return stackOnSorroundingPedestal;
    }

    private void deleteItemsOnPedestals(World world, BlockPos pedestalPos, int crystals)
    {
        int crystalsToRemove = crystals;
        ArrayList<BlockPos> posSorroundingPedestals = new ArrayList<BlockPos>();
        posSorroundingPedestals.add(pedestalPos.add(2,0,0));
        posSorroundingPedestals.add(pedestalPos.add(0,0,2));
        posSorroundingPedestals.add(pedestalPos.add(-2,0,0));
        posSorroundingPedestals.add(pedestalPos.add(0,0,-2));

        for(int i=0; i< posSorroundingPedestals.size();i++)
        {
            if(!world.getBlockState(posSorroundingPedestals.get(i)).getBlock().equals(Blocks.AIR))
            {
                TileEntity tile = world.getTileEntity(posSorroundingPedestals.get(i));
                if(tile instanceof TilePedestal)
                {
                    TilePedestal tilePedestal = (TilePedestal)tile;
                    ItemStack stack = tilePedestal.getItemInPedestal();
                    if(stack.isItemEnchanted() || stack.getItem().equals(Items.ENCHANTED_BOOK) || stack.getItem().equals(Items.NAME_TAG))
                    {
                        tilePedestal.removeItem(1);
                    }
                    else if(stack.getItem().equals(ItemRegistry.crystal))
                    {
                        if(stack.getCount() >= crystalsToRemove)
                        {
                            tilePedestal.removeItem(crystalsToRemove);
                            crystalsToRemove = 0;
                        }
                        else
                        {
                            crystalsToRemove = crystalsToRemove - stack.getCount();
                            tilePedestal.removeItem();
                        }
                    }
                }
            }
        }
    }

    private int getCrystals(ArrayList<ItemStack> stackToCombine)
    {
        int crystals = 0;
        for(int i=0;i<stackToCombine.size();i++)
        {
            if(stackToCombine.get(i).getItem().equals(ItemRegistry.crystal))
            {
                crystals = crystals + stackToCombine.get(i).getCount();
            }
        }

        return crystals;
    }


    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, itemInPedestal, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int getMaxXpValue = getExpCountByLevel(getExpBuffer(coinInPedestal));
        setMaxXP(coinInPedestal, getMaxXpValue);
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        ItemStack itemFromInv = ItemStack.EMPTY;
        ArrayList<ItemStack> stackToCombine = doSorroundingPedestalsHaveItemsToCombine(world,posOfPedestal);
        String strNameToChangeTo = "";
        Map<Enchantment, Integer> enchantsMap = Maps.<Enchantment, Integer>newLinkedHashMap();
        int overCombine = getCrystals(stackToCombine);
        int overCombineCopy = overCombine;

        int intExpInCoin = getXPStored(coinInPedestal);
        int intRepairRate = getRepairRate(coinInPedestal);
        int intLevelCostToCombine = 0;

        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
                TileEntity invToPullFrom = world.getTileEntity(posInventory);
                if(invToPullFrom instanceof TilePedestal) {
                    itemFromInv = ItemStack.EMPTY;
                }
                else {
                    if(handler != null)
                    {
                        int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                        if(i>=0)
                        {
                            itemFromInv = handler.getStackInSlot(i);
                            int slotCount = itemFromInv.getCount();
                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                            if(pedestalInv instanceof TilePedestal) {
                                TilePedestal tilePedestal = (TilePedestal) pedestalInv;

                                if(!tilePedestal.hasItem())
                                {
                                    //Repair first if possible
                                    if(itemFromInv.isItemDamaged())
                                    {
                                        if(itemFromInv.getItem().isRepairable() && itemFromInv.getItem().getMaxDamage(itemFromInv) > 0)
                                        {
                                            if(intExpInCoin >= intRepairRate)
                                            {
                                                setXPStored(coinInPedestal,(intExpInCoin-intRepairRate));
                                                itemFromInv.setItemDamage(itemFromInv.getItemDamage() - (intRepairRate*2));
                                            }
                                        }
                                    }
                                    else if(stackToCombine.size() > 0)
                                    {
                                        //First check if other enchants exist, then we Need to add the item to enchantments list for combining
                                        stackToCombine.add(itemFromInv);

                                        for(int e=0;e<stackToCombine.size();e++)
                                        {
                                            if(stackToCombine.get(e).isItemEnchanted() || stackToCombine.get(e).getItem().equals(Items.ENCHANTED_BOOK))
                                            {

                                                NBTTagList nbttaglist = stackToCombine.get(e).getItem() == Items.ENCHANTED_BOOK ? ItemEnchantedBook.getEnchantments(stackToCombine.get(e)) : stackToCombine.get(e).getEnchantmentTagList();
                                                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                                                {
                                                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                                                    Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
                                                    int k = nbttagcompound.getShort("lvl");
                                                    switch(enchantment.getRarity())
                                                    {
                                                        case COMMON:
                                                            intLevelCostToCombine +=(1*(k+1));
                                                            break;
                                                        case UNCOMMON:
                                                            intLevelCostToCombine +=(2*(k+1));
                                                            break;
                                                        case RARE:
                                                            intLevelCostToCombine +=(4*(k+1));
                                                            break;
                                                        case VERY_RARE:
                                                            intLevelCostToCombine +=(6*(k+1));
                                                            break;
                                                    }

                                                    //Check if enchant already exists in list
                                                    if(enchantsMap.containsKey(enchantment))
                                                    {
                                                        //if it does then get the value of it
                                                        int intNewValue = 0;
                                                        int e1 = enchantsMap.get(enchantment).intValue();
                                                        int e2 = Integer.valueOf(k);
                                                        if(e1 == e2)
                                                        {
                                                            intNewValue = e2 + 1;
                                                            if(intNewValue > enchantment.getMaxLevel())
                                                            {
                                                                if(overCombine >= intNewValue)
                                                                {
                                                                    overCombine = overCombine - intNewValue;
                                                                }
                                                                else
                                                                {
                                                                    intNewValue = enchantment.getMaxLevel();
                                                                }
                                                            }

                                                            enchantsMap.put(enchantment, intNewValue);
                                                        }
                                                        else if(e1 > e2)
                                                        {
                                                            //if existing enchant is better then the one being applied, then skip just this one
                                                            continue;
                                                        }
                                                        else
                                                        {
                                                            enchantsMap.put(enchantment, Integer.valueOf(k));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        enchantsMap.put(enchantment, Integer.valueOf(k));
                                                    }
                                                }
                                            }
                                            else if(stackToCombine.get(e).getItem().equals(Items.NAME_TAG))
                                            {
                                                strNameToChangeTo = stackToCombine.get(e).getDisplayName();
                                            }

                                        }

                                        //System.out.println("Level To Combine: "+ intLevelCostToCombine);
                                        int intExpCostToCombine = getExpCountByLevel(intLevelCostToCombine);
                                        //System.out.println("XP To Combine: "+ intExpCostToCombine);
                                        if(intExpInCoin >= intExpCostToCombine)
                                        {
                                            ItemStack itemFromInvCopy = itemFromInv.copy();
                                            itemFromInvCopy.setCount(1);
                                            //Charge Exp Cost
                                            setXPStored(coinInPedestal,(intExpInCoin-intExpCostToCombine));
                                            //Delete Items On Pedestals
                                            int crystalsToRemove = overCombineCopy - overCombine;
                                            deleteItemsOnPedestals(world,posOfPedestal,crystalsToRemove);
                                            EnchantmentHelper.setEnchantments(enchantsMap,itemFromInvCopy);
                                            if(strNameToChangeTo != "")
                                            {
                                                itemFromInvCopy.setStackDisplayName(strNameToChangeTo);
                                            }
                                            handler.extractItem(i,itemFromInvCopy.getCount(),false);
                                            tilePedestal.addItem(itemFromInvCopy);
                                        }
                                    }
                                    else
                                    {
                                        ItemStack itemFromInvCopy = itemFromInv.copy();
                                        handler.extractItem(i,itemFromInvCopy.getCount(),false);
                                        tilePedestal.addItem(itemFromInvCopy);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String s5 = getOperationSpeedString(stack);
        int buffer = getExpBuffer(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";

        tooltip.add(TextFormatting.GOLD + "Anvil Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);
        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: " + buffer + " Levels");

        if(stack.hasTagCompound())
        {
            if(stack.isItemEnchanted())
            {
                if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
                {
                    tooltip.add(TextFormatting.RED + "Operational Speed: " + s5);
                }
                else
                {
                    tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
                }
            }
            else
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }



    @Override
    public void onRandomDisplayTick(TilePedestal pedestal, IBlockState stateIn, World world, BlockPos pos, Random rand)
    {
        if(!world.isBlockPowered(pos))
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (i > -2 && i < 2 && j == -1)
                    {
                        j = 2;
                    }

                    if (rand.nextInt(16) == 0)
                    {
                        for (int k = 0; k <= 2; ++k)
                        {
                            BlockPos blockpos = pos.add(i, k, j);

                            if (net.minecraftforge.common.ForgeHooks.getEnchantPower(world, blockpos) > 0)
                            {
                                if (!world.isAirBlock(pos.add(i / 2, 0, j / 2)))
                                {
                                    break;
                                }

                                world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D);
                            }
                        }
                    }
                }
            }
        }
    }

}
