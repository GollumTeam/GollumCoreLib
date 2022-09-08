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
import net.minecraft.potion.PotionType;
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

	public TreeMap<String, SoundEvent> getSoundEventsList() {
		TreeMap<String, SoundEvent> sounds = new TreeMap<String, SoundEvent>();
		for (SoundEvent sound: SoundEvent.REGISTRY) {
			sounds.put(sound.getRegistryName().toString(), sound);
		}
		return sounds;
	}

	public TreeMap<String, PotionType> getPotionTypeList() {
		TreeMap<String, PotionType> potions = new TreeMap<String, PotionType>();
		for (PotionType potion: PotionType.REGISTRY) {
			potions.put(potion.getRegistryName().toString(), potion);
		}
		return potions;
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
		
		ModGollumCoreLib.logger.warning("Block not found : "+registerName);
		
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
		
		ModGollumCoreLib.logger.warning("Item not found : "+registerName);
		
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
		
		ModGollumCoreLib.logger.warning("Biome not found : "+registerName);
		
		return null;
	}

	public SoundEvent getSoundEvent(String registerName) {
		try {
			if (!registerName.contains(":")) {
				registerName = "minecraft:" + registerName;
			}
			for (ResourceLocation key: SoundEvent.REGISTRY.getKeys()) {
				if (key.toString().equals(registerName)) {
					return SoundEvent.REGISTRY.getObject(key);
				}
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.logger.warning("SoundEvent not found : "+registerName);
		
		return null;
	}

	public PotionType getPotionType(String registerName) {
		try {
			if (!registerName.contains(":")) {
				registerName = "minecraft:" + registerName;
			}
			for (ResourceLocation key: PotionType.REGISTRY.getKeys()) {
				if (key.toString().equals(registerName)) {
					return PotionType.REGISTRY.getObject(key);
				}
			}
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.logger.warning("PotionType not found : "+registerName);
		
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
	
	public String getRegisterName (PotionType potion) {
		return potion.getRegistryName().toString();
	}

}
