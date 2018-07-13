package com.minecolonies.blockout.render.standard;

import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.render.core.IScissoringController;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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
            renderingController.drawColoredRect(DEBUG_BOX, 100, new Color(Color.GREEN));
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
          (int) ((DISPLAYHEIGHT - box.getLowerRightCoordinate().getY()) * GUISCALE),
          (int) ((box.getSize().getX()) * GUISCALE),
          (int) ((box.getSize().getY()) * GUISCALE));
    }

    private static void calcScaleFactor()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sc = new ScaledResolution(mc);
        DISPLAYWIDTH = sc.getScaledWidth();
        DISPLAYHEIGHT = sc.getScaledHeight();
        GUISCALE = sc.getScaleFactor();
        DEBUG_BOX = new BoundingBox(new Vector2d(), new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT));
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
