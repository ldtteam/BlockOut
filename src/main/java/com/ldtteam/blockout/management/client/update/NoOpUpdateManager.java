package com.ldtteam.blockout.management.client.update;

import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.management.common.update.ChildUpdateManager;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

public class NoOpUpdateManager implements IUpdateManager
{

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        if (element instanceof RootGuiElement)
        {
            RootGuiElement rootGuiElement = (RootGuiElement) element;
            ChildUpdateManager childUpdateManager = new ChildUpdateManager(this);
            childUpdateManager.updateElement(rootGuiElement);
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
