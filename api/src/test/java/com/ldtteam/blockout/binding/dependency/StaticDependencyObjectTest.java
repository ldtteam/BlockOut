package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.AbstractBlockOutApiTest;
import org.junit.Assert;
import org.junit.Test;

public class StaticDependencyObjectTest extends AbstractBlockOutApiTest
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