package com.ldtteam.blockout.json.loader.binding.transformer;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.json.loader.binding.core.IBindingTransformer;
import com.ldtteam.blockout.json.loader.binding.core.ITransformerBindSupplier;
import org.jetbrains.annotations.NotNull;

public class IntToStringTransformer implements IBindingTransformer
{
    private static final String CONST_TRANSFORMERNAME = "IntToString";

    @Override
    public String getTransformerName()
    {
        return CONST_TRANSFORMERNAME;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IDependencyObject<String> generateTransformingBind(@NotNull final ITransformerBindSupplier bindSupplier)
    {
        final IDependencyObject<Integer> inputTransformer = (IDependencyObject<Integer>) bindSupplier.getNextDependencyInChain();

        return DependencyObjectHelper.transform(
          inputTransformer,
          String::valueOf,
          Integer::parseInt
        );
    }
}
