package com.ldtteam.blockout.utilities.helpers;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.element.advanced.TemplateInstance;
import com.ldtteam.blockout.utilities.template.DefaultTemplateUtils;
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
     *
     * @return an IGuiController.
     */
    public static IGuiController getGuiController()
    {
        return BlockOut.getBlockOut().getProxy().getGuiController();
    }

    /**
     * Initiate a standard chest control at a certain position in the dimension.
     *
     * @param theBuilder    the builder to append it to.
     * @param world         the dimension the chest is in.
     * @param chestPosition the position of the chest.
     */
    public static void initiateChestControlAtPosition(
      @NotNull final IBlockOutGuiConstructionDataBuilder theBuilder,
      @NotNull final World world,
      @NotNull final String controlId,
      @NotNull final BlockPos chestPosition,
      @NotNull final ResourceLocation inventoryId,
      @NotNull final ResourceLocation slotResource)
    {
        theBuilder.withControl(
          controlId,
          TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
          initiateStandardInventoryWithItemHandler(world.getTileEntity(chestPosition).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
            inventoryId,
            slotResource,
            9)
        );
    }

    /**
     * Initialize an itemHandler based inventory.
     *
     * @param handler      the handler
     * @param inventoryId  the inventoryId resource string.
     * @param slotResource the slot resource string.
     * @param width        the width.
     * @return the finished data builder.
     */
    private static Consumer<TemplateInstance.TemplateInstanceConstructionDataBuilder> initiateStandardInventoryWithItemHandler(
      @NotNull final IItemHandler handler,
      @NotNull final ResourceLocation inventoryId,
      @NotNull final ResourceLocation slotResource,
      final int width)
    {
        return templateInstanceConstructionDataBuilder -> templateInstanceConstructionDataBuilder.withDependentDataContext(
          DependencyObjectHelper.createFromValue(
            DefaultTemplateUtils
              .generateSlotGrid(
                handler,
                inventoryId,
                slotResource,
                width)
          ));
    }

    /**
     * Initiate the main player inventory for a given player.
     *
     * @param theBuilder   the builder to append it to.
     * @param entityPlayer the owner of the inventory.
     */
    public static void initiatePlayerInventoryControl(
      @NotNull final IBlockOutGuiConstructionDataBuilder theBuilder,
      @NotNull final String controlId,
      @NotNull final EntityPlayerMP entityPlayer,
      @NotNull final ResourceLocation inventoryId,
      final int minSlot,
      final int maxSlot,
      @NotNull final ResourceLocation slotResource)
    {
        theBuilder.withControl(
          controlId,
          TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
          initiatePlayerInventoryControlForRange(
            entityPlayer,
            inventoryId,
            slotResource,
            9,
            minSlot,
            maxSlot
          )
        );
    }

    /**
     * Initialize a player based inventory
     *
     * @param entityPlayer the player owner of the inventory.
     * @param slotResource the slot resource string.
     * @param width        the width.
     * @param minSlot      the starting slot.
     * @param maxSlot      the end slot.
     * @return the finished data builder.
     */
    private static Consumer<TemplateInstance.TemplateInstanceConstructionDataBuilder> initiatePlayerInventoryControlForRange(
      @NotNull final EntityPlayerMP entityPlayer,
      @NotNull final ResourceLocation inventoryId,
      @NotNull final ResourceLocation slotResource,
      final int width,
      final int minSlot,
      final int maxSlot)
    {
        return initiateStandardInventoryWithItemHandler(
          new RangedWrapper(
            (IItemHandlerModifiable)
              entityPlayer
                .getCapability(
                  CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                  EnumFacing.DOWN),
            minSlot,
            maxSlot
          ),
          inventoryId,
          slotResource,
          width);
    }
}
