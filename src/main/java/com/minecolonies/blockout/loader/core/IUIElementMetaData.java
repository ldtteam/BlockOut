package com.minecolonies.blockout.loader.core;

import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraft.util.ResourceLocation;

public interface IUIElementMetaData
{

    /**
     * Returns the type that the corresponding {@link IUIElementData} contains.
     *
     * @return The type of control that the {@link IUIElementData} contains.
     */
    ResourceLocation getType();

    /**
     * The parent of the control (if existing).
     *
     * @return The parent.
     */
    IUIElementHost getParent();

    /**
     * The parent data of the control (if existing).
     *
     * @return The parent data of the control
     */
    IUIElementData getParentData();
}
