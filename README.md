# LibSerializable
This project aims to provide a solution to serializing and deserializing entity and all their related data for spigot plugins, as an efficient solution does not currently exist.

### How to use
```
Serializer serializer = new Serializer()
entityBytes = serializer.toBytes(entity)

// Save to database / file ...

Deserializer deserializer = new Deserializer()
deserializer.spawnEntity(location, entityBytes)
```

### How it works
Uses reflection to map each super interface (if one exists) to a defined method in the target class, from which for each entity passed to the serializer will process with only the interfaces it implements.

This is a more efficient solution than wrapping each class, because we only have to do one per interface, and the interfaces are less likely to change structure than the entity itself.



## Work in Progress
