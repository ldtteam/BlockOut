package com.ldtteam.blockout.json;

import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.discovery.IJVoxModPlugin;

public class BlockOutJson implements IJVoxModPlugin
{
    @Override
    public String getTargetModId()
    {
        return "blockout";
    }

    @Override
    public void onPreInit()
    {
        ProxyHolder.getInstance().getLoaderManager().registerLoader(new JsonLoader());
    }
}
