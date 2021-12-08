package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.Tools.FilterTool;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class FilterFood extends BaseFilter{
    public FilterFood(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn, int mode) {
        boolean filterBool=getFilterType(pedestal.getFilterInPedestal(),mode);

        if(mode==0)
        {
            if(itemStackIn.isEnchanted() || itemStackIn.getItem().equals(Items.ENCHANTED_BOOK))
            {
                ItemStack filter = pedestal.getFilterInPedestal();
                List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,mode);
                int range = stackCurrent.size();

                Map<Enchantment, Integer> mapIncomming = EnchantmentHelper.getEnchantments(itemStackIn);

                for(Map.Entry<Enchantment, Integer> entry : mapIncomming.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    ItemStack itemFromInv = ItemStack.EMPTY;
                    itemFromInv = IntStream.range(0,range)//Int Range
                            .mapToObj((stackCurrent)::get)//Function being applied to each interval
                            //Check to make sure filter item is enchanted
                            .filter(itemStack -> itemStack.isEnchanted() || itemStack.getItem().equals(Items.ENCHANTED_BOOK))
                            //Check if filter item has any enchant that the item in the pedestal has
                            .filter(itemStack -> EnchantmentHelper.getEnchantments(itemStack).containsKey(enchantment))
                            .findFirst().orElse(ItemStack.EMPTY);

                    if(!itemFromInv.isEmpty())
                    {
                        return !filterBool;
                    }
                }
            }
        }
        else return !filterBool;

        return filterBool;
    }

    //Right Click
    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack itemInMainhand = player.getMainHandItem();
        ItemStack itemInOffhand = player.getOffhandItem();
        HitResult result = player.pick(5,0,false);

        if(!world.isClientSide())
        {
            //Disable Filter Base
            if(!(itemInOffhand.getItem().equals(DeferredRegisterItems.FILTER_BASE.get())) || !(itemInMainhand.getItem().equals(DeferredRegisterItems.FILTER_BASE.get())))
            {
                //Check for Offhand Only Filter
                if(itemInOffhand.getItem() instanceof IPedestalFilter && !(itemInMainhand.getItem() instanceof IPedestalFilter))
                {
                    if(result.getType().equals(HitResult.Type.MISS))
                    {
                        if(player.isCrouching())
                        {
                            setFilterMode(player,itemInOffhand,InteractionHand.OFF_HAND);
                            //return InteractionResultHolder.success(itemInOffhand);
                        }
                        else
                        {
                            setFilterTypeWhiteBlacklist(player,itemInOffhand);
                            //return InteractionResultHolder.success(itemInOffhand);
                        }
                    }
                }
                else if(itemInOffhand.getItem() instanceof IPedestalFilter && itemInMainhand.getItem() instanceof IPedestalFilter){
                    TranslatableComponent pedestalFluid = new TranslatableComponent(MODID + ".filter.message_twohanded");
                    pedestalFluid.withStyle(ChatFormatting.RED);
                    player.displayClientMessage(pedestalFluid,true);
                }
            }
        }

        return InteractionResultHolder.fail(p_41433_.getItemInHand(p_41434_));
    }
}
