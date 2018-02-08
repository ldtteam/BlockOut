package com.minecolonies.blockout.binding.dependency.injection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.reflection.ReflectionUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public final class DependencyObjectInjector
{

    private static final Cache<Class<?>, Set<Field>> FIELD_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private DependencyObjectInjector()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static void inject(@NotNull final Object target, @NotNull final IDependencyDataProvider provider)
    {
        getFields(target.getClass())
          .stream()
          .filter(field -> field.getType().equals(IDependencyObject.class))
          .forEach(field -> {
              field.setAccessible(true);

              final IDependencyObject<?, ?> current;
              try
              {
                  current = (IDependencyObject<?, ?>) field.get(target);
              }
              catch (IllegalAccessException e)
              {
                  Log.getLogger().error("Failed to get the current default dependency object. Is '" + field.getName() + "' not initialized?", e);
                  return;
              }

              final Type fieldType = field.getGenericType();
              if (fieldType instanceof ParameterizedType)
              {
                  final ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                  final Type containedType = parameterizedType.getActualTypeArguments()[0];

                  if (provider.has(field.getName()))
                  {
                      try
                      {
                          field.set(target, provider.get(field.getName(), current, containedType));
                      }
                      catch (Exception e)
                      {
                          Log.getLogger().error("Failed to bind the dependency object using the provider: " + field.getName(), e);
                      }
                  }
                  else if (containedType instanceof AnnotatedElement)
                  {
                      final AnnotatedElement annotatedElement = (AnnotatedElement) containedType;
                      if (annotatedElement.isAnnotationPresent(DependentObject.class))
                      {
                          final DependentObject dependentObject = annotatedElement.getAnnotation(DependentObject.class);
                          Object injectionControllerCandidate = null;
                          try
                          {
                              injectionControllerCandidate = dependentObject.objectHelper.getConstructor().newInstance();
                          }
                          catch (Exception e)
                          {
                              Log.getLogger().error("Failed to get the injection controller for: " + containedType.getTypeName());
                          }

                          if (injectionControllerCandidate instanceof IInjectionController)
                          {
                              final IInjectionController controller = (IInjectionController) injectionControllerCandidate;
                              try
                              {
                                  field.set(target, controller.get(current, provider));
                              }
                              catch (IllegalAccessException e)
                              {
                                  Log.getLogger().error("Failed to bind the dependency object by using its controller.", e);
                              }
                          }
                      }
                  }
              }
          });
    }

    private static final Set<Field> getFields(@NotNull final Class<?> targetClass)
    {
        try
        {
            return FIELD_CACHE.get(targetClass, () -> ReflectionUtil.getAllFields(targetClass));
        }
        catch (ExecutionException e)
        {
            Log.getLogger().error("Failed to retrieve fields from: " + targetClass.getName(), e);
            return ImmutableSet.of();
        }
    }
}