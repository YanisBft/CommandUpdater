package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentUpdater extends NbtUpdater {
    public static final Map<Integer, String> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put(0, "minecraft:protection");
        OLD_TO_NEW.put(1, "minecraft:fire_protection");
        OLD_TO_NEW.put(2, "minecraft:feather_falling");
        OLD_TO_NEW.put(3, "minecraft:blast_protection");
        OLD_TO_NEW.put(4, "minecraft:projectile_protection");
        OLD_TO_NEW.put(5, "minecraft:respiration");
        OLD_TO_NEW.put(6, "minecraft:aqua_affinity");
        OLD_TO_NEW.put(7, "minecraft:thorns");
        OLD_TO_NEW.put(8, "minecraft:depth_strider");
        OLD_TO_NEW.put(9, "minecraft:frost_walker");
        OLD_TO_NEW.put(10, "minecraft:binding_curse");
        OLD_TO_NEW.put(16, "minecraft:sharpness");
        OLD_TO_NEW.put(17, "minecraft:smite");
        OLD_TO_NEW.put(18, "minecraft:bane_of_arthropods");
        OLD_TO_NEW.put(19, "minecraft:knockback");
        OLD_TO_NEW.put(20, "minecraft:fire_aspect");
        OLD_TO_NEW.put(21, "minecraft:looting");
        OLD_TO_NEW.put(22, "minecraft:sweeping");
        OLD_TO_NEW.put(32, "minecraft:efficiency");
        OLD_TO_NEW.put(33, "minecraft:silk_touch");
        OLD_TO_NEW.put(34, "minecraft:unbreaking");
        OLD_TO_NEW.put(35, "minecraft:fortune");
        OLD_TO_NEW.put(48, "minecraft:power");
        OLD_TO_NEW.put(49, "minecraft:punch");
        OLD_TO_NEW.put(50, "minecraft:flame");
        OLD_TO_NEW.put(51, "minecraft:infinity");
        OLD_TO_NEW.put(61, "minecraft:luck_of_the_sea");
        OLD_TO_NEW.put(62, "minecraft:lure");
        OLD_TO_NEW.put(65, "minecraft:loyalty");
        OLD_TO_NEW.put(66, "minecraft:impaling");
        OLD_TO_NEW.put(67, "minecraft:riptide");
        OLD_TO_NEW.put(68, "minecraft:channeling");
        OLD_TO_NEW.put(70, "minecraft:mending");
        OLD_TO_NEW.put(71, "minecraft:vanishing_curse");
    }

    public EnchantmentUpdater(String oldKey, String newKey) {
        super(oldKey, newKey);
    }

    @Override
    public void update(NbtCompound nbtRoot) {
        if (nbtRoot.contains(oldKey, NbtElement.LIST_TYPE)) {
            NbtList oldNbt = nbtRoot.getList(oldKey, NbtElement.COMPOUND_TYPE);
            NbtList newNbt = new NbtList();

            for (int i = 0; i < oldNbt.size(); i++) {
                NbtCompound oldEnchant = oldNbt.getCompound(i);
                NbtCompound newEnchant = oldEnchant.copy();
                if (oldEnchant.contains("id")) {
                    newEnchant.putString("id", OLD_TO_NEW.getOrDefault(oldEnchant.getInt("id"), ""));
                }
                newNbt.add(newEnchant);
            }

            nbtRoot.remove(oldKey);
            nbtRoot.put(newKey, newNbt);
        }
    }
}
