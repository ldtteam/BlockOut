package com.minecolonies.blockout.render.standard;

import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.render.core.IScissoringController;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

@SideOnly(Side.CLIENT)
public class ScissoringController implements IScissoringController
{
    private static BoundingBox DEBUG_BOX = new BoundingBox();

    private static Random random = new Random(12345);
    private static int DISPLAYHEIGHT;
    private static int DISPLAYWIDTH;
    private static int GUISCALE;


    @NotNull
    private final Deque<BoundingBox> scissorsQueue     = new ConcurrentLinkedDeque<>();
    @NotNull
    private final Deque<Color>       scissorDebugColor = new ConcurrentLinkedDeque<>();

    @NotNull
    private final IRenderingController renderingController;
    private boolean _debugEnabled = false;

    public ScissoringController(@NotNull final IRenderingController renderingController)
    {
        this.renderingController = renderingController;
    }

    @Override
    public void push(@NotNull final BoundingBox box)
    {
        BoundingBox scissorBox = new BoundingBox(box);

        if (!scissorsQueue.isEmpty())
        {
            scissorBox = scissorBox.intersect(scissorsQueue.peekFirst());
            disableScissor();
        }

        scissorsQueue.addFirst(scissorBox);
        scissorDebugColor.addFirst(generateNewDebugDrawColor());
        enableScissor(scissorBox);

        if (_debugEnabled)
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            renderingController.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            renderingController.drawTexturedModalRect(new Vector2d(-10, -10),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT));
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
        }
    }

    private static void disableScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopAttrib();
    }

    @NotNull
    private Color generateNewDebugDrawColor()
    {
        Color color = new Color(random.nextInt());

        int attempts = 0;
        while (scissorDebugColor.contains(color) && attempts < 100)
        {
            attempts++;
            color = new Color(random.nextInt());
        }

        return color;
    }

    private static void enableScissor(@NotNull BoundingBox box)
    {
        calcScaleFactor();

        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (box.getUpperLeftCoordinate().getX() * GUISCALE),
          (int) ((DISPLAYHEIGHT - box.getUpperRightCoordinate().getY()) * GUISCALE),
          (int) ((box.getSize().getX()) * GUISCALE),
          (int) ((box.getSize().getY()) * GUISCALE));
        //GL11.glScissor(500,500,100,100);
    }

    private static void calcScaleFactor()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sc = new ScaledResolution(mc);
        DISPLAYWIDTH = sc.getScaledWidth();
        DISPLAYHEIGHT = sc.getScaledHeight();
        GUISCALE = sc.getScaleFactor();
        DEBUG_BOX = new BoundingBox(new Vector2d(-10000, -10000), new Vector2d(20000, 20000));
    }

    @Override
    public void pop()
    {
        if (scissorsQueue.isEmpty())
        {
            disableScissor();
            Log.getLogger().error("Cannot pop scissor box from empty stack!");
            return;
        }

        scissorsQueue.removeFirst();
        scissorDebugColor.removeFirst();

        disableScissor();

        if (!scissorsQueue.isEmpty())
        {
            enableScissor(scissorsQueue.peekFirst());
        }
    }

    @Override
    public void setDebugDrawingEnabled(final boolean enabled)
    {
        _debugEnabled = enabled;
    }
}
