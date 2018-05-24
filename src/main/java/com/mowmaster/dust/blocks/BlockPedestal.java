package com.mowmaster.dust.blocks;


import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;



public class BlockPedestal extends Block implements ITileEntityProvider
{
    public BlockPedestal(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();
        return state;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TilePedestal) {
                TilePedestal tilePedestal = (TilePedestal) tileEntity;


                if (playerIn.getHeldItem(hand).isEmpty())
                {
                    System.out.println(tilePedestal.getItemInPedestal().getDisplayName());
                    System.out.println(tilePedestal.getCoinOnPedestal().getDisplayName());


                    if (playerIn.isSneaking())
                    {
                        if(!tilePedestal.getItemInPedestal().equals(ItemStack.EMPTY))
                        {
                            playerIn.inventory.addItemStackToInventory(tilePedestal.removeItem());
                            return true;
                        }
                        else if(!tilePedestal.getCoinOnPedestal().equals(ItemStack.EMPTY))
                        {
                            playerIn.inventory.addItemStackToInventory(tilePedestal.removeCoin());
                            return true;
                        }
                    }
                }
                else if(tilePedestal.getCoinOnPedestal().equals(ItemStack.EMPTY))
                    {
                        if(playerIn.getHeldItem(hand).getItem() instanceof ItemCoin)
                        {
                            if(tilePedestal.addCoin(playerIn.getHeldItem(hand)))
                            {
                                playerIn.getHeldItem(hand).shrink(1);
                                return true;
                            }
                        }
                    }
                else if(tilePedestal.getItemInPedestal().equals(ItemStack.EMPTY))
                {
                    if(tilePedestal.addItem(playerIn.getHeldItem(hand)))
                    {
                        playerIn.getHeldItem(hand).shrink(1);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    private static AxisAlignedBB pedestal = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.75D, 0.8125D);
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return pedestal;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePedestal();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePedestal();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Used To Display Items");
    }
}
