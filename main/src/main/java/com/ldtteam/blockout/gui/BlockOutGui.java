package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.client.gui.IGuiContainer;
import com.ldtteam.jvoxelizer.client.guitemp.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.IOpenGl;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

public class BlockOutGuisdsd extends GuiContainer implements IGui, IGuiContainer<BlockOutGuiData>
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    @NotNull
    private Vector2d scaleFactor = new Vector2d(1, 1);

    /**
     * Used to track if the current slot interactions happen because of drawing or interactions.
     */
    @NotNull
    private boolean isDrawing = false;

    public BlockOutGui(final BlockOutContainer inventorySlotsIn)
    {
        super(inventorySlotsIn);
        this.key = inventorySlotsIn.getKey();
        this.root = inventorySlotsIn.getRoot();
        this.root.getUiManager().getRenderManager().setGui(this);
    }


    @Override
    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
        this.root.getUiManager().getRenderManager().setGui(this);
        this.root.update(this.root.getUiManager().getUpdateManager());
        this.initGui();
        ((BlockOutContainer) this.inventorySlots).setRoot(root);
    }


}
