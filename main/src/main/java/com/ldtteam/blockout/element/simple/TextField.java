package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.compat.ClientTickManager;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.input.IKeyAcceptingUIElement;
import com.ldtteam.blockout.element.input.client.IClientSideClickAcceptingUIElement;
import com.ldtteam.blockout.element.input.client.IClientSideKeyAcceptingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.network.message.TextFieldOnEnterPressed;
import com.ldtteam.blockout.network.message.TextFieldTabPressedMessage;
import com.ldtteam.blockout.network.message.TextFieldUpdateContentsMessage;
import com.ldtteam.blockout.network.message.TextFieldUpdateSelectionEndOrCursorPositionMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

import static com.ldtteam.blockout.network.NetworkManager.sendTo;
import static com.ldtteam.blockout.network.NetworkManager.sendToServer;
import static com.ldtteam.blockout.util.Constants.Controls.TextField.*;

public class TextField extends AbstractSimpleUIElement implements IDrawableUIElement, IClientSideClickAcceptingUIElement, IClientSideKeyAcceptingUIElement
{
    /**
     * Texture resource location.
     */
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    /**
     * The current cursor position.
     */
    private int cursorPosition = 0;

    @NotNull
    public IDependencyObject<String> contents;

    /**
     * The selection end.
     */
    private int                                         selectionEnd = 0;
    @NotNull
    public  IDependencyObject<Integer>                  maxStringLength;
    @NotNull
    public  IDependencyObject<Boolean>                  doBackgroundDrawing;
    @NotNull
    public  Event<TextField, TextFieldChangedEventArgs> onTyped      = new Event<>(TextField.class, TextFieldChangedEventArgs.class);
    @NotNull
    public  Event<TextField, TextFieldEnterEventArgs>   onEnter      = new Event<>(TextField.class, TextFieldEnterEventArgs.class);
    /**
     * The scroll offset.
     */
    private int                                         scrollOffset = -1;

