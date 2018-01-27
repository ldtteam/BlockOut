package com.minecolonies.blockout.core.element;

import com.minecolonies.blockout.core.management.IUIManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface IUIElementHost extends Map<ResourceLocation, IUIElement>, IUIElement
{
    @NotNull
    IUIManager getUiManager();

    @NotNull
    Optional<IUIElement> searchExactElementById(@NotNull final ResourceLocation id);

    @NotNull
    Optional<IUIElement> searchDeepestElementByCoord(final int localX, final int localY);

    @NotNull
    Optional<IUIElement> searchFirstElementByPredicate(@NotNull final Predicate<IUIElement> predicate);
}
