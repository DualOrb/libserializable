package org.libserializable.entities;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

public abstract class SEntity {

    protected Map<Method, Class<?>> interfaceMethodMap;
    protected JsonObject jsonRepresentation;
    protected Entity entity;

    /**
     * Takes in an entity, gets all super interfaces, and creates a mapping of the methods in
     * target class named "handle<InterfaceSimpleName>"
     * <p>
     * In this way, each target class can handle interface methods how they want for serialization
     */
    public SEntity(Entity entity, Class targetClass) {
        // TODO Handle this more gracefully

        if(!(entity instanceof LivingEntity livingEntity)) {
            throw new IllegalArgumentException("Non-living entities are not supported");
        }
        this.entity = entity;

        // Get all interfaces implemented by the object's class
        Class<?> OGinterface = livingEntity.getClass().getInterfaces()[0];

        // Create a map to store the interfaces and their superinterfaces
        Set<Class<?>> superInterfaces = new HashSet<>(getAllExtendedOrImplementedTypesRecursively(OGinterface));

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
            Bukkit.getLogger().log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Helper method to get all super classes or super interfaces of a given class/interface
     * @param ogClass Class
     */
    public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> ogClass) {
        List<Class<?>> res = new ArrayList<>();

        do {
            res.add(ogClass);

            // First, add all the interfaces implemented by this class
            Class<?>[] interfaces = ogClass.getInterfaces();
            if (interfaces.length > 0) {
                res.addAll(Arrays.asList(interfaces));

                for (Class<?> interfaze : interfaces) {
                    res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
                }
            }

            // Add the super class
            Class<?> superClass = ogClass.getSuperclass();

            // Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
            if (superClass == null) {
                break;
            }

            // Now inspect the superclass
            ogClass = superClass;
        } while (!"java.lang.Object".equals(ogClass.getCanonicalName()));

        return new HashSet<>(res);
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
