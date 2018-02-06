package com.minecolonies.blockout.util.image;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

@SideOnly(Side.CLIENT)
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
            try (ImageInputStream stream = ImageIO.createImageInputStream(BlockOut.getBlockOut().getProxy().getResourceStream(resourceLocation)))
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