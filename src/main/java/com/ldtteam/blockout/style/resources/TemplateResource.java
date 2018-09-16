package com.ldtteam.blockout.style.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.common.definition.loader.CommonResourceLocationBasedGuiDefinitionLoader;
import com.ldtteam.blockout.loader.IUIElementData;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoader;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TemplateResource implements IResource
{
    public static final class Loader implements IResourceLoader<TemplateResource>
    {

        @NotNull
        @Override
        public String getTypeId()
        {
            return Constants.Resources.CONST_TEMPLATE_RESOURCE_TYPE;
        }

        @Override
        public TemplateResource load(@NotNull final ResourceLocation id, @NotNull final JsonElement data)
        {
            if (!data.isJsonPrimitive())
            {
                throw new JsonParseException("IUIElementData needs to reference a file.");
            }

            final ResourceLocation target = new ResourceLocation(data.getAsString());
            return new TemplateResource(id, target);
        }
    }

    private final ResourceLocation id;
    private final IUIElementData   data;

    public TemplateResource(final ResourceLocation id, final ResourceLocation dataLocation)
    {
        this.id = id;
        this.data = BlockOut.getBlockOut().getProxy().getLoaderManager().loadData(new CommonResourceLocationBasedGuiDefinitionLoader(dataLocation));
    }

    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    public IUIElementData getData()
    {
        return data;
    }
}
