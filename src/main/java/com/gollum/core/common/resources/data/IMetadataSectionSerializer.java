package com.gollum.core.common.resources.data;

import com.google.gson.JsonDeserializer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IMetadataSectionSerializer extends JsonDeserializer
{
    /**
     * The name of this section type as it appears in JSON.
     */
    String getSectionName();
}