package org.libserializable.impl.interfaceRecords;

import org.bukkit.entity.Ageable;
import org.libserializable.annotations.HandleInterface;
import org.libserializable.util.enums.ActionType;

public record SAgeable(int age, boolean adult) {

    @HandleInterface(action = ActionType.GET, handles = Ageable.class)
    public static <T extends Ageable> SAgeable getAgeable(T entity) {
        return new SAgeable(entity.getAge(), entity.isAdult());
    }

    @HandleInterface(action = ActionType.SET, handles = Ageable.class)
    public <T extends Ageable> void setAgeable(T entity) {
        if(adult) entity.setAdult();

        entity.setAge(age);
    }
}
