package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashMap;

public abstract class AbstractChildrenContainingUIElement extends HashMap<String, IUIElement> implements IUIElementHost
{
    @NotNull
    private final String         id;
    @NotNull
    private final IUIManager     uiManager;
    @NotNull
    private       IUIElementHost parent;
    @NotNull
    private EnumSet<Alignment> alignments  = EnumSet.of(Alignment.NONE);
    @NotNull
    private Dock               dock        = Dock.NONE;
    @NotNull
    private AxisDistance       margin      = new AxisDistance();
    @NotNull
    private Vector2d           elementSize = new Vector2d();
    @NotNull
    private AxisDistance       padding     = new AxisDistance();

    @NotNull
    private BoundingBox localBoundingBox;
    @NotNull
    private BoundingBox absoluteBoundingBox;
    @NotNull
    private BoundingBox localInternalBoundingBox;
    @NotNull
    private BoundingBox absoluteInternalBoundingBox;

    private boolean visible = true;
    private boolean enabled = true;

    public AbstractChildrenContainingUIElement(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IUIManager uiManager,
      @NotNull final EnumSet<Alignment> alignments,
      @NotNull final Dock dock,
      @NotNull final AxisDistance margin,
      @NotNull final Vector2d elementSize,
      @NotNull final AxisDistance padding,
      final boolean visible,
      final boolean enabled
    )
    {
        this.id = id;
        this.parent = parent;
        this.uiManager = uiManager;
        this.alignments = alignments;
        this.dock = dock;
        this.margin = margin;
        this.elementSize = elementSize;
        this.padding = padding;
        this.visible = visible;
        this.enabled = enabled;
    }

    public AbstractChildrenContainingUIElement(@NotNull final String id, @NotNull final IUIManager uiManager)
    {
        this.id = id;
        this.uiManager = uiManager;
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public EnumSet<Alignment> getAlignment()
    {
        return EnumSet.copyOf(alignments);
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments = alignment;
        updateBoundingBoxes();
    }

    @Override
    public Dock getDock()
    {
        return dock;
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock = dock;
        updateBoundingBoxes();
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin;
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin = margin;
        updateBoundingBoxes();
    }

    @Override
    public Vector2d getElementSize()
    {
        return elementSize;
    }

    @Override
    public void setElementSize(@NotNull final Vector2d elementSize)
    {
        this.elementSize = elementSize;
    }

    @Override
    public void updateBoundingBoxes()
    {
        updateLocalBoundingBox();
        updateAbsoluteBoundingBox();
        updateLocalInternalBoundingBox();
        updateAbsoluteInternalBoundingBox();

        //Since our bounding mox might have moved / changed. We trigger a recalc for our children.
        values().forEach(IUIElement::updateBoundingBoxes);
    }

    @NotNull
    @Override
    public BoundingBox getAbsoluteBoundingBox()
    {
        return absoluteBoundingBox;
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
        updateBoundingBoxes();
    }

    private void updateLocalBoundingBox()
    {
        //If we have no parent we see our default size as parent.
        //Else grab the size from the parent.
        final Vector2d parentSize = getParent() != this ? getParent().getLocalBoundingBox().getSize() : getElementSize();

        final double marginLeft = Alignment.LEFT.isActive(this) ? getMargin().getLeft().orElse(0d) : 0d;
        final double marginTop = Alignment.TOP.isActive(this) ? getMargin().getTop().orElse(0d) : 0d;
        final double marginRight = Alignment.RIGHT.isActive(this) ? getMargin().getRight().orElse(0d) : 0d;
        final double marginBottom = Alignment.BOTTOM.isActive(this) ? getMargin().getBottom().orElse(0d) : 0d;

        final Vector2d origin = new Vector2d(marginLeft, marginTop);

        final Vector2d size = new Vector2d(parentSize.getX() - (marginLeft + marginRight), parentSize.getY() - (marginTop + marginBottom)).nullifyNegatives();

        this.localBoundingBox = new BoundingBox(origin, size);
    }

    private void updateAbsoluteBoundingBox()
    {
        if (getParent() == this)
        {
            this.absoluteBoundingBox = getLocalBoundingBox();
        }

        final BoundingBox parentAbsoluteBindingBox = getParent().getAbsoluteBoundingBox();
        this.absoluteBoundingBox = new BoundingBox(parentAbsoluteBindingBox.getLocalOrigin().move(getLocalBoundingBox().getLocalOrigin()), getLocalBoundingBox().getSize());
    }

    private void updateLocalInternalBoundingBox()
    {
        final Vector2d origin = localBoundingBox
                                  .getLocalOrigin()
                                  .move(getPadding().getLeft().orElse(0d),
                                    getPadding().getTop().orElse(0d));
        final Vector2d size = localBoundingBox
                                .getSize()
                                .move(-1 * (getPadding().getLeft().orElse(0d) + getPadding().getRight().orElse(0d)),
                                  -1 * (getPadding().getTop().orElse(0d) + getPadding().getBottom().orElse(0d))).nullifyNegatives();

        this.localInternalBoundingBox = new BoundingBox(origin, size);
    }

    private void updateAbsoluteInternalBoundingBox()
    {
        final Vector2d origin = absoluteBoundingBox
                                  .getLocalOrigin()
                                  .move(getPadding().getLeft().orElse(0d),
                                    getPadding().getTop().orElse(0d));
        final Vector2d size = absoluteBoundingBox
                                .getSize()
                                .move(-1 * (getPadding().getLeft().orElse(0d) + getPadding().getRight().orElse(0d)),
                                  -1 * (getPadding().getTop().orElse(0d) + getPadding().getBottom().orElse(0d))).nullifyNegatives();

        this.absoluteInternalBoundingBox = new BoundingBox(origin, size);
    }

    @NotNull
    @Override
    public BoundingBox getLocalBoundingBox()
    {
        return localBoundingBox;
    }

    @NotNull
    @Override
    public IUIManager getUiManager()
    {
        return uiManager;
    }

    @Override
    public AxisDistance getPadding()
    {
        return padding;
    }

    @Override
    public void setPadding(@NotNull final AxisDistance padding)
    {
        this.padding = padding;
        updateBoundingBoxes();
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @Override
    public BoundingBox getLocalInternalBoundingBox()
    {
        return localInternalBoundingBox;
    }

    @Override
    public BoundingBox getAbsoluteInternalBoundingBox()
    {
        return absoluteInternalBoundingBox;
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
