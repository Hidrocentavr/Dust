package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeShearer extends ItemUpgradeBase
{
    public int rangeHeight = 1;

    public ItemUpgradeShearer(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

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

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, itemInPedestal,pedestalPos, coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal,BlockPos pedestalPos, ItemStack coinInPedestal)
    {
        int width = getRangeWidth(coinInPedestal);
        BlockPos negBlockPos = getNegRangePosEntity(world,pedestalPos,width,rangeHeight);
        BlockPos posBlockPos = getPosRangePosEntity(world,pedestalPos,width,rangeHeight);
        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);
        //Entity Creature could be used to cover creepers for better with mods and such
        List<LivingEntity> baa = world.getEntitiesWithinAABB(LivingEntity.class,getBox);
        for(LivingEntity baaaaaa : baa)
        {
            if(baaaaaa instanceof IShearable)
            {
                IShearable baabaa = (IShearable)baaaaaa;
                if (baabaa.isShearable(new ItemStack(Items.SHEARS),world,baaaaaa.getPosition()))
                {
                    if(getStackInPedestal(world,pedestalPos).equals(ItemStack.EMPTY))
                    {
                        Random rando = new Random();
                        int i = 1 + rando.nextInt(3);
                        List<ItemStack> drops = baabaa.onSheared(new ItemStack(Items.SHEARS),world,baaaaaa.getPosition(),0);

                        for (int j = 0; j < i; ++j)
                        {
                            if(drops.size()>0)
                            {
                                for (int d=0;d<drops.size();d++)
                                {
                                    if(itemInPedestal.isEmpty() || drops.get(d).equals(itemInPedestal) && canAddToPedestal(world,pedestalPos,drops.get(d)) >= drops.get(d).getCount())
                                    {
                                        addToPedestal(world,pedestalPos,drops.get(d));
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int s3 = getRangeWidth(stack);
        String tr = "" + (s3+s3+1) + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Shearing Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Effected Area: " + tr+"x"+"2x"+tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item SHEARER = new ItemUpgradeShearer(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/shearer"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(SHEARER);
    }


}
