package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.BlockOutForge;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.element.simple.Label;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;

public class TextFieldInputTest implements IBlockOutGuiTest
{

    @NotNull
    @Override
    public String getTestName()
    {
        return "TextField Binding";
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        final TextFieldContext context = new TextFieldContext();

        ProxyHolder.getInstance().getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(IIdentifier.create(
                                                                                                                        "blockout_test:gui/textfield_binding_test.json"))
                                                                                                      .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                                                                                          .withControl("input",
                                                                                                                                                            TextField.TextFieldConstructionDataBuilder.class,
                                                                                                                                                            textFieldConstructionDataBuilder ->
                                                                                                                                                              textFieldConstructionDataBuilder
                                                                                                                                                                .withDataContext(
                                                                                                                                                                  context)
                                                                                                                                                                .withEnterEventHandler(
                                                                                                                                                                  (textfield, e) -> Log
                                                                                                                                                                                      .getLogger()
                                                                                                                                                                                      .warn(
                                                                                                                                                                                        "Enter pressed: "
                                                                                                                                                                                          + textfield
                                                                                                                                                                                              .getId())))
                                                                                                                                                          .withControl("output",
                                                                                                                                                            Label.LabelConstructionDataBuilder.class,
                                                                                                                                                            labelConstructionDataBuilder ->
                                                                                                                                                              labelConstructionDataBuilder
                                                                                                                                                                .withDataContext(
                                                                                                                                                                  context)))
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }

    public class TextFieldContext
    {

        private String contents = "";

        public String getContents()
        {
            return contents;
        }

        public void setContents(final String contents)
        {
            this.contents = contents;
        }
    }
}
