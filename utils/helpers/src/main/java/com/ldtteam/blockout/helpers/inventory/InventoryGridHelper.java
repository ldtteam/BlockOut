package com.ldtteam.blockout.helpers.inventory;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.element.advanced.TemplateInstance;
import com.ldtteam.blockout.helpers.template.DefaultTemplateHelper;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.jvoxelizer.common.capability.ICapability;
import com.ldtteam.jvoxelizer.dimension.IDimension;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import net.minecraftforge.items.IItemHandler;
import com.ldtteam.jvoxelizer.item.handling.IItemHandlerProvider;
import net.minecraft.util.Direction;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class InventoryGridHelper
{
        /**
         * Initiate a standard chest control at a certain position in the IDimension.
         *
         * @param theBuilder    the builder to append it to.
         * @param IDimension         the IDimension the chest is in.
         * @param chestPosition the position of the chest.
         */
        public static void initiateChestControlAtPosition(
          @NotNull final IBlockOutGuiConstructionDataBuilder theBuilder,
          @NotNull final IDimension IDimension,
          @NotNull final String controlId,
          @NotNull final BlockPos chestPosition,
          @NotNull final IIdentifier inventoryId,
          @NotNull final IIdentifier slotResource)
        {
            theBuilder.withControl(
              controlId,
              TemplateInstance.TemplateInstanceConstructionDataBuilder.class,
              initiateStandardInventoryWithItemHandler(IDimension.getBlockEntity(chestPosition).getCapability(ICapability.getItemHandlerCapability(), null),
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
          @NotNull final IIdentifier inventoryId,
          @NotNull final IIdentifier slotResource,
          final int width)
        {
            return templateInstanceConstructionDataBuilder -> templateInstanceConstructionDataBuilder.withDependentDataContext(
              DependencyObjectHelper.createFromValue(
                DefaultTemplateHelper
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
          @NotNull final IMultiplayerPlayerEntity entityPlayer,
          @NotNull final IIdentifier inventoryId,
          final int minSlot,
          final int maxSlot,
          @NotNull final IIdentifier slotResource)
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
          @NotNull final IMultiplayerPlayerEntity entityPlayer,
          @NotNull final IIdentifier inventoryId,
          @NotNull final IIdentifier slotResource,
          final int width,
          final int minSlot,
          final int maxSlot)
        {
            return initiateStandardInventoryWithItemHandler(
              IItemHandler.ranged(
                entityPlayer.getCapability(ICapability.getItemHandlerCapability(), IFacing.getDown()),
                minSlot,
                maxSlot
              ),
              inventoryId,
              slotResource,
              width);
        }
}
