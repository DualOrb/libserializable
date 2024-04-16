package org.libserializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.entities.livingentities.SLivingEntity;

public class Deserializer {


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
