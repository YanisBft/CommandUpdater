package com.yanisbft.commandupdater.nbt;

import java.util.ArrayList;
import java.util.List;

public class NbtUpdaters {
    public static final List<NbtUpdater> ALL = new ArrayList<>();

    public static final NbtUpdater ACTIVE_EFFECTS = register(new EffectUpdater("ActiveEffects", "active_effects"));
    public static final NbtUpdater CUSTOM_POTION_EFFECTS = register(new EffectUpdater("CustomPotionEffects", "custom_potion_effects"));
    public static final NbtUpdater EFFECTS = register(new EffectUpdater("Effects", "effects"));

    public static void init() {
    }

    private static NbtUpdater register(NbtUpdater updater) {
        ALL.add(updater);
        return updater;
    }
}
