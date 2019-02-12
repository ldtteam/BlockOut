package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Helper class used to build {@link IDependencyObject} in situations.
 */
public final class DependencyObjectHelper
{

    private DependencyObjectHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Creates a {@link IDependencyObject} from a static value.
     * The value can only be changed by calling {@link IDependencyObject#set(Object, Object)} on the returned {@link IDependencyObject}
     *
     * @param value The value for the static {@link IDependencyObject}
     * @param <T>   The type that is contained in the {@link IDependencyObject}
     * @return A static {@link IDependencyObject} containing the given value.
     */
    public static <T> IDependencyObject<T> createFromValue(@Nullable T value)
    {
        return new StaticDependencyObject<>(value);
    }

    /**
     * Creates a {@link IDependencyObject} that transforms the given {@link IDependencyObject} as input into a different type.
     * <p>
     * This means that when {@link IDependencyObject#get(Object)} is called on the returned instance, first {@link IDependencyObject#get(Object)}
     * is called on the given {@code inputDependency} after which the {@link Function#apply(Object)} method is called on the {@code getTransformer}.
     * <p>
     * Reversely when {@link IDependencyObject#set(Object, Object)} is called first the {@link Function#apply(Object)} method is called on the given
     * {@code setTransformer} before calling {@link IDependencyObject#set(Object, Object)} with the result of that {@link Function#apply(Object)} call.
     *
     * @param inputDependency The input {@link IDependencyObject}.
     * @param getTransformer  The converter function for the getter. Converts elements from type I into T.
     * @param setTransformer  The converter function for the setter. Converts elements from type T into T.
     * @param <T>             The type that is converted into.
     * @param <I>             The type that is converted from.
     * @return A {@link IDependencyObject} that depends on another {@link IDependencyObject}, but that has the capability to convert between input and output type.
     */
    public static <T, I> IDependencyObject<T> transform(
      final IDependencyObject<I> inputDependency,
      final Function<I, T> getTransformer,
      final Function<T, I> setTransformer)
    {
        return new TransformingDependencyObject<>(inputDependency, getTransformer, setTransformer);
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Function, BiConsumer, boolean)}.
     * <p>
     * Useful for when code clutter wants to be prevented and only a getter is required (EG in cases where the {@link IDependencyObject} should only be read by the respective
     * consumer.
     * All calls to {@link IDependencyObject#set(Object, Object)} will be disregarded.
     *
     * @param dependencyGetter The {@link Function} that functions as a getter. This will be called when {@link IDependencyObject#get(Object)} is called.
     * @param defaultValue     The default value in case the given {@link Function} returns null.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A 'readonly' {@link IDependencyObject} build from the given getter.
     */
    public static <T> IDependencyObject<T> createFromGetterOnly(
      @NotNull final Function<Object, T> dependencyGetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.create(
            Optional.of(dependencyGetter),
            Optional.empty(),
            true
          ),
          defaultValue
        );
    }

    /**
     * Creates a {@link IDependencyObject} from a {@link Property}.
     * <p>
     * This means that when ever {@link IDependencyObject#get(Object)} or {@link IDependencyObject#set(Object, Object)}
     * are called the respective methods {@link Property#apply(Object)} and {@link IDependencyObject#set(Object, Object)}
     * allowing the {@link IDependencyObject} to dynamically return a value depending on the given datacontext or other external factors.
     * <p>
     * If the {@link Property} returns{@code null} from its {@link Property#apply(Object)} during a call to {@link IDependencyObject#get(Object)}
     * then the given default value will be returned.
     *
     * @param property The {@link Property} to link the {@link IDependencyObject} to.
     * @param def      The default value.
     * @param <T>      The type of {@link IDependencyObject} to build. Needs to be the same as the type of the {@link Property}
     * @return A dynamic {@link IDependencyObject} that links a datacontext to a {@link Property}
     */
    public static <T> IDependencyObject<T> createFromProperty(@NotNull Property<T> property, @Nullable T def)
    {
        return new PropertyBasedDependencyObject<>(property, def);
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Optional, Optional,
     * boolean)}.
     * <p>
     * Useful for when code clutter wants to be prevented and only a getter is required (EG in cases where the {@link IDependencyObject} should only be read by the respective
     * consumer.
     * All calls to {@link IDependencyObject#set(Object, Object)} will be disregarded.
     *
     * @param dependencyGetter The {@link Supplier} that functions as a getter. This will be called when {@link IDependencyObject#get(Object)} is called.
     * @param defaultValue     The default value in case the given {@link Function} returns null.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A 'readonly' {@link IDependencyObject} build from the given getter.
     */
    public static <T> IDependencyObject<T> createFromGetterOnly(
      @NotNull final Supplier<T> dependencyGetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.create(
            Optional.of(c -> dependencyGetter.get()),
            Optional.empty(),
            false
          ),
          defaultValue
        );
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Optional, Optional,
     * boolean)}.
     * <p>
     * Since this has a nasty side effect of always returning the given defaultValue i am not sure how much this method will be used.
     * It is here for completeness sake.
     * <p>
     * Useful for when code clutter wants to be prevented and only a setter is required (EG in cases where the {@link IDependencyObject} should only be written by the respective
     * consumer.
     * All calls to {@link IDependencyObject#get(Object)} will be disregarded.
     *
     * @param dependencySetter The {@link BiConsumer} that functions as a setter. This will be called when {@link IDependencyObject#set(Object, Object)} is called.
     * @param defaultValue     The default value returned by {@link IDependencyObject#get(Object)} on every call to it.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A 'readonly' {@link IDependencyObject} build from the given getter.
     */
    public static <T> IDependencyObject<T> createFromSetterOnly(
      @NotNull final BiConsumer<Object, T> dependencySetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.create(
            Optional.empty(),
            Optional.of(dependencySetter),
            true
          ),
          defaultValue
        );
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Optional, Optional,
     * boolean)}.
     * <p>
     * Since this has a nasty side effect of always returning the given defaultValue i am not sure how much this method will be used.
     * It is here for completeness sake.
     * <p>
     * Useful for when code clutter wants to be prevented and only a setter is required (EG in cases where the {@link IDependencyObject} should only be written by the respective
     * consumer.
     * All calls to {@link IDependencyObject#get(Object)} will be disregarded.
     *
     * @param dependencySetter The {@link Consumer} that functions as a setter. This will be called when {@link IDependencyObject#set(Object, Object)} is called.
     * @param defaultValue     The default value returned by {@link IDependencyObject#get(Object)} on every call to it.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A 'readonly' {@link IDependencyObject} build from the given getter.
     */
    public static <T> IDependencyObject<T> createFromSetterOnly(
      @NotNull final Consumer<T> dependencySetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.create(
            Optional.empty(),
            Optional.of((c, t) -> dependencySetter.accept(t)),
            false
          ),
          defaultValue
        );
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Optional, Optional,
     * boolean)}.
     * <p>
     * Useful for when code clutter wants to be prevented.
     *
     * @param dependencyGetter The {@link Function} that functions as a getter. This will be called when {@link IDependencyObject#get(Object)} is called.
     * @param dependencySetter The {@link BiConsumer} that functions as a setter. This will be called when {@link IDependencyObject#set(Object, Object)} is called.
     * @param defaultValue     The default value in case the given getter returns null.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A {@link IDependencyObject} build from the given getter.
     *
     * @see DependencyObjectHelper#createFromProperty(Property, Object) for more information on the exact behaviour.
     */
    public static <T> IDependencyObject<T> createFromSetterAndGetter(
      @NotNull final Function<Object, T> dependencyGetter,
      @NotNull final BiConsumer<Object, T> dependencySetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.createFromNonOptional(
            dependencyGetter,
            dependencySetter,
            true
          ),
          defaultValue
        );
    }

    /**
     * This helper function wraps {@link DependencyObjectHelper#createFromProperty(Property, Object)} and {@link PropertyCreationHelper#create(Optional, Optional,
     * boolean)}.
     * <p>
     * Useful for when code clutter wants to be prevented.
     *
     * @param dependencyGetter The {@link Supplier} that functions as a getter. This will be called when {@link IDependencyObject#get(Object)} is called.
     * @param dependencySetter The {@link Consumer} that functions as a setter. This will be called when {@link IDependencyObject#set(Object, Object)} is called.
     * @param defaultValue     The default value in case the given getter returns null.
     * @param <T>              The type of the created {@link IDependencyObject}
     * @return A {@link IDependencyObject} build from the given getter.
     *
     * @see DependencyObjectHelper#createFromProperty(Property, Object) for more information on the exact behaviour.
     */
    public static <T> IDependencyObject<T> createFromSetterAndGetter(
      @NotNull final Supplier<T> dependencyGetter,
      @NotNull final Consumer<T> dependencySetter,
      @NotNull final T defaultValue
    )
    {
        return createFromProperty(
          PropertyCreationHelper.createFromNonOptional(
            c -> dependencyGetter.get(),
            (c, t) -> dependencySetter.accept(t),
            true
          ),
          defaultValue
        );
    }
}