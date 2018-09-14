package com.minecolonies.blockout.util;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.element.advanced.TemplateInstance;
import com.minecolonies.blockout.template.utils.DefaultTemplateUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * BlockOut helper class to avoid chaining common method calls.
 */
public class BlockOutHelper
{
    /**
     * Private constructor to hide implicit public one.
     */
    private BlockOutHelper()
    {
        /*
         * Intentionally left empty.
         */
    }

    /**
     * Get the GUI controller from BlockOut over the proxy.
     * @return an IGuiController.
     */
    public static IGuiController getGuiController()
    {
        return BlockOut.getBlockOut().getProxy().getGuiController();
    }

    /**
     * Initialize a player based inventory
     * @param entityPlayer the player owner of the inventory.
     * @param backGround the background resource string.
     * @param slotResource the slot resource string.
     * @param width the width.
     * @param minSlot the starting slot.
     * @param maxSlot the end slot.
     * @return the finished data builder.
     */
    private static Consumer<TemplateInstance.TemplateInstanceConstructionDataBuilder> initiatePlayerInventoryControlForRange(@NotNull final EntityPlayerMP entityPlayer, @NotNull final String backGround, @NotNull final String slotResource, final int width, final int minSlot, final int maxSlot)
    {
        return initiateStandardInventoryWithItemHandler( new RangedWrapper(
          (IItemHandlerModifiable)
            entityPlayer
              .getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                EnumFacing.DOWN),
          minSlot,
          maxSlot
        ), backGround, slotResource, width);
    }

    /**
     * Initialize an itemHandler based inventory.
     * @param handler the handler
     * @param background the background resource string.
     * @param slotResource the slot resource string.
     * @param width the width.
     * @return the finished data builder.
     */
    private static Consumer<TemplateInstance.TemplateInstanceConstructionDataBuilder> initiateStandardInventoryWithItemHandler(@NotNull final IItemHandler handler, @NotNull final String background, @NotNull final String slotResource, final int width)
    {
        return templateInstanceConstructionDataBuilder -> templateInstanceConstructionDataBuilder.withDependentDataContext(
          DependencyObjectHelper.createFromValue(
            DefaultTemplateUtils
              .generateSlotGrid(
                handler,
                new ResourceLocation(background),
                new ResourceLocation(slotResource), width)
          ));
    }

    /**
     * Initiate a standard chest control at a certain position in the world.
     * @param theBuilder the builder to append it to.
     * @param world the world the chest is in.
     * @param chestPosition the position of the chest.
     * @return true if successful.
     */
    public static boolean initiateChestControlAtPosition(@NotNull final IBlockOutGuiConstructionDataBuilder theBuilder, @NotNull final World world, @NotNull final BlockPos chestPosition)
    {
        initiateStandardInventoryWithItemHandler(world.getTileEntity(chestPosition).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), "minecraft:chest", "image:slot_default_18x18", 9).accept(new TemplateInstance.TemplateInstanceConstructionDataBuilder("chest_inventory", theBuilder));
        return true;
    }

    /**
     * Initiate the main player inventory for a given player.
     * @param theBuilder the builder to append it to.
     * @param entityPlayer the owner of the inventory.
     * @return true if successful.
     */
    public static boolean initiatePlayerMainInventoryControl(final IBlockOutGuiConstructionDataBuilder theBuilder, final EntityPlayerMP entityPlayer)
    {
        initiatePlayerInventoryControlForRange(entityPlayer, "minecraft:player_main", "image:slot_default_18x18", 9, 9, 36).accept(new TemplateInstance.TemplateInstanceConstructionDataBuilder("player_main_inventory", theBuilder));
        return true;
    }

    /**
     * Initiate the player hotBar for a given player.
     * @param theBuilder the builder to append it to.
     * @param entityPlayer the owner of the inventory.
     * @return true if successful.
     */
    public static boolean initiatePlayerHotBarControl(final IBlockOutGuiConstructionDataBuilder theBuilder, final EntityPlayerMP entityPlayer)
    {
        initiatePlayerInventoryControlForRange(entityPlayer, "minecraft:player_tool", "image:slot_default_18x18", 9, 0, 9).accept(new TemplateInstance.TemplateInstanceConstructionDataBuilder("player_tool_inventory", theBuilder));
        return true;
    }
}
