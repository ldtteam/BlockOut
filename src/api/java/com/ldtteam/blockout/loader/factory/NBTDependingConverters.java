package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NBTDependingConverters
{
    private static final NBTBaseConverter<NBTTagCompound> COMPOUND_NBT_BASE_CONVERTER = new NBTBaseConverter<>();

    public static final class ItemStackConverter implements IUIElementDataComponentConverter<ItemStack>
    {

        @NotNull
        @Override
        public ItemStack readFromElement(
          @NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final NBTTagCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            return new ItemStack(compound);
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final ItemStack value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final NBTTagCompound compound = value.writeToNBT(new NBTTagCompound());
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class BlockStateConverter implements IUIElementDataComponentConverter<IBlockState>
    {

        @NotNull
        @Override
        public IBlockState readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final NBTTagCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            return NBTUtil.readBlockState(compound);
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final IBlockState value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final NBTTagCompound compound = NBTUtil.writeBlockState(new NBTTagCompound(), value);
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }

    public static final class EntityConverter implements IUIElementDataComponentConverter<Entity>
    {

        @NotNull
        @Override
        public Entity readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final NBTTagCompound compound = COMPOUND_NBT_BASE_CONVERTER.readFromElement(component, sourceData, params);
            return EntityList.createEntityFromNBT(compound, null);
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final Entity value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final NBTTagCompound compound = value.writeToNBT(new NBTTagCompound());
            return COMPOUND_NBT_BASE_CONVERTER.writeToElement(compound, newComponentInstanceProducer);
        }
    }
}
