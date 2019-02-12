package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.binding.property.StaticValueProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class PropertyBasedDependencyObjectTest
{

    @Test
    public void requiresDataContextFalse()
    {
        final Property<String> targetOne = new StaticValueProperty<>("Hello");
        final IDependencyObject<String> depOne = new PropertyBasedDependencyObject<>(targetOne, "Default");

        Assert.assertFalse(depOne.requiresDataContext());
    }

    @Test
    public void requiresDataContextTrue()
    {
        final Property<String> targetOne = new Property<>(Optional.empty(), Optional.empty(), true);
        final IDependencyObject<String> depOne = new PropertyBasedDependencyObject<>(targetOne, "Default");

        Assert.assertTrue(depOne.requiresDataContext());
    }

    @Test
    public void get()
    {
        final Property<String> targetOne = new StaticValueProperty<>("Static");
        final IDependencyObject<String> depOne = new PropertyBasedDependencyObject<>(targetOne, "Default");

        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));
    }

    @Test
    public void set()
    {
        final Property<String> targetOne = new StaticValueProperty<>("Static");
        final IDependencyObject<String> depOne = new PropertyBasedDependencyObject<>(targetOne, "Default");

        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));

        depOne.set(new DummyInjectionTarget(), "Changed");

        final Optional<String> result = targetOne.apply(new Object());
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("Changed", result.get());
    }

    @Test
    public void hasChanged()
    {
        final Property<String> targetOne = new StaticValueProperty<>("Static");
        final IDependencyObject<String> depOne = new PropertyBasedDependencyObject<>(targetOne, "Default");

        Assert.assertTrue(depOne.hasChanged(new DummyInjectionTarget()));
        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));
        Assert.assertFalse(depOne.hasChanged(new DummyInjectionTarget()));

        depOne.set(new DummyInjectionTarget(), "Changed");
        Assert.assertTrue(depOne.hasChanged(new DummyInjectionTarget()));
    }
}