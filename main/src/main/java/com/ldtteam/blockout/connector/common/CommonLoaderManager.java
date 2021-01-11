package com.ldtteam.blockout.connector.common;

import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.loader.ILoader;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonLoaderManager implements ILoaderManager
{

    private final Set<ILoader> loaders = new HashSet<>();

    @Override
    public void registerLoader(@NotNull final ILoader loader)
    {
        loaders.add(loader);
    }

    @Override
    public IUIElementData<?> loadData(@NotNull final String dataToLoad)
    {
        try
        {
            final List<LoadingResult> resultList = loaders.stream()
                                                     .map(l -> {
                                                         try
                                                         {
                                                             return new LoadingResult(l.loadDataFromDefinition(dataToLoad), null, "");
                                                         }
                                                         catch (Exception e)
                                                         {
                                                             return new LoadingResult(null, e, l.getClass().getName());
                                                         }
                                                     })
                                                     .collect(Collectors.toList());

            if (resultList.stream().noneMatch(LoadingResult::wasSuccessful))
            {
                resultList.forEach(LoadingResult::LogError);
                return null;
            }

            return resultList.stream()
                     .filter(LoadingResult::wasSuccessful)
                     .findFirst()
                     .map(LoadingResult::getElementData)
                     .orElseThrow(() -> new IllegalStateException("Failed to find successful result!"));
        }
        catch (Exception e)
        {
            Log.getLogger().warn(e);
        }

        return null;
    }

    private final class LoadingResult
    {
        @Nullable
        private final IUIElementData<?> elementData;

        @Nullable
        private final Exception exception;

        @Nullable
        private final String loaderName;

        public LoadingResult(@Nullable final IUIElementData<?> elementData, @Nullable final Exception exception, @Nullable final String loaderName)
        {
            this.elementData = elementData;
            this.exception = exception;
            this.loaderName = loaderName;
        }

        public boolean wasSuccessful()
        {
            return exception == null && elementData != null;
        }

        @Nullable
        public IUIElementData<?> getElementData()
        {
            return elementData;
        }

        @Nullable
        public Exception getException()
        {
            return exception;
        }

        private void LogError()
        {
            Log.getLogger().warn(String.format("Failed to parse UI data into element using loader: %s", getLoaderName()), exception);
        }

        @Nullable
        public String getLoaderName()
        {
            return loaderName;
        }
    }
}
