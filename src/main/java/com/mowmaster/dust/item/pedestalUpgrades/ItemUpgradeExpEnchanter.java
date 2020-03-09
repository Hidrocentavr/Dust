package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeExpEnchanter extends ItemUpgradeBaseExp
{

    public ItemUpgradeExpEnchanter(Properties builder) {super(builder.group(dust.itemGroup));}

    public int getExpBuffer(ItemStack stack)
    {
        int value = 30;
        /*switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 30;//30
                break;
            case 1:
                value=44;//44
                break;
            case 2:
                value = 58;//58
                break;
            case 3:
                value = 72;//72
                break;
            case 4:
                value = 86;//86
                break;
            case 5:
                value=100;//100
                break;
            default: value=30;
        }*/

        return  value;
    }

    public float getEnchantmentPowerFromSorroundings(World world, BlockPos posOfPedestal)
    {

        float enchantPower = 0;

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                for (int k = 0; k <= 2; ++k) {
                    BlockPos blockpos = posOfPedestal.add(i, k, j);
                    BlockState blockNearBy = world.getBlockState(blockpos);
                    if (blockNearBy.getBlock().getEnchantPowerBonus(blockNearBy, world, blockpos)>0)
                    {
                        enchantPower +=blockNearBy.getBlock().getEnchantPowerBonus(blockNearBy, world, blockpos);
                    }
                }
            }
        }
        return enchantPower;
    }

    public float getEnchantmentPowerFromSorroundings(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

        float enchantPower = 0;
        int getMaxEnchantLevel = getExpBuffer(coinInPedestal);

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                for (int k = 0; k <= 2; ++k) {
                    BlockPos blockpos = posOfPedestal.add(i, k, j);
                    BlockState blockNearBy = world.getBlockState(blockpos);
                    if (blockNearBy.getBlock().getEnchantPowerBonus(blockNearBy, world, blockpos)>0)
                    {
                        enchantPower +=blockNearBy.getBlock().getEnchantPowerBonus(blockNearBy, world, blockpos);
                    }
                }
            }
        }

        if((int)(enchantPower*2) > getMaxEnchantLevel)
        {
            enchantPower = (float)(getMaxEnchantLevel/2);
        }

        return enchantPower;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, itemInPedestal, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int getMaxXpValue = getExpCountByLevel(getExpBuffer(coinInPedestal));
        setMaxXP(coinInPedestal, getMaxXpValue);
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        ItemStack itemFromInv = ItemStack.EMPTY;

        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).isPresent())
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).orElse(null);
                TileEntity invToPullFrom = world.getTileEntity(posInventory);
                if(invToPullFrom instanceof TilePedestal) {
                    itemFromInv = ItemStack.EMPTY;
                }
                else {
                    if(handler != null)
                    {
                        int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                        if(i>=0)
                        {
                            itemFromInv = handler.getStackInSlot(i);
                            int slotCount = itemFromInv.getCount();
                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                            if(pedestalInv instanceof TilePedestal) {
                                if(!((TilePedestal) pedestalInv).hasItem())
                                {
                                    if(itemFromInv.isEnchantable() || itemFromInv.getItem().equals(Items.BOOK))
                                    {
                                        //This is Book Shelf Enchanting level, not enchantment level (15 bookshelfves = 30 levels of enchantability)
                                        float level = getEnchantmentPowerFromSorroundings(world,posOfPedestal,coinInPedestal);
                                        int currentlyStoredExp = getXPStored(coinInPedestal);
                                        int expNeeded = getExpCountByLevel((int)(level * 2));
                                        if(currentlyStoredExp >= expNeeded)
                                        {
                                            //Enchanting Code Here
                                            Random rand = new Random();
                                            ItemStack itemToEnchant = itemFromInv.copy();
                                            itemToEnchant.setCount(1);
                                            ItemStack stackToReturn = EnchantmentHelper.addRandomEnchantment(rand,itemToEnchant ,(int)(level * 2) ,true );
                                            if(!stackToReturn.isEmpty())
                                            {
                                                int getExpLeftInPedestal = currentlyStoredExp - expNeeded;
                                                setXPStored(coinInPedestal,getExpLeftInPedestal);
                                                handler.extractItem(i,stackToReturn.getCount() ,false );
                                                world.playSound((PlayerEntity) null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                ((TilePedestal) pedestalInv).addItem(stackToReturn);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        ItemStack toReturn = itemFromInv.copy();
                                        handler.extractItem(i,toReturn.getCount() ,false );
                                        ((TilePedestal) pedestalInv).addItem(toReturn);
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
    public void onRandomDisplayTick(TilePedestal pedestal, BlockState stateIn, World world, BlockPos pos, Random rand)
    {
        if(!world.isBlockPowered(pos))
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (i > -2 && i < 2 && j == -1)
                    {
                        j = 2;
                    }

                    if (rand.nextInt(16) == 0)
                    {
                        for (int k = 0; k <= 2; ++k)
                        {
                            BlockPos blockpos = pos.add(i, k, j);

                            if (world.getBlockState(blockpos).getEnchantPowerBonus(world, pos) > 0) {
                                if (!world.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                                    break;
                                }

                                world.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D);
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

        if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: " + s5);
        }
        else
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int buffer = getExpBuffer(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Exp Enchanter Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + "Exp Levels Stored: "+xp));
        tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + "Exp Buffer Capacity: " + buffer + " Levels"));
    }

    public static final Item XPENCHANTER = new ItemUpgradeExpEnchanter(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/xpenchanter"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(XPENCHANTER);
    }


}
