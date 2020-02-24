package com.ldtteam.blockout.loader.binding.transformer;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.loader.binding.core.IBindingTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class IntToStringTransformer implements IBindingTransformer<Integer, String>
{
    private static final String CONST_TRANSFORMER_NAME = "IntToString";

    @Override
    public String getTransformerName()
    {
        return CONST_TRANSFORMER_NAME;
    }

    @Override
    public IDependencyObject<String> generateTransformingBind(@NotNull final Supplier<IDependencyObject<Integer>> bindSupplier)
    {
        final IDependencyObject<Integer> inputTransformer = bindSupplier.get();

        return DependencyObjectHelper.transform(
          inputTransformer,
          String::valueOf,
          Integer::parseInt
        );
    }
}
