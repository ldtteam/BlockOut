package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.element.IUIElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface declares that this object supports dependency injection, and that at it
 * contains at least one public field of type {@link IDependencyObject} that it wants to
 * have dependencies inserted in.
 */
public interface IDependencyReceiver
{
    /**
     * Method to get the data context of this {@link IDependencyReceiver}.
     * This object is used during data binding.
     *
     * @return The current data context.
     */
    @Nullable
    Object getDataContext();

    /**
     * The unique identifier of this dependency receiver.
     *
     * @return The id of the receiver.
     */
    @NotNull
    String getId();
}
