package com.mowmaster.dust.Block.Pedestal;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Items.Tools.IPedestalTool;
import com.mowmaster.dust.Items.Tools.LinkingTool;
import com.mowmaster.dust.Items.Tools.LinkingToolBackwards;
import com.mowmaster.dust.Items.Upgrades.Pedestal.IPedestalUpgrade;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

import static com.mowmaster.dust.References.Constants.MODID;

public class BasePedestalBlock extends BaseColoredBlock implements SimpleWaterloggedBlock, EntityBlock
{
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    //0 = default
    //1= whitelist
    //2= blacklist
    public static final IntegerProperty FILTER_STATUS = IntegerProperty.create("filter_status", 0, 2);
    protected final VoxelShape CUP;
    protected final VoxelShape CDOWN;
    protected final VoxelShape CNORTH;
    protected final VoxelShape CSOUTH;
    protected final VoxelShape CEAST;
    protected final VoxelShape CWEST;
    protected final VoxelShape LCUP;
    protected final VoxelShape LCDOWN;
    protected final VoxelShape LCNORTH;
    protected final VoxelShape LCSOUTH;
    protected final VoxelShape LCEAST;
    protected final VoxelShape LCWEST;

    public BasePedestalBlock(Properties p_152915_)
    {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, 0));
        this.CUP = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
                Block.box(5.0D, 2.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                Block.box(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        /*this.CDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(5.0D, 14.0D, 5.0D, 11.0D, 6.0D, 11.0D),
                Block.box(4.0D, 6.0D, 4.0D, 12.0D, 4.0D, 12.0D));*/
        this.CDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(5.0D, 6.0D, 5.0D, 11.0D, 14.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
        //height goes in the -z direction
        this.CNORTH = Shapes.or(Block.box(3.0D, 3.0D, 14.0D, 13.0D, 13.0D, 16.0D),
                Block.box(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 14.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 6.0D));
        /*this.CSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 2.0D, 13.0D, 13.0D, 0.0D),
                Block.box(5.0D, 5.0D, 10.0D, 11.0D, 11.0D, 2.0D),
                Block.box(4.0D, 4.0D, 12.0D, 12.0D, 12.0D, 10.0D));*/
        this.CSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 2.0D),
                Block.box(5.0D, 5.0D, 2.0D, 11.0D, 11.0D, 10.0D),
                Block.box(4.0D, 4.0D, 10.0D, 12.0D, 12.0D, 12.0D));
        //height goes in the +x direction
        /*this.CEAST = Shapes.or(Block.box(2.0D, 3.0D, 3.0D, 0.0D, 13.0D, 13.0D),
                Block.box(2.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));*/
        this.CEAST = Shapes.or(Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D),
                Block.box(2.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        /*this.CWEST = Shapes.or(Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                Block.box(14.0D, 5.0D, 5.0D, 6.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 6.0D, 12.0D, 12.0D));*/
        this.CWEST = Shapes.or(Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                Block.box(6.0D, 5.0D, 5.0D, 14.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 6.0D, 12.0D, 12.0D));

        this.LCUP = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
                Block.box(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
                Block.box(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
                Block.box(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
                Block.box(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                Block.box(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        /*this.LCDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(4.0D, 13.0D, 4.0D, 12.0D, 14.0D, 12.0D),
                Block.box(5.0D, 13.0D, 5.0D, 11.0D, 12.0D, 11.0D),
                Block.box(4.5D, 12.0D, 4.5D, 11.5D, 11.0D, 11.5D),
                Block.box(5.0D, 11.0D, 5.0D, 11.0D, 6.0D, 11.0D),
                Block.box(4.0D, 6.0D, 4.0D, 12.0D, 4.0D, 12.0D));*/
        this.LCDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(4.0D, 13.0D, 4.0D, 12.0D, 14.0D, 12.0D),
                Block.box(5.0D, 12.0D, 5.0D, 11.0D, 13.0D, 11.0D),
                Block.box(4.5D, 11.0D, 4.5D, 11.5D, 12.0D, 11.5D),
                Block.box(5.0D, 6.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
        //height goes in the -z direction
        this.LCNORTH = Shapes.or(Block.box(3.0D, 3.0D, 14.0D, 13.0D, 13.0D, 16.0D),
                Block.box(4.0D, 4.0D, 13.0D, 12.0D, 12.0D, 14.0D),
                Block.box(5.0D, 5.0D, 12.0D, 11.0D, 11.0D, 13.0D),
                Block.box(4.5D, 4.5D, 11.0D, 11.5D, 11.5D, 12.0D),
                Block.box(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 6.0D));
        /*this.LCSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 2.0D, 13.0D, 13.0D, 0.0D),
                Block.box(4.0D, 4.0D, 3.0D, 12.0D, 12.0D, 2.0D),
                Block.box(5.0D, 5.0D, 4.0D, 11.0D, 11.0D, 3.0D),
                Block.box(4.5D, 4.5D, 5.0D, 11.5D, 11.5D, 4.0D),
                Block.box(5.0D, 5.0D, 10.0D, 11.0D, 11.0D, 5.0D),
                Block.box(4.0D, 4.0D, 12.0D, 12.0D, 12.0D, 10.0D));*/
        this.LCSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 2.0D),
                Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 3.0D),
                Block.box(5.0D, 5.0D, 3.0D, 11.0D, 11.0D, 4.0D),
                Block.box(4.5D, 4.5D, 4.0D, 11.5D, 11.5D, 5.0D),
                Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 10.0D),
                Block.box(4.0D, 4.0D, 10.0D, 12.0D, 12.0D, 12.0D));
        //height goes in the +x direction
        /*this.LCEAST = Shapes.or(Block.box(2.0D, 3.0D, 3.0D, 0.0D, 13.0D, 13.0D),
                Block.box(2.0D, 4.0D, 4.0D, 3.0D, 12.0D, 12.0D),
                Block.box(3.0D, 5.0D, 5.0D, 4.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.5D, 4.5D, 5.0D, 11.5D, 11.5D),
                Block.box(5.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));*/
        this.LCEAST = Shapes.or(Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D),
                Block.box(2.0D, 4.0D, 4.0D, 3.0D, 12.0D, 12.0D),
                Block.box(3.0D, 5.0D, 5.0D, 4.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.5D, 4.5D, 5.0D, 11.5D, 11.5D),
                Block.box(5.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        this.LCWEST = Shapes.or(Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                Block.box(13.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),
                Block.box(12.0D, 5.0D, 5.0D, 13.0D, 11.0D, 11.0D),
                Block.box(11.0D, 4.5D, 4.5D, 12.0D, 11.5D, 11.5D),
                Block.box(6.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 6.0D, 12.0D, 12.0D));
    }

    public VoxelShape getShape(BlockState p_152021_, BlockGetter p_152022_, BlockPos p_152023_, CollisionContext p_152024_) {
        Direction direction = p_152021_.getValue(FACING);
        switch(direction) {
            case NORTH:
                return (p_152021_.getValue(LIT)) ? (this.LCNORTH) : (this.CNORTH);
            case SOUTH:
                return (p_152021_.getValue(LIT)) ? (this.LCSOUTH) : (this.CSOUTH);
            case EAST:
                return (p_152021_.getValue(LIT)) ? (this.LCEAST) : (this.CEAST);
            case WEST:
                return (p_152021_.getValue(LIT)) ? (this.LCWEST) : (this.CWEST);
            case DOWN:
                return (p_152021_.getValue(LIT)) ? (this.LCDOWN) : (this.CDOWN);
            case UP:
            default:
                return (p_152021_.getValue(LIT)) ? (this.LCUP) : (this.CUP);
        }
    }



    public BlockState updateShape(BlockState p_152036_, Direction p_152037_, BlockState p_152038_, LevelAccessor p_152039_, BlockPos p_152040_, BlockPos p_152041_) {
        if (p_152036_.getValue(WATERLOGGED)) {
            p_152039_.getFluidTicks();
            //.scheduleTick(p_152040_, Fluids.WATER, Fluids.WATER.getTickDelay(p_152039_))
        }

        return p_152037_ == p_152036_.getValue(FACING).getOpposite() && !p_152036_.canSurvive(p_152039_, p_152040_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152036_, p_152037_, p_152038_, p_152039_, p_152040_, p_152041_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
        LevelAccessor levelaccessor = p_152019_.getLevel();
        BlockPos blockpos = p_152019_.getClickedPos();
        Direction direction = p_152019_.getClickedFace();
        BlockState blockstate = p_152019_.getLevel().getBlockState(p_152019_.getClickedPos().relative(direction.getOpposite()));
        int getColor = ColorReference.getColorFromStateInt(blockstate);
        //Lit and Filter can never be anything other then default when placing the block
        //Also copied the facing direction stuff from EndRodBlock
        return blockstate.is(this) &&
                blockstate.getValue(FACING) == direction
                ?
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, 0)
                :
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(FACING, direction).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, 0);

    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @org.jetbrains.annotations.Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        if(!p_49847_.isClientSide())
        {
            if(p_49850_ instanceof Player)
            {
                Player player = ((Player)p_49850_);
                TranslatableComponent linksucess = new TranslatableComponent(MODID + ".tool_link_success");
                linksucess.withStyle(ChatFormatting.WHITE);
                TranslatableComponent linkunsuccess = new TranslatableComponent(MODID + ".tool_link_unsucess");
                linkunsuccess.withStyle(ChatFormatting.WHITE);
                TranslatableComponent linkremoved = new TranslatableComponent(MODID + ".tool_link_removed");
                linkremoved.withStyle(ChatFormatting.WHITE);
                TranslatableComponent linkitsself = new TranslatableComponent(MODID + ".tool_link_itsself");
                linkitsself.withStyle(ChatFormatting.WHITE);
                TranslatableComponent linknetwork = new TranslatableComponent(MODID + ".tool_link_network");
                linknetwork.withStyle(ChatFormatting.WHITE);
                TranslatableComponent linkdistance = new TranslatableComponent(MODID + ".tool_link_distance");
                linkdistance.withStyle(ChatFormatting.WHITE);
                if(player.getOffhandItem().getItem().equals(DeferredRegisterItems.TOOL_LINKINGTOOL.get()))
                {
                    LinkingTool tool = ((LinkingTool)player.getOffhandItem().getItem());
                    if(player.getOffhandItem().hasTag() && player.getOffhandItem().isEnchanted())
                    {
                        //Checks if clicked blocks is a Pedestal
                        if(p_49847_.getBlockState(p_49848_).getBlock() instanceof BasePedestalBlock)
                        {
                            //Checks Tile at location to make sure its a TilePedestal
                            BlockEntity tileEntity = p_49847_.getBlockEntity(p_49848_);
                            if (tileEntity instanceof BasePedestalBlockEntity) {
                                BasePedestalBlockEntity tilePedestal = (BasePedestalBlockEntity) tileEntity;

                                BlockEntity tileEntitySender = p_49847_.getBlockEntity(p_49848_);
                                if (tileEntity instanceof BasePedestalBlockEntity) {
                                    BasePedestalBlockEntity tileSender = (BasePedestalBlockEntity) tileEntitySender;

                                    //checks if connecting pedestal is out of range of the senderPedestal
                                    if(tilePedestal.isPedestalInRange(tileSender,tool.getStoredPosition(player.getOffhandItem())))
                                    {
                                        //Checks if pedestals to be linked are on same networks or if one is neutral
                                        if(tilePedestal.canLinkToPedestalNetwork(tool.getStoredPosition(player.getOffhandItem())))
                                        {
                                            //If stored location isnt the same as the connecting pedestal
                                            if(!tilePedestal.isSamePedestal(tool.getStoredPosition(player.getOffhandItem())))
                                            {
                                                //Checks if the conenction hasnt been made once already yet
                                                if(!tilePedestal.isAlreadyLinked(tool.getStoredPosition(player.getOffhandItem())))
                                                {
                                                    //Checks if senderPedestal has locationSlots available
                                                    //System.out.println("Stored Locations: "+ tilePedestal.getNumberOfStoredLocations());
                                                    if(tilePedestal.storeNewLocation(tool.getStoredPosition(player.getOffhandItem())))
                                                    {
                                                        player.sendMessage(linksucess, Util.NIL_UUID);
                                                    }
                                                    else player.sendMessage(linkunsuccess,Util.NIL_UUID);
                                                }
                                            }
                                            else player.sendMessage(linkitsself,Util.NIL_UUID);
                                        }
                                        else player.sendMessage(linknetwork,Util.NIL_UUID);
                                    }
                                    else player.sendMessage(linkdistance, Util.NIL_UUID);
                                }
                            }
                        }
                    }
                }
                else if(player.getOffhandItem().getItem().equals(DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get()))
                {
                    LinkingToolBackwards tool = ((LinkingToolBackwards)player.getOffhandItem().getItem());
                    if(player.getOffhandItem().hasTag() && player.getOffhandItem().isEnchanted())
                    {
                        //Checks if clicked blocks is a Pedestal
                        if(p_49847_.getBlockState(p_49848_).getBlock() instanceof BasePedestalBlock)
                        {
                            //Checks Tile at location to make sure its a TilePedestal
                            BlockEntity tileEntity = p_49847_.getBlockEntity(tool.getStoredPosition(player.getOffhandItem()));
                            if (tileEntity instanceof BasePedestalBlockEntity) {
                                BasePedestalBlockEntity tilePedestal = (BasePedestalBlockEntity) tileEntity;

                                BlockEntity tileEntitySender = p_49847_.getBlockEntity(tool.getStoredPosition(player.getOffhandItem()));
                                if (tileEntity instanceof BasePedestalBlockEntity) {
                                    BasePedestalBlockEntity tileSender = (BasePedestalBlockEntity) tileEntitySender;

                                    //checks if connecting pedestal is out of range of the senderPedestal
                                    if(tileSender.isPedestalInRange(tilePedestal,p_49848_))
                                    {
                                        //Checks if pedestals to be linked are on same networks or if one is neutral
                                        if(tileSender.canLinkToPedestalNetwork(p_49848_))
                                        {
                                            //If stored location isnt the same as the connecting pedestal
                                            if(!tileSender.isSamePedestal(p_49848_))
                                            {
                                                //Checks if the conenction hasnt been made once already yet
                                                if(!tileSender.isAlreadyLinked(p_49848_))
                                                {
                                                    //Checks if senderPedestal has locationSlots available
                                                    //System.out.println("Stored Locations: "+ tilePedestal.getNumberOfStoredLocations());
                                                    if(tileSender.storeNewLocation(p_49848_))
                                                    {
                                                        player.sendMessage(linksucess, Util.NIL_UUID);
                                                    }
                                                    else player.sendMessage(linkunsuccess,Util.NIL_UUID);
                                                }
                                            }
                                            else player.sendMessage(linkitsself,Util.NIL_UUID);
                                        }
                                        else player.sendMessage(linknetwork,Util.NIL_UUID);
                                    }
                                    else player.sendMessage(linkdistance, Util.NIL_UUID);
                                }
                            }
                        }
                    }
                }
            }
        }

        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(FACING, p_152034_.rotate(p_152033_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState p_152045_) {
        return p_152045_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_152045_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(WATERLOGGED, FACING, COLOR_RED, COLOR_GREEN, COLOR_BLUE, LIT, FILTER_STATUS);
    }

    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.IGNORE;
    }

    //Left Click Action
    @Override
    public void attack(BlockState p_60499_, Level p_60500_, BlockPos p_60501_, Player p_60502_) {
        if(!p_60500_.isClientSide())
        {
            BlockEntity blockEntity = p_60500_.getBlockEntity(p_60501_);
            if(blockEntity instanceof BasePedestalBlockEntity)
            {
                BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity)blockEntity);
                ItemStack itemInHand = p_60502_.getMainHandItem();
                ItemStack itemInOffHand = p_60502_.getOffhandItem();

                if(pedestal.hasCoin() && itemInOffHand.getItem().equals(DeferredRegisterItems.TOOL_UPGRADETOOL.get()))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeCoin());
                }
                else if(pedestal.hasFilter() && itemInOffHand.getItem().equals(DeferredRegisterItems.TOOL_FILTERTOOL.get()))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeFilter(true));
                }
                else if(pedestal.hasLight() && itemInOffHand.getItem().equals(Items.GLOWSTONE))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeLight());
                }
                else if(pedestal.hasRedstone() && itemInOffHand.getItem().equals(Items.REDSTONE))
                {
                    if(p_60502_.isCrouching())
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeAllRedstone());
                    }
                    else
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeRedstone());
                    }
                }
                else if(pedestal.hasRRobin() && itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get()))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeRRobin());
                }
                else if(pedestal.hasRenderAugment() && itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get()))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeRenderAugment());
                }
                else if(pedestal.hasNoCollide() && itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get()))
                {
                    ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeNoCollide());
                }
                else if(pedestal.hasItem())
                {
                    if(p_60502_.isCrouching())
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeItem());
                    }
                    else
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,pedestal.removeItem(1));
                    }
                }
            }
        }

        super.attack(p_60499_, p_60500_, p_60501_, p_60502_);
    }

    //Right Click Action
    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            BlockEntity blockEntity = p_60504_.getBlockEntity(p_60505_);
            if(blockEntity instanceof BasePedestalBlockEntity)
            {
                BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity)blockEntity);

                ItemStack itemInHand = p_60506_.getMainHandItem();
                ItemStack itemInOffHand = p_60506_.getOffhandItem();
                if(itemInHand.getItem() instanceof IPedestalTool)
                {

                    if(itemInHand.getItem().equals(DeferredRegisterItems.TOOL_LINKINGTOOL.get()) || itemInHand.getItem().equals(DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get())){
                        boolean getCurrentRender = pedestal.getRenderRange();
                        pedestal.setRenderRange(!getCurrentRender);
                    }
                    return InteractionResult.FAIL;
                }
                else if(itemInHand.getItem() instanceof ColorApplicator)
                {
                    int getColor = ColorReference.getColorFromItemStackInt(itemInHand);
                    BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                    p_60504_.setBlock(p_60505_,newState,3);
                    return InteractionResult.SUCCESS;
                }
                else if(itemInOffHand.getItem() instanceof ColorApplicator)
                {
                    int getColor = ColorReference.getColorFromItemStackInt(itemInOffHand);
                    BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                    p_60504_.setBlock(p_60505_,newState,3);
                    return InteractionResult.SUCCESS;
                }
                else if(itemInOffHand.getItem() instanceof IPedestalUpgrade)
                {
                    if(!pedestal.hasCoin() && pedestal.addCoin(p_60506_,itemInOffHand,true))
                    {
                        pedestal.addCoin(p_60506_,itemInOffHand,false);
                        p_60506_.getOffhandItem().shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(itemInOffHand.getItem() instanceof IPedestalFilter)
                {
                    if(!pedestal.hasFilter() && pedestal.addFilter(itemInOffHand,true))
                    {
                        pedestal.addFilter(itemInOffHand,false);
                        p_60506_.getOffhandItem().shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(itemInOffHand.getItem().equals(Items.GLOWSTONE))
                {
                    if(!pedestal.hasLight())
                    {
                        if(pedestal.addLight())
                        {
                            p_60506_.getOffhandItem().shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInOffHand.getItem().equals(Items.REDSTONE))
                {
                    if(pedestal.getRedstonePowerNeeded()<15)
                    {
                        if(pedestal.addRedstone())
                        {
                            p_60506_.getOffhandItem().shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get()))
                {
                    if(!pedestal.hasRRobin())
                    {
                        if(pedestal.addRRobin(p_60506_.getOffhandItem()))
                        {
                            p_60506_.getOffhandItem().shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get()))
                {
                    if(!pedestal.hasRenderAugment())
                    {
                        if(pedestal.addRenderAugment(p_60506_.getOffhandItem()))
                        {
                            p_60506_.getOffhandItem().shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInOffHand.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get()))
                {
                    if(!pedestal.hasNoCollide())
                    {
                        if(pedestal.addNoCollide(p_60506_.getOffhandItem()))
                        {
                            p_60506_.getOffhandItem().shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInHand.getItem().equals(Items.BUCKET))
                {
                    if(pedestal.hasFluid())
                    {
                        Item item = pedestal.getStoredFluid().copy().getFluid().getBucket();
                        if(item instanceof BucketItem)
                        {
                            BucketItem bucketItem = (BucketItem) item;
                            itemInHand.shrink(1);
                            ItemHandlerHelper.giveItemToPlayer(p_60506_,new ItemStack(bucketItem));
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInHand.isEmpty())
                {
                    if(p_60506_.isCrouching())
                    {
                        if(pedestal.hasRedstone())
                        {
                            String redstone = "Redstone Signal Strength Needed to Disable Pedestal: "+ pedestal.getRedstonePowerNeeded();
                            TranslatableComponent itemCountInPedestal = new TranslatableComponent(redstone);
                            itemCountInPedestal.withStyle(ChatFormatting.LIGHT_PURPLE);
                            p_60506_.displayClientMessage(itemCountInPedestal,true);
                        }

                        if(pedestal.hasFluid())
                        {
                            String fluid = pedestal.getStoredFluid().getDisplayName().getString() +": " +pedestal.getStoredFluid().getAmount() +"/"+pedestal.getFluidCapacity();
                            TranslatableComponent pedestalFluid = new TranslatableComponent(fluid);
                            pedestalFluid.withStyle(ChatFormatting.RED);
                            p_60506_.sendMessage(pedestalFluid,Util.NIL_UUID);
                        }

                        if(pedestal.hasEnergy())
                        {
                            String energy = "Energy: "+ pedestal.getStoredEnergy() +"/"+pedestal.getEnergyCapacity();
                            TranslatableComponent pedestalEnergy = new TranslatableComponent(energy);
                            pedestalEnergy.withStyle(ChatFormatting.RED);
                            p_60506_.sendMessage(pedestalEnergy,Util.NIL_UUID);
                        }

                        if(pedestal.hasExperience())
                        {
                            String experience = "Experience: "+ pedestal.getStoredExperience() +"/"+pedestal.getExperienceCapacity();
                            TranslatableComponent pedestalExperience = new TranslatableComponent(experience);
                            pedestalExperience.withStyle(ChatFormatting.GREEN);
                            p_60506_.sendMessage(pedestalExperience,Util.NIL_UUID);
                        }

                    }
                    else
                    {
                        if(pedestal.hasItem())
                        {
                            TranslatableComponent itemCountInPedestal = new TranslatableComponent(pedestal.getItemInPedestal().getDisplayName().getString() + " " + pedestal.getItemInPedestal().getCount());
                            itemCountInPedestal.withStyle(ChatFormatting.WHITE);
                            p_60506_.displayClientMessage(itemCountInPedestal,true);
                        }
                    }
                }
                else
                {
                    int allowedInsert = pedestal.countAllowedForInsert(itemInHand);
                    ItemStack stackToInsert = itemInHand.copy();
                    int countToSet = (allowedInsert>itemInHand.getCount())?(itemInHand.getCount()):(allowedInsert);
                    stackToInsert.setCount(countToSet);
                    ItemStack returnStack = (itemInHand.getCount()>countToSet)?(new ItemStack(itemInHand.getItem(),itemInHand.getCount()-countToSet)):(ItemStack.EMPTY);

                    if(!itemInHand.isEmpty() && allowedInsert>0)
                    {
                        if(pedestal.addItem(stackToInsert,true))
                        {
                            pedestal.addItem(stackToInsert,false);
                            p_60506_.setItemInHand(InteractionHand.MAIN_HAND,returnStack);
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }

            }

        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        /*BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            return (state.getValue(LIT))?(pedestal.getLightBrightness()):(0);
        }*/

        return (state.getValue(LIT))?(15):(0);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return super.canHarvestBlock(state, world, pos, player);
    }

    @Override
    public void playerDestroy(Level p_49827_, Player p_49828_, BlockPos p_49829_, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
        if(!p_49827_.isClientSide())
        {
            if (p_49830_.getBlock() instanceof BasePedestalBlock) {
                if (!p_49827_.isClientSide && !p_49828_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    int getColor = ColorReference.getColorFromStateInt(p_49830_);
                    ItemStack newStack = ColorReference.addColorToItemStack(itemstack,getColor);
                    newStack.setCount(1);
                    ItemEntity itementity = new ItemEntity(p_49827_, (double)p_49829_.getX() + 0.5D, (double)p_49829_.getY() + 0.5D, (double)p_49829_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_49827_.addFreshEntity(itementity);
                }
            }
        }
        super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
        p_49827_.removeBlock(p_49829_,false);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player.isCreative()) {
            if (player.getOffhandItem().getItem().equals(DeferredRegisterItems.TOOL_DEVTOOL.get()))
                return willHarvest || super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
            else
                attack(state, world, pos, player);

            return false;
        }

        return willHarvest || super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock())
        {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof BasePedestalBlockEntity) {
                BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
                pedestal.dropInventoryItems(p_60516_,p_60517_);
                pedestal.dropInventoryItemsPrivate(p_60516_,p_60517_);
                pedestal.dropLiquidsInWorld(p_60516_,p_60517_);
                pedestal.removeEnergyFromBrokenPedestal(p_60516_,p_60517_);
                pedestal.dropXPInWorld(p_60516_,p_60517_);

                p_60516_.updateNeighbourForOutputSignal(p_60517_,p_60518_.getBlock());
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }

    @Override
    public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {

        if(!p_56212_.isClientSide())
        {
            if (p_56214_.getBlock() instanceof BasePedestalBlock) {
                if (!p_56212_.isClientSide && !p_56215_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    int getColor = ColorReference.getColorFromStateInt(p_56214_);
                    ItemStack newStack = ColorReference.addColorToItemStack(itemstack,getColor);
                    newStack.setCount(1);
                    ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_56212_.addFreshEntity(itementity);
                }
            }
        }
        super.playerWillDestroy(p_56212_, p_56213_, p_56214_, p_56215_);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    private static int getRedstoneLevelPedestal(Level worldIn, BlockPos pos)
    {
        int hasItem=0;
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            ItemStack itemstack = pedestal.getItemInPedestal();
            ItemStack coin = pedestal.getCoinOnPedestal();
            /*if(coin.getItem() instanceof IPedestalUpgrade)
            {
                return ((IPedestalUpgrade)coin.getItem()).getComparatorRedstoneLevel(worldIn,pos);
            }*/
            if(!itemstack.isEmpty())
            {
                float f = (float)itemstack.getCount()/(float)itemstack.getMaxStackSize();
                hasItem = (int)Math.floor(f*14.0F)+1;
            }
        }

        return hasItem;
    }

    /*@Override
    public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction side) {
        return state.shouldCheckWeakPower(world,pos,side);
    }*/

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_60457_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState p_60487_, Level p_60488_, BlockPos p_60489_) {
        return getRedstoneLevelPedestal(p_60488_,p_60489_);
    }

    public RenderShape getRenderShape(BlockState p_50950_) {
        return RenderShape.MODEL;
    }


    public void onProjectileHit(Level p_153713_, BlockState p_153714_, BlockHitResult p_153715_, Projectile p_153716_)
    {
        if (p_153713_.isThundering() && p_153716_ instanceof ThrownTrident && ((ThrownTrident)p_153716_).isChanneling()) {
            BlockPos blockpos = p_153715_.getBlockPos();
            if (p_153713_.canSeeSky(blockpos)) {
                BlockEntity be = p_153713_.getBlockEntity(blockpos);
                {
                    if(be instanceof BasePedestalBlockEntity)
                    {
                        BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity)be);
                        if(pedestal.hasEnergy())
                        {
                            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(p_153713_);
                            lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos.above()));
                            Entity entity = p_153716_.getOwner();
                            lightningbolt.setCause(entity instanceof ServerPlayer ? (ServerPlayer)entity : null);
                            p_153713_.addFreshEntity(lightningbolt);
                            //Todo: remove the energy add thing later
                            pedestal.addEnergy(1000,false);
                            //pedestal.removeEnergy(pedestal.getStoredEnergy(),false);
                            p_153713_.playSound((Player)null, blockpos, SoundEvents.TRIDENT_THUNDER, SoundSource.WEATHER, 5.0F, 1.0F);
                        }
                    }
                }
            }
        }

    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.PEDESTAL.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((BasePedestalBlockEntity) blockEntity).tick();
    }
}
