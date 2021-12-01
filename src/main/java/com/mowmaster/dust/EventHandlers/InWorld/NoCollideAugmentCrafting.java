package com.mowmaster.dust.EventHandlers.InWorld;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.Tools.LinkingTool;
import com.mowmaster.dust.Items.Tools.LinkingToolBackwards;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class NoCollideAugmentCrafting
{
    @SubscribeEvent()
    public static void NoCollideAugment(PlayerInteractEvent.RightClickBlock event)
    {
        //Added to keep fake players from canning this every time?
        if(!(event.getPlayer() instanceof FakePlayer))
        {
            Level worldIn = event.getWorld();
            InteractionHand hand = event.getHand();
            BlockState state = worldIn.getBlockState(event.getPos());
            Player player = event.getPlayer();
            BlockPos pos = event.getPos();

            int posX = event.getPos().getX();
            int posY = event.getPos().getY();
            int posZ = event.getPos().getZ();

            int paper=0;

            if(!worldIn.isClientSide) {
                if ((player.getItemInHand(hand) != null)) {
                    if (player.getItemInHand(hand).getItem() instanceof LinkingTool || player.getItemInHand(hand).getItem() instanceof LinkingToolBackwards) {
                        //List<EntityItem> item = player.level.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
                        List<ItemEntity> items = player.level.getEntitiesOfClass(ItemEntity.class, new AABB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
                        BlockPos getPos = event.getPos();
                        for (ItemEntity item : items) {
                            ItemStack stack = item.getItem();
                            if(stack.getItem().equals(Items.PAPER))
                            {
                                getPos = item.getOnPos().above();
                                DustPacketHandler.sendToNearby(worldIn,getPos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos.getX(),getPos.getY(),getPos.getZ(),200,0,0));
                                DustPacketHandler.sendToNearby(worldIn,getPos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos.getX(),getPos.getY(),getPos.getZ(),100,100,100));
                                List<Animal> animals = worldIn.getEntitiesOfClass(Animal.class,new AABB(getPos));
                                if(animals.size()>0)
                                {
                                    paper +=stack.getCount();
                                    item.remove(Entity.RemovalReason.DISCARDED);
                                }
                            }
                        }

                        if(paper > 0)
                        {
                            worldIn.explode(new ItemEntity(worldIn, posX, posY, posZ,new ItemStack(Items.PAPER)),(DamageSource)null,new EntityBasedExplosionDamageCalculator(player), posX + 0.5, posY + 2.0, posZ + 0.25, 0.0F,false, Explosion.BlockInteraction.NONE);
                            if(paper>0)
                            {
                                //NEED TO ADD ANOTHER TAG TO ITEM TO MAKE IT NOT USEABLE IN COMBINING AGAIN!!!
                                ItemStack stacked = new ItemStack(DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get(),paper);

                                ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,stacked);
                                itemEn.setInvulnerable(true);
                                worldIn.addFreshEntity(itemEn);
                            }
                        }
                    }
                }
            }
        }
    }
}