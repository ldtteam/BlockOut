package com.ldtteam.blockout.util.profiler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ldtteam.blockout.element.IUIElement;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public final class ProfilerExporter
{

    private ProfilerExporter()
    {
        throw new IllegalStateException("Tried to initialize: ProfilerExporter but this is a Utility class.");
    }


    public static void exportProfiler(@NotNull final IUIElement element)
    {
        final Map<String,Long> sortedMap = element.getParent().getUiManager().getProfiler().profilingMap; //new TreeMap<>(element.getParent().getUiManager().getProfiler().profilingMap);

        try (Writer writer = new FileWriter("profiler.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(sortedMap, writer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
