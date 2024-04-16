package org.libserializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.entities.livingentities.SLivingEntity;

public class Deserializer {

    private String testData = "{EntityType=SEntityType[type=ZOMBIE], Nameable=SNameable[name=null], LivingEntity=null, Attributable=SAttributable[attributeMap={GENERIC_MAX_ABSORPTION=0.0, GENERIC_MAX_HEALTH=20.0, GENERIC_ARMOR_TOUGHNESS=0.0, ZOMBIE_SPAWN_REINFORCEMENTS=0.039493936800821396, GENERIC_ARMOR=2.0, GENERIC_FOLLOW_RANGE=35.0, GENERIC_ATTACK_KNOCKBACK=0.0, GENERIC_KNOCKBACK_RESISTANCE=0.0, GENERIC_ATTACK_DAMAGE=3.0, GENERIC_MOVEMENT_SPEED=0.23000000417232513}], Damageable=SDamageable[health=20.0, absorptionAmount=0.0], Ageable=SAgeable[age=0, adult=true]}";

    public Deserializer() {

    }

    public Entity spawn(String jsonString, Location location) {
        return spawn(JsonParser.parseString(jsonString).getAsJsonObject(), location);
    }

    public Entity spawn(JsonObject jsonObject, Location location) {
        SLivingEntity livingEntity = new SLivingEntity(jsonObject, location);
        return livingEntity.getEntity();
    }

    public Entity spawn(byte[] jsonBytes, Location location) {
        return spawn(new String(jsonBytes), location);
    }
}
