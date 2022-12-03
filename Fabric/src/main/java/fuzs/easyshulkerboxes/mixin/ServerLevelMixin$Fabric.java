package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.event.PlayLevelSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
abstract class ServerLevelMixin$Fabric extends Level {

    protected ServerLevelMixin$Fabric(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, holder, supplier, bl, bl2, l, i);
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$playSeededSound(@Nullable Player player, double x, double y, double z, SoundEvent soundEvent, SoundSource source, float volume, float pitch, long seed, CallbackInfo callback) {
        PlayLevelSoundEvents.POSITION.invoker().onPlaySoundAtPosition(this, new Vec3(x, y, z), soundEvent, source, volume, pitch).ifPresent(unit -> callback.cancel());
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$playSeededSound(@Nullable Player player, Entity entity, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, long seed, CallbackInfo callback) {
        PlayLevelSoundEvents.ENTITY.invoker().onPlaySoundAtEntity(entity, soundEvent, soundSource, volume, pitch).ifPresent(unit -> callback.cancel());
    }
}
