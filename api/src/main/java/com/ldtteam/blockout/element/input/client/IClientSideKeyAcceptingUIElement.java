package com.ldtteam.blockout.element.input.client;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IClientSideKeyAcceptingUIElement extends IUIElement
{
    @SideOnly(Side.CLIENT)
    boolean canAcceptKeyInputClient(final int character, final KeyboardKey key);

    @SideOnly(Side.CLIENT)
    default boolean onKeyPressedClient(final int character, final KeyboardKey key)
    {
        return false;
    }
}
