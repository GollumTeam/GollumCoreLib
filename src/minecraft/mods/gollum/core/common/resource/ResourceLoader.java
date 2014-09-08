package mods.gollum.core.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import mods.gollum.core.ModGollumCoreLib;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ResourceLoader {
	
	
	/**
	 * Renvoie le flux de fichier depuis le jar (depuis le système de fichier en mode DEV)
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream asset(String assetPath, String modId) throws FileNotFoundException {
		return this.get ("assets/"+modId.toLowerCase()+"/"+assetPath, modId);
	}

	/**
	 * Renvoie le flux de fichier depuis le jar (depuis le système de fichier en mode DEV)
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream get(String path, String modId) throws FileNotFoundException {
		
		InputStream is = null;
		
		Map<String, ModContainer> modContainers = Loader.instance().getIndexedModList();
		if (modContainers.containsKey(modId)) {
			
			ModContainer modContainer = modContainers.get(modId);
			
			File modSource = modContainer.getSource();
			
			if (modSource.isDirectory()) {
				
				ModGollumCoreLib.log.debug ("Read in directory '" + path + "'.");
				
				File file = new File (modSource, path);
				if (file.exists()) {
					is = new FileInputStream (file);
				}
			} else if (modContainer.getMod() != null) {
				
				ModGollumCoreLib.log.debug ("Read in jar file '" + path + "'.");
				
				is = modContainer.getMod().getClass().getResourceAsStream(path);
				
			}
		}
		
		return is;
	}
	
	public boolean assetExist(String assetPath, String modId) {
		return this.exist ("assets/"+modId.toLowerCase()+"/"+assetPath, modId);
	}
	
	public boolean exist (String path, String modId) {
		
		boolean rtn = false;
		
		try {
			
			Map<String, ModContainer> modContainers = Loader.instance().getIndexedModList();
			if (modContainers.containsKey(modId)) {
	
				ModContainer modContainer = modContainers.get(modId);
				
				File modSource = modContainer.getSource();
				
				if (modSource.isDirectory()) {
					rtn = new File(modSource, path).exists();
				} else if (modContainer.getMod() != null) {
					try {
						InputStream is = modContainer.getMod().getClass().getResourceAsStream(path);
						rtn = is != null;
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
