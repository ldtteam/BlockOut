package com.minecolonies.blockout.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

    @Override
    public IThreadListener getTaskExecutorForMessage(@NotNull final MessageContext ctx)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            return Minecraft.getMinecraft();
        }

        return super.getTaskExecutorForMessage(ctx);
    }
}
