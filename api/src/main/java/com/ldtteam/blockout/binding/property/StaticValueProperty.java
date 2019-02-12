package com.ldtteam.blockout.binding.property;

import java.util.Optional;

/**
 * This property implementation is a static value version, storing the value in a field that is modifiable.
 *
 * @param <T> The type of the property.
 */
public class StaticValueProperty<T> extends Property<T>
{

    private T value;

    public StaticValueProperty(T value)
    {
        super(Optional.empty(), Optional.empty(), false);
        this.value = value;
    }

    @Override
    public void accept(final Object s, final T t)
    {
        this.value = t;
    }

    @Override
    public Optional<T> apply(final Object s)
    {
        return Optional.ofNullable(value);
    }
}
