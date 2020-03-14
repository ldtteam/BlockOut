package com.ldtteam.blockout.element.advanced.list.factory;

import com.ldtteam.blockout.element.advanced.list.List;

import static com.ldtteam.blockout.util.Constants.Controls.List.*;

public final class ListFactory extends AbstractListFactory<List>
{
    public ListFactory()
    {
        super(List.class,
          KEY_LIST,
          (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled, templateResource, source, orientation, scrollBarBackgroundResource, scrollBarForegroundResource, showScrollBar, dataBoundMode, scrollOffset) -> new List(
            id,
            parent,
            styleId,
            alignments,
            dock,
            margin,
            padding,
            elementSize,
            dataContext,
            visible,
            enabled,
            dataBoundMode,
            templateResource,
            scrollBarBackgroundResource,
            scrollBarForegroundResource,
            scrollOffset,
            orientation,
            showScrollBar,
            source
          ),
          (element, builder) -> {
              //Noop default list factory handles everything.
          });
    }
}
