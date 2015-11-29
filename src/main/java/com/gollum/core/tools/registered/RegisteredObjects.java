package com.gollum.core.tools.registered;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	
	@SideOnly(Side.CLIENT)
	public TreeMap<SoundCategory, TreeSet<String>> getAllSound() {
		TreeMap<SoundCategory, TreeSet<String>> sounds = new TreeMap<SoundCategory, TreeSet<String>>();
		
		try {
			SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
			SoundRegistry soundRegistry = (SoundRegistry) Reflection.getFirstValueByFieldType(soundHandler, SoundRegistry.class);
			
			for (ResourceLocation key: soundRegistry.getKeys()) {
				SoundEventAccessorComposite accessor = soundRegistry.getObject(key);
				SoundCategory category = accessor.getSoundCategory();
				if (!sounds.containsKey(category)) {
					sounds.put(category, new TreeSet<String>());
				}
				String domain = accessor.getSoundEventLocation().getResourceDomain();
				String path = accessor.getSoundEventLocation().getResourcePath();
				sounds.get(category).add((domain.equals("minecraft") ? "" : domain+":")+path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sounds;
	}
	
	@SideOnly(Side.CLIENT)
	public SoundCategory getSoundCategoryBySound(String sound) {
		for (Entry<SoundCategory, TreeSet<String>> entry: this.getAllSound().entrySet()) {
			if (((TreeSet<String>)entry.getValue()).contains(sound)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
}
