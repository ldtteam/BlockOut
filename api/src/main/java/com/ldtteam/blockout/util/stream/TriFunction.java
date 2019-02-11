package com.ldtteam.blockout.util.stream;

import java.util.Objects;
import java.util.function.Function;

/**
 * A function with three input variables and one out.
 *
 * @param <X> First input variable
 * @param <Y> Second input variable
 * @param <Z> Third input variable
 * @param <A> Result
 */
@FunctionalInterface
public interface TriFunction<X, Y, Z, A>
{
    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     */
    default <V> TriFunction<X, Y, Z, V> andThen(Function<? super A, ? extends V> after)
    {
        Objects.requireNonNull(after);
        return (X x, Y y, Z z) -> after.apply(apply(x, y, z));
    }

    /**
     * Applies this function to the given arguments.
     *
     * @param x the first function argument
     * @param y the second function argument
     * @param z the third function argument
     * @return the function result
     */
    A apply(X x, Y y, Z z);
}
