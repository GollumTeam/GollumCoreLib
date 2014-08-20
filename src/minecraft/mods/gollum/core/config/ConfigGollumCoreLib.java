package mods.gollum.core.config;

import mods.castledefenders.ModCastleDefenders;
import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.config.Config;
import mods.gollum.core.config.ConfigProp;
import mods.gollum.core.config.container.ItemStackConfig;
import mods.gollum.core.config.container.MobCapacitiesConfig;
import net.minecraft.item.Item;

public class ConfigGollumCoreLib extends Config {
	
	public ConfigGollumCoreLib(ModGollumCoreLib mod) {
		super (mod);
	}
	
	@ConfigProp (info = "Log display level (DEBUG, INFO, WARNING, SEVERE, NONE)")
	public static String level = "WARNING";
	
	@ConfigProp (info = "Display version checker message")
	public static boolean versionChecker = true;
	
	@ConfigProp(group = "Blocks Ids")
	public static int blockSpawnerID = 1243;
	
}
