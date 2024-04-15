package org.libserializable;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.libserializable.entities.livingentities.SLivingEntity;

public class Serializer {

    private boolean shouldSerializeInternalStorage;

    public Serializer() {

    }

    public void serialize(Entity entity) {
        if(entity instanceof LivingEntity) {
            new SLivingEntity((LivingEntity) entity);
        }
    }

    /**
     * Sets whether an entity's internal data storage (chest on horse for ex) should
     * be serialized as well.
     * @param b
     */
    public void shouldSerializeInternalStorage(boolean b) {
        shouldSerializeInternalStorage = b;
    }
}
