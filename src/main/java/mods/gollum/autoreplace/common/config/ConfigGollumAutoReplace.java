package mods.gollum.autoreplace.common.config;

import mods.gollum.autoreplace.common.blocks.BlockAutoReplace.ReplaceBlock;
import mods.gollum.autoreplace.common.config.type.ReplaceBlockType;
import mods.gollum.core.common.config.Config;
import mods.gollum.core.common.config.ConfigProp;

public class ConfigGollumAutoReplace extends Config {
	
	@ConfigProp( mcRestart = true)
	public ReplaceBlockType[] blockReplaceList = new ReplaceBlockType[] {
		new ReplaceBlockType()
	};
	
}
