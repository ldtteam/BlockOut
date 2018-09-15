package com.ldtteam.blockout.loader;

import com.ldtteam.blockout.core.element.values.Alignment;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.core.element.values.ControlDirection;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * Interface that describes how to create a {@link IUIElementData} in code.
 */
public interface IUIElementDataBuilder
{
    /**
     * Method to set the type.
     *
     * @param type The type stored in the constructed {@link IUIElementData}
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder setType(ResourceLocation type);

    default IUIElementDataBuilder addChild(@NotNull final IUIElementDataBuilder builder)
    {
        return addChild(builder.build());
    }

    /**
     * Method to add a child {@link IUIElementData} to the constructed {@link IUIElementData}.
     *
     * @param elementData The {@link IUIElementData} of the child to add.
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder addChild(@NotNull IUIElementData elementData);

    /**
     * Method to add a attribute to the constructed {@link IUIElementData}.
     *
     * @param key       The key of the attribute.
     * @param attribute The value of the attribute. Has to be serializable.
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder addAttribute(@NotNull final String key, @NotNull final Serializable attribute);

    default IUIElementDataBuilder addString(@NotNull final String key, @NotNull final String value)
    {
        return addAttribute(key, value);
    }

    default IUIElementDataBuilder addDouble(@NotNull final String key, @NotNull final Double value)
    {
        return addAttribute(key, value);
    }

    default IUIElementDataBuilder addFloat(@NotNull final String key, @NotNull final Float value)
    {
        return addAttribute(key, value);
    }

    default IUIElementDataBuilder addBoolean(@NotNull final String key, @NotNull final Boolean value)
    {
        return addAttribute(key, value);
    }

    default IUIElementDataBuilder addInteger(@NotNull final String key, @NotNull final Integer value)
    {
        return addAttribute(key, value);
    }

    default <T extends Enum<T>> IUIElementDataBuilder addEnum(@NotNull final String key, @NotNull final T value)
    {
        return addString(key, value.name());
    }

    default <T extends Enum<T>> IUIElementDataBuilder addEnumSet(@NotNull final String key, @NotNull final EnumSet<T> value)
    {
        return addString(key, value.stream().map(Enum::name).reduce((s1, s2) -> s1 + "," + s2).orElse(""));
    }

    default IUIElementDataBuilder addAxisDistance(@NotNull final String key, @NotNull final AxisDistance value)
    {
        return addString(key, value.toString());
    }

    default IUIElementDataBuilder addAlignment(@NotNull final String key, @NotNull final EnumSet<Alignment> value)
    {
        return addInteger(key, Alignment.toInt(value));
    }

    default IUIElementDataBuilder addControlDirection(@NotNull final String key, @NotNull final ControlDirection value)
    {
        return addString(key, value.toString());
    }

    default IUIElementDataBuilder addVector2d(@NotNull final String key, @NotNull final Vector2d value)
    {
        return addString(key, value.toString());
    }

    default IUIElementDataBuilder addResourceLocation(@NotNull final String key, @NotNull final ResourceLocation value)
    {
        return addString(key, value.toString());
    }

    default IUIElementDataBuilder addBoundingBox(@NotNull final String key, @NotNull final BoundingBox value) { return addString(key, value.toString()); }

    /**
     * Constructs the {@link IUIElementData} from the data contained in this builder.
     *
     * @return The constructed {@link IUIElementData}.
     */
    IUIElementData build();
}
