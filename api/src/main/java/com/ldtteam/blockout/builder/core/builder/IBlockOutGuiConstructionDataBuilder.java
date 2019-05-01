package com.ldtteam.blockout.builder.core.builder;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * DataBuilder which is used to bind dependencies, eventHandlers or controls.
 */
public interface IBlockOutGuiConstructionDataBuilder
{
    /**
     * Bind a dependency to a field.
     *
     * @param controlId  the id of the control.
     * @param fieldName  the name of the field.
     * @param dependency the dependency.
     * @return an instance of this class.
     */
    @NotNull
    IBlockOutGuiConstructionDataBuilder withDependency(@NotNull final String controlId, @NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency);

    /**
     * Bind an evenHandler to an action.
     *
     * @param controlId         the id of the control causing the event.
     * @param eventName         the name of the event.
     * @param controlTypeClass  the class of the control.
     * @param argumentTypeClass the argument class of the event.
     * @param eventHandler      the event handler.
     * @param <S>               the source of the event.
     * @param <A>               the arguments of the event.
     * @return an instance of this class.
     */
    @NotNull
    <S, A> IBlockOutGuiConstructionDataBuilder withEventHandler(
      @NotNull final String controlId, @NotNull final String eventName, @NotNull final Class<S> controlTypeClass, @NotNull final Class<A> argumentTypeClass, @NotNull final
    IEventHandler<? super S, ? super A> eventHandler);

    /**
     * Binds a control as for example an inventory to a control in the GUI.
     *
     * @param controlId               the id of the control in the inventory.
     * @param builderClass            the class used to build it.
     * @param builderInstanceConsumer the consumer class.
     * @param <T>                     the UIElement.
     * @param <B>                     the builder.
     * @return an instance of this class.
     */
    @NotNull
    <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> IBlockOutGuiConstructionDataBuilder withControl(
      @NotNull final String controlId,
      @NotNull final Class<B> builderClass,
      @NotNull final Consumer<B> builderInstanceConsumer);

    /**
     * Copies the data stored on the builder into this builder.
     * Builds the given builder first.
     *
     * @param builder The builder to copy.
     * @return an instance of this interface.
     */
    default IBlockOutGuiConstructionDataBuilder copyFrom(@NotNull final IBlockOutGuiConstructionDataBuilder builder)
    {
        return copyFrom(builder.build());
    }

    /**
     * Copies the data stored on the data into this builder.
     *
     * @param data The data to copy.
     * @return an instance of this interface.
     */
    IBlockOutGuiConstructionDataBuilder copyFrom(@NotNull final IBlockOutGuiConstructionData data);

    /**
     * Finish the building sequence and return the builder.
     *
     * @return an instance of this class.
     */
    @NotNull
    IBlockOutGuiConstructionData build();
}
