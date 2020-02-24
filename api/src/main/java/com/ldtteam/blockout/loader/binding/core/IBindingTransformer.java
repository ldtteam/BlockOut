package com.ldtteam.blockout.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Defines a transformer that can be used to transform a binding from one type to another,
 * This transformation is a duplex connection, and a transform that happens in one way has to be able to happen in the other direction as well.
 *
 * @param <F> The type from which the transformation in the reading direction occurs.
 * @param <T> The type to which the transformation in the reading directions occurs,
 */
public interface IBindingTransformer<F, T>
{

    /**
     * This straight up name is used to identify which {@link IBindingTransformer} is required
     * for the binding by the {@link IBindingEngine}.
     * <p>
     * This transformer is used if the returned transformer name is residing inside the transformer name list of the binding command.
     *
     * @return The transformer name.
     */
    String getTransformerName();

    /**
     * Executes the transformation binding.
     *
     * @param bindSupplier Provides the 'from' dependency object.
     * @return The 'to' dependency object (a transformed from object)
     */
    IDependencyObject<T> generateTransformingBind(
      @NotNull Supplier<IDependencyObject<F>> bindSupplier
    );
}
