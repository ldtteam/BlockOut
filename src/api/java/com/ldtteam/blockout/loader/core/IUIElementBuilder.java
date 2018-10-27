package com.ldtteam.blockout.loader.core;

import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Orientation;
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
     * Constructs the {@link IUIElementData} from the data contained in this builder.
     *
     * @return The constructed {@link IUIElementData}.
     */
    IUIElementData build();
}
