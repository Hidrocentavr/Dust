package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeExport extends ItemUpgradeBase
{
    public ItemUpgradeExport(Properties builder) {super(builder.group(dust.ITEM_GROUP));}


    public int getTransferRate(ItemStack stack)
    {
        int transferRate = 1;
       /* switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
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
        }*/

        return  transferRate;
    }

    private int getNextSlotEmptyOrMatching(TileEntity invBeingChecked, Direction sideSlot, ItemStack itemInPedestal)
    {
        int slot = -1;
        if(invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot).isPresent())
        {
            IItemHandler handler = (IItemHandler) invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot).orElse(null);
            if(handler != null)
            {
                int range = handler.getSlots();
                for(int i=0;i<range;i++)
                {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    int maxSizeSlot = handler.getSlotLimit(i);
                    if(maxSizeSlot>0)
                    {
                        if(stackInSlot.getMaxStackSize()>1)
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
            }
        }



        return slot;
    }




    //                          impTicker,this.world,   getItemInPedestal(),      getCoinOnPedestal(),     this.getPos()
    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

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
            TileEntity invToPushTo = world.getTileEntity(posInventory);
            if(invToPushTo instanceof TilePedestal) {
                itemFromPedestal = ItemStack.EMPTY;
            }
            else {
                itemFromPedestal = getStackInPedestal(world,posOfPedestal);
                //IF pedestal is empty and has nothing to transfer then dont do anything
                if(!itemFromPedestal.isEmpty() && !itemFromPedestal.equals(ItemStack.EMPTY))
                {
                    if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).isPresent())
                    {
                        IItemHandler handler = (IItemHandler) invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).orElse(null);

                        //gets next empty or partially filled matching slot
                        int i = getNextSlotEmptyOrMatching(invToPushTo, getPedestalFacing(world, posOfPedestal), itemFromPedestal);
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

    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String s5 = getOperationSpeedString(stack);


        tooltip.add(TextFormatting.GOLD + "Item Stack Export Upgrade");
        if(stack.hasTagCompound())
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
            tooltip.add(TextFormatting.GRAY + "Transfer Rate: 1");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String tr = "" + getTransferRate(stack) + "";

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Export Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Transfer Rate: " + tr));
    }

    public static final Item EXPORT = new ItemUpgradeExport(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/export"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(EXPORT);
    }


}
