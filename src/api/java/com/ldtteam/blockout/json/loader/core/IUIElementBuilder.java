package com.ldtteam.blockout.json.loader.core;

import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.ControlDirection;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.EnumSet;

public interface IUIElementBuilder
{


    default IUIElementBuilder withMetaData(IUIElementMetaDataBuilder builder)
    {
        return withMetaData(builder.build());
    }

    IUIElementBuilder withMetaData(IUIElementMetaData metaData);

    default IUIElementBuilder addChild(@NotNull final IUIElementBuilder builder)
    {
        return addChild(builder.build());
    }

    /**
     * Method to add a child {@link IUIElementData} to the constructed {@link IUIElementData}.
     *
     * @param elementData The {@link IUIElementData} of the child to add.
     * @return The instance this was called upon.
     */
    IUIElementBuilder addChild(@NotNull IUIElementData elementData);

    /**
     * Method to add a attribute to the constructed {@link IUIElementData}.
     *
     * @param key       The key of the attribute.
     * @param attribute The value of the attribute. Has to be serializable.
     * @return The instance this was called upon.
     */
    IUIElementBuilder addAttribute(@NotNull final String key, @NotNull final Serializable attribute);

    default IUIElementBuilder addString(@NotNull final String key, @NotNull final String value)
    {
        return addAttribute(key, value);
    }

    default IUIElementBuilder addDouble(@NotNull final String key, @NotNull final Double value)
    {
        return addAttribute(key, value);
    }

    default IUIElementBuilder addFloat(@NotNull final String key, @NotNull final Float value)
    {
        return addAttribute(key, value);
    }

    default IUIElementBuilder addBoolean(@NotNull final String key, @NotNull final Boolean value)
    {
        return addAttribute(key, value);
    }

    default IUIElementBuilder addInteger(@NotNull final String key, @NotNull final Integer value)
    {
        return addAttribute(key, value);
    }

    default <T extends Enum<T>> IUIElementBuilder addEnum(@NotNull final String key, @NotNull final T value)
    {
        return addString(key, value.name());
    }

    default <T extends Enum<T>> IUIElementBuilder addEnumSet(@NotNull final String key, @NotNull final EnumSet<T> value)
    {
        return addString(key, value.stream().map(Enum::name).reduce((s1, s2) -> s1 + "," + s2).orElse(""));
    }

    default IUIElementBuilder addAxisDistance(@NotNull final String key, @NotNull final AxisDistance value)
    {
        return addString(key, value.toString());
    }

    default IUIElementBuilder addAlignment(@NotNull final String key, @NotNull final EnumSet<Alignment> value)
    {
        return addInteger(key, Alignment.toInt(value));
    }

    default IUIElementBuilder addControlDirection(@NotNull final String key, @NotNull final ControlDirection value)
    {
        return addString(key, value.toString());
    }

    default IUIElementBuilder addVector2d(@NotNull final String key, @NotNull final Vector2d value)
    {
        return addString(key, value.toString());
    }

    default IUIElementBuilder addResourceLocation(@NotNull final String key, @NotNull final ResourceLocation value)
    {
        return addString(key, value.toString());
    }

    default IUIElementBuilder addBoundingBox(@NotNull final String key, @NotNull final BoundingBox value) { return addString(key, value.toString()); }

    IUIElementBuilder addNBT(@NotNull final String key, @NotNull final NBTTagCompound compound);
    
    default IUIElementBuilder addItemStack(@NotNull final String key, @NotNull final ItemStack stack)
    {
        return addNBT(key, stack.writeToNBT(new NBTTagCompound()));
    }

    default IUIElementBuilder addBlockState(@NotNull final String key, @NotNull final IBlockState state)
    {
        return addNBT(key, NBTUtil.writeBlockState(new NBTTagCompound(), state));
    }

    default IUIElementBuilder addEntity(@NotNull final String key, @NotNull final Entity entity)
    {
        return addNBT(key, entity.writeToNBT(new NBTTagCompound()));
    }

    /**
     * Constructs the {@link IUIElementData} from the data contained in this builder.
     *
     * @return The constructed {@link IUIElementData}.
     */
    IUIElementData build();
}
