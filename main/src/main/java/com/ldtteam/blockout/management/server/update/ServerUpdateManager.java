package com.ldtteam.blockout.management.server.update;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.management.common.update.ChildUpdateManager;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.profiler.ProfilerExporter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ServerUpdateManager implements IUpdateManager
{

    @NotNull
    private final IUIManager manager;
    private       boolean    dirty = false;

    public ServerUpdateManager(@NotNull final IUIManager manager) {this.manager = manager;}

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        if (element instanceof RootGuiElement)
        {
            RootGuiElement rootGuiElement = (RootGuiElement) element;
            rootGuiElement.getUiManager().getProfiler().clearProfiling();
            rootGuiElement.getUiManager().getProfiler().startSection("Global Update");

            ChildUpdateManager childUpdateManager = new ChildUpdateManager(this);
            childUpdateManager.updateElement(rootGuiElement);
            rootGuiElement.getUiManager().getProfiler().endSection();

            File tmpDir = new File("profiler.json");
            if (!tmpDir.exists())
            {
                ProfilerExporter.exportProfiler(element);
            }
        }
        else
        {
            Log.getLogger().warn("Somebody tried to update a none root element.");
        }
    }

    @Override
    public void markDirty()
    {
        this.dirty = true;
    }

    /**
     * Called by the forge event handler to indicate that a tick has passed on this UI and that a update packet should be send.
     */
    public void onNetworkTick()
    {
        if (dirty)
        {
            manager.getNetworkManager().onElementChanged(manager.getHost());
            dirty = false;
        }
    }

    /**
     * Indicates if the server side is dirty and needs to be updated.
     *
     * @return true when dirty false when not.
     */
    public boolean isDirty()
    {
        return dirty;
    }
}
