package com.mowmaster.dust.items.armors;

import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.armors.models.ModelCrystalHelmet;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemCrystalArmor extends ItemArmor
{

    private boolean showHelm=false;
    private boolean showChest=false;
    private boolean showLegs=false;
    private boolean showBoots=false;
    private int colorHelm=0;
    private int colorChest=0;
    private int colorLeg=0;
    private int colorBoot=0;
    public ItemCrystalArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot entityEquipmentSlot, String unlocName, String registryName)
    {
        super(material,renderIndex,entityEquipmentSlot);
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setMaxStackSize(1);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    private int getColorCrystalHelm(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorHelm = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (e.getName().contains("enchantment.protect.fire"))
                {
                    colorHelm = 0;
                }
                else if (e.getName().contains("enchantment.waterWorker"))
                {
                    colorHelm = 8;
                }
                else if (e.getName().contains("enchantment.oxygen"))
                {
                    colorHelm = 16;
                }
                else if (e.getName().contains("enchantment.protect.projectile"))
                {
                    colorHelm = 24;
                }
                else if (e.getName().contains("enchantment.thorns"))
                {
                    colorHelm = 32;
                }
                else if (e.getName().contains("enchantment.protect.explosion"))
                {
                    colorHelm = 40;
                }
                else if (e.getName().contains("enchantment.mending"))
                {
                    colorHelm = 48;
                }
                else if (e.getName().contains("enchantment.protect.all"))
                {
                    colorHelm = 56;
                }
            }
        }
        return colorHelm;
    }

    private int getColorCrystalChest(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorChest = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (e.getName().contains("enchantment.protect.fire"))
                {
                    colorChest = 0;
                }
                else if (e.getName().contains("enchantment.enchantLastResort"))
                {
                    colorChest = 8;
                }
                else if (e.getName().contains("enchantment.enchantSteadfast"))
                {
                    colorChest = 16;
                }
                else if (e.getName().contains("enchantment.protect.projectile"))
                {
                    colorChest = 24;
                }
                else if (e.getName().contains("enchantment.thorns"))
                {
                    colorChest = 32;
                }
                else if (e.getName().contains("enchantment.protect.explosion"))
                {
                    colorChest = 40;
                }
                else if (e.getName().contains("enchantment.mending"))
                {
                    colorChest = 48;
                }
                else if (e.getName().contains("enchantment.protect.all"))
                {
                    colorChest = 56;
                }
            }
        }
        return colorChest;
    }

    private int getColorCrystalLeg(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorLeg = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (e.getName().contains("enchantment.protect.fire"))
                {
                    colorLeg = 0;
                }
                else if (e.getName().contains("enchantment.enchantStepAssist"))
                {
                    colorLeg = 8;
                }
                else if (e.getName().contains("enchantment.enchantQuickPace"))
                {
                    colorLeg = 16;
                }
                else if (e.getName().contains("enchantment.protect.projectile"))
                {
                    colorLeg = 24;
                }
                else if (e.getName().contains("enchantment.thorns"))
                {
                    colorLeg = 32;
                }
                else if (e.getName().contains("enchantment.protect.explosion"))
                {
                    colorLeg = 40;
                }
                else if (e.getName().contains("enchantment.mending"))
                {
                    colorLeg = 48;
                }
                else if (e.getName().contains("enchantment.protect.all"))
                {
                    colorLeg = 56;
                }
            }
        }
        return colorLeg;
    }

    private int getColorCrystalBoot(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorBoot = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            System.out.println(e.getName());

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (e.getName().contains("enchantment.protect.fire"))
                {
                    colorBoot = 0;
                }
                else if (e.getName().contains("enchantment.enchantStepAssist"))
                {
                    colorBoot = 8;
                }
                else if (e.getName().contains("enchantment.enchantQuickPace"))
                {
                    colorBoot = 16;
                }
                else if (e.getName().contains("enchantment.protect.projectile"))
                {
                    colorBoot = 24;
                }
                else if (e.getName().contains("enchantment.thorns"))
                {
                    colorBoot = 32;
                }
                else if (e.getName().contains("enchantment.protect.explosion"))
                {
                    colorBoot = 40;
                }
                else if (e.getName().contains("enchantment.mending"))
                {
                    colorBoot = 48;
                }
                else if (e.getName().contains("enchantment.protect.all"))
                {
                    colorBoot = 56;
                }
            }
        }
        return colorBoot;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(player.inventory.armorItemInSlot(3) !=null && player.inventory.armorItemInSlot(3).getItem() == ItemArmorAndToolsRegistry.crystalHelmet
                && player.inventory.armorItemInSlot(2) !=null && player.inventory.armorItemInSlot(2).getItem() == ItemArmorAndToolsRegistry.crystalChestplate
                && player.inventory.armorItemInSlot(1) !=null && player.inventory.armorItemInSlot(1).getItem() == ItemArmorAndToolsRegistry.crystalLeggings
                && player.inventory.armorItemInSlot(0) !=null && player.inventory.armorItemInSlot(0).getItem() == ItemArmorAndToolsRegistry.crystalBoots)
        {
            this.effectPlayer(player, MobEffects.RESISTANCE,0);
            this.effectPlayer(player, MobEffects.STRENGTH,0);
        }


        if(player.inventory.armorItemInSlot(3) !=null && player.inventory.armorItemInSlot(3).isItemEnchanted())
        {
            getColorCrystalHelm(player.inventory.armorItemInSlot(3));
        }
        else colorHelm=0;

        if(player.inventory.armorItemInSlot(2) !=null && player.inventory.armorItemInSlot(2).isItemEnchanted())
        {
            getColorCrystalChest(player.inventory.armorItemInSlot(2));
        }
        else colorChest=0;

        if(player.inventory.armorItemInSlot(1) !=null && player.inventory.armorItemInSlot(1).isItemEnchanted())
        {
            getColorCrystalLeg(player.inventory.armorItemInSlot(1));
        }
        else colorLeg=0;

        if(player.inventory.armorItemInSlot(0) !=null && player.inventory.armorItemInSlot(0).isItemEnchanted())
        {
            getColorCrystalBoot(player.inventory.armorItemInSlot(0));
        }
        else colorBoot=0;
    }



    private void effectPlayer(EntityPlayer player, Potion potion, int amplifier)
    {
        if(player.getActivePotionEffect(potion) == null ||player.getActivePotionEffect(potion).getDuration() <=1)
        {
            player.addPotionEffect(new PotionEffect(potion,100,amplifier,false,false));
        }
    }


    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(!itemStack.isEmpty())
        {
            if(itemStack.getItem() instanceof ItemArmor)
            {
                ModelCrystalHelmet armorModel = new ModelCrystalHelmet(1.0f,colorHelm,colorChest,colorLeg,colorBoot);
                ModelCrystalHelmet armorModelLegs = new ModelCrystalHelmet(0.5f,colorHelm,colorChest,colorLeg,colorBoot);

                armorModel.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);armorModel.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
                armorModel.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || (armorSlot == EntityEquipmentSlot.CHEST);
                armorModel.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModel.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModelLegs.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
                armorModelLegs.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
                armorModelLegs.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.FEET;
                armorModelLegs.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.FEET;

                armorModel.isSneak = _default.isSneak;
                armorModel.isRiding = _default.isRiding;
                armorModel.isChild = _default.isChild;
                armorModel.rightArmPose = _default.rightArmPose;
                armorModel.leftArmPose = _default.leftArmPose;

                armorModelLegs.isSneak = _default.isSneak;
                armorModelLegs.isRiding = _default.isRiding;
                armorModelLegs.isChild = _default.isChild;
                armorModelLegs.rightArmPose = _default.rightArmPose;
                armorModelLegs.leftArmPose = _default.leftArmPose;

                return armorModel;
            }
        }
        return null;
    }

}