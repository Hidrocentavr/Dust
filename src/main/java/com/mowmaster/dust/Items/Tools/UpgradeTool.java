package com.mowmaster.dust.Items.Tools;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UpgradeTool extends BaseTool implements IPedestalTool
{
    public UpgradeTool(Properties p_41383_) {
        super(p_41383_.stacksTo(1));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack stackInHand = player.getItemInHand(hand);
        //Build Color List from NBT
        HitResult result = player.pick(5,0,false);
        BlockPos pos = new BlockPos(result.getLocation().x,result.getLocation().y,result.getLocation().z);
        if(result.getType().equals(HitResult.Type.MISS))
        {
            if(player.isCrouching())
            {
                if(stackInHand.getItem().equals(DeferredRegisterItems.TOOL_UPGRADETOOL.get()))
                {
                    ItemStack newTool = new ItemStack(DeferredRegisterItems.TOOL_FILTERTOOL.get());
                    player.setItemInHand(hand, newTool);

                    TranslatableComponent changed = new TranslatableComponent(getDescriptionId() + ".tool_change");
                    changed.withStyle(ChatFormatting.GREEN);
                    player.displayClientMessage(changed,true);
                    return InteractionResultHolder.success(stackInHand);
                }
            }
        }
        else if(result.getType().equals(HitResult.Type.BLOCK))
        {


        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return DeferredRegisterItems.TOOL_UPGRADETOOL.get().getDefaultInstance();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
