package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.controls.Image;
import com.minecolonies.blockout.controls.ItemIcon;
import com.minecolonies.blockout.controls.Label;
import com.minecolonies.blockout.controls.button.ButtonImage;
import com.minecolonies.blockout.controls.button.ButtonVanilla;
import com.minecolonies.blockout.controls.text.Text;
import com.minecolonies.blockout.controls.text.TextFieldVanilla;
import com.minecolonies.blockout.core.Pane;
import com.minecolonies.blockout.views.*;
import com.minecolonies.blockout.views.scrolling.ScrollingGroup;
import com.minecolonies.blockout.views.scrolling.ScrollingList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ComponentConstructionController
{
    private static final Map<String, Constructor<? extends Pane>> paneConstructorMap = new HashMap<>();
    static
    {
        register("view", View.class);
        register("group", Group.class);
        register("scrollgroup", ScrollingGroup.class);
        register("list", ScrollingList.class);
        register("text", Text.class);
        register("button", ButtonVanilla.class);
        register("buttonimage", ButtonImage.class);
        register("label", Label.class);
        register("input", TextFieldVanilla.class);
        register("image", Image.class);
        register("box", Box.class);
        register("itemicon", ItemIcon.class);
        register("switch", SwitchView.class);
        register("dropdown", DropDownList.class);
        register("overlay", OverlayView.class);
    }
    /**
     * Method used to register a new pane constructor without a style.
     * @param name The pane type name.
     * @param paneClass The constructor that creates a pane with the given pane type name.
     */
    public static void register(final String name, final Class<? extends Pane> paneClass)
    {
        register(name, null, paneClass);
    }

    /**
     * Method used to register a new pane constructor with a given style.
     * @param name The pane type name.
     * @param style The style name.
     * @param paneClass The constructor that creates a pane with the given pane type name and style.
     */
    public static void register(final String name, final String style, final Class<? extends Pane> paneClass)
    {
        final String key = makeFactoryKey(name, style);

        if (paneConstructorMap.containsKey(key))
        {
            throw new IllegalArgumentException("Duplicate pane type '"
                                                 + name + "' of style '"
                                                 + style + "' when registering Pane class mapping for "
                                                 + paneClass.getName());
        }

        try
        {
            final Constructor<? extends Pane> constructor = paneClass.getDeclaredConstructor(IUIElementData.class);
            paneConstructorMap.put(key, constructor);
        }
        catch (final NoSuchMethodException exception)
        {
            throw new IllegalArgumentException("Missing (XMLNode) constructor for type '"
                                                 + name + "' when adding Pane class mapping for " + paneClass.getName(), exception);
        }
    }

    @NotNull
    private static String makeFactoryKey(@NotNull final String name, @Nullable final String style)
    {
        return name + ":" + (style != null ? style : "");
    }

    /**
     * Method used to get a {@link Constructor} to create a new pane using the given name and style.
     * @param name The name of the pane type to get the constructor for.
     * @param style The name of the style to use.
     * @return
     */
    @Nullable
    public static Constructor<? extends Pane> getConstructorForTypeAndStyle(@NotNull final String name, @Nullable final String style)
    {
        return paneConstructorMap.get(makeFactoryKey(name, style));
    }
}
