package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import java.util.stream.IntStream;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeFilterMod extends ItemUpgradeBaseFilter
{
    public ItemUpgradeFilterMod(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos pedestalPos)
    {

    }

    @Override
    public boolean canAcceptItem(World world, BlockPos posPedestal, ItemStack itemStackIn)
    {
        boolean returner = false;
        BlockPos posInventory = getPosOfBlockBelow(world, posPedestal, 1);

        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posPedestal)).isPresent())
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posPedestal)).orElse(null);
                if(handler != null)
                {
                    int range = handler.getSlots();

                    ItemStack itemFromInv = ItemStack.EMPTY;

                    itemFromInv = IntStream.range(0,range)//Int Range
                            .mapToObj((handler)::getStackInSlot)//Function being applied to each interval
                            .filter(itemStack -> itemStack.getItem().getRegistryName().getNamespace()==itemStackIn.getItem().getRegistryName().getNamespace())
                            .findFirst().orElse(ItemStack.EMPTY);

                    if(!itemFromInv.isEmpty())
                    {
                        returner = true;
                    }
                }
            }
        }

        return returner;
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Filter: Mod"));
    }

    public static final Item MOD = new ItemUpgradeFilterMod(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/filtermod"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(MOD);
    }



}