    /**
     * Public constructor to build textField.
     *
     * @param style  the used style.
     * @param id     the unique id.
     * @param parent the parent.
     */
    public TextField(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_TEXT_FIELD, style, id, parent);
        this.contents = DependencyObjectHelper.createFromValue("");
    }

    /**
     * Private constructor for internal use.
     *
     * @param styleId        the style.
     * @param id             the unique id.
     * @param parent         the parent.
     * @param alignments     the alginments.
     * @param dock           the dock.
     * @param margin         the margin.
     * @param elementSize    the size.
     * @param dataContext    the data context.
     * @param visible        if visible.
     * @param enabled        if enabled.
     * @param contents       the string content.
     * @param cursorPosition the cursor position.
     * @param scrollOffset   the scroll offset.
     * @param selectionEnd   the selection end.
     */
    private TextField(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<String> contents,
      @NotNull final IDependencyObject<Boolean> doBackgroundDrawing,
      @NotNull final IDependencyObject<Integer> maxStringLength,
      final int cursorPosition,
      final int scrollOffset,
      final int selectionEnd
    )
    {
        super(KEY_TEXT_FIELD, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.contents = contents;
        this.cursorPosition = cursorPosition;
        this.scrollOffset = scrollOffset;
        this.selectionEnd = selectionEnd;
        this.doBackgroundDrawing = doBackgroundDrawing;
        this.maxStringLength = maxStringLength;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (contents.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }

        if (scrollOffset == -1)
        {
            calcScrollOffset();
        }
    }

    private void calcScrollOffset()
    {
        if (ProxyHolder.getInstance().getFontRenderer() == null)
        {
            return;
        }

        final int internalWidth = getInternalWidth();
        if (internalWidth > 0)
        {
            if (scrollOffset > getContents().length())
            {
                scrollOffset = getContents().length();
            }

            final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), internalWidth);
            final int rightmostVisibleChar = visibleString.length() + scrollOffset;

            if (selectionEnd == scrollOffset)
            {
                scrollOffset -= BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents(), internalWidth, true).length();
            }

            if (selectionEnd > rightmostVisibleChar)
            {
                scrollOffset += selectionEnd - rightmostVisibleChar;
            }
            else if (selectionEnd <= scrollOffset)
            {
                scrollOffset -= scrollOffset - selectionEnd;
            }

            scrollOffset = MathHelper.clamp(scrollOffset, 0, getContents().length());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final int maxTextLength =
          (int) (getLocalBoundingBox().getSize().getX() * (int) (getLocalBoundingBox().getSize().getY() / BlockOut.getBlockOut().getProxy().getFontRenderer().FONT_HEIGHT));

        //controller.getScissoringController().focus(this);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        BoundingBox box = getLocalBoundingBox();

        final double width = box.getSize().getX();
        final double height = box.getSize().getY();
        controller.drawRect(-1, -1, width + 1, height + 1, new Color(0xFFA0A0A0));
        controller.drawRect(1, 1, width - 1, height - 1, new Color(Color.BLACK));

        final FontRenderer fontRenderer = BlockOut.getBlockOut().getProxy().getFontRenderer();

        final boolean shadow = false;
        final int color = Color.RED.getRGB(); //isEnabled() ? 0xffffff : 0xffffff;
        final double drawWidth = box.getSize().getX();
        final int drawX = 2;
        final int drawY = 2;

        //  Determine the portion of the string that is visible on screen
        final String visibleString = fontRenderer.trimStringToWidth(getContents().substring(scrollOffset), (int) drawWidth);

        final int relativeCursorPosition = cursorPosition - scrollOffset;
        int relativeSelectionEnd = selectionEnd - scrollOffset;
        final boolean cursorVisible = relativeCursorPosition >= 0 && relativeCursorPosition <= visibleString.length();
        final boolean cursorBeforeEnd = cursorPosition < getContents().length() || fontRenderer.getStringWidth(getContents()) >= maxTextLength;

        //  Enforce selection to the length limit of the visible string
        if (relativeSelectionEnd != relativeCursorPosition && relativeSelectionEnd > visibleString.length())
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
        if (getParent().getUiManager().getFocusManager().isFocusedElement(this) && cursorVisible && (ClientTickManager.getInstance().getTickCount() / 20 % 2 == 0))
        {
            int x = fontRenderer.getStringWidth(visibleString.substring(0, Math.min(visibleString.length(), relativeCursorPosition)));
            if (cursorBeforeEnd)
            {
                controller.drawRect(x, drawY, x + 1, fontRenderer.FONT_HEIGHT + 1, new Color(color));
            }
            else
            {
                controller.bindTexture(TEXTURE);
                fontRenderer.drawString("_", (float) x, drawY, color, shadow);
            }
        }

        //  Draw selection
        if (relativeSelectionEnd != relativeCursorPosition)
        {
            final int min = Math.max(0, Math.min(relativeSelectionEnd, relativeCursorPosition));
            final int max = Math.max(relativeSelectionEnd, relativeCursorPosition);
            double selectionStartX = fontRenderer.getStringWidth(visibleString.substring(0, min));
            double selectionEndX = selectionStartX + fontRenderer.getStringWidth(visibleString.substring(min, Math.min(visibleString.length(), max)));
            controller.drawRect(selectionStartX,
              drawY,
              selectionEndX,
              fontRenderer.FONT_HEIGHT + 1,
              new Color(Color.white.getRed(), Color.WHITE.getGreen(), Color.white.getBlue(), 200));
        }


        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        //controller.getScissoringController().pop();

        //doDraw(controller);
    }

    @SideOnly(Side.CLIENT)
    public void doDraw(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        final int x = (int) this.getLocalBoundingBox().getLocalOrigin().getX();
        final int y = (int) this.getLocalBoundingBox().getLocalOrigin().getY();
        final int width = (int) this.getLocalBoundingBox().getSize().getX();
        final int height = (int) this.getLocalBoundingBox().getSize().getY();
        final String contents = getContents();

        //TODO: Create bindings for colors.
        final Color outerBackgroundColor = new Color(-6250336);
        final Color innerBackgroundColor = new Color(-16777216);

        final int enabledColor = 14737632;
        final int disabledColor = 7368816;

        final Color cursorColor = new Color(-3092272);
        final Color selectionColor = new Color(Color.BLUE);

        if (shouldDrawBackground())
        {
            //controller.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, outerBackgroundColor);
            //controller.drawRect(x, y, x + width, y + height, innerBackgroundColor);
        }

        int fontColor = this.isEnabled() ? enabledColor : disabledColor;
        int cursorScrollOffset = this.cursorPosition - this.scrollOffset;
        int selectScrollOffset = this.selectionEnd - this.scrollOffset;
        String visibleString = getFontRenderer().trimStringToWidth(contents.substring(this.scrollOffset), width);
        boolean cursorVisible = cursorScrollOffset >= 0 && cursorScrollOffset <= visibleString.length();
        boolean doDrawCursor = isFocused() && getCursorCounter() / 6 % 2 == 0 && cursorVisible;
        int l = shouldDrawBackground() ? x + 4 : x;
        int i1 = shouldDrawBackground() ? y + (height - 8) / 2 : y;
        int j1 = l;

        if (selectScrollOffset > visibleString.length())
        {
            selectScrollOffset = visibleString.length();
        }

        if (!visibleString.isEmpty())
        {
            String s1 = cursorVisible ? visibleString.substring(0, cursorScrollOffset) : visibleString;
            j1 = getFontRenderer().drawStringWithShadow(s1, (float) l, (float) i1, fontColor);
        }

        boolean flag2 = this.cursorPosition < contents.length() || contents.length() >= this.getMaxStringLength();
        int k1 = j1;

        if (!cursorVisible)
        {
            k1 = cursorScrollOffset > 0 ? l + width : l;
        }
        else if (flag2)
        {
            k1 = j1 - 1;
            --j1;
        }

        if (!visibleString.isEmpty() && cursorVisible && cursorScrollOffset < visibleString.length())
        {
            j1 = getFontRenderer().drawStringWithShadow(visibleString.substring(cursorScrollOffset), (float) j1, (float) i1, fontColor);
        }

        if (doDrawCursor)
        {
            if (flag2)
            {
                controller.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + getFontRenderer().FONT_HEIGHT, cursorColor);
            }
            else
            {
                getFontRenderer().drawStringWithShadow("_", (float) k1, (float) i1, fontColor);
            }
        }

        if (selectScrollOffset != cursorScrollOffset)
        {
            int l1 = l + getFontRenderer().getStringWidth(visibleString.substring(0, selectScrollOffset));
            //this.drawSelectionBox(x, width, k1, i1 - 1, l1 - 1, i1 + 1 + getFontRenderer().FONT_HEIGHT, selectionColor);
        }


        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }

    public boolean shouldDrawBackground()
    {
        return doBackgroundDrawing.get(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
    }

    @SideOnly(Side.CLIENT)
    private FontRenderer getFontRenderer()
    {
        return BlockOut.getBlockOut().getProxy().getFontRenderer();
    }

    @SideOnly(Side.CLIENT)
    private long getCursorCounter()
    {
        return ClientTickManager.getInstance().getTickCount();
    }

    /**
     * Get the text content.
     *
     * @return the text.
     */
    public String getContents()
    {
        return contents.get(this);
    }

    /**
     * Set the text contents
     *
     * @param contents the new string to set.
     */
    public void setContents(@NotNull final String contents)
    {
        this.contents.set(this, contents);
        getParent().getUiManager().getUpdateManager().markDirty();
    }

    /**
     * Get the internal width of the textField.
     *
     * @return the width.
     */
    private int getInternalWidth()
    {
        return (int) getLocalBoundingBox().getSize().getX();
    }

    /**
     * Setter for the cursor position.
     *
     * @param pos the x pos.
     */
    public void setCursorPosition(final int pos)
    {
        cursorPosition = MathHelper.clamp(pos, 0, getContents().length());
        setSelectionEnd(cursorPosition);
        getParent().getUiManager().getUpdateManager().markDirty();
    }

    public int getMaxStringLength()
    {
        return maxStringLength.get(this);
    }

    public void setMaxStringLength(int length)
    {
        maxStringLength.set(this, length);
    }

    /**
     * Draws the blue selection box.
     */
    @SideOnly(Side.CLIENT)
    private void drawSelectionBox(int x, int width, int startX, int startY, int endX, int endY, Color selectionColor)
    {
        if (startX < endX)
        {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY)
        {
            int j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > x + width)
        {
            endX = x + width;
        }

        if (startX > x + width)
        {
            startX = x + width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        selectionColor.performOpenGLColoring();
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
        bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void setShouldDrawBackground(final boolean drawBackground)
    {
        doBackgroundDrawing.set(this, drawBackground);
    }

    /**
     * Write text into the field.
     *
     * @param str the string to write.
     */
    @SideOnly(Side.CLIENT)
    private void writeText(final String str)
    {
        final FontRenderer fontRenderer = BlockOut.getBlockOut().getProxy().getFontRenderer();
        final int maxTextLength =
          (int) (getLocalBoundingBox().getSize().getX() * (int) (getLocalBoundingBox().getSize().getY() / BlockOut.getBlockOut().getProxy().getFontRenderer().FONT_HEIGHT));


        final int insertAt = Math.min(cursorPosition, selectionEnd);
        final int insertEnd = Math.max(cursorPosition, selectionEnd);
        final int availableChars = (maxTextLength - fontRenderer.getStringWidth(getContents())) + (insertEnd - insertAt);

        if (availableChars < 0)
        {
            return;
        }

        @NotNull final StringBuilder resultBuffer = new StringBuilder();
        if (getContents().length() > 0 && insertAt > 0)
        {
            resultBuffer.append(getContents(), 0, insertAt);
        }

        final int insertedLength;
        if (availableChars < str.length())
        {
            resultBuffer.append(str, 0, availableChars);
            insertedLength = availableChars;
        }
        else
        {
            resultBuffer.append(str);
            insertedLength = str.length();
        }

        if (getContents().length() > 0 && insertEnd < getContents().length())
        {
            resultBuffer.append(getContents().substring(insertEnd));
        }

        sendToServer(new TextFieldUpdateContentsMessage(getId(), resultBuffer.toString()));

        int newCursorPos = selectionEnd + (insertAt - selectionEnd) + insertedLength;

        sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), newCursorPos, newCursorPos));
    }

    /**
     * Delete an amount of words.
     *
     * @param count the amount.
     */
    private void deleteWords(final int count)
    {
        if (getContents().length() != 0)
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
    private void deleteFromCursor(final int count)
    {
        if (getContents().length() == 0)
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
                result = getContents().substring(0, start);
            }

            if (end < getContents().length())
            {
                result = result + getContents().substring(end);
            }

            sendToServer(new TextFieldUpdateContentsMessage(getId(), result));
            if (backwards)
            {
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), selectionEnd + count, selectionEnd));
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
    private int getNthWordFromPos(final int count, final int pos)
    {
        final boolean reverse = count < 0;
        int position = pos;

        for (int i1 = 0; i1 < Math.abs(count); ++i1)
        {
            if (reverse)
            {
                while (position > 0 && getContents().charAt(position - 1) == ' ')
                {
                    --position;
                }
                while (position > 0 && getContents().charAt(position - 1) != ' ')
                {
                    --position;
                }
            }
            else
            {
                position = getContents().indexOf(' ', position);

                if (position == -1)
                {
                    position = getContents().length();
                }
                else
                {
                    while (position < getContents().length() && getContents().charAt(position) == ' ')
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
    private int getNthWordFromCursor(final int count)
    {
        return getNthWordFromPos(count, cursorPosition);
    }

    /**
     * Get the selection end.
     *
     * @return the end pos.
     */
    private int getSelectionEnd()
    {
        return selectionEnd;
    }

    /**
     * Set the end of the selection.
     *
     * @param pos the end pos.
     */
    public void setSelectionEnd(final int pos)
    {
        selectionEnd = pos;


    }

    /**
     * Get the selected text.
     *
     * @return the selected text.
     */
    @NotNull
    private String getSelectedText()
    {
        final int start = Math.min(cursorPosition, selectionEnd);
        final int end = Math.max(cursorPosition, selectionEnd);
        return getContents().substring(start, end);
    }

    /**
     * Handle key event.
     *
     * @param c   the character.
     * @param key the key.
     */
    @SideOnly(Side.CLIENT)
    private void handleKey(final char c, final KeyboardKey key)
    {
        switch (key)
        {
            case KEY_LSHIFT:
            case KEY_RSHIFT:
            case KEY_LCONTROL:
            case KEY_RCONTROL:
                break;
            case KEY_BACK:
            case KEY_DELETE:
                handleDelete(key);
                break;
            case KEY_HOME:
            case KEY_END:
                handleHomeEnd(key);
                break;
            case KEY_LEFT:
            case KEY_RIGHT:
                handleArrowKeys(key);
                break;
            case KEY_TAB:
                handleTab();
                break;
            case KEY_RETURN:
                sendToServer(new TextFieldOnEnterPressed(getId()));
                break;
            default:
                writeText(Character.toString(c));
        }
    }

    /**
     * Handle tab to jump to next control.
     */
    @SideOnly(Side.CLIENT)
    private void handleTab()
    {
        final Optional<IUIElement> optionalNextElement =
          getParent().getNextElement(this, iUiElement -> iUiElement instanceof IClientSideKeyAcceptingUIElement || iUiElement instanceof IKeyAcceptingUIElement);

        optionalNextElement
          .filter(element -> element != this)
          .ifPresent(element -> {
              sendToServer(new TextFieldTabPressedMessage(element.getId()));
          });
    }

    /**
     * Handling arrow key for textfield navigation.
     *
     * @param key the clicked key.
     */
    @SideOnly(Side.CLIENT)
    private void handleArrowKeys(final KeyboardKey key)
    {
        final int direction = (key == KeyboardKey.KEY_LEFT) ? -1 : 1;

        if (GuiScreen.isShiftKeyDown())
        {
            if (GuiScreen.isCtrlKeyDown())
            {
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition, getNthWordFromPos(direction, getSelectionEnd())));
            }
            else
            {
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition, getSelectionEnd() + direction));
            }
        }
        else if (GuiScreen.isCtrlKeyDown())
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), getNthWordFromCursor(direction), selectionEnd));
        }
        else
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), selectionEnd + direction, selectionEnd));
        }
    }

    /**
     * On home end key clicked.
     *
     * @param key the key specifics.
     */
    @SideOnly(Side.CLIENT)
    private void handleHomeEnd(final KeyboardKey key)
    {
        final int position = (key == KeyboardKey.KEY_HOME) ? 0 : getContents().length();

        if (GuiScreen.isShiftKeyDown())
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition, position));
        }
        else
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), position, getSelectionEnd()));
        }
    }

    /**
     * Handle delete action.
     *
     * @param key the pressed key.
     */
    @SideOnly(Side.CLIENT)
    private void handleDelete(final KeyboardKey key)
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
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canAcceptMouseInputClient(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onMouseClickBeginClient(final int localX, final int localY, final MouseButton button)
    {
        if (localX < 0)
        {
            return true;
        }

        final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(visibleString, localX);

        sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), trimmedString.length() + scrollOffset, selectionEnd));

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onMouseClickMoveClient(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(visibleString, localX);

        sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition, trimmedString.length() + scrollOffset));

        return true;
    }

    @Override
    public boolean canAcceptKeyInputClient(final int character, final KeyboardKey key)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onKeyPressedClient(final int character, final KeyboardKey key)
    {
        switch (character)
        {
            case 1:
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), getContents().length(), 0));
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

        return true;
    }

    public void raiseOnContentChanged()
    {
        onTyped.raise(this, new TextFieldChangedEventArgs(getContents()));
    }

    public void raiseOnEnterPressed()
    {
        onEnter.raise(this, new TextFieldEnterEventArgs(getContents()));
    }

    /**
     * Data builder for the text field.
     */
    public static class TextFieldConstructionDataBuilder extends SimpleControlConstructionDataBuilder<TextFieldConstructionDataBuilder, TextField>
    {
        public TextFieldConstructionDataBuilder(
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

        @NotNull
        public TextFieldConstructionDataBuilder withChangedEventHandler(@NotNull final IEventHandler<TextField, TextField.TextFieldChangedEventArgs> eventHandler)
        {
            return withEventHandler("onChanged", TextField.TextFieldChangedEventArgs.class, eventHandler);
        }

        @NotNull
        public TextFieldConstructionDataBuilder withEnterEventHandler(@NotNull final IEventHandler<TextField, TextField.TextFieldEnterEventArgs> eventHandler)
        {
            return withEventHandler("onEnter", TextField.TextFieldEnterEventArgs.class, eventHandler);
        }
    }

    /**
     * Element factory to instantiate the text field.
     */
    public static class Factory extends AbstractSimpleUIElementFactory<TextField>
    {
        public Factory()
        {
            super(TextField.class, KEY_TEXT_FIELD, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {

                final IDependencyObject<String> contents = elementData.getFromRawDataWithDefault(CONST_CONTENT, engine, "");
                final IDependencyObject<Boolean> doBackgroundDraw = elementData.getFromRawDataWithDefault(CONST_DO_BACK_DRAW, engine, true);
                final IDependencyObject<Integer> maxContentLenght = elementData.getFromRawDataWithDefault(CONST_MAX_LENGTH, engine, Integer.MAX_VALUE);

                final int cursorPosition = elementData.getRawWithoutBinding(CONST_CURSOR_POS, 0);
                final int scrollOffset = elementData.getRawWithoutBinding(CONST_CURSOR_SCROLL_OFF, 0);
                final int selectionEnd = elementData.getRawWithoutBinding(CONST_CURSOR_SEL_END, 0);

                final TextField element = new TextField(
                  id,
                  parent,
                  styleId,
                  alignments,
                  dock,
                  margin,
                  elementSize,
                  dataContext,
                  visible,
                  enabled,
                  contents,
                  doBackgroundDraw,
                  maxContentLenght,
                  cursorPosition,
                  scrollOffset,
                  selectionEnd
                );

                return element;
            }, (element, builder) -> {
                builder
                  .addComponent(CONST_CONTENT, element.getContents())
                  .addComponent(CONST_CURSOR_POS, element.cursorPosition)
                  .addComponent(CONST_CURSOR_SCROLL_OFF, element.scrollOffset)
                  .addComponent(CONST_CURSOR_SEL_END, element.selectionEnd)
                  .addComponent(CONST_DO_BACK_DRAW, element.shouldDrawBackground())
                  .addComponent(CONST_MAX_LENGTH, element.getMaxStringLength());
            });
        }
    }

    /**
     * Event arguments on change.
     */
    public static class TextFieldChangedEventArgs
    {
        /**
         * The content of the event.
         */
        private final String newContent;

        /**
         * Creates the event argument on text field changed
         *
         * @param newContent the new content.
         */
        public TextFieldChangedEventArgs(final String newContent)
        {
            this.newContent = newContent;
        }

        /**
         * Gets the new text field content after event.
         *
         * @return the content StringConverter.
         */
        public String getNewContent()
        {
            return newContent;
        }
    }

    /**
     * Event arguments on enter press.
     */
    public static class TextFieldEnterEventArgs
    {
        /**
         * The content of the event.
         */
        private final String newContent;

        /**
         * Creates the event argument on text field changed
         *
         * @param newContent the new content.
         */
        public TextFieldEnterEventArgs(final String newContent)
        {
            this.newContent = newContent;
        }

        /**
         * Gets the new text field content after event.
         *
         * @return the content StringConverter.
         */
        public String getNewContent()
        {
            return newContent;
        }
    }
}
