package com.gollum.core.common.handlers;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.achievement.GollumGuiStats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class GuiScreenHandler {
	
	@SubscribeEvent
	public void onActionPerformedPre (ActionPerformedEvent.Pre event) {
		
		if (ModGollumCoreLib.proxy.isRemote() && event.getGui() instanceof GuiIngameMenu && event.getButton().id == 6) {
			
			Minecraft mc = Minecraft.getMinecraft();
			
			if (mc.player != null) {
				mc.displayGuiScreen(new GollumGuiStats(event.getGui(), mc.player.getStatFileWriter()));
			}
			event.setCanceled(true);
		}
		
	}
	
}
