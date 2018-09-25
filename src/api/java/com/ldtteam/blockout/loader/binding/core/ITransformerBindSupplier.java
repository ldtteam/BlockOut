package com.ldtteam.blockout.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;

@FunctionalInterface
public interface ITransformerBindSupplier
{

    IDependencyObject<?> getNextDependencyInChain();
}
