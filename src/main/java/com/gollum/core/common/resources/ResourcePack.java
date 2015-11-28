package com.gollum.core.common.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.gollum.core.common.resources.data.MetadataSection;
import com.gollum.core.common.resources.data.MetadataSerializer;

import net.minecraft.util.ResourceLocation;

public interface ResourcePack {
	InputStream getInputStream(ResourceLocation resourcelocation) throws IOException;

	boolean resourceExists(ResourceLocation resourcelocation);

	Set getResourceDomains();

	MetadataSection getPackMetadata(MetadataSerializer metadataserializer, String s) throws IOException;

	BufferedImage getPackImage() throws IOException;

	String getPackName();
}
