package com.ldtteam.blockout.binding.dependency.injection;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.reflection.ReflectionManager;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class DependencyObjectInjector
{

    private DependencyObjectInjector()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static void inject(@NotNull final IUIElement target, @NotNull final IDependencyDataProvider provider)
    {
        ReflectionManager.getInstance().getFieldsForClass(target.getClass())
          .stream()
          .filter(field -> field.getType().equals(IDependencyObject.class))
          .forEach(field -> {
              final IDependencyObject<?> current;
              try
              {
                  current = (IDependencyObject<?>) field.get(target);
              }
              catch (IllegalAccessException ex)
              {
                  Log.getLogger().error("Failed to get dependency object instance. Needs to be either protected, public, or package private. Private field is not supported.");
                  return;
              }

              if (current == null)
              {
                  Log.getLogger().error("Failed to get dependency object instance. It is not set!");
                  return;
              }

              final Type fieldType = current.getClass().getGenericInterfaces()[0];
              if (fieldType instanceof ParameterizedType)
              {
                  final ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                  final Type containedType = parameterizedType.getActualTypeArguments()[0];
                  final String dependencyDataName = String.format("%s#%s", target.getId(), field.getName());

                  if (provider.hasDependencyData(dependencyDataName))
                  {
                      try
                      {
                          field.set(target, provider.get(dependencyDataName));
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
                                  Log.getLogger().error("Failed to bind the dependency object using the provider: " + field.getName(), e);
                              }
                          }
                      }
                  }
              }
          });
    }
}