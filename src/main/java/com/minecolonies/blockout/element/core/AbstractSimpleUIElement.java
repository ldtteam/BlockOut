package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.util.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSimpleUIElement implements IUIElement
{
    @NotNull
    private final String         id;
    @NotNull
    private       BoundingBox    localBoundingBox;
    @NotNull
    private       IUIElementHost parent;
    private boolean visible = true;
    private boolean enabled = true;

    protected AbstractSimpleUIElement(
      @NotNull final String id,
      @NotNull final BoundingBox localBoundingBox,
      @NotNull final IUIElementHost parent,
      final boolean visible,
      final boolean enabled)
    {
        this.id = id;
        this.localBoundingBox = localBoundingBox;
        this.parent = parent;
        this.visible = visible;
        this.enabled = enabled;
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @NotNull
    @Override
    public BoundingBox getLocalBoundingBox()
    {
        return localBoundingBox;
    }

    @Override
    public void setLocalBoundingBox(@NotNull final BoundingBox box)
    {
        this.localBoundingBox = box;
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isVisible()
    {
        return visible;
    }

    @Override
    public void setVisible(final boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }
}
