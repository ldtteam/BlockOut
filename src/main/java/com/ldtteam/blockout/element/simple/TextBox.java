package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.core.element.input.*;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.element.input.IScrollAcceptingUIElement;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class TextBox extends AbstractSimpleUIElement
    implements IScrollAcceptingUIElement, IClientSideClickAcceptingUIElement, IClientSideKeyAcceptingUIElement
{

    IDependencyObject<String> contents;
    IDependencyObject<Integer> cursorPosition;
    IDependencyObject<Integer> selectionEnd;

    public TextBox(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id, @Nullable final IUIElementHost parent)
    {
        super(type, style, id, parent);
    }

    public TextBox(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(type, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canAcceptMouseInputClient(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final int deltaWheel)
    {
        return false;
    }

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {

    }

    @Override
    public boolean canAcceptKeyInputClient(final int character, final KeyboardKey key)
    {
        return true;
    }

    public String getContents()
    {
        return this.contents.get(this);
    }

    public void setContents(@NotNull final String contents)
    {
        this.contents.set(this, contents);
    }

    public Integer getCursorPosition()
    {
        return this.cursorPosition.get(this);
    }

    public void setCursorPosition(@NotNull final Integer cursorPosition)
    {
        this.cursorPosition.set(this, cursorPosition);
    }

    public Integer getSelectionEnd()
    {
        return this.selectionEnd.get(this);
    }

    public void setSelectionEnd(@NotNull final Integer selectionEnd)
    {
        this.selectionEnd.set(this, selectionEnd);
    }
}
