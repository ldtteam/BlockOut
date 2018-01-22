package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.util.mouse.MouseButton;

public class ClickManager extends AbstractInputManager<IClickAcceptingUIElement> implements IClickManager
{

    protected ClickManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(localX, localY, (u, x, y) -> u.canAcceptMouseInput(x, y, button), (u, x, y) -> u.onMouseClickBegin(x, y, button), IClickAcceptingUIElement.class);
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(localX, localY, (u, x, y) -> u.canAcceptMouseInput(x, y, button), (u, x, y) -> u.onMouseClickEnd(x, y, button), IClickAcceptingUIElement.class);
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        attemptInputInteraction(localX,
          localY,
          (u, x, y) -> u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickMove(x, y, button, timeElapsed),
          IClickAcceptingUIElement.class);
    }
}
