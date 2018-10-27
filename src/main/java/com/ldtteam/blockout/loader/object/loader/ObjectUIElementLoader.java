package com.ldtteam.blockout.loader.object.loader;

import com.ldtteam.blockout.loader.ILoader;
import com.ldtteam.blockout.loader.object.ObjectUIElementBuilder;
import org.jetbrains.annotations.NotNull;

public class ObjectUIElementLoader implements ILoader
{
    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public IUIElementData createFromDataAndBindingEngine(@NotNull final String data)
    {
        if (!data.endsWith(".class"))
        {
            throw new IllegalArgumentException("Not a class name");
        }

        try
        {
            final String className = data.replace(".class", "");
            final Class<?> potentialFileLoader = Class.forName(className);
            final Class<? extends IClassBasedUICreator> fileLoader = (Class<? extends IClassBasedUICreator>) potentialFileLoader;
            final IClassBasedUICreator instance = fileLoader.newInstance();

            final ObjectUIElementBuilder builder = new ObjectUIElementBuilder();

            instance.build(builder);

            return builder.build();
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Construction using a code based class has failed!");
        }
    }
}
