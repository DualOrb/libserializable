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

}
