package org.libserializable.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.impl.entities.SLivingEntity;

import javax.annotation.Nullable;

public class Serializer {

    private boolean shouldSerializeInternalStorage;

    public Serializer() {

    }

    public @Nullable String serialize(Entity entity) {
        if(entity instanceof LivingEntity) {
            return new SLivingEntity((LivingEntity) entity).toString();
        }
        return null;
    }

    /**
     * Sets whether an entity's internal data storage (chest on horse for ex) should
     * be serialized as well.
     * @param b Whether the serializer should serialize the internal storage of the entity
     */
    public void shouldSerializeInternalStorage(boolean b) {
        shouldSerializeInternalStorage = b;
    }
}
