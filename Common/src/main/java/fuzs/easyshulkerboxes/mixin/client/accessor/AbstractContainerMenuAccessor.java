package fuzs.easyshulkerboxes.mixin.client.accessor;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor {

    @Accessor("menuType")
    MenuType<?> easyshulkerboxes$getMenuType();
}
