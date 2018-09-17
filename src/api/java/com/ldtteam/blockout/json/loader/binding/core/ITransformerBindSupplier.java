package com.ldtteam.blockout.json.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;

@FunctionalInterface
public interface ITransformerBindSupplier
{

    IDependencyObject<?> getNextDependencyInChain();
}
