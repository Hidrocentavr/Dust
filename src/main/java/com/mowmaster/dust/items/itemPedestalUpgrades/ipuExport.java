package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExport extends ipuBasic
{
    public int transferRate = 0;
    public int transferSpeed = 0;

    public ipuExport(String unlocName, String registryName)
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
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
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

    public int getTransferSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                transferSpeed = 20;//normal speed
                break;
            case 1:
                transferSpeed=10;//2x faster
                break;
            case 2:
                transferSpeed = 5;//4x faster
                break;
            case 3:
                transferSpeed = 3;//6x faster
                break;
            case 4:
                transferSpeed = 2;//10x faster
                break;
            case 5:
                transferSpeed=1;//20x faster
                break;
            default: transferSpeed=20;
        }

        return  transferSpeed;
    }

    private int getNextSlotEmptyOrMatching(TileEntity invBeingChecked, EnumFacing sideSlot, ItemStack itemInPedestal)
    {
        int slot = -1;
        if(invBeingChecked.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,sideSlot))
        {
            IItemHandlerModifiable handler = (IItemHandlerModifiable) invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot);
            int range = handler.getSlots();
            for(int i=0;i<=range;i++)
            {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                int maxSizeSlot = handler.getSlotLimit(i);
                if(maxSizeSlot>0)
                {
                    if(doItemsMatch(stackInSlot,itemInPedestal) && stackInSlot.getCount() < handler.getSlotLimit(i))
                    {
                        slot = i;
                        break;
                    }
                    else if(stackInSlot.isEmpty())
                    {
                        slot = i;
                        break;
                    }
                    //if chest is full
                    else if(i==range)
                    {
                        slot=-1;
                    }
                }
            }
        }



        return slot;
    }




//                          impTicker,this.world,   getItemInPedestal(),      getCoinOnPedestal(),     this.getPos()
    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getTransferSpeed(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int upgradeTransferRate = getTransferRate(coinInPedestal);
        ItemStack itemFromPedestal = ItemStack.EMPTY;
            //Checks to make sure a TE exists
            if(world.getTileEntity(posInventory) !=null)
            {
                //Gets inventory TE then makes sure its not a pedestal
                TileEntity invToPushInto = world.getTileEntity(posInventory);
                if(invToPushInto instanceof TilePedestal) {
                    itemFromPedestal = ItemStack.EMPTY;
                }
                else {
                    TileEntity invToPushTo = world.getTileEntity(posInventory);
                    if(invToPushTo instanceof TilePedestal) {
                        itemFromPedestal = ItemStack.EMPTY;
                    }
                    else {
                        itemFromPedestal = getStackInPedestal(world,posOfPedestal);
                        //IF pedestal is empty and has nothing to transfer then dont do anything
                        if(!itemFromPedestal.isEmpty() && !itemFromPedestal.equals(ItemStack.EMPTY))
                        {
                            if(invToPushTo.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                            {
                                IItemHandlerModifiable handler = (IItemHandlerModifiable) invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));

                                //gets next empty or partially filled matching slot
                                int i = getNextSlotEmptyOrMatching(invToPushInto, getPedestalFacing(world, posOfPedestal), itemFromPedestal);
                                if(handler != null)
                                {
                                    if(i>=0)
                                    {
                                        if(handler.isItemValid(i, itemFromPedestal))
                                        {
                                            itemFromPedestal = getStackInPedestal(world,posOfPedestal).copy();
                                            ItemStack itemFromInventory = handler.getStackInSlot(i);
                                            int spaceInInventoryStack = handler.getSlotLimit(i) - itemFromInventory.getCount();

                                            //if inv slot is empty it should be able to handle as much as we can give it
                                            int allowedTransferRate = upgradeTransferRate;
                                            //checks allowed slot size amount and sets it if its lower then transfer rate
                                            if(handler.getSlotLimit(i) <= allowedTransferRate) allowedTransferRate = handler.getSlotLimit(i);
                                            //never have to check to see if pedestal and stack match because the slot checker does it for us
                                            //if our transfer rate is bigger then what can go in the slot if its partially full we set the transfer size to what can fit
                                            //Otherwise if space is bigger then rate we know it can accept as much as we're putting in
                                            if(allowedTransferRate> spaceInInventoryStack) allowedTransferRate = spaceInInventoryStack;
                                            //IF items in pedestal are less then the allowed transfer amount then set it as the amount
                                            if(allowedTransferRate > itemFromPedestal.getCount()) allowedTransferRate = itemFromPedestal.getCount();



                                            //After all calculations for transfer rate, set stack size to transfer and transfer the items
                                            itemFromPedestal.setCount(allowedTransferRate);
                                            if(handler.insertItem(i,itemFromPedestal,true ).equals(ItemStack.EMPTY)){
                                                removeFromPedestal(world,posOfPedestal ,allowedTransferRate);
                                                handler.insertItem(i,itemFromPedestal,false );
                                            }
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
        int s2 = getTransferRate(stack);
        String s3 = "";

        switch (getTransferSpeed(stack))
            {
            case 1:
                s3 = "20x Faster";
                break;
            case 2:
                s3="10x Faster";
                break;
            case 3:
                s3 = "6x Faster";
                break;
            case 5:
                s3 = "4x Faster";
                break;
            case 10:
                s3 = "2x Faster";
                break;
            case 20:
                s3="Normal Speed";
                break;
            default: s3="Normal Speed";
        }


        String tr = "" + s2 + "";
        String trr = s3;
        tooltip.add("Item Stack Export Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add("Transfer Rate: 1");
            }

            if(stack.isItemEnchanted() && getTransferRate(stack) >0)
            {
                tooltip.add("Transfer Speed: " + trr);
            }
            else
            {
                tooltip.add("Transfer Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
            tooltip.add("Transfer Speed: Normal Speed");
        }
    }

}