package com.ldtteam.blockout.render.standard;

import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.core.IScissoringController;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ScissoringController implements IScissoringController
{
    private static BoundingBox DEBUG_BOX = new BoundingBox();

    private static Random random = new Random(12345);
    private static int    DISPLAYHEIGHT;
    private static int    DISPLAYWIDTH;
    private static double    GUISCALE;

    @NotNull
    private final Deque<BoundingBox> scissorsQueue     = new ConcurrentLinkedDeque<>();
    @NotNull
    private final Deque<Color>       scissorDebugColor = new ConcurrentLinkedDeque<>();

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
            RenderSystem.pushMatrix();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            renderingController.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            renderingController.drawTexturedModalRect(new Vector2d(-10, -10),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT),
              new Vector2d(DISPLAYWIDTH, DISPLAYHEIGHT));
            RenderSystem.disableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.popMatrix();
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

        enableScissor((int) (box.getUpperLeftCoordinate().getX() * GUISCALE),
          (int) ((DISPLAYHEIGHT - box.getUpperRightCoordinate().getY()) * GUISCALE),
          (int) ((box.getSize().getX()) * GUISCALE),
          (int) ((box.getSize().getY()) * GUISCALE));
    }

    private static void enableScissor(final int x, final int y, final int w, final int h)
    {
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, w, h);
    }

    private static void calcScaleFactor()
    {
        Minecraft mc = Minecraft.getInstance();
        MainWindow window = mc.getMainWindow();
        DISPLAYWIDTH = window.getScaledWidth();
        DISPLAYHEIGHT = window.getScaledHeight();
        GUISCALE = window.getGuiScaleFactor();
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
