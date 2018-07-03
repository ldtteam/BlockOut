package com.minecolonies.blockout.loader.xml;

import com.google.common.base.Functions;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.property.Property;
import com.minecolonies.blockout.binding.property.PropertyCreationHelper;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.AxisDistanceBuilder;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.xml.XMLStreamSupport;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

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

    @NotNull
    @Override
    public IDependencyObject<ResourceLocation> getBoundStyleId()
    {
        return bindOrReturnBoundTo(
          "style",
          ResourceLocation::new,
          PropertyCreationHelper.createFromNonOptional(
            Optional.of(c -> {
                if (getParentView() != null)
                {
                    return getParentView().getStyleId();
                }

                return new ResourceLocation(Constants.MOD_ID, Constants.Styles.CONST_MINECRAFT);
            }),
            Optional.empty()
          ),
          new ResourceLocation(Constants.MOD_ID, Constants.Styles.CONST_MINECRAFT)
        );
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
        return XMLStreamSupport
                 .streamChildren(node)
                 .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                 .map(n -> new XMLUIElementData(n, parentOfChildren))
                 .collect(Collectors.toList());
    }

    /**
     * Checks if this control has possible children.
     *
     * @return True when this control has children, false when not.
     */
    @Override
    public boolean hasChildren()
    {
        return node.hasChildNodes() && XMLStreamSupport
                                         .streamChildren(node)
                                         .anyMatch(n -> n.getNodeType() == Node.ELEMENT_NODE);
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

    @Override
    public IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name, final int def)
    {
        return bindOrReturnStatic(name, Integer::new, def);
    }

    @Override
    public IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name, final float def)
    {
        return bindOrReturnStatic(name, Float::new, def);
    }

    @Override
    public IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name, final double def)
    {
        return bindOrReturnStatic(name, Double::new, def);
    }

    @Override
    public IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return bindOrReturnStatic(name, Boolean::new, def);
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, final Class<T> clazz, final T def)
    {
        return bindOrReturnStatic(name, s -> Enum.valueOf(clazz, s), def);
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

    @Override
    public <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz)
    {
        return bindOrReturnStatic(name, s -> {
            final String[] splitted = s.split(",");

            final EnumSet<T> result = EnumSet.noneOf(clazz);
            for (String e : splitted)
            {
                result.add(Enum.valueOf(clazz, e.trim()));
            }

            return result;
        }, EnumSet.noneOf(clazz));
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

    @Override
    public IDependencyObject<String> getBoundStringAttribute(@NotNull final String name, final String def)
    {
        return bindOrReturnStatic(name, Functions.identity(), def);
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

    @Override
    public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
      @NotNull final String name, final AxisDistance def)
    {
        return bindOrReturnStatic(name, s -> {
            final AxisDistanceBuilder builder = new AxisDistanceBuilder();
            builder.readFromString(getParentView().getElementSize(), s);

            return builder.create();
        }, def);
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

    @Override
    public IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(
      @NotNull final String name, final EnumSet<Alignment> def)
    {
        return bindOrReturnStatic(name, Alignment::fromString, def);
    }

    /**
     * Returns a bound ControlDirection attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<ControlDirection> getBoundControlDirectionAttribute(
      @NotNull final String name, final ControlDirection def)
    {
        return bindOrReturnStatic(name, ControlDirection::fromString, def);
    }

    @Override
    public IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull final String name, final Vector2d def)
    {
        return bindOrReturnStatic(name, s -> {
            final String[] components = s.split(",");
            if (components.length == 1)
            {
                return new Vector2d(Double.parseDouble(components[0]));
            }
            else if (components.length == 2)
            {
                return new Vector2d(Double.parseDouble(components[0]), Double.parseDouble(components[1]));
            }
            else
            {
                return def;
            }
        }, def);
    }

    @Override
    public IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull final String name, final ResourceLocation def)
    {
        return bindOrReturnStatic(name, ResourceLocation::new, def);
    }

    @Override
    public IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(
      @NotNull final String name, final BoundingBox def)
    {
        return bindOrReturnStatic(name, BoundingBox::fromString, def);
    }

    @Override
    public IDependencyObject<Object> getBoundDatacontext()
    {
        return bindOrReturnStatic("datacontext", e -> new Object(), new Object());
    }

    private <T> IDependencyObject<T> bindOrReturnStatic(
      @NotNull final String name, @NotNull final
    Function<String, T> extract, T defaultValue)
    {
        @Nullable final String attribute = getStringAttribute(name, null);
        if (attribute == null)
        {
            return DependencyObjectHelper.createFromValue(defaultValue);
        }

        final String elementContents = attribute;

        final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(elementContents);
        if (singleNameMatcher.matches())
        {
            final String fieldName = singleNameMatcher.group("singleName");
            final Property<T> fieldProperty = PropertyCreationHelper.createFromName(Optional.of(fieldName));
            return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
        }

        final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(elementContents);
        if (multiNameMatcher.matches())
        {
            final String getterName = multiNameMatcher.group("getterName");
            final String setterName = multiNameMatcher.group("setterName");
            final Property<T> getterSetterProperty = PropertyCreationHelper.createFromName(Optional.of(getterName), Optional.of(setterName));
            return DependencyObjectHelper.createFromProperty(getterSetterProperty, defaultValue);
        }

        return DependencyObjectHelper.createFromValue(extract.apply(attribute));
    }

    private <T> IDependencyObject<T> bindOrReturnBoundTo(
      @NotNull final String name, @NotNull final
    Function<String, T> extract, Property<T> boundTo, T defaultValue)
    {
        @Nullable final String attribute = getStringAttribute(name, null);
        if (attribute == null)
        {
            return DependencyObjectHelper.createFromProperty(boundTo, defaultValue);
        }

        final String elementContents = attribute;

        final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(elementContents);
        if (singleNameMatcher.matches())
        {
            final String fieldName = singleNameMatcher.group("singleName");
            final Property<T> fieldProperty = PropertyCreationHelper.createFromName(Optional.of(fieldName));
            return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
        }

        final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(elementContents);
        if (multiNameMatcher.matches())
        {
            final String getterName = multiNameMatcher.group("getterName");
            final String setterName = multiNameMatcher.group("setterName");
            final Property<T> getterSetterProperty = PropertyCreationHelper.createFromName(Optional.of(getterName), Optional.of(setterName));
            return DependencyObjectHelper.createFromProperty(getterSetterProperty, defaultValue);
        }

        return DependencyObjectHelper.createFromValue(extract.apply(attribute));
    }
}
