package com.minecolonies.blockout_test.tests.guis;

import com.minecolonies.blockout.element.simple.Button;
import com.minecolonies.blockout.util.BlockOutHelper;
import com.minecolonies.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class InventoryGridTest implements IBlockOutUITest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return "Inventory Grid";
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        BlockOutHelper.getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                   .ofFile(new ResourceLocation("blockout_test:gui/inventory_grid_test.json"))
                                                                                   .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                                                                       .forwardingControl(BlockOutHelper.initiateChestControlAtPosition(iBlockOutGuiConstructionDataBuilder, entityPlayer.world, new BlockPos(0,5,0)))
                                                                                                                                       .forwardingControl(BlockOutHelper.initiatePlayerMainInventoryControl(iBlockOutGuiConstructionDataBuilder, entityPlayer))
                                                                                                                                       .forwardingControl(BlockOutHelper.initiatePlayerHotBarControl(iBlockOutGuiConstructionDataBuilder, entityPlayer))
                                                                                   )
                                                                                   .withItemHandlerManager(iItemHandlerManagerBuilder -> iItemHandlerManagerBuilder
                                                                                                                                           .withTileBasedProvider(
                                                                                                                                             new ResourceLocation(
                                                                                                                                               "minecraft:chest"),
                                                                                                                                             0,
                                                                                                                                             new BlockPos(0,
                                                                                                                                               5,
                                                                                                                                               0),
                                                                                                                                             null)
                                                                                                                                           .withEntityBasedProvider(
                                                                                                                                             new ResourceLocation(
                                                                                                                                               "minecraft:player"),
                                                                                                                                             entityPlayer,
                                                                                                                                             EnumFacing.DOWN)
                                                                                                                                           .withWrapped(new ResourceLocation(
                                                                                                                                               "minecraft:player_main"),
                                                                                                                                             new ResourceLocation(
                                                                                                                                               "minecraft:player"),
                                                                                                                                             9,
                                                                                                                                             36)
                                                                                                                                           .withWrapped(new ResourceLocation(
                                                                                                                                               "minecraft:player_tool"),
                                                                                                                                             new ResourceLocation(
                                                                                                                                               "minecraft:player"),
                                                                                                                                             0,
                                                                                                                                             9))
                                                                                   .forEntity(entityPlayer));
    }
}
