package com.yanisbft.commandupdater.nbt;

import java.util.ArrayList;
import java.util.List;

public class NbtUpdaters {
    public static final List<NbtUpdater> ALL = new ArrayList<>();

    public static final NbtUpdater CAT_VARIANT = register(new CatVariantUpdater("CatType", "variant"));
    public static final NbtUpdater ENCHANTMENTS = register(new EnchantmentUpdater("ench", "Enchantments"));
    public static final NbtUpdater ATTRIBUTES = register(new AttributeUpdater("Attributes"));
    public static final NbtUpdater ATTRIBUTE_MODIFIERS = register(new AttributeUpdater("AttributeModifiers"));
    public static final NbtUpdater ACTIVE_EFFECTS = register(new EffectUpdater("ActiveEffects", "active_effects"));
    public static final NbtUpdater CUSTOM_POTION_EFFECTS = register(new EffectUpdater("CustomPotionEffects", "custom_potion_effects"));
    public static final NbtUpdater EFFECTS = register(new EffectUpdater("Effects", "effects"));
    public static final NbtUpdater PRIMARY_EFFECT = register(new BeaconEffectUpdater("Primary", "primary_effect"));
    public static final NbtUpdater SECONDARY_EFFECT = register(new BeaconEffectUpdater("Secondary", "secondary_effect"));

    public static void init() {
    }

    private static NbtUpdater register(NbtUpdater updater) {
        ALL.add(updater);
        return updater;
    }
}
