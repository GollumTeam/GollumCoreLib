package mods.gollum.core.client.gui;
import java.io.File;

import mods.gollum.core.ModGollumCoreLib;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class TestModConfigGUI extends GuiConfig {
	
	public TestModConfigGUI(GuiScreen parent) {
		super(
			parent, 
			new ConfigElement(getCategory ()).getChildElements(),
			ModGollumCoreLib.MODID,
			false,
			false, 
			"Title Config"
		);
	}

	private static ConfigCategory getCategory() {
		
		Configuration configFile = new Configuration (new File("Test.cfg"));

		int i1 = configFile.getInt("aaaaa 1", Configuration.CATEGORY_GENERAL, 1, 0, 10, "Super commentaire 1");
		int i2 = configFile.getInt("bbbb 2", Configuration.CATEGORY_GENERAL, 1, 0, 10, "Super commentaire 2");
		
		return configFile.getCategory(Configuration.CATEGORY_GENERAL);
	}

}