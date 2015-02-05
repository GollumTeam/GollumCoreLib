package mods.gollum.core.tools.registry;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;

import mods.gollum.core.tools.helper.IBlockHelper;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.core.utils.reflection.Reflection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
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
			
			if (vanillaBlock != null) {
				
				log.message("Override registery block \""+registerName+"\" by "+block.getClass().getSimpleName());
				
				Field f1 = null;
				for (Field f : Blocks.class.getDeclaredFields()) {
					f.setAccessible(true);
					
					if (Modifier.isStatic(f.getModifiers()) && f.get(null) == vanillaBlock) {
						f1 = f;
						break;
					}
				}
				if (f1 == null) {
					throw new Exception(" 1 - Replace \""+registerName+"\" registery is KO because field not founs in minecraft.init.Blocks");
				}

//				Reflection.setFinalStatic(f1, vanillaBlock);
				Reflection.setFinalStatic(f1, block);
//				Reflection.setFinalStatic(Blocks.class.getDeclaredField("piston"), (BlockPistonBase)block);
				log.debug (" 1 - Replace \""+registerName+"\" registery : Blocks.");
				
				FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
				Field f2 = RegistrySimple.class.getDeclaredField("registryObjects");
				f2.setAccessible(true);
				Map registryObjects = (Map)f2.get(blockRegistry);
				registryObjects.put("minecraft:piston", block);
				log.debug (" 2 - Replace \""+registerName+"\" registery : registryObjects");
				

				Field f3 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
				f3.setAccessible(true);
				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f3.get(blockRegistry);
				
				int id = underlyingIntegerMap.func_148747_b(vanillaBlock);
				if (id == -1) {
					throw new Exception(" 3 - Replace \""+registerName+"\" registery is KO because id not found");
				}
				underlyingIntegerMap.func_148746_a(block, id);
				log.debug (" 3 - Replace \""+registerName+"\" registery : underlyingIntegerMap id="+id);
				
			} else {
				log.severe("The original block \""+registerName+"\" not found for replace registery.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
