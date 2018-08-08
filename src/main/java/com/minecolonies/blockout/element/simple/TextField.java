package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.compat.ClientTickManager;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.input.IKeyAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.core.resources.core.IResource;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.TextField.*;

public class TextField extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement, IKeyAcceptingUIElement
{
    private static TextField focus = null;
    /**
     * Texture resource location.
     */
    private static final ResourceLocation TEXTURE           = new ResourceLocation("textures/gui/widgets.png");
    private static final int RECT_COLOR = -3_092_272;
    private static final int DEFAULT_MAX_TEXT_LENGTH = 32;

    //  Attributes
    protected            int              maxTextLength     = DEFAULT_MAX_TEXT_LENGTH;

    @Nullable
    protected            String           tabNextPaneID     = null;

    //  Runtime
    protected            String           text              = "";
    protected int cursorPosition     = 0;
    protected int scrollOffset       = 0;
    protected int selectionEnd       = 0;

    @NotNull
    private IDependencyObject<String> contents;

    public TextField(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_TEXT_FIELD, style, id, parent);

        this.contents = DependencyObjectHelper.createFromValue("");
    }

    public TextField(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<String> contents,
      @NotNull final IDependencyObject<Integer> curser)
    {
        super(KEY_TEXT_FIELD, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.contents = contents;
        this.text = contents.get(getDataContext());
        this.cursorPosition = curser.get(getDataContext());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        controller.getScissoringController().focus(this);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        BoundingBox box = getLocalBoundingBox();

        final double width = box.getSize().getX();
        final double height = box.getSize().getY();
        drawRect(0 - 1, 0 - 1, 0 + width + 1, 0 + height + 1, 0xFFA0A0A0);
        drawRect(0, 0, 0 + width, 0 + height, 0xFF000000);

        final FontRenderer fontRenderer = BlockOut.getBlockOut().getProxy().getFontRenderer();
        fontRenderer.drawSplitString(getContents(), 0,2, (int) getElementSize().getX(), 0xffffff);

        final boolean shadow = false;
        final int color = isEnabled() ? 0xffffff : 0xffffff;
        final double drawWidth = box.getSize().getX();
        final int drawX = 0;
        final int drawY = 2;

        //  Determine the portion of the string that is visible on screen
        final String visibleString = fontRenderer.trimStringToWidth(getContents().substring(scrollOffset), (int) drawWidth);

        final int relativeCursorPosition = cursorPosition - scrollOffset;
        int relativeSelectionEnd = selectionEnd - scrollOffset;
        final boolean cursorVisible = relativeCursorPosition >= 0 && relativeCursorPosition <= visibleString.length();
        final boolean cursorBeforeEnd = cursorPosition < getContents().length() || getContents().length() >= maxTextLength;

        //  Enforce selection to the length limit of the visible string
        if (relativeSelectionEnd > visibleString.length())
        {
            relativeSelectionEnd = visibleString.length();
        }

        //  Draw string up through cursor
        int textX = drawX;
        if (visibleString.length() > 0)
        {
            @NotNull final String s1 = cursorVisible ? visibleString.substring(0, relativeCursorPosition) : visibleString;
            controller.bindTexture(TEXTURE);
            textX = fontRenderer.drawString(s1, textX, drawY, color, shadow);
        }

        //  Draw string after cursor
        if (visibleString.length() > 0 && cursorVisible && relativeCursorPosition < visibleString.length())
        {
            controller.bindTexture(TEXTURE);
            fontRenderer.drawString(visibleString.substring(relativeCursorPosition), textX, drawY, color, shadow);
        }

        //  Should we draw the cursor this frame?
        if (cursorVisible && (ClientTickManager.getInstance().getTickCount() / 20 % 2 == 0))
        {
            int x = fontRenderer.getStringWidth(getContents().substring(0, Math.min(getContents().length(), cursorPosition)));
            if (cursorBeforeEnd)
            {
                drawRect(x, drawY, x+1, fontRenderer.FONT_HEIGHT+1, RECT_COLOR);
            }
            else
            {
                controller.bindTexture(TEXTURE);
                fontRenderer.drawString("_", (float) x, drawY, color, shadow);
            }
        }

        //  Draw selection
        if (relativeSelectionEnd != relativeCursorPosition && false)
        {
            final int selectedDrawX = drawX + fontRenderer.getStringWidth(visibleString.substring(0, relativeSelectionEnd));

            double selectionStartX = Math.min(cursorPosition, selectedDrawX - 1);
            double selectionEndX = Math.max(cursorPosition, selectedDrawX - 1);

            if (selectionStartX > (width))
            {
                selectionStartX = width;
            }

            if (selectionEndX > (width))
            {
                selectionEndX = width;
            }

            final Tessellator tessellator = Tessellator.getInstance();
            GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.enableColorLogic();
            GlStateManager.colorLogicOp(GL11.GL_OR_REVERSE);
            final BufferBuilder vertexBuffer = tessellator.getBuffer();

            // There are several to choose from, look at DefaultVertexFormats for more info
            vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            //Since our points do not have any u,v this seems to be the correct code
            vertexBuffer.pos(selectionStartX, (double) drawY + 1 + fontRenderer.FONT_HEIGHT, 0.0D).endVertex();
            vertexBuffer.pos(selectionEndX, (double) drawY + 1 + fontRenderer.FONT_HEIGHT, 0.0D).endVertex();
            vertexBuffer.pos(selectionEndX, (double) drawY - 1, 0.0D).endVertex();
            vertexBuffer.pos(selectionStartX, (double) drawY - 1, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.disableColorLogic();
            GlStateManager.enableTexture2D();
        }


        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        controller.getScissoringController().pop();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
    }

    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public String getContents()
    {
        return text;
    }

    public void setContents(String contents)
    {
        this.contents = DependencyObjectHelper.createFromValue(contents);
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {

    }

    public int getInternalWidth()
    {
        return (int) getLocalBoundingBox().getSize().getX();
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        if (localX < 0)
        {
            return;
        }

        final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(text.substring(scrollOffset), getInternalWidth());
        final String trimmedString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(visibleString, localX);

        // Cache and restore scrollOffset when we change focus via click,
        // because onFocus() sets the cursor (and thus scroll offset) to the end.
        final int oldScrollOffset = scrollOffset;
        setFocus();
        scrollOffset = oldScrollOffset;
        setCursorPosition(trimmedString.length() + scrollOffset);
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {

    }

    @Override
    public boolean canAcceptKeyInput(final int character, final KeyboardKey key)
    {
        return true;
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        switch (character)
        {
            case 1:
                setCursorPosition(text.length());
                setSelectionEnd(0);
                break;

            case 3:
                GuiScreen.setClipboardString(getSelectedText());
                break;
            case 22:
                writeText(GuiScreen.getClipboardString());
                break;
            case 24:
                GuiScreen.setClipboardString(getSelectedText());
                writeText("");
                break;
            default:
                handleKey((char) character, key);
        }
    }

    /**
     * Write text into the field.
     *
     * @param str the string to write.
     */
    public void writeText(final String str)
    {
        final int insertAt = Math.min(cursorPosition, selectionEnd);
        final int insertEnd = Math.max(cursorPosition, selectionEnd);
        final int availableChars = (maxTextLength - text.length()) + (insertEnd - insertAt);

        if (availableChars < 0)
        {
            return;
        }

        @NotNull final StringBuilder resultBuffer = new StringBuilder();
        if (text.length() > 0 && insertAt > 0)
        {
            resultBuffer.append(text.substring(0, insertAt));
        }

        final int insertedLength;
        if (availableChars < str.length())
        {
            resultBuffer.append(str.substring(0, availableChars));
            insertedLength = availableChars;
        }
        else
        {
            resultBuffer.append(str);
            insertedLength = str.length();
        }

        if (text.length() > 0 && insertEnd < text.length())
        {
            resultBuffer.append(text.substring(insertEnd));
        }

        text = resultBuffer.toString();
        moveCursorBy((insertAt - selectionEnd) + insertedLength);
    }

    /**
     * Delete an amount of words.
     *
     * @param count the amount.
     */
    public void deleteWords(final int count)
    {
        if (text.length() != 0)
        {
            if (selectionEnd != cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                deleteFromCursor(this.getNthWordFromCursor(count) - this.cursorPosition);
            }
        }
    }

    /**
     * Delete amount of words from cursor.
     *
     * @param count the amount.
     */
    public void deleteFromCursor(final int count)
    {
        if (text.length() == 0)
        {
            return;
        }

        if (selectionEnd != cursorPosition)
        {
            this.writeText("");
        }
        else
        {
            final boolean backwards = count < 0;
            final int start = backwards ? (this.cursorPosition + count) : this.cursorPosition;
            final int end = backwards ? this.cursorPosition : (this.cursorPosition + count);
            @NotNull String result = "";

            if (start > 0)
            {
                result = text.substring(0, start);
            }

            if (end < text.length())
            {
                result = result + text.substring(end);
            }

            text = result;

            if (backwards)
            {
                this.moveCursorBy(count);
            }
        }
    }

    /**
     * Get the n'th word from a position.
     *
     * @param count the n.
     * @param pos   the position.
     * @return the length of the word.
     */
    public int getNthWordFromPos(final int count, final int pos)
    {
        final boolean reverse = count < 0;
        int position = pos;

        for (int i1 = 0; i1 < Math.abs(count); ++i1)
        {
            if (reverse)
            {
                while (position > 0 && text.charAt(position - 1) == ' ')
                {
                    --position;
                }
                while (position > 0 && text.charAt(position - 1) != ' ')
                {
                    --position;
                }
            }
            else
            {
                position = text.indexOf(' ', position);

                if (position == -1)
                {
                    position = text.length();
                }
                else
                {
                    while (position < text.length() && text.charAt(position) == ' ')
                    {
                        ++position;
                    }
                }
            }
        }

        return position;
    }

    /**
     * Get n'th word from cursor position.
     *
     * @param count the n.
     * @return the length.
     */
    public int getNthWordFromCursor(final int count)
    {
        return getNthWordFromPos(count, cursorPosition);
    }

    public void setCursorPosition(final int pos)
    {
        cursorPosition = MathHelper.clamp(pos, 0, text.length());
        setSelectionEnd(cursorPosition);
    }

    /**
     * Move the cursor by an offset.
     *
     * @param offset the offset.
     */
    public void moveCursorBy(final int offset)
    {
        setCursorPosition(selectionEnd + offset);
    }

    public int getSelectionEnd()
    {
        return selectionEnd;
    }

    public void setSelectionEnd(final int pos)
    {
        selectionEnd = MathHelper.clamp(pos, 0, text.length());

        final int internalWidth = getInternalWidth();
        if (internalWidth > 0)
        {
            if (scrollOffset > text.length())
            {
                scrollOffset = text.length();
            }

            final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(text.substring(scrollOffset), internalWidth);
            final int rightmostVisibleChar = visibleString.length() + scrollOffset;

            if (selectionEnd == scrollOffset)
            {
                scrollOffset -= BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(text, internalWidth, true).length();
            }

            if (selectionEnd > rightmostVisibleChar)
            {
                scrollOffset += selectionEnd - rightmostVisibleChar;
            }
            else if (selectionEnd <= scrollOffset)
            {
                scrollOffset -= scrollOffset - selectionEnd;
            }

            scrollOffset = MathHelper.clamp(scrollOffset, 0, text.length());
        }
    }

    @NotNull
    public String getSelectedText()
    {
        final int start = Math.min(cursorPosition, selectionEnd);
        final int end = Math.max(cursorPosition, selectionEnd);
        return text.substring(start, end);
    }

    /**
     * Handle key event.
     *
     * @param c   the character.
     * @param key the key.
     * @return if it should be processed or not.
     */
    private boolean handleKey(final char c, final KeyboardKey key)
    {
        Log.getLogger().warn(key.name());
        switch (key)
        {
            case KEY_LSHIFT:
            case KEY_RSHIFT:
                return true;
            case KEY_BACK:
            case KEY_DELETE:
                return handleDelete(key);
            case KEY_HOME:
            case KEY_END:
                return handleHomeEnd(key);
            case KEY_LEFT:
            case KEY_RIGHT:
                return handleArrowKeys(key);
            case KEY_TAB:
                return handleTab();
            default:
                return handleChar(c);
        }
    }

    private boolean handleChar(final char c)
    {
        writeText(Character.toString(c));
        return true;
    }

    private boolean handleTab()
    {
        if (tabNextPaneID != null)
        {
            /* todo get next child in parent
            final Pane next = getWindow().findPaneByID(tabNextPaneID);
            if (next != null)
            {
                next.setFocus();
            }*/
        }
        return true;
    }

    private boolean handleArrowKeys(final KeyboardKey key)
    {
        final int direction = (key == KeyboardKey.KEY_LEFT) ? -1 : 1;

        if (GuiScreen.isShiftKeyDown())
        {
            if (GuiScreen.isCtrlKeyDown())
            {
                setSelectionEnd(getNthWordFromPos(direction, getSelectionEnd()));
            }
            else
            {
                setSelectionEnd(getSelectionEnd() + direction);
            }
        }
        else if (GuiScreen.isCtrlKeyDown())
        {
            setCursorPosition(getNthWordFromCursor(direction));
        }
        else
        {
            moveCursorBy(direction);
        }
        return true;
    }

    private boolean handleHomeEnd(final KeyboardKey key)
    {
        final int position = (key == KeyboardKey.KEY_HOME) ? 0 : text.length();

        if (GuiScreen.isShiftKeyDown())
        {
            setSelectionEnd(position);
        }
        else
        {
            setCursorPosition(position);
        }
        return true;
    }

    private boolean handleDelete(final KeyboardKey key)
    {
        final int direction = (key == KeyboardKey.KEY_BACK) ? -1 : 1;

        if (GuiScreen.isCtrlKeyDown())
        {
            deleteWords(direction);
        }
        else
        {
            deleteFromCursor(direction);
        }

        return true;
    }
    
    /**
     * Set Focus to this Pane.
     */
    public final void setFocus()
    {
        setFocus(this);
    }

    /**
     * Return <tt>true</tt> if this Pane is the current focus.
     *
     * @return <tt>true</tt> if this Pane is the current focus.
     */
    public final synchronized boolean isFocus()
    {
        return focus == this;
    }

    public void onFocus()
    {
        setCursorPosition(getContents().length());
    }

    /**
     * Set the currently focused Pane.
     *
     * @param f Pane to focus, or nil.
     */
    public static synchronized void setFocus(final TextField f)
    {
        if (focus != null)
        {
            focus = null;
        }

        focus = f;

        if (focus != null)
        {
            focus.onFocus();
        }
    }

    @NotNull
    @Override
    public <T extends IResource> T getResource(final ResourceLocation resourceId) throws IllegalArgumentException
    {
        return null;
    }
    
    public static class TextFieldConstructionDataBuilder extends SimpleControlConstructionDataBuilder<TextFieldConstructionDataBuilder, TextField>
    {

        protected TextFieldConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, TextField.class);
        }

        @NotNull
        public TextFieldConstructionDataBuilder withDependentContents(@NotNull final IDependencyObject<String> contents)
        {
            return withDependency("contents", contents);
        }
    }

    public static class Factory implements IUIElementFactory<TextField>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_TEXT_FIELD;
        }

        @NotNull
        @Override
        public TextField readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<String> contents = elementData.getBoundStringAttribute(CONST_CONTENT);
            final IDependencyObject<Integer> curser = elementData.getBoundIntegerAttribute(CONST_CURSER);

            return new TextField(
              style,
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              dataContext,
              visible,
              enabled,
              contents,
              curser
            );
        }

        @Override
        public void writeToElementData(@NotNull final TextField element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addString(CONST_CONTENT, element.getContents())
              .addInteger(CONST_CURSER, element.cursorPosition);
        }
    }
}
