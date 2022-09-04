package com.gollum.core.client.gui.config;

import java.util.Set;

import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ConfigModGuiFactory implements IModGuiFactory {
	
	private Minecraft mc;
	
	@Override
	public void initialize(Minecraft minecraftInstance) {
		mc = minecraftInstance;
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiModConfig(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

//	@Override
//	public Class<? extends GuiScreen> mainConfigGuiClass() {
//		return GuiModConfig.class;
//	}

//	@Override
//	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
//		return null;
//	}

//	@Override
//	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
//		return null;
//	}

}