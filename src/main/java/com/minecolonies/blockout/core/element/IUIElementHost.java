package com.minecolonies.blockout.core.element;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public interface IUIElementHost extends Map<ResourceLocation, IUIElement>, IUIElement
{

    @NotNull
    Optional<IUIElement> searchElementById(@NotNull final ResourceLocation id);

    @NotNull
    Optional<IUIElement> searchElementByCoord(final int localX, final int localY);
}
