package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.StaticValueProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class StaticDependencyObjectTest
{

    @Test
    public void get()
    {
        final IDependencyObject<String> depOne = new StaticDependencyObject<>("Static");

        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));
    }

    @Test
    public void set()
    {
        final IDependencyObject<String> depOne = new StaticDependencyObject<>("Static");

        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));

        depOne.set(new DummyInjectionTarget(), "Changed");

        Assert.assertEquals("Changed", depOne.get(new DummyInjectionTarget()));
    }

    @Test
    public void hasChanged()
    {
        final IDependencyObject<String> depOne = new StaticDependencyObject<>("Static");

        Assert.assertTrue(depOne.hasChanged(new DummyInjectionTarget()));
        Assert.assertEquals("Static", depOne.get(new DummyInjectionTarget()));
        Assert.assertFalse(depOne.hasChanged(new DummyInjectionTarget()));

        depOne.set(new DummyInjectionTarget(), "Changed");
        Assert.assertTrue(depOne.hasChanged(new DummyInjectionTarget()));
    }
}