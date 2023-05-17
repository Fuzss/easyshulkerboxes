package fuzs.easyshulkerboxes.api.container.v1;

import fuzs.easyshulkerboxes.api.container.v1.provider.ItemContainerProvider;
import fuzs.easyshulkerboxes.impl.world.item.container.ContainerItemHelperImpl;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public interface ContainerItemHelper {
    ContainerItemHelper INSTANCE = new ContainerItemHelperImpl();

    @Nullable ItemContainerProvider getItemContainerProvider(ItemStack stack);

    SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, int inventorySize, boolean allowSaving, String nbtKey);

    SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, IntFunction<SimpleContainer> containerFactory, boolean allowSaving, String nbtKey);

    int getItemWeight(ItemStack stack);

    NonNullList<ItemStack> convertContainerToList(SimpleContainer container);

    float[] getBackgroundColor(@Nullable DyeColor backgroundColor);
}
