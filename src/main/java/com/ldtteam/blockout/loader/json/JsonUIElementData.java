package com.ldtteam.blockout.loader.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.core.element.values.Alignment;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.core.element.values.AxisDistanceBuilder;
import com.ldtteam.blockout.core.element.values.ControlDirection;
import com.ldtteam.blockout.loader.IUIElementData;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.json.JSONToNBT;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.parsing.ParsingUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonUIElementData implements IUIElementData
{
    private final JsonObject     object;
    private final IUIElementHost parent;

    public JsonUIElementData(final JsonObject object, final IUIElementHost parent) {this.object = object;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(object.get("type").getAsString());
    }

    @NotNull
    @Override
    public IDependencyObject<ResourceLocation> getBoundStyleId()
    {
        if (!object.has("style"))
        {
            return DependencyObjectHelper.createFromProperty(PropertyCreationHelper.createFromNonOptional(
              Optional.of(c -> {
                  if (getParentView() != null)
                  {
                      return getParentView().getStyleId();
                  }

                  return Constants.Styles.CONST_DEFAULT;
              }),
              Optional.empty()
              ),
              Constants.Styles.CONST_DEFAULT);
        }

        return bindOrReturnBoundTo(object.get("style"), (JsonElement::isJsonPrimitive), (JsonElement element) -> {
              final String attribute = element.getAsString();
              return new ResourceLocation(attribute);
          },
          PropertyCreationHelper.createFromNonOptional(
            Optional.of(c -> {
                if (getParentView() != null)
                {
                    return getParentView().getStyleId();
                }

                return Constants.Styles.CONST_DEFAULT;
            }),
            Optional.empty()
          ),
          Constants.Styles.CONST_DEFAULT
        );
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
        if (!object.has("children") || !object.get("children").isJsonArray())
        {
            return ImmutableList.of();
        }

        return StreamSupport.stream(object.get("children").getAsJsonArray().spliterator(), false)
                 .filter(JsonElement::isJsonObject)
                 .map(JsonElement::getAsJsonObject)
                 .map(childData -> new JsonUIElementData(childData, parentOfChildren))
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
        return object.has("children") && object.get("children").isJsonArray();
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsInt();
    }

    @Override
    public IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name, final int def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (e -> e.isJsonPrimitive() && ParsingUtils.isParseable(e.getAsString(), Integer::parseInt)), JsonElement::getAsInt, def);
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsFloat();
    }

    @Override
    public IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name, final float def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (e -> e.isJsonPrimitive() && ParsingUtils.isParseable(e.getAsString(), Float::parseFloat)), JsonElement::getAsFloat, def);
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsDouble();
    }

    @Override
    public IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name, final double def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (e -> e.isJsonPrimitive() && ParsingUtils.isParseable(e.getAsString(), Double::parseDouble)), JsonElement::getAsDouble, def);
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsBoolean();
    }

    @Override
    public IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name, final boolean def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (e -> e.isJsonPrimitive() && ParsingUtils.isParseable(e.getAsString(), Boolean::parseBoolean)), JsonElement::getAsBoolean, def);
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, final Class<T> clazz, final T def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get("name"), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            final String attr = element.getAsString();
            if (attr != null)
            {
                return Enum.valueOf(clazz, attr);
            }
            return def;
        }, def);
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz)
    {
        final EnumSet<T> def = EnumSet.noneOf(clazz);

        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            final String attr = element.getAsString();
            final String[] splitted = attr.split(",");

            final EnumSet<T> result = EnumSet.noneOf(clazz);
            for (String e : splitted)
            {
                result.add(Enum.valueOf(clazz, e.trim()));
            }

            return result;
        }, def);
    }

    @NotNull
    @Override
    public String getStringAttribute(@NotNull final String name, @NotNull final String def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsString();
    }

    @Override
    public IDependencyObject<String> getBoundStringAttribute(@NotNull final String name, final String def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, JsonElement::getAsString, def);
    }

    @Override
    public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
      @NotNull final String name, final AxisDistance def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            final String attribute = element.getAsString();

            final AxisDistanceBuilder builder = new AxisDistanceBuilder();
            builder.readFromString(getParentView().getElementSize(), attribute);

            return builder.create();
        }, def);
    }

    @Override
    public IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(
      @NotNull final String name, final EnumSet<Alignment> def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            final String attribute = element.getAsString();
            return Alignment.fromString(attribute);
        }, def);
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
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            final String attribute = element.getAsString();
            return ControlDirection.fromString(attribute);
        }, def);
    }

    @Override
    public IDependencyObject<Vector2d> getBoundVector2dAttribute(
      @NotNull final String name, final Vector2d def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), JsonElement::isJsonPrimitive, (JsonElement element) -> {
            @Nullable final String attribute = element.getAsString();
            if (attribute == null || attribute.trim().isEmpty())
            {
                return def;
            }

            final String[] components = attribute.split(",");
            if (components.length == 1)
            {
                return new Vector2d(Double.parseDouble(components[0].trim()));
            }
            else if (components.length == 2)
            {
                return new Vector2d(Double.parseDouble(components[0].trim()), Double.parseDouble(components[1].trim()));
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
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (JsonElement::isJsonPrimitive), (JsonElement element) -> {
            final String attribute = element.getAsString();
            return new ResourceLocation(attribute);
        }, def);
    }

    @Override
    public IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(
      @NotNull final String name, final BoundingBox def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(object.get(name), (JsonElement::isJsonPrimitive), (JsonElement element) -> {
            final String attribute = element.getAsString();
            return BoundingBox.fromString(attribute);
        }, def);
    }

    @Override
    public IDependencyObject<Object> getBoundDataContext()
    {
        if (!object.has("datacontext"))
        {
            if (getParentView() != null)
            {
                //Bind the childs datacontext if not specified directly to the parent.
                return bindOrReturnBoundTo(
                  new JsonPrimitive("?!?!Dummy!?!?"),
                  e -> false,
                  e -> new Object(),
                  PropertyCreationHelper.create(
                    Optional::of,
                    null
                  ),
                  new Object());
            }

            return DependencyObjectHelper.createFromValue(new Object());
        }

        return bindOrReturnStatic(object.get("datacontext"), e -> false, e -> new Object(), new Object());
    }

    @Override
    public IDependencyObject<Object> getBoundObject(@NotNull final String name, final Object def)
    {
        if (!object.has(name))
        {
            return DependencyObjectHelper.createFromValue(def);
        }

        return bindOrReturnStatic(
          object.get(name),
          e -> false,
          JsonElement::getAsString,
          def);
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
        final JsonElement element = object.get(name);
        try
        {
            return (T) JSONToNBT.fromJSON(element);
        }
        catch (ClassCastException ex)
        {
            Log.getLogger().warn("Failed to convert given JSON NBT String into requested type.");
            return null;
        }
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
        return bindOrReturnStatic(object.get(name), (e) -> true, element -> {
              try
              {
                  return (T) JSONToNBT.fromJSON(element);
              }
              catch (ClassCastException ex)
              {
                  Log.getLogger().warn("Failed to convert given JSON NBT String into requested type.");
                  return null;
              }
          },
          def);
    }

    private <T> IDependencyObject<T> bindOrReturnStatic(
      @NotNull final JsonElement element,
      @NotNull final Predicate<JsonElement> elementMatchesTypePredicate,
      @NotNull final Function<JsonElement, T> jsonExtractor,
      T defaultValue)
    {
        if (element.isJsonPrimitive())
        {
            final String stringContent = element.getAsString();
            final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(stringContent);
            final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(stringContent);
            String getterName = "";
            String setterName = "";
            if (singleNameMatcher.matches())
            {
                getterName = singleNameMatcher.group("singleName");
                setterName = getterName;
            }
            else if (multiNameMatcher.matches())
            {
                getterName = multiNameMatcher.group("getterName");
                setterName = multiNameMatcher.group("setterName");
            }

            if (!getterName.equals("") && !setterName.equals(""))
            {
                final Property<T> fieldProperty;
                if (getterName.equalsIgnoreCase("this") && getterName.equals(setterName))
                {
                    fieldProperty = PropertyCreationHelper.createFromNonOptional(
                      Optional.of((context) -> (T) context),
                      Optional.empty()
                    );
                }
                else
                {
                    fieldProperty = PropertyCreationHelper.createFromName(Optional.of(String.format("get%s", getterName)), Optional.of(String.format("set%s", setterName)));
                }

                return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
            }
        }

        if (elementMatchesTypePredicate.test(element))
        {
            return DependencyObjectHelper.createFromValue(jsonExtractor.apply(element));
        }

        return DependencyObjectHelper.createFromValue(defaultValue);
    }

    private <T> IDependencyObject<T> bindOrReturnBoundTo(
      @NotNull final JsonElement element,
      @NotNull final Predicate<JsonElement> elementMatchesTypePredicate,
      @NotNull final Function<JsonElement, T> jsonExtractor,
      @NotNull final Property<T> boundTo,
      @NotNull final T defaultValue)
    {
        if (element.isJsonPrimitive())
        {
            final String stringContent = element.getAsString();
            final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(stringContent);
            final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(stringContent);
            String getterName = "";
            String setterName = "";
            if (singleNameMatcher.matches())
            {
                getterName = singleNameMatcher.group("singleName");
                setterName = getterName;
            }
            else if (multiNameMatcher.matches())
            {
                getterName = multiNameMatcher.group("getterName");
                setterName = multiNameMatcher.group("setterName");
            }

            if (!getterName.equals("") && !setterName.equals(""))
            {
                final Property<T> fieldProperty;
                if (getterName.equalsIgnoreCase("this") && getterName.equals(setterName))
                {
                    fieldProperty = PropertyCreationHelper.createFromNonOptional(
                      Optional.of((context) -> (T) context),
                      Optional.empty()
                    );
                }
                else
                {
                    fieldProperty = PropertyCreationHelper.createFromName(Optional.of(String.format("get%s", getterName)), Optional.of(String.format("set%s", setterName)));
                }

                return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
            }
        }

        if (elementMatchesTypePredicate.test(element))
        {
            return DependencyObjectHelper.createFromValue(jsonExtractor.apply(element));
        }

        return DependencyObjectHelper.createFromProperty(boundTo, defaultValue);
    }
}
