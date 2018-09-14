package com.minecolonies.blockout_test.tests.guis;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.element.advanced.TemplateInstance;
import com.minecolonies.blockout.element.simple.Button;
import com.minecolonies.blockout.template.utils.DefaultTemplateUtils;
import com.minecolonies.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
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
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                      .ofFile(new ResourceLocation("blockout_test:gui/inventory_grid_test.json"))
                                                                                                      .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                                                                                          .withControl(
                                                                                                                                                            "chest_inventory",
                                                                                                                                                            TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
                                                                                                                                                            templateInstanceConstructionDataBuilder ->
                                                                                                                                                              templateInstanceConstructionDataBuilder
                                                                                                                                                                .withDependentDataContext(
                                                                                                                                                                  DependencyObjectHelper.createFromValue(
                                                                                                                                                                    DefaultTemplateUtils
                                                                                                                                                                      .generateSlotGrid(
                                                                                                                                                                        entityPlayer.world
                                                                                                                                                                                         .getTileEntity(
                                                                                                                                                                                           new BlockPos(
                                                                                                                                                                                             0,
                                                                                                                                                                                             5,
                                                                                                                                                                                             0))
                                                                                                                                                                                         .getCapability(
                                                                                                                                                                                           CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                                                                                                                                                                           null),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "minecraft:chest"),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "image:slot_default_18x18"),
                                                                                                                                                                        9)
                                                                                                                                                                  )
                                                                                                                                                                ))
                                                                                                                                                          .withControl(
                                                                                                                                                            "player_main_inventory",
                                                                                                                                                            TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
                                                                                                                                                            templateInstanceConstructionDataBuilder ->
                                                                                                                                                              templateInstanceConstructionDataBuilder
                                                                                                                                                                .withDependentDataContext(
                                                                                                                                                                  DependencyObjectHelper.createFromValue(
                                                                                                                                                                    DefaultTemplateUtils
                                                                                                                                                                      .generateSlotGrid(
                                                                                                                                                                        new RangedWrapper(
                                                                                                                                                                          (IItemHandlerModifiable)
                                                                                                                                                                            entityPlayer
                                                                                                                                                                              .getCapability(
                                                                                                                                                                                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                                                                                                                                                                EnumFacing.DOWN),
                                                                                                                                                                          9,
                                                                                                                                                                          36
                                                                                                                                                                        ),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "minecraft:player_main"),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "image:slot_default_18x18"),
                                                                                                                                                                        9)
                                                                                                                                                                  )
                                                                                                                                                                ))
                                                                                                                                                          .withControl(
                                                                                                                                                            "player_tool_inventory",
                                                                                                                                                            TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
                                                                                                                                                            templateInstanceConstructionDataBuilder ->
                                                                                                                                                              templateInstanceConstructionDataBuilder
                                                                                                                                                                .withDependentDataContext(
                                                                                                                                                                  DependencyObjectHelper.createFromValue(
                                                                                                                                                                    DefaultTemplateUtils
                                                                                                                                                                      .generateSlotGrid(
                                                                                                                                                                        new RangedWrapper(
                                                                                                                                                                          (IItemHandlerModifiable)
                                                                                                                                                                            entityPlayer
                                                                                                                                                                              .getCapability(
                                                                                                                                                                                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                                                                                                                                                                EnumFacing.DOWN),
                                                                                                                                                                          0,
                                                                                                                                                                          9
                                                                                                                                                                        ),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "minecraft:player_tool"),
                                                                                                                                                                        new ResourceLocation(
                                                                                                                                                                          "image:slot_default_18x18"),
                                                                                                                                                                        9)
                                                                                                                                                                  )
                                                                                                                                                                ))
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
