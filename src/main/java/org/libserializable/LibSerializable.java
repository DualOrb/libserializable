package org.libserializable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
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
        zombie.setCustomName("EGOR HERMANDEZ");
        zombie.setHealth(10.0);
        zombie.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(32.4);
        Serializer serializer = new Serializer();
        String stringified = serializer.serialize(zombie);   // Upcasts to living entity

        System.out.println(stringified);

        Deserializer deserializer = new Deserializer();


        Entity entity = deserializer.spawn(stringified, world.getSpawnLocation());
        System.out.println("Testing Re-Serializing of entity: " + serializer.serialize(zombie));
        // Tests for if entity successfully deserialized
        assert(entity.getCustomName().equals("EGOR HERMANDEZ"));
        if(entity instanceof Zombie) {
            Zombie zomb = (Zombie) entity;
            assert(zomb.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue() == 32.4);
            assert(zomb.getHealth() == 10.0);
            System.out.println("All tests pass");
        } else {
            System.out.println("Error: Entity is not a zombie");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
