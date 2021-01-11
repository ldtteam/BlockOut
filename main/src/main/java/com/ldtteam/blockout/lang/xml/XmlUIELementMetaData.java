package com.ldtteam.blockout.lang.xml;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.util.Constants;
import org.w3c.dom.Node;

import java.util.Optional;

public class XmlUIELementMetaData implements IUIElementMetaData
{
    final Node xmlNode;
    final IUIElementHost parent;

    public XmlUIELementMetaData(final Node xmlNode, final IUIElementHost parent) {
        this.xmlNode = xmlNode;
        this.parent = parent;
    }

    /**
     * Returns the type that the corresponding {@link IUIElementData} contains.
     *
     * @return The type of control that the {@link IUIElementData} contains.
     */
    @Override
    public String getType()
    {
        return xmlNode.getAttributes().getNamedItem(Constants.Controls.General.CONST_TYPE).getNodeValue();
    }

    /**
     * Returns the id of control that the corresponding {@link IUIElementData} contains.
     *
     * @return the id of control that the corresponding {@link IUIElementData} contains.
     */
    @Override
    public String getId()
    {
        return xmlNode.getAttributes().getNamedItem(Constants.Controls.General.CONST_ID).getNodeValue();
    }

    /**
     * The parent of the control (if existing).
     *
     * @return The parent.
     */
    @Override
    public Optional<IUIElementHost> getParent()
    {
        return Optional.ofNullable(parent);
    }

    /**
     * Indicates if this control has children, that can be found under a compound with the id {@link Constants.Controls.General#CONST_CHILDREN}
     *
     * @return True when this control has children, false when not.
     */
    @Override
    public boolean hasChildren()
    {
        return xmlNode.hasChildNodes();
    }
}
