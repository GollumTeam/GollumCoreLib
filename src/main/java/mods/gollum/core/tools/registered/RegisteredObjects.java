package mods.gollum.core.tools.registered;

import java.util.Hashtable;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisteredObjects {
	
	private static RegisteredObjects instance = null;
	
	public static RegisteredObjects instance () {
		
		if (instance == null) {
			instance = new RegisteredObjects();
		}
		
		return instance;
	}
	
	public Block getBlock (String registerName) {
		
		try {
			
			String modId = registerName.substring(0, registerName.indexOf(":"));
			String name  = registerName.substring(registerName.indexOf(":")+1);
			
			if (modId.equals("minecraft")) {
				
				return (Block)Block.blockRegistry.getObject(name);
				
			} else {
				// TODO A Analyser
				return (Block)Block.blockRegistry.getObject(registerName);
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Block not found : "+registerName);
		
		return null;
	}
	
	public Item getItem (String registerName) {
		
		try {
			
			String modId = "minecraft";
			String name  = registerName;			
			if (registerName.contains(":")) {	
				modId = registerName.substring(0, registerName.indexOf(":"));
				name  = registerName.substring(registerName.indexOf(":")+1);
			}
			if (modId.equals("minecraft")) {
				return (Item)Item.itemRegistry.getObject(name);
				
			} else {
				return (Item)Item.itemRegistry.getObject(registerName);
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Item not found : "+registerName);
		
		return null;
	}
	
	public String getRegisterName (Block block) {
		for (Object key: Block.blockRegistry.getKeys()) {
			if (block == Block.blockRegistry.getObject(key)) {
				return (String) key;
			}
		}
		return null;
	}
	
	public String getRegisterName (Item item) {
		for (Object key: Item.itemRegistry.getKeys()) {
			if (item == Item.itemRegistry.getObject(key)) {
				return (String) key;
			}
		}
		return null;
	}
	
}
