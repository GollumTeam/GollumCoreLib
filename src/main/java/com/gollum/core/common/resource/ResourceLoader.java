package com.gollum.core.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import com.gollum.core.ModGollumCoreLib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ResourceLoader {
	
	private static ConcurrentHashMap<String, FileResourcePack> resourcePachs = new ConcurrentHashMap<String, FileResourcePack>();
	
	/**
	 * Renvoie le flux de fichier depuis le jar (depuis le système de fichier en mode DEV)
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public InputStream asset(String assetPath, String modId) throws IOException {
		return this.get (assetPath, modId);
	}

	/**
	 * Renvoie le flux de fichier depuis le jar (depuis le système de fichier en mode DEV)
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	protected InputStream get(String path, String modId) throws IOException {
		
		InputStream is = null;
		
		Map<String, ModContainer> modContainers = Loader.instance().getIndexedModList();
		if (modContainers.containsKey(modId)) {
			
			ModContainer modContainer = modContainers.get(modId);
			
			File modSource = modContainer.getSource();
			
			if (modSource.isDirectory()) {
				
				ModGollumCoreLib.log.debug ("Read in directory 'assets/"+modId.toLowerCase()+"/"+path + "'.");
				
				File file = new File (modSource, "assets/"+modId.toLowerCase()+"/"+path);
				if (file.exists()) {
					is = new FileInputStream (file);
				}
			} else if (modContainer.getMod() != null) {
				
				ModGollumCoreLib.log.debug ("Read in jar file '" + path + "'.");
				ModGollumCoreLib.log.debug ("Read in jar file '" + modId.toLowerCase()+":"+path + "'.");
				ResourceLocation location = new ResourceLocation(modId.toLowerCase()+":"+path);
				
				is = this.getResourcePack(modSource).getInputStream(location);
				
			}
		}
		
		return is;
	}
	
	public boolean assetExist(String assetPath, String modId) {
		return this.exist (assetPath, modId);
	}
	
	protected FileResourcePack getResourcePack (File file) {
		
		if (!resourcePachs.containsKey(file.getPath())) {
			resourcePachs.put(file.getPath(), new FileResourcePack (file));
		}
		
		return resourcePachs.get(file.getPath());
	}
	
	protected boolean exist (String path, String modId) {
		
		boolean rtn = false;
		
		try {
			
			Map<String, ModContainer> modContainers = Loader.instance().getIndexedModList();
			if (modContainers.containsKey(modId)) {
	
				ModContainer modContainer = modContainers.get(modId);
				
				File modSource = modContainer.getSource();
				
				if (modSource.isDirectory()) {
					ModGollumCoreLib.log.debug ("Test in directory '" + "assets/"+modId.toLowerCase()+"/"+path + "'.");
					rtn = new File(modSource, "assets/"+modId.toLowerCase()+"/"+path).exists();
				} else if (modContainer.getMod() != null) {
					try {
						
						ModGollumCoreLib.log.debug ("Test in jar file '" + modId.toLowerCase()+":"+path + "'.");
						ResourceLocation location = new ResourceLocation(modId.toLowerCase()+":"+path);
						
						rtn = this.getResourcePack(modSource).getInputStream(location) != null;
					} catch (Exception e) {
					}
						
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
}
