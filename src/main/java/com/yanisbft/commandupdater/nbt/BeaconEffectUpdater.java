package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;

public class BeaconEffectUpdater extends NbtUpdater {

    public BeaconEffectUpdater(String oldKey, String newKey) {
        super(oldKey, newKey);
    }

    @Override
    public void update(NbtCompound nbtRoot) {
        if (nbtRoot.contains(oldKey)) {
            int oldNbt = nbtRoot.getInt(oldKey);
            nbtRoot.remove(oldKey);
            nbtRoot.putString(newKey, EffectUpdater.OLD_TO_NEW.getOrDefault(oldNbt, ""));
        }
    }
}
