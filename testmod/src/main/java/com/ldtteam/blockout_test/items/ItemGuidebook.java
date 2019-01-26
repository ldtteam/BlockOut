package com.ldtteam.blockout_test.items;

import com.ldtteam.blockout.BlockOut;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

/**
 * Class describing the Guide Book item.
 */
public class ItemGuidebook extends AbstractItemMinecolonies
{
    private static final String LOCATION_OF_GUIDEBOOKS = ":gui/guidebook/";

    /**
     * Sets the name, creative tab, and registers the Guide Book item.
     */
    public ItemGuidebook()
    {
        super("guidebook");
        super.setCreativeTab(ModCreativeTabs.BOTEST);
        maxStackSize = 1;
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand)
    {
        final ItemStack stack = playerIn.getHeldItem(hand);
        final String clientLanguage;

        // TODO: check if a book translation (LOCATION_OF_GUIDEBOOKS) exists and then use it
        if (!worldIn.isRemote)
        {
            if (true)
            {
                clientLanguage = "en_us";
            }
            else
            {
                clientLanguage = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
            }
            // com.ldtteam.botest.BoTest.logger.info(worldIn.isRemote);

            if (!(playerIn instanceof EntityPlayerMP))
            {
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }

            //@Nullable
            //final WindowGuideBook window = new WindowGuideBook(stack, ((EntityPlayerMP) playerIn).getAdvancements(), clientLanguage);

            EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;
            BlockOut.getBlockOut().getProxy().getGuiController()
              .openUI(playerMP, iGuiKeyBuilder -> iGuiKeyBuilder
                                                    .ofFile(new ResourceLocation("blockout_test" + LOCATION_OF_GUIDEBOOKS + clientLanguage + ".json"))
                                                    .forEntity(playerMP));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}