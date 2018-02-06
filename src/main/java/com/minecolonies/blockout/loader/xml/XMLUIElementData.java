package com.minecolonies.blockout.loader.xml;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Localization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Special parameters for the panes.
 */
public class XMLUIElementData implements IUIElementData
{

    private final Node           node;
    private       IUIElementHost parentView;

    /**
     * Instantiates the pane parameters.
     *
     * @param n the node.
     */
    public XMLUIElementData(final Node n)
    {
        node = n;
    }

    @Override
    public String getType()
    {
        return node.getNodeName();
    }

    @Override
    public IUIElementHost getParentView()
    {
        return parentView;
    }

    @Override
    public void setParentView(final IUIElementHost parent)
    {
        parentView = parent;
    }

    @Override
    public double getParentWidth()
    {
        return parentView != null ? (int) parentView.getAbsoluteInternalBoundingBox().getSize().getX() : 0;
    }

    @Override
    public double getParentHeight()
    {
        return parentView != null ? (int) parentView.getAbsoluteInternalBoundingBox().getSize().getY() : 0;
    }

    @Override
    @Nullable
    public List<IUIElementData> getChildren()
    {
        List<IUIElementData> list = null;

        Node child = node.getFirstChild();
        while (child != null)
        {
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if (list == null)
                {
                    list = new ArrayList<>();
                }

                list.add(new XMLUIElementData(child));
            }
            child = child.getNextSibling();
        }

        return list;
    }

    @Override
    @NotNull
    public String getText()
    {
        return node.getTextContent().trim();
    }

    @Override
    @Nullable
    public String getLocalizedText()
    {
        return Localization.localize(node.getTextContent().trim());
    }



    /**
     * Get the string attribute.
     *
     * @param name the name to search.
     * @return the attribute.
     */
    @Override
    public String getStringAttribute(final String name)
    {
        return getStringAttribute(name, "");
    }

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @Override
    public String getStringAttribute(final String name, final String def)
    {
        final Node attr = getAttribute(name);
        return (attr != null) ? attr.getNodeValue() : def;
    }

    private Node getAttribute(final String name)
    {
        return node.getAttributes().getNamedItem(name);
    }

    /**
     * Get the localized string attribute from the name.
     *
     * @param name the name.
     * @return the string attribute.
     */
    @Override
    @Nullable
    public String getLocalizedStringAttribute(final String name)
    {
        return getLocalizedStringAttribute(name, "");
    }

    /**
     * Get the localized String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the string.
     */
    @Override
    @Nullable
    public String getLocalizedStringAttribute(final String name, final String def)
    {
        return Localization.localize(getStringAttribute(name, def));
    }

    /**
     * Get the integer attribute from the name.
     *
     * @param name the name.
     * @return the integer.
     */
    @Override
    public int getIntegerAttribute(final String name)
    {
        return getIntegerAttribute(name, 0);
    }

    /**
     * Get the integer attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the int.
     */
    @Override
    public int getIntegerAttribute(final String name, final int def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Integer.parseInt(attr);
        }
        return def;
    }

    /**
     * Get the float attribute from name.
     *
     * @param name the name.
     * @return the float.
     */
    @Override
    public float getFloatAttribute(final String name)
    {
        return getFloatAttribute(name, 0);
    }

    /**
     * Get the float attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the float.
     */
    @Override
    public float getFloatAttribute(final String name, final float def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Float.parseFloat(attr);
        }
        return def;
    }

    /**
     * Get the double attribute from name.
     *
     * @param name the name.
     * @return the double.
     */
    @Override
    public double getDoubleAttribute(final String name)
    {
        return getDoubleAttribute(name, 0);
    }

    /**
     * Get the double attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the double.
     */
    @Override
    public double getDoubleAttribute(final String name, final double def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Double.parseDouble(attr);
        }

        return def;
    }

    /**
     * Get the boolean attribute from name.
     *
     * @param name the name.
     * @return the boolean.
     */
    @Override
    public boolean getBooleanAttribute(final String name)
    {
        return getBooleanAttribute(name, false);
    }

    /**
     * Get the boolean attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the boolean.
     */
    @Override
    public boolean getBooleanAttribute(final String name, final boolean def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Boolean.parseBoolean(attr);
        }
        return def;
    }
}
