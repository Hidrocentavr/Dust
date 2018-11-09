package com.mowmaster.dust.blocks;

import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

//Result of the dust from a crusher
public class BlockDustCloud extends BlockFalling {
    public BlockDustCloud(String unloc, String registryName) {
        super(Material.SAND);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(1);
        this.setResistance(1);
        this.setSoundType(SoundType.SAND);
        //this.setTickRandomly(true);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }
/*
    @Override
    public boolean canCollideCheck(IBlockState state, boolean b)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        return null;
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World par1World, BlockPos pos, Vec3d par5Vec3, Vec3d par6Vec3)
    {
        return null;
    }
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.IGNORE;
    }
    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }
*/
    //Only until the Crusher is implimented
    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }
    /*
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {

        final int CHANCE = 1;
        int rand = random.nextInt(100);
        if (rand <= CHANCE) {
            if (this.equals(BlockRegistry.redDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(redDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 0)));}}
            else if (this.equals(blueDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(blueDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 1)));}}
            else if (this.equals(yellowDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(yellowDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 2)));}}
            else if (this.equals(BlockRegistry.purpleDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(purpleDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 3)));}}
            else if (this.equals(BlockRegistry.orangeDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(orangeDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 5)));}}
            else if (this.equals(greenDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(greenDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 4)));}}
            else if (this.equals(BlockRegistry.whiteDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(whiteDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 6)));}}
            else if (this.equals(BlockRegistry.blackDust)) {if(!(worldIn.getBlockState(pos.down()).getBlock().equals(blackDust))) {worldIn.setBlockToAir(pos);worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 7)));}}
        }
    }
    */
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,3.0F, true);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if(entityIn instanceof EntityPlayer)
        {super.canCollideCheck(this.getDefaultState(),true);}
        else {super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, bool);}
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Can only be gotten from Crushed Crystals,");
        tooltip.add("in next beta release.");
    }
/*
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
            if(entityIn instanceof EntityPlayer)
            {
                EntityPlayer playerIn = (EntityPlayer) entityIn;
                if(playerIn.hasAchievement(AchievementHandler.achievementDust))
                {
                    playerIn.addStat(AchievementHandler.achievementDust);
                }
            }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if(playerIn.hasAchievement(AchievementHandler.achievementDust))
        {
            playerIn.addStat(AchievementHandler.achievementDust);
        }
    }
    */
}

