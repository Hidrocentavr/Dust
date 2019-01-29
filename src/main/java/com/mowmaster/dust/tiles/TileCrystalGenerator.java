package com.mowmaster.dust.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class TileCrystalGenerator extends TileEntity implements ITickable,IEnergyStorage {

    private ItemStackHandler crystalStackHandler;
    private EnergyStorage storedEnergy;

    public TileCrystalGenerator()
    {
        this.crystalStackHandler = new ItemStackHandler(2);
    }

    public ItemStack getFromInv(int num)
    {
        return this.crystalStackHandler.getStackInSlot(num);
    }

    public ItemStack getcrystalIn()
    {
        return crystalStackHandler.getStackInSlot(0);
    }

    public ItemStack getcrystalOut()
    {
        return crystalStackHandler.getStackInSlot(1);
    }

    public int getEnergyValue()
    {
        return storedEnergy.getEnergyStored();
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 1000;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public void update() {

    }
}
