package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NBTDependingConverters
{
    private static final NBTBaseConverter<INBTCompound> COMPOUND_NBT_BASE_CONVERTER = new NBTBaseConverter.CompoundConverter();

    public static final class ItemStackConverter implements IUIElementDataComponentConverter<IItemStack>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public IItemStack readFromElement(
          @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            final INBTCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            final IItemStack stack = IItemStack.create();

            stack.read(compound);

            return stack;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final IItemStack value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final INBTCompound compound = value.write();
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class BlockStateConverter implements IUIElementDataComponentConverter<IBlockState>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public IBlockState readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            final INBTCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            final IBlockState state = IBlockState.create();

            state.read(compound);

            return state;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final IBlockState value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final INBTCompound compound = value.write();
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class EntityConverter implements IUIElementDataComponentConverter<IEntity>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return COMPOUND_NBT_BASE_CONVERTER.matchesInputTypes(component);
        }

        @NotNull
        @Override
        public IEntity readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            final INBTCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);

            final IEntity entity = IEntity.create();

            entity.read(compound);

            return entity;
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final IEntity value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final INBTCompound compound = value.write();
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }
}
