package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.utilities.helpers.BlockOutHelper;
import com.ldtteam.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class InventoryGridTest implements IBlockOutUITest
{
    private static final String CHEST_CONTROL_ID = "chest_inventory";
    private static final ResourceLocation CHEST_INVENTORY_ID = new ResourceLocation("minecraft:chest");
    private static final String PLAYER_MAIN_CONTROL_ID = "player_main_inventory";
    private static final String PLAYER_TOOL_CONTROL_ID = "player_tool_inventory";
    private static final ResourceLocation PLAYER_INVENTORY_ID = new ResourceLocation("minecraft:player");
    private static final ResourceLocation PLAYER_MAIN_INVENTORY_ID = new ResourceLocation("minecraft:player_main");
    private static final ResourceLocation PLAYER_TOOL_INVENTORY_ID = new ResourceLocation("minecraft:player_tool");
    private static final ResourceLocation SLOT_BACKGROUND = new ResourceLocation("image:slot_default_18x18");


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
                                                                                   .usingData(
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiateChestControlAtPosition(iBlockOutGuiConstructionDataBuilder, entityPlayer.world, CHEST_CONTROL_ID, new BlockPos(0,5,0), CHEST_INVENTORY_ID, SLOT_BACKGROUND),
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiatePlayerInventoryControl(iBlockOutGuiConstructionDataBuilder, PLAYER_MAIN_CONTROL_ID, entityPlayer, PLAYER_MAIN_INVENTORY_ID, 9, 36, SLOT_BACKGROUND),
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiatePlayerInventoryControl(iBlockOutGuiConstructionDataBuilder, PLAYER_TOOL_CONTROL_ID, entityPlayer, PLAYER_TOOL_INVENTORY_ID, 0, 9, SLOT_BACKGROUND)
                                                                                   )
                                                                                   .withItemHandlerManager(iItemHandlerManagerBuilder -> iItemHandlerManagerBuilder
                                                                                                                                           .withTileBasedProvider(
                                                                                                                                               CHEST_INVENTORY_ID,
                                                                                                                                             0,
                                                                                                                                             new BlockPos(0,
                                                                                                                                               5,
                                                                                                                                               0),
                                                                                                                                             null)
                                                                                                                                           .withEntityBasedProvider(
                                                                                                                                             PLAYER_INVENTORY_ID,
                                                                                                                                             entityPlayer,
                                                                                                                                             EnumFacing.DOWN)
                                                                                                                                           .withWrapped(
                                                                                                                                             PLAYER_MAIN_INVENTORY_ID,
                                                                                                                                             PLAYER_INVENTORY_ID,
                                                                                                                                             9,
                                                                                                                                             36)
                                                                                                                                           .withWrapped(
                                                                                                                                             PLAYER_TOOL_INVENTORY_ID,
                                                                                                                                             PLAYER_INVENTORY_ID,
                                                                                                                                             0,
                                                                                                                                             9))
                                                                                   .forEntity(entityPlayer));
    }
}
