package com.ldtteam.blockout.loader.binding.engine;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.loader.binding.core.IBindingCommand;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.binding.core.IBindingTransformer;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleBindingEngine implements IBindingEngine
{
    private static final String COMMAND_SYNTAX_KEYWORD = "%COMMAND_SYNTAX%";
    private static final String COMMAND_DATA_KEYWORD   = "%COMMAND_DATA%";
    private static final String TRANSFORMER_NAME       = "transformers";
    private static final String BINDINGCOMMAND_PATTERN = "\\{(" + COMMAND_SYNTAX_KEYWORD + "):(" + COMMAND_DATA_KEYWORD + ")((:)(?<" + TRANSFORMER_NAME + ">.+))*}";

    private static SimpleBindingEngine              ourInstance    = new SimpleBindingEngine();
    private final  Map<Pattern, IBindingCommand>    commandMap     = Collections.synchronizedMap(new LinkedHashMap<>());
    private final  Map<String, IBindingTransformer<?,?>> transformerMap = new ConcurrentHashMap<>();

    private SimpleBindingEngine()
    {
    }

    public static SimpleBindingEngine getInstance()
    {
        return ourInstance;
    }

    @Override
    public <T> Optional<IDependencyObject<T>> attemptBind(@NotNull final IUIElementDataComponent component, @Nullable T defaultValue)
    {
        try
        {
            for (final Map.Entry<Pattern, IBindingCommand> knownPatternsAndCommands :
              commandMap.entrySet())
            {
                final Matcher attemptMatcher = knownPatternsAndCommands.getKey().matcher(component.getAsString());
                if (attemptMatcher.matches())
                {
                    return Optional.of(executeBindingAttempt(attemptMatcher, knownPatternsAndCommands.getValue(), defaultValue));
                }
            }
        }
        catch (ClassCastException cce)
        {
            Log.getLogger().warn("Failed to convert binding types into another. Either your transformer order or binding is broken.", cce);
            return Optional.of(DependencyObjectHelper.createFromValue(defaultValue));
        }
        catch (Exception ex)
        {
            Log.getLogger().error("Failed to build binding.", ex);
            return Optional.of(DependencyObjectHelper.createFromValue(defaultValue));
        }

        return Optional.empty();
    }

    @Override
    @NotNull
    public IBindingEngine registerBindingCommand(@NotNull final IBindingCommand... commands)
    {
        for (final IBindingCommand command :
          commands)
        {
            final Pattern compiledRegexPattern = Pattern.compile(BINDINGCOMMAND_PATTERN
                                                                   .replace(COMMAND_SYNTAX_KEYWORD, command.getBindCommandSyntax())
                                                                   .replace(COMMAND_DATA_KEYWORD, command.getBindDataSyntax()));

            this.commandMap.put(compiledRegexPattern, command);
        }

        return this;
    }

    @Override
    @NotNull
    public IBindingEngine registerBindingTransformer(@NotNull final IBindingTransformer<?,?>... transformers)
    {
        for (final IBindingTransformer<?,?> transformer : transformers)
        {
            this.transformerMap.put(transformer.getTransformerName(), transformer);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> IDependencyObject<T> executeBindingAttempt(@NotNull final Matcher bindingCommandMatcher, @NotNull final IBindingCommand command, @Nullable T defaultValue)
    {
        final String transformerNames = bindingCommandMatcher.group(TRANSFORMER_NAME);
        if (transformerNames != null && !transformerNames.isEmpty())
        {
            final LinkedHashSet<IBindingTransformer<?,?>> transformers =
              Arrays.stream(transformerNames.split(":")).map(transformerMap::get).collect(Collectors.toCollection(LinkedHashSet::new));

            IDependencyObject<?> currentDependency = command.bind(bindingCommandMatcher, defaultValue);
            for (final IBindingTransformer<?,?> transformer :
              transformers)
            {
                currentDependency = processTransform(currentDependency, transformer);
            }

            return (IDependencyObject<T>) currentDependency;
        }
        else
        {
            return command.bind(bindingCommandMatcher, defaultValue);
        }
    }

    @SuppressWarnings("unchecked")
    private <F, T> IDependencyObject<T> processTransform(final IDependencyObject<?> input, final IBindingTransformer<F, T> transformer)
    {
        try {
            final IDependencyObject<F> fromDependency = (IDependencyObject<F>) input;
            return transformer.generateTransformingBind(() -> fromDependency);
        }
        catch (ClassCastException ccEx)
        {
            Log.getLogger().error("Failed to process the dependency object: " + input + " the type of its value does not match the input type of the transformer: " + transformer);
            throw new IllegalArgumentException("Input is not of a valid type for the transformer.", ccEx);
        }


    }
}
