package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.event.entity.EntityJoinLevelCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public abstract class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void addEntity$inject$head(T entityAccess, boolean loadedFromDisk, CallbackInfoReturnable<Boolean> callback) {
        if (entityAccess instanceof Entity entity && !(entityAccess instanceof ServerPlayer) && !EntityJoinLevelCallback.EVENT.invoker().onEntityJoinLevel(entity, entity.level, loadedFromDisk)) callback.setReturnValue(false);
    }
}
