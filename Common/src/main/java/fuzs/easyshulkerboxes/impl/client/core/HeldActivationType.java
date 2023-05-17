package fuzs.easyshulkerboxes.impl.client.core;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public abstract class HeldActivationType {
    public static final String TOOLTIP_HOLD_TRANSLATION_KEY = "item.container.tooltip.hold";
    private static final Map<String, HeldActivationType> ACTIVATION_TYPES_BY_ID = Maps.newHashMap();

    private final String id;
    private final String displayName;

    HeldActivationType(String name) {
        this(name, name);
    }

    private HeldActivationType(String id, String displayName) {
        this.id = id.toUpperCase(Locale.ROOT);
        this.displayName = displayName;
        ACTIVATION_TYPES_BY_ID.put(this.id, this);
    }

    public String getIdentifier() {
        return this.id;
    }

    public Component getComponent(String translationId) {
        return Component.translatable(translationId, Component.translatable(TOOLTIP_HOLD_TRANSLATION_KEY), Component.literal(this.displayName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
    }

    public abstract boolean isActive();

    public static HeldActivationType getActivationTypeById(String id, @Nullable HeldActivationType keyFallback) {
        Objects.requireNonNull(id, "id is null");
        if (id.equals("KEY")) {
            Objects.requireNonNull(keyFallback, "key fallback is null");
            id = keyFallback.id;
        }
        HeldActivationType activationType = ACTIVATION_TYPES_BY_ID.get(id);
        Objects.requireNonNull(activationType, "activation type is null");
        return activationType;
    }

    public static Stream<KeyMappingProvider> getKeyMappingProviders() {
        return ACTIVATION_TYPES_BY_ID.values().stream().filter(KeyMappingProvider.class::isInstance).map(KeyMappingProvider.class::cast);
    }

    public static HeldActivationType of(String name, BooleanSupplier activationSupplier) {
        return of(name, name, activationSupplier);
    }

    public static HeldActivationType of(String id, String name, BooleanSupplier activationSupplier) {
        return new HeldActivationType(id, name) {

            @Override
            public boolean isActive() {
                return activationSupplier.getAsBoolean();
            }
        };
    }
}
