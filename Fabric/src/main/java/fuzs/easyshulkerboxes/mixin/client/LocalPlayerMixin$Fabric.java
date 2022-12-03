package fuzs.easyshulkerboxes.mixin.client;

import com.mojang.authlib.GameProfile;
import fuzs.easyshulkerboxes.api.event.PlayLevelSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
abstract class LocalPlayerMixin$Fabric extends AbstractClientPlayer {

    public LocalPlayerMixin$Fabric(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(clientLevel, gameProfile, profilePublicKey);
    }

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$playSound(SoundEvent sound, float volume, float pitch, CallbackInfo callback) {
        PlayLevelSoundEvents.ENTITY.invoker().onPlaySoundAtEntity(this, sound, this.getSoundSource(), volume, pitch).ifPresent(unit -> callback.cancel());
    }
}
