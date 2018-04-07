package com.minecolonies.blockout.loader.xml;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
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
    private final IUIElementHost parent;

    /**
     * Instantiates the pane parameters.
     *
     * @param n the node.
     * @param parent
     */
    public XMLUIElementData(final Node n, final IUIElementHost parent)
    {
        node = n;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(Constants.MOD_ID, node.getNodeName());
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Override
    @Nullable
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
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

                list.add(new XMLUIElementData(child, parentOfChildren));
            }
            child = child.getNextSibling();
        }

        return list;
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
