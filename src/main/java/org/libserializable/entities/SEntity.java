package org.libserializable.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
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
        Class<?> OGinterface = livingEntity.getClass().getInterfaces()[0];

        // Create a map to store the interfaces and their superinterfaces
        Set<Class<?>> superInterfaces = new HashSet<>(getAllExtendedOrImplementedTypesRecursively(OGinterface));
        System.out.println(OGinterface);
        System.out.println(superInterfaces);

        // Map each interface to a method in this class
        interfaceMethodMap = new HashMap<>();
        try {
            Method[] methods = targetClass.getDeclaredMethods();
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
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get all superinterfaces of an interface recursively
    public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
        List<Class<?>> res = new ArrayList<>();

        do {
            res.add(clazz);

            // First, add all the interfaces implemented by this class
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                res.addAll(Arrays.asList(interfaces));

                for (Class<?> interfaze : interfaces) {
                    res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
                }
            }

            // Add the super class
            Class<?> superClass = clazz.getSuperclass();

            // Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
            if (superClass == null) {
                break;
            }

            // Now inspect the superclass
            clazz = superClass;
        } while (!"java.lang.Object".equals(clazz.getCanonicalName()));

        return new HashSet<Class<?>>(res);
    }

    /*
        Default constructor to be used when deserializing into an entity
     */
    public SEntity() {

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

    /**
     * Returns the entity object that is either being serialized or has been spawned and deserialized
     * @return
     */
    public Entity getEntity() {
        return this.entity;
    }
}
