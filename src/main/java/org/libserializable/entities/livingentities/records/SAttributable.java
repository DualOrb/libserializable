package org.libserializable.entities.livingentities.records;

import org.bukkit.attribute.Attribute;

import java.util.Map;

public record SAttributable(Map<Attribute, Double> attributeMap) {}
