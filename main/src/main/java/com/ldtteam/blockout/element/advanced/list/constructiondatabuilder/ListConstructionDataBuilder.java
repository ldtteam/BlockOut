package com.ldtteam.blockout.element.advanced.list.constructiondatabuilder;

import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.advanced.list.List;

public final class ListConstructionDataBuilder extends AbstractListConstructionDataBuilder<ListConstructionDataBuilder, List>
{

    public ListConstructionDataBuilder(
      final String controlId,
      final IBlockOutGuiConstructionDataBuilder data)
    {
        super(controlId, data, List.class);
    }
}
