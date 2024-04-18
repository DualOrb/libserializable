package org.libserializable.impl;

import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.impl.entities.SLivingEntity;

import java.lang.reflect.Method;
import java.util.*;

import static org.libserializable.util.InterfaceHandler.generateInterfaceMethodMap;

public abstract class SEntity {

    protected Map<Method, Class<?>> interfaceMethodMap;
    protected JsonObject jsonRepresentation;
    protected Entity entity;

    protected abstract JsonObject createJsonRepresentation();


    /**
     * Takes in an entity, gets all super interfaces, and creates a mapping of the methods in
     * target class named "handle<InterfaceSimpleName>"
     * <p>
     * In this way, each target class can handle interface methods how they want for serialization
     * @param entity The specific entity (zombie, cobblestone, etc) to be serialized
     * @param targetClass The target class to be searched for reflection methods
     * @param <T>
     */
    public <T extends Entity> SEntity(T entity, Class<?> targetClass) {
        // TODO Handle this more gracefully

        this.entity = entity;

        this.interfaceMethodMap = generateInterfaceMethodMap(targetClass, entity, Arrays.asList("set", "get"));

    }



    /**
     * Returns a representation of the living entity in bytes (JSON string to bytes)
     * @return byte[]
     */
    public byte[] toBytes() {
        return this.toString().getBytes();
    }

    /**
     * Returns a representation of the living entity as a JSON object
     */
    public JsonObject toJSON() {
        return jsonRepresentation;
    }

    /**
     * Returns the string representation of the JSON object
     */
    public String toString() {
        return jsonRepresentation.toString();
    }

    /**
     * Returns the entity object that is either being serialized or has been spawned and deserialized
     * @return the Entity object that was deserialized and spawned in the world
     */
    public Entity getEntity() {
        return this.entity;
    }
}
