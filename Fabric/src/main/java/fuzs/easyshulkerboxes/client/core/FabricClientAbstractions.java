package fuzs.easyshulkerboxes.client.core;

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public List<ClientTooltipComponent> getTooltipComponents(Screen screen, Font font, int mouseX, int mouseY, ItemStack stack) {
        List<Component> tooltips = screen.getTooltipFromItem(stack);
        List<ClientTooltipComponent> list = tooltips.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .collect(Collectors.toList());
        // integrate hook from Fabric Api to avoid IllegalArgumentException from vanilla factory
        stack.getTooltipImage().ifPresent(tooltipComponent -> {
            ClientTooltipComponent component = TooltipComponentCallback.EVENT.invoker().getComponent(tooltipComponent);
            if (component != null) {
                list.add(1, component);
                return;
            }
            list.add(1, ClientTooltipComponent.create(tooltipComponent));
        });
        return list;
    }

    @Override
    public boolean isKeyActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return keyMapping.matches(keyCode, scanCode);
    }
}
