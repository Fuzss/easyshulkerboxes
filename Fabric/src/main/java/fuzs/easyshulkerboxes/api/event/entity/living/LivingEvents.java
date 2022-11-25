package fuzs.easyshulkerboxes.api.event.entity.living;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public final class LivingEvents {
    public static final Event<LivingTick> TICK = EventFactory.createArrayBacked(LivingTick.class, listeners -> (LivingEntity entity) -> {
        for (LivingTick event : listeners) {
            if (!event.onLivingTick(entity)) {
                return false;
            }
        }
        return true;
    });

    private LivingEvents() {

    }

    @FunctionalInterface
    public interface LivingTick {

        /**
         * called at the beginning of the living entity tick method, can be cancelled
         *
         * @param entity    the ticking entity
         * @return          return false to prevent the entity from ticking
         */
        boolean onLivingTick(LivingEntity entity);
    }
}
