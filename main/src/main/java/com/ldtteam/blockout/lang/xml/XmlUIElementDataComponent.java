package com.ldtteam.blockout.lang.xml;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.util.stream.CollectorHelper;
import com.ldtteam.blockout.lang.xml.util.XmlStreamSupport;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlUIElementDataComponent implements IUIElementDataComponent
{

    private final Node node;
    private final boolean excludeNodeNamedPrefixedChildren;

    public XmlUIElementDataComponent(final Node node, final boolean excludeNodeNamedPrefixedChildren) {this.node = node;
        this.excludeNodeNamedPrefixedChildren = excludeNodeNamedPrefixedChildren;
    }

    /**
     * Returns the string representation of the component.
     *
     * @return The string.
     */
    @Override
    public String getAsString()
    {
        return node.getNodeValue();
    }

    /**
     * Returns the boolean representation of the component.
     *
     * @return The boolean.
     */
    @Override
    public Boolean getAsBoolean()
    {
        return Boolean.parseBoolean(node.getNodeValue());
    }

    /**
     * Sets the boolean on the current IUIElementData.
     *
     * @param bool The boolean to set.
     * @throws IllegalArgumentException when current component is not a Boolean.
     */
    @Override
    public void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException
    {
        node.setNodeValue(bool.toString());
    }

    /**
     * Returns the integer representation of the component.
     *
     * @return The integer.
     */
    @Override
    public Integer getAsInteger()
    {
        return Integer.decode(node.getNodeValue());
    }

    /**
     * Sets the integer on the current IUIElementData.
     *
     * @param integer The integer to set.
     * @throws IllegalArgumentException when current component is not a Integer.
     */
    @Override
    public void setInteger(@NotNull final Integer integer) throws IllegalArgumentException
    {
        node.setNodeValue(integer.toString());
    }

    /**
     * Returns the double representation of the component.
     *
     * @return The double.
     */
    @Override
    public Double getAsDouble()
    {
        return Double.parseDouble(node.getNodeValue());
    }

    /**
     * Sets the double on the current IUIElementData.
     *
     * @param d The double to set.
     * @throws IllegalArgumentException when current component is not a Double.
     */
    @Override
    public void setDouble(@NotNull final Double d) throws IllegalArgumentException
    {
        node.setNodeValue(d.toString());
    }

    /**
     * Returns the float representation of the component.
     *
     * @return The float
     */
    @Override
    public Float getAsFloat()
    {
        return Float.parseFloat(node.getNodeValue());
    }

    /**
     * Sets the float on the current IUIElementData.
     *
     * @param f The float to set.
     * @throws IllegalArgumentException when current component is not a Float.
     */
    @Override
    public void setFloat(@NotNull final Float f) throws IllegalArgumentException
    {
        node.setNodeValue(f.toString());
    }

    /**
     * Returns the list representation of the component.
     *
     * @return The list.
     */
    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return XmlStreamSupport.streamNodeList(node.getChildNodes())
          .filter(childNode -> !excludeNodeNamedPrefixedChildren || !childNode.getNodeName().startsWith(node.getPrefix()))
          .map(childNode -> new XmlUIElementDataComponent(childNode, false))
          .collect(Collectors.toList());
    }

    /**
     * Returns the map based representation of this component.
     *
     * @return The map based representation of this component.
     */
    @Override
    public Map<String, IUIElementDataComponent> getAsMap()
    {
        return XmlStreamSupport.streamNodeList(node.getChildNodes())
                 .filter(childNode -> !excludeNodeNamedPrefixedChildren || !childNode.getNodeName().startsWith(node.getPrefix()))
          .map(childNode -> new Tuple<String, IUIElementDataComponent>(childNode.getNodeName(), new XmlUIElementDataComponent(childNode, false)))
          .collect(CollectorHelper.tupleToMapCollector());
    }

    /**
     * Sets the map based representation of this component.
     *
     * @param map The map to set.
     * @throws IllegalArgumentException when current component is not complex.
     */
    @Override
    public void setMap(@NotNull final Map<String, ? extends IUIElementDataComponent> map) throws IllegalArgumentException
    {
        final List<Node> nodesToRemove = XmlStreamSupport.streamNodeList(node.getChildNodes()).collect(Collectors.toList());
        nodesToRemove.forEach(node::removeChild);

        map.keySet().forEach(nodeName -> {
            final IUIElementDataComponent component = map.get(nodeName);
            if (!(component instanceof XmlUIElementDataComponent))
                return;

            final XmlUIElementDataComponent xmlUIElementDataComponent = (XmlUIElementDataComponent) component;
            final Node childNode = xmlUIElementDataComponent.node;

            node.appendChild(childNode);
            node.getOwnerDocument().renameNode(childNode, null, nodeName);
        });
    }

    /**
     * Turns this {@link IUIElementDataComponent} into a fullblown {@link IUIElementData} by parsing the {@link IUIElementMetaData}
     *
     * @param parent The parent control of the target.
     * @return The {@link IUIElementData}.
     */
    @Override
    public IUIElementData<?> toIUIElementData(@Nullable final IUIElementHost parent)
    {
        return new XmlUIElementData(node, parent);
    }

    /**
     * Sets the string on the current IUIElementData.
     *
     * @param string The string to set.
     * @throws IllegalArgumentException when current component is not a String.
     */
    @Override
    public void setString(@NotNull final String string) throws IllegalArgumentException
    {
        node.setNodeValue(string);
    }

    /**
     * Returns the type of data stored in this data component.
     *
     * @return The type of component data.
     */
    @Override
    public ComponentType getType()
    {
        if (node.getNodeValue() != null)
        {
            final String nodeValue = node.getNodeValue();
            if (nodeValue.equalsIgnoreCase("false") || nodeValue.equalsIgnoreCase("true"))
                return ComponentType.BOOL;

            try {
                Double.parseDouble(nodeValue);
                return ComponentType.NUMBER;
            }
            catch (NumberFormatException e)
            {
                //Noop
            }

            try {
                Float.parseFloat(nodeValue);
                return ComponentType.NUMBER;
            }
            catch (NumberFormatException e)
            {
                //Noop
            }

            try {
                Integer.parseInt(nodeValue);
                return ComponentType.NUMBER;
            }
            catch (NumberFormatException e)
            {
                //Noop
            }

            return ComponentType.STRING;
        }

        return ComponentType.UNKNOWN;
    }

    @Override
    public boolean isList()
    {
        return getType() == ComponentType.UNKNOWN;
    }

    @Override
    public boolean isComplex()
    {
        return isList();
    }

    /**
     * Sets the list on the current IUIElementData.
     *
     * @param list The list to set.
     * @throws IllegalArgumentException when current component is not a List.
     */
    @Override
    public void setList(@NotNull final List<? extends IUIElementDataComponent> list) throws IllegalArgumentException
    {
        final List<Node> nodesToRemove = XmlStreamSupport.streamNodeList(node.getChildNodes()).collect(Collectors.toList());
        nodesToRemove.forEach(node::removeChild);

        list.forEach(component -> {
            if (!(component instanceof XmlUIElementDataComponent))
                return;

            final XmlUIElementDataComponent xmlUIElementDataComponent = (XmlUIElementDataComponent) component;
            final Node childNode = xmlUIElementDataComponent.node;

            node.appendChild(childNode);
        });
    }
}
