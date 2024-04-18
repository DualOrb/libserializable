package org.libserializable.impl.interfaceRecords;

import org.bukkit.attribute.Attribute;

import java.util.Map;

public record SAttributable(Map<Attribute, Double> attributeMap) {}
