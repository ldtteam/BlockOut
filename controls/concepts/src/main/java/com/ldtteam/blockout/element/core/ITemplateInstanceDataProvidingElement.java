package com.ldtteam.blockout.element.core;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IChildDrawableUIElement;
import org.jetbrains.annotations.NotNull;

public interface ITemplateInstanceDataProvidingElement extends IUIElementHost, IChildDrawableUIElement {
    IBlockOutGuiConstructionData getTemplateConstructionData();

    void setTemplateConstructionData(@NotNull IBlockOutGuiConstructionData templateConstructionData);
}
