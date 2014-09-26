package mods.gollum.core.tools.registered;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
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

	public HashMap<String, Block> getBlocksList() {
		HashMap<String, Block>list = new HashMap<String, Block>();
		
		for (Object o : Block.blockRegistry) {
			Block b = (Block)o;
			list.put (this.getRegisterName(b), b);
		}
		
		return list;
	}

	public HashMap<String, Item> getItemsList() {
		HashMap<String, Item>list = new HashMap<String, Item>();
		
		for (Object o : Item.itemRegistry) {
			Item i = (Item)o;
			list.put (this.getRegisterName(i), i);
		}
		
		return list;
	}
	
	public Block getBlock (String registerName) {
		
		try {
			
			String modId = registerName.substring(0, registerName.indexOf(":"));
			String name  = registerName.substring(registerName.indexOf(":")+1);
			
			if (modId.equals("minecraft")) {
				
				return (Block)Block.blockRegistry.getObject(name);
				
			} else {
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
