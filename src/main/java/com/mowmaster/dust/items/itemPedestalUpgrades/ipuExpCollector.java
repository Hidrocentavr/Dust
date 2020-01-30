package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static net.minecraft.block.BlockDirectional.FACING;

public class ipuExpCollector extends ipuBasicExpUpgrade
{
    public int rangeWidth = 0;
    public int operationalSpeed = 0;
    public int maxXP;
    public int suckiRate = 0;


    public ipuExpCollector(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int getOperationSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                operationalSpeed = 20;//normal speed
                break;
            case 1:
                operationalSpeed=10;//2x faster
                break;
            case 2:
                operationalSpeed = 5;//4x faster
                break;
            case 3:
                operationalSpeed = 3;//6x faster
                break;
            case 4:
                operationalSpeed = 2;//10x faster
                break;
            case 5:
                operationalSpeed=1;//20x faster
                break;
            default: operationalSpeed=20;
        }

        return  operationalSpeed;
    }

    public int getSuckiRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                suckiRate = 7;//1
                break;
            case 1:
                suckiRate=16;//2
                break;
            case 2:
                suckiRate = 27;//3
                break;
            case 3:
                suckiRate = 40;//4
                break;
            case 4:
                suckiRate = 55;//5
                break;
            case 5:
                suckiRate=160;//10
                break;
            default: suckiRate=7;
        }

        return  suckiRate;
    }



    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
                upgradeActionSendExp(world, coinInPedestal,pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        setMaxXP(coinInPedestal,getExpCountByLevel(30) );
        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;
        BlockPos negBlockPos = getNegRangePosEntity(world,posOfPedestal,width,height);
        BlockPos posBlockPos = getPosRangePosEntity(world,posOfPedestal,width,height);

        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);

        List<EntityXPOrb> xpList = world.getEntitiesWithinAABB(EntityXPOrb.class,getBox);
        for(EntityXPOrb getXPFromList : xpList)
        {
            world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
            if(pedestalInv instanceof TilePedestal) {
                int currentlyStoredExp = ((TilePedestal) pedestalInv).getStoredValueForUpgrades();
                if(currentlyStoredExp < getMaxXP(coinInPedestal))
                {
                    int value = getXPFromList.getXpValue();
                    getXPFromList.setDead();
                    ((TilePedestal) pedestalInv).setStoredValueForUpgrades(currentlyStoredExp + value);
                }
            }
            break;
        }
    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityXPOrb)
        {
            EntityXPOrb getXPFromList = ((EntityXPOrb)entityIn);
            world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
            int currentlyStoredExp = tilePedestal.getStoredValueForUpgrades();
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            if(currentlyStoredExp < getMaxXP(coin))
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.setDead();
                tilePedestal.setStoredValueForUpgrades(currentlyStoredExp + value);
            }
        }

        if(entityIn instanceof EntityPlayer)
        {
            EntityPlayer getPlayer = ((EntityPlayer)entityIn);
            int currentlyStoredExp = tilePedestal.getStoredValueForUpgrades();
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            if(currentlyStoredExp < getMaxXP(coin))
            {
                int transferRate = getSuckiRate(tilePedestal.getCoinOnPedestal());
                int value = removeXp(getPlayer, transferRate);
                if(value > 0)
                {
                    world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
                    tilePedestal.setStoredValueForUpgrades(currentlyStoredExp + value);
                }

            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        String s5 = "";
        String tr = "";
        String sr = "";

        switch (getOperationSpeed(stack))
        {
            case 1:
                s5 = "20x Faster";
                break;
            case 2:
                s5="10x Faster";
                break;
            case 3:
                s5 = "6x Faster";
                break;
            case 5:
                s5 = "4x Faster";
                break;
            case 10:
                s5 = "2x Faster";
                break;
            case 20:
                s5="Normal Speed";
                break;
            default: s5="Normal Speed";
        }

        switch (getExpTransferRate(stack))
        {
            case 55:
                tr = "5 Levels";
                break;
            case 160:
                tr="10 Levels";
                break;
            case 315:
                tr = "15 Levels";
                break;
            case 550:
                tr = "20 Levels";
                break;
            case 910:
                tr = "25 Levels";
                break;
            case 1395:
                tr="30 Levels";
                break;
            default: tr="5 Levels";
        }

        /*switch (getSuckiRate(stack))
        {
            case 7:
                sr = "1 Level";
                break;
            case 16:
                sr="2 Levels";
                break;
            case 27:
                sr = "3 Levels";
                break;
            case 40:
                sr = "4 Levels";
                break;
            case 55:
                sr = "5 Levels";
                break;
            case 160:
                sr="10 Levels";
                break;
            default: sr="1 Level";
        }*/

        String trr = "" + (s3+s3+1) + "";

        tooltip.add(TextFormatting.GOLD + "Exp Collector Upgrade");

        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: 30 Levels");

        /*if(stack.hasTagCompound()) {
            if (getSuckiRate(stack) > 0) {
                tooltip.add("???: " + sr);
            } else {
                tooltip.add("???: 1 Level");
            }
        }*/

            if(s3>0)
        {
            tooltip.add("Effected Area: " + trr+"x"+trr+"x"+trr);
        }
        else
        {
            tooltip.add("Effected Area: " + trr+"x"+trr+"x"+trr);
        }

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add("Exp Transfer Ammount: " + tr);
        }
        else
        {
            tooltip.add("Exp Transfer Ammount: 5 Levels");
        }

        if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
        {
            tooltip.add("Operational Speed: " + s5);
        }
        else
        {
            tooltip.add("Operational Speed: Normal Speed");
        }
    }



}
