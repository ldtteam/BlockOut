package com.ldtteam.blockout.binding.dependency.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DependentObject
{

    /**
     * Class that implements injection
     */
    Class<?> objectHelper = null;
}
