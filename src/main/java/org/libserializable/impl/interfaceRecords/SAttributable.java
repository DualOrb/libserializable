package org.libserializable.impl.interfaceRecords;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.libserializable.annotations.HandleInterface;
import org.libserializable.util.enums.ActionType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record SAttributable(Map<Attribute, Double> attributeMap) {

    @HandleInterface(action = ActionType.GET, handles = Attributable.class)
    public static <T extends Attributable> SAttributable getAttributable(T entity) {
        Map<Attribute, Double> attributes = new HashMap<>();
        // Just load up a map with all the attributes
        for(Attribute attribute : Attribute.values()) {
            AttributeInstance a = entity.getAttribute(attribute);
            if(a == null) continue;

            attributes.put(attribute, a.getBaseValue());
        }

        return new SAttributable(attributes);
    }

    @HandleInterface(action = ActionType.SET, handles = Attributable.class)
    public <T extends Attributable> void setAttributable(T entity) {
        for(Map.Entry<Attribute, Double> entry : attributeMap.entrySet()) {
            Objects.requireNonNull(entity.getAttribute(entry.getKey())).setBaseValue(entry.getValue());
        }
    }

}
