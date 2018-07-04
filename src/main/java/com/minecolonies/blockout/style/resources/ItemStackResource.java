package com.minecolonies.blockout.style.resources;

import com.google.gson.JsonElement;
import com.minecolonies.blockout.style.core.resources.core.IResource;
import com.minecolonies.blockout.style.core.resources.loader.IResourceLoader;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
            return Constants.Resources.CONST_ITEMSTACK_RESOURCE_TYPE;
        }

        /**
         * Loads a resource for a given id and data.
         *
         * @param id   THe id to load for.
         * @param data The data to load from.
         */
        @Override
        public ItemStackResource load(@NotNull final ResourceLocation id, @NotNull final JsonElement data)
        {
            try
            {
                final NBTTagCompound compound = JsonToNBT.getTagFromJson(data.toString());
                final ItemStack stack = new ItemStack(compound);

                return new ItemStackResource(id, stack);
            }
            catch (NBTException e)
            {
                throw new IllegalArgumentException(String.format("The given NBT for ItemStackResource: %s is not parsable", id), e);
            }
        }
    }

    private final ResourceLocation id;
    private final ItemStack        stack;

    public ItemStackResource(final ResourceLocation id, final ItemStack stack)
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
    public ResourceLocation getId()
    {
        return id;
    }

    public ItemStack getStack()
    {
        return stack.copy();
    }
}
