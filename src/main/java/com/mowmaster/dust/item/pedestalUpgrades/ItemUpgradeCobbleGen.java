package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeCobbleGen extends ItemUpgradeBase
{

    public int intCobbleSpawned = 1;
    private int maxStored = Integer.MAX_VALUE;

    public ItemUpgradeCobbleGen(Properties builder) {super(builder.group(dust.itemGroup));}

    public int getCobbleGenSpawnRate(ItemStack stack)
    {
        /*switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                intCobbleSpawned = 1;
                break;
            case 1:
                intCobbleSpawned=4;
                break;
            case 2:
                intCobbleSpawned = 8;
                break;
            case 3:
                intCobbleSpawned = 16;
                break;
            case 4:
                intCobbleSpawned = 32;
                break;
            case 5:
                intCobbleSpawned=64;
                break;
            default: intCobbleSpawned=1;
        }*/

        return  intCobbleSpawned;
    }

    public Item getItemToSpawn(ItemStack coinInPedestal)
    {
        Item getItem = new ItemStack(Blocks.COBBLESTONE).getItem();
        if(coinInPedestal.isEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,coinInPedestal)> 0)
            {
                getItem = new ItemStack(Blocks.STONE).getItem();
            }
        }
        return getItem;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            //Keep Pedestal Full at all times
            fillPedestalAction(world,itemInPedestal,coinInPedestal,pedestalPos);
            //Cobble Gen Only Works So Fast
            if (tick%speed == 0) {
                upgradeAction(world,itemInPedestal,coinInPedestal,pedestalPos);
            }
        }
    }

    public void fillPedestalAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int intSpace = intSpaceLeftInStack(itemInPedestal);
        TileEntity tileCheckForPedestal = world.getTileEntity(pedestalPos);
        ItemStack stackSpawnedItem = new ItemStack(getItemToSpawn(coinInPedestal));
        stackSpawnedItem.setCount(intSpace);
        if(tileCheckForPedestal instanceof TilePedestal)
        {
            TilePedestal tilePedestal = ((TilePedestal)tileCheckForPedestal);
            int intGetStored = tilePedestal.getStoredValueForUpgrades();
            if(intSpace>0)
            {
                if(intGetStored >= intSpace)
                {
                    int intNewStored = intGetStored - intSpace;
                    tilePedestal.setStoredValueForUpgrades(intNewStored);
                    tilePedestal.addItem(stackSpawnedItem);
                }
                else
                {
                    int intNewStored = 0;
                    stackSpawnedItem.setCount(intGetStored);
                    tilePedestal.setStoredValueForUpgrades(intNewStored);
                    tilePedestal.addItem(stackSpawnedItem);
                }
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        TileEntity tileCheckForPedestal = world.getTileEntity(pedestalPos);
        int intSpawnRate = getCobbleGenSpawnRate(coinInPedestal);
        if(tileCheckForPedestal instanceof TilePedestal)
        {
            TilePedestal tilePedestal = ((TilePedestal)tileCheckForPedestal);
            int intGetStored = tilePedestal.getStoredValueForUpgrades();
            int intNewStored = intGetStored + intSpawnRate;
            if(intGetStored <= (maxStored - intSpawnRate))
            {
                tilePedestal.setStoredValueForUpgrades(intNewStored);
            }
        }
    }


    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
    {
        if(entityIn instanceof ItemEntity)
        {
            ItemStack stackPedestal = tilePedestal.getItemInPedestal();
            ItemStack stackCollidedItem = ((ItemEntity) entityIn).getItem();
            int intCurrentlyStored = tilePedestal.getStoredValueForUpgrades();
            if(doItemsMatch(stackPedestal,stackCollidedItem))
            {
                if(intSpaceLeftInStack(stackPedestal) >= stackCollidedItem.getCount())
                {
                    ItemStack stackCollidedItemCopy = stackCollidedItem.copy();
                    entityIn.remove();
                    tilePedestal.addItem(stackCollidedItemCopy);
                }
                else
                {
                    ItemStack stackCollidedItemCopy = stackCollidedItem.copy();
                    stackCollidedItemCopy.setCount(intSpaceLeftInStack(stackPedestal));
                    int intCountDifference = stackCollidedItem.getCount() - intSpaceLeftInStack(stackPedestal);
                    if((intCurrentlyStored+intCountDifference) < maxStored)
                    {
                        tilePedestal.setStoredValueForUpgrades((intCurrentlyStored+intCountDifference));
                    }
                    entityIn.remove();
                    tilePedestal.addItem(stackCollidedItemCopy);
                }
            }
        }
    }

    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String tr = "" + getCobbleGenSpawnRate(stack) + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(TextFormatting.GOLD + "Cobble Generator Upgrade");

        tooltip.add(TextFormatting.GRAY + "Cobble Spawned Per Opperation: "+tr);

        if(stack.isItemEnchanted() && getOperationSpeed(stack) > 0)
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

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Cobble Generator Upgrade"));
    }

    public static final Item COBBLE = new ItemUpgradeCobbleGen(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/cobble"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(COBBLE);
    }


}
