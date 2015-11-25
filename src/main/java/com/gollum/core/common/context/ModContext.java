package com.gollum.core.common.context;

import com.gollum.core.common.mod.GollumMod;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ModContext {
	
	private static ModContext instance = new ModContext();
	
	private GollumMod current;
	
	public static ModContext instance () {
		return instance;
	}
	
	public GollumMod getCurrent() {
		if (this.current == null) {
			try {
				throw new Exception("You should get a current mod by ModContext, but the context is not init. Your mod must extend of GollumMod. after in begin of preInit() and in end of postInit() you must call a super method ");
			} catch (Exception e) {
				e.printStackTrace();
				FMLCommonHandler.instance().exitJava(-1, true);
			}
		}
		return this.current;
	}
	
	public ModContext setCurrent(GollumMod current) {
		if (current == null) {
			try {
				throw new Exception("GollumMod.setCurrent() can't be null. use pop ()");
			} catch (Exception e) {
				e.printStackTrace();
				FMLCommonHandler.instance().exitJava(-1, true);
			}
		}
		this.current = current;
		return this;
	}
	
	public GollumMod pop() {
		GollumMod current = this.current;
		this.current = null;
		return current;
	}
}
