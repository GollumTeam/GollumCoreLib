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
//				ItemStack s  = GameRegistry.findItemStack(modId, name, 1);
//				return Block.blocksList[s.itemID];
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Block not found : "+registerName);
		
		return null;
	}
	
	public Item getItem (String registerName) {
		
		try {
			String modId = registerName.substring(0, registerName.indexOf(":"));
			String name  = registerName.substring(registerName.indexOf(":")+1);
			
			if (modId.equals("minecraft")) {
				
				return (Item)Item.itemRegistry.getObject(name);
				
			} else {
				// TODO A Analyser
				return (Item)Item.itemRegistry.getObject(registerName);
//				ItemStack s  = GameRegistry.findItemStack(modId, name, 1);
//				return s.getItem();
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Item not found : "+registerName);
		
		return null;
	}
	
	public String getRegisterName (Block block) {
		// TODO A Analyser
		for (Object key: Block.blockRegistry.getKeys()) {
			if (block == Item.itemRegistry.getObject(key)) {
				return (String) key;
			}
		}
//		if (this.vanillaBlocks.contains(block)) {
//			
//			for (Entry<String, Block> entry: this.vanillaBlocks.entrySet()) {
//				if (entry.getValue() == block) {
//					return entry.getKey();
//				}
//			}
//		}
		return null;
	}
	
	public String getRegisterName (Item item) {
		// TODO A Analyser
		for (Object key: Item.itemRegistry.getKeys()) {
			if (item == Item.itemRegistry.getObject(key)) {
				return (String) key;
			}
		}
//		if (this.vanillaItems.contains(item)) {
//			
//			for (Entry<String, Item> entry: this.vanillaItems.entrySet()) {
//				if (entry.getValue() == item) {
//					return entry.getKey();
//				}
//			}
//		}
		return null;
	}
	
}
