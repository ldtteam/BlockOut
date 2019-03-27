package com.ldtteam.blockout.loader.core;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.util.Constants;

import java.util.Optional;

public interface IUIElementMetaData
{

    /**
     * Returns the type that the corresponding {@link IUIElementData} contains.
     *
     * @return The type of control that the {@link IUIElementData} contains.
     */
    String getType();

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

    /**
     * Indicates if this control has children, that can be found under a compound with the id {@link Constants.Controls.General#CONST_CHILDREN}
     *
     * @return True when this control has children, false when not.
     */
    boolean hasChildren();
}
