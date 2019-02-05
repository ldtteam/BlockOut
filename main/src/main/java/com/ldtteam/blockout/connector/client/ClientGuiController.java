package com.ldtteam.blockout.connector.client;

import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.NetworkManager;
import com.ldtteam.blockout.network.message.CloseGuiRequestMessage;
import com.ldtteam.blockout.network.message.OpenGuiRequestMessage;
import com.ldtteam.blockout.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class ClientGuiController implements IGuiController
{
    @Override
    public void openUI(
      @NotNull final EntityPlayer player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@NotNull final EntityPlayer player, @NotNull final IGuiKey key)
    {
        openUI(player.getUniqueID(), key);
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(playerId, builder.build());
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot open UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new OpenGuiRequestMessage(key));
    }

    @Override
    public void closeUI(@NotNull final EntityPlayer player)
    {
        closeUI(player.getUniqueID());
    }

    @Override
    public void closeUI(@NotNull final UUID playerId)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot close UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new CloseGuiRequestMessage());
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final EntityPlayer player)
    {
        return null;
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final UUID player)
    {
        return null;
    }

    @Nullable
    @Override
    public RootGuiElement getRoot(@NotNull final IGuiKey guiKey)
    {
        return null;
    }
}