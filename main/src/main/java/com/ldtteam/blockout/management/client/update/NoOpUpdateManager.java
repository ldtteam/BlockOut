package com.ldtteam.blockout.management.client.update;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.management.common.update.ChildUpdateManager;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

public class NoOpUpdateManager implements IUpdateManager
{

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        if (element instanceof IRootGuiElement)
        {
            IRootGuiElement rootGuiElement = (IRootGuiElement) element;
            ((IRootGuiElement) element).getUiManager().getProfiler().startTick();
            ChildUpdateManager childUpdateManager = new ChildUpdateManager(this);
            childUpdateManager.updateElement(rootGuiElement);
            ((IRootGuiElement) element).getUiManager().getProfiler().endTick();
        }
        else
        {
            Log.getLogger().warn("Somebody tried to update a none root element.");
        }
    }

    @Override
    public void markDirty()
    {

    }
}
