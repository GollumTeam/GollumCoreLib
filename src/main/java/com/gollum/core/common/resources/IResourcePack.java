package com.gollum.core.common.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.gollum.core.common.resources.data.IMetadataSection;
//import com.gollum.core.common.resources.data.IMetadataSerializer;

import net.minecraft.util.ResourceLocation;

public interface IResourcePack
{
    InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException;

    boolean resourceExists(ResourceLocation p_110589_1_);

    Set getResourceDomains();

//    IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException;

    BufferedImage getPackImage() throws IOException;

    String getPackName();
}