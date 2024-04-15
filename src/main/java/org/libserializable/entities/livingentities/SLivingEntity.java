package org.libserializable.entities.livingentities;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.entities.SEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.google.gson.Gson;
import org.libserializable.entities.livingentities.records.SAgeable;

/**
 * An internal serializable representation of a living entity
 */
public class SLivingEntity extends SEntity {

    private Gson gson;

    public SLivingEntity(LivingEntity livingEntity) {
        super((Entity) livingEntity, SLivingEntity.class);

        gson = new Gson();

        // Invoke corresponding method for each interface
        for (Map.Entry<Method, Class<?>> entry : interfaceMethodMap.entrySet()) {
            try {
                entry.getKey().invoke(this);

                // Build the JSON object / add to existing


            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
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
     * @return
     */
    public SAgeable getAgeable() {
        Ageable ageable = (Ageable)this.entity;
        return new SAgeable(ageable.getAge(), ageable.isAdult());
    }

    public void setAgeable() {

    }

    /**
     *
     */





}


