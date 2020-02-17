package com.ldtteam.blockout_test;

import com.ldtteam.blockout_test.command.CommandOpenTestGui;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod("blockout_test")
public class BlockOut_Test
{

    private static BlockOut_Test blockOutTest;

    public static BlockOut_Test getBlockOutTest()
    {
        return blockOutTest;
    }

    public BlockOut_Test() {
        blockOutTest = this;

        Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onFMLServerStarting);
    }

    public void onFMLServerStarting(final FMLServerStartingEvent event)
    {
        CommandOpenTestGui.register(event.getCommandDispatcher());
    }
}
