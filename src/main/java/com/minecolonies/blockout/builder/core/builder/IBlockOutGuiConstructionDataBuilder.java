package com.minecolonies.blockout.builder.core.builder;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * DataBuilder which is used to bind dependencies, eventHandlers or controls.
 */
public interface IBlockOutGuiConstructionDataBuilder
{
    /**
     * Bind a dependency to a field.
     * @param controlId the id of the control.
     * @param fieldName the name of the field.
     * @param dependency the dependency.
     * @return an instance of this class.
     */
    @NotNull
    IBlockOutGuiConstructionDataBuilder withDependency(@NotNull final String controlId, @NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency);

    /**
     * Bind an evenHandler to an action.
     * @param controlId the id of the control causing the event.
     * @param eventName the name of the event.
     * @param controlTypeClass the class of the control.
     * @param argumentTypeClass the argument class of the event.
     * @param eventHandler the event handler.
     * @param <S> the source of the event.
     * @param <A> the arguments of the event.
     * @return an instance of this class.
     */
    @NotNull
    <S, A> IBlockOutGuiConstructionDataBuilder withEventHandler(
      @NotNull final String controlId, @NotNull final String eventName, @NotNull final Class<S> controlTypeClass, @NotNull final Class<A> argumentTypeClass, @NotNull final
    IEventHandler<S, A> eventHandler);

    /**
     * Forward the control and do nothing. Used if util classes will be used to construct an event handler, dependency or control.
     * @param successful if the adding of the controls was successful.
     * @param <T> the UIElement.
     * @param <B> the builder.
     * @return an instance of this class.
     */
    @NotNull <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> IBlockOutGuiConstructionDataBuilder forwardingControl(boolean successful);

    /**
     * Binds a control as for example an inventory to a control in the GUI.
     * @param controlId the id of the control in the inventory.
     * @param builderClass the class used to build it.
     * @param builderInstanceConsumer the consumer class.
     * @param <T> the UIElement.
     * @param <B> the builder.
     * @return an instance of this class.
     */
    @NotNull
    <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> IBlockOutGuiConstructionDataBuilder withControl(
      @NotNull final String controlId,
      @NotNull final Class<B> builderClass,
      @NotNull final Consumer<B> builderInstanceConsumer);

    /**
     * Finish the building sequence and return the builder.
     * @return an instance of this class.
     */
    @NotNull
    IBlockOutGuiConstructionData build();
}
