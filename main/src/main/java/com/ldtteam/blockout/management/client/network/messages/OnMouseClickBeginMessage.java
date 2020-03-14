package com.ldtteam.blockout.management.client.network.messages;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

public class OnMouseClickBeginMessage implements IBlockOutClientToServerMessage
{

    private static final long serialVersionUID = 942118386920517343L;

    private final int         localX;
    private final int         localY;
    @NotNull
    private final MouseButton button;

    public OnMouseClickBeginMessage(final int localX, final int localY, @NotNull final MouseButton button)
    {
        this.localX = localX;
        this.localY = localY;
        this.button = button;
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final NetworkEvent.Context ctx)
    {
        final ServerPlayerEntity player = ctx.getSender();
        Validate.notNull(player);

        final IGuiKey key = ProxyHolder.getInstance().getGuiController().getOpenUI(player);
        if (key == null)
        {
            Log.getLogger().error("Player is not watching a BlockOut Gui.");
            return;
        }

        final IRootGuiElement rootGuiElement = ProxyHolder.getInstance().getGuiController().getRoot(key);
        if (rootGuiElement == null)
        {
            Log.getLogger().error("Player seems to be watching an unknown Gui.");
            return;
        }

        rootGuiElement.getUiManager().getClickManager().onMouseClickBegin(localX, localY, button);
    }
}
