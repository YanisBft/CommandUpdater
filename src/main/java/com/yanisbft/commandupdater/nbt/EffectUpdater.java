package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public class EffectUpdater extends NbtUpdater {
    public static final Map<Integer, String> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put(1, "minecraft:speed");
        OLD_TO_NEW.put(2, "minecraft:slowness");
        OLD_TO_NEW.put(3, "minecraft:haste");
        OLD_TO_NEW.put(4, "minecraft:mining_fatigue");
        OLD_TO_NEW.put(5, "minecraft:strength");
        OLD_TO_NEW.put(6, "minecraft:instant_health");
        OLD_TO_NEW.put(7, "minecraft:instant_damage");
        OLD_TO_NEW.put(8, "minecraft:jump_boost");
        OLD_TO_NEW.put(9, "minecraft:nausea");
        OLD_TO_NEW.put(10, "minecraft:regeneration");
        OLD_TO_NEW.put(11, "minecraft:resistance");
        OLD_TO_NEW.put(12, "minecraft:fire_resistance");
        OLD_TO_NEW.put(13, "minecraft:water_breathing");
        OLD_TO_NEW.put(14, "minecraft:invisibility");
        OLD_TO_NEW.put(15, "minecraft:blindness");
        OLD_TO_NEW.put(16, "minecraft:night_vision");
        OLD_TO_NEW.put(17, "minecraft:hunger");
        OLD_TO_NEW.put(18, "minecraft:weakness");
        OLD_TO_NEW.put(19, "minecraft:poison");
        OLD_TO_NEW.put(20, "minecraft:wither");
        OLD_TO_NEW.put(21, "minecraft:health_boost");
        OLD_TO_NEW.put(22, "minecraft:absorption");
        OLD_TO_NEW.put(23, "minecraft:saturation");
        OLD_TO_NEW.put(24, "minecraft:glowing");
        OLD_TO_NEW.put(25, "minecraft:levitation");
        OLD_TO_NEW.put(26, "minecraft:luck");
        OLD_TO_NEW.put(27, "minecraft:unluck");
        OLD_TO_NEW.put(28, "minecraft:slow_falling");
        OLD_TO_NEW.put(29, "minecraft:conduit_power");
        OLD_TO_NEW.put(30, "minecraft:dolphins_grace");
        OLD_TO_NEW.put(31, "minecraft:bad_omen");
        OLD_TO_NEW.put(32, "minecraft:hero_of_the_village");
        OLD_TO_NEW.put(33, "minecraft:darkness");
    }

    public EffectUpdater(String oldKey, String newKey) {
        super(oldKey, newKey);
    }

    @Override
    public void update(NbtCompound nbtRoot) {
        if (nbtRoot.contains(oldKey, NbtElement.LIST_TYPE)) {
            NbtList oldNbt = nbtRoot.getList(oldKey, NbtElement.COMPOUND_TYPE);
            NbtList newNbt = new NbtList();

            for (int i = 0; i < oldNbt.size(); i++) {
                NbtCompound oldEffect = oldNbt.getCompound(i);
                NbtCompound newEffect = new NbtCompound();
                if (oldEffect.contains("Id")) {
                    newEffect.putString("id", OLD_TO_NEW.getOrDefault(oldEffect.getInt("Id"), ""));
                }
                if (oldEffect.contains("EffectId")) {
                    newEffect.putString("id", OLD_TO_NEW.getOrDefault(oldEffect.getInt("EffectId"), ""));
                }
                updateKey(newEffect, "amplifier", oldEffect, "Amplifier");
                updateKey(newEffect, "duration", oldEffect, "Duration");
                updateKey(newEffect, "duration", oldEffect, "EffectDuration");
                updateKey(newEffect, "show_particles", oldEffect, "ShowParticles");
                updateKey(newEffect, "show_icon", oldEffect, "ShowIcon");
                updateKey(newEffect, "ambient", oldEffect, "Ambient");
                newNbt.add(newEffect);
            }

            nbtRoot.remove(oldKey);
            nbtRoot.put(newKey, newNbt);
        }
    }
}
