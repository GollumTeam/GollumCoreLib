package com.gollum.core.common.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.ForgeSubscribe;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.achievement.GollumGuiStats;


public class GuiScreenHandler {

	@ForgeSubscribe
	public void onGuiOpen (GuiOpenEvent event) {
		
		if (ModGollumCoreLib.proxy.isRemote() && event.gui.getClass() == GuiStats.class) {
			
			Minecraft mc = Minecraft.getMinecraft();
			
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GollumGuiStats(event.gui, mc.statFileWriter));
			}
			event.setCanceled(true);
		}
		
	}
	
}
