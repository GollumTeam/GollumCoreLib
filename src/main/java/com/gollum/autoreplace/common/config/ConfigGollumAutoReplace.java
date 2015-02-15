package com.gollum.autoreplace.common.config;

import com.gollum.autoreplace.common.blocks.BlockAutoReplace.ReplaceBlock;
import com.gollum.autoreplace.common.config.type.ReplaceBlockType;
import com.gollum.core.common.config.Config;
import com.gollum.core.common.config.ConfigProp;

public class ConfigGollumAutoReplace extends Config {
	
	@ConfigProp( mcRestart = true)
	public ReplaceBlockType[] blockReplaceList = new ReplaceBlockType[] {
		new ReplaceBlockType()
	};
	
}
