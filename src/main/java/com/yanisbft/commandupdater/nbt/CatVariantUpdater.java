package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class CatVariantUpdater extends NbtUpdater {
    private static final Map<Integer, String> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put(0, "minecraft:tabby");
        OLD_TO_NEW.put(1, "minecraft:black");
        OLD_TO_NEW.put(2, "minecraft:red");
        OLD_TO_NEW.put(3, "minecraft:siamese");
        OLD_TO_NEW.put(4, "minecraft:british_shorthair");
        OLD_TO_NEW.put(5, "minecraft:calico");
        OLD_TO_NEW.put(6, "minecraft:persian");
        OLD_TO_NEW.put(7, "minecraft:ragdoll");
        OLD_TO_NEW.put(8, "minecraft:white");
        OLD_TO_NEW.put(9, "minecraft:jellie");
        OLD_TO_NEW.put(10, "minecraft:all_black");
    }

    public CatVariantUpdater(String oldKey, String newKey) {
        super(oldKey, newKey);
    }

    @Override
    public void update(NbtCompound nbtRoot) {
        if (nbtRoot.contains(oldKey)) {
            int oldNbt = nbtRoot.getInt(oldKey);
            nbtRoot.remove(oldKey);
            nbtRoot.putString(newKey, OLD_TO_NEW.getOrDefault(oldNbt, ""));
        }
    }
}
