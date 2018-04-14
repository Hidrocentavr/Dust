package com.mowmaster.dust.blocks.ancientblocks;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;




public class BlockAncientStairs extends BlockStairs
{
    public BlockAncientStairs(String unloc, String registryName, Block block, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super (block.getDefaultState());
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(soundType);
        this.useNeighborBrightness = true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
