package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.log;

import com.gollum.castledefenders.client.gui.achievement.GollumGuiStats;
import com.gollum.core.ModGollumCoreLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class GuiScreenHandler {
	
	@SubscribeEvent
	public void onActionPerformedPre (ActionPerformedEvent.Pre event) {
		
		if (ModGollumCoreLib.proxy.isRemote() && event.gui instanceof GuiIngameMenu && event.button.id == 6) {
			
			Minecraft mc = Minecraft.getMinecraft();
			
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GollumGuiStats(event.gui, mc.thePlayer.getStatFileWriter()));
			}
			event.setCanceled(true);
		}
		
	}
	
}
