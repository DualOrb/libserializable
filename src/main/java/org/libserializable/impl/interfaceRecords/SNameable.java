package org.libserializable.impl.interfaceRecords;

import org.bukkit.Nameable;
import org.libserializable.annotations.HandleInterface;
import org.libserializable.util.enums.ActionType;

public record SNameable(String name) {

    @HandleInterface(action = ActionType.GET, handles = Nameable.class)
    public static <T extends Nameable> SNameable getNameable(T entity) {
        return new SNameable(entity.getCustomName());
    }

    @HandleInterface(action = ActionType.SET, handles = Nameable.class)
    public <T extends Nameable> void setNameable(T entity) {

    }
}