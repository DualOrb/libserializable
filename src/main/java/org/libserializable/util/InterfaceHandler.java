package org.libserializable.util;

import org.libserializable.util.enums.ActionType;

import java.lang.reflect.Method;
import java.util.*;

import static org.libserializable.annotations.HandleInterface.AnnotationUtils.getMethodsByAction;

/**
 * A class handling all methods related to our implementation of reflection requiring handling of interfaces
 * TODO - Maybe make this into a service with service locator pattern later on
 */
public class InterfaceHandler {

    private final static String RECORDSPACKAGE = "org.libserializable.impl.interfaceRecords";

    /**
     *
     * From a given object, get all superclasses / superinterfaces and create a map of "Method(<prefixInterfaceName>,InterfaceName)
     * @param instance The instance object to generate the tree of superclasses/interfaces. should be a specific type of entity such as <zombie> for example
     * @return A mapping of the method to each interface
     */
    public static <T> Map<Method, Class<?>> generateInterfaceMethodMap(T instance, ActionType... actionTypes) {
        Map<Method, Class<?>> interfaceMethodMap = new HashMap<>();

        Set<Class<?>> superclasses = getAllExtendedOrImplementedTypesRecursively(instance.getClass());

        // Retrieve annotated methods from the interfaceRecords package for each specified ActionType
        for(Class<?> clazz: superclasses) {
            for(ActionType actionType: actionTypes) {
                List<Method> methods = getMethodsByAction(RECORDSPACKAGE, actionType, clazz);
                methods.forEach((method) -> interfaceMethodMap.put(method, clazz));
            }
        }

        return interfaceMethodMap;
    }


    /**
     * Helper method to get all super classes or super interfaces of a given class/interface
     * @param ogClass Class to search
     */
    public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> ogClass) {
        List<Class<?>> res = new ArrayList<>();

        // Recursively search all superclasses / superinterfaces under "Object"
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
