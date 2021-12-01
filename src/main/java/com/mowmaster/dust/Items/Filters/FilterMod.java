package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class FilterMod extends BaseFilter{
    public FilterMod(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, Level world, BlockPos pos, ItemStack itemInPedestal, ItemStack itemStackIncoming, int mode) {

        ItemStack filter = pedestal.getFilterInPedestal();
        List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,mode);
        int range = stackCurrent.size();

        ItemStack itemFromInv = ItemStack.EMPTY;
        itemFromInv = IntStream.range(0,range)//Int Range
                .mapToObj((stackCurrent)::get)//Function being applied to each interval
                .filter(itemStack -> itemStack.getItem() instanceof FilterRestricted)
                .findFirst().orElse(ItemStack.EMPTY);

        if(!itemFromInv.isEmpty())
        {
            if(itemInPedestal.isEmpty())
            {
                List<ItemStack> stackCurrentRestricted = readFilterQueueFromNBT(itemFromInv,getFilterMode(itemFromInv));
                int rangeRestricted = stackCurrentRestricted.size();
                int count = 0;
                int maxIncomming = itemStackIncoming.getMaxStackSize();
                for(int i=0;i<rangeRestricted;i++)
                {
                    count +=stackCurrent.get(i).getCount();
                    if(count>=maxIncomming)break;
                }

                if(mode==0)
                {
                    return (count>0)?((count>maxIncomming)?(maxIncomming):(count)):(1);
                }
                else return count;

            }

            return 0;
        }

        return super.canAcceptCount(pedestal, world, pos, itemInPedestal, itemStackIncoming,mode);
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn, int mode) {
        boolean filterBool=getFilterType(pedestal.getFilterInPedestal());

        if(mode<=1)
        {
            ItemStack filter = pedestal.getFilterInPedestal();
            List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,mode);
            int range = stackCurrent.size();

            ItemStack itemFromInv = ItemStack.EMPTY;
            itemFromInv = IntStream.range(0,range)//Int Range
                    .mapToObj((stackCurrent)::get)//Function being applied to each interval
                    .filter(itemStack -> itemStack.getItem().getRegistryName().getNamespace()==itemStackIn.getItem().getRegistryName().getNamespace())
                    .findFirst().orElse(ItemStack.EMPTY);

            if(!itemFromInv.isEmpty())
            {
                return !filterBool;
            }
        }
        else return !filterBool;

        return filterBool;
    }


    @Override
    public void chatDetails(Player player, BasePedestalBlockEntity pedestal) {
        ItemStack filterStack = pedestal.getFilterInPedestal();
        if(!filterStack.getItem().equals(DeferredRegisterItems.FILTER_BASE.get()))
        {
            TranslatableComponent filterList = new TranslatableComponent(filterStack.getDisplayName().getString());
            filterList.withStyle(ChatFormatting.GOLD);
            player.sendMessage(filterList, Util.NIL_UUID);

            //For each Mode
            for(int i=0;i<4;i++)
            {
                List<ItemStack> filterQueue = readFilterQueueFromNBT(filterStack,i);
                if(filterQueue.size()>0)
                {
                    TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist");
                    enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
                    player.sendMessage(enchant, Util.NIL_UUID);

                    for(int j=0;j<filterQueue.size();j++) {

                        if(!filterQueue.get(j).isEmpty())
                        {
                            TranslatableComponent enchants = new TranslatableComponent(filterQueue.get(j).getItem().getRegistryName().getNamespace());
                            enchants.withStyle(ChatFormatting.GRAY);
                            player.sendMessage(enchants, Util.NIL_UUID);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        boolean filterType = getFilterType(p_41421_);
        TranslatableComponent filterList = new TranslatableComponent(MODID + ".filters.tooltip_filtertype");
        TranslatableComponent white = new TranslatableComponent(MODID + ".filters.tooltip_filterwhite");
        TranslatableComponent black = new TranslatableComponent(MODID + ".filters.tooltip_filterblack");
        filterList.append((filterType)?(black):(white));
        filterList.withStyle(ChatFormatting.GOLD);
        p_41423_.add(filterList);

        List<ItemStack> filterQueue = readFilterQueueFromNBT(p_41421_,getFilterMode(p_41421_));
        if(filterQueue.size()>0)
        {
            TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist");
            enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
            p_41423_.add(enchant);

            for(int i=0;i<filterQueue.size();i++) {

                if(!filterQueue.get(i).isEmpty())
                {
                    TranslatableComponent enchants = new TranslatableComponent(filterQueue.get(i).getItem().getRegistryName().getNamespace());
                    enchants.withStyle(ChatFormatting.GRAY);
                    p_41423_.add(enchants);
                }
            }
        }
    }
}
