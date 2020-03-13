package com.mowmaster.dust.enchants;

import com.google.common.base.Preconditions;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

import static com.mowmaster.dust.references.Reference.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public final class EnchantmentRegistry
{
    public static final EnchantmentType COINUPGRADE = EnchantmentType.create("pedestalupgrade", ItemUpgradeBase.class::isInstance);


    public static final Enchantment OPERATIONSPEED = new EnchantmentOperationSpeed();
    public static final Enchantment RANGE = new EnchantmentRange();
    public static final Enchantment CAPACITY = new EnchantmentCapacity();



    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();
        registry.registerAll(
                // All
                setup(OPERATIONSPEED, "upgradespeed"),
                setup(RANGE, "upgraderange"),
                setup(CAPACITY, "upgradecapacity")
        );
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
        return setup(entry, new ResourceLocation(MODID, name));
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) {
        Preconditions.checkNotNull(entry, "Entry cannot be null!");
        Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
        entry.setRegistryName(registryName);
        return entry;
    }
}
