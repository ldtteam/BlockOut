package com.ldtteam.blockout.loader.core;

import com.ldtteam.blockout.element.IUIElementHost;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IUIElementMetaData
{

    /**
     * Returns the type that the corresponding {@link IUIElementData} contains.
     *
     * @return The type of control that the {@link IUIElementData} contains.
     */
    ResourceLocation getType();

    /**
     * Returns the id of control that the corresponding {@link IUIElementData} contains.
     *
     * @return the id of control that the corresponding {@link IUIElementData} contains.
     */
    String getId();

    /**
     * The parent of the control (if existing).
     *
     * @return The parent.
     */
    Optional<IUIElementHost> getParent();
}
