package com.ldtteam.blockout.element.tooltip;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.IUIElementWithTooltip;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.tooltip.ITooltipDelayHandler;
import com.ldtteam.blockout.tooltip.ITooltipHost;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

import static com.ldtteam.blockout.util.Constants.Controls.Root.KEY_ROOT;
import static com.ldtteam.blockout.util.Constants.Controls.TooltipHost.KEY_TOOLTIP_HOST;

public class TooltipHost extends AbstractChildrenContainingUIElement implements ITooltipHost {

    private final IUIElementWithTooltip tooltipElement;
    private final ITooltipDelayHandler tooltipDelayHandler = new FixedTooltipDelayHandler();

    public TooltipHost(@NotNull final IDependencyObject<ResourceLocation> style, @Nullable final IUIElementWithTooltip parent, @NotNull final IDependencyObject<EnumSet<Alignment>> alignments, @NotNull final IDependencyObject<Dock> dock, @NotNull final IDependencyObject<AxisDistance> margin, @NotNull final IDependencyObject<Vector2d> elementSize, @NotNull final IDependencyObject<AxisDistance> padding, @NotNull final IDependencyObject<Object> dataContext, @NotNull final IDependencyObject<Boolean> visible, @NotNull final IDependencyObject<Boolean> enabled) {
        super(KEY_TOOLTIP_HOST, style, Objects.requireNonNull(parent).getId() + "_" + KEY_TOOLTIP_HOST.split(":")[1], null, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
        this.tooltipElement = parent;
    }

    @Override
    public IUIElement getTooltipElement() {
        return tooltipElement;
    }

    @Override
    public ITooltipDelayHandler getTooltipDelayHandler() {
        return tooltipDelayHandler;
    }

    @Override
    public void setUiManager(@NotNull final IUIManager manager) {
        //Noop tooltip hosts can not update the UIManager.
    }

    public static final class TooltipHostConstructionDataBuilder extends SimpleControlConstructionDataBuilder<TooltipHostConstructionDataBuilder, TooltipHost>
    {
        public TooltipHostConstructionDataBuilder(
                final String controlId,
                final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, TooltipHost.class);
        }
    }

    public static class Factory extends AbstractChildrenContainingUIElementFactory<TooltipHost>
    {
        public Factory()
        {
            super(TooltipHost.class,
                    KEY_TOOLTIP_HOST,
                    (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                        if (!(parent instanceof IUIElementWithTooltip))
                            throw new IllegalArgumentException("Parent does not have a tooltip. As such you can not construct a tooltip for it!");

                        return new TooltipHost(
                                styleId,
                                (IUIElementWithTooltip) parent,
                                alignments,
                                dock,
                                margin,
                                elementSize,
                                padding,
                                dataContext,
                                visible,
                                enabled);
                    }, (element, builder) -> {
                        //No additional information is stored in here.
                    });
        }
    }
}
