package com.ldtteam.blockout.element.root;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.management.IUIManager;
import org.jetbrains.annotations.NotNull;

public interface IRootGuiElement extends IUIElementHost {
    void setUiManager(@NotNull IUIManager manager);
}
