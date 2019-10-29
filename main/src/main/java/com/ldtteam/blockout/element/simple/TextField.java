package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.compat.ClientTickManager;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
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
import com.ldtteam.blockout.proxy.IFontRendererProxy;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.color.ColorUtils;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractSimpleUIElement;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

import static com.ldtteam.blockout.network.NetworkManager.sendToServer;
import static com.ldtteam.blockout.util.Constants.Controls.TextField.*;

public class TextField extends AbstractSimpleUIElement implements IDrawableUIElement, IClientSideClickAcceptingUIElement, IClientSideKeyAcceptingUIElement
{
    /**
     * The current cursor position.
     */
    private int cursorPosition = 0;

    @NotNull
    public IDependencyObject<String> contents;

    /**
     * The selection end.
     */
    private int                                         selectionEnd        = 0;
    @NotNull
    public  IDependencyObject<Integer>                  maxStringLength     = DependencyObjectHelper.createFromValue(Integer.MAX_VALUE);
    @NotNull
    public  IDependencyObject<Boolean>                  doBackgroundDrawing = DependencyObjectHelper.createFromValue(true);
    @NotNull
    public  Event<TextField, TextFieldChangedEventArgs> onTyped             = new Event<>(TextField.class, TextFieldChangedEventArgs.class);
    @NotNull
    public  Event<TextField, TextFieldEnterEventArgs>   onEnter             = new Event<>(TextField.class, TextFieldEnterEventArgs.class);

    @NotNull
    public IDependencyObject<String> outerBackgroundColor = DependencyObjectHelper.createFromValue("-6250336");
    @NotNull
    public IDependencyObject<String> innerBackgroundColor = DependencyObjectHelper.createFromValue("-16777216");
    @NotNull
    public IDependencyObject<String> enabledColor         = DependencyObjectHelper.createFromValue("14737632");
    @NotNull
    public IDependencyObject<String> disabledColor        = DependencyObjectHelper.createFromValue("7368816");
    @NotNull
    public IDependencyObject<String> cursorColor          = DependencyObjectHelper.createFromValue("-3092272");
    @NotNull
    public IDependencyObject<String> selectionColor       = DependencyObjectHelper.createFromValue("Blue");


    /**
     * The scroll offset.
     */
    private int                                         scrollOffset = -1;

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
      @NotNull final IDependencyObject<String> outerBackgroundColor,
      @NotNull final IDependencyObject<String> innerBackgroundColor,
      @NotNull final IDependencyObject<String> enabledColor,
      @NotNull final IDependencyObject<String> disabledColor,
      @NotNull final IDependencyObject<String> cursorColor,
      @NotNull final IDependencyObject<String> selectionColor,
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
        this.outerBackgroundColor = outerBackgroundColor;
        this.innerBackgroundColor = innerBackgroundColor;
        this.enabledColor = enabledColor;
        this.disabledColor = disabledColor;
        this.cursorColor = cursorColor;
        this.selectionColor = selectionColor;
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

