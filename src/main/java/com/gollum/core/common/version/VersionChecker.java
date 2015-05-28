package com.gollum.core.common.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.log.Logger;
import com.gollum.core.common.mod.GollumMod;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class VersionChecker extends Thread {
	
	/**
	 * Affiche le message de mise à jour
	 */
	private static boolean display = true;
	
	private GollumMod mod = null;
	private String message = null;
	private String type = "";
	
	
	public class EnterWorldHandler implements ITickHandler {
		
		private boolean nagged = false;;
		
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) {
		}
		
		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) {
			
			if (nagged || !VersionChecker.display) {
				return;
			}
			if (message != null) {
				EntityPlayer player = (EntityPlayer) tickData[0];
				player.addChatMessage("-------------------------------");
				player.addChatMessage(EnumChatFormatting.YELLOW + message);
				player.addChatMessage("-------------------------------");
			}
			nagged = true;
		}

		@Override
		public EnumSet<TickType> ticks() {
			return EnumSet.of(TickType.PLAYER);
		}

		@Override
		public String getLabel() {
			return mod.getModId() + " - Player update tick";
		}
	}
	
	/**
	 * Recupère l'instance
	 * @param display Affiche ou non le message de version
	 * @return VersionChecker
	 */
	public static void setDisplay(boolean display) {
		VersionChecker.display = display;
	}
	
	public VersionChecker () {
		this.mod = ModContext.instance().getCurrent();
		TickRegistry.registerTickHandler(new EnterWorldHandler(), Side.CLIENT);
		start ();
	}
	
	public void run () {
		
		String player = "MINECRAFT_SERVER";
		if (ModGollumCoreLib.proxy.isRemote ()) {
			player = Minecraft.getMinecraft().getSession().getUsername();
		}
		
		try {
			
			String modid = mod.getModId ();
			String modidEnc = URLEncoder.encode(mod.getModId (), "UTF-8");
			String versionEnc = URLEncoder.encode(mod.getVersion (), "UTF-8");
			String playerEnc = URLEncoder.encode(player, "UTF-8");
			String mcVersionEnc = URLEncoder.encode(mod.getMinecraftVersion (), "UTF-8");
			
			URL url = new URL ("http://minecraft-mods.elewendyl.fr/index.php/mmods/default/version?mod="+modidEnc+"&version="+versionEnc+"&player="+playerEnc+"&mversion="+mcVersionEnc);
			ModGollumCoreLib.log.debug("URL Checker : "+url);
			
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
			String strJSON = bufferedreader.readLine();
			
			JdomParser parser = new JdomParser();
			JsonRootNode root = parser.parse(strJSON);
			
			try { message = root.getStringValue("message");  } catch (Exception exception) {}
			try { type    = root.getStringValue("type");     } catch (Exception exception) {}
			
			if (type.equals("info")) {
				Logger.log("VersionChecker "+modid, Logger.LEVEL_INFO, message);
			} else {
				Logger.log("VersionChecker "+modid, Logger.LEVEL_WARNING, message);
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
}
