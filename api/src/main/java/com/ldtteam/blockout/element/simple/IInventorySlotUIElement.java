package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.element.IUIElement;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IInventorySlotUIElement extends IUIElement {
    @NotNull
    ResourceLocation getInventoryId();

    @NotNull
    Integer getInventoryIndex();

    @NotNull
    int getSlotIndex();

    void setSlotIndex(@NotNull int slotIndex);
}
