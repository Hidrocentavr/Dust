package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeExpDropper extends ItemUpgradeBaseExp
{
    public int range = 0;

    public ItemUpgradeExpDropper(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    @Override
    public Boolean canAcceptRange() {
        return true;
    }

    @Override
    public Boolean canAcceptCapacity() {
        return true;
    }

    public int getTransferRate(ItemStack stack)
    {
        int summonRate = 7;
        switch (getCapacityModifier(stack))
        {
            case 0:
                summonRate = 7;//1
                break;
            case 1:
                summonRate=16;//2
                break;
            case 2:
                summonRate = 40;//4
                break;
            case 3:
                summonRate = 72;//6
                break;
            case 4:
                summonRate = 112;//8
                break;
            case 5:
                summonRate=160;//10
                break;
            default: summonRate=7;
        }

        return  summonRate;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        if(!hasMaxXpSet(coinInPedestal)) {setMaxXP(coinInPedestal,getExpCountByLevel(10));}
        int rate = getTransferRate(coinInPedestal);
        int range = getRange(coinInPedestal);


        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
        if(pedestalInv instanceof TilePedestal) {
            int currentlyStoredExp = getXPStored(coinInPedestal);
            if(currentlyStoredExp > 0)
            {
                if(currentlyStoredExp < rate)
                {
                    rate = currentlyStoredExp;
                }

                ExperienceOrbEntity expEntity = new ExperienceOrbEntity(world,getPosOfBlockBelow(world,posOfPedestal,-range).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-range).getY(),getPosOfBlockBelow(world,posOfPedestal,-range).getZ() + 0.5,rate);
                expEntity.setMotion(0D,0D,0D);

                int getExpLeftInPedestal = currentlyStoredExp - rate;
                world.playSound((PlayerEntity) null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.25F, 1.0F);
                setXPStored(coinInPedestal,getExpLeftInPedestal);
                world.playSound((PlayerEntity)null, expEntity.getPosition().getX(), expEntity.getPosition().getY(), expEntity.getPosition().getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
                world.addEntity(expEntity);
            }
        }
    }

    @Override
    public void actionOnCollideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn) {

    }

    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {



        if(stack.hasTagCompound())
        {


            if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
            {

            }
            else
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Dropper Range: " + trr);
            tooltip.add(TextFormatting.GRAY + "Exp Dropped Ammount: 1 Level");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }*/

    public int getExpBuffer(ItemStack stack)
    {
        return  10;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int tr = 1;

        switch (getTransferRate(stack))
        {
            case 7:
                tr = 1;
                break;
            case 16:
                tr=2;
                break;
            case 40:
                tr = 4;
                break;
            case 72:
                tr = 6;
                break;
            case 112:
                tr = 8;
                break;
            case 160:
                tr=10;
                break;
            default: tr=1;
        }

        TranslationTextComponent range = new TranslationTextComponent(getTranslationKey() + ".tooltip_range");
        range.appendText("" +  getRange(stack) + "");
        TranslationTextComponent rate = new TranslationTextComponent(getTranslationKey() + ".tooltip_rate");
        rate.appendText("" +  tr + "");

        TranslationTextComponent speed = new TranslationTextComponent(getTranslationKey() + ".tooltip_speed");
        speed.appendText(getOperationSpeedString(stack));

        range.applyTextStyle(TextFormatting.WHITE);
        rate.applyTextStyle(TextFormatting.GRAY);
        speed.applyTextStyle(TextFormatting.RED);

        tooltip.add(range);
        tooltip.add(rate);
        tooltip.add(speed);
    }

    public static final Item XPDROPPER = new ItemUpgradeExpDropper(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/xpdropper"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(XPDROPPER);
    }


}
