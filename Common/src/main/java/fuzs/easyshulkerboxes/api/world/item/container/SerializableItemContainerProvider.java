package fuzs.easyshulkerboxes.api.world.item.container;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * an extension to {@link ItemContainerProvider} that allows for serialization as JSON, which is required for the data-driven side of Easy Shulker Boxes
 */
public interface SerializableItemContainerProvider extends ItemContainerProvider {
    Map<ResourceLocation, Serializer> REGISTRY_BY_ID = Collections.synchronizedMap(Maps.newHashMap());
    Map<Class<? extends SerializableItemContainerProvider>, Serializer> REGISTRY_BY_TYPE = Collections.synchronizedMap(Maps.newHashMap());

    static void register(Class<? extends SerializableItemContainerProvider> clazz, ResourceLocation id, Function<JsonElement, ItemContainerProvider> deserializer) {
        Serializer serializer = new Serializer(clazz, id, deserializer);
        REGISTRY_BY_ID.put(id, serializer);
        REGISTRY_BY_TYPE.put(clazz, serializer);
    }

    static JsonElement serialize(ItemContainerProvider provider) {
        if (provider instanceof SerializableItemContainerProvider serializable) {
            Serializer serializer = REGISTRY_BY_TYPE.get(serializable.getClass());
            Objects.requireNonNull(serializer, "no serializer registered for class %s".formatted(serializable.getClass()));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", serializer.id().toString());
            serializable.toJson(jsonObject);
            return jsonObject;
        }
        throw new IllegalArgumentException("provider of class %s is not serializable".formatted(provider.getClass()));
    }

    static ItemContainerProvider deserialize(JsonObject jsonObject) {
        ResourceLocation identifier = new ResourceLocation(GsonHelper.getAsString(jsonObject, "type"));
        Serializer serializer = REGISTRY_BY_ID.get(identifier);
        Objects.requireNonNull(serializer, "no serializer registered for identifier %s".formatted(identifier));
        return serializer.deserializer().apply(jsonObject);
    }

    void toJson(JsonObject jsonObject);

    record Serializer(Class<? extends SerializableItemContainerProvider> clazz, ResourceLocation id, Function<JsonElement, ItemContainerProvider> deserializer) {

    }
}
