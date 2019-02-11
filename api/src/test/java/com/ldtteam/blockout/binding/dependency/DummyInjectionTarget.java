package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.dependency.IDependencyReceiver;
import org.jetbrains.annotations.NotNull;

/**
 * Dummy injection target for testing.
 * Contains exactly one injectable field: {@link #insertionTarget}
 */
public final class DummyInjectionTarget implements IDependencyReceiver
{

    public static final String                    INITIAL_VALUE     = "initial";
    public static final String                    ID                = "Target";
    public static final String                    TARGET_FIELD_NAME = "insertionTarget";
    @NotNull
    private final       Object                    dataContext       = new Object();
    @NotNull
    public              IDependencyObject<String> insertionTarget   = DependencyObjectHelper.createFromValue(INITIAL_VALUE);

    @NotNull
    @Override
    public Object getDataContext()
    {
        return dataContext;
    }

    @NotNull
    @Override
    public String getId()
    {
        return ID;
    }

    public String getCurrentValue()
    {
        return insertionTarget.get(this);
    }
}
