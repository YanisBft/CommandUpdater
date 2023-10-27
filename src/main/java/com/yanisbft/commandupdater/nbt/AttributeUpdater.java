package com.yanisbft.commandupdater.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public class AttributeUpdater extends NbtUpdater {
    public static final Map<String, String> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put("generic.armorToughness", "generic.armor_toughness");
        OLD_TO_NEW.put("generic.attackDamage", "generic.attack_damage");
        OLD_TO_NEW.put("generic.attackKnockback", "generic.attack_knockback");
        OLD_TO_NEW.put("generic.attackSpeed", "generic.attack_speed");
        OLD_TO_NEW.put("generic.flyingSpeed", "generic.flying_speed");
        OLD_TO_NEW.put("generic.followRange", "generic.follow_range");
        OLD_TO_NEW.put("generic.knockbackResistance", "generic.knockback_resistance");
        OLD_TO_NEW.put("generic.maxHealth", "generic.max_health");
        OLD_TO_NEW.put("generic.movementSpeed", "generic.movement_speed");
        OLD_TO_NEW.put("horse.jumpStrength", "horse.jump_strength");
        OLD_TO_NEW.put("zombie.spawnReinforcements", "zombie.spawn_reinforcements");
    }

    public AttributeUpdater(String key) {
        super(key, key);
    }

    @Override
    public void update(NbtCompound nbtRoot) {
        if (nbtRoot.contains(oldKey, NbtElement.LIST_TYPE)) {
            NbtList oldNbt = nbtRoot.getList(oldKey, NbtElement.COMPOUND_TYPE);
            NbtList newNbt = new NbtList();

            for (int i = 0; i < oldNbt.size(); i++) {
                NbtCompound oldAttribute = oldNbt.getCompound(i);
                NbtCompound newAttribute = oldAttribute.copy();
                if (oldAttribute.contains("AttributeName")) {
                    newAttribute.putString("AttributeName", OLD_TO_NEW.getOrDefault(oldAttribute.getString("AttributeName"), oldAttribute.getString("AttributeName")));
                }
                if (oldAttribute.contains("Name")) {
                    newAttribute.putString("Name", OLD_TO_NEW.getOrDefault(oldAttribute.getString("Name"), oldAttribute.getString("Name")));
                }
                newNbt.add(newAttribute);
            }

            nbtRoot.remove(oldKey);
            nbtRoot.put(newKey, newNbt);
        }
    }
}
