package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractFilteringChildrenContainingUIElement extends AbstractChildrenContainingUIElement
{
    public AbstractFilteringChildrenContainingUIElement(final ResourceLocation type, final String id, final IUIElementHost parent) {super(type, id, parent);}

    public AbstractFilteringChildrenContainingUIElement(
      final ResourceLocation type,
      final String id,
      final IUIElementHost parent,
      final IDependencyObject<EnumSet<Alignment>> alignments,
      final IDependencyObject<Dock> dock,
      final IDependencyObject<AxisDistance> margin,
      final IDependencyObject<Vector2d> elementSize,
      final IDependencyObject<AxisDistance> padding,
      final IDependencyObject<Object> dataContext, final IDependencyObject<Boolean> visible, final IDependencyObject<Boolean> enabled)
    {super(type, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);}

    public abstract Predicate<IUIElement> IsValidChildPredicate();

    @Override
    public IUIElement put(final String key, final IUIElement value)
    {
        IsValidChild(value);
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends IUIElement> m)
    {
        for (IUIElement e : m.values())
        {
            IsValidChild(e);
        }

        super.putAll(m);
    }

    @Override
    public IUIElement putIfAbsent(final String key, final IUIElement value)
    {
        if (!containsKey(key))
        {
            IsValidChild(value);
        }

        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean replace(final String key, final IUIElement oldValue, final IUIElement newValue)
    {
        if (containsKey(key) && Objects.equals(get(key), oldValue))
        {
            IsValidChild(newValue);
        }

        return super.replace(key, oldValue, newValue);
    }

    @Override
    public IUIElement replace(final String key, final IUIElement value)
    {
        if (containsKey(key))
        {
            IsValidChild(value);
        }

        return super.replace(key, value);
    }

    @Override
    public IUIElement computeIfAbsent(final String key, final Function<? super String, ? extends IUIElement> mappingFunction)
    {
        if (get(key) == null)
        {
            final IUIElement target = mappingFunction.apply(key);
            put(key, target);
        }

        return get(key);
    }

    @Override
    public IUIElement computeIfPresent(
      final String key, final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        if (get(key) != null)
        {
            final IUIElement current = get(key);
            final IUIElement target = remappingFunction.apply(key, current);
            put(key, target);
        }

        return get(key);
    }

    @Override
    public IUIElement compute(
      final String key, final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        final IUIElement current = get(key);
        final IUIElement target = remappingFunction.apply(key, current);

        put(key, target);

        return get(key);
    }

    @Override
    public IUIElement merge(
      final String key, final IUIElement value, final BiFunction<? super IUIElement, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        final IUIElement oldValue = get(key);
        final IUIElement newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);

        put(key, newValue);

        return get(key);
    }

    @Override
    public void replaceAll(final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> function)
    {
        try
        {
            for (Entry<String, IUIElement> childEntry : entrySet())
            {
                final IUIElement newElement = function.apply(childEntry.getKey(), childEntry.getValue());
                IsValidChild(newElement);

                childEntry.setValue(newElement);
            }
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Failed to replace some elements.", ex);
        }
    }

    private void IsValidChild(IUIElement element)
    {
        if (!IsValidChildPredicate().test(element))
        {
            throw new IllegalArgumentException(String.format("An Element of type: %s cannot contain elements of type: %s", getType(), element.getType()));
        }

        if (element instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) element;
            iuiElementHost.values().forEach(this::IsValidChild);
        }
    }
}
