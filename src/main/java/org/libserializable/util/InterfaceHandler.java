package org.libserializable.util;

import org.bukkit.Bukkit;
import org.libserializable.impl.SEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * A class handling all methods related to our implementation of reflection requiring handling of interfaces
 *
 * TODO - Maybe make this into a service with service locator pattern later on
 */
public class InterfaceHandler {

    /**
     * From a given object, get all superclasses / superinterfaces and create a map of "Method(<prefixInterfaceName>,InterfaceName)
     * @param targetClass The class which should be searched for method implementations
     * @param instance The instance object to generate the tree of superclasses/interfaces. should be a specific type of entity such as <zombie> for example
     * @param prefixes A list of Strings which prefix each method to be found
     * @return A mapping of the method to each interface
     */
    public static <T> Map<Method, Class<?>> generateInterfaceMethodMap(Class<?> targetClass, T instance, List<String> prefixes) {
        Map<Method, Class<?>> interfaceMethodMap = new HashMap<>();

        // Get all interfaces implemented by the object's class
        Class<?>[] interfaces = instance.getClass().getInterfaces();

        for (Class<?> OGinterface : interfaces) {

            // Create a set to store the superinterfaces
            Set<Class<?>> superInterfaces = getAllExtendedOrImplementedTypesRecursively(OGinterface);
            // Map each interface method to a method in the target class
            try {
                Method[] methods = targetClass.getDeclaredMethods();
                for (Class<?> interf : superInterfaces) {
                    for (Method method : methods) {
                        for (String prefix : prefixes) {
                            if (method.getName().startsWith(prefix + interf.getSimpleName())) {
                                interfaceMethodMap.put(method, interf);
                                break;
                            }
                        }
                    }
                }
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        }
        return interfaceMethodMap;
    }

    /**
     * Invokes all the methods as defined in the mapping above.
     * @param interfaceMethodMap
     */
    public static void setAllInterfaceMethods(Map<Method, Class<?>> interfaceMethodMap, SEntity sEntity) {
        // Go through all the other attributes and deserialize
        for (Map.Entry<Method, Class<?>> entry : interfaceMethodMap.entrySet()) {
            try {
                if(entry.getKey().getName().startsWith("set")) {
                    entry.getKey().invoke(sEntity);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

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




}
