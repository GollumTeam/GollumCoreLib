package mods.gollum.core.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import mods.gollum.core.mod.ModMetaInfos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class VersionChecker extends Thread {
	
	
	/**
	 * Affiche le message de mise à jour
	 */
	private static boolean display = true;
	
	
	public class EnterWorldHandler {
		
		private boolean nagged = false;;
		
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void OnPlayerTickEvent(EntityJoinWorldEvent event) {
			
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			if (
				player == null ||
				nagged ||
				!VersionChecker.display
			) {
				return;
			}
			
			if (message != null) {
				
				String chat = "";
				for (int i = 0; i < message.length(); i++) {
					chat = chat + EnumChatFormatting.YELLOW + message.charAt(i);
				}

				player.addChatMessage(new ChatComponentText("-------------------------------"));
				player.addChatMessage(new ChatComponentText(chat));
				player.addChatMessage(new ChatComponentText("-------------------------------"));
				nagged = true;
			}
		}
		
	}
	
	private String message = null;
	private String type = "";
	private ModMetaInfos mod = null;
	
	/**
	 * Recupère l'instance
	 * @param display Affiche ou non le message de version
	 * @return VersionChecker
	 */
	public static void setDisplay(boolean display) {
		VersionChecker.display = display;
	}
	
	public VersionChecker (Object mod) {
		this.mod = new ModMetaInfos (mod);
		MinecraftForge.EVENT_BUS.register(new EnterWorldHandler ());
		start ();
	}
	
	public void run () {
		
		String player = "MINECRAFT_SERVER";
		if (MinecraftServer.getServer() == null) {
			player = Minecraft.getMinecraft().getSession().getUsername();
		}
		
		try {
			
			String modid = mod.getModid ();
			String modidEnc = URLEncoder.encode(mod.getModid (), "UTF-8");
			String versionEnc = URLEncoder.encode(mod.getVersion (), "UTF-8");
			String playerEnc = URLEncoder.encode(player, "UTF-8");
			String mcVersionEnc = URLEncoder.encode(mod.getMinecraftVersion (), "UTF-8");
			
			URL url = new URL ("http://minecraft-mods.elewendyl.fr/index.php/mmods/default/version?mod="+modidEnc+"&version="+versionEnc+"&player="+playerEnc+"&mversion="+mcVersionEnc);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
			String strJSON = bufferedreader.readLine();

			JdomParser parser = new JdomParser();   
			JsonRootNode root = parser.parse(strJSON);

			try { message = root.getStringValue("message");  } catch (Exception exception) {}
			try { type    = root.getStringValue("type");     } catch (Exception exception) {}
			
			Calendar calendar = Calendar.getInstance();
			String h =  String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
			String m =  String.format("%02d", calendar.get(Calendar.MINUTE));
			String s =  String.format("%02d", calendar.get(Calendar.SECOND));
			
			String log = "["+h+":"+m+":"+s+"] ["+(type.equals("info") ? "INFO" : "WARNING" )+"] ["+modid+"]: "+ message;
						
			if (type.equals("info")) {
				System.out.println (log);
			} else {
				System.err.println (log);
			}
			
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
}
