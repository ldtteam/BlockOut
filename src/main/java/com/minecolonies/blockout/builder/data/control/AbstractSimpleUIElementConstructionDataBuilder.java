package com.minecolonies.blockout.builder.data.control;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.builder.core.IBlockOutUIElementConstructionDataBuilder;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;

public class AbstractSimpleUIElementConstructionDataBuilder<C extends AbstractSimpleUIElementConstructionDataBuilder<C, T>, T extends AbstractSimpleUIElement>
  implements IBlockOutUIElementConstructionDataBuilder<C, T>
{

    private final String                              controlId;
    private final IBlockOutGuiConstructionDataBuilder builder;

    public AbstractSimpleUIElementConstructionDataBuilder(final String controlId, final IBlockOutGuiConstructionDataBuilder builder)
    {
        this.controlId = controlId;
        this.builder = builder;
    }

    @NotNull
    public final C withBoundAlignments(@NotNull final IDependencyObject<Alignment> dependencyObject)
    {
        return withDependency(CONST_ALIGNMENT, dependencyObject);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public C withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency)
    {
        builder.withDependency(controlId, fieldName, dependency);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <S, A> C withEventHandler(
      @NotNull final String eventName, @NotNull final Class<S> controlTypeClass, @NotNull final Class<A> argumentTypeClass, @NotNull final IEventHandler<S, A> eventHandler)
    {
        builder.withEventHandler(controlId, eventName, controlTypeClass, argumentTypeClass, eventHandler);
        return (C) this;
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionDataBuilder done()
    {
        return builder;
    }

    @NotNull
    public final C withBoundDock(@NotNull final IDependencyObject<Dock> dependencyObject)
    {
        return withDependency(CONST_DOCK, dependencyObject);
    }

    @NotNull
    public final C withBoundMargin(@NotNull final IDependencyObject<AxisDistance> dependencyObject)
    {
        return withDependency(CONST_MARGIN, dependencyObject);
    }

    @NotNull
    public final C withBoundElementSize(@NotNull final IDependencyObject<Vector2d> dependencyObject)
    {
        return withDependency(CONST_ELEMENT_SIZE, dependencyObject);
    }

    @NotNull
    public final C withBoundDataContext(@NotNull final IDependencyObject<Object> dependencyObject)
    {
        return withDependency(CONST_DATA_CONTEXT, dependencyObject);
    }

    @NotNull
    public final C withBoundVisibility(@NotNull final IDependencyObject<Boolean> dependencyObject)
    {
        return withDependency(CONST_VISIBLE, dependencyObject);
    }

    @NotNull
    public final C withBoundEnablement(@NotNull final IDependencyObject<Boolean> dependencyObject)
    {
        return withDependency(CONST_ENABLED, dependencyObject);
    }
}
