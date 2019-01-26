package com.ldtteam.blockout.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

/**
 * Represents a single binding command within the binding mechanics of
 * BlockOut.
 * <p>
 * Used during deserialization (but serialization for networking) to bind the server
 * side representation to some contextual data.
 */
public interface IBindingCommand
{

    /**
     * Syntax for the binding command keyword. Used to compile a Regex that is used to check for binding.
     * Does not need to be unique. However if duplicates are specified, calling order can not be guaranteed.
     * <p>
     * Illegal characters: {@literal :, {, }} that the data may never contain. The implementer needs to make sure that
     * these characters will never be part of a valid bind syntax, since those are used by the overarching engine to detect
     * binding structures.
     * <p>
     * EG: ((B|b)inding) or ((T|t)emplate(B|b)inding
     *
     * @return The bind command syntax.
     */
    String getBindCommandSyntax();

    /**
     * Syntax for the binding data.
     * Again used to compile a Regex that is used to check for binding.
     * Does not need to be unique. However if duplicates are specified, called order can not be guaranteed.
     * <p>
     * Illegal characters: {@literal :, {, }} that the data may never contain. The implementer needs to make sure that
     * these characters will never be part of a valid bind syntax, since those are used by the overarching engine to detect
     * binding structures.
     * <p>
     * EG: (?<singleName>[a-zA-Z_]+) or ((?<getterName>[a-zA-Z_]+)#(?<setterName>[a-zA-Z_]+))
     *
     * @return The bind data syntax.
     */
    String getBindDataSyntax();

    /**
     * Called to bind the components binding command.
     *
     * @param bindMatcher  The regex matcher build from the syntax information that indicated that this command should be used.
     *                     Can be used to extract data from the command, since the {@link #getBindDataSyntax()} is embedded into the
     *                     regex that has been used to build this matching {@link Matcher}. It is guaranteed that {@link Matcher#matches()}
     *                     has been called to verify that the given {@link Matcher} matches this command, it is however possible that other commands
     *                     also match, and those will be called if binding failed.
     * @param defaultValue The default value in case the binding false.
     * @param <T>          The type that is bound to.
     * @return A {@link IDependencyObject} containing the bound information. {@code null} is binding failed.
     */
    @Nullable
    <T> IDependencyObject<T> bind(
      @NotNull Matcher bindMatcher,
      @Nullable T defaultValue
    );
}
