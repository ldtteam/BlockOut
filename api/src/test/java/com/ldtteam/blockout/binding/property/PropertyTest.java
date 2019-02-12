package com.ldtteam.blockout.binding.property;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import scala.tools.cmd.gen.AnyValReps;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class PropertyTest
{

    @Parameters({
      "true",
      "false"
    })
    @Test
    public void requiresDataContext(boolean input)
    {
        final Property<String> targetOne = new Property<>(Optional.empty(), Optional.empty(), input);

        Assert.assertEquals(input, targetOne.requiresDataContext());
    }

    @Parameters({
      "input",
      "anotherInput"
    })
    @Test
    public void accept(String input)
    {
        final Property<String> target = new Property<>(Optional.of(c -> input), Optional.empty(), false);
        Assert.assertEquals(input, target.apply(new Object()).get());
        Assert.assertEquals(input, target.apply("").get());
    }

    @Parameters({
      "input",
      "anotherInput"
    })
    @Test
    public void apply(String input)
    {
        final VariableHolder holder = new VariableHolder("");
        final Property<String> target = new Property<>(Optional.empty(), Optional.of((Object c, String o) -> holder.setValue(o)), false);

        Assert.assertEquals("", holder.getValue());
        target.accept(new Object(), input);
        Assert.assertEquals(input, holder.getValue());
    }

    private class VariableHolder
    {
        private String value;

        public VariableHolder(final String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(final String value)
        {
            this.value = value;
        }
    }
}