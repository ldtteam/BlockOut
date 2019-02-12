package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.*;

public class DependencyObjectHelperTest
{

    @Test
    public void createFromValue()
    {
        final IDependencyObject<String> target;
    }

    @Test
    public void transform()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final IDependencyObject<String> input = new TestInputDependencyObject();
        final IDependencyObject<String> transformed = DependencyObjectHelper.transform(input, s -> s.replace("World", "Test"), s -> s.replace("Test", "World"));

        Assert.assertTrue(transformed.hasChanged(target));
        Assert.assertEquals("HelloTest", transformed.get(target));

        transformed.set(target, "HelloTest");
        Assert.assertEquals("HelloWorld", input.get(target));

        transformed.set(target, "Hello");
        Assert.assertEquals("Hello", input.get(target));
    }

    @Test
    public void createFromGetterOnly()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final IDependencyObject<String> dep = DependencyObjectHelper.createFromGetterOnly(() -> "Value", "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Value", dep.get(target));

        dep.set(target, "NotValue");

        Assert.assertEquals("Value", dep.get(target));
    }

    @Test
    public void createFromProperty()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final Property<String> property = PropertyCreationHelper.create(Optional.empty(), Optional.empty(), false);
        final IDependencyObject<String> dep = DependencyObjectHelper.createFromProperty(property, "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Default", dep.get(target));
    }

    @Test
    public void createFromGetterOnly1()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final LambdaWrapper wrapper = new LambdaWrapper();
        final IDependencyObject<String> dep = DependencyObjectHelper.createFromGetterOnly((c) -> wrapper.getValue(), "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Value", dep.get(target));

        dep.set(target, "Set");

        Assert.assertEquals("Value", wrapper.getValue());
        Assert.assertEquals("Value", dep.get(target));
    }

    @Test
    public void createFromSetterOnly()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final LambdaWrapper wrapper = new LambdaWrapper();

        final IDependencyObject<String> dep = DependencyObjectHelper.createFromSetterOnly(wrapper::setValue, "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Default", dep.get(target));

        Assert.assertEquals("Value", wrapper.getValue());
        dep.set(target, "Set");

        Assert.assertEquals("Default", dep.get(target));
        Assert.assertEquals("Set", wrapper.getValue());
    }

    @Test
    public void createFromSetterOnly1()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final LambdaWrapper wrapper = new LambdaWrapper();

        final IDependencyObject<String> dep = DependencyObjectHelper.createFromSetterOnly((c, o) -> wrapper.setValue(o), "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Default", dep.get(target));

        Assert.assertEquals("Value", wrapper.getValue());
        dep.set(target, "Set");

        Assert.assertEquals("Default", dep.get(target));
        Assert.assertEquals("Set", wrapper.getValue());
    }

    @Test
    public void createFromSetterAndGetter()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final LambdaWrapper wrapper = new LambdaWrapper();

        final IDependencyObject<String> dep = DependencyObjectHelper.createFromSetterAndGetter(wrapper::getValue, wrapper::setValue, "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Value", dep.get(target));
        Assert.assertEquals("Value", wrapper.getValue());

        dep.set(target, "Set");

        Assert.assertEquals("Set", dep.get(target));
        Assert.assertEquals("Set", wrapper.getValue());
    }

    @Test
    public void createFromSetterAndGetter1()
    {
        final DummyInjectionTarget target = new DummyInjectionTarget();
        final LambdaWrapper wrapper = new LambdaWrapper();

        final IDependencyObject<String> dep = DependencyObjectHelper.createFromSetterAndGetter(o -> wrapper.getValue(), (o, s) -> wrapper.setValue(s), "Default");

        Assert.assertTrue(dep instanceof PropertyBasedDependencyObject);
        Assert.assertEquals("Value", dep.get(target));
        Assert.assertEquals("Value", wrapper.getValue());

        dep.set(target, "Set");

        Assert.assertEquals("Set", dep.get(target));
        Assert.assertEquals("Set", wrapper.getValue());
    }

    private static final class LambdaWrapper
    {
        private String value = "Value";

        public String getValue()
        {
            return value;
        }

        public void setValue(final String value)
        {
            this.value = value;
        }
    }

    private final class TestInputDependencyObject implements IDependencyObject<String>
    {

        private static final String CONST_INITIAL_VALUE = "HelloWorld";

        private boolean hasChanged = true;
        private String  value      = CONST_INITIAL_VALUE;

        @Override
        public boolean requiresDataContext()
        {
            return false;
        }

        @Nullable
        @Override
        public String get(@Nullable final Object context)
        {
            hasChanged = false;
            return value;
        }

        @Override
        public void set(@Nullable final Object context, @Nullable final String value)
        {
            this.value = value;
            this.hasChanged = true;
        }

        @Override
        public boolean hasChanged(@Nullable final Object context)
        {
            return hasChanged;
        }
    }
}