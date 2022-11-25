package fuzs.easyshulkerboxes.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public final class PlayLevelSoundEvents {
    public static final Event<AtPosition> POSITION = EventFactory.createArrayBacked(AtPosition.class, listeners -> (Level level, Vec3 position, SoundEvent sound, SoundSource source, float volume, float pitch) -> {
        for (AtPosition event : listeners) {
            if (event.onPlaySoundAtPosition(level, position, sound, source, volume, pitch).isPresent())
                return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    });
    public static final Event<AtEntity> ENTITY = EventFactory.createArrayBacked(AtEntity.class, listeners -> (Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) -> {
        for (AtEntity event : listeners) {
            if (event.onPlaySoundAtEntity(entity, sound, source, volume, pitch).isPresent())
                return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    });

    private PlayLevelSoundEvents() {

    }

    @FunctionalInterface
    public interface AtPosition {

        /**
         * called when a sound event is played at a specific position in the world, allows for cancelling the sound
         *
         * @param level         the current level
         * @param position      the position the sound is to be played at
         * @param sound         the sound event, can be exchanged here
         * @param source        sound category
         * @param volume        volume
         * @param pitch         pitch
         * @return              if present the sound will be cancelled
         */
        Optional<Unit> onPlaySoundAtPosition(Level level, Vec3 position, SoundEvent sound, SoundSource source, float volume, float pitch);
    }

    @FunctionalInterface
    public interface AtEntity {

        /**
         * called when a sound event is played at a specific entity, allows for cancelling the sound
         *
         * @param entity        the entity the sound is playing from
         * @param sound         the sound event, can be exchanged here
         * @param source        sound category
         * @param volume        volume
         * @param pitch         pitch
         * @return              if present the sound will be cancelled
         */
        Optional<Unit> onPlaySoundAtEntity(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch);
    }
}
