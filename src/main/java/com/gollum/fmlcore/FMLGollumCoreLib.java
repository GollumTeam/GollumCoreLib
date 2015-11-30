package com.gollum.fmlcore;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

// Go see https://github.com/Guichaguri/TickrateChanger

@TransformerExclusions({"com.gollum.fmlcore"})
public class FMLGollumCoreLib implements IFMLLoadingPlugin, IFMLCallHook {
	
	private FMLGollumCoreLib instance;
	
	public FMLGollumCoreLib() {
		instance = this;
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"me.guichaguri.tickratechanger.TickrateTransformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return "me.guichaguri.tickratechanger.TickrateContainer";
	}

	@Override
	public String getSetupClass() {
		return "me.guichaguri.tickratechanger.TickrateChanger";
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
	@Override
	public Void call() throws Exception {
		return null;
	}
	
}
