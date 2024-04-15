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
_Note to self: May want to make it an injected service with singleton pattern_

### How it works
Defines a tree hierarchy of classes that are wrappers to the spigot interfaces. The uses Java Serialization / Deserialization to convert to bytes, or GSON to convert to JSON.



## BETA