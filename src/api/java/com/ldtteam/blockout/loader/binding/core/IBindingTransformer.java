package com.ldtteam.blockout.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

public interface IBindingTransformer
{

    /**
     * This straight up name is used to identify which {@link IBindingTransformer} is required
     * for the binding by the {@link IBindingEngine}.
     *
     * This transformer is used if the returned transformer name is residing inside the transformer name list of the binding command.
     *
     * @return The transformer name.
     */
    String getTransformerName();

    <T> IDependencyObject<T> generateTransformingBind(
      @NotNull ITransformerBindSupplier bindSupplier
    );
}
