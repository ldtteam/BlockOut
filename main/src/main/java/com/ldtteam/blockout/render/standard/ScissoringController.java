package com.ldtteam.blockout.render.standard;

import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.core.IScissoringController;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.client.renderer.opengl.IOpenGl;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ScissoringController implements IScissoringController
{
    private static BoundingBox DEBUG_BOX = new BoundingBox();

    private static Random random = new Random(12345);
    private static int    DISPLAYHEIGHT;
    private static int    DISPLAYWIDTH;
    private static int    GUISCALE;

    @NotNull
    private final Deque<BoundingBox> scissorsQueue     = new ConcurrentLinkedDeque<>();
    @NotNull
    private final Deque<IColor>      scissorDebugColor = new ConcurrentLinkedDeque<>();

    @NotNull
    private final IRenderingController renderingController;
    private       boolean              _debugEnabled = false;

    public ScissoringController(@NotNull final IRenderingController renderingController)
    {
        this.renderingController = renderingController;
    }

    @Override
    public void push(@NotNull final BoundingBox box)
    {
        BoundingBox scissorBox = box.scale(renderingController.getRenderingScalingFactor());

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
            IOpenGl.pushMatrix();
            IOpenGl.enableAlpha();
            IOpenGl.enableBlend();
            IOpenGl.blendFunc(IOpenGl.SourceFactor.SRC_ALPHA, IOpenGl.DestFactor.ONE_MINUS_SRC_ALPHA);
            renderingController.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            renderingController.drawTexturedModalRect(new Vector2d(-10, -10),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT));
            IOpenGl.disableBlend();
            IOpenGl.disableAlpha();
            IOpenGl.popMatrix();
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
