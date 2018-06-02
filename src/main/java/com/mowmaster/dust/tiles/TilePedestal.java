package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.items.ItemCoin;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class TilePedestal extends TileEntity implements ITickable
{
    private ItemStack item = ItemStack.EMPTY;
    private ItemStack coin = ItemStack.EMPTY;
    private ItemStack display = ItemStack.EMPTY;

    public ItemStack getItemInPedestal() {return item;}
    public ItemStack getCoinOnPedestal() {return coin;}
    public ItemStack getDisplay() {return display;}
    public boolean hasItem()
    {
        if(item.isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public boolean hasCoin()
    {
        if(coin.isEmpty())
        {
            return false;
        }
        else  return true;
    }

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    public boolean isEqualTo(ItemStack itemFromBlock)
    {
        if(itemFromBlock.getItem() instanceof ItemCoin)
        {
            return true;
        }
        return false;
    }

    public boolean addItem(ItemStack itemFromBlock)
    {
        item = itemFromBlock.copy();
        item.setCount(1);
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        coin = coinFromBlock.copy();
        coin.setCount(1);
        updateBlock();
        return true;
    }

    public ItemStack removeItem() {
        ItemStack old = item.copy();
        item = ItemStack.EMPTY;
        updateBlock();
        return old;
    }
    public ItemStack removeCoin() {
        ItemStack old = coin.copy();
        coin = ItemStack.EMPTY;
        updateBlock();
        return old;
    }

    private int ticker=0;
    @Override
    public void update() {

        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);
        if(!world.isRemote)
        {
            if(isBlockUnder(0,-1,0))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.UP))
                    {
                        display = getNextSlotInChest(0,-1,0);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
            if(isBlockUnder(0,1,0))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.DOWN))
                    {
                        display = getNextSlotInChest(0,1,0);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
            if(isBlockUnder(0,0,1))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.NORTH))
                    {
                        display = getNextSlotInChest(0,0,1);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
            if(isBlockUnder(0,0,-1))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.SOUTH))
                    {
                        display = getNextSlotInChest(0,0,-1);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
            if(isBlockUnder(-1,0,0))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.EAST))
                    {
                        display = getNextSlotInChest(-1,0,0);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
            if(isBlockUnder(1,0,0))
            {
                ticker++;
                if(ticker>=20)
                {
                    if (enumfacing.equals(EnumFacing.WEST))
                    {
                        display = getNextSlotInChest(1,0,0);
                        updateBlock();
                        ticker=0;
                    }
                }
            }
        }
    }

    public boolean isBlockUnder(int x,int y,int z)
    {
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        if(tileEntity instanceof TileEntityLockable)
        {
            return true;
        }
        return false;
    }

    public ItemStack getNextSlotInChest(int x,int y,int z)
    {
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        return IntStream.range(0,((TileEntityLockable) tileEntity).getSizeInventory())//Int Range
                .mapToObj(((TileEntityLockable) tileEntity)::getStackInSlot)//Function being applied to each interval
                .filter(itemStack -> !itemStack.isEmpty())
                .findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("coin",coin.writeToNBT(new NBTTagCompound()));
        compound.setTag("item",item.writeToNBT(new NBTTagCompound()));
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound itemCoin = compound.getCompoundTag("coin");
        this.coin = new ItemStack(itemCoin);
        NBTTagCompound itemTag = compound.getCompoundTag("item");
        this.item = new ItemStack(itemTag);
        NBTTagCompound itemTagD = compound.getCompoundTag("display");
        this.display = new ItemStack(itemTagD);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
}
