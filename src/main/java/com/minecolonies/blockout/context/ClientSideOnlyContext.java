package com.minecolonies.blockout.context;

import com.minecolonies.blockout.context.core.IContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSideOnlyContext implements IContext
{

    @Override
    public String toString()
    {
        return "CLIENT SIDE UI";
    }
}
