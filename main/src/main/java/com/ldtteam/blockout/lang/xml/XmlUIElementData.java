package com.ldtteam.blockout.lang.xml;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.lang.xml.util.XmlStreamSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.Optional;

public class XmlUIElementData implements IUIElementData<XmlUIElementDataComponent>
{
    private final Node node;
    private final IUIElementMetaData metaData;

    public XmlUIElementData(final Node node, final IUIElementHost parent)
    {
        this.node = node;
        this.metaData = new XmlUIELementMetaData(node, parent);
    }

    /**
     * Returns the metadata of a element.
     *
     * @return The metadata.
     */
    @NotNull
    @Override
    public IUIElementMetaData getMetaData()
    {
        return metaData;
    }

    /**
     * Returns a component with the given name.
     *
     * @param name The name of the component.
     * @return The optional containing the component if known.
     */
    @Nullable
    @Override
    public Optional<XmlUIElementDataComponent> getComponentWithName(@NotNull final String name, final boolean isPrimary)
    {
        if (node.getAttributes().getNamedItem(name) != null)
            return Optional.of(new XmlUIElementDataComponent(node, false));

        final Optional<Node> possibleChildNode = XmlStreamSupport.streamNodeList(node.getChildNodes()).filter(childNode -> childNode.getNodeName().equalsIgnoreCase(node.getNodeName() + "." + name)).findAny();
        if (possibleChildNode.isPresent())
            return Optional.of(new XmlUIElementDataComponent(possibleChildNode.get(), false));

        if (isPrimary)
            return Optional.of(new XmlUIElementDataComponent(node, true));

        return Optional.empty();
    }

    /**
     * Writes the current element into a data component.
     *
     * @param toWriteInto The component to write the data into.
     * @return The data component containing this element datas data.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto)
    {
        if (toWriteInto instanceof XmlUIElementDataComponent)
        {
            return (D) toDataComponent();
        }

        return toWriteInto;
    }

    private XmlUIElementDataComponent toDataComponent()
    {
        return new XmlUIElementDataComponent(node, false);
    }
}
