package com.ldtteam.blockout.element.advanced.list.constructiondatabuilder;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.advanced.list.List;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

public final class ListConstructionDataBuilder extends AbstractListConstructionDataBuilder<ListConstructionDataBuilder, List>
{

    public ListConstructionDataBuilder(
      final String controlId,
      final IBlockOutGuiConstructionDataBuilder data)
    {
        super(controlId, data, List.class);
    }
}
