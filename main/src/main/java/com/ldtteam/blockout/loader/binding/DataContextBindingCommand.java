package com.ldtteam.blockout.loader.binding;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.loader.binding.core.IBindingCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Matcher;

public class DataContextBindingCommand implements IBindingCommand
{
    @Override
    public String getBindCommandSyntax()
    {
        return "(B|b)inding";
    }

    @Override
    public String getBindDataSyntax()
    {
        return "((?<getterName>[a-zA-Z_]+)#(?<setterName>[a-zA-Z_]+))|(?<singleName>[a-zA-Z_]+)";
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> IDependencyObject<T> bind(
      @NotNull final Matcher bindMatcher, @Nullable final T defaultValue)
    {
        final String singleName = bindMatcher.group("singleName");
        if (singleName != null && !singleName.isEmpty())
        {
            if (singleName.trim().equalsIgnoreCase("this"))
            {
                return DependencyObjectHelper.createFromSetterAndGetter((Object c) -> (T) c, (c, o) -> {}, defaultValue);
            }

            return DependencyObjectHelper.createFromProperty(PropertyCreationHelper.createFromName(Optional.of(singleName)), defaultValue);
        }


        String getterName = bindMatcher.group("getterName");
        String setterName = bindMatcher.group("setterName");

        if (getterName != null && getterName.isEmpty())
        {
            getterName = null;
        }

        if (setterName != null && setterName.isEmpty())
        {
            setterName = null;
        }

        if (getterName == null && setterName == null)
        {
            return DependencyObjectHelper.createFromValue(defaultValue);
        }

        return DependencyObjectHelper.createFromProperty(PropertyCreationHelper.createFromName(Optional.ofNullable(getterName), Optional.ofNullable(setterName)), defaultValue);
    }
}
