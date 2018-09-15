package com.ldtteam.blockout.style.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.style.core.resources.core.IDiskResource;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoader;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.ldtteam.blockout.util.Constants.Resources.CONST_IMAGE_RESOURCE_TYPE;

public class ImageResource implements IDiskResource
{

    public static final class Loader implements IResourceLoader<ImageResource>
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
            return CONST_IMAGE_RESOURCE_TYPE;
        }

        /**
         * Loads a resource for a given id and data.
         *
         * @param id   THe id to load for.
         * @param data The data to load from.
         */
        @Override
        public ImageResource load(@NotNull final ResourceLocation id, @NotNull final JsonElement data)
        {
            if (data.isJsonPrimitive())
            {
                final String diskLocationPath = data.getAsString();
                final ResourceLocation diskLocation = new ResourceLocation(diskLocationPath);
                return new ImageResource(id, diskLocation);
            }

            if (!data.isJsonObject())
            {
                throw new IllegalArgumentException(String.format("Data for ImageResource: %s is not an object or direct file link.", id));
            }

            final JsonObject object = data.getAsJsonObject();

            if (!object.has("file") || !object.get("file").isJsonPrimitive())
            {
                throw new IllegalArgumentException(String.format("File target for ImageResource: %s is not a String.", id));
            }

            final String diskLocationPath = data.getAsString();
            final ResourceLocation diskLocation = new ResourceLocation(diskLocationPath);

            final Vector2d offset;
            if (object.has("offset") && object.get("offset").isJsonArray())
            {
                final JsonArray offsetArray = object.get("offset").getAsJsonArray();
                offset = new Vector2d(offsetArray.get(0).getAsDouble(), offsetArray.get(1).getAsDouble());
            }
            else
            {
                offset = new Vector2d();
            }

            final Vector2d size;
            if (object.has("size") && object.get("size").isJsonArray())
            {
                final JsonArray sizeArray = object.get("size").getAsJsonArray();
                size = new Vector2d(sizeArray.get(0).getAsDouble(), sizeArray.get(1).getAsDouble());
            }
            else
            {
                final Vector2d imageSize = BlockOut.getBlockOut().getProxy().getImageSize(diskLocation);
                size = imageSize.move(offset.invert()).nullifyNegatives();
            }

            return new ImageResource(id, diskLocation, size, offset, size);
        }
    }

    private final ResourceLocation diskLocation;
    private final ResourceLocation id;
    private final Vector2d         size;
    private final Vector2d         offset;
    private final Vector2d         fileSize;

    public ImageResource(final ResourceLocation id, final ResourceLocation diskLocation)
    {
        this(id, diskLocation, BlockOut.getBlockOut().getProxy().getImageSize(diskLocation));
    }

    public ImageResource(final ResourceLocation id, final ResourceLocation diskLocation, final Vector2d size)
    {
        this(id, diskLocation, size, new Vector2d(), size);
    }

    public ImageResource(final ResourceLocation id, final ResourceLocation diskLocation, final Vector2d size, final Vector2d offset, final Vector2d fileSize)
    {
        this.diskLocation = diskLocation;
        this.id = id;
        this.size = size;
        this.offset = offset;
        this.fileSize = fileSize;
    }

    public static Vector2d getScalingFactory(@NotNull final ImageResource resource, final Vector2d targetSize)
    {
        return resource.getScalingFactor(targetSize);
    }

    public Vector2d getScalingFactor(@NotNull final Vector2d targetSize)
    {
        if (targetSize.getX() == 0d || targetSize.getY() == 0d)
        {
            return new Vector2d(1d);
        }

        return new Vector2d(targetSize.getX() / getSize().getX(), targetSize.getY() / getSize().getY());
    }

    public Vector2d getSize()
    {
        return size;
    }

    /**
     * Returns the location of the {@link IDiskResource} on disk.
     *
     * @return The location of the {@link IDiskResource} on disk.
     */
    @NotNull
    @Override
    public ResourceLocation getDiskLocation()
    {
        return diskLocation;
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

    public Vector2d getOffset()
    {
        return offset;
    }

    public Vector2d getFileSize()
    {
        return fileSize;
    }
}
