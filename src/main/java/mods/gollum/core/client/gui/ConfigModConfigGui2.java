package mods.gollum.core.client.gui;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.io.File;
import java.lang.reflect.Field;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.common.ModContainer;

public class ConfigModConfigGui2 extends GuiConfig {
	
	GollumMod mod;
	
	public ConfigModConfigGui2(GuiScreen parent) {
		super(
			parent, 
			new ConfigElement(getCategory ()).getChildElements(),
			ModGollumCoreLib.MODID,
			false,
			false, 
			"Title Config"
		);
		
		if (parent instanceof GuiModList) {
			try {
				Field f = parent.getClass().getDeclaredField("selectedMod");
				f.setAccessible(true);
				ModContainer modContainer = (ModContainer)f.get(parent);
				mod = (GollumMod) modContainer.getMod();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		log.debug ("Config mod : " + mod.getModId());
		
	}

	private static ConfigCategory getCategory() {
		
		Configuration configFile = new Configuration (new File("Test.cfg"));

		int i1 = configFile.getInt("aaaaa 1", Configuration.CATEGORY_GENERAL, 1, 0, 10, "Super commentaire 1");
		int i2 = configFile.getInt("bbbb 2", Configuration.CATEGORY_GENERAL, 1, 0, 10, "Super commentaire 2");
		
		return configFile.getCategory(Configuration.CATEGORY_GENERAL);
	}

}