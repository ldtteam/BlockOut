package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.utilities.helpers.BlockOutHelper;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.facing.Facing;
import com.ldtteam.jvoxelizer.util.facing.IFacing;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.math.coordinate.block.IBlockCoordinate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class InventoryGridTest implements IBlockOutGuiTest
{
    private static final String      CHEST_CONTROL_ID         = "chest_inventory";
    private static final IIdentifier CHEST_INVENTORY_ID       = IIdentifier.create("minecraft:chest");
    private static final String      PLAYER_MAIN_CONTROL_ID   = "player_main_inventory";
    private static final String      PLAYER_TOOL_CONTROL_ID   = "player_tool_inventory";
    private static final IIdentifier PLAYER_INVENTORY_ID      = IIdentifier.create("minecraft:player");
    private static final IIdentifier PLAYER_MAIN_INVENTORY_ID = IIdentifier.create("minecraft:player_main");
    private static final IIdentifier PLAYER_TOOL_INVENTORY_ID = IIdentifier.create("minecraft:player_tool");
    private static final IIdentifier SLOT_BACKGROUND          = IIdentifier.create("image:slot_default_18x18");

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
        BlockOutHelper.getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                           .ofFile(IIdentifier.create("blockout_test:gui/inventory_grid_test.json"))
                                                                                   .usingData(
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiateChestControlAtPosition(
                                                                                       iBlockOutGuiConstructionDataBuilder,
                                                                                       entityPlayer.world,
                                                                                       CHEST_CONTROL_ID,
                                                                                       new BlockPos(0, 5, 0),
                                                                                       CHEST_INVENTORY_ID,
                                                                                       SLOT_BACKGROUND),
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiatePlayerInventoryControl(
                                                                                       iBlockOutGuiConstructionDataBuilder,
                                                                                       PLAYER_MAIN_CONTROL_ID,
                                                                                       entityPlayer,
                                                                                       PLAYER_MAIN_INVENTORY_ID,
                                                                                       9,
                                                                                       36,
                                                                                       SLOT_BACKGROUND),
                                                                                     iBlockOutGuiConstructionDataBuilder -> BlockOutHelper.initiatePlayerInventoryControl(
                                                                                       iBlockOutGuiConstructionDataBuilder,
                                                                                       PLAYER_TOOL_CONTROL_ID,
                                                                                       entityPlayer,
                                                                                       PLAYER_TOOL_INVENTORY_ID,
                                                                                       0,
                                                                                       9,
                                                                                       SLOT_BACKGROUND)
                                                                                   )
                                                                                   .withItemHandlerManager(iItemHandlerManagerBuilder -> iItemHandlerManagerBuilder
                                                                                                                                           .withTileBasedProvider(
                                                                                                                                             CHEST_INVENTORY_ID,
                                                                                                                                             0,
                                                                                                                                             IBlockCoordinate.create(0,
                                                                                                                                               5,
                                                                                                                                               0),
                                                                                                                                             null)
                                                                                                                                           .withEntityBasedProvider(
                                                                                                                                             PLAYER_INVENTORY_ID,
                                                                                                                                             PlayerEntity.fromForge(entityPlayer),
                                                                                                                                             Facing.fromForge(EnumFacing.DOWN))
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
                                                                                                           .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }
}
