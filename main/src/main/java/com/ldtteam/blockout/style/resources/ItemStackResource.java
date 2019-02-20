package com.ldtteam.blockout.style.resources;

import com.google.gson.JsonElement;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoader;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.item.IItemStack;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.nbt.INBTBase;
import com.ldtteam.jvoxelizer.util.nbt.INBTCompound;
import org.jetbrains.annotations.NotNull;

public class ItemStackResource implements IResource
{

    public static final class Loader implements IResourceLoader<ItemStackResource>
    {

        /**
         * Returns the if of the type that this {@link IResourceLoader} can load.
         *
         * @return The id if the type. EG: image, template.
         */
        @NotNull
        @Override
        public String getTypeId()
        {
            return Constants.ResourceTypes.CONST_ITEMSTACK_RESOURCE_TYPE;
        }

        /**
         * Loads a resource for a given id and data.
         *
         * @param id   THe id to load for.
         * @param data The data to load from.
         */
        @Override
        public ItemStackResource load(@NotNull final IIdentifier id, @NotNull final JsonElement data)
        {
            try
            {
                final INBTCompound compound = INBTBase.createFromJson(data.toString());
                final IItemStack stack = IItemStack.create();

                stack.read(compound);

                return new ItemStackResource(id, stack);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(String.format("The given NBT for ItemStackResource: %s is not parsable", id), e);
            }
        }
    }

    private final IIdentifier id;
    private final IItemStack        stack;

    public ItemStackResource(final IIdentifier id, final IItemStack stack)
    {
        this.id = id;
        this.stack = stack;
    }

    /**
     * Returns the ID of the {@link IResource}.
     *
     * @return The ID of the {@link IResource}.
     */
    @NotNull
    @Override
    public IIdentifier getId()
    {
        return id;
    }

    public IItemStack getStack()
    {
        return stack.copy();
    }

    public Vector2d getScalingFactor(@NotNull final Vector2d targetSize)
    {
        if (targetSize.getX() == 0d || targetSize.getY() == 0d)
        {
            return new Vector2d(1d);
        }

        return new Vector2d(16 / targetSize.getX(), 16 / targetSize.getY());
    }
}
