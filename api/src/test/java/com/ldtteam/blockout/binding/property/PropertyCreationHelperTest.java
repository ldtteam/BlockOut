package com.ldtteam.blockout.binding.property;

import com.ldtteam.blockout.AbstractBlockOutApiTest;
import com.ldtteam.blockout.binding.dependency.DummyInjectionTarget;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.*;

public class PropertyCreationHelperTest extends AbstractBlockOutApiTest
{

    @Test
    public void createFromStaticValue()
    {
        final Property<String> property = PropertyCreationHelper.createFromStaticValue("Static");

        Assert.assertTrue(property instanceof StaticValueProperty);
        Assert.assertEquals("Static", property.apply(new Object()).get());

        property.accept(new Object(), "Set");

        Assert.assertEquals("Set", property.apply(new Object()).get());
    }

    @Test
    public void createFromOptional()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> bothSet = PropertyCreationHelper.createFromOptional(o -> testTarget.getValueOne(), (o, s) -> testTarget.setValueOne(s), false);

        Assert.assertFalse(bothSet instanceof StaticValueProperty);
        Assert.assertEquals("One", bothSet.apply(new Object()).get());

        bothSet.accept(new Object(), "Set");
        Assert.assertEquals("Set", testTarget.getValueOne());

        final Property<String> getterNoneNull = PropertyCreationHelper.createFromOptional(o -> testTarget.getValueOne(), null, false);
        Assert.assertEquals("Set", getterNoneNull.apply(new Object()).get());

        getterNoneNull.accept(new Object(), "NotSet");
        Assert.assertEquals("Set", testTarget.getValueOne());

        final Property<String> setterNoneNull = PropertyCreationHelper.createFromOptional(null, (c, o) -> testTarget.setValueOne(o), false);
        Assert.assertFalse(setterNoneNull.apply(new Object()).isPresent());

        setterNoneNull.accept(new Object(), "NotSet");
        Assert.assertEquals("NotSet", testTarget.getValueOne());
    }

    @Test
    public void create()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> bothSet = PropertyCreationHelper.create(Optional.of(o -> testTarget.getValueOne()), Optional.of((o, s) -> testTarget.setValueOne(s)), false);

        Assert.assertFalse(bothSet instanceof StaticValueProperty);
        Assert.assertEquals("One", bothSet.apply(new Object()).get());

        bothSet.accept(new Object(), "Set");
        Assert.assertEquals("Set", testTarget.getValueOne());

        final Property<String> getterNoneNull = PropertyCreationHelper.create(Optional.of(o -> testTarget.getValueOne()), Optional.empty(), false);
        Assert.assertEquals("Set", getterNoneNull.apply(new Object()).get());

        getterNoneNull.accept(new Object(), "NotSet");
        Assert.assertEquals("Set", testTarget.getValueOne());

        final Property<String> setterNoneNull = PropertyCreationHelper.create(Optional.empty(), Optional.of((c, o) -> testTarget.setValueOne(o)), false);
        Assert.assertFalse(setterNoneNull.apply(new Object()).isPresent());

        setterNoneNull.accept(new Object(), "NotSet");
        Assert.assertEquals("NotSet", testTarget.getValueOne());
    }

    @Test
    public void createFromNonOptionalSuccessful()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> bothSet = PropertyCreationHelper.createFromNonOptional(o -> testTarget.getValueOne(), (o, s) -> testTarget.setValueOne(s), false);

        Assert.assertFalse(bothSet instanceof StaticValueProperty);
        Assert.assertEquals("One", bothSet.apply(new Object()).get());

        bothSet.accept(new Object(), "Set");
        Assert.assertEquals("Set", testTarget.getValueOne());
    }

    @Test(expected = NullPointerException.class)
    public void createFromNonOptionalFailureGetter()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> getterNoneNull = PropertyCreationHelper.createFromNonOptional(o -> testTarget.getValueOne(), null, false);

        fail();
    }

    @Test(expected = NullPointerException.class)
    public void createFromNonOptionalFailureSetter()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> setterNoneNull = PropertyCreationHelper.createFromNonOptional(null, (c, o) -> testTarget.setValueOne(o), false);

        fail();
    }

    @Test
    public void createFromName()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> bothSet = PropertyCreationHelper.createFromName(Optional.of("ValueOne"));

        Assert.assertFalse(bothSet instanceof StaticValueProperty);
        Assert.assertEquals("One", bothSet.apply(testTarget).get());

        bothSet.accept(testTarget, "Set");
        Assert.assertEquals("Set", testTarget.getValueOne());
    }

    @Test
    public void createFromName1()
    {
        final DummyTestTarget testTarget = new DummyTestTarget();
        final Property<String> bothSet = PropertyCreationHelper.createFromName(Optional.of("getValueOne"), Optional.of("setValueTwo"));

        Assert.assertFalse(bothSet instanceof StaticValueProperty);
        Assert.assertEquals("One", bothSet.apply(testTarget).get());

        bothSet.accept(testTarget, "Set");
        Assert.assertEquals("Set", testTarget.getValueTwo());
        Assert.assertEquals("One", testTarget.getValueOne());
    }

    private static final class DummyTestTarget
    {
        private String valueOne = "One";
        private String valueTwo = "Two";

        public DummyTestTarget()
        {
        }

        public String getValueOne()
        {
            return valueOne;
        }

        public void setValueOne(final String valueOne)
        {
            this.valueOne = valueOne;
        }

        public String getValueTwo()
        {
            return valueTwo;
        }

        public void setValueTwo(final String valueTwo)
        {
            this.valueTwo = valueTwo;
        }
    }
}