package com.ldtteam.blockout.core.element.input;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IClientSideScrollAcceptingUIElement
{
    @SideOnly(Side.CLIENT)
    boolean canAcceptMouseInputClient(final int localX, final int localY, final int deltaWheel);

    @SideOnly(Side.CLIENT)
    void onMouseScrollClient(final int localX, final int localY, final int deltaWheel);
}
