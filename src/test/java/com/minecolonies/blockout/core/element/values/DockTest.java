package com.minecolonies.blockout.core.element.values;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class DockTest
{

    @NotNull
    private final BoundingBox localParentBox  = new BoundingBox(new Vector2d(), new Vector2d(10, 6));
    @NotNull
    private final BoundingBox localTestBox    = new BoundingBox(new Vector2d(2, 2), new Vector2d(2, 3));
    @NotNull
    private final BoundingBox resultLeftBox   = new BoundingBox(new Vector2d(), new Vector2d(2, 6));
    @NotNull
    private final BoundingBox resultTopBox    = new BoundingBox(new Vector2d(), new Vector2d(10, 3));
    @NotNull
    private final BoundingBox resultRightBox  = new BoundingBox(new Vector2d(8, 0), new Vector2d(2, 6));
    @NotNull
    private final BoundingBox resultBottomBox = new BoundingBox(new Vector2d(0, 3), new Vector2d(10, 3));
    @NotNull
    private final BoundingBox resultNoneBox   = new BoundingBox(localTestBox);
    @NotNull
    private final BoundingBox resultFullBox   = new BoundingBox(localParentBox);
    @Mock
    private IUIElementHost host;
    @Mock
    private IUIElement     element;

    @Before
    public void setUp()
    {
        when(host.getLocalInternalBoundingBox()).thenReturn(localParentBox);
        when(element.getParent()).thenReturn(host);
    }

    @Test
    public void applyWithValidAlignment()
    {
        //Lets run all test with a valid NONE alignment to verify BoundingBox calculations.
        when(element.getAlignment()).thenReturn(EnumSet.of(Alignment.NONE));
        assertEquals(resultLeftBox, Dock.LEFT.apply(element, localTestBox));
        assertEquals(resultTopBox, Dock.TOP.apply(element, localTestBox));
        assertEquals(resultRightBox, Dock.RIGHT.apply(element, localTestBox));
        assertEquals(resultBottomBox, Dock.BOTTOM.apply(element, localTestBox));
        assertEquals(resultFullBox, Dock.FULL.apply(element, localTestBox));
        assertEquals(resultNoneBox, Dock.NONE.apply(element, localTestBox));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWithInvalidAlignmentLeft()
    {
        when(element.getAlignment()).thenReturn(EnumSet.of(Alignment.RIGHT));
        assertEquals(resultLeftBox, Dock.LEFT.apply(element, localTestBox));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWithInvalidAlignmentTop()
    {
        when(element.getAlignment()).thenReturn(EnumSet.of(Alignment.BOTTOM));
        assertEquals(resultLeftBox, Dock.TOP.apply(element, localTestBox));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWithInvalidAlignmentRight()
    {
        when(element.getAlignment()).thenReturn(EnumSet.of(Alignment.LEFT));
        assertEquals(resultLeftBox, Dock.RIGHT.apply(element, localTestBox));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWithInvalidAlignmentBottom()
    {
        when(element.getAlignment()).thenReturn(EnumSet.of(Alignment.TOP));
        assertEquals(resultLeftBox, Dock.BOTTOM.apply(element, localTestBox));
    }
}