package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.element.simple.Label;
import com.ldtteam.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownTest implements IBlockOutUITest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.WHITE + "Image Only" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                      .ofFile(new ResourceLocation("blockout_test:gui/countdown_to_time.json"))
                                                                                                      .usingData(builder -> {
                                                                                                          builder.withControl("countdown",
                                                                                                            Label.LabelConstructionDataBuilder.class,
                                                                                                            labelConstructionDataBuilder -> {
                                                                                                                labelConstructionDataBuilder.withDataContext(new CountdownTest.CountdownDataContext());
                                                                                                            });
                                                                                                      })
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                      .forEntity(entityPlayer));
    }

    public class CountdownDataContext
    {

        private String           myDate = "2019/02/06 16:00:00";
        private SimpleDateFormat sdf    = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        public String getCountdown() throws ParseException
        {
            Date date = sdf.parse(myDate);
            long millis = date.getTime();

            long diff = -1 * (System.currentTimeMillis() - millis);
            if (diff <= 0)
            {
                return "Starting....";
            }

            return "See you in: " + new SimpleDateFormat("mm:ss").format(new Date(Math.abs(diff)));
        }
    }
}
