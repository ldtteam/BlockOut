package com.minecolonies.blockout.proxy;

import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class CommonProxy implements IProxy
{


    @Override
    public IThreadListener getTaskExecutorForMessage(@NotNull final MessageContext ctx)
    {
        return ctx.getServerHandler().player.getServerWorld();
    }
}
