package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiCommandMessage implements IBlockOutServerToClientMessage
{
    private static final long serialVersionUID = Constants.SERIAL_VAR_ID;

    @NotNull
    private IGuiKey        key;
    @NotNull
    private IUIElementData data;

    public OpenGuiCommandMessage()
    {
    }

    public OpenGuiCommandMessage(@NotNull final IGuiKey key, @NotNull final IUIElementData data)
    {
        this.key = key;
        this.data = data;
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }

    @NotNull
    public IUIElementData getData()
    {
        return data;
    }

    @Nullable
    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
    }
}
