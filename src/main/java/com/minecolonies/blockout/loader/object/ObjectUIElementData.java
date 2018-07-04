package com.minecolonies.blockout.loader.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectUIElementData implements IUIElementData, Serializable
{
    @NotNull
    private final           String                    type;
    @NotNull
    private final           List<ObjectUIElementData> children;
    @NotNull
    private final           Map<String, Serializable> attributes;
    @Nullable
    private final transient IUIElementHost            parent;

    public ObjectUIElementData(
      @NotNull final ObjectUIElementData copyFrom,
      @Nullable final IUIElementHost parent
    )
    {
        this(copyFrom.getType().toString(), Lists.newArrayList(copyFrom.children), Maps.newHashMap(copyFrom.attributes), parent);
    }

    public ObjectUIElementData(
      @NotNull final String type,
      @NotNull final List<ObjectUIElementData> children,
      @NotNull final Map<String, Serializable> attributes,
      @Nullable final IUIElementHost parent)
    {
        this.type = type;
        this.children = children;
        this.attributes = attributes;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(type);
    }

    @NotNull
    @Override
    public IDependencyObject<ResourceLocation> getBoundStyleId()
    {
        return DependencyObjectHelper.createFromValue(new ResourceLocation(getAttribute("style", String.class).orElseGet(Constants.Styles.CONST_DEFAULT::toString)));
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Nullable
    @Override
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
    {
        return children.stream()
                 .map(objectUIElementData -> new ObjectUIElementData(objectUIElementData, parentOfChildren))
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
        return !children.isEmpty();
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        return getAttribute(name, Integer.class).orElse(def);
    }

    @Override
    public IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name, final int def)
    {
        return DependencyObjectHelper.createFromValue(getIntegerAttribute(name, def));
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        return getAttribute(name, Float.class).orElse(def);
    }

    @Override
    public IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name, final float def)
    {
        return DependencyObjectHelper.createFromValue(getFloatAttribute(name, def));
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        return getAttribute(name, Double.class).orElse(def);
    }

    @Override
    public IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name, final double def)
    {
        return DependencyObjectHelper.createFromValue(getDoubleAttribute(name, def));
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return getAttribute(name, Boolean.class).orElse(def);
    }

    @Override
    public IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return DependencyObjectHelper.createFromValue(getBooleanAttribute(name, def));
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, final Class<T> clazz, final T def)
    {
        return DependencyObjectHelper.createFromValue(getEnumAttribute(name, clazz, def));
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz)
    {
        return DependencyObjectHelper.createFromValue(getEnumSetAttributes(name, clazz));
    }

    @Nullable
    @Override
    public String getStringAttribute(@NotNull final String name, @Nullable final String def)
    {
        return getAttribute(name, String.class).orElse(def);
    }

    @Override
    public IDependencyObject<String> getBoundStringAttribute(@NotNull final String name, final String def)
    {
        return DependencyObjectHelper.createFromValue(getStringAttribute(name, def));
    }

    @Override
    public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
      @NotNull final String name, final AxisDistance def)
    {
        return DependencyObjectHelper.createFromValue(getAxisDistanceAttribute(name, def));
    }

    @Override
    public IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(
      @NotNull final String name, final EnumSet<Alignment> def)
    {
        return DependencyObjectHelper.createFromValue(getAlignmentAttribute(name, def));
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
        return DependencyObjectHelper.createFromValue(getControlDirectionAttribute(name, def));
    }

    @Override
    public IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull final String name, final Vector2d def)
    {
        return DependencyObjectHelper.createFromValue(getVector2dAttribute(name, def));
    }

    @Override
    public IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull final String name, final ResourceLocation def)
    {
        return DependencyObjectHelper.createFromValue(getResourceLocationAttribute(name, def));
    }

    @Override
    public IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(
      @NotNull final String name, final BoundingBox def)
    {
        return DependencyObjectHelper.createFromValue(getBoundingBoxAttribute(name, def));
    }

    @Override
    public IDependencyObject<Object> getBoundDatacontext()
    {
        return DependencyObjectHelper.createFromValue(new Object());
    }

    private <T extends Serializable> Optional<T> getAttribute(@NotNull final String name, @NotNull final Class<T> cls)
    {
        if (attributes.get(name) != null)
        {
            Serializable val = attributes.get(name);

            if (cls.isInstance(val))
            {
                return Optional.of(cls.cast(val));
            }
        }

        return Optional.empty();
    }
}
