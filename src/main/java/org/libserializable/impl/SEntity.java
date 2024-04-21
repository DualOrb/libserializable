package org.libserializable.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.libserializable.impl.interfaceRecords.SEntityType;
import org.libserializable.util.enums.ActionType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.libserializable.util.InterfaceHandler.generateInterfaceMethodMap;

public abstract class SEntity <T extends Entity> {

    protected Map<Method, Class<?>> interfaceMethodMap;
    protected JsonObject jsonRepresentation;
    protected T entity;

    /**
     * Takes in an entity, gets all super interfaces, and creates a mapping of the methods in
     * target class named "handle<InterfaceSimpleName>"
     * <p>
     * In this way, each target class can handle interface methods how they want for serialization
     * @param entity The specific entity (zombie, cobblestone, etc) to be serialized
     */
    public SEntity(T entity) {
        // TODO Handle this more gracefully

        this.entity = entity;
        this.interfaceMethodMap = setInterfaceMethodMap();
        this.jsonRepresentation = toJson();
    }

    /**
     * Returns the entity object that is either being serialized or has been spawned and deserialized
     * @return the Entity object that was deserialized and spawned in the world
     */
    public Entity getEntity() {
        return this.entity;
    }

    /*
     * Serialization Utils
     */


    /**
     * Returns a representation of the living entity in bytes (JSON string to bytes)
     * @return byte[]
     */
    public byte[] toBytes() {
        return this.toString().getBytes();
    }

    public JsonObject toJson() {
        Gson gson = new Gson();

        JsonObject serializedEntity = new JsonObject();
        serializedEntity.add("EntityType", gson.toJsonTree(new SEntityType(entity.getType())));
        JsonObject attributes = new JsonObject();

        for (Map.Entry<Method, Class<?>> entry : interfaceMethodMap.entrySet()) {
            try {
                if (entry.getKey().getName().startsWith("get")) {

                    Record record = (Record) entry.getKey().invoke(null, this.entity);
                    JsonObject recordJson = gson.toJsonTree(record).getAsJsonObject();
                    attributes.add(entry.getValue().getSimpleName(), recordJson);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        serializedEntity.add("Attributes", attributes);

        return serializedEntity;
    }

    @Override
    public String toString() {
        return this.jsonRepresentation.toString();
    }

    protected Map<Method, Class<?>> setInterfaceMethodMap() {
        return generateInterfaceMethodMap(this.getEntity(), ActionType.SET, ActionType.GET);
    }
}
