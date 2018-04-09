package com.minecolonies.blockout.builder.data.builder.control;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.element.simple.Image;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ImageConstructionDataBuilder extends AbstractSimpleUIElementConstructionDataBuilder<ImageConstructionDataBuilder, Image>
{
    public ImageConstructionDataBuilder(final String controlId, final IBlockOutGuiConstructionDataBuilder builder)
    {
        super(controlId, builder);
    }

    @NotNull
    public final ImageConstructionDataBuilder withBoundIcon(@NotNull final IDependencyObject<ResourceLocation> dependencyObject)
    {
        return withDependency(Constants.Controls.Image.CONST_ICON, dependencyObject);
    }
}
