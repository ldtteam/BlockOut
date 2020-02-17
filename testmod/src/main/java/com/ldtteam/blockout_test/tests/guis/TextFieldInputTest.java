package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.element.simple.Label;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
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
            final ServerPlayerEntity entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        final TextFieldContext context = new TextFieldContext();

        ProxyHolder.getInstance().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(new ResourceLocation(
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
                                                                                                                      .forEntity(entityPlayer));
    }

    public class TextFieldContext
    {

        private String contents = "";

        private String borderColor = "";

        public String getContents()
        {
            return contents;
        }

        public void setContents(final String contents)
        {
            this.contents = contents;
            if (contents.equals(""))
            {
                borderColor = "Yellow";
                return;
            }

            try{
                Integer.parseInt(contents);
                borderColor = "Green";
            }
            catch (Exception ex)
            {
                borderColor = "Red";
            }
        }

        public String getBorderColor()
        {
            return borderColor;
        }

        public void setBorderColor(final String borderColor)
        {
            this.borderColor = borderColor;
        }
    }
}
