package com.gollum.core.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

// Go see https://github.com/Guichaguri/TickrateChanger
@IFMLLoadingPlugin.MCVersion("1.8.8")
@TransformerExclusions({"com.gollum.core.asm"})
public class GollumCoreLibPlugin implements IFMLLoadingPlugin /*, IFMLCallHook */ {
	
	public GollumCoreLibPlugin() {
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
			GollumCoreLibTransformer.class.getCanonicalName()
		};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
