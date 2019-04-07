package com.ldtteam.blockout.utilities.template;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultTemplateUtils
{

    private static final List<IBlockState> states = Lists.newArrayList();
    static
    {
        for (Block block : ForgeRegistries.BLOCKS)
        {
            for (IBlockState state : block.getBlockState().getValidStates())
            {
                try
                {
                    if (state != block.getStateFromMeta(block.getMetaFromState(state)))
                    {
                        continue;
                    }

                    states.add(state);
                }
                catch (Throwable t)
                {
                    // those are important so rethrow
                    if (t instanceof VirtualMachineError)
                    {
                        throw (VirtualMachineError) t;
                    }
                    // everything else - -assume mods are stupid and just log it
                    // this is awful but some mods just need their exceptions to be caught here
                    Log.getLogger().warn(t);
                }
            }
        }
    }

    private DefaultTemplateUtils()
    {
        throw new IllegalArgumentException("Utility");
    }

    public static List<GridUtilityWrapper<BlockStateUtilityWrapper>> generateBlockStateGrid(
      @NotNull final IIdentifier templateId,
      @NotNull final int width
    )
    {
        final List<BlockStateUtilityWrapper> wrappers = states.parallelStream().map(BlockStateUtilityWrapper::new).collect(Collectors.toList());

        return generateGrid(
          wrappers,
          width,
          templateId
        );
    }

    /**
     * Generates a grid of the Input.
     *
     * @param input           The input.
     * @param width           The amount of elements in the Row.
     * @param entryTemplateId The template Id.
     * @param <T>             The type contained.
     * @return A list ready for use with the grid template.
     */
    public static <T> List<GridUtilityWrapper<T>> generateGrid(
      @NotNull final List<T> input,
      @NotNull final int width,
      @NotNull final IIdentifier entryTemplateId
    )
    {
        final List<GridUtilityWrapper<T>> gridList = new ArrayList<>();
        final int rowCount = input.size() / width;
        for (int i = 0; i < input.size(); i += width)
        {
            @NotNull final List<T> entries = input.subList(i, (i + width > input.size()) ? input.size() : i + width);
            gridList.add(new GridUtilityWrapper<>(entries, entryTemplateId));
        }

        return gridList;
    }

    public static List<GridUtilityWrapper<SlotUtilityWrapper>> generateSlotGrid(
      @NotNull final IItemHandler iItemHandler,
      @NotNull final IIdentifier inventoryId,
      @NotNull final IIdentifier texture,
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
          IIdentifier.create("template:slot")
        );
    }

    public static List<SlotUtilityWrapper> generateSlotUtilsFromIItemHandler(
      @NotNull final IItemHandler iItemHandler,
      @NotNull final IIdentifier inventoryId,
      @NotNull final IIdentifier texture
    )
    {
        return IntStream.range(0, iItemHandler.getSlots()).mapToObj(index -> new SlotUtilityWrapper(inventoryId, texture, index)).collect(Collectors.toList());
    }
}
