package com.ldtteam.blockout.binding.dependency.injection;

import com.ldtteam.blockout.AbstractBlockOutApiTest;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.DummyInjectionTarget;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.util.Suppression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link DependencyObjectInjector}.
 * Validates the behaviour of injection.
 */
public class DependencyObjectInjectorTest extends AbstractBlockOutApiTest
{

    /**
     * Unit test to assure that injection of static values works.
     */
    @Test
    public void InjectStatic()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final SuccessfullStaticInjectionDataProvider staticInjectionDataProvider = new SuccessfullStaticInjectionDataProvider();

        DependencyObjectInjector.inject(target, staticInjectionDataProvider);

        Assert.assertNotEquals(DummyInjectionTarget.INITIAL_VALUE, target.getCurrentValue());
        Assert.assertEquals(SuccessfullStaticInjectionDataProvider.TARGET_VALUE, target.getCurrentValue());
    }

    /**
     * Unit test to assure that injection of dynamic datacontext depending values works.
     */
    @Test
    public void InjectDynamic()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final SuccessfullDynamicInjectionDataProvider staticInjectionDataProvider = new SuccessfullDynamicInjectionDataProvider();

        DependencyObjectInjector.inject(target, staticInjectionDataProvider);

        Assert.assertNotEquals(DummyInjectionTarget.INITIAL_VALUE, target.getCurrentValue());
        Assert.assertEquals(target.getDataContext().toString(), target.getCurrentValue());
    }

    /**
     * Unit test to assure that injection without valid information does not change the existing value.
     */
    @Test
    public void InjectFailed()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final FailedInjectionDataProvider staticInjectionDataProvider = new FailedInjectionDataProvider();

        DependencyObjectInjector.inject(target, staticInjectionDataProvider);

        Assert.assertEquals(DummyInjectionTarget.INITIAL_VALUE, target.getCurrentValue());
    }

    /**
     * Unit test data provider class that will successfully inject a static object.
     * inject a target value.
     */
    private static final class SuccessfullStaticInjectionDataProvider implements IDependencyDataProvider
    {

        private static final String TARGET_VALUE = "NewValueIsNotInitial";

        @Override
        public boolean hasDependencyData(@NotNull final String name)
        {
            return name.equals(String.format("%s#%s", DummyInjectionTarget.ID, DummyInjectionTarget.TARGET_FIELD_NAME));
        }

        @SuppressWarnings(Suppression.UNCHECKED)
        @NotNull
        @Override
        public <T> IDependencyObject<T> get(@NotNull final String name)
        {
            return (IDependencyObject<T>) DependencyObjectHelper.createFromValue(TARGET_VALUE);
        }
    }

    /**
     * Unit test data provider class that will successfully inject a dynamic datacontext depending dependency.
     * inject a target value.
     */
    private static final class SuccessfullDynamicInjectionDataProvider implements IDependencyDataProvider
    {

        private static final String TARGET_VALUE = "NewValueIsNotInitial";

        @Override
        public boolean hasDependencyData(@NotNull final String name)
        {
            return name.equals(String.format("%s#%s", DummyInjectionTarget.ID, DummyInjectionTarget.TARGET_FIELD_NAME));
        }

        @SuppressWarnings(Suppression.UNCHECKED)
        @NotNull
        @Override
        public <T> IDependencyObject<T> get(@NotNull final String name)
        {
            return (IDependencyObject<T>) DependencyObjectHelper.createFromGetterOnly(Object::toString, "ERROR");
        }
    }

    /**
     * Unit test data provider class that will fail to inject any dependency.
     * inject a target value.
     */
    private static final class FailedInjectionDataProvider implements IDependencyDataProvider
    {

        private static final String TARGET_VALUE = "NewValueIsNotInitial";

        @Override
        public boolean hasDependencyData(@NotNull final String name)
        {
            return false;
        }

        @SuppressWarnings(Suppression.UNCHECKED)
        @Nullable
        @Override
        public <T> IDependencyObject<T> get(@NotNull final String name)
        {
            return null;
        }
    }
}