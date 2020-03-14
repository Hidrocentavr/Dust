package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeEffectHarvester extends ItemUpgradeBase
{
    public ItemUpgradeEffectHarvester(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    @Override
    public Boolean canAcceptRange() {
        return true;
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rangeWidth = 0;
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;

        BlockPos negBlockPos = getNegRangePos(world,pedestalPos,width,height);
        BlockPos posBlockPos = getPosRangePos(world,pedestalPos,width,height);

        if(!world.isBlockPowered(pedestalPos)) {
            for (int x = negBlockPos.getX(); x <= posBlockPos.getX(); x++) {
                for (int z = negBlockPos.getZ(); z <= posBlockPos.getZ(); z++) {
                    for (int y = negBlockPos.getY(); y <= posBlockPos.getY(); y++) {
                        BlockPos posTargetBlock = new BlockPos(x, y, z);
                        BlockState targetBlock = world.getBlockState(posTargetBlock);
                        if (tick%speed == 0) {
                            ticked++;
                        }

                        if(ticked > 84)
                        {
                            upgradeAction(world, itemInPedestal,coinInPedestal, pedestalPos, posTargetBlock, targetBlock);
                            ticked=0;
                        }
                        else
                        {
                            ticked++;
                        }
                    }
                }
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal, BlockPos posTarget, BlockState target)
    {
        if(target.getBlock() instanceof IGrowable && !target.getBlock().isAir(target,world,posTarget))
        {
            if(!((IGrowable) target.getBlock()).canGrow(world,posTarget,target,false))
            {
                FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(world.getServer().getWorld(world.getDimension().getType()));
                fakePlayer.setPosition(posOfPedestal.getX(),posOfPedestal.getY(),posOfPedestal.getZ());
                ItemStack choppingAxe = new ItemStack(Items.DIAMOND_AXE,1);
                if(!itemInPedestal.isEmpty())
                {
                    fakePlayer.setHeldItem(Hand.MAIN_HAND,itemInPedestal);
                }
                else
                {
                    if(EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.SILK_TOUCH))
                    {
                        choppingAxe.addEnchantment(Enchantments.SILK_TOUCH,1);
                        fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                    }
                    else if (EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.FORTUNE))
                    {
                        int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coinInPedestal);
                        choppingAxe.addEnchantment(Enchantments.FORTUNE,lvl);
                        fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                    }
                    else
                    {
                        fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                    }
                }

                if(fakePlayer.canHarvestBlock(target))
                {
                    target.getBlock().harvestBlock(world, fakePlayer, posTarget, target, null, fakePlayer.getHeldItemMainhand());
                }
                target.getBlock().removedByPlayer(target,world,posTarget,fakePlayer,false,null);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int s3 = getRangeWidth(stack);
        String tr = "" + (s3+s3+1) + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Harvester Effect Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Effected Area: " + tr+"x"+tr+"x"+tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item HARVESTER = new ItemUpgradeEffectHarvester(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/harvester"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(HARVESTER);
    }


}
