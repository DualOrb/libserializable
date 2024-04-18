package org.libserializable.annotations;

import org.libserializable.util.enums.ActionType;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * An annotation for marking a handled interface for serialization
 * - SET
 * TODO Make this part of the API, so users can extend functionality
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleInterface {
    ActionType action();
    Class<?> handles();

    class AnnotationUtils {
        public static List<Method> getMethodsByAction(String packageName, ActionType action, Class<?> interfaceClass) {
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(
                            ClasspathHelper.forPackage(packageName)
                    ).setScanners(new MethodAnnotationsScanner())
            );

            Set<Method> methods = reflections.getMethodsAnnotatedWith(HandleInterface.class);
            List<Method> filteredMethods = new ArrayList<>();

            for (Method method : methods) {
                HandleInterface annotation = method.getAnnotation(HandleInterface.class);
                if (annotation != null && annotation.action() == action && annotation.handles().equals(interfaceClass)) {
                    filteredMethods.add(method);
                }
            }

            return filteredMethods;
        }
    }
}



