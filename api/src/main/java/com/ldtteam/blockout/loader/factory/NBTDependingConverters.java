package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NBTDependingConverters
{
    private static final NBTBaseConverter<CompoundNBT> COMPOUND_NBT_BASE_CONVERTER = new NBTBaseConverter.CompoundConverter();

    public static final class ItemStackConverter implements IUIElementDataComponentConverter<ItemStack>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public ItemStack readFromElement(
          @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData<?> sourceData, @NotNull final Object... params)
        {
            final CompoundNBT compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            final ItemStack stack = ItemStack.read(compound);

            return stack;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final ItemStack value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final CompoundNBT compound = new CompoundNBT();
            value.write(compound);

            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class BlockStateConverter implements IUIElementDataComponentConverter<BlockState>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public BlockState readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData<?> sourceData, @NotNull final Object... params)
        {
            final CompoundNBT compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            final BlockState state = NBTUtil.readBlockState(compound);

            return state;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final BlockState value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final CompoundNBT compound = NBTUtil.writeBlockState(value);
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class EntityConverter implements IUIElementDataComponentConverter<Entity>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public Entity readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData<?> sourceData, @NotNull final Object... params)
        {
            final CompoundNBT compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);

            final Entity entity = EntityType.loadEntityUnchecked(compound, null).orElseThrow(() -> new IllegalArgumentException("Could not load entity."));
            entity.read(compound);

            return entity;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Entity value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final CompoundNBT compound = value.serializeNBT();
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }
}
