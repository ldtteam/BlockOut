package com.ldtteam.blockout.element;

import com.ldtteam.blockout.tooltip.ITooltipHost;

public interface IUIElementWithTooltip extends IUIElement {

    ITooltipHost getTooltipHost();
}
