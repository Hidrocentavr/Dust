package com.mowmaster.dust.item;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemCrystalWrench extends Item {

    private static final BlockPos defaultPos = new BlockPos(0,-2000,0);
    public BlockPos storedPosition = defaultPos;

    public ItemCrystalWrench() {
        super(new Item.Properties().maxStackSize(1).group(dust.itemGroup));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World worldIn = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();

        if(!worldIn.isRemote)
        {
            BlockState getBlockState = worldIn.getBlockState(pos);
            if(player.isCrouching())
            {
                if(getBlockState.getBlock() instanceof BlockPedestal)
                {
                    if(player.getHeldItemMainhand().isEnchanted()==false)
                    {
                        //Gets Pedestal Clicked on Pos
                        this.storedPosition = pos;
                        //Writes to NBT
                        writePosToNBT(player.getHeldItemMainhand());
                        //Applies effect to wrench in hand
                        if(player.getHeldItemMainhand().getItem() instanceof ItemCrystalWrench)
                        {
                            player.getHeldItemMainhand().addEnchantment(Enchantments.UNBREAKING,-1);
                        }
                    }
                    //If wrench has the compound stacks and has a position stored(is enchanted)
                    else if(player.getHeldItemMainhand().hasTag() && player.getHeldItemMainhand().isEnchanted())
                    {
                        //Checks if clicked blocks is a Pedestal
                        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPedestal)
                        {
                            //Checks Tile at location to make sure its a TilePedestal
                            TileEntity tileEntity = worldIn.getTileEntity(pos);
                            if (tileEntity instanceof TilePedestal) {
                                TilePedestal tilePedestal = (TilePedestal) tileEntity;

                                //checks if connecting pedestal is out of range of the senderPedestal
                                if(isPedestalInRange(tilePedestal,getStoredPosition(player.getHeldItemMainhand())))
                                {
                                    //Checks if pedestals to be linked are on same networks or if one is neutral
                                    if(tilePedestal.canLinkToPedestalNetwork(getStoredPosition(player.getHeldItemMainhand())))
                                    {
                                        //If stored location isnt the same as the connecting pedestal
                                        if(!tilePedestal.isSamePedestal(getStoredPosition(player.getHeldItemMainhand())))
                                        {
                                            //Checks if the conenction hasnt been made once already yet
                                            if(!tilePedestal.isAlreadyLinked(getStoredPosition(player.getHeldItemMainhand())))
                                            {
                                                //Checks if senderPedestal has locationSlots available
                                                //System.out.println("Stored Locations: "+ tilePedestal.getNumberOfStoredLocations());
                                                if(tilePedestal.storeNewLocation(getStoredPosition(player.getHeldItemMainhand())))
                                                {
                                                    //If slots are available then set wrench properties back to a default value
                                                    this.storedPosition = defaultPos;
                                                    writePosToNBT(player.getHeldItemMainhand());
                                                    worldIn.notifyBlockUpdate(pos,worldIn.getBlockState(pos),worldIn.getBlockState(pos),2);
                                                    if(player.getHeldItemMainhand().getItem() instanceof ItemCrystalWrench)
                                                    {
                                                        if(player.getHeldItemMainhand().isEnchanted())
                                                        {
                                                            player.getHeldItemMainhand().removeChildTag("ench");
                                                        }
                                                    }
                                                    player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Link Successful"));
                                                }
                                                else player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Link Unsuccessful - Maximum Links Reached"));
                                            }
                                            else
                                            {
                                                tilePedestal.removeLocation(getStoredPosition(player.getHeldItemMainhand()));
                                                player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Link Successfully Removed"));
                                            }
                                        }
                                        else player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Cannot be Linked to Itsself"));
                                    }
                                    else player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Cannot Connect to this Network"));
                                }
                                else player.sendMessage(new TranslationTextComponent(TextFormatting.WHITE + "Too Far to Connect"));
                            }
                        }
                    }



                }
                else
                {
                    this.storedPosition = defaultPos;
                    writePosToNBT(player.getHeldItemMainhand());
                    worldIn.notifyBlockUpdate(pos,worldIn.getBlockState(pos),worldIn.getBlockState(pos),2);
                    if(player.getHeldItemMainhand().getItem() instanceof ItemCrystalWrench)
                    {
                        if(player.getHeldItemMainhand().isEnchanted())
                        {
                            player.getHeldItemMainhand().removeChildTag("ench");
                        }
                    }
                }
            }
        }

        return super.onItemUse(context);
    }

    int ticker=0;

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        //super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (stack.hasTag()) {
            this.getPosFromNBT(stack);
            BlockPos pos = this.getStoredPosition(stack);
            Random rand = new Random();

            int zmin = -8;
            int zmax = 8+1;
            int xmin = -8;
            int xmax = 8+1;
            int ymin = -8;
            int ymax = 8+1;

            if(worldIn.isAreaLoaded(pos,1))
            {
                if(worldIn.getTileEntity(pos) instanceof TilePedestal)
                {
                    TilePedestal pedestal = (TilePedestal)worldIn.getTileEntity(pos);
                    int range = pedestal.getPedestalTransferRange();
                    zmin = -range;
                    zmax = range;
                    xmin = -range;
                    xmax = range;
                    ymin = -range;
                    ymax = range;
                }
            }

            if(storedPosition!=defaultPos)
            {
                if(isSelected)
                {
                    if(worldIn.isRemote)
                    {
                        ticker++;
                        if(ticker>30)
                        {
                            //Test to see what location is stored in the wrench System.out.println(this.getStoredPosition(stack));
                            for (int c = zmin; c <= zmax; c++) {
                                for (int a = xmin; a <= xmax; a++) {
                                    for (int b = ymin; b <= ymax; b++) {
                                        worldIn.addParticle(ParticleTypes.ENCHANT,true,pos.add(a,b,c).getX()+0.5f,pos.add(a,b,c).getY()+0.5f,pos.add(a,b,c).getZ()+0.5f, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
                                    }
                                }
                            }
                            ticker=0;
                        }
                    }
                }
            }

        }
    }

    public boolean isPedestalInRange(TilePedestal pedestal, BlockPos pedestalToBeLinked)
    {
        int range = pedestal.getPedestalTransferRange();
        int x = pedestalToBeLinked.getX();
        int y = pedestalToBeLinked.getY();
        int z = pedestalToBeLinked.getZ();
        int x1 = pedestal.getPos().getX();
        int y1 = pedestal.getPos().getY();
        int z1 = pedestal.getPos().getZ();
        int xF = Math.abs(Math.subtractExact(x,x1));
        int yF = Math.abs(Math.subtractExact(y,y1));
        int zF = Math.abs(Math.subtractExact(z,z1));

        if(xF>range || yF>range || zF>range)
        {
            return false;
        }
        else return true;
    }

    public BlockPos getStoredPosition(ItemStack getWrenchItem)
    {
        getPosFromNBT(getWrenchItem);
        return storedPosition;
    }

    public void writePosToNBT(ItemStack stack)
    {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("stored_x",this.storedPosition.getX());
        compound.putInt("stored_y",this.storedPosition.getY());
        compound.putInt("stored_z",this.storedPosition.getZ());
        stack.setTag(compound);
    }

    public void getPosFromNBT(ItemStack stack)
    {
        if(stack.hasTag())
        {
            CompoundNBT getCompound = stack.getTag();
            int x = getCompound.getInt("stored_x");
            int y = getCompound.getInt("stored_y");
            int z = getCompound.getInt("stored_z");
            this.storedPosition = new BlockPos(x,y,z);
        }

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.getItem() instanceof ItemCrystalWrench) {
            if (stack.hasTag()) {
                if (stack.isEnchanted()) {
                    tooltip.add(new TranslationTextComponent("Selected Block = X:" + this.getStoredPosition(stack).getX() + " Y:" + this.getStoredPosition(stack).getY() + " Z:" + this.getStoredPosition(stack).getZ()));
                } else tooltip.add(new TranslationTextComponent("No Block Location Stored"));
            } else tooltip.add(new TranslationTextComponent("No Block Location Stored"));
        }
    }

    public static final Item DEFAULT = new ItemCrystalWrench().setRegistryName(new ResourceLocation(MODID, "crystalwrench"));
    public static final Item CONNECTIONS = new ItemCrystalWrench().setRegistryName(new ResourceLocation(MODID, "crystalwrenchconnect"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(DEFAULT);
        event.getRegistry().register(CONNECTIONS);
    }




}
