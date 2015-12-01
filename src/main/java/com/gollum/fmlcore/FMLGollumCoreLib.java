package com.gollum.fmlcore;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

// Go see https://github.com/Guichaguri/TickrateChanger

@TransformerExclusions({"com.gollum.fmlcore"})
public class FMLGollumCoreLib implements IFMLLoadingPlugin, IFMLCallHook {

	public final static String MODID = "FMLGollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib CoreMod";
	public final static String VERSION = "1.0.0";
	public final static String MINECRAFT_VERSION = "1.8.8";
	
	private FMLGollumCoreLib instance;
	
	public FMLGollumCoreLib() {
		instance = this;
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{FMLGollumCoreLibTransformer.class.getCanonicalName()};
	}
	
	@Override
	public String getModContainerClass() {
		return FMLGollumCoreLibContainer.class.getCanonicalName();
	}

	@Override
	public String getSetupClass() {
		return FMLGollumCoreLib.class.getCanonicalName();
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
