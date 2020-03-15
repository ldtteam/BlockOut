package com.ldtteam.blockout.tooltip;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.management.DelegatingUIManager;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

public interface ITooltipHost extends IUIElementHost, IRootGuiElement {

    IUIElement getTooltipElement();

    ITooltipDelayHandler getTooltipDelayHandler();

    default boolean shouldDisplayTooltip(final int mouseX, final int mouseY)
    {
        if (!getTooltipDelayHandler().shouldDisplay())
            return false;

        if (this.getTooltipElement().getAbsoluteBoundingBox().includes(new Vector2d(mouseX, mouseY)))
            return true;

        getTooltipDelayHandler().reset();
        return false;
    }

    @NotNull
    @Override
    default IUIManager getUiManager() {
        return new DelegatingUIManager(getTooltipElement().getParent().getUiManager())
        {
            @NotNull
            @Override
            public IUIElementHost getHost() {
                return ITooltipHost.this;
            }
        };
    }
}
