package com.ldtteam.blockout.style.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.common.definition.loader.CommonIIdentifierBasedGuiDefinitionLoader;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoader;
import com.ldtteam.blockout.util.Constants;
import org.jetbrains.annotations.NotNull;

public class TemplateResource implements IResource
{
    public static final class Loader implements IResourceLoader<TemplateResource>
    {

        @NotNull
        @Override
        public String getTypeId()
        {
            return Constants.ResourceTypes.CONST_TEMPLATE_RESOURCE_TYPE;
        }

        @Override
        public TemplateResource load(@NotNull final IIdentifier id, @NotNull final JsonElement data)
        {
            if (!data.isJsonPrimitive())
            {
                throw new JsonParseException("IUIElementData needs to reference a file.");
            }

            final IIdentifier target = IIdentifier.create(data.getAsString());
            return new TemplateResource(id, target);
        }
    }

    private final IIdentifier id;
    private final IUIElementData   data;

    public TemplateResource(final IIdentifier id, final IIdentifier dataLocation)
    {
        this.id = id;
        this.data = BlockOut.getBlockOut().getProxy().getLoaderManager().loadData(new CommonIIdentifierBasedGuiDefinitionLoader(dataLocation));
    }

    @NotNull
    @Override
    public IIdentifier getId()
    {
        return id;
    }

    public IUIElementData getData()
    {
        return data;
    }
}
