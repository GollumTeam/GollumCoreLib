package mods.gollum.core.tools.registry;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import mods.gollum.core.tools.helper.IBlockHelper;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.core.utils.reflection.Reflection;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;


public class BlockRegistry {

	private static BlockRegistry instance = new BlockRegistry();
	
	private ArrayList<IBlockHelper> blocks = new ArrayList<IBlockHelper>();
	
	public static BlockRegistry instance () {
		return instance;
	}
	
	public void add (IBlockHelper block) {
		if (!this.blocks.contains(block)) {
			this.blocks.add(block);
		}
	}
	
	public void registerAll () {
		for (IBlockHelper block : this.blocks) {
			block.register();
		}
		this.blocks.clear();
	}
	
	public void overrideRegistered (String registerName, Block block) {
		
		try {
			
			Block vanillaBlock = RegisteredObjects.instance().getBlock(registerName);
			
			Reflection.setFinalStatic(Blocks.class.getDeclaredField("piston"), block);
			
			FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
			Field f = RegistrySimple.class.getDeclaredField("registryObjects");
			f.setAccessible(true);
			Map registryObjects = (Map)f.get(blockRegistry);
			registryObjects.put("minecraft:piston", block);
			

//			Reflection.setFinalStatic(Blocks.class.getDeclaredField("piston"), proxy);
			
//			FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
//			Field f = RegistrySimple.class.getDeclaredField("registryObjects");
//			f.setAccessible(true);
//			Map registryObjects = (Map)f.get(blockRegistry);
//			registryObjects.remove("minecraft:piston");
//			registryObjects.put("minecraft:piston", proxy);
//			log.debug ("Replace minecraft:piston registery : registryObjects");
//
//			Field f2 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
//			f2.setAccessible(true);
//			ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f2.get(blockRegistry);
//			underlyingIntegerMap.func_148746_a(proxy, 33);
			
//			Field f2 = RegistryNamespaced.class.getDeclaredField("field_148758_b");
//			f2.setAccessible(true);
//			HashBiMap field_148758_b = (HashBiMap)f.get(blockRegistry);
//			field_148758_b.remove("minecraft:piston");
//			field_148758_b.put("minecraft:piston", proxy);
//			log.debug ("Replace minecraft:piston registery : field_148758_b");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
