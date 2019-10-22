package com.ldtteam.blockout.util.image;

import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

public final class ImageUtil
{

    private ImageUtil()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Load and image from a {@link ResourceLocation} and return a {@link Vector2d} containing its width and height.
     *
     * @param resourceLocation The {@link ResourceLocation} pointing to the image.
     * @return Width and height.
     */
    public static Vector2d getImageDimensions(final ResourceLocation resourceLocation)
    {
        final Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix("png");
        if (it.hasNext())
        {
            final ImageReader reader = it.next();
            try (ImageInputStream stream = ImageIO.createImageInputStream(ProxyHolder.getInstance().getResourceStream(resourceLocation)))
            {
                reader.setInput(stream);

                return new Vector2d(reader.getWidth(reader.getMinIndex()), reader.getHeight(reader.getMinIndex()));
            }
            catch (Exception e)
            {
                Log.getLogger().error(e);
            }
            finally
            {
                reader.dispose();
            }
        }

        return new Vector2d();
    }
}