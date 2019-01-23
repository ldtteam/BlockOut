package com.ldtteam.blockout_test.tests.guis;

import com.google.common.collect.ImmutableList;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.advanced.List;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout_test.context.BindingTestContext;
import com.ldtteam.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class BoundListHorizontalTest implements IBlockOutUITest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.WHITE + "Horizontal Bound List.";
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                      .ofFile(new ResourceLocation("blockout_test:gui/horizontal_bound_list_test.json"))
                                                                                                      .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                                      .withControl("list", List.ListConstructionDataBuilder.class, listConstructionDataBuilder -> listConstructionDataBuilder
                                                                                                      .withSource(ImmutableList.of(
                                                                                                        new BindingTestContext("Bound Entry 1"),
                                                                                                        new BindingTestContext("Bound Entry 2"),
                                                                                                        new BindingTestContext("Hello this is 3")
                                                                                                      ))))
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                      .forEntity(entityPlayer));
    }
}
