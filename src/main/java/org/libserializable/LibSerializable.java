package org.libserializable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.libserializable.entities.livingentities.SLivingEntity;

public final class LibSerializable extends JavaPlugin {

    @Override
    public void onEnable() {
        // Test for development server
        World world = Bukkit.getWorlds().get(0);
        Horse horse = (Horse) world.spawnEntity(world.getSpawnLocation(), EntityType.HORSE);
        Serializer serializer = new Serializer();
        serializer.serialize(horse);   // Upcasts to living entity

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
