package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.client.renderer.opengl.IOpenGl;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.Slot.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class Slot extends AbstractSimpleUIElement implements IDrawableUIElement
{
    public static final class SlotConstructionDataBuilder extends AbstractSimpleUIElement.SimpleControlConstructionDataBuilder<SlotConstructionDataBuilder, Slot>
    {

        public SlotConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data,
          final Class<Slot> controlClass)
        {
            super(controlId, data, controlClass);
        }

        public SlotConstructionDataBuilder withDependentInventoryId(@NotNull final IDependencyObject<IIdentifier> inventoryId)
        {
            return withDependency("inventoryId", inventoryId);
        }

        public SlotConstructionDataBuilder withInventoryId(@NotNull final IIdentifier inventoryId)
        {
            return withDependency("inventoryId", DependencyObjectHelper.createFromValue(inventoryId));
        }

        public SlotConstructionDataBuilder withDependentInventoryIndex(@NotNull final IDependencyObject<Integer> inventoryIndex)
        {
            return withDependency("inventoryIndex", inventoryIndex);
        }

        public SlotConstructionDataBuilder withInventoryIndex(@NotNull final Integer inventoryIndex)
        {
            return withDependency("inventoryIndex", DependencyObjectHelper.createFromValue(inventoryIndex));
        }

        public SlotConstructionDataBuilder withDependentBackgroundImageResource(@NotNull final IDependencyObject<IIdentifier> backgroundImageResource)
        {
            return withDependency("backgroundImageResource", backgroundImageResource);
        }

        public SlotConstructionDataBuilder withBackgroundImageResource(@NotNull final IIdentifier backgroundImageResource)
        {
            return withDependency("backgroundImageResource", DependencyObjectHelper.createFromValue(backgroundImageResource));
        }
    }

    @NotNull
    public  IDependencyObject<IIdentifier> inventoryId;
    @NotNull
    public  IDependencyObject<Integer>          inventoryIndex;
    @NotNull
    public  IDependencyObject<IIdentifier> backgroundImageResource;
    @NotNull
    private int                                 slotIndex;

    public Slot(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<IIdentifier> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<IIdentifier> inventoryId,
      @NotNull final IDependencyObject<Integer> inventoryIndex,
      @NotNull final IDependencyObject<IIdentifier> backgroundImageResource)
    {
        super(KEY_SLOT, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.inventoryId = inventoryId;
        this.inventoryIndex = inventoryIndex;
        this.backgroundImageResource = backgroundImageResource;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (inventoryId.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (inventoryIndex.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (backgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final Vector2d size = getLocalBoundingBox().getSize();
        final ImageResource resource = getBackgroundImage();
        final Vector2d scalingFactor = resource.getScalingFactor(size);

        IOpenGl.pushMatrix();
        IOpenGl.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);
        IOpenGl.disableLighting();
        IOpenGl.disableDepth();
        IOpenGl.disableAlpha();
        IOpenGl.enableBlend();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        IOpenGl.disableBlend();
        IOpenGl.enableAlpha();
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
        IOpenGl.popMatrix();

        controller.drawSlotContent(this);
        controller.drawSlotMouseOverlay(this);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public ImageResource getBackgroundImage()
    {
        return getResource(getBackgroundImageResource());
    }

    @NotNull
    public IIdentifier getBackgroundImageResource()
    {
        return backgroundImageResource.get(this);
    }

    void setBackgroundImageResource(@NotNull final IIdentifier location)
    {
        backgroundImageResource.set(this, location);
    }

    @NotNull
    public IIdentifier getInventoryId()
    {
        return inventoryId.get(this);
    }

    void setInventoryId(@NotNull final IIdentifier inventoryId)
    {
        this.inventoryId.set(this, inventoryId);
    }

    @NotNull
    public Integer getInventoryIndex()
    {
        return inventoryIndex.get(this);
    }

    void setInventoryIndex(@NotNull final Integer index)
    {
        inventoryIndex.set(this, index);
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

    public static class Factory extends AbstractSimpleUIElementFactory<Slot>
    {

        public Factory()
        {
            super(Slot.class, KEY_SLOT, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<IIdentifier> inventoryId =
                  elementData.getFromRawDataWithDefault(CONST_INVENTORY_ID, engine, IIdentifier.create(MISSING), IIdentifier.class);
                final IDependencyObject<Integer> inventoryIndex = elementData.getFromRawDataWithDefault(CONST_INVENTORY_INDEX, engine, -1, Integer.class);
                final IDependencyObject<IIdentifier> icon = elementData.getFromRawDataWithDefault(CONST_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING), IIdentifier.class);

                final Slot element = new Slot(
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
                  inventoryId,
                  inventoryIndex,
                  icon);

                return element;
            }, (element, builder) -> builder
                                       .addComponent(CONST_BACKGROUND_IMAGE, element.getBackgroundImageResource(), IIdentifier.class)
                                       .addComponent(CONST_INVENTORY_ID, element.getInventoryId(), IIdentifier.class)
                                       .addComponent(CONST_INVENTORY_INDEX, element.getInventoryIndex(), Integer.class));
        }
    }
}
