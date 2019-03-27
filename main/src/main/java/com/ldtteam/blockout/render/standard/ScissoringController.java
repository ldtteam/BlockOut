package com.ldtteam.blockout.render.standard;

import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.core.IScissoringController;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
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
            IOpenGl.blendFunc(SourceFactor.SRC_ALPHA, DestinationFactor.ONE_MINUS_SRC_ALPHA);
            renderingController.bindTexture(ISpriteMap.getLocationOfBlocksTexture());
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
        IOpenGl.disableScissor();
    }

    @NotNull
    private IColor generateNewDebugDrawColor()
    {
        IColor color = IColor.create(random.nextInt());

        int attempts = 0;
        while (scissorDebugColor.contains(color) && attempts < 100)
        {
            attempts++;
            color = IColor.create(random.nextInt());
        }

        return color;
    }

    private static void enableScissor(@NotNull BoundingBox box)
    {
        calcScaleFactor();

        IOpenGl.enableScissor((int) (box.getUpperLeftCoordinate().getX() * GUISCALE),
          (int) ((DISPLAYHEIGHT - box.getUpperRightCoordinate().getY()) * GUISCALE),
          (int) ((box.getSize().getX()) * GUISCALE),
          (int) ((box.getSize().getY()) * GUISCALE));
    }

    private static void calcScaleFactor()
    {
        IGameEngine mc = IGameEngine.getInstance();
        IScaledResolution sc = IScaledResolution.create(mc);
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
