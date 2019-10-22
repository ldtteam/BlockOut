package com.ldtteam.blockout_test.tests.guis;

import com.google.common.collect.ImmutableList;
import com.ldtteam.blockout.BlockOutForge;
import com.ldtteam.blockout.element.advanced.list.constructiondatabuilder.ListConstructionDataBuilder;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.context.BindingTestContext;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class BoundListHorizontalTest implements IBlockOutGuiTest
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
        ProxyHolder.getInstance().getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(new ResourceLocation(
                                                                                                                        "blockout_test:gui/horizontal_bound_list_test.json"))
                                                                                                      .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                                                                                          .withControl("list",
                                                                                                                                                            ListConstructionDataBuilder.class,
                                                                                                                                                            listConstructionDataBuilder -> listConstructionDataBuilder
                                                                                                                                                                                             .withSource(
                                                                                                                                                                                               ImmutableList
                                                                                                                                                                                                 .of(
                                                                                                                                                                                                   new BindingTestContext(
                                                                                                                                                                                                     "Bound Entry 1"),
                                                                                                                                                                                                   new BindingTestContext(
                                                                                                                                                                                                     "Bound Entry 2"),
                                                                                                                                                                                                   new BindingTestContext(
                                                                                                                                                                                                     "Hello this is 3")
                                                                                                                                                                                                 ))))
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }
}
