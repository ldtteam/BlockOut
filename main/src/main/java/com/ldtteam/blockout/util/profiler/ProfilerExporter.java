package com.ldtteam.blockout.util.profiler;

import com.ldtteam.blockout.element.IUIElement;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ProfilerExporter
{

    private ProfilerExporter()
    {
        throw new IllegalStateException("Tried to initialize: ProfilerExporter but this is a Utility class.");
    }

    public static void exportProfiler(@NotNull final IUIElement element)
    {
      element.getParent().getUiManager().getProfiler().getResults().writeToFile(new File(new File("./"), "profile.json"));
    }
}
