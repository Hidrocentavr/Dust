package com.mowmaster.dust.Items.Tools;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class DevTool extends BaseTool implements IPedestalTool
{
    public DevTool(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack stackInHand = player.getItemInHand(hand);
        //Build Color List from NBT
        HitResult result = player.pick(5,0,false);
        if(result.getType().equals(HitResult.Type.MISS))
        {

        }
        else if(result.getType().equals(HitResult.Type.BLOCK))
        {


        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    /*@Override
    public boolean hasContainerItem(ItemStack stack) {
        return super.hasContainerItem(DeferredRegisterItems.TOOL_DEVTOOL.get().getDefaultInstance());
    }*/
}
