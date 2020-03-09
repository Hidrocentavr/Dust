package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class ItemUpgradeBase extends Item {

    public ItemUpgradeBase(Properties builder) {super(builder.group(dust.itemGroup));}

    public boolean isFilter;

    public boolean hasEnchant;

    public boolean hasEffect;

    public int intRate = 0;


    public boolean hasEnchant(ItemStack stack)
    {
        hasEnchant =  stack.isEnchanted();
        return hasEnchant;
    }

    public int getRangeModifier(ItemStack stack)
    {
        int range = 0;
        /*if(stack.isEnchanted())
        {
            range = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,stack);
        }*/
        return range;
    }

    /*public int intOperationalSpeedModifier(ItemStack stack)
    {
        int rate = 0;
        if(stack.isItemEnchanted())
        {
            rate = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentTransferRate,stack);
        }
        return rate;
    }*/

    public void onRandomDisplayTick(TilePedestal pedestal, BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {

    }

    /*
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public PotionEffect getPotionEffectFromStack(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
        }
        return potionEffect;
    }
     */

    public int getOperationSpeed(ItemStack stack)
    {
        int intOperationalSpeed = 20;
        /*switch (intOperationalSpeedModifier(stack))
        {
            case 0:
                intOperationalSpeed = 20;//normal speed
                break;
            case 1:
                intOperationalSpeed=10;//2x faster
                break;
            case 2:
                intOperationalSpeed = 5;//4x faster
                break;
            case 3:
                intOperationalSpeed = 3;//6x faster
                break;
            case 4:
                intOperationalSpeed = 2;//10x faster
                break;
            case 5:
                intOperationalSpeed=1;//20x faster
                break;
            default: intOperationalSpeed=20;
        }*/

        return  intOperationalSpeed;
    }

    public String getOperationSpeedString(ItemStack stack)
    {
        String str = "Normal Speed";
        /*switch (intOperationalSpeedModifier(stack))
        {
            case 0:
                str = "Normal Speed";//normal speed
                break;
            case 1:
                str = "2x Faster";//2x faster
                break;
            case 2:
                str = "4x Faster";//4x faster
                break;
            case 3:
                str = "6x Faster";//6x faster
                break;
            case 4:
                str = "10x Faster";//10x faster
                break;
            case 5:
                str = "20x Faster";//20x faster
                break;
            default: str = "Normal Speed";;
        }*/

        return  str;
    }


    public ItemStack getStackInPedestal(World world, BlockPos posOfPedestal)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            stackInPedestal = ((TilePedestal) pedestalInventory).getItemInPedestal();
        }

        return stackInPedestal;
    }

    public void removeFromPedestal(World world, BlockPos posOfPedestal,int count)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            ((TilePedestal) pedestalInventory).removeItem(count);
        }
    }

    public void addToPedestal(World world, BlockPos posOfPedestal,ItemStack itemStackToAdd)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            ((TilePedestal) pedestalInventory).addItem(itemStackToAdd);
        }
    }

    /*public void addExpToPedestal(World world, BlockPos posOfPedestal,int expToAdd)
    {
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            ((TilePedestal) pedestalInventory).addExpToPedestal(expToAdd);
        }
    }
    public void removeExpFromPedestal(World world, BlockPos posOfPedestal,int expToRemove)
    {
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            ((TilePedestal) pedestalInventory).removeExpFromPedestal(expToRemove);
        }
    }*/

    public void setIntValueToPedestal(World world, BlockPos posOfPedestal,int value)
    {
        TileEntity pedestal = world.getTileEntity(posOfPedestal);
        if(pedestal instanceof TilePedestal) {
            ((TilePedestal) pedestal).setStoredValueForUpgrades(value);
        }
    }

    public int getIntValueFromPedestal(World world, BlockPos posOfPedestal)
    {
        int value = 0;
        TileEntity pedestal = world.getTileEntity(posOfPedestal);
        if(pedestal instanceof TilePedestal) {
            value = ((TilePedestal) pedestal).getStoredValueForUpgrades();
        }

        return value;
    }

    public int intSpaceLeftInStack (ItemStack stack)
    {
        int value = 0;
        if(stack.equals(ItemStack.EMPTY))
        {
            value = 64;
        }
        else
        {
            int maxSize = stack.getMaxStackSize();
            int currentSize = stack.getCount();
            value = maxSize-currentSize;
        }


        return value;
    }

    public boolean doItemsMatch(ItemStack stackPedestal, ItemStack itemStackIn)
    {
        if(!stackPedestal.equals(ItemStack.EMPTY))
        {
            if(itemStackIn.hasTag())
            {
                CompoundNBT itemIn = itemStackIn.getTag();
                CompoundNBT itemStored = stackPedestal.getTag();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(stackPedestal.getItem()))
                {
                    return true;
                }
                else return false;
            }
            /*else if(itemStackIn.isDamaged())
            {
                if(itemStackIn.isDamaged()==stackPedestal.getDamage() && itemStackIn.getMetadata()==stackPedestal.getMetadata())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }*/
            else
            {
                if(itemStackIn.getItem().equals(stackPedestal.getItem()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    //For Filters to return if they can or cannot allow items to pass
    //Will probably need overwritten
    public boolean canAcceptItem(World world, BlockPos posPedestal, ItemStack itemStackIn)
    {
        return false;
    }

    //Mainly Used in the Import, Furnace, and  Milker Upgrades
    /*
        This Method gets the next slot with items in the given tile
     */
    public int getNextSlotWithItems(TileEntity invBeingChecked, Direction sideSlot, ItemStack stackInPedestal)
    {
        int slot = -1;
        if(invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,sideSlot ).isPresent()) {
            IItemHandler handler = (IItemHandler) invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot).orElse(null);
            int range = handler.getSlots();
            for(int i=0;i<range;i++)
            {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                //find a slot with items
                if(!stackInSlot.isEmpty())
                {
                    //check if it could pull the item out or not
                    if(!handler.extractItem(i,1 ,true ).equals(ItemStack.EMPTY))
                    {
                        //If pedestal is empty accept any items
                        if(stackInPedestal.isEmpty())
                        {
                            slot=i;
                            break;
                        }
                        //if stack in pedestal matches items in slot
                        else if(doItemsMatch(stackInPedestal,stackInSlot))
                        {
                            slot=i;
                            break;
                        }
                    }
                }
            }
        }

        return slot;
    }


    /**
     * Can this hopper insert the specified item from the specified slot on the specified side?
     */
    public static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, Direction side)
    {
        if (!inventoryIn.isItemValidForSlot(index, stack))
        {
            return false;
        }
        else
        {
            return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
        }
    }

    /**
     * Can this hopper extract the specified item from the specified slot on the specified side?
     */
    public static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, Direction side)
    {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }

    public int[] getSlotsForSide(World world, BlockPos posOfPedestal, IInventory inventory)
    {
        int[] slots = new int[]{};

        if(inventory instanceof ISidedInventory)
        {
            slots= ((ISidedInventory) inventory).getSlotsForFace(getPedestalFacing(world, posOfPedestal));
        }

        return slots;
    }


    /*public boolean canInsertIntoSide(World world, BlockPos posOfPedestal, IInventory inventory, ItemStack itemFromPedestal, int slot)
    {
        boolean value = false;
        if(inventory instanceof ISidedInventory)
        {
            int[] slots= ((ISidedInventory) inventory).getSlotsForFace(getPedestalFacing(world, posOfPedestal));
            for(int i=0;i<slots.length;i++)
            {
                if(canInsertItemInSlot(inventory,itemFromPedestal,slots[i],getPedestalFacing(world, posOfPedestal)))
                {
                    value=true;
                }
                else
                {
                    value=false;
                    break;
                }
            }
        }
        else
        {
            if(canInsertItemInSlot(inventory,itemFromPedestal,slot,getPedestalFacing(world, posOfPedestal))) value=true;
        }
        return value;
    }*/

    public BlockPos getPosOfBlockBelow(World world,BlockPos posOfPedestal, int numBelow)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.get(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(0,-numBelow,0);
            case DOWN:
                return blockBelow.add(0,numBelow,0);
            case NORTH:
                return blockBelow.add(0,0,numBelow);
            case SOUTH:
                return blockBelow.add(0,0,-numBelow);
            case EAST:
                return blockBelow.add(-numBelow,0,0);
            case WEST:
                return blockBelow.add(numBelow,0,0);
            default:
                return blockBelow;
        }
    }



    public BlockPos getNegRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.get(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(-intWidth,0,-intWidth);
            case DOWN:
                return blockBelow.add(-intWidth,-intHeight,-intWidth);
            case NORTH:
                return blockBelow.add(-intWidth,-intWidth,-intHeight);
            case SOUTH:
                return blockBelow.add(-intWidth,-intWidth,0);
            case EAST:
                return blockBelow.add(0,-intWidth,-intWidth);
            case WEST:
                return blockBelow.add(-intHeight,-intWidth,-intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.get(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(intWidth,intHeight,intWidth);
            case DOWN:
                return blockBelow.add(intWidth,0,intWidth);
            case NORTH:
                return blockBelow.add(intWidth,intWidth,0);
            case SOUTH:
                return blockBelow.add(intWidth,intWidth,intHeight);
            case EAST:
                return blockBelow.add(intHeight,intWidth,intWidth);
            case WEST:
                return blockBelow.add(0,intWidth,intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getNegRangePosEntity(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.get(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(-intWidth,0,-intWidth);
            case DOWN:
                return blockBelow.add(-intWidth,-intHeight,-intWidth);
            case NORTH:
                return blockBelow.add(-intWidth,-intWidth,-intHeight);
            case SOUTH:
                return blockBelow.add(-intWidth,-intWidth,0);
            case EAST:
                return blockBelow.add(0,-intWidth,-intWidth);
            case WEST:
                return blockBelow.add(-intHeight,-intWidth,-intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosRangePosEntity(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.get(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(intWidth+1,intHeight,intWidth+1);
            case DOWN:
                return blockBelow.add(intWidth+1,0,intWidth+1);
            case NORTH:
                return blockBelow.add(intWidth+1,intWidth,0+1);
            case SOUTH:
                return blockBelow.add(intWidth+1,intWidth,intHeight+1);
            case EAST:
                return blockBelow.add(intHeight+1,intWidth,intWidth+1);
            case WEST:
                return blockBelow.add(0+1,intWidth,intWidth+1);
            default:
                return blockBelow;
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        /*return !EnchantmentRegistry.UPGRADES.equals(enchantment.type) && super.canApplyAtEnchantingTable(stack, enchantment);*/
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 10;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public Direction getPedestalFacing(World world,BlockPos posOfPedestal)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        return state.get(FACING);
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {

    }

    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
    {

    }

}
