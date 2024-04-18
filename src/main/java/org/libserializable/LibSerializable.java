package org.libserializable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.libserializable.api.Deserializer;
import org.libserializable.api.Serializer;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.libserializable.annotations.HandleInterface;

public final class LibSerializable extends JavaPlugin {

    @Override
    public void onEnable() {
        // Test for development server
        World world = Bukkit.getWorlds().get(0);
        Zombie zombie = (Zombie) world.spawnEntity(world.getSpawnLocation(), EntityType.ZOMBIE);
        Serializer serializer = new Serializer();
        String stringified = serializer.serialize(zombie);   // Upcasts to living entity

        System.out.println(stringified);

        Deserializer deserializer = new Deserializer();
        deserializer.spawn(stringified, world.getSpawnLocation());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static List<Class<?>> loadClassesWithAnnotation(String basePackage, Class<?> annotationClass) throws Exception {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        File baseDir = new File(basePackage.replace(".", "/"));
        URLClassLoader classLoader = new URLClassLoader(new URL[]{baseDir.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

        File[] files = baseDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf('.'));
                    Class<?> clazz = classLoader.loadClass(basePackage + "." + className);
                    if (clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClass)) {
                        annotatedClasses.add(clazz);
                    }
                }
            }
        }

        return annotatedClasses;
    }
}
