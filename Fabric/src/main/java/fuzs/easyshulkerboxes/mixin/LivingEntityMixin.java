package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.event.entity.living.LivingEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick$inject$head(CallbackInfo callback) {
        if (!LivingEvents.TICK.invoker().onLivingTick((LivingEntity) (Object) this)) callback.cancel();
    }
}
