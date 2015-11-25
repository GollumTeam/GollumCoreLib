package com.gollum.core.tools.registered;

import java.util.TreeMap;

import com.gollum.core.ModGollumCoreLib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisteredObjects {
	
	private static RegisteredObjects instance = null;
	
	public static RegisteredObjects instance () {
		
		if (instance == null) {
			instance = new RegisteredObjects();
		}
		
		return instance;
	}

	public TreeMap<String, Block> getBlocksList() {
		TreeMap<String, Block>list = new TreeMap<String, Block>();
		
		for (Object o : Block.blockRegistry) {
			Block b = (Block)o;
			list.put (this.getRegisterName(b), b);
		}
		
		return list;
	}

	public TreeMap<String, Item> getItemsList() {
		TreeMap<String, Item>list = new TreeMap<String, Item>();
		
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
			
			return GameRegistry.findBlock(modId, name);
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Block not found : "+registerName);
		
		return null;
	}
	
	public Item getItem (String registerName) {
		
		try {
			
			String modId = "minecraft";
			String name  = registerName;
			
			return GameRegistry.findItem(modId, name);
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Item not found : "+registerName);
		
		return null;
	}
	
	public String getRegisterName (Block block) {
		
		for (ResourceLocation key: Block.blockRegistry.getKeys()) {
			if (block == Block.blockRegistry.getObject(key)) {
				if (key != null) {
					return key.toString();
				}
			}
		}
		return null;
	}
	
	public String getRegisterName (Item item) {
		
		for (ResourceLocation key: Item.itemRegistry.getKeys()) {
			if (item == Item.itemRegistry.getObject(key)) {
				if (key != null) {
					return key.toString();
				}
			}
		}
		return null;
	}
	
	public BiomeGenBase[] getAllBiomes() {
		return BiomeGenBase.getBiomeGenArray();
	}
	
	public BiomeGenBase getBiome(String name) {
		for (BiomeGenBase biome: this.getAllBiomes()) {
			if (biome != null && biome.biomeName.equals(name)) {
				return biome;
			}
		}
		return null;
	}
	
}
