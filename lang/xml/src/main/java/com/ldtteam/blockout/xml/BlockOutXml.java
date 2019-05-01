package com.ldtteam.blockout.xml;

import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.jvoxelizer.discovery.IJVoxModPlugin;

public class BlockOutXml implements IJVoxModPlugin
{
    @Override
    public String getTargetModId()
    {
        return "blockout";
    }

    @Override
    public void onPreInit()
    {
        IProxy.getInstance().getLoaderManager().registerLoader(new XmlLoader());
    }
}
