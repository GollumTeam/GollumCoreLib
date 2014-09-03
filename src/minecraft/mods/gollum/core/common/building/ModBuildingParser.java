package mods.gollum.core.common.building;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cpw.mods.fml.common.Loader;
import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.resource.ResourceLoader;
import net.minecraft.util.ResourceLocation;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

public class ModBuildingParser {

	public static final String NAME_JSON = "buildings.json";
	public static final String DIR_BUILDING_ASSETS  = "buildings/";

	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	
	public void parse(String modId) {
		
		if (!resourceLoader.assetExist(DIR_BUILDING_ASSETS+NAME_JSON, modId)) {
			ModGollumCoreLib.log.debug("No buildings Found : "+modId);
			return;
		}
		
		try {
			
			InputStream isJson = resourceLoader.asset (DIR_BUILDING_ASSETS+NAME_JSON, modId);
			JsonRootNode json  = this.parser.parse(new InputStreamReader(isJson));
			
			isJson.close();
			
		} catch (Exception e) {
			ModGollumCoreLib.log.severe("Erreur parsing buildings : "+modId);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Renvoie le flux de fichier depuis le jar (depuis le système de fichier en mode DEV)
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public File getResource (String path) throws FileNotFoundException {
		
//		InputStream is = getClass().getResourceAsStream(path);
		
//		if (is == null) {
//			ModGollumCoreLib.log.warning ("Failed to read resource '" + path + "' in jar. read by path file");
//			is = new FileInputStream (Minecraft.getMinecraft().mcDataDir + path);
//		}
//		
//		if (is != null) {
//			ModGollumCoreLib.log.info ("Read resource '" + path + "' in jar");
//		}
		ResourceLocation resource = new ResourceLocation(path);
		
		return new File (resource.getResourcePath());
	}
}
