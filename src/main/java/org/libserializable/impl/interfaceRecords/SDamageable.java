package org.libserializable.impl.interfaceRecords;

import org.bukkit.entity.Damageable;
import org.libserializable.annotations.HandleInterface;
import org.libserializable.util.enums.ActionType;

public record SDamageable(double health, double absorptionAmount) {

    @HandleInterface(action = ActionType.GET, handles = Damageable.class)
    public static <T extends Damageable> SDamageable getDamageable(T entity) {
        return new SDamageable(entity.getHealth(), entity.getAbsorptionAmount());
    }

    @HandleInterface(action = ActionType.SET, handles = Damageable.class)
    public <T extends Damageable> void setDamageable(T entity) {

    }
}