            final String visibleString = ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), internalWidth);
            final int rightmostVisibleChar = visibleString.length() + scrollOffset;

            if (selectionEnd == scrollOffset)
            {
                scrollOffset -= ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(getContents(), internalWidth, true).length();
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

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        controller.getScissoringController().focus(this);

        doDraw(controller);

        controller.getScissoringController().pop();
    }

    public void doDraw(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        final int x = 0;
        final int y = 0;
        final int width = (int) this.getLocalBoundingBox().getSize().getX();
        final int height = (int) this.getLocalBoundingBox().getSize().getY();
        final String contents = getContents();

        final Color outerBackgroundColor = ColorUtils.convertToColor(getOuterBackgroundColor());
        final Color innerBackgroundColor = ColorUtils.convertToColor(getInnerBackgroundColor());

        final Color enabledColor = ColorUtils.convertToColor(getEnabledColor());
        final Color disabledColor = ColorUtils.convertToColor(getDisabledColor());

        final Color cursorColor = ColorUtils.convertToColor(getCursorColor());
        final Color selectionColor = ColorUtils.convertToColor(getSelectionColor());

        if (shouldDrawBackground())
        {
            controller.drawRect(x, y, x + width, y + height, outerBackgroundColor);
            controller.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, innerBackgroundColor);

        }

        Color fontColor = this.isEnabled() ? enabledColor : disabledColor;
        int cursorScrollOffset = this.cursorPosition - this.scrollOffset;
        int selectScrollOffset = this.selectionEnd - this.scrollOffset;
        String visibleString = getFontRenderer().trimStringToWidth(contents.substring(this.scrollOffset), width);
        boolean cursorVisible = cursorScrollOffset >= 0 && cursorScrollOffset <= visibleString.length();
        boolean doDrawCursor = isFocused() && getCursorCounter() / 6 % 2 == 0 && cursorVisible;
        int drawStartX = shouldDrawBackground() ? x + 4 : x;
        int drawStartY = shouldDrawBackground() ? y + (height - 8) / 2 : y;
        int drawCurrentX = drawStartX;

        if (selectScrollOffset > visibleString.length())
        {
            selectScrollOffset = visibleString.length();
        }

        if (!visibleString.isEmpty())
        {
            String s1 = cursorVisible ? visibleString.substring(0, cursorScrollOffset) : visibleString;
            drawCurrentX = getFontRenderer().drawStringWithShadow(s1, (float) drawStartX, (float) drawStartY, fontColor.getRGB());
        }

        boolean drawFullCursor = this.cursorPosition < contents.length() || contents.length() >= this.getMaxStringLength();
        int cursorDrawX = drawCurrentX;

        if (!cursorVisible)
        {
            cursorDrawX = cursorScrollOffset > 0 ? drawStartX + width : drawStartX;
        }
        else if (drawFullCursor)
        {
            cursorDrawX = drawCurrentX - 1;
            --drawCurrentX;
        }

        if (!visibleString.isEmpty() && cursorVisible && cursorScrollOffset < visibleString.length())
        {
            getFontRenderer().drawStringWithShadow(visibleString.substring(cursorScrollOffset), (float) drawCurrentX, (float) drawStartY, fontColor.getRGB());
        }

        if (doDrawCursor)
        {
            if (drawFullCursor)
            {
                controller.drawRect(cursorDrawX, drawStartY - 1, cursorDrawX + 1, drawStartY + 1 + getFontRenderer().getFontHeight(), cursorColor);
            }
            else
            {
                getFontRenderer().drawStringWithShadow("_", (float) cursorDrawX, (float) drawStartY, cursorColor.getRGB());
            }
        }

        if (selectScrollOffset != cursorScrollOffset)
        {
            int selectionDrawEnd = drawStartX + getFontRenderer().getStringWidth(visibleString.substring(0, selectScrollOffset));
            this.drawSelectionBox(x, width, cursorDrawX, drawStartY - 1, selectionDrawEnd - 1, drawStartY + 1 + getFontRenderer().getFontHeight(), selectionColor);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    public boolean shouldDrawBackground()
    {
        return doBackgroundDrawing.get(this);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
    }

    private IFontRendererProxy getFontRenderer()
    {
        return IProxy.getInstance().getFontRenderer();
    }

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

    public String getOuterBackgroundColor()
    {
        return this.outerBackgroundColor.get(this);
    }

    public void setOuterBackgroundColor(String value)
    {
        this.outerBackgroundColor.set(this, value);
    }

    public String getInnerBackgroundColor()
    {
        return this.innerBackgroundColor.get(this);
    }

    public void setInnerBackgroundColor(String value)
    {
        this.innerBackgroundColor.set(this, value);
    }

    public String getEnabledColor()
    {
        return this.enabledColor.get(this);
    }

    public void setEnabledColor(String value)
    {
        this.enabledColor.set(this, value);
    }

    public String getDisabledColor()
    {
        return this.disabledColor.get(this);
    }

    public void setDisabledColor(String value)
    {
        this.disabledColor.set(this, value);
    }

    public String getCursorColor()
    {
        return this.cursorColor.get(this);
    }

    public void setCursorColor(String value)
    {
        this.cursorColor.set(this, value);
    }

    public String getSelectionColor()
    {
        return this.selectionColor.get(this);
    }

    public void setSelectionColor(String value)
    {
        this.selectionColor.set(this, value);
    }
    /**
     * Draws the blue selection box.
     */
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
        GlStateManager.disableTexture();
        GlStateManager.enableColorLogicOp();
        GlStateManager.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
        bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogicOp();
        GlStateManager.enableTexture();
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
    private void writeText(final String str)
    {
        final IFontRendererProxy fontRenderer = ProxyHolder.getInstance().getFontRenderer();
        final int maxTextLength =
          (int) (getLocalBoundingBox().getSize().getX() * (int) (getLocalBoundingBox().getSize().getY() / ProxyHolder.getInstance().getFontRenderer().getFontHeight()));


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
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), selectionEnd + count, selectionEnd+count));
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
    private void handleArrowKeys(final KeyboardKey key)
    {
        final int direction = (key == KeyboardKey.KEY_LEFT) ? -1 : 1;

        if (Screen.hasShiftDown())
        {
            if (Screen.hasControlDown())
            {
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), getNthWordFromCursor(direction), getSelectionEnd()));
            }
            else
            {
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition + direction, getSelectionEnd()));
            }
        }
        else if (Screen.hasControlDown())
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), getNthWordFromCursor(direction), getNthWordFromCursor(direction)));
        }
        else
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition + direction, cursorPosition + direction));
        }
    }

    /**
     * On home end key clicked.
     *
     * @param key the key specifics.
     */
    private void handleHomeEnd(final KeyboardKey key)
    {
        final int position = (key == KeyboardKey.KEY_HOME) ? 0 : getContents().length();

        if (Screen.hasShiftDown())
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), position, getSelectionEnd()));
        }
        else
        {
            sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), position, position));
        }
    }

    /**
     * Handle delete action.
     *
     * @param key the pressed key.
     */
    private void handleDelete(final KeyboardKey key)
    {
        final int direction = (key == KeyboardKey.KEY_BACK) ? -1 : 1;

        if (Screen.hasControlDown())
        {
            deleteWords(direction);
        }
        else
        {
            deleteFromCursor(direction);
        }
    }

    @Override
    public boolean canAcceptMouseInputClient(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @Override
    public boolean onMouseClickBeginClient(final int localX, final int localY, final MouseButton button)
    {
        if (localX < 0)
        {
            return true;
        }

        final String visibleString = ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(visibleString, localX);

        sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), trimmedString.length() + scrollOffset, selectionEnd));

        return true;
    }

    @Override
    public boolean onMouseClickMoveClient(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        final String visibleString = ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = ProxyHolder.getInstance().getFontRenderer().trimStringToWidth(visibleString, localX);

        sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), cursorPosition, trimmedString.length() + scrollOffset));

        return true;
    }

    @Override
    public boolean canAcceptKeyInputClient(final int character, final KeyboardKey key)
    {
        return true;
    }

    @Override
    public boolean onKeyPressedClient(final int character, final KeyboardKey key)
    {
        switch (character)
        {
            case 1:
                sendToServer(new TextFieldUpdateSelectionEndOrCursorPositionMessage(getId(), getContents().length(), 0));
                break;
            case 3:
                Minecraft.getInstance().keyboardListener.setClipboardString(getSelectedText());
                break;
            case 22:
                writeText(Minecraft.getInstance().keyboardListener.getClipboardString());
                break;
            case 24:
                Minecraft.getInstance().keyboardListener.setClipboardString(getSelectedText());
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

                final IDependencyObject<String> contents = elementData.getFromRawDataWithDefault(CONST_CONTENT, engine, "", String.class);
                final IDependencyObject<Boolean> doBackgroundDraw = elementData.getFromRawDataWithDefault(CONST_DO_BACK_DRAW, engine, true, Boolean.class);
                final IDependencyObject<Integer> maxContentLenght = elementData.getFromRawDataWithDefault(CONST_MAX_LENGTH, engine, Integer.MAX_VALUE, Integer.class);
                final IDependencyObject<String> outerBackgroundColor = elementData.getFromRawDataWithDefault(CONST_OUTER_BACKGROUND_COLOR, engine, "-6250336", String.class);
                final IDependencyObject<String> innerBackgroundColor = elementData.getFromRawDataWithDefault(CONST_INNER_BACKGROUND_COLOR, engine, "-16777216", String.class);
                final IDependencyObject<String> enabledFontColor = elementData.getFromRawDataWithDefault(CONST_ENABLED_FONT_COLOR, engine, "14737632", String.class);
                final IDependencyObject<String> disabledFontColor = elementData.getFromRawDataWithDefault(CONST_DISABLED_FONT_COLOR, engine, "7368816", String.class);
                final IDependencyObject<String> cursorColor = elementData.getFromRawDataWithDefault(CONST_CURSOR_COLOR, engine, "-3092272", String.class);
                final IDependencyObject<String> selectionColor = elementData.getFromRawDataWithDefault(CONST_SELECTION_COLOR, engine, "Blue", String.class);


                final int cursorPosition = elementData.getRawWithoutBinding(CONST_CURSOR_POS, 0, Integer.class);
                final int scrollOffset = elementData.getRawWithoutBinding(CONST_CURSOR_SCROLL_OFF, 0, Integer.class);
                final int selectionEnd = elementData.getRawWithoutBinding(CONST_CURSOR_SEL_END, 0, Integer.class);

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
                  outerBackgroundColor,
                  innerBackgroundColor,
                  enabledFontColor,
                  disabledFontColor,
                  cursorColor,
                  selectionColor,
                  cursorPosition,
                  scrollOffset,
                  selectionEnd
                );

                return element;
            }, (element, builder) -> {
                builder
                  .addComponent(CONST_CONTENT, element.getContents(), String.class)
                  .addComponent(CONST_CURSOR_POS, element.cursorPosition, Integer.class)
                  .addComponent(CONST_CURSOR_SCROLL_OFF, element.scrollOffset, Integer.class)
                  .addComponent(CONST_CURSOR_SEL_END, element.selectionEnd, Integer.class)
                  .addComponent(CONST_DO_BACK_DRAW, element.shouldDrawBackground(), Boolean.class)
                  .addComponent(CONST_MAX_LENGTH, element.getMaxStringLength(), Integer.class)
                  .addComponent(CONST_OUTER_BACKGROUND_COLOR, element.getOuterBackgroundColor(), String.class)
                  .addComponent(CONST_INNER_BACKGROUND_COLOR, element.getInnerBackgroundColor(), String.class)
                  .addComponent(CONST_ENABLED_FONT_COLOR, element.getEnabledColor(), String.class)
                  .addComponent(CONST_DISABLED_FONT_COLOR, element.getDisabledColor(), String.class)
                  .addComponent(CONST_CURSOR_COLOR, element.getCursorColor(), String.class)
                  .addComponent(CONST_SELECTION_COLOR, element.getSelectionColor(), String.class);
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
