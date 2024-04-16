package org.libserializable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;

public final class LibSerializable extends JavaPlugin {

    private String testData = "{\"EntityType\":{\"type\":\"ZOMBIE\"},\"Attributes\":{\"Ageable\":{\"age\":0,\"adult\":true},\"Damageable\":{\"health\":20.0,\"absorptionAmount\":0.0},\"Attributable\":{\"attributeMap\":{\"GENERIC_ARMOR_TOUGHNESS\":0.0,\"GENERIC_MAX_HEALTH\":20.0,\"GENERIC_MOVEMENT_SPEED\":0.23000000417232513,\"GENERIC_ATTACK_DAMAGE\":3.0,\"GENERIC_MAX_ABSORPTION\":0.0,\"GENERIC_KNOCKBACK_RESISTANCE\":0.0,\"GENERIC_ARMOR\":2.0,\"GENERIC_ATTACK_KNOCKBACK\":0.0,\"GENERIC_FOLLOW_RANGE\":35.0,\"ZOMBIE_SPAWN_REINFORCEMENTS\":0.011355051147791688}}}}";

    @Override
    public void onEnable() {
        // Test for development server
        World world = Bukkit.getWorlds().get(0);
        Zombie zombie = (Zombie) world.spawnEntity(world.getSpawnLocation(), EntityType.ZOMBIE);
        Serializer serializer = new Serializer();
        serializer.serialize(zombie);   // Upcasts to living entity

        Deserializer deserializer = new Deserializer();
        deserializer.spawn(testData, world.getSpawnLocation());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
