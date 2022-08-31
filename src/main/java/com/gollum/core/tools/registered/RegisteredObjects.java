package com.gollum.core.tools.registered;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
//import net.minecraft.client.audio.SoundCategory;
//import net.minecraft.client.audio.SoundEventAccessorComposite;
//import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

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
		
		for (Block b : Block.REGISTRY) {
			list.put (this.getRegisterName(b), b);
		}
		
		return list;
	}

	public TreeMap<String, Item> getItemsList() {
		TreeMap<String, Item>list = new TreeMap<String, Item>();
		
		for (Item i : Item.REGISTRY) {
			list.put (this.getRegisterName(i), i);
		}
		
		return list;
	}
	
	public TreeMap<String, Biome> getBiomesList() {
		TreeMap<String, Biome> biomes = new TreeMap<String, Biome>();
		for (Biome biome: Biome.REGISTRY) {
			biomes.put(biome.getRegistryName().toString(), biome);
		}
		return biomes;
	}
	
	public Block getBlock (String registerName) {
		
		try {
			if (!registerName.contains(":")) {
				registerName = "minecraft:" + registerName;
			}
			for (ResourceLocation key: Block.REGISTRY.getKeys()) {
				if (key.toString().equals(registerName)) {
					return Block.REGISTRY.getObject(key);
				}
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Block not found : "+registerName);
		
		return null;
	}
	
	public Item getItem (String registerName) {
		
		try {
			if (!registerName.contains(":")) {
				registerName = "minecraft:" + registerName;
			}
			for (ResourceLocation key: Item.REGISTRY.getKeys()) {
				if (key.toString().equals(registerName)) {
					return Item.REGISTRY.getObject(key);
				}
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Item not found : "+registerName);
		
		return null;
	}
	
	public Biome getBiome(String registerName) {
		try {
			if (!registerName.contains(":")) {
				registerName = "minecraft:" + registerName;
			}
			for (ResourceLocation key: Biome.REGISTRY.getKeys()) {
				if (key.toString().equals(registerName)) {
					return Biome.REGISTRY.getObject(key);
				}
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Biome not found : "+registerName);
		
		return null;
	}
	
	public String getRegisterName (Block block) {
		return block.getRegistryName().toString();
	}
	
	public String getRegisterName (Item item) {
		return item.getRegistryName().toString();
	}
	
	public String getRegisterName (Biome biome) {
		return biome.getRegistryName().toString();
	}
	
	public String getRegisterName (SoundEvent sound) {
		return sound.getRegistryName().toString();
	}

	
	public TreeSet<String> getAllSound() {
		TreeSet<String> sounds = new TreeSet<String>();
		
		for (ResourceLocation key: SoundEvent.REGISTRY.getKeys()) {
			SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(key);
			sounds.add(key.toString());
		}
		return sounds;
	}
	
	public TreeMap<SoundCategory, TreeSet<String>> getAllSoundByCategories() {
		TreeMap<SoundCategory, TreeSet<String>> sounds = new TreeMap<SoundCategory, TreeSet<String>>();
		
		for (ResourceLocation key: SoundEvent.REGISTRY.getKeys()) {
			SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(key);
			SoundCategory category = SoundCategory.getByName(key.toString());
			if (!sounds.containsKey(category)) {
				sounds.put(category, new TreeSet<String>());
			}
			sounds.get(category).add(key.toString());
		}
		return sounds;
	}
	
	public SoundCategory getSoundCategoryBySound(String sound) {
		try {
			SoundCategory category = SoundCategory.getByName(sound);
			if (category != null) {
				return category;
			}
		} catch (Exception e) {
		}
		ModGollumCoreLib.log.warning("Sound not found : "+sound);
		
		return null;
	}
	
	public SoundCategory getSoundCategoryBySound(SoundEvent sound) {
		return this.getSoundCategoryBySound(sound.getRegistryName().toString());
	}
	
	public SoundEvent getSoundEvent(String sound) {

		for (ResourceLocation key: SoundEvent.REGISTRY.getKeys()) {
			if (key.toString().equals(sound)) {
				return SoundEvent.REGISTRY.getObject(key);
			}
		}
		
		ModGollumCoreLib.log.warning("Sound not found : "+sound);
		return null;
	}
	
}
