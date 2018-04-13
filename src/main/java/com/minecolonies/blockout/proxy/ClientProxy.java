package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.client.ClientGuiController;
import com.minecolonies.blockout.connector.core.IGuiController;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

    @NotNull
    private final ClientGuiController guiController;

    public ClientProxy() {guiController = new ClientGuiController();}

    @NotNull
    @Override
    public IGuiController getGuiController()
    {
        return guiController;
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }
}
