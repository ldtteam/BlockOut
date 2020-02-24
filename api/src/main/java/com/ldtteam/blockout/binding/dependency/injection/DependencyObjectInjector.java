package com.ldtteam.blockout.binding.dependency.injection;

import com.ldtteam.blockout.binding.dependency.IDependencyReceiver;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class that handles injecting dependency data from the {@link IDependencyDataProvider} into the {@link IDependencyReceiver}
 * <p>
 * This is a utility class that has only static methods, calling its constructor will throw an exception.
 */
public final class DependencyObjectInjector
{

    /**
     * Private constructor.
     * Calling this throws an exception since this class is a utility class with only static methods.
     */
    private DependencyObjectInjector()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Method to handle the injection over ReflectASM.
     * Will find all public fields of type {@link IDependencyObject} and will try to bind them using the {@link IDependencyDataProvider}.
     * <p>
     * If the provider can not bind to the {@link IDependencyObject} then no change is made.
     *
     * @param target   The target object on which binding is performed.
     * @param provider The provider to test against.
     */
    @SuppressWarnings("static")
    public static void inject(@NotNull final IDependencyReceiver target, @NotNull final IDependencyDataProvider provider)
    {
        ProxyHolder.getInstance().getReflectionManager().getFieldsForClass(target.getClass())
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