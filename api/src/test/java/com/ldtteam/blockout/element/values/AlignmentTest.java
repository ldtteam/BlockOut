package com.ldtteam.blockout.element.values;

import com.ldtteam.blockout.AbstractBlockOutApiTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.EnumSet;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class AlignmentTest extends AbstractBlockOutApiTest
{

    @Parameters({
      "LEFT, 1",
      "RIGHT, 2",
      "TOP, 4",
      "BOTTOM, 8",
      "NONE, 0"
    })
    @Test
    public void toIntSingle(Alignment alignment, int expected)
    {
        Assert.assertEquals(expected, Alignment.toInt(EnumSet.of(alignment)));
    }

    @Parameters({
      "LEFT, 1",
      "RIGHT, 2",
      "TOP, 4",
      "BOTTOM, 8",
      "NONE, 0"
    })
    @Test
    public void getBitMask(Alignment alignment, int expected)
    {
        Assert.assertEquals(expected, alignment.getBitMask());
    }

    @Parameters({
      "LEFT, 1",
      "RIGHT, 2",
      "TOP, 4",
      "BOTTOM, 8",
      "NONE, 0"
    })
    @Test
    public void fromString(String alignment, int expected)
    {
        Assert.assertEquals(expected, Alignment.toInt(Alignment.fromString(alignment)));
    }

    @Parameters({
      "1, 1",
      "2, 2",
      "4, 4",
      "8, 8",
      "0, 0"
    })
    @Test
    public void fromInt(int alignment, int expected)
    {
        Assert.assertEquals(expected, Alignment.toInt(Alignment.fromInt(alignment)));
    }

    @Parameters({
      "LEFT, true, false, false, false, false",
      "RIGHT, false, true, false, false ,false",
      "TOP, false, false, true, false, false",
      "BOTTOM, false, false, false, true, false",
      "NONE, false, false, false, false, true"
    })
    @Test
    public void isActive(Alignment alignment, boolean left, boolean right, boolean top, boolean bottom, boolean none)
    {
        Assert.assertEquals(left, Alignment.LEFT.isActive(EnumSet.of(alignment)));
        Assert.assertEquals(right, Alignment.RIGHT.isActive(EnumSet.of(alignment)));
        Assert.assertEquals(top, Alignment.TOP.isActive(EnumSet.of(alignment)));
        Assert.assertEquals(bottom, Alignment.BOTTOM.isActive(EnumSet.of(alignment)));
        Assert.assertEquals(none, Alignment.NONE.isActive(EnumSet.of(alignment)));
    }
}