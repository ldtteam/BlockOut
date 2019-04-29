package com.ldtteam.blockout_test.tests.guis;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.element.advanced.list.constructiondatabuilder.ListConstructionDataBuilder;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.context.BindingTestContext;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GrowingListWithButtonTest implements IBlockOutGuiTest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.RED + "Dynamic list" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        final ArrayList<BindingTestContext> list = Lists.newArrayList();

        ProxyHolder.getInstance().getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(IIdentifier.create(
                                                                                                                        "blockout_test:gui/dynamic_list.json"))
                                                                                              .usingData(b -> b
                                                                                                                .withControl("add_button",
                                                                                                                  Button.ButtonConstructionDataBuilder.class,
                                                                                                                  bb -> bb
                                                                                                                          .withClickedEventHandler((bu, e) -> {
                                                                                                                              if (!e.isStart())
                                                                                                                              {
                                                                                                                                  return;
                                                                                                                              }

                                                                                                                              list.add(new BindingTestContext(
                                                                                                                                "Entry: " + (list.size() + 1)));
                                                                                                                          }))
                                                                                                                .withControl("content_list",
                                                                                                                  ListConstructionDataBuilder.class,
                                                                                                                  ll -> ll.withDataContext(list)
                                                                                                                          .withTemplateConstructionData(tcb -> tcb
                                                                                                                                                                 .withControl(
                                                                                                                                                                   "delete_button",
                                                                                                                                                                   Button.ButtonConstructionDataBuilder.class,
                                                                                                                                                                   bb -> bb.withClickedEventHandler(
                                                                                                                                                                     (bu, e) -> {
                                                                                                                                                                         if (!e.isStart())
                                                                                                                                                                         {
                                                                                                                                                                             return;
                                                                                                                                                                         }

                                                                                                                                                                         list.remove(
                                                                                                                                                                           bu.getDataContext());
                                                                                                                                                                     }))))
                                                                                              )
                                                                                              .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }
}
