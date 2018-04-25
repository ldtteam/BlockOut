package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.Image.CONST_ICON;
import static com.minecolonies.blockout.util.Constants.Controls.Slot.*;

public class Slot extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> inventoryId;
    @NotNull
    private IDependencyObject<Integer>          inventoryIndex;
    @NotNull
    private IDependencyObject<ResourceLocation> backgroundImage;

    @NotNull
    private int slotIndex;

    public Slot(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> inventoryId,
      @NotNull final IDependencyObject<Integer> inventoryIndex,
      @NotNull final IDependencyObject<ResourceLocation> backgroundImage)
    {
        super(KEY_SLOT, id, parent);
        this.inventoryId = inventoryId;
        this.inventoryIndex = inventoryIndex;
        this.backgroundImage = backgroundImage;
    }

    public Slot(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> inventoryId,
      @NotNull final IDependencyObject<Integer> inventoryIndex,
      @NotNull final IDependencyObject<ResourceLocation> backgroundImage)
    {
        super(KEY_SLOT, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.inventoryId = inventoryId;
        this.inventoryIndex = inventoryIndex;
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();

        controller.bindTexture(getBackgroundImage());
        controller.drawTexturedModalRect(getLocalBoundingBox(), getBackgroundImageSize());

        GlStateManager.popMatrix();
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public ResourceLocation getBackgroundImage()
    {
        return backgroundImage.get(getDataContext());
    }

    void setBackgroundImage(@NotNull final ResourceLocation location)
    {
        backgroundImage = DependencyObjectHelper.createFromValue(location);
    }

    @NotNull
    public Vector2d getBackgroundImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getBackgroundImage());
    }

    @NotNull
    public ResourceLocation getInventoryId()
    {
        return backgroundImage.get(getDataContext());
    }

    void setInventoryId(@NotNull final ResourceLocation inventoryId)
    {
        backgroundImage = DependencyObjectHelper.createFromValue(inventoryId);
    }

    @NotNull
    public Integer getInventoryIndex()
    {
        return inventoryIndex.get(getDataContext());
    }

    void setInventoryIndex(@NotNull final Integer index)
    {
        inventoryIndex = DependencyObjectHelper.createFromValue(index);
    }

    @NotNull
    public int getSlotIndex()
    {
        return slotIndex;
    }

    public void setSlotIndex(@NotNull final int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public static class Factory implements IUIElementFactory<Slot>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_SLOT;
        }

        @NotNull
        @Override
        public Slot readFromElementData(@NotNull final IUIElementData elementData)
        {
            final String id = elementData.getStringAttribute(CONST_ID);
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDatacontext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> inventoryId = elementData.getBoundResourceLocationAttribute(CONST_INVENTORY_ID);
            final IDependencyObject<Integer> inventoryIndex = elementData.getBoundIntegerAttribute(CONST_INVENTORY_INDEX);
            final IDependencyObject<ResourceLocation> icon = elementData.getBoundResourceLocationAttribute(CONST_BACKGROUND_IMAGE);

            return new Slot(id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              dataContext,
              visible,
              enabled,
              inventoryId,
              inventoryIndex,
              icon);
        }

        @Override
        public void writeToElementData(@NotNull final Slot element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_ICON, element.getBackgroundImage())
              .addResourceLocation(CONST_INVENTORY_ID, element.getInventoryId())
              .addInteger(CONST_INVENTORY_INDEX, element.getInventoryIndex());
        }
    }
}
