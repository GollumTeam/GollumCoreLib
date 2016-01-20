package com.gollum.core.tools.registry;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockRegistry {

	private static BlockRegistry instance = new BlockRegistry();
	
	private HashMap<GollumMod, ArrayList<IBlockHelper>> blocks = new HashMap<GollumMod, ArrayList<IBlockHelper>>();
	
	public static BlockRegistry instance () {
		return instance;
	}
	
	public void add (IBlockHelper block) {
		GollumMod mod = ModContext.instance().getCurrent();
		if (!this.blocks.containsKey(mod)) {
			this.blocks.put(mod, new ArrayList<IBlockHelper>());
		}
		ArrayList<IBlockHelper> blocks = this.blocks.get(mod);
		if (!blocks.contains(block)) {
			blocks.add(block);
		}
	}
	
	public void registerAll () {
		GollumMod mod = ModContext.instance().getCurrent();
		if (this.blocks.containsKey(mod)) {
			ArrayList<IBlockHelper> blocks = this.blocks.get(mod);
			for (IBlockHelper block : blocks) {
				block.register();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerRenders() {
		GollumMod mod = ModContext.instance().getCurrent();
		if (this.blocks.containsKey(mod)) {
			ArrayList<IBlockHelper> blocks = this.blocks.get(mod);
			for (IBlockHelper block : blocks) {
				block.registerRender();
			}
		}
	}
	
	public void overrideBlocksClassField (Block vanillaBlock, Block block) {
		
		try {
			
			Field field = null;
			
			for (Field f : Blocks.class.getFields()) {
				f.setAccessible(true);
				if (Modifier.isStatic(f.getModifiers()) && f.get(null) == vanillaBlock) {
					field = f;
				}
			}
			if (field == null) {
				throw new Exception("This block \""+vanillaBlock.getClass().getName()+"\" not found in fields "+Blocks.class.getName());
			}
			
			this.overrideBlocksClassField(field.getName(), block);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideBlocksClassField (String fieldName, Block block) {
		try {
			Reflection.setFinalStatic(Blocks.class.getDeclaredField(fieldName), block);
			log.message("Override Blocks field fieldName=\""+fieldName+"\" by "+block.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void overrideRegistered (String registerName, Block block) {
		
		try {
			
			Block vanillaBlock = RegisteredObjects.instance().getBlock(registerName);
			
			if (vanillaBlock != null) {
				
				log.message("Override registery block \""+registerName+"\" by "+block.getClass().getSimpleName());
				
				FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
				Field f1 = null;
				try {
					f1 = RegistrySimple.class.getDeclaredField("field_82596_a");
				} catch (Exception e) {
					log.message("Unofuscate property RegistrySimple : field_82596_a => registryObjects");
					f1 = RegistrySimple.class.getDeclaredField("registryObjects");
				}
				f1.setAccessible(true);
				Map registryObjects = (Map)f1.get(blockRegistry);
				registryObjects.put(registerName, block);
				log.debug (" 1 - Replace \""+registerName+"\" registery : registryObjects");
				
				
				Field f2 = null;
				try {
					f2 = RegistryNamespaced.class.getDeclaredField("field_148759_a");
				} catch (Exception e) {
					log.message("Unofuscate property RegistrySimple : field_148759_a => underlyingIntegerMap");
					f2 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
				}
				f2.setAccessible(true);
				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f2.get(blockRegistry);
				
				int id = underlyingIntegerMap.get(vanillaBlock);
				if (id == -1) {
					throw new Exception(" 2 - Replace \""+registerName+"\" registery is KO because id not found");
				}
				underlyingIntegerMap.put(block, id);
				log.debug (" 2 - Replace \""+registerName+"\" registery : underlyingIntegerMap id="+id);
				
			} else {
				log.severe("The original block \""+registerName+"\" not found for replace registery.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
