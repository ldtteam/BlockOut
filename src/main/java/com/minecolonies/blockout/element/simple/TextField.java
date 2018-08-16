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
import com.minecolonies.blockout.event.Event;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.TextField.*;

public class TextField extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement, IKeyAcceptingUIElement
{
    /**
     * Texture resource location.
     */
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    /**
     * The current cursor position.
     */
    private int cursorPosition = 0;

    /**
     * The scroll offset.
     */
    private int scrollOffset = 0;

    /**
     * The selection end.
     */
    private int selectionEnd = 0;

    @NotNull
    private IDependencyObject<String> contents;

    @NotNull
    private Event<TextField, TextFieldChangedEventArgs> onClicked = new Event<>(TextField.class, TextFieldChangedEventArgs.class);

    /**
     * Public constructor to create textField.
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
     * @param style          the style.
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
      final int cursorPosition,
      final int scrollOffset,
      final int selectionEnd
    )
    {
        super(KEY_TEXT_FIELD, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.contents = contents;
        this.cursorPosition = cursorPosition;
        this.scrollOffset = scrollOffset;
        this.selectionEnd = selectionEnd;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final int maxTextLength = (int) (getLocalBoundingBox().getSize().getX() * (int) (getLocalBoundingBox().getSize().getY() / BlockOut.getBlockOut().getProxy().getFontRenderer().FONT_HEIGHT));

        controller.getScissoringController().focus(this);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        BoundingBox box = getLocalBoundingBox();

        final double width = box.getSize().getX();
        final double height = box.getSize().getY();
        controller.drawRect(-1, -1, width + 1, height + 1, new Color(0xFFA0A0A0));
        controller.drawRect(0, 0, width, height, new Color(Color.BLACK));

        final FontRenderer fontRenderer = BlockOut.getBlockOut().getProxy().getFontRenderer();
        //fontRenderer.drawString(getContents(), 0,2, 0xffffff);

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
            double selectionEndX = selectionStartX + fontRenderer.getStringWidth(visibleString.substring(min, Math.min(visibleString.length(),max)));
            controller.drawRect(selectionStartX,
              drawY,
              selectionEndX,
              fontRenderer.FONT_HEIGHT + 1,
              new Color(Color.white.getRed(), Color.WHITE.getGreen(), Color.white.getBlue(), 200));
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

    /**
     * Get the text content.
     *
     * @return the text.
     */
    public String getContents()
    {
        return contents.get(getDataContext());
    }

    /**
     * Set the text contents
     *
     * @param contents the new string to set.
     */
    public void setContents(@NotNull final String contents)
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
        if (localX < 0)
        {
            return;
        }

        final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(visibleString, localX);

        // Cache and restore scrollOffset when we change focus via click,
        // because onFocus() sets the cursor (and thus scroll offset) to the end.
        final int oldScrollOffset = scrollOffset;
        scrollOffset = oldScrollOffset;
        setCursorPosition(trimmedString.length() + scrollOffset);
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

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {

    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        final String visibleString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(getContents().substring(scrollOffset), getInternalWidth());
        final String trimmedString = BlockOut.getBlockOut().getProxy().getFontRenderer().trimStringToWidth(visibleString, localX);
        final int oldScrollOffset = scrollOffset;
        scrollOffset = oldScrollOffset;
        setSelectionEnd(trimmedString.length() + scrollOffset);
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
                setCursorPosition(getContents().length());
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
            resultBuffer.append(getContents().substring(0, insertAt));
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

        if (getContents().length() > 0 && insertEnd < getContents().length())
        {
            resultBuffer.append(getContents().substring(insertEnd));
        }

        setContents(resultBuffer.toString());
        moveCursorBy((insertAt - selectionEnd) + insertedLength);

        onClicked.raise(this, new TextFieldChangedEventArgs(getContents()));
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

            setContents(result);

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
     * Setter for the cursor position.
     *
     * @param pos the x pos.
     */
    private void setCursorPosition(final int pos)
    {
        cursorPosition = MathHelper.clamp(pos, 0, getContents().length());
        setSelectionEnd(cursorPosition);
    }

    /**
     * Move the cursor by an offset.
     *
     * @param offset the offset.
     */
    private void moveCursorBy(final int offset)
    {
        setCursorPosition(selectionEnd + offset);
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
    private void setSelectionEnd(final int pos)
    {
        selectionEnd = pos;

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
            default:
                writeText(Character.toString(c));
        }
    }

    /**
     * Handle tab to jump to next control (todo).
     */
    private void handleTab()
    {
        //if (tabNextPaneID != null)
        {
            /* todo get next child in parent
            final Pane next = getWindow().findPaneByID(tabNextPaneID);
            if (next != null)
            {
                next.setFocus();
            }*/
        }
    }

    /**
     * Handling arrow key for textfield navigation.
     *
     * @param key the clicked key.
     */
    private void handleArrowKeys(final KeyboardKey key)
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
    }

    /**
     * On home end key clicked.
     *
     * @param key the key specifics.
     */
    private void handleHomeEnd(final KeyboardKey key)
    {
        final int position = (key == KeyboardKey.KEY_HOME) ? 0 : getContents().length();

        if (GuiScreen.isShiftKeyDown())
        {
            setSelectionEnd(position);
        }
        else
        {
            setCursorPosition(position);
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

        if (GuiScreen.isCtrlKeyDown())
        {
            deleteWords(direction);
        }
        else
        {
            deleteFromCursor(direction);
        }
    }

    /**
     * Data builder for the text field.
     */
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

        @NotNull
        public TextFieldConstructionDataBuilder withChangedEventHandler(@NotNull final IEventHandler<TextField, TextField.TextFieldChangedEventArgs> eventHandler)
        {
            return withEventHandler("onChanged", TextField.TextFieldChangedEventArgs.class, eventHandler);
        }
    }

    /**
     * Element factory to instantiate the text field.
     */
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

            final int cursorPosition = elementData.getIntegerAttribute(CONST_CURSOR_POS);
            final int scrollOffset = elementData.getIntegerAttribute(CONST_CURSOR_SCROLL_OFF);
            final int selectionEnd = elementData.getIntegerAttribute(CONST_CURSOR_SEL_END);

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
              cursorPosition,
              scrollOffset,
              selectionEnd
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
              .addInteger(CONST_CURSOR_POS, element.cursorPosition)
              .addInteger(CONST_CURSOR_SCROLL_OFF, element.scrollOffset)
              .addInteger(CONST_CURSOR_SEL_END, element.selectionEnd);
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
         * @param newContent the new content.
         */
        public TextFieldChangedEventArgs(final String newContent)
        {
            this.newContent = newContent;
        }

        /**
         * Creates the event argument on text field changed with delta.
         * @param newContent the new content.
         * @param timeDelta the delta.
         */
        public TextFieldChangedEventArgs(final String newContent, final float timeDelta)
        {
            this.newContent = newContent;
        }

        /**
         * Gets the new text field content after event.
         * @return the content String.
         */
        public String getNewContent()
        {
            return newContent;
        }
    }
}
