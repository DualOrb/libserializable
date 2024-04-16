package org.libserializable.entities.livingentities;

import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.libserializable.entities.SEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import org.libserializable.entities.livingentities.records.*;

import javax.annotation.Nullable;

/**
 * An internal serializable representation of a living entity
 */
public class SLivingEntity extends SEntity {

    private Gson gson;
    public SLivingEntity(LivingEntity livingEntity) {
        super((Entity) livingEntity, SLivingEntity.class);

        gson = new Gson();

        Map<String, Record> serializedEntity = new HashMap<>();

        // Store type in a separate way since it's an enum
        serializedEntity.put("EntityType", new SEntityType(entity.getType()));

        // Invoke corresponding method for each interface
        for (Map.Entry<Method, Class<?>> entry : interfaceMethodMap.entrySet()) {
            try {
                if(entry.getKey().getName().startsWith("get")) {
                    Object record = entry.getKey().invoke(this);

                    // Build the JSON object / add to existing
                    serializedEntity.put(entry.getValue().getSimpleName(), (Record) record);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Serialized Entity = " + serializedEntity.toString());
    }

    /**
     * From json, spawns the entity at the specified location
     * @param obj
     * @param location
     */
    public SLivingEntity(JsonObject obj, Location location) {

        gson = new Gson();

        Type mapType = new com.google.gson.reflect.TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> map = gson.fromJson(obj, mapType);

        EntityType type = (EntityType) map.remove("EntityType");

        World world = location.getWorld();
        assert world != null;

        this.entity = world.spawnEntity(location, type);

        // Go through all the other attributes and deserialize
        for (Map.Entry<Method, Class<?>> entry : interfaceMethodMap.entrySet()) {
            try {
                if(entry.getKey().getName().startsWith("set")) {
                    entry.getKey().invoke(this);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Deserialize the map back into object form
     * @param json String
     * @return Map<Class<?>, Record>
     */
    private Map<Class<?>, Record> deserializeMap(String json) {
        Type mapType = new com.google.gson.reflect.TypeToken<Map<String, Record>>() {}.getType();
        Map<String, Record> serializedMap = gson.fromJson(json, mapType);

        Map<Class<?>, Record> deserializedMap = new HashMap<>();
        for (Map.Entry<String, Record> entry : serializedMap.entrySet()) {
            try {
                Class<?> key = Class.forName(entry.getKey()); // Convert string key back to Class<?>
                Record value = entry.getValue();
                if(value == null) continue;
                deserializedMap.put(key, value);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return deserializedMap;
    }

    /*
        Handled Interfaces Section

        Uses reflection to assign these methods to an interface.
            Each get method should return a serializable value
            Each set method should apply the values passed (Same format as it was given) to a given entity
     */

    /**
     * Ageable interface
     * - int Age
     * - bool isAdult
     * @return SAgeable
     */
    public SAgeable getAgeable() {
        Ageable ageable = (Ageable)this.entity;
        return new SAgeable(ageable.getAge(), ageable.isAdult());
    }

    public void setAgeable() {

    }

    /**
     * Damageable interface
     * - double health
     * - double absorption
     * @return SDamageable
     */
    public SDamageable getDamageable() {
        Damageable damageable = (Damageable) this.entity;
        return new SDamageable(damageable.getHealth(), damageable.getAbsorptionAmount());
    }

    public void setDamageable() {

    }

    /**
     * Attributable interface
     * - hashmap with values for all non-null attributes
     * @return SAttributable
     */
    public SAttributable getAttributable() {
        Attributable attributable = (Attributable) entity;
        Map<Attribute, Double> attributes = new HashMap<>();
        // Just load up a map with all the attributes
        for(Attribute attribute : Attribute.values()) {
            AttributeInstance a = attributable.getAttribute(attribute);
            if(a == null) continue;

            attributes.put(attribute, a.getBaseValue());
        }

        return new SAttributable(attributes);
    }

    public void setAttributable() {

    }

    /**
     * Nameable Interface
     * - String name
     *
     * @return SNameable
     */
    public SNameable setNameable() {
        Nameable nameable = (Nameable) entity;
        return new SNameable(nameable.getCustomName());
    }

    public void getNameable() {

    }

    /**
     * Handles serialization that is not directly handled by an interface (i.e. inventories)
     * @return
     */
    @Nullable
    public SEquipment setLivingEntity() {
        // TODO implement item serialization and deserialization + inventory serialization
//        LivingEntity livingEntity = (LivingEntity) entity;
//        ItemStack[] armourContents = Objects.requireNonNull(livingEntity.getEquipment()).getArmorContents();
//        if(armourContents == null) {
//            return null;
//        } else {
//            return new SEquipment(armourContents);
//        }
        return null;
    }

    public void getLivingEntity() {

    }


}


