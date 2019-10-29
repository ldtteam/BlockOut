package com.ldtteam.blockout.proxy;

/**
 * The proxy for the translation engine of the game.
 */
public interface II18nProxy
{
    /**
     * Translates and formats the result of the translation with the given parameters.
     *
     * @param translateKey The translation key to translate.
     * @param parameters The parameters to format the resulting translation with.
     * @return The translated and formatted result.
     */
    String format(String translateKey, Object... parameters);
}
