package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.context.core.IContext;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiKey extends Serializable, Comparable<IGuiKey>
{

    @NotNull
    ResourceLocation getGuiDefinition();

    @NotNull
    IContext getGuiContext();
}
