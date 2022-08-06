package fuzs.easyshulkerboxes.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityJoinLevelCallback {
    Event<EntityJoinLevelCallback> EVENT = EventFactory.createArrayBacked(EntityJoinLevelCallback.class, listeners -> (Entity entity, Level level, boolean loadedFromDisk) -> {
        for (EntityJoinLevelCallback event : listeners) {
            if (!event.onEntityJoinLevel(entity, level, loadedFromDisk)) {
                return false;
            }
        }
        return true;
    });

    default boolean onEntityJoinLevel(Entity entity, Level level) {
        return this.onEntityJoinLevel(entity, level, false);
    }

    /**
     * called when an entity is loaded into a level on both client and server
     * Fabric's
     *
     * @param entity            the entity being loaded
     * @param level             the level the entity is loaded in
     * @param loadedFromDisk    is the entity being loaded from disk, otherwise it has just been spawned
     * @return                  is loading this entity allowed to continue
     */
    boolean onEntityJoinLevel(Entity entity, Level level, boolean loadedFromDisk);
}
