package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiCommandMessage implements IBlockOutServerToClientMessage
{
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

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(data);

        if (!(element instanceof RootGuiElement))
        {
            throw new IllegalStateException("Root element is not a RootGuiElement");
        }

        final RootGuiElement root = (RootGuiElement) element;
        final BlockOutGui gui = new BlockOutGui(new BlockOutContainer(key, root));

        Minecraft.getMinecraft().displayGuiScreen(gui);
    }
}
