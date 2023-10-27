package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;

public abstract class NbtUpdater {
    protected String oldKey;
    protected String newKey;

    public NbtUpdater(String oldKey, String newKey) {
        this.oldKey = oldKey;
        this.newKey = newKey;
    }

    public abstract void update(NbtCompound nbtRoot);

    protected void updateKey(NbtCompound newNbt, String newKey, NbtCompound oldNbt, String oldKey) {
        if (oldNbt.contains(oldKey)) {
            newNbt.put(newKey, oldNbt.get(oldKey));
        }
    }
}
