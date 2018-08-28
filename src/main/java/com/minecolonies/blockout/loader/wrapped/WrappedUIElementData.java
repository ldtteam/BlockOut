package com.minecolonies.blockout.loader.wrapped;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class WrappedUIElementData implements IUIElementData
{

    private final IUIElementData wrapped;

    public WrappedUIElementData(final IUIElementData wrapped) {this.wrapped = wrapped;}

    /**
     * Method to get the type name of the Pane that is to be constructed from these {@link IUIElementData}
     *
     * @return The pane type.
     */
    @Override
    public ResourceLocation getType()
    {
        return wrapped.getType();
    }

    /**
     * Method to get the style id, bound, for this control.
     *
     * @return The bound style id.
     */
    @NotNull
    @Override
    public IDependencyObject<ResourceLocation> getBoundStyleId()
    {
        return wrapped.getBoundStyleId();
    }

    /**
     * Method used to get the parent {@link IUIElementHost} if it exists.
     */
    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return wrapped.getParentView();
    }

    /**
     * Method used to get a list of {@link IUIElementData} of children of the Pane that is to be constructed.
     *
     * @return A list with {@link IUIElementData} to construct the children.
     */
    @Nullable
    @Override
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
    {
        return wrapped.getChildren(parentOfChildren);
    }

    /**
     * Checks if this control has possible children.
     *
     * @return True when this control has children, false when not.
     */
    @Override
    public boolean hasChildren()
    {
        return wrapped.hasChildren();
    }

    /**
     * Get the integer attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the int.
     */
    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        return wrapped.getIntegerAttribute(name, def);
    }

    /**
     * Returns a bound integer attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name, final int def)
    {
        return wrapped.getBoundIntegerAttribute(name, def);
    }

    /**
     * Get the float attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the float.
     */
    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        return wrapped.getFloatAttribute(name, def);
    }

    /**
     * Returns a bound float attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name, final float def)
    {
        return wrapped.getBoundFloatAttribute(name, def);
    }

    /**
     * Get the double attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the double.
     */
    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        return wrapped.getDoubleAttribute(name, def);
    }

    /**
     * Returns a bound double attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name, final double def)
    {
        return wrapped.getBoundDoubleAttribute(name, def);
    }

    /**
     * Get the boolean attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the boolean.
     */
    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return wrapped.getBooleanAttribute(name, def);
    }

    /**
     * Returns a bound boolean attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return wrapped.getBoundBooleanAttribute(name, def);
    }

    /**
     * Returns a bound enum attribute. If not found nor bound a static bound to the given default is returned.
     *
     * @param name  The name.
     * @param clazz The class of the enum
     * @param def   The default value if not defined.
     * @return The bound enum attribute.
     */
    @Override
    public <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, final Class<T> clazz, final T def)
    {
        return wrapped.getBoundEnumAttribute(name, clazz, def);
    }

    /**
     * Returns a bound enum set attribute. If not found nor bound a static bound to an empty enumset is returned.
     *
     * @param name  The name.
     * @param clazz The class of the enum
     * @return The bound enum attribute.
     */
    @Override
    public <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz)
    {
        return wrapped.getBoundEnumSetAttribute(name, clazz);
    }

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @NotNull
    @Override
    public String getStringAttribute(@NotNull final String name, @NotNull final String def)
    {
        return wrapped.getStringAttribute(name, def);
    }

    /**
     * Returns a bound string attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<String> getBoundStringAttribute(@NotNull final String name, final String def)
    {
        return wrapped.getBoundStringAttribute(name, def);
    }

    /**
     * Returns a bound axisDistance attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
      @NotNull final String name, final AxisDistance def)
    {
        return wrapped.getBoundAxisDistanceAttribute(name, def);
    }

    /**
     * Returns a bound alignment attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(
      @NotNull final String name, final EnumSet<Alignment> def)
    {
        return wrapped.getBoundAlignmentAttribute(name, def);
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
        return wrapped.getBoundControlDirectionAttribute(name, def);
    }

    /**
     * Returns a bound vector2d attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull final String name, final Vector2d def)
    {
        return wrapped.getBoundVector2dAttribute(name, def);
    }

    /**
     * Returns a bound resourceLocation attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull final String name, final ResourceLocation def)
    {
        return wrapped.getBoundResourceLocationAttribute(name, def);
    }

    /**
     * Returns a bound boundingBox attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(
      @NotNull final String name, final BoundingBox def)
    {
        return wrapped.getBoundBoundingBoxAttribute(name, def);
    }

    /**
     * Returns the bound datacontext for the ui element this data belongs to.
     * Only returns a meaningfull value on the server side.
     * On the client side this value will always be bound to a plain Object.
     *
     * @return The bound datacontext.
     */
    @Override
    public IDependencyObject<Object> getBoundDataContext()
    {
        return wrapped.getBoundDataContext();
    }

    /**
     * Returns the nbt stored in the attribute with the given name.
     *
     * @param name The name to lookup the nbt for.
     * @return The NBT contained in the attribute with the given name.
     */
    @Override
    public <T extends NBTBase> T getNBTAttribute(@NotNull final String name)
    {
        return wrapped.getNBTAttribute(name);
    }

    /**
     * Returns the bound nbt stored in the attribute with the given name.
     *
     * @param name The name to lookup the bound nbt for.
     * @param def  The default used incase of binding failure.
     * @return The bound nbt.
     */
    @Override
    public <T extends NBTBase> IDependencyObject<T> getBoundNBTAttribute(@NotNull final String name, @NotNull final T def)
    {
        return wrapped.getBoundNBTAttribute(name, def);
    }
}
