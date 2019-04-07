package com.ldtteam.blockout_test.tests;

import com.google.common.collect.Lists;
import com.ldtteam.blockout_test.tests.guis.*;

import java.util.List;

public class BlockOutUITestManager
{
    private static BlockOutUITestManager  ourInstance = new BlockOutUITestManager();
    private final  List<IBlockOutGuiTest> testList;

    private BlockOutUITestManager()
    {
        testList = Lists.newArrayList(
          new ImageOnlyTest(),
          new BoundListHorizontalTest(),
          new InventoryGridTest(),
          new TextFieldInputTest(),
          new CountdownTest(),
          new GrowingListWithButtonTest(),
          new RangeSelectorTest()
        );
    }

    public static BlockOutUITestManager getInstance()
    {
        return ourInstance;
    }

    public List<IBlockOutGuiTest> getTestList()
    {
        return testList;
    }
}
