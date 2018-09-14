package com.minecolonies.blockout.template.utils;

import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultTemplateUtils
{

    private DefaultTemplateUtils() {
        throw new IllegalArgumentException("Utility");
    }

    /**
     * Generates a grid of the Input.
     *
     * @param input The input.
     * @param width The amount of elements in the Row.
     * @param entryTemplateId The template Id.
     * @param <T> The type contained.
     *
     * @return A list ready for use with the grid template.
     */
    public static <T> List<GridUtilityWrapper<T>> generateGrid(
      @NotNull final List<T> input,
      @NotNull final int width,
      @NotNull final ResourceLocation entryTemplateId
    )
    {
        final List<GridUtilityWrapper<T>> gridList = new ArrayList<>();
        final int rowCount = input.size() / width;
        for (int i = 0; i < input.size(); i+=width)
        {
            @NotNull final List<T> entries = input.subList(i, (i+width > input.size()) ? input.size() : i + width);
            gridList.add(new GridUtilityWrapper<>(entries, entryTemplateId));
        }

        return gridList;
    }

    public static List<SlotUtilityWrapper> generateSlotUtilsFromIItemHandler(
      @NotNull final IItemHandler iItemHandler,
      @NotNull final ResourceLocation inventoryId,
      @NotNull final ResourceLocation texture
    )
    {
        return IntStream.range(0, iItemHandler.getSlots()).mapToObj(index -> new SlotUtilityWrapper(inventoryId, texture, index)).collect(Collectors.toList());
    }

    public static List<GridUtilityWrapper<SlotUtilityWrapper>> generateSlotGrid(
      @NotNull final IItemHandler iItemHandler,
      @NotNull final ResourceLocation inventoryId,
      @NotNull final ResourceLocation texture,
      @NotNull final int width
    )
    {
        return generateGrid(
          generateSlotUtilsFromIItemHandler(
            iItemHandler,
            inventoryId,
            texture
          ),
          width,
          new ResourceLocation("template:slot")
        );
    }
}
