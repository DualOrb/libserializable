package org.libserializable.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.entities.livingentities.SLivingEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class SEntity {

    protected Map<Method, Class<?>> interfaceMethodMap;
    protected JsonObject jsonRepresentation;
    protected Entity entity;

    /**
     * Takes in an entity, gets all super interfaces, and creates a mapping of the methods in
     * target class named "handle<InterfaceSimpleName>"
     *
     * In this way, each target class can handle interface methods how they want for serialization
     * @param entity
     * @param targetClass
     */
    public SEntity(Entity entity, Class targetClass) {
        // TODO Handle this more gracefully

        if(!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Non-living entities are not supported");
        }
        this.entity = entity;
        LivingEntity livingEntity = (LivingEntity) entity;

        // Get all interfaces implemented by the object's class
        Class<?>[] interfaces = livingEntity.getClass().getInterfaces();

        // Create a map to store the interfaces and their superinterfaces
        Map<Class<?>, Set<Class<?>>> interfaceMap = new HashMap<>();

        // Add the interfaces and their superinterfaces to the map
        for (Class<?> interf : interfaces) {
            // Check if the interface is a subinterface of LivingEntity
            if (LivingEntity.class.isAssignableFrom(interf) && Modifier.isInterface(interf.getModifiers())) {
                Set<Class<?>> allSuperInterfaces = getAllSuperInterfaces(interf);
                interfaceMap.put(interf, allSuperInterfaces);
            }
        }

        // Map each interface to a method in this class
        interfaceMethodMap = new HashMap<>();
        try {
            Method[] methods = targetClass.getDeclaredMethods();
            for (Set<Class<?>> superInterfaces : interfaceMap.values()) {
                for (Class<?> interf : superInterfaces) {
                    for (Method method : methods) {
                        if (method.getName().equals("set" + interf.getSimpleName())) {
                            interfaceMethodMap.put(method, interf);
                            break;
                        } else if (method.getName().equals("get" + interf.getSimpleName())) {
                            interfaceMethodMap.put(method, interf);
                            break;
                        }

                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get all superinterfaces of an interface recursively
    private static Set<Class<?>> getAllSuperInterfaces(Class<?> interf) {
        Set<Class<?>> allSuperInterfaces = new HashSet<>();
        Queue<Class<?>> queue = new LinkedList<>();
        queue.add(interf);

        while (!queue.isEmpty()) {
            Class<?> currentInterface = queue.poll();
            Class<?>[] superInterfaces = currentInterface.getInterfaces();
            for (Class<?> superInterface : superInterfaces) {
                if (LivingEntity.class.isAssignableFrom(superInterface) &&
                        Modifier.isInterface(superInterface.getModifiers()) &&
                        !allSuperInterfaces.contains(superInterface)) {
                    allSuperInterfaces.add(superInterface);
                    queue.add(superInterface);
                }
            }
        }

        return allSuperInterfaces;

    }



    /**
     * Returns a representation of the living entity in bytes (JSON string to bytes)
     * @return byte[]
     */
    public byte[] toBytes() {
        return jsonRepresentation.toString().getBytes();
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
}
